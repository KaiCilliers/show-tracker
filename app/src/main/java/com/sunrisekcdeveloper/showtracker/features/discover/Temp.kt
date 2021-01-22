///*
// * Copyright 2016, The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.example.android.architecture.blueprints.todoapp.data.source
//
//import com.example.android.architecture.blueprints.todoapp.data.Task
//import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
//import java.util.*
//
///**
// * Concrete implementation to load tasks from the data sources into a cache.
// *
// *
// * For simplicity, this implements a dumb synchronisation between locally persisted data and data
// * obtained from the server, by using the remote data source only if the local database doesn't
// * exist or is empty.
// *
// * //TODO: Implement this class using LiveData.
// */
//class TasksRepository private constructor(
//    tasksRemoteDataSource: TasksDataSource,
//    tasksLocalDataSource: TasksDataSource
//) :
//    TasksDataSource {
//    private val mTasksRemoteDataSource: TasksDataSource
//    private val mTasksLocalDataSource: TasksDataSource
//
//    /**
//     * This variable has package local visibility so it can be accessed from tests.
//     */
//    var mCachedTasks: MutableMap<String, Task?>? = null
//
//    /**
//     * Marks the cache as invalid, to force an update the next time data is requested. This variable
//     * has package local visibility so it can be accessed from tests.
//     */
//    private var mCacheIsDirty = false
//
//    /**
//     * Gets tasks from cache, local data source (SQLite) or remote data source, whichever is
//     * available first.
//     *
//     *
//     * Note: [LoadTasksCallback.onDataNotAvailable] is fired if all data sources fail to
//     * get the data.
//     */
//    fun getTasks(callback: LoadTasksCallback) {
//        com.google.common.base.Preconditions.checkNotNull<Any>(callback)
//
//        // Respond immediately with cache if available and not dirty
//        if (mCachedTasks != null && !mCacheIsDirty) {
//            callback.onTasksLoaded(ArrayList<Any?>(mCachedTasks!!.values))
//            return
//        }
//        EspressoIdlingResource.increment() // App is busy until further notice
//        if (mCacheIsDirty) {
//            // If the cache is dirty we need to fetch new data from the network.
//            getTasksFromRemoteDataSource(callback)
//        } else {
//            // Query the local storage if available. If not, query the network.
//            mTasksLocalDataSource.getTasks(object : LoadTasksCallback() {
//                fun onTasksLoaded(tasks: List<Task>) {
//                    refreshCache(tasks)
//                    EspressoIdlingResource.decrement() // Set app as idle.
//                    callback.onTasksLoaded(ArrayList<Any?>(mCachedTasks!!.values))
//                }
//
//                fun onDataNotAvailable() {
//                    getTasksFromRemoteDataSource(callback)
//                }
//            })
//        }
//    }
//
//    fun saveTask(task: Task) {
//        com.google.common.base.Preconditions.checkNotNull<Any>(task)
//        mTasksRemoteDataSource.saveTask(task)
//        mTasksLocalDataSource.saveTask(task)
//
//        // Do in memory cache update to keep the app UI up to date
//        if (mCachedTasks == null) {
//            mCachedTasks = LinkedHashMap<String, Task?>()
//        }
//        mCachedTasks!![task.getId()] = task
//    }
//
//    fun completeTask(task: Task) {
//        com.google.common.base.Preconditions.checkNotNull<Any>(task)
//        mTasksRemoteDataSource.completeTask(task)
//        mTasksLocalDataSource.completeTask(task)
//        val completedTask = Task(task.getTitle(), task.getDescription(), task.getId(), true)
//
//        // Do in memory cache update to keep the app UI up to date
//        if (mCachedTasks == null) {
//            mCachedTasks = LinkedHashMap<String, Task?>()
//        }
//        mCachedTasks!![task.getId()] = completedTask
//    }
//
//    fun completeTask(taskId: String) {
//        com.google.common.base.Preconditions.checkNotNull<String>(taskId)
//        completeTask(getTaskWithId(taskId))
//    }
//
//    fun activateTask(task: Task) {
//        com.google.common.base.Preconditions.checkNotNull<Any>(task)
//        mTasksRemoteDataSource.activateTask(task)
//        mTasksLocalDataSource.activateTask(task)
//        val activeTask = Task(task.getTitle(), task.getDescription(), task.getId())
//
//        // Do in memory cache update to keep the app UI up to date
//        if (mCachedTasks == null) {
//            mCachedTasks = LinkedHashMap<String, Task?>()
//        }
//        mCachedTasks!![task.getId()] = activeTask
//    }
//
//    fun activateTask(taskId: String) {
//        com.google.common.base.Preconditions.checkNotNull<String>(taskId)
//        activateTask(getTaskWithId(taskId))
//    }
//
//    fun clearCompletedTasks() {
//        mTasksRemoteDataSource.clearCompletedTasks()
//        mTasksLocalDataSource.clearCompletedTasks()
//
//        // Do in memory cache update to keep the app UI up to date
//        if (mCachedTasks == null) {
//            mCachedTasks = LinkedHashMap<String, Task?>()
//        }
//        val it: MutableIterator<Map.Entry<String, Task?>> = mCachedTasks!!.entries.iterator()
//        while (it.hasNext()) {
//            val entry: Map.Entry<String, Task?> = it.next()
//            if (entry.value.isCompleted()) {
//                it.remove()
//            }
//        }
//    }
//
//    /**
//     * Gets tasks from local data source (sqlite) unless the table is new or empty. In that case it
//     * uses the network data source. This is done to simplify the sample.
//     *
//     *
//     * Note: [GetTaskCallback.onDataNotAvailable] is fired if both data sources fail to
//     * get the data.
//     */
//    fun getTask(taskId: String, callback: GetTaskCallback) {
//        com.google.common.base.Preconditions.checkNotNull<String>(taskId)
//        com.google.common.base.Preconditions.checkNotNull<Any>(callback)
//        val cachedTask: Task? = getTaskWithId(taskId)
//
//        // Respond immediately with cache if available
//        if (cachedTask != null) {
//            callback.onTaskLoaded(cachedTask)
//            return
//        }
//        EspressoIdlingResource.increment() // App is busy until further notice
//
//        // Load from server/persisted if needed.
//
//        // Is the task in the local data source? If not, query the network.
//        mTasksLocalDataSource.getTask(taskId, object : GetTaskCallback() {
//            fun onTaskLoaded(task: Task) {
//                // Do in memory cache update to keep the app UI up to date
//                if (mCachedTasks == null) {
//                    mCachedTasks = LinkedHashMap<String, Task?>()
//                }
//                mCachedTasks!![task.getId()] = task
//                EspressoIdlingResource.decrement() // Set app as idle.
//                callback.onTaskLoaded(task)
//            }
//
//            fun onDataNotAvailable() {
//                mTasksRemoteDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback() {
//                    fun onTaskLoaded(task: Task?) {
//                        if (task == null) {
//                            onDataNotAvailable()
//                            return
//                        }
//                        // Do in memory cache update to keep the app UI up to date
//                        if (mCachedTasks == null) {
//                            mCachedTasks = LinkedHashMap<String, Task?>()
//                        }
//                        mCachedTasks!![task.getId()] = task
//                        EspressoIdlingResource.decrement() // Set app as idle.
//                        callback.onTaskLoaded(task)
//                    }
//
//                    fun onDataNotAvailable() {
//                        EspressoIdlingResource.decrement() // Set app as idle.
//                        callback.onDataNotAvailable()
//                    }
//                })
//            }
//        })
//    }
//
//    fun refreshTasks() {
//        mCacheIsDirty = true
//    }
//
//    fun deleteAllTasks() {
//        mTasksRemoteDataSource.deleteAllTasks()
//        mTasksLocalDataSource.deleteAllTasks()
//        if (mCachedTasks == null) {
//            mCachedTasks = LinkedHashMap<String, Task?>()
//        }
//        mCachedTasks!!.clear()
//    }
//
//    fun deleteTask(taskId: String) {
//        mTasksRemoteDataSource.deleteTask(com.google.common.base.Preconditions.checkNotNull(taskId))
//        mTasksLocalDataSource.deleteTask(com.google.common.base.Preconditions.checkNotNull(taskId))
//        mCachedTasks!!.remove(taskId)
//    }
//
//    private fun getTasksFromRemoteDataSource(callback: LoadTasksCallback) {
//        mTasksRemoteDataSource.getTasks(object : LoadTasksCallback() {
//            fun onTasksLoaded(tasks: List<Task>) {
//                refreshCache(tasks)
//                refreshLocalDataSource(tasks)
//                EspressoIdlingResource.decrement() // Set app as idle.
//                callback.onTasksLoaded(ArrayList<Any?>(mCachedTasks!!.values))
//            }
//
//            fun onDataNotAvailable() {
//                EspressoIdlingResource.decrement() // Set app as idle.
//                callback.onDataNotAvailable()
//            }
//        })
//    }
//
//    private fun refreshCache(tasks: List<Task>) {
//        if (mCachedTasks == null) {
//            mCachedTasks = LinkedHashMap<String, Task?>()
//        }
//        mCachedTasks!!.clear()
//        for (task in tasks) {
//            mCachedTasks!![task.getId()] = task
//        }
//        mCacheIsDirty = false
//    }
//
//    private fun refreshLocalDataSource(tasks: List<Task>) {
//        mTasksLocalDataSource.deleteAllTasks()
//        for (task in tasks) {
//            mTasksLocalDataSource.saveTask(task)
//        }
//    }
//
//    private fun getTaskWithId(id: String): Task? {
//        com.google.common.base.Preconditions.checkNotNull<String>(id)
//        return if (mCachedTasks == null || mCachedTasks!!.isEmpty()) {
//            null
//        } else {
//            mCachedTasks!![id]
//        }
//    }
//
//    companion object {
//        @Volatile
//        private var INSTANCE: TasksRepository? = null
//
//        /**
//         * Returns the single instance of this class, creating it if necessary.
//         *
//         * @param tasksRemoteDataSource the backend data source
//         * @param tasksLocalDataSource  the device storage data source
//         * @return the [TasksRepository] instance
//         */
//        fun getInstance(
//            tasksRemoteDataSource: TasksDataSource,
//            tasksLocalDataSource: TasksDataSource
//        ): TasksRepository? {
//            if (INSTANCE == null) {
//                synchronized(TasksRepository::class.java) {
//                    if (INSTANCE == null) {
//                        INSTANCE =
//                            TasksRepository(tasksRemoteDataSource, tasksLocalDataSource)
//                    }
//                }
//            }
//            return INSTANCE
//        }
//
//        /**
//         * Used to force [.getInstance] to create a new instance
//         * next time it's called.
//         */
//        fun destroyInstance() {
//            INSTANCE = null
//        }
//    }
//
//    // Prevent direct instantiation.
//    init {
//        mTasksRemoteDataSource =
//            com.google.common.base.Preconditions.checkNotNull(tasksRemoteDataSource)
//        mTasksLocalDataSource =
//            com.google.common.base.Preconditions.checkNotNull(tasksLocalDataSource)
//    }
//}
//
///*
// * Copyright 2016, The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//import com.example.android.architecture.blueprints.todoapp.data.Task
//
//
///**
// * Main entry point for accessing tasks data.
// */
//interface TasksDataSource {
//    interface LoadTasksCallback {
//        fun onTasksLoaded(tasks: List<Task?>?)
//        fun onDataNotAvailable()
//    }
//
//    interface GetTaskCallback {
//        fun onTaskLoaded(task: Task?)
//        fun onDataNotAvailable()
//    }
//
//    fun getTasks(callback: LoadTasksCallback)
//    fun getTask(taskId: String, callback: GetTaskCallback)
//    fun saveTask(task: Task)
//    fun completeTask(task: Task)
//    fun completeTask(taskId: String)
//    fun activateTask(task: Task)
//    fun activateTask(taskId: String)
//    fun clearCompletedTasks()
//    fun refreshTasks()
//    fun deleteAllTasks()
//    fun deleteTask(taskId: String)
//}