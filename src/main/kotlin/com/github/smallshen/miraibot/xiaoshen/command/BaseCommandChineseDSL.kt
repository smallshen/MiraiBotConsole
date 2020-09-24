package com.github.smallshen.miraibot.xiaoshen.command

import com.github.smallshen.miraibot.BotConsole
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.SingleMessage
import net.mamoe.mirai.message.data.content

typealias 命令执行块<T> = suspend 执行块<T>.() -> Unit

open class 基本指令(
    val command: String
) : CommandHandler {

    private var executors: MutableMap<Long, 命令执行块<User>> = mutableMapOf()


    open fun 执行(blockCommand: 命令执行块<User>) {
        executors[System.currentTimeMillis()] = blockCommand
    }


    override suspend fun onCommand(e: MessageEvent, args: MessageChain) {
        val executor = 执行块(e.sender, args = args, e = e)
        executors.forEach {
            it.value.invoke(executor)
        }
    }

    fun 注册() {
        BotConsole.bot.onAnywhereCommand(command, handler = this)
    }


}

fun 指令(指令: String, base: 基本指令.() -> Unit) =
    基本指令(command = 指令).apply(base)


class 执行块<E : User>(val sender: E, val args: MessageChain, val e: MessageEvent) {
    fun 字符(index: Int): String? {
        return args[index].content
    }

    fun 整数(index: Int): Int? {
        return 字符(index)?.toIntOrNull()
    }

    fun 浮点数(index: Int): Float? {
        return 字符(index)?.toFloatOrNull()
    }

    fun 长整数(index: Int): Long? {
        return 字符(index)?.toLongOrNull()
    }

    fun 双精数(index: Int): Double? {
        return 字符(index)?.toDoubleOrNull()
    }

    suspend fun 回复(消息: String) {
        e.reply(消息)
    }

    suspend fun 回复(消息: MessageChain) {
        e.reply(消息)
    }

    suspend fun reply(message: SingleMessage) {
        e.reply(message)
    }


}

val MessageEvent.发送人: User get() = this.sender
