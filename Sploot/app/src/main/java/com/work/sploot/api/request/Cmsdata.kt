package com.work.sploot.api.request

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

public class Cmsdata
{

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var cms_data: ArrayList<cmsviewdata>? = null

    inner class cmsviewdata{

        @SerializedName("no")
        @Expose
        var no:Int?=null

        @SerializedName("heading")
        @Expose
        var heading:String?=null


        @SerializedName("subheading")
        @Expose
        var subheading:String?=null


        @SerializedName("category")
        @Expose
        var category:String?=null


        @SerializedName("tag")
        @Expose
        var tag:String?=null

        @SerializedName("authordetails")
        @Expose
        var authordetails:String?=null

        @SerializedName("schedule")
        @Expose
        var schedule:String?=null

        @SerializedName("content")
        @Expose
        var content:String?=null

        @SerializedName("titleImage")
        @Expose
        var titleImage:String?=null

        @SerializedName("active")
        @Expose
        var active:String?=null

        @SerializedName("createdAt")
        @Expose
        var createdAt:String?=null

        @SerializedName("updatedAt")
        @Expose
        var updatedAt:String?=null

    }
}