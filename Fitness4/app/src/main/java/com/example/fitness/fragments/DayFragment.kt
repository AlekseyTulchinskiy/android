package com.example.fitness.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitness.R
import com.example.fitness.adapters.DayAdapter
import com.example.fitness.adapters.DayModel
import com.example.fitness.adapters.ExerciseModel
import com.example.fitness.databinding.FragmentDayBinding
import com.example.fitness.utils.DialogManager
import com.example.fitness.utils.FragmentManager
import com.example.fitness.utils.MainViewModel

class DayFragment : Fragment(), DayAdapter.Listener {
    private lateinit var binding: FragmentDayBinding
    private lateinit var adapter: DayAdapter
    private val model: MainViewModel by activityViewModels()
    private var ab: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.currentDay = 0
        initRVDays()
        adapter.submitList(fillDaysArray())
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = resources.getString(R.string.training_days)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.reset) {
            DialogManager.showDialog(
                activity as AppCompatActivity,
                R.string.reset_all_days,
                object : DialogManager.Listener {
                    override fun onClick() {
                        model.pref?.edit()?.clear()?.apply()
                        adapter.submitList(fillDaysArray())
                    }
                }
            )
        } else if(item.itemId == R.id.info) {
            FragmentManager.setFragment(InfoFragment.newInstance(), activity as AppCompatActivity)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initRVDays() = with(binding){
        adapter = DayAdapter(this@DayFragment)
        rvDays.layoutManager = LinearLayoutManager(activity as AppCompatActivity)
        rvDays.adapter = adapter
    }

    private fun fillDaysArray() : ArrayList<DayModel>{
        val tArray = ArrayList<DayModel>()
        var daysLeft = 0
        resources.getStringArray(R.array.days).forEach {
            model.currentDay++
            val exCounter = it.split(",").size
            tArray.add(DayModel(it, 0,model.getExercisesDone() == exCounter))
        }

        tArray.forEach {
            if(it.isDone) daysLeft++
        }
        fillProgress(tArray.size, daysLeft)
        return tArray
    }

    private fun fillProgress(sizeOfArray: Int, daysLeft: Int) = with(binding) {
        pbDaysLeft.max = sizeOfArray
        pbDaysLeft.progress = daysLeft
        var daysLeftToComplete = root.context.getString(R.string.days_left) + " ${sizeOfArray - daysLeft}"
        tvDaysLeft.text = daysLeftToComplete
    }

    private fun fillExercisesArray(day: DayModel) {
        val tArray = ArrayList<ExerciseModel>()
        day.exercise.split(",").forEach {
            val exArray = resources.getStringArray(R.array.exercises)
            val currentEx = exArray[it.toInt()]
            val exPart = currentEx.split("|")
            tArray.add(ExerciseModel(exPart[0], exPart[1], exPart[2], false))
        }

        model.mutableListDay.value = tArray
    }

    companion object {
        @JvmStatic
        fun newInstance() = DayFragment()
    }

    override fun onClick(day: DayModel) {
        if(!day.isDone) {
            fillExercisesArray(day)
            model.currentDay = day.dayNumber
            FragmentManager.setFragment(ExerciseFragment.newInstance(), activity as AppCompatActivity)
        } else {
            DialogManager.showDialog(
                activity as AppCompatActivity,
                R.string.reset_clicked_day,
                object : DialogManager.Listener {
                    override fun onClick() {
                        model.savePref(day.dayNumber.toString(), 0)
                        fillExercisesArray(day)
                        FragmentManager.setFragment(ExerciseFragment.newInstance(), activity as AppCompatActivity)
                    }
                }
            )
        }
    }
}