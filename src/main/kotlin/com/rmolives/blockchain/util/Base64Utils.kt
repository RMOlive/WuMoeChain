package com.rmolives.blockchain.util

import java.util.*

/**
 * @author RMOLive (rmolives@gmail.com)
 */
object Base64Utils {
    fun decode(base64: String): ByteArray {
        return Base64.getDecoder().decode(base64.toByteArray())
    }

    fun encode(bytes: ByteArray): String {
        return String(Base64.getEncoder().encode(bytes))
    }

    fun encode(str: String): String {
        val bytes = str.toByteArray(charset("utf-8"))
        return encode(bytes)
    }
}