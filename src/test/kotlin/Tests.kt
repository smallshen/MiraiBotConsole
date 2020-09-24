import com.github.smallshen.miraibot.util.chinese.*
import com.github.smallshen.miraibot.util.chinese.math.大于
import com.github.smallshen.miraibot.util.chinese.math.小于


fun main() {
    iterTest()
}

fun iterTest() {
    listOf("String", "awa").便利 { 对象 ->
        打印(对象)
    }

}


fun whenTest() {
    val a = "awa"

    当(a) {
        值 等于 "awa" 时 {
            打印("是awa呀")
        }

        值 等于 "qwq" 时 {
            打印("是qwq")
        }
    }
}

fun iftest() {
    如果(2 小于 1) {
        打印("1 比 2 大")
    } 否则 如果(2 小于 2) {
        打印("2 等于 2")
    } 否则 如果((3 - 1) 大于 (4 - 1)) {
        打印("(3 - 1) 大于 (4 - 1)")
    } 否则 {
        打印("以上等式都不成立")
    }
}
