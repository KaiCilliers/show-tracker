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

package com.sunrisekcdeveloper.common

import timber.log.Timber

class TimberLogger(thisRef: Any, tag: String? = null): Timber.Tree() {

    private val tag = tag ?: thisRef.toTag()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Timber.tag(tag ?: this.tag).log(priority, t, message)
    }

    private fun Any.toTag(): String {
        val str = this::class.java.simpleName.run {
            if (endsWith("Contract")) substring(0, length - 4) else this
        }
        if (str.length <= 23) {
            return str
        }
        return str
            .replace("Repository", "Repo")
            .replace("Service", "Serv")
            .replace("UseCase", "UC")
            .replace("RemoteDataSource", "RemDaSo")
            .replace("Fragment", "Frag")
            .replace("ViewModel", "VM")
            .replace("BottomSheet", "BotSh")
            .replace("RemoteMediator", "RemMed")
            .replace("Activity", "Act")
            .take(23)
    }
}