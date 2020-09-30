package com.github.smallshen.miraibot.util

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.TempMessageEvent
import net.mamoe.mirai.message.data.MessageChainBuilder
import java.io.InputStream

suspend fun HttpClient.getImg(url: String): InputStream {
    val response = get<HttpResponse>(url)
    return response.content.toInputStream()
}

suspend infix fun GroupMessageEvent.reply(m: MessageChainBuilder): MessageReceipt<Contact> {
    return this.reply(m.asMessageChain())
}


suspend infix fun FriendMessageEvent.reply(m: MessageChainBuilder): MessageReceipt<Contact> {
    return reply(m.asMessageChain())
}

suspend infix fun TempMessageEvent.reply(m: MessageChainBuilder): MessageReceipt<Contact> {
    return reply(m.asMessageChain())
}