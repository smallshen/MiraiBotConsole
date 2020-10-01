package com.github.smallshen.miraibot.xiaoshen.command

import com.github.smallshen.miraibot.BotConsole
import com.github.smallshen.miraibot.util.MessageBuilderDSL
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isAdministrator
import net.mamoe.mirai.contact.isOwner
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.*

typealias ExecutorBlock<T> = suspend GroupCommandExecutor<T>.() -> Unit

open class GroupCommandDSL(
    val command: String,
    var permissionMessage: String? = "你的权限不够",
    var requireAdmin: Boolean = false,
) : GroupCommandHandler {

    private var executors: MutableMap<Long, ExecutorBlock<Member>> = mutableMapOf()

    private var commandExecutorNotAdmin: MutableMap<Long, ExecutorBlock<Member>> = mutableMapOf()

    open fun executor(blockCommand: ExecutorBlock<Member>) {
        executors[System.currentTimeMillis()] = blockCommand
    }

    open fun executorNotAdmin(blockCommand: ExecutorBlock<Member>) {
        commandExecutorNotAdmin[System.currentTimeMillis()] = blockCommand
    }


    override suspend fun onGroupCommand(e: GroupMessageEvent, args: MessageChain) {
        val executor = GroupCommandExecutor(sender = e.sender, args = args, e = e)
        executors.forEach {
            if (requireAdmin) {
                when {
                    e.sender.isAdministrator() -> {
                        it.value.invoke(executor)
                    }

                    e.sender.isOwner() -> {
                        it.value.invoke(executor)
                    }

                    else -> {
                        if (permissionMessage != null) {
                            e.reply(permissionMessage!!)
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

    fun register() {
        BotConsole.bot.onGroupCommand(command, handler = this)
    }

    fun register(group: Long) {
        BotConsole.bot.onSpecificGroupCommand(command, handler = this, group = group)
    }


}

fun groupCommand(command: String, groupCommand: GroupCommandDSL.() -> Unit) =
    GroupCommandDSL(command = command).apply(groupCommand)


fun modifyCommand(dsl: GroupCommandDSL, modification: GroupCommandDSL.() -> Unit) = dsl.apply(modification)

class GroupCommandExecutor<E : Member>(val sender: E, val args: MessageChain, val e: GroupMessageEvent) {
    fun text(index: Int): String? {
        return args[index].content
    }

    fun int(index: Int): Int? {
        return text(index)?.toIntOrNull()
    }

    fun float(index: Int): Float? {
        return text(index)?.toFloatOrNull()
    }

    fun double(index: Int): Double? {
        return text(index)?.toDoubleOrNull()
    }

    fun long(index: Int): Long? {
        return text(index)?.toLongOrNull()
    }

    fun member(index: Int): Member? {
        return when (args[index]) {
            is At -> e.group.getOrNull((args[index] as At).target)

            is PlainText -> e.group.getOrNull(args[index].content.toLongOrNull()!!)

            else -> null
        }
    }


    suspend fun reply(message: MessageBuilderDSL.() -> Unit) {
        val a = MessageBuilderDSL().apply(message)
        reply(a)
    }

    suspend fun reply(message: String) {
        e.reply(message)
    }

    suspend fun reply(message: MessageChain) {
        e.reply(message)
    }

    suspend infix fun reply(message: MessageChainBuilder): MessageReceipt<Contact> {
        return e.reply(message.asMessageChain())
    }

    suspend fun reply(message: SingleMessage) {
        e.reply(message)
    }

}
