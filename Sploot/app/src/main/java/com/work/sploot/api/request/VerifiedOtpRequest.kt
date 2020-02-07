package com.work.sploot.api.request

class VerifiedOtpRequest {
    private var email: String? = null
    private var otp: Int? = null
    fun getemail(): String? {
        return email
    }
    fun setemail(status: String) {
        this.email = status
    }
    fun getotp(): Int? {
        return otp
    }
    fun setotp(status: String) {
        this.otp = status.toInt()
    }
}