package com.example.checklist.utils

import android.widget.Toast
import com.example.checklist.R
import com.example.checklist.activities.MainActivity
import com.google.firebase.auth.FirebaseUser

class AccountHelper(private val act: MainActivity) {

    fun signUpAcc(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(task.result.user!!)
                    } else {
                        Toast.makeText(act, R.string.letter_mistake, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(act, R.string.fill_fields, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(act, R.string.letter_sent, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(act, R.string.letter_mistake, Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun signInAcc(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    act.uiUpgrade(task.result.user)
                    Toast.makeText(act, R.string.entrance_correct, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(act, R.string.enter_mistake, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(act, R.string.fill_fields, Toast.LENGTH_SHORT).show()
        }
    }
}