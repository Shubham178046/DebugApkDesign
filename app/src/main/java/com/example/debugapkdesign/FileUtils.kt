package com.example.debugapkdesign

import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat

object FileUtils {
    fun getSize(j: Long): String? {
        if (j <= 0) {
            return "0"
        }
        val d = j.toDouble()
        val log10 = (Math.log10(d) / Math.log10(1024.0)).toInt()
        val sb = StringBuilder()
        val decimalFormat = DecimalFormat("#,##0.#")
        val pow = Math.pow(1024.0, log10.toDouble())
        java.lang.Double.isNaN(d)
        sb.append(decimalFormat.format(d / pow))
        sb.append(" ")
        sb.append(arrayOf("B", "KB", "MB", "GB", "TB")[log10])
        return sb.toString()
    }
    fun getDate(str: String?): String? {
        var str2: String?
        return try {
            val simpleDateFormat = SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy")
            val simpleDateFormat2 = SimpleDateFormat("MM")
            val simpleDateFormat3 = SimpleDateFormat("dd, yyyy, hh:MM a")
            var str3: String? = null
            try {
                str2 = simpleDateFormat2.format(simpleDateFormat.parse(str))
                try {
                    str3 = simpleDateFormat3.format(simpleDateFormat.parse(str))
                } catch (e: ParseException) {
                    e.printStackTrace()
                    return getMonth(str2.toInt()).toString() + " " + str3
                }
            } catch (e2: ParseException) {
                str2 = null
                e2.printStackTrace()
                return getMonth(str2!!.toInt()).toString() + " " + str3
            }
            getMonth(str2.toInt()).toString() + " " + str3
        } catch (unused: Exception) {
            "N/F"
        }
    }
    fun getMonth(i: Int): String? {
        return when (i) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "Aug"
            9 -> "Sept"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> "Worng Months"
        }
    }
}