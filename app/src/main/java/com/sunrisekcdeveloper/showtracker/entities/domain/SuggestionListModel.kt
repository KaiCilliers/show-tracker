/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.entities.domain

/**
 * Suggestion List Model is a domain object that represents a single list item that consists of
 * multiple data types, namely [MovieItem] and [HeaderItem]
 */
sealed class SuggestionListModel {
    /**
     * Movie Item encapsulates a single [Movie] object
     *
     * @property movie
     */
    data class MovieItem(val movie: Movie) : SuggestionListModel()

    /**
     * Header Item encapsulates a header title
     *
     * @property name of header
     */
    data class HeaderItem(val name: String) : SuggestionListModel()
}