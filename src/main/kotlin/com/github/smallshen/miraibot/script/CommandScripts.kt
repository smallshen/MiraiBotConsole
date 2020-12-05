package com.github.smallshen.miraibot.script

import com.github.smallshen.miraibot.loadLibs
import de.swirtz.ktsrunner.objectloader.KtsObjectLoader
import de.swirtz.ktsrunner.objectloader.LoadException
import org.hydev.logger.HyLogger
import java.io.File


fun loadScripts(logger: HyLogger) {
    loadLibs(logger)
    val loader = KtsObjectLoader()
    File("scripts").apply { if (!exists()) mkdir() }
        .listFiles()!!
        .filter { it.canonicalPath.endsWith(".kts") }
        .forEach {
            logger.log("¿ªÊ¼¼ÓÔØ ${it.canonicalPath}")
            loader.loadNoCast(it.readText())
        }
}

fun KtsObjectLoader.loadNoCast(script: String) {
    kotlin.runCatching { engine.eval(script) }
        .getOrElse { throw LoadException("Cannot load script", it) }
}