package com.example.william.my.module.sample.frame.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.base.BaseRecyclerFragment
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.frame.contract.TasksContract
import com.example.william.my.module.sample.frame.data.source.TasksRepository
import com.example.william.my.module.sample.frame.data.source.remote.TasksRemoteDataSource
import com.example.william.my.module.sample.frame.presenter.TasksPresenter

/**
 * MVP：Model-View-Presenter
 */
class MvpFragment : BaseRecyclerFragment<ArticleDetailBean?>(), TasksContract.View {

    private lateinit var tasksPresenter: TasksPresenter

    override fun getAdapter(): BaseQuickAdapter<ArticleDetailBean?, BaseViewHolder> {
        return ArticleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create the presenter
        tasksPresenter = TasksPresenter(TasksRepository.getInstance(TasksRemoteDataSource), this)

        queryData()
    }

    override fun queryData() {
        super.queryData()
        tasksPresenter.loadTasks(mPage)
    }

    override fun showTasks(tasks: List<ArticleDetailBean>) {
        onDataSuccess(tasks)
    }

    override fun showNoTasks() {
        onDataFail()
    }
}