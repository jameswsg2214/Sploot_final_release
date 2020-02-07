package com.work.sploot.api.request

class Createreq {

    private var userId: Int? = null

    private  var sex : String? = null

    private var petName : String? = null

    private var age : String? = null

    private var petCategoryId: Int? = null

    private var breedId: String? = null


    fun setuserId(userId: Int) {

        this.userId = userId

    }


    fun setsex(sex: String) {

        this.sex = sex

    }

    fun setpetName(petName: String) {

        this.petName = petName

    }

    fun setage(age: String) {

        this.age = age

    }

    fun setpetCategoryId(petCategoryId: Int) {
        this.petCategoryId = petCategoryId
    }

    fun setbreedId(breedId: String) {
        this.breedId = breedId
    }

}