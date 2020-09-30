package com.github.smallshen.miraibot


import com.github.smallshen.miraibot.plugin.BotPlugin
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.TempMessageEvent
import net.mamoe.mirai.message.data.FlashImage
import net.mamoe.mirai.message.data.queryUrl
import net.mamoe.mirai.network.LoginFailedException
import org.hydev.logger.HyLogger
import org.hydev.logger.HyLoggerConfig
import org.hydev.logger.appenders.ColorCompatibility
import org.hydev.logger.foreground
import org.hydev.logger.format.AnsiColor
import java.awt.Color
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

/**
 * @author xiaoshen
 */

object BotConsole {
    lateinit var bot: Bot
    val logger = HyLogger("控制台")
    val loadedPlugins = mutableListOf<BotPlugin>()


    suspend fun unloadAllPlugin() {
        loadedPlugins.forEach {
            it.onPluginDisable()
            it.logger.log("插件已关闭!")
        }

        loadedPlugins.clear()
        logger.log("全部关闭")
    }
}


suspend fun main(args: Array<String>) {
    loadInConsole()
}


suspend fun loadInConsole() {
    HyLoggerConfig.colorCompatibility = ColorCompatibility.DISABLED
    val logger: HyLogger = BotConsole.logger
    val configFile = ConsoleConfig()
    if (!configFile.configFile.exists()) {
        logger.warning("没有检测到配置文件，创建中")

        with(configFile) {
            configFile.configFile.createNewFile()
            set("QQ.account", "机器人QQ")
            set("QQ.password", "机器人密码")
            save()
        }

        println("请前往 ${configFile.configFile.absolutePath} 填写配置文件")
        return
    }

    configFile.load()

    logger.log("当前账号: ${configFile.getLong("QQ.account")}")

    BotConsole.bot = Bot(configFile.getLong("QQ.account"), configFile.getString("QQ.password")) {
        fileBasedDeviceInfo("${configFile.getString("QQ.account")}.json")
        noBotLog()
        noNetworkLog()
        loginSolver = CustomLoginSolver()
    }


    try {
        BotConsole.bot.login()
    } catch (e: LoginFailedException) {
        logger.warning("登录失败，请检查密码")
        return
    }
    logger.log("登录成功")

    logger.log("当前昵称: ${BotConsole.bot.nick}")
    logger.log("好友: ${BotConsole.bot.friends.size}")
    logger.log("群聊: ${BotConsole.bot.groups.size}")


    val quickImageFolder = File("FlashImages")
    if (!quickImageFolder.exists()) {
        quickImageFolder.mkdir()
    }


    logger.log("开始加载插件")
    val pluginFolder = File("plugins")
    if (!pluginFolder.exists()) {
        logger.warning("没有检测到 plugins 文件夹, 创建中... ")
        pluginFolder.mkdir()
    }

    val pluginFiles = getPluginJars()

    val plugins = mutableListOf<BotPlugin>()

    pluginFiles.forEach {
        try {
            plugins += getPlugin(it)
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> {
                    logger.warning("Jar文件 ${it.absolutePath} 没有在 MANIFEST.MF 中设置 Plugin-Class, 请调整")
                }

                is ClassNotFoundException -> {
                    logger.warning("Jar文件 ${it.absolutePath} 找不到在 MANIFEST.MF 中设置 Plugin-Class, 请调整")
                }

                else -> {
                    logger.warning("无法正常加载 Jar文件 ${it.absolutePath}, 请检查是否为错误文件")
                }
            }
        }
    }

    plugins.forEach {
        if (it.defaultConfig != null) {
            it.defaultConfig.configFile = File(it.pluginDir.absolutePath + "/" + it.defaultConfig.filePath)
            if (!it.pluginDir.exists()) {
                it.pluginDir.mkdir()
            }

            if (!it.defaultConfig.configFile.exists()) {
                it.defaultConfig.configFile.createNewFile()
                it.defaultConfig.onInit()
                it.defaultConfig.save()
            }

            it.defaultConfig.load()
        }
    }

    plugins.forEach {
        it.logger = HyLogger("${Color(0, 255, 0).foreground()}${it.pluginName}")
    }



    plugins.forEach {
        it.bot = BotConsole.bot
        it.onPluginStart()
        BotConsole.loadedPlugins += it
        it.logger.log("插件已启动!")
    }

    logger.log("插件加载完成")

    basicEvents(bot = BotConsole.bot)


    startConsoleListener()


    logger.warning("退出机器人...")
}

