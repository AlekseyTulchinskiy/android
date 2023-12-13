package com.example.checklist.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.checklist.R
import com.example.checklist.databinding.TemplateToDoBinding
import com.example.checklist.entities.ShoppingListName

class ListToDoAdapter(private val listener: Listener): ListAdapter<ShoppingListName, ListToDoAdapter.ListToDoHolder>(MyComparator()) {
    class ListToDoHolder(view: View) : ViewHolder(view) {
        private val binding = TemplateToDoBinding.bind(view)
        fun setDate(listToDo: ShoppingListName, listener: Listener) = with(binding) {
            val itemCheckedCounter = "${listToDo.checkedItemsCounter}/${listToDo.allItemCounter}"
            tvCount.text = itemCheckedCounter
            if(listToDo.allItemCounter == listToDo.checkedItemsCounter) {
                tvCount.setBackgroundColor(root.context.getColor(R.color.green))
                pb.progressTintList = ColorStateList.valueOf(root.context.getColor(R.color.green))
            } else {
                tvCount.setBackgroundColor(root.context.getColor(R.color.grey_dark))
                pb.progressTintList = ColorStateList.valueOf(root.context.getColor(R.color.grey_dark))
            }
            tvListName.text = listToDo.name
            tvTime.text = listToDo.time
            pb.max = listToDo.allItemCounter
            pb.progress = listToDo.checkedItemsCounter
            ibDelete.setOnClickListener {
                listener.deleteToDoList(listToDo.id!!)
            }
            ibEdit.setOnClickListener {
                listener.updateToDoListName(listToDo)
            }
            itemView.setOnClickListener {
                listener.onClickItem(listToDo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListToDoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_to_do, parent, false)
        return ListToDoHolder(view)
    }

    override fun onBindViewHolder(holder: ListToDoHolder, position: Int) {
        holder.setDate(getItem(position), listener)
    }

    class MyComparator: DiffUtil.ItemCallback<ShoppingListName>() {
        override fun areItemsTheSame(
            oldItem: ShoppingListName,
            newItem: ShoppingListName
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShoppingListName,
            newItem: ShoppingListName
        ): Boolean {
            return newItem == oldItem
        }
    }

    interface Listener {
        fun onClickItem(toDoListName: ShoppingListName)
        fun deleteToDoList(id: Int)
        fun updateToDoListName(toDoListName: ShoppingListName)
    }
}