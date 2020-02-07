package com.work.sploot.api.request

class reminderreq {





    var email: String? = null
    var title: String? = null
    var medicine_name: String? = null

    var date: String? = null

    var time:String?=null

    var name:String?=null

    fun getUserName(): String? {
        return email
    }

    fun setUserName(username: String) {
        this.email = username
    }

    fun gettitle(): String? {
        return title
    }

    fun settitle(title: String) {

        this.title = title

    }

    fun getmedicine_name(): String? {
        return medicine_name
    }

    fun setmedicine_name(medicine_name: String) {
        this.medicine_name = medicine_name
    }

}