package com.github.smallshen.miraibot.util

object SystemUtil {
    fun isWindows(): Boolean {
        return System.getProperty("os.name") == "Windows"
    }

    fun isUnix() : Boolean {
        return !isWindows()
    }
}