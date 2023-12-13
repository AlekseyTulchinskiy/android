package com.example.checklist.db

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.checklist.entities.*
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface Dao {
    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM shop_list_item WHERE listId LIKE :listId")
    fun getAllToDoItemsList(listId: Int): Flow<List<ShoppingListItem>>

    @Query("SELECT * FROM library WHERE name LIKE :name")
    suspend fun getAllLibraryItems(name: String): List<LibraryItem>

    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteToDoListName(id: Int)

    @Query("DELETE FROM shop_list_item WHERE listId LIKE :listId")
    suspend fun deleteToDoItem(listId: Int)

    @Query("DELETE FROM library WHERE id IS :id")
    suspend fun deleteLibraryItem(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertToDoListItem(toDoListItem: ShoppingListItem)

    @Insert
    suspend fun insertLibraryItem(libraryItem: LibraryItem)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Update
    suspend fun updateToDoListName(note: ShoppingListName)

    @Update
    suspend fun updateToDoItemList(toDoListItem: ShoppingListItem)

    @Update
    suspend fun updateLibraryItem(libraryItem: LibraryItem)

    @Insert
    suspend fun insertShopListName(name: ShoppingListName)

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShoppingListName>>
}