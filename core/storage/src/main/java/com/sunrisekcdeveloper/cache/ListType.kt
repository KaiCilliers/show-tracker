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

package com.sunrisekcdeveloper.cache

// Lint false positive
// TODO lint is giving a false positive when a sealed class is used in DiffUtil without overriding equals function. Changed lint check from error to warning Settings -> Editor -> Inspections -> Suspicious DiffUtil Equality. Change back once IDE is updated to latest version as the bug has been fixed: http://youtrack.jetbrains.com/issues/KT-31239
sealed class ListType {
    object NoList : ListType()
    object MoviePopular : ListType()
    object MovieTopRated : ListType()
    object MovieUpcoming : ListType()
    object ShowPopular : ListType()
    object ShowTopRated : ListType()
    object ShowAiringToday : ListType()

    companion object {
        fun noList() = NoList
        fun moviePopular() = MoviePopular
        fun movieTopRated() = MovieTopRated
        fun movieUpcoming() = MovieUpcoming
        fun showPopular() = ShowPopular
        fun showTopRated() = ShowTopRated
        fun showAiringToday() = ShowAiringToday
    }
}