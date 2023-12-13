package com.example.checklist.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.checklist.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_settings, rootKey)
    }
}