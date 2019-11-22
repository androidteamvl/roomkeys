package com.krys.kotlinamritlife.NetworkCall

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class AppUtils {

    companion object{
        var BaseUrl:String = "https://roomkeys.in/dev/rent/";
        val appkey:String = "@sfrtyuop(roomkey)1a8448ee8ty"
        fun isNetworkAvalilable(context: Context): Boolean {

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkinfo = cm.activeNetworkInfo
            return activeNetworkinfo != null && activeNetworkinfo.isConnected
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        fun retrofit() : Retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit()
            .create(ApiList::class.java)

        @RequiresApi(Build.VERSION_CODES.CUPCAKE)
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun getApiKey():String {
            return appkey
        }
    }
}