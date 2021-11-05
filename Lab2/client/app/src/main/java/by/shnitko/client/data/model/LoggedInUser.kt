package by.shnitko.client.data.model

import java.security.PrivateKey
import java.security.PublicKey
import java.util.*

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
        val id: UUID,
        val name: String,
        val privateKey: PrivateKey,
        val publicKey: PublicKey
)