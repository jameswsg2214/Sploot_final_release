package com.work.sploot.api.request

class profilereq {



    public var userId : Int?=null


    public var UserName : String?=null

    public var number : String?=null

    public var email : String?=null

    public var address : String?=null




    public var state : String?=null

    public var country : String?=null



    public var city : String?=null



    public var pin : String?=null

    public var imagePath : String?=null

    fun setuserid(userId: Int) {
        this.userId = userId
    }


    fun setuserName(userName: String) {
        this.UserName = userName
    }


    fun setnumber(number: String) {
        this.number = number
    }



    fun setemail(email: String) {
        this.email = email
    }


    fun setaddress(address: String) {
        this.address = address
    }


    fun setcountry(country: String) {
        this.country = country
    }


    fun setstate(state: String) {
        this.state = state
    }


    fun setcity(city: String) {
        this.city = city
    }



    fun setpin(pin: String) {
        this.pin = pin
    }


    fun setimagePath(imagePath: String) {
        this.imagePath = imagePath
    }


}