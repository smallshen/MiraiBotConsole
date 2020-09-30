import com.github.smallshen.miraibot.util.chinese.*
import com.github.smallshen.miraibot.util.chinese.math.大于
import com.github.smallshen.miraibot.util.chinese.math.小于


fun main() {
    wen()
}


fun wen() {
    val 甲 = 吾有一数()
    甲 曰 10
    吾有一言 曰 甲

    吾有一言 曰 (加(甲) 以 2)
    吾有一言 曰 (减(甲) 以 2)

    为是(甲) 遍 {
        若(吾值 小于 5) {
            吾有一言 曰 "吾值小于五之"
        } 若非 {
            吾有一言 曰 "吾值与五不可比也"
        }
    }

    val 列 = 吾有一列("甲", "乙", "丙", "丁")

    吾有一言 曰 列.之长

    吾有一言 曰 (列 值之 0)

    列 添 "戊"

    吾有一言 曰 (列 值之 4)
}

val <T> List<T>.之长 get() = this.size

infix fun Int.小于(敌: Int): Boolean {
    return this < 敌
}

infix fun <T> List<T>.值之(应: Int): T {
    return this[应]
}

fun <T> List<T>.可变其值(): MutableList<T> {
    return this.toMutableList()
}

infix fun <T> MutableList<T>.添(应: T) {
    this.add(应)
}

fun iterTest() {
    组("字符1", "字符2").遍历 {
        打印(it)
    }
}


fun whenTest() {
    val 任意字符串 = "字符串"

    当(任意字符串) {
        值 等于 "字符串" 时 {
            打印("内容是字符串")
        }
    }

    val 任意数字 = 5

    当(任意数字) {
        值 等于 2 时 {
            打印("结果为 2")
        }

        值 等于 5 时 {
            打印("结果为 5")
        } 否则 {
            打印("结果不为 5")
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


class 吾有一数(var 值: Int? = null) {
    override fun toString(): String {
        return 值.toString()
    }
}

class 为是(val 数: 吾有一数)

infix fun 为是.遍(运: 运.() -> Unit) {
    repeat(this.数.值!!) {
        运.invoke(运(it))
    }
}

class 运(val 吾值: Int)

infix fun 吾有一数.曰(值: Int): Int {
    this.值 = 值
    return 值
}

val 吾有一言 = 言()

class 言

infix fun 言.曰(值: Any?) {
    println(值)
}

private class ArrayAsCollection<T>(val values: Array<out T>, val isVarargs: Boolean) : Collection<T> {
    override val size: Int get() = values.size
    override fun isEmpty(): Boolean = values.isEmpty()
    override fun contains(element: T): Boolean = values.contains(element)
    override fun containsAll(elements: Collection<T>): Boolean = elements.all { contains(it) }
    override fun iterator(): Iterator<T> = values.iterator()

    // override hidden toArray implementation to prevent copying of values array
    fun toArray(): Array<out Any?> = values.copyToArrayOfAny(isVarargs)
}

internal fun <T> Array<out T>.copyToArrayOfAny(isVarargs: Boolean): Array<out Any?> =
    if (isVarargs && this.javaClass == Array<Any?>::class.java)
    // if the array came from varargs and already is array of Any, copying isn't required
        @Suppress("UNCHECKED_CAST") (this as Array<Any?>)
    else
        java.util.Arrays.copyOf(this, this.size, Array<Any?>::class.java)

fun <T> 吾有一列(vararg 元素: T): MutableList<T> =
    if (元素.isEmpty()) ArrayList() else ArrayList(ArrayAsCollection(元素, isVarargs = true))

val 百 = 100

fun 若(条件: Boolean, 执行: 判断执行块.() -> Unit): 判断执行块 {
    val e = 判断执行块()
    if (条件) {
        e.result = true
        执行.invoke(e)
    }

    return e
}

class 判断执行块(var result: Boolean = false)

infix fun 判断执行块.若非(执行: 判断执行块.() -> Unit) {
    if (!this.result) {
        执行.invoke(判断执行块().apply { result = false })
    }
}

infix fun 判断执行块.若非(执行: 判断执行块): 判断执行块 {
    return this
}

class 加(val 值: 吾有一数)

infix fun 加.以(值: Int): Int {
    return this.值.值!! + 值
}

class 减(val 值: 吾有一数)

infix fun 减.以(值: Int): Int {
    return this.值.值!! - 值
}