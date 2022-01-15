package com.example.mymvvmsample.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.nammametromvvm.utility.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {
    private val applicationContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!isInternetAvailable()) {
            throw NoInternetException("No Internet")
        }

        return chain.proceed(chain.request())
    }

    /*private fun isInternetAvailable(): Boolean{
        val  connectivityManager=applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also { network -> return network!=null }
    }*/
    private fun isInternetAvailable(): Boolean {
        var result = false
        val cm =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
        return result
    }
}