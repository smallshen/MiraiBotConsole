package com.github.smallshen.miraibot.util.dsl

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.GroupMessageEvent

suspend operator fun Member.invoke(v: suspend Member.() -> Unit) {
    v.invoke(this@invoke)
}

suspend operator fun Group.invoke(v: suspend Group.() -> Unit) {
    v.invoke(this@invoke)
}

suspend operator fun GroupMessageEvent.invoke(v: suspend GroupMessageEvent.() -> Unit) {
    v.invoke(this)
}

var Member.mute: Int
    get() {
        return this.muteTimeRemaining
    }
    set(value) {
        runBlocking {
            this@mute.mute(value)
        }
    }
