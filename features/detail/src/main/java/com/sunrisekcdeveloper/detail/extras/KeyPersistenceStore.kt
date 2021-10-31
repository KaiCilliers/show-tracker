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

package com.sunrisekcdeveloper.detail.extras

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

class KeyPersistenceStore(private val key: String) : KeyPersistenceStoreContract<String> {
    override fun value(): String = key

    override fun asDataStoreKey(): Preferences.Key<String> = stringPreferencesKey(key)
}

interface KeyPersistenceStoreContract<T> {
    fun value(): T
    fun asDataStoreKey(): Preferences.Key<T>
}