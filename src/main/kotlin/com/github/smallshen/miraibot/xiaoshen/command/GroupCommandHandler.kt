package com.github.smallshen.miraibot.xiaoshen.command

import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain

interface GroupCommandHandler {
    /**
     * @param e 原始消息事件
     * @param args
     * @see MessageChain.parseCommand
     */
    suspend fun onGroupCommand(e: GroupMessageEvent, args: MessageChain)
}