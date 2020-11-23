package com.github.smallshen.miraibot.extension

import com.github.smallshen.miraibot.BotConsole
import io.xiaoshen.commandbuilder.*

fun friendCommand(command: String, block: FriendCommand.() -> Unit) =
    FriendCommand(command, BotConsole.bot).apply(block)

fun tempCommand(command: String, block: TempCommand.() -> Unit) =
    TempCommand(command, BotConsole.bot).apply(block)

fun groupCommand(command: String, block: groupBlock) =
    GroupCommand(command, BotConsole.bot).apply { executor(block) }

fun prefixCommands(prefix: String, block: PrefixCommandScope.() -> Unit) =
    PrefixCommandScope(prefix, BotConsole.bot).apply(block)