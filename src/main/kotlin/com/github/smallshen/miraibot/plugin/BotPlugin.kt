package com.github.smallshen.miraibot.plugin

import net.mamoe.mirai.Bot
import org.hydev.logger.HyLogger
import java.io.File

abstract class BotPlugin(
    val pluginName: String,
    val pluginVersion: String,
    val pluginAuthor: List<String>? = null,
    val defaultConfig: PluginConfig? = null,
    val acceptBots: (Bot) -> (Boolean) = { true }
) {


    lateinit var bot: Bot

    lateinit var logger: HyLogger


    val pluginDir: File
        get() = File("plugins/${this.pluginName}")


    open suspend fun onPluginStart() {

    }

    open suspend fun onPluginDisable() {

    }

    fun getConfig(fileName: String): PluginConfig {
        return PluginConfig("${pluginDir.absolutePath}/${fileName}").apply {
            this.load()
        }
    }


}