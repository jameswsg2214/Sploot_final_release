package com.work.sploot.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendotpResponse{
    @SerializedName("status")
    @Expose
    private var status: Boolean? = null
    @SerializedName("message")
    @Expose
    private var msg: String? = null
    fun getStatus(): Boolean? {
        return status
    }
    fun setStatus(status: Boolean) {
        this.status = status
    }
    fun getMsg(): String? {
        return msg
    }
}