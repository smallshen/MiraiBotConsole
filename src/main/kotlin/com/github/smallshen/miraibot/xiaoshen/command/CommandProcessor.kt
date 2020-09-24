package com.github.smallshen.miraibot.xiaoshen.command


import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText

object CommandProcessor {
    var registeredCommands = mutableListOf<String>()
}

fun Bot.onAnywhereCommand(command: String, handler: CommandHandler) {
    CommandProcessor.registeredCommands.plusAssign(command)
    this.subscribeAlways<MessageEvent> {
        if (Command.isCommand(this.message, command)) {
            handler.onCommand(this, this.message.parseCommand())
        }
    }
}

fun Bot.onGroupCommand(command: String, handler: GroupCommandHandler) {
    CommandProcessor.registeredCommands.plusAssign(command)
    this.subscribeAlways<GroupMessageEvent> {
        if (Command.isCommand(this.message, command)) {
            handler.onGroupCommand(this, this.message.parseCommand())
        }
    }
}

fun Bot.onGroupCommand(command: String, handler: CommandHandler) {
    CommandProcessor.registeredCommands.plusAssign(command)
    this.subscribeAlways<GroupMessageEvent> {
        if (Command.isCommand(this.message, command)) {
            handler.onCommand(this, this.message.parseCommand())
        }
    }
}

fun Bot.onSpecificGroupCommand(command: String, handler: GroupCommandHandler, group: Long) {
    CommandProcessor.registeredCommands.plusAssign(command)
    this.subscribeAlways<GroupMessageEvent> {
        if (this.group.id == group) {
            if (Command.isCommand(this.message, command)) {
                handler.onGroupCommand(this, this.message.parseCommand())
            }
        }
    }
}


object Command {
    fun isCommand(m: MessageChain, cmd: String): Boolean {
        if (m[1] is PlainText) {
            val c: PlainText = m[1] as PlainText
            if (c.content.equals("/${cmd}", true)) {
                if (m.size == 2) {
                    return true
                }
            }
            if (c.content.startsWith("/${cmd} ", true)) {
                return true
            }
        }
        return false
    }
}


/**
 *
 * 解析消息事件变为指令
 * 例子: /mute [一个@对象] 200
 * 返回: MessageChain
 *      atTarget = MessageChain[0] as At
 *      duration = MessageChain[1].content.toIntOrNull()
 */
fun MessageChain.parseCommand(): MessageChain {
    if (this[1] !is PlainText) {
        throw NotCommandException()
    } else {
        val split = (this[1] as PlainText).content.split("\\s+".toRegex()).toMutableList()
        val rebuild = MessageChainBuilder()
        if (!(split[0].startsWith("/"))) {
            throw NotCommandException()
        } else {
            try {
                split.removeAt(0)
                for (s in split) {
                    if (s == "") {
                        continue
                    }
                    rebuild.add(PlainText(s))
                }

                for (m in this) {
                    if (m == this[0]) {
                        continue
                    } else if (m == this[1]) {
                        continue
                    } else {
                        if (m is PlainText) {
                            if (m.content == " ") {
                                continue
                            } else if (m.content == "") {
                                continue
                            } else {
                                m.content.split("\\s+".toRegex()).forEach {
                                    if (it != "") {
                                        rebuild.add(PlainText(it))
                                    }
                                }
                            }

                        } else {
                            rebuild.add(m)
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return rebuild.asMessageChain()

        }
    }

}

