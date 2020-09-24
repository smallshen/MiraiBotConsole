package com.github.smallshen.miraibot

import cc.moecraft.yaml.HyConfig
import java.io.File

class ConsoleConfig : HyConfig(File("config.yml"),false,true) {
    override fun save(): Boolean {
        return try {
            save(configFile)
            true
        } catch (e: Exception) {
            false
        }
    }
}