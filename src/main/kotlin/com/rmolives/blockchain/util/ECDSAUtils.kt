package com.rmolives.blockchain.util

import java.security.*
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * Elliptic Curve Digital Signature Algorithm.
 * @author RMOLive (rmolives@gmail.com)
 */
object ECDSAUtils {
    private const val ALGORITHM = "EC"
    private const val SIGN_ALGORITHM = "SHA256withECDSA"

    /**
     * Initialize public and private keys.
     * @return public and private keys.
     */
    fun initKey(): KeyPair {
        return try {
            val generator = KeyPairGenerator.getInstance(ALGORITHM)
            val ecGenParameterSpec = ECGenParameterSpec("secp256k1")
            generator.initialize(ecGenParameterSpec, SecureRandom())
            generator.initialize(256)
            generator.generateKeyPair()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * @param data Data to sign.
     * @param privateKey Signer's private key.
     * @return signature.
     */
    fun sign(data: ByteArray, privateKey: ByteArray): ByteArray {
        return try {
            val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(privateKey)
            val keyFactory = KeyFactory.getInstance(ALGORITHM)
            val priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec)
            val signature = Signature.getInstance(SIGN_ALGORITHM)
            signature.initSign(priKey)
            signature.update(data)
            signature.sign()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * @param data Signed data.
     * @param publicKey Public key of signer for verifying signature.
     * @param sign Signed by the signer.
     * @return Validation results.
     */
    fun verify(data: ByteArray, publicKey: ByteArray, sign: ByteArray): Boolean {
        return try {
            val keySpec = X509EncodedKeySpec(publicKey)
            val keyFactory = KeyFactory.getInstance(ALGORITHM)
            val pubKey = keyFactory.generatePublic(keySpec)
            val signature = Signature.getInstance(SIGN_ALGORITHM)
            signature.initVerify(pubKey)
            signature.update(data)
            signature.verify(sign)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}