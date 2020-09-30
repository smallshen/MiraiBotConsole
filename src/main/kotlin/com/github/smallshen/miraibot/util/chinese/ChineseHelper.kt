package com.github.smallshen.miraibot.util.chinese


val 是 = true
val 否 = false

val 空 = null


val 真 = true
val 假 = false

fun 打印(内容: Any) {
    println(内容)
}


fun 如果(条件: Boolean, 执行: 判断执行块.() -> Unit): 判断执行块 {
    val e = 判断执行块()
    if (条件) {
        e.result = true
        执行.invoke(e)
    }

    return e
}

class 判断执行块(var result: Boolean = false)

infix fun 判断执行块.否则(执行: 判断执行块.() -> Unit) {
    if (!this.result) {
        执行.invoke(判断执行块().apply { result = false })
    }
}

infix fun 判断执行块.否则(执行: 判断执行块): 判断执行块 {
    return 执行
}

fun 当(value: Any?, 执行块: 当判断.() -> Unit): 当判断 {
    val b = 当判断(value)
    执行块.invoke(b)
    return b
}


class 当判断(val 值: Any?) {
    infix fun Any?.等于(比较对象: Any?): Boolean {
        return 比较对象 == 值
    }

    infix fun Boolean.时(执行: 判断执行块.() -> Unit): 判断执行块 {
        val e = 判断执行块()
        if (this) {
            e.result = true
            执行.invoke(e)
        }

        return e
    }
}

inline fun <T> Iterable<T>.遍历(action: (T) -> Unit): Unit {
    for (element in this) action(element)
}

public fun <T> 组(vararg 元素: T): List<T> = if (元素.size > 0) 元素.asList() else emptyList()

public fun <T> Iterable<T>.移除(数量: Int): List<T> {
    require(数量 >= 0) { "Requested element count $数量 is less than zero." }
    if (数量 == 0) return toList()
    val list: ArrayList<T>
    if (this is Collection<*>) {
        val resultSize = size - 数量
        if (resultSize <= 0)
            return emptyList()
        if (resultSize == 1)
            return listOf(last())
        list = ArrayList<T>(resultSize)
        if (this is List<T>) {
            if (this is RandomAccess) {
                for (index in 数量 until size)
                    list.add(this[index])
            } else {
                for (item in listIterator(数量))
                    list.add(item)
            }
            return list
        }
    }
    else {
        list = ArrayList<T>()
    }
    var count = 0
    for (item in this) {
        if (count >= 数量) list.add(item) else ++count
    }
    return list.optimizeReadOnlyList()
}

internal fun <T> List<T>.optimizeReadOnlyList() = when (size) {
    0 -> emptyList()
    1 -> listOf(this[0])
    else -> this
}





