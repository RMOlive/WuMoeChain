package com.rmolives.blockchain.util

import java.lang.StringBuilder
import java.security.MessageDigest

fun sha256(s: String): String {
    val messageDigest = MessageDigest.getInstance("SHA-256")
    messageDigest.update(s.toByteArray())
    val byteBuffer = messageDigest.digest()

    val strHexString = StringBuilder()
    for (i in byteBuffer.indices) {
        val hex = Integer.toHexString(0xff and byteBuffer[i].toInt())
        if (hex.length == 1)
            strHexString.append('0')
        strHexString.append(hex)
    }
    return strHexString.toString()
}