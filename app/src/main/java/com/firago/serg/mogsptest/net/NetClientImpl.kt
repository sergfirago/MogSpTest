package com.firago.serg.mogsptest.net

import com.firago.serg.mogsptest.data.NetClient
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * implementation of the interface NetClient using OkHttpClient
 */
class NetClientImpl: NetClient {
    private val client = OkHttpClient()

    override fun get(url: String): String {
        return client.get(url)
    }
}





private fun OkHttpClient.get(url :String): String{
    val request = Request.Builder().url(url).build()
    val response = this.newCall(request).execute()
    if (!response.isSuccessful) throw  UnsuccessfulResponse(response.message()).initCause(IOException())
    return response.body()!!.string()
}
