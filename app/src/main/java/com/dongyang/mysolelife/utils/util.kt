package com.dongyang.mysolelife.utils

import java.text.SimpleDateFormat
import java.util.*


// time.get
fun getTime(): String {
    val currentDateTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)

    return dateFormat
}