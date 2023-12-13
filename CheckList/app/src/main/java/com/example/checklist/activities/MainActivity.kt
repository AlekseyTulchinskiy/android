package com.example.checklist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.preference.PreferenceManager
import com.example.checklist.R
import com.example.checklist.databinding.ActivityMainBinding
import com.example.checklist.dialogs.AuthenticationDialog
import com.example.checklist.entities.User
import com.example.checklist.fragments.ListFragment
import com.example.checklist.fragments.NoteFragment
import com.example.checklist.utils.FragmentHelper
import com.example.checklist.utils.MainViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var defPref: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentTheme: String
    private var preFragment = R.id.note
    private var tvHeader: TextView? = null
    val mAuth = FirebaseAuth.getInstance()
    private val authenticationDialog = AuthenticationDialog(this)
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((this.applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        currentTheme = defPref.getString("topic_key", "dark").toString()
        setContentView(binding.root)
        setBottomNavListener()
        init()
    }

    override fun onStart() {
        super.onStart()
        uiUpgrade(mAuth.currentUser)
    }

    private fun init() = with(binding) {
        val toggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navMenu.setNavigationItemSelectedListener(this@MainActivity)
        tvHeader = binding.navMenu.getHeaderView(0).findViewById(R.id.tvMail)
    }

    private fun getSelectedTheme(): Int {
        return if (defPref.getString("topic_key", "dark") == "dark") {
            R.style.Theme_CheckListDarkMain
        } else {
            R.style.Theme_CheckListWhiteMain
        }
    }

    private fun setBottomNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                }
                R.id.note -> {
                    preFragment = PRE_FRAG_NOTE
                    FragmentHelper.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.item -> {
                    preFragment = PRE_FRAG_ToDoList
                    FragmentHelper.setFragment(ListFragment.newInstance(), this)
                }
                R.id.new_note -> {
                    FragmentHelper.currentFragment?.onClickNew()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = preFragment
        if (currentTheme != defPref.getString("topic_key", "dark")) recreate()
    }

    companion object {
        const val PRE_FRAG_NOTE = R.id.note
        const val PRE_FRAG_ToDoList = R.id.item
        const val SIGN_IN_BUTTON = 0
        const val SIGN_UP_BUTTON = 1
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_up -> {
                authenticationDialog.signInUpDialog(SIGN_UP_BUTTON)
            }
            R.id.sign_in -> {
                authenticationDialog.signInUpDialog(SIGN_IN_BUTTON)
            }
            R.id.sign_out -> {
                uiUpgrade(null)
                mAuth.signOut()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uiUpgrade(user: FirebaseUser?) {
        if (user == null) {
            tvHeader?.text = resources.getString(R.string.sign_in_or_sign_up)
        } else {
            tvHeader?.text = user.email
            addNewUser()
        }
    }

    private fun addNewUser() {
        mainViewModel.insertUser(
            User(
                null,
                tvHeader.toString()
            )
        )
    }
}