package com.github.smallshen.miraibot.xiaoshen.command

import com.github.smallshen.miraibot.BotConsole
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isAdministrator
import net.mamoe.mirai.contact.isOwner
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*

typealias 群执行块<T> = suspend 群组执行块.() -> Unit

open class 群指令(
    val 指令: String,
    var 权限提示消息: String? = "你的权限不够",
    var 需要管理员: Boolean = false,
) : GroupCommandHandler {

    private var executors: MutableMap<Long, 群执行块<Member>> = mutableMapOf()

    private var commandExecutorNotAdmin: MutableMap<Long, 群执行块<Member>> = mutableMapOf()

    open fun 执行(blockCommand: 群执行块<Member>) {
        executors[System.currentTimeMillis()] = blockCommand
    }

    open fun 非管理时(blockCommand: 群执行块<Member>) {
        commandExecutorNotAdmin[System.currentTimeMillis()] = blockCommand
    }


    override suspend fun onGroupCommand(e: GroupMessageEvent, args: MessageChain) {
        val executor = 群组执行块(指令参数 = args, 事件 = e)
        executors.forEach {
            if (需要管理员) {
                when {
                    e.sender.isAdministrator() -> {
                        it.value.invoke(executor)
                    }

                    e.sender.isOwner() -> {
                        it.value.invoke(executor)
                    }

                    else -> {
                        if (权限提示消息 != null) {
                            e.reply(权限提示消息!!)
                        }
                        commandExecutorNotAdmin.forEach { nd ->
                            nd.value.invoke(executor)
                        }
                    }
                }
            } else {
                it.value.invoke(executor)
            }
        }


    }

    fun 注册() {
        BotConsole.bot.onGroupCommand(指令, handler = this)
    }

    fun 注册(群号: Long) {
        BotConsole.bot.onSpecificGroupCommand(指令, handler = this, group = 群号)
    }


}

fun 群组指令(指令: String, groupCommand: 群指令.() -> Unit) =
    群指令(指令 = 指令).apply(groupCommand)


class 群组执行块(val 指令参数: MessageChain, val 事件: GroupMessageEvent) {
    fun 字符(index: Int): String? {
        return 指令参数[index].content
    }

    fun 整数(index: Int): Int? {
        return 字符(index)?.toIntOrNull()
    }

    fun 浮点数(index: Int): Float? {
        return 字符(index)?.toFloatOrNull()
    }

    fun 双精数(index: Int): Double? {
        return 字符(index)?.toDoubleOrNull()
    }

    fun 长整数(index: Int): Long? {
        return 字符(index)?.toLongOrNull()
    }

    fun 成员(index: Int): Member? {
        return when (指令参数[index]) {
            is At -> 事件.group.getOrNull((指令参数[index] as At).target)

            is PlainText -> 事件.group.getOrNull(指令参数[index].content.toLongOrNull()!!)

            else -> null
        }
    }

    fun 回复(消息: String) {
        runBlocking {
            事件.reply(消息)
        }
    }

    fun 回复(消息: MessageChain) {
        runBlocking {
            事件.reply(消息)
        }
    }

    fun 回复(消息: SingleMessage) {
        runBlocking {
            事件.reply(消息)
        }
    }

}

suspend fun Member.踢出() {
    this.kick()
}

val GroupMessageEvent.发送人: Member get() = this.sender