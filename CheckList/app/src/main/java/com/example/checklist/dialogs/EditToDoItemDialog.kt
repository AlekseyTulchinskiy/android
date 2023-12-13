package com.example.checklist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.checklist.databinding.DialogEditItemBinding
import com.example.checklist.entities.ShoppingListItem

object EditToDoItemDialog {
    fun showDialog(context: Context, item: ShoppingListItem, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val binding = DialogEditItemBinding.inflate(LayoutInflater.from(context.applicationContext))
        builder.setView(binding.root)
        val dialog = builder.create()

        binding.apply {
            edNameItem.setText(item.name)
            edDescriptionItem.setText(item.itemInfo)
            if(item.itemType == 1) edDescriptionItem.visibility = View.GONE
            bCancel.setOnClickListener {
                dialog.dismiss()
            }
            bEdit.setOnClickListener {
                listener.onClick(item.copy(name = edNameItem.text.toString(), itemInfo = edDescriptionItem.text.toString()))
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    interface Listener {
        fun onClick(item: ShoppingListItem)
    }
}