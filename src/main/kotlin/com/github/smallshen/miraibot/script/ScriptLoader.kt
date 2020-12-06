package com.github.smallshen.miraibot.script

import com.github.smallshen.miraibot.loadLibs
import de.swirtz.ktsrunner.objectloader.KtsObjectLoader
import de.swirtz.ktsrunner.objectloader.LoadException
import io.xiaoshen.commandbuilder.command.dsl.PrefixCommandScope
import org.hydev.logger.HyLogger
import java.io.File

val loadedScripts = mutableListOf<PrefixCommandScope>()

fun loadScripts(logger: HyLogger) {
    val loader = KtsObjectLoader()
    File("scripts").apply { if (!exists()) mkdir() }
        .listFiles()!!
        .filter { it.canonicalPath.endsWith(".kts") }
        .forEach {
            println(it.readText())
            logger.log("¿ªÊ¼¼ÓÔØ ${it.canonicalPath}")
            loadedScripts.add(loader.load(it.readText()))
        }
}

fun KtsObjectLoader.loadNoCast(script: String) {
    kotlin.runCatching { engine.eval(script) }
        .getOrElse { throw LoadException("Cannot load script", it) }
}