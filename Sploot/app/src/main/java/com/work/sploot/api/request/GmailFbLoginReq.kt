package com.work.sploot.api.request

class GmailFbLoginReq {
    private var email: String? = null
    private var userId: String? = null


    fun getemail(): String? {
        return email
    }
    fun setemail(username: String) {
        this.email = username
    }
    fun getuserId(): String? {
        return userId
    }
    fun setuserId(userId: String) {
        this.userId = userId
    }

}