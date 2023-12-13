package com.example.checklist.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.checklist.activities.MainActivity
import com.example.checklist.activities.MainApp
import com.example.checklist.activities.NewNoteActivity
import com.example.checklist.adapters.NoteAdapter
import com.example.checklist.databinding.FragmentNoteBinding
import com.example.checklist.entities.NoteItem
import com.example.checklist.utils.MainViewModel

class NoteFragment : BaseFragment(), NoteAdapter.Listener {
    private lateinit var binding: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NoteAdapter
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }
    private lateinit var defPref: SharedPreferences

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRVNotes()
        observer()
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val editState = it.data?.getStringExtra(EDIT_STATE_KEY)
                if(editState == "update") {
                    mainViewModel.updateNote(it.data?.getSerializableExtra(NOTE_KEY) as NoteItem)
                } else {
                    mainViewModel.insertNote(it.data?.getSerializableExtra(NOTE_KEY) as NoteItem)
                }
            }
        }
    }

    private fun initRVNotes() = with(binding) {
        defPref = PreferenceManager.getDefaultSharedPreferences(activity as AppCompatActivity)
        rvNotes.layoutManager = layoutManager()
        adapter = NoteAdapter(this@NoteFragment, defPref)
        rvNotes.adapter = adapter
    }

    private fun layoutManager() : RecyclerView.LayoutManager {
        return if(defPref.getString("style_key", "Grid") == "Grid") {
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        } else {
            LinearLayoutManager(activity)
        }
    }

    private fun observer() {
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    companion object {
        const val NOTE_KEY = "note_key"
        const val EDIT_STATE_KEY = "edit_state_key"

        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }

    override fun onClickItem(note: NoteItem) {
        val intent = Intent(activity, NewNoteActivity::class.java).apply {
            putExtra(NOTE_KEY, note)
        }
        editLauncher.launch(intent)
    }
}