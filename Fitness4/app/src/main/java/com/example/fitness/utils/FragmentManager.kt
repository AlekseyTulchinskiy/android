package com.example.fitness.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fitness.R

object FragmentManager {
    var currentFragment: Fragment? = null
    fun setFragment(newFragment: Fragment, act: AppCompatActivity) {
        val transaction = act.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.actMain, newFragment)
        transaction.commit()

        currentFragment = newFragment
    }
}