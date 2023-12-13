package com.example.checklist.utils

import androidx.lifecycle.*
import com.example.checklist.activities.ToDoListItemActivity
import com.example.checklist.db.MainDataBase
import com.example.checklist.entities.*
import kotlinx.coroutines.launch

class MainViewModel(dataBase: MainDataBase) : ViewModel() {
    private val dao = dataBase.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShopListNames: LiveData<List<ShoppingListName>> = dao.getAllShopListNames().asLiveData()
    val libraryItem = MutableLiveData<List<LibraryItem>>()
    fun allToDoItemsList(listId: Int): LiveData<List<ShoppingListItem>> {
        return dao.getAllToDoItemsList(listId).asLiveData()
    }

    fun getAllLibraryItems(name: String) = viewModelScope.launch {
        libraryItem.postValue(dao.getAllLibraryItems(name))
    }

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertUser(user: User) = viewModelScope.launch {
        dao.insertUser(user)
    }

    fun insertToDoItem(toDoItem: ShoppingListItem) = viewModelScope.launch {
        dao.insertToDoListItem(toDoItem)
        if(!isLibraryItemExists(toDoItem.name)) dao.insertLibraryItem(LibraryItem(null, toDoItem.name))
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteLibraryItem(id: Int) = viewModelScope.launch {
        dao.deleteLibraryItem(id)
    }

    fun deleteToDoListName(id: Int, state: Int) = viewModelScope.launch {
        if(state == ToDoListItemActivity.DELETE_LIST) {
            dao.deleteToDoListName(id)
        }
        dao.deleteToDoItem(id)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun updateToDoListName(toDoListName: ShoppingListName) = viewModelScope.launch {
        dao.updateToDoListName(toDoListName)
    }

    fun updateToDoItemList(toDoItemList: ShoppingListItem) = viewModelScope.launch {
        dao.updateToDoItemList(toDoItemList)
    }

    fun updateLibraryItem(item: LibraryItem) = viewModelScope.launch {
        dao.updateLibraryItem(item)
    }

    fun insertShopListName(shopList: ShoppingListName) = viewModelScope.launch {
        dao.insertShopListName(shopList)
    }

    private suspend fun isLibraryItemExists(name: String): Boolean {
        return dao.getAllLibraryItems(name).isNotEmpty()
    }

    class MainViewModelFactory(private val database: MainDataBase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw java.lang.IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}