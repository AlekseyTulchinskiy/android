package com.example.fitness.utils

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitness.adapters.ExerciseModel

class MainViewModel: ViewModel() {
    val mutableListDay = MutableLiveData<ArrayList<ExerciseModel>>()
    var pref: SharedPreferences? = null
    var currentDay = 0

    fun savePref(key: String, value: Int) {
        pref?.edit()?.putInt(key, value)?.apply()
    }

    fun getExercisesDone(): Int {
        return pref?.getInt(currentDay.toString(), 0) ?: 0
    }
}