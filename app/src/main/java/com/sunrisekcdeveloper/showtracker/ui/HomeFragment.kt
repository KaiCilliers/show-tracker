package com.sunrisekcdeveloper.showtracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sunrisekcdeveloper.showtracker.databinding.FragmentHomeBinding
import com.sunrisekcdeveloper.showtracker.util.click
import timber.log.Timber

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.apply {
            tvOther.click {
                findNavController().navigate(
                        HomeFragmentDirections.toSearch()
                )
            }
            tvBack.click {
                findNavController().navigate(
                        HomeFragmentDirections.toProgress()
                )
            }
        }
        return binding.root
    }
}