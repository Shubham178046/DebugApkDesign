package com.example.debugapkdesign

import android.content.Context
import android.database.Cursor
import android.net.Uri
import java.io.*

object FilesUtil {
    /* renamed from: a */
    private const val f5177a = -1

    /* renamed from: b */
    private const val f5178b = 4096

    private fun C1039k() {}

    /* renamed from: a */
    private fun m6539a(inputStream: InputStream, outputStream: OutputStream): Long {
        val bArr = ByteArray(4096)
        var j: Long = 0
        while (true) {
            val read: Int = inputStream.read(bArr)
            if (-1 == read) {
                return j
            }
            outputStream.write(bArr, 0, read)
            j += read.toLong()
        }
    }

    /* renamed from: a */
    fun m6540a(context: Context, uri: Uri): File? {
        return try {
            val openInputStream: InputStream? = context.getContentResolver().openInputStream(uri)
            val b = m6543b(context, uri)
            val c = m6545c(b)
            if (c[0].length == 0) {
                return File("")
            }
            val a: File = m6541a(File.createTempFile("ac" + c[0], c[1]), b)
            a.deleteOnExit()
            var fileOutputStream: FileOutputStream? = null
            try {
                fileOutputStream = FileOutputStream(a)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (openInputStream != null) {
                m6539a(openInputStream, fileOutputStream as OutputStream)
                openInputStream.close()
            }
            if (fileOutputStream != null) {
                fileOutputStream.close()
            }
            a
        } catch (unused: NullPointerException) {
            File("")
        } catch (unused2: RuntimeException) {
            File("")
        }
    }

    /* renamed from: a */
    private fun m6541a(file: File, str: String): File {
        val file2 = File(file.getParent(), str)
        if (!file2.equals(file)) {
            if (file2.exists()) {
                file2.delete()
            }
            file.renameTo(file2)
        }
        return file2
    }

    /* renamed from: a */
    fun m6542a(str: String): String? {
        return str.substring(str.lastIndexOf(".") + 1).toLowerCase()
    }

    /* renamed from: b */
    private fun m6543b(context: Context, uri: Uri): String {
        var str: String? = null
        if (uri.getScheme().equals("content")) {
            val query: Cursor? = context.getContentResolver().query(
                uri,
                null as Array<String?>?,
                null as String?,
                null as Array<String?>?,
                null as String?
            )
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        str = query.getString(query.getColumnIndex("_display_name"))
                    }
                } catch (th: Throwable) {
                    query.close()
                    throw th
                }
            }
            query!!.close()
        }
        if (str != null) {
            return str
        }
        val path: String? = uri.getPath()
        val lastIndexOf = path!!.lastIndexOf(47.toChar())
        return if (lastIndexOf != -1) path.substring(lastIndexOf + 1) else path
    }

    /* renamed from: b */
   /* fun m6544b(str: String): String? {
        return str.substring(str.lastIndexOf(C7745Vy.f29348g) + 1)
    }*/

    /* renamed from: c */
    private fun m6545c(str: String): Array<String> {
        var str = str
        val str2: String
        val lastIndexOf = str.lastIndexOf(".")
        if (lastIndexOf != -1) {
            val substring = str.substring(0, lastIndexOf)
            str2 = str.substring(lastIndexOf)
            str = substring
        } else {
            str2 = ""
        }
        return arrayOf(str, str2)
    }
}