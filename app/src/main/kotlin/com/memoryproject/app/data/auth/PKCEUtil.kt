package com.memoryproject.app.data.auth

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object PKCEUtil {

    private const val CODE_VERIFIER_LENGTH = 64
    private val BASE64_URL_SAFE = Base64.getUrlEncoder().withoutPadding()

    /**
     * Generates a cryptographically random code verifier for PKCE.
     * Must be 43-128 characters. We use 64 alphanumeric chars.
     */
    fun generateCodeVerifier(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
        val random = SecureRandom()
        return (1..CODE_VERIFIER_LENGTH)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
    }

    /**
     * Generates the S256 code challenge from a code verifier.
     * code_challenge = BASE64URL(SHA256(code_verifier))
     */
    fun generateCodeChallenge(verifier: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(verifier.toByteArray(Charsets.US_ASCII))
        return BASE64_URL_SAFE.encodeToString(hash)
    }

    /**
     * Generates a random state parameter for CSRF protection.
     */
    fun generateState(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return BASE64_URL_SAFE.encodeToString(bytes)
    }
}
