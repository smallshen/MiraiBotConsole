package com.github.smallshen.miraibot.xiaoshen.command

class NotCommandException(message: String) : Exception(message) {
    constructor() : this("This is not String")
}
