package com.example.fitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness.R
import com.example.fitness.databinding.FragmentFinishBinding
import com.example.fitness.utils.FragmentManager
import pl.droidsonroids.gif.GifDrawable

class FinishFragment : Fragment() {
    private lateinit var binding: FragmentFinishBinding
    private var ab: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivWinningGif.setImageDrawable(GifDrawable(resources.assets, "winning-trump.gif"))
        binding.bReady.setOnClickListener {
            FragmentManager.setFragment(DayFragment.newInstance(), activity as AppCompatActivity)
        }
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = resources.getString(R.string.ready)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FinishFragment()
    }
}