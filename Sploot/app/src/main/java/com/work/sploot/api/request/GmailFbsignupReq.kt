package com.work.sploot.api.request

class GmailFbsignupReq {
    private var email: String? = null
    private var userName: String? = null
    private var password: String? = null
    private var loginType: String? = null
    fun getemail(): String? {
        return email
    }
    fun setemail(username: String) {
        this.email = username
    }
    fun getname(): String? {
        return userName
    }
    fun setname(name: String) {
        this.userName = name
    }
    fun getuserId(): String? {
        return password
    }
    fun setuserId(userId: String) {
        this.password = userId
    }
    fun getloginType(): String? {
        return loginType
    }
    fun setloginType(userId: String) {
        this.loginType = userId
    }

}