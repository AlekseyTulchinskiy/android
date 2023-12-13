package com.example.checklist.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checklist.R
import com.example.checklist.adapters.ToDoItemAdapter
import com.example.checklist.databinding.ActivityToDoListItemBinding
import com.example.checklist.dialogs.EditToDoItemDialog
import com.example.checklist.entities.LibraryItem
import com.example.checklist.entities.ShoppingListItem
import com.example.checklist.entities.ShoppingListName
import com.example.checklist.utils.MainViewModel

class ToDoListItemActivity : AppCompatActivity(), ToDoItemAdapter.Listener {
    private lateinit var binding: ActivityToDoListItemBinding
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }
    private var toDoListName: ShoppingListName? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ToDoItemAdapter? = null
    private lateinit var textWatcher: TextWatcher
    private lateinit var defPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToDoListItemBinding.inflate(layoutInflater)
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(binding.root)
        toDoListName = intent.getSerializableExtra(CURRENT_TO_DO_LIST_NAME) as ShoppingListName
        initRVToDoItemsList()
        observer()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_item_menu, menu)
        saveItem = menu!!.findItem(R.id.save)
        saveItem.isVisible = false
        val newItem = menu.findItem(R.id.add)
        edItem = newItem.actionView?.findViewById(R.id.edAddCase) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        textWatcher = onTextWatcher()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                addNewToDoItem(edItem?.text.toString())
            }
            R.id.delete_list -> {
                mainViewModel.deleteToDoListName(toDoListName?.id!!, DELETE_LIST)
                finish()
            }
            R.id.clear_list -> {
                mainViewModel.deleteToDoListName(toDoListName?.id!!, DELETE_ITEMS)
            }
        }
        return true
    }

    private fun onTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.getAllLibraryItems("%$p0%")
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }
    }

    private fun isItems(it: Boolean) = with(binding) {
        if(it) {
            tvEmpty.visibility = View.VISIBLE
        } else {
            tvEmpty.visibility = View.GONE
        }
    }

    private fun addNewToDoItem(name: String) {
        if (name.isEmpty()) return
        val item = ShoppingListItem(
            null,
            name,
            "",
            false,
            toDoListName?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertToDoItem(item)
    }

    private fun expandActionView(): OnActionExpandListener {
        return object : OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                saveItem.isVisible = true
                edItem?.addTextChangedListener(textWatcher)
                libraryObserver()
                mainViewModel.allToDoItemsList(toDoListName?.id!!).removeObservers(this@ToDoListItemActivity)
                mainViewModel.getAllLibraryItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                mainViewModel.libraryItem.removeObservers(this@ToDoListItemActivity)
                edItem?.setText("")
                observer()
                return true
            }
        }
    }

    private fun initRVToDoItemsList() = with(binding) {
        adapter = ToDoItemAdapter(this@ToDoListItemActivity)
        rvToDoItemsList.layoutManager = LinearLayoutManager(this@ToDoListItemActivity)
        rvToDoItemsList.adapter = adapter
    }

    private fun observer() {
        mainViewModel.allToDoItemsList(toDoListName?.id!!).observe(this) {
            adapter?.submitList(it)
            isItems(it.isEmpty())
        }
    }

    companion object {
        const val CURRENT_TO_DO_LIST_NAME = "current_to_do_list_name"
        const val DELETE_LIST = 0
        const val DELETE_ITEMS = 1
    }

    override fun onClickItem(toDoItem: ShoppingListItem, state: Int) {
        when(state) {
            ToDoItemAdapter.CHECKBOX_STATE -> {
                mainViewModel.updateToDoItemList(toDoItem)
            }
            ToDoItemAdapter.EDIT_STATE -> {
                editListItem(toDoItem)
            }
            ToDoItemAdapter.EDIT_LIBRARY_ITEM -> {
                editLibraryItem(toDoItem)
            }
            ToDoItemAdapter.DELETE_LIBRARY_ITEM -> {
                mainViewModel.deleteLibraryItem(toDoItem.id!!)
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }
            ToDoItemAdapter.ADD_LIBRARY_ITEM -> {
                addNewToDoItem(toDoItem.name)
            }
        }
    }

    private fun editLibraryItem(toDoItem: ShoppingListItem) {
        EditToDoItemDialog.showDialog(this, toDoItem, object : EditToDoItemDialog.Listener {
            override fun onClick(item: ShoppingListItem) {
                mainViewModel.updateLibraryItem(LibraryItem(item.id, name = item.name))
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }

        })
    }

    private fun editListItem(item: ShoppingListItem) {
        EditToDoItemDialog.showDialog(this, item, object : EditToDoItemDialog.Listener {
            override fun onClick(item: ShoppingListItem) {
                mainViewModel.updateToDoItemList(item)
            }
        })
    }

    private fun libraryObserver() {
        mainViewModel.libraryItem.observe(this) {
            val tempToDoList = ArrayList<ShoppingListItem>()
            it.forEach { item ->
                val toDoItem = ShoppingListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )
                tempToDoList.add(toDoItem)
            }
            adapter?.submitList(tempToDoList)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        getItemsCounter()
    }

    private fun getItemsCounter(){
        var tempCheckList = 0
        adapter?.currentList?.forEach {
            if (it.itemChecked) {
                tempCheckList++
            }
        }
        val tempToDoListName = toDoListName?.copy(
            allItemCounter = adapter?.itemCount!!,
            checkedItemsCounter = tempCheckList
        )
        mainViewModel.updateToDoListName(tempToDoListName!!)
    }

    private fun getSelectedTheme() : Int{
        return if(defPref.getString("topic_key", "dark") == "dark") {
            R.style.Theme_CheckListDark
        } else {
            R.style.Theme_CheckListWhite
        }
    }
}