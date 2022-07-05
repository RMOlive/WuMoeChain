package com.rmolives.blockchain

import com.rmolives.blockchain.util.sha256

import java.time.Instant

/**
 * Blocks in the blockchain.
 * @author RMOLive (rmolives@gmail.com)
 * @param previousHash The hash value of the predecessor block.
 * @param transactions All transactions packaged in the block.
 * @param timestamp Time to pack blocks.
 */
class Block private constructor(val previousHash: String, val transactions: Array<Transaction>, private val timestamp: Long) {
    var hash: String
    private val hashTransaction = hashTransaction(transactions)

    companion object {
        /**
         * Create new block.
         * @param previousHash The hash value of the predecessor block.
         * @param transactions Packaged trading pool.
         * @return new block.
         */
        fun newBlock(previousHash: String, transactions: Array<Transaction>): Block {
            return Block(previousHash, transactions, Instant.now().epochSecond)
        }
    }

    init {
        hash = computeHash()
    }

    /**
     * Get the hash value of all transactions in the transaction pool.
     * @param array transactions.
     * @return hash value.
     */
    private fun hashTransaction(array: Array<Transaction>): String {
        if (array.size == 1)
            return sha256(sha256(array[0].hash))
        // Internal function, calculate and generate hash tree.
        fun hashTree(low: Array<Transaction>, high: Array<Transaction>): String {
            val lowHash = if (low.size == 1)
                low[0].hash
            else {
                val lowMid = (low.size - 1) / 2
                hashTree(low.slice(0..lowMid).toTypedArray(),
                        low.slice(lowMid + 1 until low.size).toTypedArray())
            }

            val highHash = if (high.size == 1)
                high[0].hash
            else {
                val highMid = (high.size - 1) / 2
                hashTree(high.slice(0..highMid).toTypedArray(),
                        high.slice(highMid + 1 until high.size).toTypedArray())
            }
            return sha256(sha256(lowHash + highHash))
        }
        val mid = (array.size - 1) / 2
        return hashTree(array.slice(0..mid).toTypedArray(),
                array.slice(mid + 1 until array.size).toTypedArray())
    }

    /**
     * Calculate the hash value of the current block.
     * @return hash value.
     */
    fun computeHash(): String =
            sha256(sha256(previousHash + hashTransaction + timestamp))

    /**
     * Verify all transactions in the current block.
     * @return <Validation results, Problematic transaction>
     */
    fun validateBlockTransactions(): Pair<Boolean, Transaction?> {
        for (transaction in transactions) {
            if (!transaction.isValid())
                return Pair(false, transaction)
        }
        return Pair(true, null)
    }
}