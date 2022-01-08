/*
 * Copyright © 2021. The Android Open Source Project
 *
 * @author Kai Cilliers
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunrisekcdeveloper.showtracker

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.showtracker.databinding.FragmentSecondMoviesBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieFragment : Fragment() {

    private var _binding: FragmentSecondMoviesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.wtf("create view movie")
        _binding = FragmentSecondMoviesBinding.inflate(inflater, container, false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        val newId = 101

        if (menu?.findItem(newId) == null) {
            val newMenuItem = menu?.add(
                Menu.NONE,
                newId,
                0,
                "dynamic"
            )
            newMenuItem?.setIcon(R.drawable.ic_baseline_filter_list_24)
            newMenuItem?.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            newMenuItem?.setOnMenuItemClickListener {
                toaster(it.itemId)
                true
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun toaster(k: Int) {
        Toast.makeText(requireContext(), "tttttt $k", Toast.LENGTH_SHORT).show()
    }
}