fun getPluginJars(): List<File> {
    val jars: MutableList<File> = mutableListOf()
    val f = File("plugins")
    f.listFiles()!!.forEach {
        if (it.isFile) {
            if (it.name.endsWith(".jar")) {
                jars += it
            }
        }
    }

    return jars
}

fun getPlugin(file: File): BotPlugin {
    val fileUrl = file.toURI().toURL()
    val jarURL = "jar:$fileUrl!/"
    val urls = arrayOf(URL(jarURL))
    val ucl = URLClassLoader(urls)
    return (Class.forName(JarFile(file).manifest.mainAttributes.getValue("Plugin-Class")!!, true, ucl)
        .newInstance() as BotPlugin)
}

fun startConsoleListener() {
    while (true) {
        val string = readLine()
        if (string != null) {
            when (string) {
                "stop" -> {
                    runBlocking {
                        BotConsole.unloadAllPlugin()
                    }
                    break
                }
            }
        }
    }
    BotConsole.logger.log("登出QQ...")
    BotConsole.bot.close()
}

fun basicEvents(bot: Bot) {
    bot.subscribeAlways<FriendMessageEvent> {
        HyLogger("${AnsiColor.GREEN}好友消息")
            .log("<${sender.id}> [${sender.nick}] ${message.contentToString()}")
    }

    bot.subscribeAlways<GroupMessageEvent> {
        HyLogger("${AnsiColor.YELLOW}群组消息")
            .log("<${group.id}> [${sender.nameCardOrNick}] ${message.contentToString()}")
    }

    bot.subscribeAlways<TempMessageEvent> {
        HyLogger("${AnsiColor.CYAN}临时消息")
            .log("<${sender.id}> [${sender.nameCardOrNick}] ${message.contentToString()}")
    }

    bot.subscribeAlways<MemberJoinEvent> {
        HyLogger("${AnsiColor.YELLOW}群组消息").log("用户${this.member.id} 加入了群组")
    }

    bot.subscribeAlways<MemberLeaveEvent> {
        HyLogger("${AnsiColor.YELLOW}群组消息").log("用户${this.member.id} 离开了群组")
    }

    bot.subscribeAlways<TempMessagePostSendEvent> {
        HyLogger("${AnsiColor.CYAN}发送临时消息")
            .log("<${this.target.id}> [${this.target.nameCard}] ${message.contentToString()}")
    }

    bot.subscribeAlways<FriendMessagePostSendEvent> {
        HyLogger("${AnsiColor.CYAN}发送好友消息")
            .log("<${this.target.id}> [${this.target.nick}] ${message.contentToString()}")
    }

    bot.subscribeAlways<GroupMessagePostSendEvent> {
        HyLogger("${AnsiColor.CYAN}发送群组消息")
            .log("<${this.target.id}> [${bot.nick}] ${message.contentToString()}")
    }

    bot.subscribeAlways<MessageEvent> {
        val logger = HyLogger("闪照提醒")
        this.message.forEach {
            if (it is FlashImage) {
                logger.warning("发现闪照, 地址 ${it.image.queryUrl()}")
                File("FlashImages/${it.image.imageId}.jpg").apply {
                    createNewFile()
                    writeBytes(URL(it.image.queryUrl()).readBytes())
                }
                logger.log("下载 ${it.image.queryUrl()} 完成")
            }
        }
    }

}