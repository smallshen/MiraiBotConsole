package com.github.smallshen.miraibot.script

import io.xiaoshen.commandbuilder.command.dsl.PrefixCommandScope
import org.hydev.logger.HyLogger
import java.io.File
import javax.script.ScriptEngineManager


val loadedScripts = mutableListOf<PrefixCommandScope>()
val scriptLogger = HyLogger("脚本加载器")
val engine = ScriptEngineManager().getEngineByExtension("kts")

fun loadScripts() {
    File("scripts").apply { if (!exists()) mkdir() }
        .listFiles()!!
        .filter { it.canonicalPath.endsWith(".kts") }
        .forEach {
            loadedScripts.add(engine.eval(it.readText()) as PrefixCommandScope)
            scriptLogger.log("成功加载脚本 ${it.canonicalPath}")
        }
}

fun reloadScripts() {
    loadedScripts.forEach { it ->
        it.commands.forEach {
            it.state = false
            it.remove()
        }
    }
    loadedScripts.clear()
    loadScripts()
}
