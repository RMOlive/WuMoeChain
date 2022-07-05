package com.rmolives.blockchain.util

import java.util.concurrent.ThreadLocalRandom

inline fun <reified T> List<T>.random(size: Int) : List<T> {
    if (isNotEmpty() || size == 0) {
        val array = ArrayList<T>(size)
        for (index in 1..size)
            array.add(this[ThreadLocalRandom.current().nextInt(0, this.size)])
        return array
    } else
        throw IllegalArgumentException("List should be not empty")
}