package com.example.checklist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.checklist.R
import com.example.checklist.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    private lateinit var defPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(R.layout.activity_settings)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.settingsPlaceHolder, SettingsFragment()).commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSelectedTheme() : Int{
        return if(defPref.getString("topic_key", "dark") == "dark") {
            R.style.Theme_CheckListDark
        } else {
            R.style.Theme_CheckListWhite
        }
    }
}