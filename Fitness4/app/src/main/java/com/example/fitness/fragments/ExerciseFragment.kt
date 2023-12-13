package com.example.fitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitness.R
import com.example.fitness.adapters.ExerciseAdapter
import com.example.fitness.databinding.FragmentExerciseBinding
import com.example.fitness.utils.FragmentManager
import com.example.fitness.utils.MainViewModel

class ExerciseFragment : Fragment() {
    private lateinit var binding: FragmentExerciseBinding
    private lateinit var adapter: ExerciseAdapter
    private val model: MainViewModel by activityViewModels()
    private var ab: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRVExercises()
        model.mutableListDay.observe(viewLifecycleOwner) {
            for(i in 0 until model.getExercisesDone()) {
                it[i] = it[i].copy(isDone = true)
            }
            adapter.submitList(it)
        }
        binding.bStart.setOnClickListener {
            FragmentManager.setFragment(TimerFragment.newInstance(), activity as AppCompatActivity)
        }
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = resources.getString(R.string.exercises_list)
    }

    private fun initRVExercises() = with(binding) {
        adapter = ExerciseAdapter()
        rvExercises.layoutManager = LinearLayoutManager(activity as AppCompatActivity)
        rvExercises.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExerciseFragment()
    }
}