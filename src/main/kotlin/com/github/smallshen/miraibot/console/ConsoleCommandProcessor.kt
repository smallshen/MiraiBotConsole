package com.github.smallshen.miraibot.console

object ConsoleCommandProcessor {
    var registeredConsoleCommands = mutableListOf<Pair<String, ConsoleCommandHandler>>()
}


interface ConsoleCommandHandler {
    suspend fun onConsoleCommand(original: String, rawArg: String, args: List<String>)
}

class ConsoleCommandDSL(val command: String) : ConsoleCommandHandler {
    private val blocks = mutableListOf<ConsoleCommandExecutor.() -> Unit>()
    override suspend fun onConsoleCommand(original: String, rawArg: String, args: List<String>) {
        blocks.forEach {
            ConsoleCommandExecutor(original, rawArg, args).apply(it)
        }
    }

    fun executor(block: ConsoleCommandExecutor.() -> Unit) {
        blocks.add(block)
    }

    fun register() {
        ConsoleCommandProcessor.registeredConsoleCommands.plusAssign(this.command to this)
    }

}


class ConsoleCommandExecutor(val original: String, val rawArg: String, val args: List<String>)