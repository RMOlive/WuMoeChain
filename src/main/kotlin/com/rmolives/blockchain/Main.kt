package com.rmolives.blockchain

fun main() {
    val chain = Chain()
    val u1 = Register()
    val u2 = Register()
    val t1 = u1.member.transaction(u2.member.user.address, 0)
    val t2 = u2.member.transaction(u1.member.user.address, 0)
    chain.addTransaction(t1)
    chain.addTransaction(t2)
    println(chain.mineTransactionPool(u1.member.user.address).hash)
    println(chain.validateChain())
}