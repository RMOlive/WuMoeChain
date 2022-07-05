package com.rmolives.blockchain

import com.rmolives.blockchain.exceptions.SignatureException
import com.rmolives.blockchain.util.AddressUtils
import com.rmolives.blockchain.util.Base64Utils
import com.rmolives.blockchain.util.ECDSAUtils
import com.rmolives.blockchain.util.sha256

/**
 * Blockchain transactions.
 * @author RMOLive (rmolives@gmail.com)
 * @param from The person who initiated the transfer.
 * @param to Receiver.
 * @param amount Number of transfers.
 */
class Transaction(val from: String, val to: String, val amount: Long, private val fromPublicKey: String) {
    var signature = ""          // Signature of current transaction.
    val hash: String            // The hash of the current transaction.

    init {
        hash = computeHash()
    }

    /**
     * Get the hash value of the current transaction.
     * @return hash value.
     */
    private fun computeHash(): String = sha256(sha256(from + to + amount))

    /**
     * @param key Private key for signature.
     * @return signature.
     */
    fun sign(key: String): String {
        // If the transaction initiator's public key is incorrect.
        if (AddressUtils.getAddress(fromPublicKey) != from)
            throw SignatureException(from, fromPublicKey)
        signature = Base64Utils.encode(ECDSAUtils.sign(hash.toByteArray(), Base64Utils.decode(key)))
        return signature
    }

    /**
     * @return Verified result.
     */
    fun isValid(): Boolean {
        if (from == "" || to == "")
            return true
        if(signature == "")
            return false
        return ECDSAUtils.verify(this.computeHash().toByteArray(),
                Base64Utils.decode(fromPublicKey),
                Base64Utils.decode(this.signature))
    }
}