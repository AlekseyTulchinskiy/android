package com.example.checklist.entities

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "shopping_list_names")
data class ShoppingListName(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "allItemCounter")
    val allItemCounter: Int,

    @ColumnInfo(name = "checkedItemsCounter")
    val checkedItemsCounter: Int,

    @ColumnInfo(name = "itemsIds")
    val itemsIds: String,
) : Serializable