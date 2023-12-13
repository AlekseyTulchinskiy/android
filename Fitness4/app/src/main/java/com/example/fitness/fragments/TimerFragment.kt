package com.example.fitness.fragments

import android.os.Bundle
import android.os.CountDownTimer
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
import com.example.fitness.databinding.FragmentTimerBinding
import com.example.fitness.utils.FragmentManager
import com.example.fitness.utils.MainViewModel
import com.example.fitness.utils.TimerManager

class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding
    private lateinit var timer: CountDownTimer
    private var ab: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pbTimer.max = TIMER_TO_START
        startTimer()
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = resources.getString(R.string.be_ready)
    }

    private fun startTimer() = with(binding) {
        timer = object : CountDownTimer(TIMER_TO_START.toLong(), 1) {
            override fun onTick(p0: Long) {
                pbTimer.progress = p0.toInt()
                tvTime.text = TimerManager.setFormat(p0 + 900)
            }

            override fun onFinish() {
                FragmentManager.setFragment(TrainingFragment.newInstance(), activity as AppCompatActivity)
            }
        }.start()
    }

    companion object {
        @JvmStatic
        fun newInstance() = TimerFragment()
        const val TIMER_TO_START = 3000
    }
}