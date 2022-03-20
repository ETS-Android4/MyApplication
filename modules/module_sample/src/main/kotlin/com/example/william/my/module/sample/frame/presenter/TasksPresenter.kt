/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.william.my.module.sample.frame.presenter

import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.module.sample.frame.contract.TasksContract
import com.example.william.my.module.sample.frame.data.source.TasksDataSource
import com.example.william.my.module.sample.frame.data.source.TasksRepository

/**
 * Listens to user actions from the UI ([TasksFragment]), retrieves the data and updates the
 * UI as required.
 */
class TasksPresenter(private val tasksRepository: TasksRepository, val tasksView: TasksContract.View) : TasksContract.Presenter {

    override fun loadTasks(page: Int) {
        tasksRepository.getTasks(page, object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: ArticleDataBean) {
                tasksView.showTasks(tasks.datas)
            }

            override fun onDataNotAvailable() {
                tasksView.showNoTasks()
            }
        })
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }
}
