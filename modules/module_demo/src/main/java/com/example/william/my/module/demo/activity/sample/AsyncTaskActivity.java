package com.example.william.my.module.demo.activity.sample;

import android.os.AsyncTask;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

import java.lang.ref.WeakReference;

/**
 * AsyncTask
 */
@SuppressWarnings("deprecation")
@Route(path = ARouterPath.Demo.Demo_AsyncTask)
public class AsyncTaskActivity extends BaseResponseActivity {

    private MyAsyncTask mAsyncTask;

    /**
     * 第一个参数是doInBackground回调中传入的参数
     * 第二个参数是进度，onProgressUpdate的参数类型
     * 第三个参数是doInBackground返回值类型，onPostExecute传入的参数类型
     */
    private static class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {

        private final WeakReference<AsyncTaskActivity> weakReference;

        MyAsyncTask(AsyncTaskActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        /**
         * 在execute被调用后立即执行
         * 一般用来执行后台操作前对UI做一些标记
         */
        @Override
        protected void onPreExecute() {
            if (weakReference.get() != null) {
                weakReference.get().showResponse("onPreExecute");
            }
        }

        /**
         * 必须重写
         * AsyncTask的关键，用于执行耗时操作
         * 执行过程中可以调用publishProgress来更新进度信息
         */
        @Override
        protected Void doInBackground(Integer... params) {
            for (int i = 10; i <= 100; i += 10) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return null;
        }

        /**
         * 调用publishProgress时，此方法被执行
         * 将进度信息更新到UI组件
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            if (weakReference.get() != null) {
                weakReference.get().showResponse("Progress : " + values[0]);
            }
        }

        /**
         * 当后台操作结束时，此方法会被调用，
         * 将计算结果传递到此方法中，直接将结果显示到UI组件。
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            if (weakReference.get() != null) {
                weakReference.get().showResponse("onPostExecute");
            }
        }
    }

    @Override
    public void initView() {
        super.initView();
        mAsyncTask = new MyAsyncTask(AsyncTaskActivity.this);
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        mAsyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAsyncTask != null && mAsyncTask.isCancelled()
                && mAsyncTask.getStatus() == AsyncTask.Status.RUNNING)
            mAsyncTask.cancel(true);
    }
}