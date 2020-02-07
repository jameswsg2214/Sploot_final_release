package com.work.sploot.api.request

class LoginRequest {
    private var email: String? = null
    private var password: String? = null


    fun getUserName(): String? {
        return email
    }

    fun setUserName(username: String) {
        this.email = username
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }


}