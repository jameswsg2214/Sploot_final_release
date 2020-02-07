package com.work.sploot.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.activity_forgetpassword.*
import kotlinx.android.synthetic.main.activity_petprofile.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Petprofile : Fragment()
{
    var petid:String?=null

    private var splootDB: SplootAppDB? = null

    lateinit var image_profile:ImageView

    companion object {

        var localpath=""

        var transa:FragmentManager? =null

        fun newInstance(transaction: FragmentManager): Petprofile {

            transa=transaction

            return Petprofile()

        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val views = inflater.inflate(R.layout.activity_petprofile, container, false)

        splootDB = SplootAppDB.getInstance(views.context)

        image_profile=views.findViewById(R.id.profile_image)

        callprocess(views.context,views)

        Log.e("pathdata","form front page $localpath")

        var petimage by stringPref("petimage", null)

        if(petimage!=null) {
            Log.e("petimgaeurl",petimage)

            var photoUri: Uri = Uri.fromFile(File(petimage))

            Glide.with(views.context).load(photoUri).centerCrop().into(views.profile_image)


        }
        Log.e("image bitmap","---->$petimage")

        views.alertclick.setOnClickListener{

            Log.e("Click","alert")

             try {

                    val transaction = transa?.beginTransaction()

                    transaction?.replace(R.id.view_pager, TasklayoutActivity.newInstance())

                    transaction?.commit()
                } catch (e: Exception) {

                }

        }


        val transaction = transa?.beginTransaction()
        views.petitlayout.setOnClickListener {
            var petid by stringPref("petid", null)
            if(petid!=null) {


                    try {
                        transaction?.replace(R.id.view_pager, PetDetailsview.newInstance())
                        transaction?.addToBackStack("")
                        transaction?.commit()
                    } catch (e: Exception) {

                    }

            }
            else{
                Toast.makeText(views.context,"Please register pet first",Toast.LENGTH_LONG).show()
              //  startActivity(Intent(views.context, Petregister::class.java))
            }
        }
        return views
    }
    private fun callprocess(context: Context, views: View) {

        var petid by stringPref("petid", null)

        val date = Calendar.getInstance().time

        var userId by stringPref("userId", null)

        AsyncTask.execute {

            try {
                   val callDetails = splootDB!!.petMasterDao()

                    Log.e("message","petname $petid >>>>>>>>>>>>>>>>>>>>>>>>>>>>>...")
                    if (petid!=null){
                        val viewdata2 = splootDB!!.petMasterDao().getSelect(petid!!)

                        val ischeckvi=callDetails.find_date_dv(date, petid!!, userId!!,1)

                        val ifcheckde=callDetails.find_date_dv(date, petid!!, userId!!,4)

                        Log.e("check","Deworming $ischeckvi $ifcheckde")

                        if(ischeckvi){
                            val vaccination_data=callDetails.view_date_dv(date, petid!!, userId!!,1)

                            val dateInString = vaccination_data.start_date?.toString("dd/MM/YYY")

                            Log.e("deworming","$dateInString")

                            views.viewviccination.post(Runnable {

                                views.viewviccination.text= dateInString
                                    //date?.toString("dd/MM/YYY")

                            })

                        }
                        else{
                            Log.e("No Vavination ","upcoming")
                        }
                        if(ifcheckde){

                            val Deworming_date=callDetails.view_date_dv(date, petid!!, userId!!,4)

                            val dateInString = Deworming_date.start_date?.toString("dd/MM/YYY")

                            Log.e("deworming","$dateInString")

                            views.viewdeworming.post(Runnable {

                                views.viewdeworming.text= dateInString
                                    //date?.toString("dd/MM/YYY")

                            })
                        }


                        Log.e("View on next page", "worked  $viewdata2")

                       // views.post(Runnable {
                            views.viewpetname.text= viewdata2!!.petName
                            views.viewbreed.text=viewdata2!!.breedId
                            views.viewgender.text=viewdata2!!.sex


                        val dob=viewdata2!!.age

                        val sdf = SimpleDateFormat("dd/MM/yyyy")

                        val dateInString = sdf.format(Date())

                        val formatter = SimpleDateFormat("dd/MM/yyyy")

                        val formatter2 = SimpleDateFormat("yyyy/MM/dd")

                        val current_date = formatter.parse(dateInString)

                        val dob_date=formatter2.parse(dob)

                        var age=current_date.year-dob_date.year


                        if(age==0){

                            age=current_date.month-dob_date.month

                            if(age==1){
                                views.viewage.text="$age month"

                            }

                            else {
                                views.viewage.text="$age months"

                            }


                        }
                        else if(age==1){


                            views.viewage.text="$age Year"

                        }
                        else{

                            views.viewage.text="$age Years"

                        }






                       // })

                        if(viewdata2.petCategoryId=="Cat"){
                            //bread_icon
                            views.post(Runnable {
                                views.bread_icon.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        views.context, // Context
                                        R.drawable.catbreadicon // Drawable
                                    )
                                )
                            })

                        }
                    }
                    else{
                        Log.e("table null","=>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.")
                    }
                } catch (e: Exception) {
                    val s = e.message;
                    Log.e("Error in pet details",s)
                }
            }
    }


    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

}