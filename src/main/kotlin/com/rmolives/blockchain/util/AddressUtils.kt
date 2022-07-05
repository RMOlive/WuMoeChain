package com.rmolives.blockchain.util

/**
 * @author RMOLive (rmolives@gmail.com)
 */
object AddressUtils {
    private const val version = "1.0"
    /**
     * @param publicKey User's signature public key.
     * @param version Address version number.
     * @return address
     */
    fun getAddress(publicKey: String): String {
        var ans = version + sha256(sha256(publicKey))
        ans = sha256(sha256(ans)).substring(0, 8) + ans
        return Base64Utils.encode(sha256(sha256(ans)))
    }
}