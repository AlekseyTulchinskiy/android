package com.example.checklist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo("email", defaultValue = "")
    val email: String
) : java.io.Serializable
