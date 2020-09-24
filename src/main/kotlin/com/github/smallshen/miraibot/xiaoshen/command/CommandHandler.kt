package com.github.smallshen.miraibot.xiaoshen.command

import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.MessageChain

interface CommandHandler {
    /**
     * [e] 当前事件，没有任何更改
     * [args] 解析过的消息串
     * @see MessageChain.parseCommand
     */
    suspend fun onCommand(e: MessageEvent, args: MessageChain)
}