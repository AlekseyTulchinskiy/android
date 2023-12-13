package com.example.checklist.utils

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {
    const val DEFAULT_TIME_FORMAT = "hh:mm:ss - yyyy:MM:dd"
    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    fun getTimeFormat(time: String, defPref: SharedPreferences): String {
        val defFormatter = SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault())
        val defDate = defFormatter.parse(time)
        val chosenFormat = defPref.getString("time_key", DEFAULT_TIME_FORMAT)
        val chosenFormatter = SimpleDateFormat(chosenFormat, Locale.getDefault())
        return if(defDate != null) {
            chosenFormatter.format(defDate)
        } else {
            time
        }
    }
}