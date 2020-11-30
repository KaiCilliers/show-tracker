package com.sunrisekcdeveloper.showtracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sunrisekcdeveloper.showtracker.databinding.FragmentProgressBinding
import com.sunrisekcdeveloper.showtracker.util.click

class ProgressFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProgressBinding.inflate(inflater)
        binding.apply {
            tvProgress.click {
                findNavController().navigate(
                    ProgressFragmentDirections.toHome()
                )
            }
        }
        return binding.root
    }
}