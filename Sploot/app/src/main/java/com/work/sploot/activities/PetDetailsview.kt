package com.work.sploot.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.api.ApiProduction
import com.work.sploot.api.request.CityRequest
import com.work.sploot.api.request.StateRequest
import com.work.sploot.api.response.*
import com.work.sploot.api.service.CommonServices
import com.work.sploot.data.ConstantMethods
import com.work.sploot.data.petdata
import com.work.sploot.data.stringPref
import com.work.sploot.rx.RxAPICallHelper
import com.work.sploot.rx.RxAPICallback
import com.yalantis.ucrop.UCrop
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_pet_detailsview.*
import kotlinx.android.synthetic.main.activity_pet_detailsview.view.*





import kotlinx.android.synthetic.main.calender.*
import kotlinx.android.synthetic.main.imagepick.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList
@Suppress("DEPRECATION")
class PetDetailsview : Fragment() {

    private val GALLERY = 1
    private val CAMERA = 2

    private val PIC_CROP = 3



    private val doc_GALLERY = 11
    private val doc_pick_camaera = 12

    private var splootDB: SplootAppDB? = null
    var currentColor:Int=0
    var viewdata:View?=null
    var cal = Calendar.getInstance()
    var petname:String?=null
    var breedid:String?=null
    //basic info data
    var bittextbreeddata:String?=null
    var bigenderdata:String?=null
    var biperioddata:String?=null
    var cycledata:String?=null
    var agedata:String?=null
    var dobdata:String?=null
    //moreinfo
    var heightdata:String?=null
    var lengthdata:String?=null
    var weightdata:String?=null
    var colourcodedata:String?=null
    var birthplacedata:String?=null
    var identiticesdata:String?=null
    //pet parent details
    var petfathernamedata:String?=null
    var petfatherbreeddata:String?=null
    var petmothernamedata:String?=null
    var petmotherbreeddata:String?=null
    var petparentadd1data:String?=null
    var petparentadd2data:String?=null
    var filepathdata:String?=null
    //pet owner details
    var ownernamedata:String?=null
    var ownernumberdata:String?=null
    var owneradd1data:String?=null
    var owneradd2data:String?=null
    //clinic details
    var docternamedata:String?=null
    var clinicnamedata:String?=null
    var mobilenodata:String?=null
    var emaildata:String?=null
    var addressdata:String?=null
    var citydata:String?=null
    var pincodedata:String?=null
    var statedata:String?=null
    var countryddata:String?=null
    var breedarray = petdata().dog
    val fatherbreedarray = petdata().dog
    val motherbreedarray = petdata().dog
    val gendersarray = arrayOf("Gender","Female","Male")
    val cyclearray = arrayOf("Cycle","1 Months", "2 Months","3 Months","4 Months","5 Months")
    val periodarray = arrayOf("Period","1 Weeks", "2 Weeks","3 Weeks","4 Weeks")
    val cityarray = arrayOf("Select city","Chennai","Coimbatore", "Madurai","Erode")
    val statearray = arrayOf("Select state","Tamilnadu","Kerala", "AP","Mumbai","Delhi")

    var countrylist=ArrayList<Data>()

    var countrynamelist=ArrayList<String>()

    var countrynameid=ArrayList<Int>()



    var statelist=ArrayList<Data1>()

    var statenamelist=ArrayList<String>()

    var statenameid=ArrayList<Int>()


    var citylist=ArrayList<Data3>()

    var citynamelist=ArrayList<String>()



    var RESULT_OK=69
    var itsok:Boolean=true
    var photo:String?=null
    // bigender

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
        fun newInstance(): PetDetailsview {
            return PetDetailsview()
        }
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val views = inflater.inflate(R.layout.activity_pet_detailsview, container, false)
        viewdata=views
        var bigender=views.findViewById<Spinner>(R.id.bigender)
        var biperiod=views.findViewById<Spinner>(R.id.biperiod)
        var cycle=views.findViewById<Spinner>(R.id.BIcycle)

        splootDB = SplootAppDB.getInstance(views.context)
        var petimage by stringPref("petimage", null)

        if(petimage!=null) {

            photo=petimage

            Log.e("petimgaeurl",petimage)

            var photoUri: Uri = Uri.fromFile(File(petimage))

            Glide.with(views.context).load(photoUri).into(views.edit_profile_imagae)
        }
        var petid by stringPref("petid", null)

        var petId= petid?.toLong()


        var userId by stringPref("userId", null)

        callprocess(userId,petid,views)

        views.certificate_view.setOnClickListener {


            if(filepathdata==null){

                Toast.makeText(views.context,"No certificate attached",Toast.LENGTH_LONG).show()

            }
            else{

                certificte(filepathdata!!)
            }


        }

