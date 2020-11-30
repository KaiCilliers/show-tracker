package com.sunrisekcdeveloper.showtracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSearchBinding
import com.sunrisekcdeveloper.showtracker.util.click

class SearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSearchBinding.inflate(inflater)
        binding.apply {
            tvSearch.click {
                findNavController().navigate(
                    SearchFragmentDirections.toHome()
                )
            }
        }
        return binding.root
    }
}