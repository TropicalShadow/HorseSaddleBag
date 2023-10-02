package club.tesseract.horsechestsaddle.utils

import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

class ByteArraySerializer {
    companion object {
        fun serializeNullableByteArrays(byteArrays: List<ByteArray?>): ByteArray {
            val outputStream = ByteArrayOutputStream()
            val dataOutputStream = DataOutputStream(outputStream)

            for (byteArray in byteArrays) {
                if (byteArray == null) {
                    // Write -1 to indicate a null byte array
                    dataOutputStream.writeInt(-1)
                } else {
                    // Write the byte array size and data
                    dataOutputStream.writeInt(byteArray.size)
                    dataOutputStream.write(byteArray)
                }
            }

            dataOutputStream.close()
            return outputStream.toByteArray()
        }

        fun deserializeNullableByteArrays(data: ByteArray): List<ByteArray?> {
            val inputStream = DataInputStream(data.inputStream())
            val byteArrays = mutableListOf<ByteArray?>()

            while (inputStream.available() > 0) {
                val byteArraySize = inputStream.readInt()
                if (byteArraySize == -1) {
                    // Read -1 to indicate a null byte array
                    byteArrays.add(null)
                } else {
                    val byteArray = ByteArray(byteArraySize)
                    inputStream.readFully(byteArray)
                    byteArrays.add(byteArray)
                }
            }

            return byteArrays
        }
    }
}
