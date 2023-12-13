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
import com.example.fitness.R
import com.example.fitness.adapters.ExerciseModel
import com.example.fitness.databinding.FragmentTrainingBinding
import com.example.fitness.utils.FragmentManager
import com.example.fitness.utils.MainViewModel
import com.example.fitness.utils.TimerManager
import pl.droidsonroids.gif.GifDrawable

class TrainingFragment : Fragment() {
    private lateinit var binding: FragmentTrainingBinding
    private var timer: CountDownTimer? = null
    private val model: MainViewModel by activityViewModels()
    private lateinit var exList: ArrayList<ExerciseModel>
    private var currentEx = 0
    private var currentDay = 0
    private var ab: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentEx = model.getExercisesDone()
        currentDay = model.currentDay
        model.mutableListDay.observe(viewLifecycleOwner) {
            exList = it
            setExercise()
        }
    }

    private fun setExercise() {
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = "${currentEx + 1}/ ${exList.size}"
        if(currentEx < exList.size) {
            timer?.cancel()
            val ex = exList[currentEx++]
            setCurrentExercise(ex)
            setNextExercise()
            isTimeOrCount(ex)
            binding.bNext.setOnClickListener {
                setExercise()
            }
        } else {
            currentEx++
            FragmentManager.setFragment(FinishFragment.newInstance(), activity as AppCompatActivity)
        }
    }

    private fun isTimeOrCount(exercise: ExerciseModel) = with(binding){
        if(exercise.time.startsWith("x")) {
            pbTimer.visibility = View.GONE
            tvTime.text = exercise.time
            bNext.text = resources.getString(R.string.ready)
        } else {
            bNext.text = resources.getString(R.string.throw_exercise)
            pbTimer.visibility = View.VISIBLE
            startTimer(exercise)
        }
    }

    private fun startTimer(exercise: ExerciseModel) = with(binding) {
        tvTime.text = exercise.time
        val time = exercise.time.toLong() * 1000
        pbTimer.max = time.toInt()
        timer = object : CountDownTimer(time, 1) {
            override fun onTick(p0: Long) {
                pbTimer.progress = p0.toInt()
                tvTime.text = TimerManager.setFormat(p0 + 900)
            }

            override fun onFinish() {
                setExercise()
            }
        }.start()
    }

    private fun setNextExercise() = with(binding) {
        if(currentEx < exList.size) {
            val nextEx = exList[currentEx]
            tvNextExName.text = nextEx.name
            ivNextExGif.setImageDrawable(GifDrawable(root.context.assets, nextEx.image))
        } else {
            ivNextExGif.setImageDrawable(GifDrawable(root.context.assets, "winning-trump.gif"))
            tvNextExName.text = resources.getString(R.string.ready)
        }
    }

    private fun setCurrentExercise(exercise: ExerciseModel) = with(binding) {
        tvCurExName.text = exercise.name
        ivCurExGif.setImageDrawable(GifDrawable(root.context.assets, exercise.image))
    }

    override fun onDetach() {
        super.onDetach()
        timer?.cancel()
        model.savePref(currentDay.toString(), currentEx - 1)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrainingFragment()
    }
}