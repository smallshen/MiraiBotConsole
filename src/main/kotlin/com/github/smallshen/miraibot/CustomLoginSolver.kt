package com.github.smallshen.miraibot

import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.LoginSolver
import org.hydev.logger.HyLogger
import org.hydev.logger.coloring.GradientPresets
import org.hydev.logger.format.AnsiColor
import java.io.File
import java.nio.ByteBuffer


/**
 * 借鉴了 https://github.com/mamoe/mirai-console/blob/reborn/frontend/mirai-console-terminal/src/main/kotlin/net/mamoe/mirai/console/MiraiConsoleTerminalFrontEnd.kt好神奇唉w
 **/

class CustomLoginSolver : LoginSolver() {
    private val logger: HyLogger = HyLogger("${AnsiColor.RED}登录指示器")
    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        val tempFile: File = createTempFile(suffix = ".png").apply { deleteOnExit() }
        withContext(Dispatchers.IO) {
            tempFile.createNewFile()
            logger.warning("需要图片验证码登录, 验证码为 4 字母")
            try {
                tempFile.writeChannel().apply {
                    writeFully(ByteBuffer.wrap(data))
                    close()
                }
                logger.warning("请查看文件 ${tempFile.absolutePath}")
            } catch (e: Exception) {
                logger.warning("验证码无法保存")
            }

        }
        print("请输入验证码> ")
        val variableName: String = readLine()!!
        return variableName.replace("\n", "").takeUnless { it.length != 4 || it.isEmpty() }.also {
            pushLog("提交中。。。")
        }

    }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
        logger.fancy.gradient("############## 需要滑动验证码 ##############", GradientPresets.BPR)
        // pushLog("需要滑动验证码")
        pushLog("请在任意浏览器中打开以下链接并完成验证码. ")
        print("完成后请按回车> ")
        pushLog(url)
        return readLine()!!.also {
            pushLog("验证中。。。")
        }
    }

    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
        pushLog("需要进行账户安全认证")
        pushLog("该账户有[设备锁]/[不常用登录地点]/[不常用设备登录]的问题")
        pushLog("完成以下账号认证即可成功登录|理论本认证在 Mirai 每个账户中最多出现1次")
        pushLog("请将该链接在浏览器中打开并完成认证, 验证完成后回车")
        pushLog("这步操作将在后续的版本中优化")
        pushLog("链接: $url")
        print("完成后请按回车> ")
        return readLine().also {
            pushLog("提交中。。。")
        }
    }

    private fun pushLog(s: String) {
        logger.log(s)
    }
}