package com.github.smallshen.miraibot

import org.hydev.logger.HyLogger
import java.io.File
import java.net.URL


fun loadLibs(logger: HyLogger) {
    logger.log("开始加载外置依赖")
    File("libs").apply { if (!exists()) mkdir() }
        .listFiles()!!
        .filter { it.canonicalPath.endsWith(".jar") }
        .forEach(::addJarToClasspath)
    logger.log("外置依赖加载完成")
}


fun addJarToClasspath(jar: File) {
    // Get the ClassLoader class
    val cl = ClassLoader.getSystemClassLoader()
    val clazz: Class<*> = cl.javaClass

    // Get the protected addURL method from the parent URLClassLoader class
    val method = clazz.superclass.getDeclaredMethod("addURL", *arrayOf(URL::class.java))

    // Run projected addURL method to add JAR to classpath
    method.isAccessible = true
    method.invoke(cl, arrayOf<Any>(jar.toURI().toURL()))
}