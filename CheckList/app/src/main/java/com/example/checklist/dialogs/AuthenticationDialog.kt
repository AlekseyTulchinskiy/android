package com.example.checklist.dialogs

import android.app.AlertDialog
import com.example.checklist.R
import com.example.checklist.activities.MainActivity
import com.example.checklist.activities.MainActivity.Companion.SIGN_IN_BUTTON
import com.example.checklist.databinding.SignInUpDialogBinding
import com.example.checklist.utils.AccountHelper

class AuthenticationDialog(private val act: MainActivity) {
    private lateinit var binding: SignInUpDialogBinding
    private val accountHelper = AccountHelper(act)
    fun signInUpDialog(index: Int) {
        val builder = AlertDialog.Builder(act)
        binding = SignInUpDialogBinding.inflate(act.layoutInflater)
        builder.setView(binding.root)
        val dialog = builder.create()
        binding.apply {
            if (index == SIGN_IN_BUTTON) {
                tvSignInUp.text = act.resources.getString(R.string.entrance)
                bSignIn.text = act.resources.getString(R.string.sign_in)
                bSignIn.setOnClickListener {
                    accountHelper.signInAcc(edEmail.text.toString(), edPassword.text.toString())
                    dialog.dismiss()
                }
            } else {
                tvSignInUp.text = act.resources.getString(R.string.registration)
                bSignIn.text = act.resources.getString(R.string.sign_up)
                bSignIn.setOnClickListener {
                    accountHelper.signUpAcc(edEmail.text.toString(), edPassword.text.toString())
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }
}