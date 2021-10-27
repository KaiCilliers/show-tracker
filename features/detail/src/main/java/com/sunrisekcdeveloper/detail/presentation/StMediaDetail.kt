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

package com.sunrisekcdeveloper.detail.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.sunrisekcdeveloper.detail.ImageLoadingContract
import com.sunrisekcdeveloper.detail.ImageLoadingStandardGlide
import com.sunrisekcdeveloper.detail.R
import com.sunrisekcdeveloper.detail.databinding.StMediaDetailsBinding
import com.sunrisekcdeveloper.detail.setMaxLinesToEllipsize
import com.sunrisekcdeveloper.models.EndpointImageContract
import com.sunrisekcdeveloper.models.EndpointPosterStandard

class StMediaDetail(
    private val ctxt: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int = 0,
    private val imageLoader: ImageLoadingContract = ImageLoadingStandardGlide(ctxt)
) : ConstraintLayout(ctxt, attributeSet, defStyleAttr) {

    private val binding: StMediaDetailsBinding =
        StMediaDetailsBinding.inflate(LayoutInflater.from(ctxt), this, true)

    init {
        addView(binding.root)
    }

    fun title(title: String) {
        binding.tvDetailMediaTitle.text = title
    }

    fun posterUrl(url: String) {
        // TODO do not create objects inside function (hides dependencies)
        //  Problem is this being a custom view
        imageLoader.load(EndpointPosterStandard(url).url(), binding.imgDetailMediaPoster)
    }

    fun description(description: String) {
        binding.tvDetailMediaDescription.text = description
        binding.tvDetailMediaDescription.setMaxLinesToEllipsize()
    }

    fun tags(tags: List<String>) {
        binding.tvDetailMediaTags.text = tags.joinToString(separator = " | ")
    }

    fun primaryButton(type: MediaDetailPrimaryButton, clickAction: OnClickListener) {
        binding.btnDetailMediaAddRemove.apply {
            text = when (type) {
                MediaDetailPrimaryButton.Add -> ctxt.getString(R.string.media_add)
                MediaDetailPrimaryButton.Remove -> ctxt.getString(R.string.remove)
            }
            setOnClickListener(clickAction)
        }
    }

    fun secondaryButton(type: MediaDetailSecondaryButton, clickAction: OnClickListener) {
        binding.btnDetailMediaWatchStatus.apply {
            text = when(type) {
                MediaDetailSecondaryButton.SetAsWatched -> ctxt.getString(R.string.mark_watched)
                MediaDetailSecondaryButton.StartWatching -> ctxt.getString(R.string.show_start_watching)
                MediaDetailSecondaryButton.UpToDate -> ctxt.getString(R.string.show_up_to_date)
                MediaDetailSecondaryButton.UpdateProgress -> ctxt.getString(R.string.show_update_progress)
                MediaDetailSecondaryButton.Watched -> ctxt.getString(R.string.watched)
            }
        }
        setOnClickListener(clickAction)
    }
}

sealed interface MediaDetailPrimaryButton {
    object Add : MediaDetailPrimaryButton
    object Remove : MediaDetailPrimaryButton
}

sealed interface MediaDetailSecondaryButton {
    object Watched : MediaDetailSecondaryButton
    object SetAsWatched : MediaDetailSecondaryButton
    object StartWatching : MediaDetailSecondaryButton
    object UpdateProgress : MediaDetailSecondaryButton
    object UpToDate : MediaDetailSecondaryButton
}
