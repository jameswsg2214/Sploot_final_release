package com.work.sploot.api.response

class StateResponse {
    var data: ArrayList<Data1>? = null

    var message: String? = null

    var status: Boolean? = null

    override fun toString(): String {
        return "StateResponse [data = $data, message = $message, status = $status]"
    }


}

public class Data1
    {
        var createdAt: String?=null

        var  code: String?=null

        var name: String?=null

        var id: Int?=null

        var countryId: String?=null

        var updatedAt: String?=null


        override fun toString(): String        {
            return "ClassPojo [createdAt = "+createdAt+", code = "+code+", name = "+name+", id = "+id+", countryId = "+countryId+", updatedAt = "+updatedAt+"]";
        }
    }


