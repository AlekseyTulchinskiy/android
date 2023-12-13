package com.example.checklist.utils

import androidx.appcompat.app.AppCompatActivity
import com.example.checklist.fragments.BaseFragment
import com.example.checklist.R

object FragmentHelper {
    var currentFragment: BaseFragment? = null

    fun setFragment(newFragment: BaseFragment, act: AppCompatActivity) {
        val transaction = act.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.placeHolder, newFragment)
        transaction.commit()
        currentFragment = newFragment
    }
}