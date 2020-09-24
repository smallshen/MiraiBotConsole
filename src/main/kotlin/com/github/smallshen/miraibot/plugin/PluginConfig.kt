package com.github.smallshen.miraibot.plugin

import cc.moecraft.yaml.HyConfig
import java.io.File

open class PluginConfig(val filePath: String) :
    HyConfig(File(filePath), false, true) {
    open fun onInit() {

    }
}

