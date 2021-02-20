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

package com.sunrisekcdeveloper.showtracker.features.discover.domain.model

import com.google.gson.annotations.SerializedName

// todo merge into single envelope
//  issue is having variable media of type List<SealedClass>
//  retrofit does not know which implementation of the sealed class to use
//  possible solution is using a custom retrofit adapter
data class EnvelopePaginatedMovie(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val media: List<ResponseStandardMedia.ResponseMovie>,
    @SerializedName("total_pages") val pages: Int
)
data class EnvelopePaginatedShow(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val media: List<ResponseStandardMedia.ResponseShow>,
    @SerializedName("total_pages") val pages: Int
)