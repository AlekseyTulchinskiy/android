package com.example.fitness.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

object TimerManager {

    @SuppressLint("SimpleDateFormat")
    val formatter = SimpleDateFormat("mm:ss")

    fun setFormat(time: Long) : String {
        val cv = Calendar.getInstance()
        cv.timeInMillis = time
        return formatter.format(cv.time)
    }
}