        views.update_image.setOnClickListener {
            var RECORD_REQUEST_CODE=101
            val permission = ContextCompat.checkSelfPermission(views.context,
                Manifest.permission.CAMERA)
            val permission2 = ContextCompat.checkSelfPermission(views.context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            when{
                permission != PackageManager.PERMISSION_GRANTED -> ActivityCompat.requestPermissions(
                    views.context as Activity,
                    arrayOf(Manifest.permission.CAMERA),
                    RECORD_REQUEST_CODE)
                permission2 != PackageManager.PERMISSION_GRANTED->ActivityCompat.requestPermissions(
                    views.context as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    RECORD_REQUEST_CODE)
                else->
                {
                    takePhotoFromCamera()

                }
            }
        }
        views.update_gallary.setOnClickListener {
            var MY_PERMISSIONS_REQUEST_READ_CONTACTS=101
            var RECORD_REQUEST_CODE=101
            val permission = ContextCompat.checkSelfPermission(views.context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            val permission2 = ContextCompat.checkSelfPermission(views.context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

            when {
                permission != PackageManager.PERMISSION_GRANTED-> ActivityCompat.requestPermissions(views.context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RECORD_REQUEST_CODE)
                //    choosePhotoFromGallary()

                permission2 != PackageManager.PERMISSION_GRANTED->ActivityCompat.requestPermissions(views.context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),RECORD_REQUEST_CODE)

                else->{

                    choosePhotoFromGallary()

                }

            }


        }


        val adapter = ArrayAdapter<String>(
            views.context,
            android.R.layout.simple_dropdown_item_1line,
            breedarray
        )
        views.bittextbreed.setAdapter(adapter)
        views.bittextbreed.threshold = 1
        views.bittextbreed.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()

        }
        views.bittextbreed.setOnDismissListener {


        }
        views.bittextbreed.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
              //  views.bittextbreed.showDropDown()
            }
        }



        val spinnergender = ArrayAdapter(

            views.context, R.layout.spin_textview, gendersarray
        )
        spinnergender.setDropDownViewResource(R.layout.spin_textview)
        bigender.adapter = spinnergender

        bigender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Log.e("masdfdd", "" + gendersarray[position])
                if( gendersarray[position]=="Female"){

                    bicyclelayout.visibility=View.VISIBLE
                }else{
                    bicyclelayout.visibility=View.GONE
                }
                bigenderdata= gendersarray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //  spinnertextview.error ="Not selected"

            }
        }
        val spinnercycle = ArrayAdapter(views.context, R.layout.spin_textview, cyclearray)
        spinnercycle.setDropDownViewResource(R.layout.spin_textview)
        cycle.adapter = spinnercycle
        cycle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Log.e("cycle", "" + cyclearray[position])
                cycledata=cyclearray[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
        val spinnerperiod = ArrayAdapter(views.context, R.layout.spin_textview, periodarray)
        spinnerperiod.setDropDownViewResource(R.layout.spin_textview)
        biperiod.adapter = spinnerperiod

        biperiod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Log.e("masdfdd", "" + periodarray[position])
                biperioddata= periodarray[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        var city = ArrayAdapter<String>(
            views.context,
            android.R.layout.simple_dropdown_item_1line,
            citynamelist
        )

        var state = ArrayAdapter<String>(
            views.context,
            android.R.layout.simple_dropdown_item_1line,
            statenamelist
        )



        val country = ArrayAdapter<String>(
            views.context,
            android.R.layout.simple_dropdown_item_1line,
            countrynamelist
        )



        views.cliniccountry.setAdapter(country)

        views.cliniccountry.threshold = 1

        views.cliniccountry.onItemClickListener = AdapterView.OnItemClickListener{

                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()

            countryddata=selectedItem

            val data= countrynameid[position]


            var commService: CommonServices = ApiProduction(views!!.context).provideService(CommonServices::class.java)

            // val otpRequest = SendotpRequest()
            //otpRequest.setemail(Emailid)

            var stateRequest = StateRequest()

            stateRequest.id=data.toString()

            var apiCall: Observable<StateResponse> = commService.getstatebycountryid(stateRequest)

            RxAPICallHelper().call(apiCall, object : RxAPICallback<StateResponse> {

                override fun onSuccess(Response: StateResponse) {

                    if(Response.status!!)
                    {
                        statelist= Response.data!!

                        for(i in 0..statelist.size)
                        {
                            statenamelist.add(statelist.get(i).code.toString())

                            statenameid.add(statelist.get(i).id!!)
                        }


                        state=ArrayAdapter<String>(
                            views.context,
                            android.R.layout.simple_dropdown_item_1line,
                            statenamelist
                        )



                    }
                    else
                    {
                        Toast.makeText(views.context,Response.message,Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailed(throwable: Throwable) {

                    Log.e("addjourney_dategrid", " clicked Throwable:$throwable")

                    Toast.makeText(views.context,"$throwable",Toast.LENGTH_LONG).show()

                }
            })


            //  process(selectedItem)

            //   Toast.makeText(views.context,"Selected : $selectedItem", Toast.LENGTH_SHORT).show()
        }
        views.cliniccountry.setOnDismissListener {
            //  Toast.makeText(views.context,"Suggestion closed.", Toast.LENGTH_SHORT).show()
            // process(views.auto_complete_text_view.text.toString())
        }
        views.cliniccountry.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
                views.cliniccountry.showDropDown()
            }
        }



        views.clinicstate.setAdapter(state)

        views.clinicstate.threshold = 1

        views.clinicstate.onItemClickListener = AdapterView.OnItemClickListener{

                parent,view,position,id->

            val selectedItem = parent.getItemAtPosition(position).toString()

            statedata=selectedItem

            val data= statenameid[position]

            var commService: CommonServices = ApiProduction(views!!.context).provideService(CommonServices::class.java)

            var stateRequest = CityRequest()

            stateRequest.id=data.toString()

            var apiCall: Observable<CityResponce> = commService.getcitybystateid(stateRequest)

            RxAPICallHelper().call(apiCall, object : RxAPICallback<CityResponce> {

                override fun onSuccess(Response: CityResponce) {

                    if(Response.status!!)
                    {
                        citylist= Response.data!!

                        for(i in 0..citylist.size)
                        {
                            citynamelist.add(citylist.get(i).name.toString())

                        }

                        city = ArrayAdapter<String>(
                            views.context,
                            android.R.layout.simple_dropdown_item_1line,
                            citynamelist
                        )

                    }
                    else
                    {
                        Toast.makeText(views.context,Response.message,Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailed(throwable: Throwable) {

                    Log.e("addjourney_dategrid", " clicked Throwable:$throwable")

                    Toast.makeText(views.context,"$throwable",Toast.LENGTH_LONG).show()

                }
            })

        }
        views.clinicstate.setOnDismissListener {

        }
        views.clinicstate.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
                views.clinicstate.showDropDown()
            }
        }

        views.cliniccity.setAdapter(city)

        views.cliniccity.threshold = 1

        views.cliniccity.onItemClickListener = AdapterView.OnItemClickListener{

                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()

            citydata=selectedItem

        }
        views.cliniccity.setOnDismissListener {

        }
        views.cliniccity.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
                views.cliniccity.showDropDown()
            }
        }


        val ddate= DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

            Log.e("data"," $i  $i2  $i3 $datePicker")

            val formatter1 = SimpleDateFormat("dd/MM/yyyy")

            val date=formatter1.parse("$i3/${i2+1}/$i")

            val formatter2 = SimpleDateFormat("yyyy/MM/dd")

            val viewdate=formatter2.format(date)

            val secr_date=Date("$i3/$i2/$i")

            val current_date=Date()


           /// val dafs=Period.between(current_date,date)

            val months=fun_of_month(current_date,date)

            val week=fun_of_week(current_date,date)

            val valide= current_date.getTime() - date.getTime()

            val days = valide / (1000 * 3600 * 24)

            val mindata=addday(date,-2)

            val aka=fun_getweek(date)


            Log.e("1234","$current_date $date $days...$months  $week   $mindata ${date.day}")


            Log.e("qwertyuio","$aka")

            views.biage.setText(viewdate)

        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            cal.set(Calendar.YEAR, year)

            cal.set(Calendar.MONTH, monthOfYear)

            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            var select=cal.time

            //select_date=select
            val formatter1 = SimpleDateFormat("dd/MM/yyyy")

            var date=formatter1.format(select)


      //      val minus=select-current_date

            views.biage.setText(date.toString())

        }



        views.biagedone.setOnClickListener {


            val getdateform_path=views.biage.text.toString().trim()

            if(!getdateform_path.isNullOrEmpty()){

            val formatter1 = SimpleDateFormat("dd/MM/yyyy")

                val calls=Calendar.getInstance()

            var date_s=formatter1.parse(getdateform_path)

                Log.e("datesd","$date_s")

                Log.e("calenderdata","$getdateform_path $date_s ${date_s.year} ${date_s.month}  ${date_s.date}")

           val pick_date= DatePickerDialog(views.context,
               ddate,
                // set DatePickerDialog to point to today's date when it loads up
               cal.get(Calendar.YEAR),
               cal.get(Calendar.MONTH),
               cal.get(Calendar.DAY_OF_MONTH)
           )

            pick_date.datePicker.maxDate = Calendar.getInstance().timeInMillis


            pick_date.show()

            }
            else{

                val pick_date= DatePickerDialog(views.context,
                    ddate,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH))


                pick_date.datePicker.maxDate = Calendar.getInstance().timeInMillis

                pick_date.show()
            }

        }

        // pet parent details


        val fatheradapter = ArrayAdapter<String>(
            views.context,
            android.R.layout.simple_dropdown_item_1line,
            breedarray
        )
        views.ppdfatherbreedtext.setAdapter(fatheradapter)
        views.ppdfatherbreedtext.threshold = 1
        views.ppdfatherbreedtext.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()

        }
        views.ppdfatherbreedtext.setOnDismissListener {

        }
        views.ppdfatherbreedtext.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
         //       views.ppdfatherbreedtext.showDropDown()
            }
        }
        //  val spinnermotherbreed = ArrayAdapter(views.context, R.layout.spin_textview, motherbreedarray)
        views.ppdmotherbreedname.setAdapter(fatheradapter)
        views.ppdmotherbreedname.threshold = 1
        views.ppdmotherbreedname.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()

        }
        views.ppdmotherbreedname.setOnDismissListener {

        }
        views.ppdmotherbreedname.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){

            }
        }


        //contry list api

        var commService: CommonServices = ApiProduction(views!!.context).provideService(
            CommonServices::class.java)

        var apiCall: Observable<CountryResponse> = commService.getcountry()

        RxAPICallHelper().call(apiCall, object : RxAPICallback<CountryResponse> {

            override fun onSuccess(Response: CountryResponse) {

                if(Response.status!!)
                {
                    countrylist= Response.data!!

                    for(i in 0..countrylist.size)
                    {
                        countrynamelist.add(countrylist.get(i).countryName.toString())

                        countrynameid.add(countrylist.get(i).id!!)
                    }
                }
                else
                {

                }
            }
            override fun onFailed(throwable: Throwable) {

                Log.e("addjourney_dategrid", " clicked Throwable:$throwable")


            }
        })





        views.basicinfo.setOnClickListener {
            // view process
            basicinfolayout.visibility=View.VISIBLE

            petparentinfolayout.visibility=View.GONE
            petownerinfolayout.visibility=View.GONE
            vertinationinfolayout.visibility=View.GONE
            basicinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_edittext)
            moreinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            petparentinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            petownerinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            vertinationinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
        }
        views.moreinfo.setOnClickListener {
            // view process
            basicinfolayout.visibility=View.GONE

            petparentinfolayout.visibility=View.GONE
            petownerinfolayout.visibility=View.GONE
            vertinationinfolayout.visibility=View.GONE
            basicinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            moreinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_edittext)
            petparentinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            petownerinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            vertinationinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
        }
        views.petparentinfo.setOnClickListener {
            // view process
            basicinfolayout.visibility=View.GONE

            petparentinfolayout.visibility=View.VISIBLE
            petownerinfolayout.visibility=View.GONE
            vertinationinfolayout.visibility=View.GONE
            basicinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            moreinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            petparentinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_edittext)
            petownerinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            vertinationinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
        }
        views.petownerinfo.setOnClickListener {
            // view process
            basicinfolayout.visibility=View.GONE

            petparentinfolayout.visibility=View.GONE
            petownerinfolayout.visibility=View.VISIBLE
            vertinationinfolayout.visibility=View.GONE
            basicinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            moreinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            petparentinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            petownerinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_edittext)
            vertinationinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
        }
        views.vertinationinfo.setOnClickListener {
            // view process
            basicinfolayout.visibility=View.GONE

            petparentinfolayout.visibility=View.GONE
            petownerinfolayout.visibility=View.GONE
            vertinationinfolayout.visibility=View.VISIBLE
            basicinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            moreinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            petparentinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            petownerinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_pet_catogory)
            vertinationinfo.background = ContextCompat.getDrawable(views.context, R.drawable.ic_edittext)
        }
       views.ppdfileattach.setOnClickListener {


            upload_dialog(views.context)

        }





        views.editpagesubmint.setOnClickListener {


            var petimage by stringPref("petimage", null)

            petimage=photo


            if (views.editpagepetname.text.toString().trim().isNullOrEmpty()) {

                views.editpagepetname.error = "Please enter pet name"
                itsok = false
            }
            else{

                petname=views.editpagepetname.text.toString()

                itsok=true
            }

            if (!views.biage.text.toString().trim().isNullOrEmpty()) {
                agedata = views.biage.text.toString()
            }

            if (!views.ppdfathername.text.toString().trim().isNullOrEmpty()){

                petfathernamedata=views.ppdfathername.text.toString().trim()

            }
            if (!views.pptmothername.text.toString().trim().isNullOrEmpty()){
                petmothernamedata=views.pptmothername.text.toString()
            }
            if (!views.ppdparentadd1.text.toString().trim().isNullOrEmpty()){

                petparentadd1data=views.ppdparentadd1.text.toString()

            }
            if (!views.ppdparentadd2.text.toString().trim().isNullOrEmpty()){

                petparentadd2data=views.ppdparentadd2.text.toString()

            }
            if (!views.ppdattachcenter.text.toString().trim().isNullOrEmpty()){

            }
            if (!views.ownername.text.toString().trim().isNullOrEmpty()){

                ownernamedata=views.ownername.text.toString()

            }
            if (!views.ownernumber.text.toString().trim().isNullOrEmpty()){
                if(views.ownernumber.length()< 10){
                    views.ownernumber.error="Invalid number"
                    itsok=false
                }
                else{
                    ownernumberdata=views.ownernumber.text.toString()
                    itsok=true
                }
            }

            if (!views.owneradd1.text.toString().trim().isNullOrEmpty()){
                owneradd1data=views.owneradd1.text.toString()
            }
            if (!views.owneradd2.text.toString().trim().isNullOrEmpty()){
                owneradd2data=views.owneradd2.text.toString()
            }
            if (!views.doctername.text.toString().trim().isNullOrEmpty()){
                docternamedata=views.doctername.text.toString()
            }
            if (!views.clinicname.text.toString().trim().isNullOrEmpty()){
                clinicnamedata=views.clinicname.text.toString()
            }
            if (!views.clinicnumber.text.toString().trim().isNullOrEmpty()){
                if(views.clinicnumber.length()<10){
                    views.clinicnumber.error="Invalid number"
                    itsok=false
                }
                else{
                    mobilenodata=views.clinicnumber.text.toString()
                    itsok=true
                }
            }
            if (!views.clinicmailid.text.toString().trim().isNullOrEmpty()){
                val validate= ConstantMethods().emailvalidation(clinicmailid.text.toString().trim())
                if (!validate){
                    clinicmailid.error="Invalid Format"
                    itsok=false
                }
                else {
                    emaildata = clinicmailid.text.toString()
                    itsok=true
                }
            }
            if (!views.clinicaddress.text.toString().trim().isNullOrEmpty()){
                addressdata=views.clinicaddress.text.toString()
            }
            if (views.clinicpin.text.toString().trim()!=""){
                pincodedata=views.clinicpin.text.toString()
            }
            Log.e("message toinsert....>>>"," $userId,\n" +
                    "                $petid,\n" +
                    "                $petname,\n" +
                    "                $bittextbreeddata,\n" +
                    "                $bigenderdata,\n" +
                    "                $biperioddata,\n" +
                    "                $cycledata,\n" +
                    "                $agedata,\n" +
                    "                $dobdata,\n" +
                    "                $heightdata,\n" +
                    "                $lengthdata,\n" +
                    "                $weightdata,\n" +
                    "                $colourcodedata,\n" +
                    "                $birthplacedata,\n" +
                    "                $identiticesdata,\n" +
                    "                $petfathernamedata,\n" +
                    "                $petfatherbreeddata,\n" +
                    "                $petmothernamedata,\n" +
                    "                $petmotherbreeddata,\n" +
                    "                $petparentadd1data,\n" +
                    "                $petparentadd2data,\n" +
                    "                $filepathdata,\n" +
                    "                $ownernamedata,\n" +
                    "                $ownernumberdata,\n" +
                    "                $owneradd1data,\n" +
                    "                $owneradd2data,\n" +
                    "                $docternamedata,\n" +
                    "                $clinicnamedata,\n" +
                    "                $mobilenodata,\n" +
                    "                $emaildata,\n" +
                    "                $addressdata,\n" +
                    "                $citydata,\n" +
                    "                $pincodedata,\n" +
                    "                $statedata,\n" +
                    "                $countryddata")



            if(itsok) {

                when {
                    views.editpagepetname.text.isNullOrEmpty() -> Toast.makeText(this.context,"Pet name cannot be empty",Toast.LENGTH_SHORT).show()

                    views.bittextbreed.text.isNullOrEmpty() -> Toast.makeText(this.context,"Pet breed type cannot be empty",Toast.LENGTH_SHORT).show()

                    bigenderdata=="Gender" -> Toast.makeText(this.context,"Please select gender",Toast.LENGTH_SHORT).show()


                    else -> {

                        process(
                            userId,
                            petid,
                            petname,
                            views.bittextbreed.text.toString(),
                            bigenderdata,
                            biperioddata,
                            cycledata,
                            agedata,
                            dobdata,
                            heightdata,
                            lengthdata,
                            weightdata,
                            colourcodedata,
                            birthplacedata,
                            identiticesdata,
                            views.ppdfathername.text.toString(),
                            views.ppdfatherbreedtext.text.toString(),
                            petmothernamedata,
                            views.ppdmotherbreedname.text.toString(),
                            petparentadd1data,
                            petparentadd2data,
                            filepathdata,
                            ownernamedata,
                            ownernumberdata,
                            owneradd1data,
                            owneradd2data,
                            docternamedata,
                            clinicnamedata,
                            mobilenodata,
                            emaildata,
                            addressdata,
                            citydata,
                            pincodedata,
                            statedata,
                            countryddata,
                            views,
                            photo
                        )


                    }
                }




            }
        }
        return views
    }

    private fun fun_getweek(date: Date?): ArrayList<Int> {

        var dates:ArrayList<Int> = arrayListOf()

        if(date!!.day==0){

            for(i in 6 downTo 0){

                val mindata=addday(date,i)

                dates!!.add(mindata!!.date)

            }

            return dates!!
        }
        else{

            val mindata=addday(date,-1)



            return fun_getweek(mindata)
        }


    }

    private fun fun_of_week(currentDate: Date?, date: Date?): Int {

        var a=date
        var b =currentDate

        if (b!!.before(a)) {

            return -fun_of_week(b, a)
        }
        a = resetTime(a!!)

        b = resetTime(b!!)


        val cal = GregorianCalendar()
        cal.setTime(a)
        var weeks = 0
        while (cal.getTime().before(b)) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1)
            weeks++
        }
        return weeks


    }

    private fun resetTime(d: Date?): Date? {

        val cal = GregorianCalendar()
        cal.time = d
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.getTime()

    }

    private fun fun_of_month(date1: Date, date2: Date?): Int {

        var a=date1

        var b=date2

            val cal = Calendar.getInstance()
            if (a.before(b)) {
                cal.setTime(a)
            } else {
                cal.setTime(b)
                b = a
            }
            var c = 0
            while (cal.getTime().before(b)) {
                cal.add(Calendar.MONTH, 1)
                c++
            }
            return c - 1


    }

    private fun callprocess(userId: String?, petid: String?, views: View) {
        AsyncTask.execute {
            try {
                Log.e("workflow","$userId $petid")

                val viewdata = splootDB!!.petMasterDao().getSelectdata(petid!!,userId!!)
                Log.e("view data===>", "worked   ${viewdata}")
                petname= viewdata.petName

                views.editpagepetname!!.setText(viewdata.petName)
                var age=views.findViewById<EditText>(R.id.biage)

                if (viewdata.petCategoryId!=null){

                    if(viewdata.petCategoryId=="Dog"){


                        activity!!.runOnUiThread(Runnable {
                            breedarray=petdata().dog

                        })

                    }
                    else if(viewdata.petCategoryId=="Cat"){


                        activity!!.runOnUiThread(Runnable {
                            breedarray=petdata().cat

                        })


                        //
                    }
                }
                if(viewdata.breedId!=null){

                    bittextbreeddata=viewdata.breedId

                     views.bittextbreed.setText(bittextbreeddata)

                }
                if(viewdata.sex!=null) {

                    bigenderdata = viewdata.sex

                    if (bigenderdata!=null){
                        for(i in 1..gendersarray.size-1)
                        {
                            if(bigenderdata.equals(gendersarray[i]))
                            {
                                views.bigender.setSelection(i)
                            }
                        }
                    }

                    if(bigenderdata=="Female"){
                        views.bicyclelayout.visibility=View.VISIBLE
                    }

                }



                if(viewdata.age!=null){

                    views.biage!!.setText(viewdata.age)

                }

                if(viewdata.monthlyCycle!= null){

                    cycledata=viewdata.monthlyCycle

                    if (cycledata!=null){
                        for(i in 0..cyclearray.size-1)
                        {
                            if(cycledata.equals(cyclearray[i]))
                            {
                                views.BIcycle.setSelection(i)
                            }
                        }
                    }

                }
                if(viewdata.period!=null){

                    biperioddata=viewdata.period

                    if (biperioddata!=null){
                        for(i in 0..periodarray.size-1)
                        {
                            if(biperioddata.equals(periodarray[i]))
                            {
                                views.biperiod.setSelection(i)
                            }
                        }
                    }

                }


                //pet parent details
                if (viewdata.parentFatherName!=null) {
                    petfathernamedata = viewdata.parentFatherName
                    views.ppdfathername!!.setText(petfathernamedata)
                }
                petfatherbreeddata=viewdata.parentFatherBreedName

                if(petfatherbreeddata!=null){
                    views.ppdfatherbreedtext.setText(petfatherbreeddata!!)
                }
                if (viewdata.parentMotherName!=null) {
                    petmothernamedata = viewdata.parentMotherName
                    views.pptmothername!!.setText(petmothernamedata)
                }
                petmotherbreeddata=viewdata.parentMotherBreedName

                if(petmotherbreeddata!=null){
                    motherbreedarray[0]=petmotherbreeddata!!

                    views.ppdmotherbreedname.setText(petmotherbreeddata!!)
                }

                var add=viewdata.parentAddress
                if(add!="null\$null"){

                    Log.e("data address","work flow $add")

                    val parts = add?.split("$")
                    petparentadd1data= parts!![0]

                    if(petparentadd1data!=null) {

                        views.ppdparentadd1!!.setText(petparentadd1data)

                    }

                    petparentadd2data = parts!![1]

                    if(petparentadd2data!=null) {

                        views.ppdparentadd2!!.setText(petparentadd2data)

                    }


                }
                if (viewdata.certificatepath!=null) {
                    filepathdata = viewdata.certificatepath
                    views.ppdattachcenter!!.setText("File Path:"+filepathdata)

                }
                Log.e("filepath--->>>","$filepathdata")
                //owner details

                if(viewdata.parenOwnerName!=null) {

                    ownernamedata = viewdata.parenOwnerName

                    views.ownername!!.setText(viewdata.parenOwnerName)

                }

                if(viewdata.parenMobileNumber!=null) {

                    ownernumberdata = viewdata.parenMobileNumber
                    views.ownernumber!!.setText(ownernumberdata)
                }
                var owneradd=viewdata.parentOwnerAddress
                if(owneradd!="null\$null"){
                    val owneradds = owneradd?.split("$")
                    owneradd1data= owneradds!![0]
                    views.owneradd1!!.setText(owneradd1data)
                    owneradd2data= owneradds!![1]
                    views.owneradd2!!.setText(owneradd2data)
                }
                //docter
                if(viewdata.drName!=null) {
                    docternamedata = viewdata.drName

                    views.doctername!!.setText(docternamedata)
                }
                if (viewdata.drhospitalName!=null) {
                    clinicnamedata = viewdata.drhospitalName
                    views.clinicname!!.setText(clinicnamedata)
                }
                if (viewdata.drMobile!=null){
                    mobilenodata=viewdata.drMobile
                    views.clinicnumber!!.setText(mobilenodata)
                }
                if (viewdata.drEmail!=null){
                    emaildata = viewdata.drEmail
                    views.clinicmailid!!.setText(emaildata)
                }



                if (viewdata.drAddress!=null){

                    addressdata=viewdata.drAddress

                    views.clinicaddress!!.setText(viewdata.drAddress)
                }

                Log.e("View on next page", "worked  $viewdata")
            } catch (e: Exception) {
                val s = e.message
//                Log.e("Error in pet details",s)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun process(
        userId: String?,
        petid: String?,
        petname: String?,
        bittextbreeddata: String?,
        bigenderdata: String?,
        biperioddata: String?,
        cycledata: String?,
        agedata: String?,
        dobdata: String?,
        heightdata: String?,
        lengthdata: String?,
        weightdata: String?,
        colourcodedata: String?,
        birthplacedata: String?,
        identiticesdata: String?,
        petfathernamedata: String?,
        petfatherbreeddata: String?,
        petmothernamedata: String?,
        petmotherbreeddata: String?,
        petparentadd1data: String?,
        petparentadd2data: String?,
        filepathdata: String?,
        ownernamedata: String?,
        ownernumberdata: String?,
        owneradd1data: String?,
        owneradd2data: String?,
        docternamedata: String?,
        clinicnamedata: String?,
        mobilenodata: String?,
        emaildata: String?,
        addressdata: String?,
        citydata: String?,
        pincodedata: String?,
        statedata: String?,
        countryddata: String?,
        views: View,
        photo: String?
    ) {

        AsyncTask.execute {

            //  var userId by stringPref("userId", null)
            var user= userId?.toInt()
            var pet= petid?.toLong()

            var petimage by stringPref("petimage", null)

            val viewdata = splootDB!!.petMasterDao().getSelectdata(petid!!,userId!!)

            petimage=photo
       /*
            if(petparentadd1data!= null && petparentadd2data!=null){


            }*/

            var parenetaddress= "$petparentadd1data$$petparentadd2data"
            var owneraddress= "$owneradd1data$$owneradd2data"
           // var draddress= "$addressdata$$pincodedata"
            try {
                //val callDetails = splootDB!!.petMasterDao().getAll()

                var tbldata = petMasterEntity(


                    petId = pet,
                    userId = user,
                    petCategoryId = viewdata.petCategoryId,
                    petName = petname,
                    breedId = bittextbreeddata,
                    sex = bigenderdata,
                    period = biperioddata,
                    monthlyCycle = cycledata,
                    age = agedata,
                    parentFatherName = petfathernamedata,
                    parentFatherBreedName = petfatherbreeddata,
                    parentMotherName = petmothernamedata,
                    parentMotherBreedName = petmotherbreeddata,
                    parentAddress = parenetaddress,
                    certificatepath = filepathdata,
                    parenOwnerName = ownernamedata,
                    parenMobileNumber = ownernumberdata,
                    parentOwnerAddress = owneraddress,
                    drName = docternamedata,
                    drhospitalName = clinicnamedata,
                    drMobile = mobilenodata,
                    drEmail = emaildata,
                    drAddress = addressdata,
                    photo = photo,
                    active = 1
                )
                val callDetails = splootDB!!.petMasterDao().update(tbldata)

                startActivity(Intent(views.context,firstpage::class.java))

                activity!!.finish()


            } catch (e: Exception) {
                val s = e.message;
                Log.e("Error",s)
                Toast.makeText(views.context,"Error $s",Toast.LENGTH_LONG).show()
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data

                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)

                val path = saveImage(bitmap)

                photo=path

                val diff=Uri.parse(path)



                val wallpaperDirectory = File(
                    (Environment.getExternalStorageDirectory()).toString() + "/sploot"
                )
                // have the object build the directory structure, if needed.
                Log.d("fee",wallpaperDirectory.toString())

                if (!wallpaperDirectory.exists())
                {
                    wallpaperDirectory.mkdirs()
                }

                try {
                    Log.d("heel", wallpaperDirectory.toString())

                    val f = File(
                        wallpaperDirectory,
                        ((Calendar.getInstance().getTimeInMillis()).toString() + ".jpg")

                    )

                    photo=f.absolutePath

                }catch (e:Exception)
                {

                }


                val diff1=Uri.parse(photo)

                    openCropActivity(contentURI, diff1)




             //   performCrop(contentURI!!)


               /* try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    photo=path
                    //   petimage=path
                    viewdata!!.edit_profile_imagae!!.setImageBitmap(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("uploaderror","$e")
                }*/
            }
        }
        else if (requestCode == CAMERA && resultCode==Activity.RESULT_OK)
        {

            AsyncTask.execute {




            var photoUri = Uri.fromFile(File(currentPhotoPath))

            photo=currentPhotoPath

            val uripath= Uri.parse(photo)



            val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + "/sploot"
            )
            // have the object build the directory structure, if needed.
            Log.d("fee",wallpaperDirectory.toString())

            if (!wallpaperDirectory.exists())
            {
                wallpaperDirectory.mkdirs()
            }

            try {
                Log.d("heel", wallpaperDirectory.toString())

                val f = File(
                    wallpaperDirectory,
                    ((Calendar.getInstance().getTimeInMillis()).toString() + ".jpg")

                )

                photo=f.absolutePath

            }catch (e:Exception)
            {

            }


            val diff1=Uri.parse(photo)



            openCropActivity(photoUri, diff1)

            }



            try{



            }

            catch (e: IOException) {
                e.printStackTrace()
                Log.e("uploaderror","$e")
            }

        }

        else if (requestCode == UCrop.REQUEST_CROP  && resultCode==Activity.RESULT_OK) {

            val uri = UCrop.getOutput(data!!)

            Log.e("request","check $uri")

            Glide.with(this).load((photo)).into(edit_profile_imagae)

        }

        else if (requestCode == doc_pick_camaera && resultCode==Activity.RESULT_OK)
        {


            var photoUri = Uri.fromFile(File(currentPhotoPath))

            filepathdata=currentPhotoPath

            val wallpaperDirectory = File((Environment.getExternalStorageDirectory()).toString() + "/sploot")
            // have the object build the directory structure, if needed.
            Log.d("fee",wallpaperDirectory.toString())

            if (!wallpaperDirectory.exists())
            {
                wallpaperDirectory.mkdirs()
            }

            try {
                Log.d("heel", wallpaperDirectory.toString())

                val f = File(
                    wallpaperDirectory,
                    ((Calendar.getInstance().getTimeInMillis()).toString() + ".jpg")

                )

                filepathdata=f.absolutePath

            }catch (e:Exception)
            {

            }


            val diff1=Uri.parse(filepathdata)

            certificatecrop(photoUri, diff1)

        }

       else if(requestCode==doc_GALLERY && resultCode==Activity.RESULT_OK){

            if (data != null) {

                val contentURI = data!!.data


                val wallpaperDirectory = File(
                    (Environment.getExternalStorageDirectory()).toString() + "/sploot"
                )
                // have the object build the directory structure, if needed.
                Log.d("fee",wallpaperDirectory.toString())

                if (!wallpaperDirectory.exists())
                {
                    wallpaperDirectory.mkdirs()
                }

                try {
                    Log.d("heel", wallpaperDirectory.toString())

                    val f = File(
                        wallpaperDirectory,
                        ((Calendar.getInstance().getTimeInMillis()).toString() + ".jpg")

                    )

                    filepathdata=f.absolutePath

                }catch (e:Exception)
                {

                }


                val diff1=Uri.parse(filepathdata)

                certificatecrop(contentURI, diff1)


            }


        }

        else if (requestCode == PIC_CROP  && resultCode==Activity.RESULT_OK) {

            viewdata!!.ppdattachcenter.setText("File path:"+filepathdata)



        }

    }

    private fun openCropActivity(contentURI: Uri?, contentURI1: Uri?) {

        UCrop.of(contentURI!!, contentURI1!!)
            .withMaxResultSize(700  , 400)
            .withAspectRatio(4f, 3f)
            .start(viewdata!!.context,this,UCrop.REQUEST_CROP)
    }

    private fun certificatecrop(contentURI: Uri?, contentURI1: Uri?) {

        UCrop.of(contentURI!!, contentURI1!!)
            .withMaxResultSize(1000  , 1000)
            .withAspectRatio(8.5f, 11f)
            .start(viewdata!!.context,this,PIC_CROP)
    }


    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)

    }
    private fun takePhotoFromCamera() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this.context!!,
                        activity?.getPackageName() +".provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA)
                }
            }
        }


    }



    var currentPhotoPath: String=""

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + profilepicfragment.IMAGE_DIRECTORY
        )

        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs()
        }

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    fun saveImage(myBitmap: Bitmap):String {

        val bytes = ByteArrayOutputStream()

        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + profilepicfragment.IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())

        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel",wallpaperDirectory.toString())

            val f = File(wallpaperDirectory, ((Calendar.getInstance().getTimeInMillis()).toString() + ".jpg"))

            f.createNewFile()

            val fo = FileOutputStream(f)

            fo.write(bytes.toByteArray())

            MediaScannerConnection.scanFile(viewdata?.context, arrayOf(f.getPath()),arrayOf("image/jpeg"), null)

            fo.close()

            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }


    fun certificte(certicate_link: String): String? {

        val dialog = Dialog(viewdata!!.context)
        Log.e("diloge","------------------------------------------>")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.certificate_dailog)

        var imageView=dialog.findViewById<ImageView>(R.id.certificate_Image)

        var photoUri2: Uri = Uri.fromFile(File(certicate_link))

        Glide.with(viewdata!!.context).load(photoUri2).into(imageView)

        dialog.calenderokbtn.setOnClickListener {

            dialog.dismiss()
        }

        dialog.show()

        return null

    }


    fun upload_dialog(commandAdapter: Context) {
        val dialog = Dialog(commandAdapter)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(true)
        dialog.onBackPressed()
        dialog.setContentView(R.layout.imagepick)

        dialog.capture.setOnClickListener {
            var RECORD_REQUEST_CODE=101
            val permission = ContextCompat.checkSelfPermission(commandAdapter,
                Manifest.permission.CAMERA)
            val permission2 = ContextCompat.checkSelfPermission(commandAdapter,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            when{
                permission != PackageManager.PERMISSION_GRANTED -> ActivityCompat.requestPermissions(
                    commandAdapter as Activity,
                    arrayOf(Manifest.permission.CAMERA),
                    RECORD_REQUEST_CODE)
                permission2 != PackageManager.PERMISSION_GRANTED->ActivityCompat.requestPermissions(
                    commandAdapter as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    RECORD_REQUEST_CODE)
                else->
                {

                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        // Ensure that there's a camera activity to handle the intent
                        takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                            // Create the File where the photo should go
                            val photoFile: File? = try {
                                createImageFile()
                            } catch (ex: IOException) {
                                // Error occurred while creating the File
                                null
                            }
                            // Continue only if the File was successfully created
                            photoFile?.also {
                                val photoURI: Uri = FileProvider.getUriForFile(
                                    this.context!!,
                                    activity?.getPackageName() +".provider",
                                    it
                                )

                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                                startActivityForResult(takePictureIntent, doc_pick_camaera)
                            }
                        }
                    }


                    dialog.dismiss()
                }


            }

        }

        dialog.upload1.setOnClickListener {
            var MY_PERMISSIONS_REQUEST_READ_CONTACTS=101
            var RECORD_REQUEST_CODE=101
            val permission = ContextCompat.checkSelfPermission(commandAdapter,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    commandAdapter as Activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    RECORD_REQUEST_CODE)
            }
            else{
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, doc_GALLERY)
                dialog.dismiss()
            }
        }

        dialog.imagecapcancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }



    private fun addday(d: Date?,day:Int): Date? {

        val cal = GregorianCalendar()
        cal.setTime(d)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        cal.add(Calendar.DAY_OF_YEAR, -day)

        return cal.getTime()

    }




}
