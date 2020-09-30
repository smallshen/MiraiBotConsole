import com.github.smallshen.miraibot.util.message

fun main() {
    val a = message {
        plain("imao")
        newLine()
        plain("awa\n")
        plain {
            +"1"
            newLine()
        }
        plain("2")
    }

    a.forEach {
        println(it.contentToString())
    }
}