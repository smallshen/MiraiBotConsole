package com.github.smallshen.miraibot.console

import com.github.smallshen.miraibot.script.loadScripts
import com.github.smallshen.miraibot.script.loadedScripts
import org.hydev.logger.HyLogger

fun registerDefaultConsoleCommand() {
    val loggerScripts = HyLogger("重载脚本")
    ConsoleCommandDSL("scripts").apply {
        executor {
            when(args[0]) {
                "reload" -> {
                    loadedScripts.forEach {
                        it.commands.forEach { c ->
                            c.state = false
                            c.remove()
                        }
                    }

//                  Collection#clear removeAll 好像还会存在内存里头, 总之重新 reload console 是最好的awa
                    loadedScripts.clear()
                    loadScripts(logger = loggerScripts)
                    loggerScripts.log("重载完成")
                }
            }
        }
    }.register()
}