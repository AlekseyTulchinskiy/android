package com.example.checklist.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.checklist.entities.*

@Database(
    entities = [LibraryItem::class, NoteItem::class, ShoppingListItem::class, ShoppingListName::class, User::class],
    version = 2,
    exportSchema = true, autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class MainDataBase : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context): MainDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, MainDataBase::class.java, "shopping_list.db"
                ).build()
                instance
            }
        }
    }
}