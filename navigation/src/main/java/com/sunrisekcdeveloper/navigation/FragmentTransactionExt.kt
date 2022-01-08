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

package com.sunrisekcdeveloper.navigation
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sunrisekcdeveloper.navigation.TransactionAnimations.*
import com.sunrisekcdeveloper.navigation.TransactionType.ADD_FRAGMENT
import com.sunrisekcdeveloper.navigation.TransactionType.REPLACE_FRAGMENT

/**
 * Extension for add or replace the fragment to the container.
 * [TransactionType.ADD_FRAGMENT] - Will put the [newFragment] on top of the current fragment.
 * [TransactionType.REPLACE_FRAGMENT] - Will replace the current fragment for the [newFragment] & the current fragment would not be visible.
 *
 * @param transactionType a [TransactionType] object.
 * @param newFragment the fragment to ADD or REPLACE.
 * @param addToBackStack if true then, the [newFragment] will be added to the back stack.
 * @param containerId the VIEW container id. The ID passed here it's supposed to be the ID of the View that contains the Fragment/s. Usually a FrameLayout or a FragmentContainerView.
 * @param transactionAnimations a [TransactionAnimations] object that will determine the animation to apply to the current transaction.
 */
internal fun FragmentTransaction.fragmentTransactionExt(
    transactionType: TransactionType,
    newFragment: Fragment,
    addToBackStack: Boolean,
    containerId: Int,
    fragmentTag: String,
    transactionAnimations: TransactionAnimations
) {
    Log.i(
        "TransactionExt",
        "transactionType: $transactionType | addToBackStack: $addToBackStack | containerId: $containerId | fragmentTag: $fragmentTag | transactionAnimations: $transactionAnimations"
    )
    when (transactionAnimations) {
        BOTTOM_TO_TOP -> {
//            setCustomAnimations(
//                R.anim.slide_in_from_bottom, R.anim.slide_out_from_top,
//                R.anim.slide_in_from_bottom, R.anim.slide_out_from_top
//            )
        }
        RIGHT_TO_LEFT -> {
            setCustomAnimations(
                R.anim.slide_in_right, R.anim.slide_out_left,
                R.anim.slide_in_left, R.anim.slide_out_right
            )
        }
        NONE -> {
            /* None transition animation is applied */
        }
    }

    when (transactionType) {
        ADD_FRAGMENT -> add(containerId, newFragment, fragmentTag)
        REPLACE_FRAGMENT -> replace(containerId, newFragment, fragmentTag)
    }

    if (addToBackStack) {
        addToBackStack(fragmentTag)
    }

    commit()
}