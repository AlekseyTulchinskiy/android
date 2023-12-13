package com.example.checklist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.checklist.R
import com.example.checklist.databinding.DialogNewListBinding

object CreateNewListDialog {
    fun showDialog(context: Context, listener: Listener, name: String) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = DialogNewListBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            edNewList.setText(name)
            if(edNewList.text.isNotEmpty()) bCreate.setText(R.string.update)
            bCreate.setOnClickListener {
                if(edNewList.text.isNotEmpty()) {
                    listener.onClick(edNewList.text.toString())
                }
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(name: String)
    }
}