package com.example.william.my.module.sample.utils.callback

import androidx.lifecycle.MutableLiveData
import com.example.william.my.module.sample.utils.exception.KtApiException
import com.example.william.my.module.sample.utils.response.KtRetrofitResponse

/**
 * 携带状态 [State] 的 LiveData
 */
class KtLiveDataCallback<Bean>(private val liveData: MutableLiveData<KtRetrofitResponse<Bean>>) :
    KtRetrofitFlowCallback<KtRetrofitResponse<Bean>> {

    override fun onLoading() {
        this.liveData.postValue(KtRetrofitResponse.loading())
    }

    override fun onResponse(response: KtRetrofitResponse<Bean>) {
        try {
            liveData.postValue(response)
        } catch (e: Exception) {
            liveData.postValue(KtRetrofitResponse.error("数据异常"))
        }
    }

    override fun onFailure(e: KtApiException) {
        liveData.postValue(KtRetrofitResponse.error(e.message))
    }
}