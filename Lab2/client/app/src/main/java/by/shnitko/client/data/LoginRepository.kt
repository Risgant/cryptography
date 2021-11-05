package by.shnitko.client.data

import android.os.Build
import androidx.annotation.RequiresApi
import by.shnitko.client.data.model.LoggedInUser
import by.shnitko.client.data.model.UserCreds
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.security.KeyPairGenerator
import java.security.Security
import java.util.*
import javax.crypto.Cipher


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository {

    // in-memory cache of the loggedInUser object

    private val httpClient: HttpClient = HttpClient(CIO) {
        install(JsonFeature)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        println("providers: "+Arrays.toString(Security.getProviders()))
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        val keyPair = generator.genKeyPair()
        val publicKeyStr = Base64.getEncoder().encodeToString(keyPair.public.encoded)
        try {
            // TODO: handle loggedInUser authentication
            println("before sent request")
            val encryptedResponseStr: String = runBlocking {
                httpClient.post("http://10.0.2.2:9040/user/login") {
                    accept(ContentType.Any)
                    contentType(ContentType.Application.Json)
                    body = UserCreds(username, password, publicKeyStr)
                }
            }
            val decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.private)
            var encryptedResponse = Base64.getDecoder().decode(encryptedResponseStr)
            val decryptedResponse = String(decryptCipher.doFinal(encryptedResponse))
            val userId = UUID.fromString(decryptedResponse)
            val myUser = LoggedInUser(userId, username, keyPair.private, keyPair.public)
            setLoggedInUser(myUser)
            return Result.Success(myUser)
        } catch (e: Throwable) {
            println(e)
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout(userId: UUID) {
        val response: HttpResponse = runBlocking {
            httpClient.post("http://10.0.2.2:9040/user/logout/${userId}") {
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
        println("response: $response")
        UserCache.loggedUser = null

    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        UserCache.loggedUser = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}