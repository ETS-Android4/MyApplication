package com.example.william.my.module.sample.utils.callback

import androidx.lifecycle.MutableLiveData
import com.example.william.my.module.sample.utils.exception.KtApiException
import com.example.william.my.module.sample.utils.response.KtRetrofitResponse

/**
 * 携带状态 [State] 的 LiveData
 */
class KtLiveDataCallback<Bean, Data> : KtRetrofitFlowCallback<KtRetrofitResponse<Bean>> {

    var convert: LiveDataConvert<Bean, Data>? = null
    private val liveData: MutableLiveData<KtRetrofitResponse<Data>>

    /**
     * @param liveData Data
     */
    constructor(liveData: MutableLiveData<KtRetrofitResponse<Data>>) {
        this.liveData = liveData
        this.liveData.postValue(KtRetrofitResponse.loading())
    }

    /**
     * @param liveData Data
     */
    constructor(
        liveData: MutableLiveData<KtRetrofitResponse<Data>>,
        convert: LiveDataConvert<Bean, Data>
    ) {
        this.liveData = liveData
        this.convert = convert
        this.liveData.postValue(KtRetrofitResponse.loading())
    }

    override fun onResponse(response: KtRetrofitResponse<Bean>) {
        try {
            liveData.postValue(
                if (convert == null)
                    response as KtRetrofitResponse<Data> else convert!!.onResponse(response)
            )
        } catch (e: Exception) {
            liveData.postValue(KtRetrofitResponse.error("数据异常"))
        }
    }

    override fun onFailure(e: KtApiException) {
        liveData.postValue(KtRetrofitResponse.error(e.message))
    }

    interface LiveDataConvert<Bean, Data> {
        @Throws(Exception::class)
        fun onResponse(data: KtRetrofitResponse<Bean>): KtRetrofitResponse<Data>
    }
}