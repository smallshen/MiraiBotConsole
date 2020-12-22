package com.github.smallshen.miraibot.console

import com.github.smallshen.miraibot.script.loadScripts
import com.github.smallshen.miraibot.script.loadedScripts
import com.github.smallshen.miraibot.script.reloadScripts
import org.hydev.logger.HyLogger

fun registerDefaultConsoleCommand() {
    val loggerScripts = HyLogger("重载脚本")
    ConsoleCommandDSL("scripts").apply {
        executor {
            when(args[0]) {
                "reload" -> {
                    reloadScripts()
                }
            }
        }
    }.register()
}