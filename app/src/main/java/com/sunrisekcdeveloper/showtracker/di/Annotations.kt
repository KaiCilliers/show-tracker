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

package com.sunrisekcdeveloper.showtracker.common.util

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RepoProgress

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RepoDetail

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RepoDiscovery

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RepoWatchlist

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RepoSearch

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SourceProgress

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiProgress

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SourceDetail

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiDetail

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SourceDiscovery

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiDiscovery

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SourceSearch

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiSearch