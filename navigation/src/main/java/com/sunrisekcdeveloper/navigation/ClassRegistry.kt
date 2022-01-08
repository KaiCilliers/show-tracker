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

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

internal object ClassRegistry {
    private val CLASS_MAP = ConcurrentHashMap<String, Class<*>>()

    @JvmStatic
    fun <T : Activity> loadActivityOrThrow(className: String): Class<T> =
        loadClassOrThrow(className, Activity::class)

    @JvmStatic
    fun <T : Fragment> loadFragmentOrThrow(className: String): Class<T> =
        loadClassOrThrow(className, Fragment::class)

    @JvmStatic
    fun <T : Service> loadServiceOrThrow(className: String): Class<T> =
        loadClassOrThrow(className)

    @JvmStatic
    fun <T : BroadcastReceiver> loadReceiverOrThrow(className: String): Class<T> =
        loadClassOrThrow(className)

    @Suppress("UNCHECKED_CAST")
    fun <T> loadClassOrNull(className: String): Class<T>? {
        return CLASS_MAP.getOrPut(className) {
            try {
                Class.forName(className)
            } catch (e: ClassNotFoundException) {
                // Can't store a null value in the concurrent map
                return null
            }
        } as? Class<T>
    }

    @Throws(ClassNotFoundException::class)
    private fun <T> loadClassOrThrow(className: String, type: KClass<*>? = null): Class<T> =
        loadClassOrNull(className) ?: throw ClassNotFoundException("Class not found $className")

    /** Given a FQN class name, returns the simple name. */
    fun simpleName(className: String) = className.split(".").lastOrNull()
}

fun loadFragment(className: String, args: Bundle? = null): Fragment =
    ClassRegistry.loadFragmentOrThrow<Fragment>(className).newInstance().apply { arguments = args }

fun loadIntent(className: String): Intent =
    Intent(Intent.ACTION_VIEW).setClassName(BuildConfig.RootPackage, className)