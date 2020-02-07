package com.work.sploot.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.api.ApiProduction
import com.work.sploot.api.request.Createreq
import com.work.sploot.api.request.pet_register_req
import com.work.sploot.api.response.CityResponce
import com.work.sploot.api.response.Createres
import com.work.sploot.api.response.pet_create_res
import com.work.sploot.api.response.profileres
import com.work.sploot.api.service.CommonServices
import com.work.sploot.data.ConstantMethods
import com.work.sploot.data.stringPref
import com.work.sploot.rx.RxAPICallHelper
import com.work.sploot.rx.RxAPICallback
import kotlinx.android.synthetic.main.activity_petregister.*
import kotlinx.android.synthetic.main.getbreadtype.*
import kotlinx.android.synthetic.main.getdob.*
import kotlinx.android.synthetic.main.getpetname.*
import org.intellij.lang.annotations.JdkConstants
import java.util.*

class Petregister : AppCompatActivity() {


    public var mPager: ViewPager?=null

    private var splootDB: SplootAppDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_petregister)

        splootDB = SplootAppDB.getInstance(this)

        var petid by stringPref("petid", null)

        var userid by stringPref("userId", null)

        Log.e("petid","=======>>>>$petid")

        Log.e("userid","=======>>>>$userid")

        mPager = findViewById(R.id.pager)

        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)

        mPager!!.adapter = pagerAdapter

        mPager!!.setPageTransformer(true, ZoomOutPageTransformer())

        next_page.setOnClickListener {

            val data =mPager!!.currentItem

            Log.e("Data","$data")

            val ref=data+1

            mPager!!.currentItem=ref

        }

        savebtn.setOnClickListener {

            getdataproces(3)

        }
        mPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
               // Log.d("psaha","$state")
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.d("psaha","$position")
            }

            override fun onPageSelected(position: Int) {
                Log.e("onPageSelected","$position")

                when (position) {

                    0 ->
                    {

                        next_page.visibility=View.VISIBLE

                        savebtn.visibility=View.GONE

                    }

                    1 -> {
                        getdataproces(1)

                        next_page.visibility=View.VISIBLE

                        savebtn.visibility=View.GONE

                    }

                    2 -> {
                       // Log.e("petnemedata","${petname?.text.toString().trim()}")


                        mPager!!.post(Runnable {

                            if (petname?.text.toString().trim().isNullOrEmpty()) {

                                petname.error = "Pet name cannot be empty"

                                mPager!!.currentItem = 1

                            } else {

                                Log.e("petnamedata", "${petname?.text.toString().trim()}")

                            }

                        })

                        next_page.visibility=View.VISIBLE
                        savebtn.visibility=View.GONE
                    }
                    3 -> {

                        auto_complete_text_view.post(Runnable {
                            if (auto_complete_text_view?.text.toString().trim().isNullOrEmpty()) {

                                auto_complete_text_view.error = "Pet Type cannot be empty"

                                mPager!!.currentItem = mPager!!.currentItem-1

                            }

                        })
                        savebtn.visibility=View.GONE
                        next_page.visibility=View.VISIBLE
                        Log.e("Error check",".......................................${auto_complete_text_view?.text.toString().trim()}//////////////////////////////////")
                }

                    4 -> {
                        if (petage?.text.toString().trim().isNullOrEmpty()){
                        //    petage.error="Date of Birth cann't be empty"

                            Toast.makeText(this@Petregister,"Date of Birth can't be empty",Toast.LENGTH_SHORT).show()

                            mPager!!.currentItem = 3

                        }

                        savebtn.visibility=View.GONE

                        next_page.visibility=View.VISIBLE
                    }
                    5 -> {
                        getdataproces(2)
                        pro()


                    }


                    else -> {
                        savebtn.visibility=View.GONE

                        next_page.visibility=View.GONE


                        if(iswork) {

                            //nextinhome.visibility=View.VISIBLE
                            var petname = petname?.text.toString()

                            Log.e("scolledname====>", "$petname")

                            var dob = petage?.text.toString()

                            Log.e("scolledname====>", "$dob")

                            var breed = auto_complete_text_view?.text.toString()

                            Log.e("scolledname====>", "$breed")

                            datafatch(petname, dob, breed)
                        }

                    }
                }
            }
        })
    }


    private fun datafatch(petname: String, dob: String, breed: String) {

        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var petid by stringPref("petid", null)
            var username by stringPref("name", null)
            var petimage by stringPref("petimage", null)
            var user= userId?.toInt()
            var petId= petid?.toLong()
            try {
                //val callDetails = splootDB!!.petMasterDao().getAll()
                val viewdata = splootDB!!.petMasterDao().getSelectdata(petid!!,userId!!)

                var pet = petMasterEntity(
                    petId = petId,
                    userId= user,
                    sex = viewdata.sex,
                    petName = petname,
                    age = dob,
                    petCategoryId = viewdata.petCategoryId,
                    breedId= breed,
                    parenOwnerName = username,
                    photo = viewdata.photo,
                    active=1
                )

                val callDetails = splootDB!!.petMasterDao().update(pet)

                val get = splootDB!!.petMasterDao().getSelect(petid!!)

                petimage=viewdata.photo

                //Toast.makeText(this,"Your Pet registered successfully",Toast.LENGTH_LONG).show()

                Log.e("tabledata",""+get)


                runOnUiThread {

                    var type:Int?=null

                    if(get.petCategoryId=="Dog"){

                        type=1


                    }
                    else if(get.petCategoryId=="Cat"){

                        type=2


                    }

                    var alertDialog = ConstantMethods().setProgressDialog(this)
                    alertDialog.show()
                    var commService: CommonServices = ApiProduction(this).provideService(CommonServices::class.java)
                    val req = Createreq()
                    req.setuserId(get.userId!!)
                    req.setpetName(get.petName!!)
                    req.setage(get.age!!)
                    req.setsex(get.sex!!)
                    req.setpetCategoryId(type!!)
                    req.setbreedId(get.breedId!!)


                    var apiCall = commService.petcreate(req)
                    RxAPICallHelper().call(apiCall, object : RxAPICallback<Createres> {
                        override fun onSuccess(loginResponse: Createres) {
                            if(loginResponse.getStatus()!!)
                            {

                                Log.e("Insert","Sucessfully")

                                alertDialog.dismiss()
                            }
                            else
                            {

                                Log.e("Insert","statas_false")
                                //      Toast.makeText(this@LoginActivity, loginResponse.getMsg(), Toast.LENGTH_SHORT).show()
                                alertDialog.dismiss()
                                //    LoginManager.getInstance().logOut()
                            }
                        }
                        override fun onFailed(throwable: Throwable) {

                            Log.e("Insert","Status failed")
                            alertDialog.dismiss()
                        }
                    })



                }

            } catch (e: Exception) {
                val s = e.message
                Log.e("Error",s)
            }
        }
    }
    private fun getdataproces(i: Int):String {
        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var petid by stringPref("petid", null)
            var username by stringPref("name", null)
            var user= userId?.toInt()
            var petId= petid?.toLong()
            try {
                //val callDetails = splootDB!!.petMasterDao().getAll()
                val viewdata = splootDB!!.petMasterDao().getSelectdata(petid!!,userId!!)
                Log.e("tabledata",""+viewdata+"   123456789    "  + viewdata.petCategoryId)
                if(i==1){
                    when {

                        viewdata.petCategoryId=="Cat" -> {

                            petType=2

                            //mPager!!.setCurrentItem(1,true)

                        }

                        viewdata.petCategoryId=="Dog" -> {

                            petType=1


//                            mPager!!.setCurrentItem(1,true)

                        }

                        viewdata.petCategoryId==null -> {
                            Log.e("process","")
                          //  Toast.makeText(applicationContext,"Select pet Type ",Toast.LENGTH_LONG).show()


                            mPager!!.post(Runnable {

                                mPager!!.currentItem = 0

                                Toast.makeText(applicationContext,"Please Select pet Type ",Toast.LENGTH_SHORT).show()

                            })

                        }

                    }
                }
                else  if (i==2){
                    when {
                        viewdata.sex==null -> {
                            Log.e("process","1234567890-")
//                            Toast.makeText(getApplicationContext(),"Select pet Type ",Toast.LENGTH_LONG).show()

                            mPager!!.post(Runnable {
                            mPager!!.currentItem = 4

                                Toast.makeText(applicationContext,"Please Select Sex ",Toast.LENGTH_SHORT).show()


                                })
                        }

                        else->{

                            savebtn.post(Runnable {
                                savebtn.visibility=View.VISIBLE
                            })


                            next_page.post(Runnable {
                                next_page.visibility=View.GONE
                            })



                        }
                    }

                }
                else  if (i==3){
                    when {
                        viewdata.photo!=null -> {
                            Log.e("process","Enter")
                            iswork=false
                            var text = petname?.text.toString()
                            var petname = petname?.text.toString()
                            Log.e("scolledname====>", "$petname")
                            var dob = petage?.text.toString()
                            Log.e("scolledname====>", "$dob")
                            var breed = auto_complete_text_view?.text.toString()
                            Log.e("scolledname====>", "$breed")
                            datafatch(petname, dob, breed)
                            Log.e("petnnames", "$text")

                            runOnUiThread(Runnable {

                                Toast.makeText(this,"Ready To Sploot!",Toast.LENGTH_LONG).show()

                                startActivity(Intent(this, firstpage::class.java))

                                finishAffinity()

                            })



                        }
                        else ->{

                            runOnUiThread(Runnable {

                                Toast.makeText(this,"Please upload the photo",Toast.LENGTH_LONG).show()

                            })



                        }

                    }

                }


            } catch (e: Exception) {
                val s = e.message
//                Log.e("Error12345",s)
            }
        }
        return ""
    }

    private fun toastfun() {
        Toast.makeText(applicationContext,"Select pet Type ",Toast.LENGTH_LONG).show()
    }

    private fun pro() {


    }
    override fun onBackPressed() {
        if (mPager!!.currentItem == 0) {
            super.onBackPressed()
        } else {
            mPager!!.currentItem = mPager!!.currentItem - 1
        }
    }
    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = Companion.NUM_PAGES
        override fun getItem(position: Int): Fragment? {
            var fragment: Fragment? = null
            when (position) {
                0 -> fragment = selectpet()
                1 -> fragment = getpetname()
                2 -> fragment = Getbreadtype.newInstance(petType)
                3 -> fragment = Getdob()
                4 -> fragment = selectsex()
                5 -> fragment = profilepicfragment()
            }
            return fragment
            //return ScreenSlidePageFragment()
        }
    }
    companion object {
        const val NUM_PAGES = 6
        var petType=0
        var iswork:Boolean=true

        var pos=0

            fun newInstance(position:Int): Petregister {

                pos=position

                return Petregister()

            }
    }

}