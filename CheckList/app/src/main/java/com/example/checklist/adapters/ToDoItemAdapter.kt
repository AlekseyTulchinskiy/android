package com.example.checklist.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.checklist.R
import com.example.checklist.databinding.TemplateToDoItemBinding
import com.example.checklist.databinding.TemplateToDoLibraryItemBinding
import com.example.checklist.dialogs.EditToDoItemDialog
import com.example.checklist.entities.ShoppingListItem
import com.example.checklist.utils.MainViewModel

class ToDoItemAdapter(private val listener: Listener) :
    ListAdapter<ShoppingListItem, ToDoItemAdapter.ToDoItemHolder>(MyComparator()) {
    class ToDoItemHolder(val view: View) : ViewHolder(view) {
        fun setData(item: ShoppingListItem, listener: Listener) {
            val binding = TemplateToDoItemBinding.bind(view)
            binding.apply {
                tvToDoItem.text = item.name
                tvDescription.text = item.itemInfo
                tvDescription.visibility = setTvDescription(item)
                chb.isChecked = item.itemChecked
                setFlagAndColor(binding, item)
                chb.setOnClickListener {
                    listener.onClickItem(item.copy(itemChecked = chb.isChecked), CHECKBOX_STATE)
                }
                ibEdit.setOnClickListener {
                    listener.onClickItem(item, EDIT_STATE)
                }
            }
        }

        fun setLibraryData(item: ShoppingListItem, listener: Listener) {
            val libraryBinding = TemplateToDoLibraryItemBinding.bind(view)
            libraryBinding.apply {
                tvToDoItem.text = item.name
                ibEdit.setOnClickListener {
                    listener.onClickItem(item, EDIT_LIBRARY_ITEM)
                }
                ibDelete.setOnClickListener {
                    listener.onClickItem(item, DELETE_LIBRARY_ITEM)
                }
                itemView.setOnClickListener {
                    listener.onClickItem(item, ADD_LIBRARY_ITEM)
                }
            }
        }

        private fun setTvDescription(item: ShoppingListItem): Int {
            return if (item.itemInfo.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        private fun setFlagAndColor(binding: TemplateToDoItemBinding, item: ShoppingListItem) {
            binding.apply {
                if(chb.isChecked) {
                    tvToDoItem.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvToDoItem.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey_light))
                    tvDescription.visibility = View.GONE
                } else {
                    tvToDoItem.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvToDoItem.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                    tvDescription.visibility = setTvDescription(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemHolder {
        return if (viewType == 0) {
            ToDoItemHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.template_to_do_item, parent, false)
            )
        } else {
            ToDoItemHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.template_to_do_library_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ToDoItemHolder, position: Int) {
        if (getItem(position).itemType == 0) {
            holder.setData(getItem(position), listener)
        } else {
            holder.setLibraryData(getItem(position), listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class MyComparator : DiffUtil.ItemCallback<ShoppingListItem>() {
        override fun areItemsTheSame(
            oldItem: ShoppingListItem,
            newItem: ShoppingListItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShoppingListItem,
            newItem: ShoppingListItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun onClickItem(toDoItem: ShoppingListItem, state: Int)
    }

    companion object {
        const val EDIT_STATE = 0
        const val CHECKBOX_STATE = 1
        const val EDIT_LIBRARY_ITEM = 2
        const val DELETE_LIBRARY_ITEM = 3
        const val ADD_LIBRARY_ITEM = 4
    }
}