package com.example.checklist.activities

import android.app.Application
import com.example.checklist.db.MainDataBase

class MainApp : Application() {
    val database by lazy { MainDataBase.getDataBase(this) }
}