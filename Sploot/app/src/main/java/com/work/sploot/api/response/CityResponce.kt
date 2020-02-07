package com.work.sploot.api.response

class CityResponce {
    var data: ArrayList<Data3>? = null

    var message: String? = null

    var status: Boolean? = null

    override fun toString(): String {
        return "CityResponse [data = $data, message = $message, status = $status]"
    }


}

public class Data3
{
    var createdAt:String?=null

    var stateId: String?=null

    var name:String?=null

    var id:String?=null

    var updatedAt:String?=null


    override fun toString(): String {
        return "ClassPojo [createdAt = "+createdAt+", stateId = "+stateId+", name = "+name+", id = "+id+", updatedAt = "+updatedAt+"]";
    }
}