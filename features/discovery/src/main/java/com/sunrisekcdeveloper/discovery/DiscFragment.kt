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

package com.sunrisekcdeveloper.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.discovery.databinding.FragmentThirdDiscoveryBinding
import com.sunrisekcdeveloper.navigation.TransactionAnimations
import com.sunrisekcdeveloper.navigation.loadFragment
import com.sunrisekcdeveloper.navigation.replaceFragmentExt
import timber.log.Timber

class DiscFragment : Fragment() {
    private var _binding: FragmentThirdDiscoveryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.wtf("create disc")
        _binding = FragmentThirdDiscoveryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textviewSecond.setOnClickListener {
            replaceFragmentExt(
                newFragment = loadFragment("com.sunrisekcdeveloper.detail.TempSecFragment").apply {
                    arguments = bundleOf("cool" to "SMILES")
                },
                addToBackStack = true,
                transactionAnimations = TransactionAnimations.RIGHT_TO_LEFT
            )
//            loadFragment("com.sunrisekcdeveloper.detail.TempSecFragment")
//                .apply {
//                    arguments = bundleOf("cool" to "SMILES")
//                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.wtf("destroy view desc")
        _binding = null
    }

    override fun onDestroy() {
        Timber.wtf("destroy desc")
        super.onDestroy()
    }
}