package com.github.smallshen.miraibot.util

import com.github.smallshen.miraibot.xiaoshen.command.GroupCommandExecutor
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.events.GroupEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.uploadImage

class MessageBuilderDSL(
    private val groupMessageEvent: GroupMessageEvent? = null,
    private val group: Group? = groupMessageEvent?.group
) : MessageChainBuilder() {
    fun plain(block: PlainTextDSL.() -> Unit): PlainText {
        val p = PlainText(PlainTextDSL().apply(block).content)
        add(p)
        return p
    }

    fun plain(text: String): PlainText {
        val p = PlainText(text)
        add(p)
        return p
    }


    fun img(block: ImageDSL.() -> Unit): Image {
        val i = ImageDSL().apply(block)

        var f: Image?

        runBlocking {
            f = group!!.uploadImage(HttpClient().getImg(i.url))
            add(f!!)
        }

        return f!!

    }

    fun img(id: String): Image {
        val i = Image(id)
        add(i)
        return i
    }

    fun at(block: AtDSL.() -> Unit): At {
        val a = AtDSL(group!!).apply(block)
        val at = At(a.target)
        add(at)
        return at
    }


    fun at(): At {
        with(At(this.groupMessageEvent!!.sender)) {
            add(this)
            return this
        }
    }

    fun at(member: Member): At {
        val at = At(member)
        add(at)
        return at
    }

    fun at(member: Long): At {
        val at = At(group!!.getOrNull(member)!!)
        add(at)
        return at
    }

    fun newLine(): PlainText {
        with(PlainText("\n")) {
            add(this)
            return this
        }
    }


}


fun message(buildAction: MessageBuilderDSL.() -> Unit) = MessageBuilderDSL().apply(buildAction)

fun GroupEvent.message(buildAction: MessageBuilderDSL.() -> Unit) = MessageBuilderDSL(group = this.group).apply(buildAction)

fun GroupCommandExecutor<Member>.message(buildAction: MessageBuilderDSL.() -> Unit) =
    MessageBuilderDSL(groupMessageEvent = this.e).apply(buildAction)


sealed class MessageDSL

data class ImageDSL(var url: String = "") : MessageDSL()
data class PlainTextDSL(var content: String = "") : MessageDSL() {
    operator fun plus(s: Int): Unit {
        content + s
    }

    operator fun String.unaryPlus(): Unit {
        content + this
    }

    fun newLine() {
        this.content += "\n"
    }
}


class AtDSL(group: Group, var target: Member = group.botAsMember) : MessageDSL()
