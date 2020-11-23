import com.github.smallshen.miraibot.util.chinese.command.撤回
import com.github.smallshen.miraibot.util.chinese.command.禁言
import com.github.smallshen.miraibot.util.chinese.command.群组指令
import com.github.smallshen.miraibot.util.chinese.是

fun 禁言() = 群组指令("禁言") {
    需要管理员 = 是
    权限提示消息 = "你没有使用禁言的权限"

    执行 {
        成员(0)!!.禁言(整数(1)!!)
        回复("已将 ${成员(0)!!.id} 禁言").撤回(60000)
    }
}

