package com.work.sploot.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class LoginResponse {

//    "msg": "User not found"
    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var msg: String? = null
    @SerializedName("token")
    @Expose
    private var token: String? = null
    @SerializedName("data")
    @Expose
    private var userDetails: UserDetails? = null

    fun getStatus(): Boolean? {
        return status
    }

    fun setStatus(status: Boolean) {
        this.status = status
    }

    fun getMsg(): String? {
        return msg
    }
    fun getToken(): String? {
        return token
    }

    fun setToken(token: String) {
        this.token = token
    }

    fun setUserDetails(userDetails: UserDetails) {
        this.userDetails = userDetails
    }

    fun getUserDetails(): UserDetails? {
        return userDetails
    }



    inner class UserDetails {

        @SerializedName("userId")
        @Expose
        var userId: String? = null
//        @SerializedName("userType")
//        @Expose
//        var userType: String? = null
        @SerializedName("userName")
        @Expose
        var userName: String? = null
//        @SerializedName("userImageUrl")
//        @Expose
//        var userImageUrl: String? = null
        @SerializedName("email")
        @Expose
        var emailId: String? = null
        @SerializedName("phoneNo")
        @Expose
        var phoneNo: String? = null
        @SerializedName("active")
        @Expose
        var active: String? = null
        @SerializedName("verified")
        @Expose
        var verified: String? = null
    }



}