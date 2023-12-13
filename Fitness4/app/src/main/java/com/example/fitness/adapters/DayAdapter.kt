package com.example.fitness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.fitness.R
import com.example.fitness.databinding.TemplateDayBinding

class DayAdapter(var listener: Listener): ListAdapter<DayModel, DayAdapter.DayHolder>(MyComparator()) {

    class DayHolder(view: View) : ViewHolder(view) {
        private val binding = TemplateDayBinding.bind(view)
        fun setData(day: DayModel, listener: Listener) = with(binding){
            val dayNumber = root.context.getString(R.string.day) + " ${adapterPosition + 1}"
            tvDay.text = dayNumber

            val exCounter = root.context.getString(R.string.exercises_amount) + " " + day.exercise.split(",").size
            tvExCounter.text = exCounter
            checkBox.isChecked = day.isDone

            itemView.setOnClickListener {
                listener.onClick(day.copy(dayNumber = adapterPosition + 1))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_day, parent, false)
        return DayHolder(view)
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class MyComparator: DiffUtil.ItemCallback<DayModel>() {
        override fun areItemsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun onClick(day: DayModel)
    }
}