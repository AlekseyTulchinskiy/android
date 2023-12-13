package com.example.checklist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.checklist.databinding.DialogDeleteToDoListBinding

object DeleteToDoListDialog {
    fun showDialog(context: Context, listener: Listener) {
        val binding = DialogDeleteToDoListBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)
        val dialog = builder.create()
        binding.apply {
            bDelete.setOnClickListener {
                listener.onClick()
                dialog.dismiss()
            }
            bCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}