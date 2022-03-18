package com.example.nammametromvvm.utility

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

class HashCalClass {
    var hashSequence =
        "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt"

    companion object {
        fun hashCal(type: String?, hashString: String): String {
            val hash = StringBuilder()
            var messageDigest: MessageDigest? = null
            try {
                messageDigest = MessageDigest.getInstance(type)
                messageDigest.update(hashString.toByteArray())
                val mdbytes = messageDigest.digest()
                for (hashByte in mdbytes) {
                    hash.append(
                        Integer.toString((hashByte and 0xff.toByte()) + 0x100, 16).substring(1)
                    )
                }
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return hash.toString()
        }
    }
}