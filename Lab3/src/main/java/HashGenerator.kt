import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
fun sha1Hash(text: String): BigInteger {
    val md = MessageDigest.getInstance("SHA-1")
    val textBytes = text.toByteArray(charset("UTF-8"))
    md.update(textBytes, 0, textBytes.size)
    val sha1hash = md.digest()
    return BigInteger(1, sha1hash)
}