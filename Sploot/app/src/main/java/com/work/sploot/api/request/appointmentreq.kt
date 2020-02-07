package com.work.sploot.api.request


class appointmentreq {


        private var userId: Int? = null

    private var userName: String? = null

        private  var petId : Int? = null

        private var task_name : String? = null

        private var start_date : String? = null

        private var end_date: String? = null

        private var repeat_type: Int? = null

    private var frequency_type_id:Int?=null

    private var every_frequency:Int?=null

    private var selective_week:String?=null

    private var active:Int?=null

    private var cat_type:Int?=null


        fun setuserId(userId: Int) {

            this.userId = userId

        }


    fun setName(userName: String) {

        this.userName = userName

    }

        fun setpetId(petId: Int) {

            this.petId = petId

        }

        fun settask_name(task_name: String) {

            this.task_name = task_name

        }

        fun setstart_date(start_date: String) {

            this.start_date = start_date

        }

        fun setend_date(end_date: String) {
            this.end_date = end_date
        }

        fun setfrequency_type_id(frequency_type_id: Int) {

            this.frequency_type_id = frequency_type_id

        }

    fun setselective_week(selective_week: String) {
        this.selective_week = selective_week
    }

    fun setevery_frequency(every_frequency: Int) {
        this.every_frequency = every_frequency
    }

    fun setrepeat_type(repeat_type: Int) {

        this.repeat_type = repeat_type

    }

    fun setactive(active: Int) {

        this.active = active

    }

    fun setcat_type(cat_type: Int) {

        this.cat_type = cat_type

    }

}