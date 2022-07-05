package com.rmolives.blockchain

/**
 * @author RMOLive (rmolives@gmail.com)
 * @param user Current member user.
 * @param publicKey Current member public key.
 * @param privateKey Current member private key
 */
class Member(val user: User, private val publicKey: String, private val privateKey: String) {
    /**
     * Initiate a new transaction.
     * @param to Receiver.
     * @param amount Number of transfers.
     * @return <signature, transaction>
    */
    fun transaction(to: String, amount: Long, chain: Chain): Transaction {
        val transaction = Transaction(user.address, to, amount, publicKey)
        transaction.sign(privateKey)
        return transaction
    }
}