package com.example.checklist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.service.autofill.UserData
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.checklist.R
import com.example.checklist.databinding.ActivityNewNoteBinding
import com.example.checklist.entities.NoteItem
import com.example.checklist.entities.User
import com.example.checklist.fragments.NoteFragment
import com.example.checklist.utils.HtmlManager
import com.example.checklist.utils.MyTouchListener
import com.example.checklist.utils.TimeHelper
import com.google.firebase.auth.FirebaseUser
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null
    private lateinit var defPref: SharedPreferences
    private var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(binding.root)

        binding.bSave.setOnClickListener {
            onClickSave()
        }
        actionBarSettings()
        editNote()
        setTextSize()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.style_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.bold -> {
                setBoldStyle()
            }
            R.id.color -> {
                if (binding.colorPicker.isShown) {
                    closeColorPicker()
                } else {
                    openColorPicker()
                    moveColorPicker()
                    onClickColorListener()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun actionBarSettings() {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onClickSave() = with(binding) {
        var editState = "new"
        val tempNote: NoteItem? = if (note == null) {
            getNewNote()
        } else {
            editState = "update"
            updateNote()
        }
        val i = Intent().apply {
            putExtra(NoteFragment.NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, i)
        finish()
    }

    private fun getNewNote(): NoteItem = with(binding) {

        return NoteItem(
            null,
            edTitle.text.toString(),
            HtmlManager.toHtml(edDescription.text),
            TimeHelper.getCurrentTime(),
            "",
        )
    }

    private fun editNote() = with(binding) {
        val sNote = intent.getSerializableExtra(NoteFragment.NOTE_KEY)
        if (sNote != null) {
            note = intent.getSerializableExtra(NoteFragment.NOTE_KEY) as NoteItem
            edTitle.setText(note?.title)
            edDescription.setText(HtmlManager.getFromHtml(note!!.content)!!.trim())
        }
    }

    private fun updateNote(): NoteItem? = with(binding) {
        return note?.copy(
            title = edTitle.text.toString(),
            content = HtmlManager.toHtml(edDescription.text).trim()
        )
    }

    private fun setBoldStyle() = with(binding) {
        val startPosDesc = edDescription.selectionStart
        val endPosDesc = edDescription.selectionEnd
        val styleDesc = edDescription.text.getSpans(startPosDesc, endPosDesc, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null

        if (styleDesc.isNotEmpty()) {
            edDescription.text.removeSpan(styleDesc[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }

        edDescription.text.setSpan(
            boldStyle,
            startPosDesc,
            endPosDesc,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        edDescription.text.trim()
        edDescription.setSelection(startPosDesc)
    }

    private fun closeColorPicker() = with(binding) {
        val closeAnim =
            AnimationUtils.loadAnimation(this@NewNoteActivity, R.anim.close_color_picker)
        closeAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }
        })
        colorPicker.startAnimation(closeAnim)
    }

    private fun openColorPicker() = with(binding) {
        colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this@NewNoteActivity, R.anim.open_color_picker)
        colorPicker.startAnimation(openAnim)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun moveColorPicker() = with(binding) {
        colorPicker.setOnTouchListener(MyTouchListener())
    }

    private fun setColor(colorId: Int) = with(binding) {
        val startPosDesc = edDescription.selectionStart
        val endPosDesc = edDescription.selectionEnd
        val styleDesc =
            edDescription.text.getSpans(startPosDesc, endPosDesc, ForegroundColorSpan::class.java)

        if (styleDesc.isNotEmpty()) edDescription.text.removeSpan(styleDesc[0])

        edDescription.text.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this@NewNoteActivity,
                    colorId
                )
            ), startPosDesc, endPosDesc, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        edDescription.text.trim()

        edDescription.setSelection(startPosDesc)
    }

    private fun onClickColorListener() = with(binding) {
        ibBlack.setOnClickListener {
            setColor(R.color.black)
        }
        ibRed.setOnClickListener {
            setColor(R.color.red)
        }
        ibBlue.setOnClickListener {
            setColor(R.color.blue)
        }
        ibYellow.setOnClickListener {
            setColor(R.color.yellow)
        }
        ibGreen.setOnClickListener {
            setColor(R.color.green)
        }
        ibOrange.setOnClickListener {
            setColor(R.color.orange)
        }
    }

    private fun setTextSize() = with(binding){
        pref = PreferenceManager.getDefaultSharedPreferences(this@NewNoteActivity)
        edTitle.setTextSize(pref?.getString("title_key", "16"))
        edDescription.setTextSize(pref?.getString("description_key", "14"))
    }

    private fun EditText.setTextSize(size: String?) {
        if(size != null) {
            this.textSize = size.toFloat()
        }
    }

    private fun getSelectedTheme() : Int{
        return if(defPref.getString("topic_key", "dark") == "dark") {
            R.style.Theme_CheckListDark
        } else {
            R.style.Theme_CheckListWhite
        }
    }
}