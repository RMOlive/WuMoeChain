package com.rmolives.blockchain

import com.rmolives.blockchain.exceptions.TransactionException
import java.util.*

/**
 * Chain in the blockchain.
 * @author RMOLive (rmolives@gmail.com)
 */
class Chain {
    private val blockList = LinkedList<Block>()                     // Blockchain.
    private val minerTransactionPool = LinkedList<Transaction>()    // Transaction pool.

    init {
        // Initialize ancestor block.
        blockList.add(bigBlock())
    }

    /**
     * Get the last block of the blockchain、
     * @return last block.
     */
    private fun getLatestBlock(): Block = blockList[blockList.size - 1]

    /**
     * Add a new transaction to the transaction pool.
     * @param transaction Transactions added to the trading pool.
     * @return Add transaction successfully.
     */
    fun addTransaction(transaction: Transaction): Boolean {
        if (!transaction.isValid() ||
                ((transaction.from != "" || transaction.to != "") &&
                        !searchWallet(transaction.from, transaction.amount)))
            return false
        minerTransactionPool.add(transaction)
        return true
    }

    /**
     * Query user wallet.
     * @param address Inquired users.
     * @param amount Inquired amount.
     * @return Does this user have so much money.
     */
    private fun searchWallet(address: String, amount: Long): Boolean {
        var size = 0L
        for (block in blockList) {
            if (size >= amount)
                return true
            for (transaction in block.transactions) {
                if (size >= amount)
                    return true
                if (!transaction.isValid())
                    throw TransactionException(transaction.hash)
                if (transaction.from == address) {
                    if (size < transaction.amount)
                        throw TransactionException(transaction.hash)
                    size -= transaction.amount
                }
                if (transaction.to == address) {
                    size += transaction.amount
                }
            }
        }
        return false
    }

    /**
     * packed transaction pool.
     * @param minerRewardAddress Packer's account.
     * @return Packed blocks.
     */
    fun mineTransactionPool(minerRewardAddress: String): Block {
        val transactionPool = LinkedList(minerTransactionPool)
        // Create a new block.
        var newBlock = Block.newBlock(getLatestBlock().hash, transactionPool.toTypedArray())
        while (!newBlock.hash.startsWith("0"))
            newBlock = Block.newBlock(getLatestBlock().hash, transactionPool.toTypedArray())
        // Add a package reward transaction that the system transfers to the packager.
        transactionPool.add(Transaction("", minerRewardAddress, 1, ""))
        blockList.add(newBlock)
        minerTransactionPool.clear()
        return newBlock
    }

    /**
     * Verify if the blockchain has been tampered with.
     * @return critical result.
     */
    fun validateChain(): Boolean {
        if (blockList.size == 1) {
            if (blockList[0].hash != blockList[0].computeHash())
                return false
            return true
        }
        // Iterate over all nodes in the blockchain.
        for (index in 1 until blockList.size) {
            val blockToValidate = blockList[index]
            val (validate) = blockToValidate.validateBlockTransactions()
            if (!validate)
                return false
            // Determine whether the current block has been tampered with.
            if (blockToValidate.hash != blockToValidate.computeHash())
                return false
            val previousBlock = blockList[index - 1]
            // Determine if the blockchain is broken.
            if (blockToValidate.previousHash != previousBlock.hash)
                return false
        }
        return true
    }

    /**
     * Get ancestor block.
     * @return ancestor block.
     */
    private fun bigBlock(): Block {
        return Block.newBlock("", arrayOf(Transaction("", "", 0, "")))
    }
}