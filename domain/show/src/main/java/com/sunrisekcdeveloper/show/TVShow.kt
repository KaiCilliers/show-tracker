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

package com.sunrisekcdeveloper.show

import com.sunrisekcdeveloper.show.valueobjects.Identification
import com.sunrisekcdeveloper.show.valueobjects.ImageUrl
import com.sunrisekcdeveloper.show.valueobjects.Meta
import com.sunrisekcdeveloper.show.valueobjects.Stats

data class TVShow(
    val identification: Identification,
    val images: ImageUrl,
    val stats: Stats,
    val meta: Meta
)