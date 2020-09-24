package com.github.smallshen.miraibot.xiaoshen.command


import com.github.smallshen.miraibot.BotConsole
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.SingleMessage
import net.mamoe.mirai.message.data.content

typealias CommandExecutorBlock<T> = suspend Executor<T>.() -> Unit

open class BaseCommandDSL(
    val command: String
) : CommandHandler {

    private var executors: MutableMap<Long, CommandExecutorBlock<User>> = mutableMapOf()


    open fun executor(blockCommand: CommandExecutorBlock<User>) {
        executors[System.currentTimeMillis()] = blockCommand
    }


    override suspend fun onCommand(e: MessageEvent, args: MessageChain) {
        val executor = Executor(e.sender, args = args, e = e)
        executors.forEach {
            it.value.invoke(executor)
        }
    }

    fun register() {
        BotConsole.bot.onAnywhereCommand(command, handler = this)
    }


}

fun command(command: String, base: BaseCommandDSL.() -> Unit) =
    BaseCommandDSL(command = command).apply(base)


class Executor<E : User>(val sender: E, val args: MessageChain, val e: MessageEvent) {
    fun text(index: Int): String? {
        return args[index].content
    }

    fun int(index: Int): Int? {
        return text(index)?.toIntOrNull()
    }

    fun float(index: Int): Float? {
        return text(index)?.toFloatOrNull()
    }

    fun long(index: Int): Long? {
        return text(index)?.toLongOrNull()
    }

    fun double(index: Int): Double? {
        return text(index)?.toDoubleOrNull()
    }

    suspend fun reply(message: String) {
        e.reply(message)
    }

    suspend fun reply(message: MessageChain) {
        e.reply(message)
    }

    suspend fun reply(message: SingleMessage) {
        e.reply(message)
    }


}
