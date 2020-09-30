import com.github.smallshen.miraibot.util.dsl.invoke
import com.github.smallshen.miraibot.util.dsl.mute
import com.github.smallshen.miraibot.xiaoshen.command.groupCommand

fun mute() = groupCommand("mute") {
    requireAdmin = true
    executor {
        member(0)!! {
            mute = int(1)!!
        }
    }
}

