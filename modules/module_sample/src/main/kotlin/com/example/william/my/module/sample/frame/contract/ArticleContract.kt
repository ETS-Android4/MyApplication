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
package com.example.william.my.module.sample.frame.contract

import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.presenter.IBasePresenter
import com.example.william.my.library.view.IBaseView

/**
 * This specifies the contract between the view and the presenter.
 */
interface ArticleContract {

    interface View : IBaseView<Presenter> {

        fun showArticle(article: List<ArticleDetailBean>)

        fun showNoArticle()
    }

    interface Presenter : IBasePresenter {

        fun loadArticle(page: Int)
    }
}
