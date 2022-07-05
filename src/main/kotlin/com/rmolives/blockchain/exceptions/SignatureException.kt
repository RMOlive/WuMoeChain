package com.rmolives.blockchain.exceptions

import java.lang.Exception

class SignatureException(address: String, publicKey: String) : Exception("$address public key is not a $publicKey!")