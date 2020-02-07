package com.work.sploot.api.response


class CountryResponse{
    var data: ArrayList<Data>? = null

    var message: String? = null

    var status: Boolean? = null

    override fun toString(): String {
        return "CountryResponse[data = $data, message = $message, status = $status]"
    }


}

public class Data
{
    var createdAt:String?=null

     var countryCode:String?=null

     var  id: Int?=null

      var  countryName: String?=null

      var updatedAt :String?=null


    override fun toString(): String {
        return "CountryResponce [createdAt = "+createdAt+", countryCode = "+countryCode+", id = "+id+", countryName = "+countryName+", updatedAt = "+updatedAt+"]";
    }
}




