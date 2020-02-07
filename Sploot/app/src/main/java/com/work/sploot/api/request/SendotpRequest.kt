package com.work.sploot.api.request

class SendotpRequest {
    private var email: String? = null
    fun getemail(): String? {
        return email
    }
    fun setemail(status: String) {
        this.email = status
    }

}