package com.example.fitness.utils

import android.app.AlertDialog
import android.content.Context
import com.example.fitness.R

object DialogManager {
    fun showDialog(context: Context, im: Int, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        var dialog: AlertDialog? = null

        builder.setTitle(R.string.attention)
        builder.setMessage(im)
        builder.setPositiveButton(R.string.yes) { _,_ ->
            listener.onClick()
            dialog?.dismiss()
        }

        builder.setNegativeButton(R.string.cancel) { _,_ ->
            dialog?.dismiss()
        }

        dialog = builder.create()
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}