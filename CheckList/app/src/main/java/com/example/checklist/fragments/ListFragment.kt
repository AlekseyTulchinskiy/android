package com.example.checklist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checklist.activities.MainApp
import com.example.checklist.activities.ToDoListItemActivity
import com.example.checklist.adapters.ListToDoAdapter
import com.example.checklist.databinding.FragmentListBinding
import com.example.checklist.dialogs.CreateNewListDialog
import com.example.checklist.dialogs.DeleteToDoListDialog
import com.example.checklist.entities.ShoppingListName
import com.example.checklist.entities.User
import com.example.checklist.utils.MainViewModel
import com.example.checklist.utils.TimeHelper

class ListFragment : BaseFragment(), ListToDoAdapter.Listener {
    private lateinit var binding: FragmentListBinding
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }
    private lateinit var adapter: ListToDoAdapter
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
    }

    override fun onClickNew() {
        CreateNewListDialog.showDialog(activity as AppCompatActivity, object : CreateNewListDialog.Listener {
            override fun onClick(name: String) {
                val shopListName = ShoppingListName(
                    null,
                    name,
                    TimeHelper.getCurrentTime(),
                    0,
                    0,
                    "",
                )
                mainViewModel.insertShopListName(shopListName)
            }
        }, "")
    }

    private fun observer() {
        mainViewModel.allShopListNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        initRVToDoList()
    }

    private fun initRVToDoList() = with(binding){
        adapter = ListToDoAdapter(this@ListFragment)
        rvToDoList.layoutManager = LinearLayoutManager(activity)
        rvToDoList.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()
    }

    override fun onClickItem(toDoListName: ShoppingListName) {
        val i = Intent(activity, ToDoListItemActivity::class.java).apply {
            putExtra(ToDoListItemActivity.CURRENT_TO_DO_LIST_NAME, toDoListName)
        }
        startActivity(i)
    }

    override fun deleteToDoList(id: Int) {
        DeleteToDoListDialog.showDialog(context as AppCompatActivity, object : DeleteToDoListDialog.Listener {
            override fun onClick() {
                mainViewModel.deleteToDoListName(id, ToDoListItemActivity.DELETE_LIST)
            }
        })
    }

    override fun updateToDoListName(toDoListName: ShoppingListName) {
        CreateNewListDialog.showDialog(context as AppCompatActivity, object : CreateNewListDialog.Listener {
            override fun onClick(name: String) {
                mainViewModel.updateToDoListName(toDoListName.copy(name = name))
            }
        }, toDoListName.name)
    }
}