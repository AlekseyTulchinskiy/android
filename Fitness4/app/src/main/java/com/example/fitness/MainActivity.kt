package com.example.fitness

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.fitness.fragments.DayFragment
import com.example.fitness.utils.FragmentManager
import com.example.fitness.utils.MainViewModel

class MainActivity : AppCompatActivity() {
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FragmentManager.setFragment(DayFragment.newInstance(), this)

        model.pref = getSharedPreferences("main", MODE_PRIVATE)
    }

    override fun onBackPressed() {
        if(FragmentManager.currentFragment is DayFragment) {
            super.onBackPressed()
        } else {
            FragmentManager.setFragment(DayFragment.newInstance(), this)
        }
    }
}