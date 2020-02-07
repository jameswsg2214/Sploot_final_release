package com.work.sploot.api

import com.work.sploot.data.stringPref
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor : Interceptor {

    var token : String = "";

    fun Token(token: String ) {
        this.token = token;
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()


        //        System.getProperty("os.version"); // OS version
//        android.os.Build.VERSION.SDK      // API Level
//        android.os.Build.DEVICE           // Device
//        android.os.Build.MODEL            // Model
//        android.os.Build.PRODUCT          // Product

//        userID: "demo_user_id",
//        userName: "demo_user",
//        hostName: "WEB-028",
//        IPAddress: "10.91.19.11",
//        deviceID: "123123",
//        deviceName: "WEB-028",
//        deviceVersion: "2.0.0",
//        deviceOS: "Windows",
//        Token: "123454esdfr43wedfgtrfdx",
//        LoginDate:

//        val wm = getSystemService(WIFI_SERVICE) as WifiManager?
//        val ip = Formatter.formatIpAddress(wm!!.connectionInfo.ipAddress)


//        user-id: "demo_user_id",
//        user-name: "demo_user",
//        host-name: "WEB-028",
//        ip-address: "10.91.19.11",
//        device-id: "123123",
//        device-name: "WEB-028",
//        device-version: "2.0.0",
//        device-os: "Windows",
//        token: "123454esdfr43wedfgtrfdx",
//        login-date:

        var userId by stringPref("userId", null)
        var loginTime by stringPref("loginTime", null)
        var username by stringPref("name", null)
        var androidId by stringPref("android_id", null)


        if(request.header("No-Authentication")==null){
            //val token = getTokenFromSharedPreference();
            //or use Token Function
            if(!token.isNullOrEmpty())
            {
               // val finalToken =  "Bearer "+token

                try
                {
                    request = request.newBuilder()
                        .addHeader("x-access-token", token)
                        .addHeader("user-id", userId.toString())
                        .addHeader("user-name", username.toString())
//                        .addHeader("hostName", ConstantMethods().getLocalIpAddress() )
//                        .addHeader("ip-address", ConstantMethods().getLocalIpAddress())
//                        .addHeader("device-id", androidId)
//                        .addHeader("device-name", ConstantMethods().getDeviceName())
//                        .addHeader("device-version", System.getProperty("os.version"))
                        .addHeader("device-os", "Android")
                        .addHeader("login-date", loginTime)
                        .build()
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                    request = request.newBuilder()
                        .addHeader("x-access-token", token)
                        .build()
                }

            }

        }

        return chain.proceed(request)
    }
}