package com.example.fitness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.fitness.R
import com.example.fitness.databinding.TemplateExerciseBinding
import pl.droidsonroids.gif.GifDrawable

class ExerciseAdapter: ListAdapter<ExerciseModel, ExerciseAdapter.ExerciseHolder>(MyComparator()) {

    class ExerciseHolder(view: View) : ViewHolder(view) {
        private val binding = TemplateExerciseBinding.bind(view)
        fun setData(exercise: ExerciseModel) = with(binding){
            tvName.text = exercise.name
            tvTime.text = exercise.time
            ivExGif.setImageDrawable(GifDrawable(root.context.assets, exercise.image))
            chB.isChecked = exercise.isDone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_exercise, parent, false)
        return ExerciseHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class MyComparator: DiffUtil.ItemCallback<ExerciseModel>() {
        override fun areItemsTheSame(oldItem: ExerciseModel, newItem: ExerciseModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ExerciseModel, newItem: ExerciseModel): Boolean {
            return oldItem == newItem
        }
    }
}