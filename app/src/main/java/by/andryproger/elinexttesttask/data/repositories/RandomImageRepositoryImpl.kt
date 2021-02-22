package by.andryproger.elinexttesttask.data.repositories

import by.andryproger.elinexttesttask.domain.gateways.repository.RandomImageRepository
import okhttp3.*
import java.io.IOException

class RandomImageRepositoryImpl(
    private val okHttpClient: OkHttpClient
) : RandomImageRepository {

    override fun get(onResult: (String?, Throwable?) -> Unit) {

        val requestHead = Request.Builder()
            .url(LOREM_FLICKR)
            .head()
            .build()

        okHttpClient.newCall(requestHead)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onResult(null, e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        onResult(response.request().url().url().toString(), null)
                    } else {
                        onResult(null, null)
                    }
                }
            })
    }

    companion object {
        const val LOREM_FLICKR = "http://loremflickr.com/200/200/"
    }

}