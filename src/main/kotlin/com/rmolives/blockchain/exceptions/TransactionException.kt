package com.rmolives.blockchain.exceptions

import java.lang.Exception

class TransactionException(hash: String): Exception("The transaction with hash value $hash is incorrect!")