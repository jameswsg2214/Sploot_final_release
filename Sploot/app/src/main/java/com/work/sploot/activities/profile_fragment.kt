package com.work.sploot.activities

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.opengl.ETC1.encodeImage
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.Entity.userprofile
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.api.ApiProduction
import com.work.sploot.api.request.*
import com.work.sploot.api.response.*
import com.work.sploot.api.service.CommonServices
import com.work.sploot.data.ConstantMethods
import com.work.sploot.data.stringPref
import com.work.sploot.rx.RxAPICallHelper
import com.work.sploot.rx.RxAPICallback
import com.yalantis.ucrop.UCrop
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_petprofile.view.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.calender.*
import kotlinx.android.synthetic.main.imagepick.*
import kotlinx.android.synthetic.main.myprofile.view.*
import kotlinx.android.synthetic.main.myprofile.view.profile_save

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Base64.Encoder
import java.util.Base64.getEncoder
import kotlin.collections.ArrayList

class profile_fragment:Fragment() {

    private val GALLERY = 1
    private val CAMERA = 2
    private var viewdata:View?=null
    //var bitmat: Bitmap? =null

    var profileimage: ImageView? =null

    var check_iamge:ImageView?=null

    var image_path:String?=null

    var countrylist=ArrayList<Data>()

    var countrynamelist=ArrayList<String>()

    var countrynameid=ArrayList<Int>()

    var statelist= ArrayList<Data1>()

    var statenamelist= ArrayList<String>()

    var statenameid= ArrayList<Int>()


    var citylist= ArrayList<Data3>()

    var citynamelist= ArrayList<String>()


    private var splootDB: SplootAppDB? = null

    companion object {
        val IMAGE_DIRECTORY = "/demonuts"

        fun newInstance(): profile_fragment {
            return profile_fragment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        val views=inflater.inflate(R.layout.myprofile, container, false)

        viewdata=views

        splootDB = SplootAppDB.getInstance(views.context)

        profileimage=views.findViewById(R.id.user_image)

        check_iamge=views.findViewById(R.id.check_image)


        if(!ConstantMethods().checkNetwork(views.context)){

            Toast.makeText(activity, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show()

        }

        fetchdata()

        var username by stringPref("name", null)
        var data:String= username.toString()
        if(username!="null"){
            views.name.setText(username)
            Log.e("username12345","$username")
        }
        var useremail by stringPref("useremail", null)
        if(useremail!="null"){
            views.email.setText(useremail)
            Log.e("username12345","$username")
        }


        views.profile_close.setOnClickListener {
            val intent= Intent(views.context, firstpage::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(intent)
        }

        var commService: CommonServices = ApiProduction(views!!.context).provideService(CommonServices::class.java)

        var apiCall: Observable<CountryResponse> = commService.getcountry()

        RxAPICallHelper().call(apiCall, object : RxAPICallback<CountryResponse> {

            override fun onSuccess(Response: CountryResponse) {

                if(Response.status!!)
                {
                    countrylist= Response.data!!

                    for(i in 0..countrylist.size-1)
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

        views.user_country.setAdapter(country)

        views.user_country.threshold = 1

        views.user_country.onItemClickListener = AdapterView.OnItemClickListener{

                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()

            var countryddata = selectedItem

            val data= countrynameid[position]


            var commService: CommonServices = ApiProduction(views!!.context).provideService(CommonServices::class.java)

            var stateRequest = StateRequest()

            stateRequest.id=data.toString()

            var apiCall: Observable<StateResponse> = commService.getstatebycountryid(stateRequest)

            RxAPICallHelper().call(apiCall, object : RxAPICallback<StateResponse> {

                override fun onSuccess(Response: StateResponse) {

                    if(Response.status!!)
                    {
                        statelist= Response.data!!

                        for(i in 0..statelist.size-1)
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

                //    Toast.makeText(views.context,"$throwable",Toast.LENGTH_LONG).show()

                }
            })


        }
        views.user_country.setOnDismissListener {
            //  Toast.makeText(views.context,"Suggestion closed.", Toast.LENGTH_SHORT).show()
            // process(views.auto_complete_text_view.text.toString())
        }
        views.user_country.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
                views.user_country.showDropDown()
            }
        }



        views.user_state.setAdapter(state)

        views.user_state.threshold = 1

        views.user_state.onItemClickListener = AdapterView.OnItemClickListener{

                parent,view,position,id->

            val selectedItem = parent.getItemAtPosition(position).toString()

            var statedata = selectedItem

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

                        for(i in 0..citylist.size-1)
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

            //  process(selectedItem)

            //   Toast.makeText(views.context,"Selected : $selectedItem", Toast.LENGTH_SHORT).show()
        }
        views.user_state.setOnDismissListener {
            //  Toast.makeText(views.context,"Suggestion closed.", Toast.LENGTH_SHORT).show()
            // process(views.auto_complete_text_view.text.toString())
        }
        views.user_state.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
                views.user_state.showDropDown()
            }
        }


        views.user_profile_city.setAdapter(city)

        views.user_profile_city.threshold = 1

        views.user_profile_city.onItemClickListener = AdapterView.OnItemClickListener{

                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()

            //=selectedItem

        }
        views.user_profile_city.setOnDismissListener {
            //  Toast.makeText(views.context,"Suggestion closed.", Toast.LENGTH_SHORT).show()
            // process(views.auto_complete_text_view.text.toString())
        }
        views.user_profile_city.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
                views.user_profile_city.showDropDown()
            }
        }







        views.user_image.setOnClickListener {
            dialogdaysspicker(views.context)
        }


        views.profile_save.setOnClickListener {
            when{


                views.name.text.toString().trim().isNullOrEmpty()->views.name.error="Name field can't be empty"
                views.phone_no.text.toString().trim().isNullOrEmpty()->views.phone_no.error="Phone number field can't be empty"
                views.phone_no.text.toString().trim().length<10->views.phone_no.error="Phone number minimum 10 digit"
                views.address.text.toString().trim().isNullOrEmpty()->views.address.error="Address field can't be empty"
                views.user_country.text.toString().trim().isNullOrEmpty()->views.user_country.error="Country field can't be empty"
                views.user_state.text.toString().trim().isNullOrEmpty()->views.user_state.error="State field can't be empty"
                views.user_profile_city.text.toString().trim().isNullOrEmpty()->views.user_profile_city.error="City field can't be empty"
                views.user_pin.text.toString().trim().isNullOrEmpty()->views.user_pin.error="PIN field can't be empty"
                views.user_pin.text.toString().trim().length<6->views.user_pin.error="PIN number minimum 6 digit"
                image_path==null->Toast.makeText(views.context,"Please Upload profile Photo",Toast.LENGTH_LONG).show()
                ConstantMethods().checkNetwork(views.context) -> {
                    process(views.name.text.toString().trim(),
                        views.phone_no.text.toString().trim(),
                        views.address.text.toString().trim(),
                        views.user_country.text.toString().trim(),
                        views.user_state.text.toString().trim(),
                        views.user_profile_city.text.toString(),
                        views.user_pin.text.toString().trim(),
                        image_path
                        )

                    process2(views.name.text.toString().trim(),
                        views.phone_no.text.toString().trim(),
                        views.address.text.toString().trim(),
                        views.user_country.text.toString().trim(),
                        views.user_state.text.toString().trim(),
                        views.user_profile_city.text.toString(),
                        views.user_pin.text.toString().trim(),
                        image_path!!
                    )
                    Toast.makeText(views.context,"Profile Details Save Successfully",Toast.LENGTH_LONG).show()
                    }
                else -> Toast.makeText(activity, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show()

            }
        }
        return views
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun process2(
        name: String,
        phone_no: String,
        address: String,
        user_country: String,
        user_state: String,
        user_profile_city: String,
        user_pin: String,
        imagePath: String
    ) {


        var userId by stringPref("userId", null)

        var petid by stringPref("petid", null)

        var useremail by stringPref("useremail", null)

        var username by stringPref("name", null)

        var alertDialog = ConstantMethods().setProgressDialog(this.context!!)

        alertDialog.show()

        val bm = BitmapFactory.decodeFile(imagePath);

        val baos =  ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object


        val b = baos.toByteArray()

        val image_base64= "data:image/jpeg;base64,"+getEncoder().encodeToString(b)

        Log.e("Image data",image_base64  +"\n ${image_base64.length}")


        username = name

        var commService: CommonServices = ApiProduction(this.context!!).provideService(CommonServices::class.java)

        var profilereq = profilereq()

        profilereq.setuserid(userId!!.toInt())

        profilereq.setuserName(name!!)

        profilereq.setnumber(phone_no!!)

        profilereq.setemail(useremail!!)

        profilereq.setaddress(address!!)

        profilereq.setcountry(user_country!!)

        profilereq.setstate(user_state!!)

        profilereq.setcity(user_profile_city!!)

        profilereq.setpin(user_pin!!)

        profilereq.setimagePath(image_base64)

        Log.e("data","$profilereq")

        var apiCall: Observable<profileres> = commService.userprofile(profilereq)

        RxAPICallHelper().call(apiCall, object : RxAPICallback<profileres> {

            override fun onSuccess(Response: profileres) {
                if(Response.getStatus()!!)
                {

                    Log.e("ProfileInsert", "Insert Sucess")

                   // Toast.makeText(viewdata?.context, Response.getMsg(), Toast.LENGTH_SHORT).show()

                    alertDialog.dismiss()
                }
                else
                {

                    Log.e("ProfileInsert", "Insert Failed ")

                   // Toast.makeText(viewdata?.context, Response.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }
            override fun onFailed(throwable: Throwable) {
                Log.e("ProfileInsert", " clicked Throwable: $throwable")
              //  Toast.makeText(viewdata?.context, "Error", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        })


    }





    fun dialogdaysspicker(commandAdapter: Context) {

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
                    takePhotoFromCamera()
                dialog.dismiss()}

            }

        }
        dialog.upload1.setOnClickListener {
            var MY_PERMISSIONS_REQUEST_READ_CONTACTS=101
            var RECORD_REQUEST_CODE=101
            val permission = ContextCompat.checkSelfPermission(commandAdapter,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            val permission2 = ContextCompat.checkSelfPermission(commandAdapter,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

            when {
                permission != PackageManager.PERMISSION_GRANTED-> ActivityCompat.requestPermissions(commandAdapter as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RECORD_REQUEST_CODE)
                //    choosePhotoFromGallary()

                permission2 != PackageManager.PERMISSION_GRANTED->ActivityCompat.requestPermissions(commandAdapter as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),RECORD_REQUEST_CODE)

                else->{

                    choosePhotoFromGallary()
                    dialog.dismiss()

                }

            }
        }
        dialog.imagecapcancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
              //  val contentURI = data!!.data
                try
                {



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

                        image_path=f.absolutePath

                    }catch (e:Exception)
                    {

                    }


                    val diff1=Uri.parse(image_path)

                    openCropActivity(contentURI, diff1)

                    // profileimage!!.setImageBitmap(bitmap)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("uploaderror","$e")
                }
            }
        }

        else if (requestCode == CAMERA && resultCode==Activity.RESULT_OK) {

            var photoUri = Uri.fromFile(File(currentPhotoPath))

            image_path=currentPhotoPath

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

                image_path=f.absolutePath

            }catch (e:Exception)
            {

            }


            val diff1=Uri.parse(image_path)



            openCropActivity(photoUri, diff1)




        }

        else if (requestCode == UCrop.REQUEST_CROP  && resultCode==Activity.RESULT_OK) {

            val uri = UCrop.getOutput(data!!);

            Log.e("request","check $uri")

         //   updatedata(image_path)

            Glide.with(this).load((image_path)).into(profileimage!!)

        }

    }


    private fun openCropActivity(contentURI: Uri?, contentURI1: Uri?) {

        UCrop.of(contentURI!!, contentURI1!!)
            .withMaxResultSize(300  , 300)
            .withAspectRatio(1f, 1f)
            .start(viewdata!!.context,this, UCrop.REQUEST_CROP);
    }

    private fun updatedata(path: String) {

    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs()
        }
        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance().timeInMillis).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                viewdata?.context,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())
            return f.absolutePath
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    //db process
    private fun fetchdata() {
        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var petid by stringPref("petid", null)
            var user= userId?.toLong()
            var petId= petid?.toLong()
            try {
                val callDetails = splootDB!!.petMasterDao()
                val check=callDetails.ispresent_user(user!!)
                if(check){
                  Log.e("usertable","data found")
                    val getdata=callDetails.get_user(user!!)
                    viewdata?.name?.setText(getdata.userName)
                    viewdata?.phone_no?.setText(getdata.number)
                    viewdata?.email?.setText(getdata.email)
                    viewdata?.address?.setText(getdata.address)
                    viewdata?.user_country?.setText(getdata.country)
                    viewdata?.user_state?.setText(getdata.state)
                    viewdata?.user_profile_city?.setText(getdata.city)
                    viewdata?.user_pin?.setText(getdata.pin.toString())
                    image_path=getdata.imagepath
                    Log.e("image data","$image_path")

                    profileimage!!.post(Runnable {
                        var photoUri: Uri = Uri.fromFile(File(getdata.imagepath))

                        Glide.with(viewdata!!.context).load(photoUri).into(profileimage!!)
                    })

                }
                else{
                    Log.e("usertable","data not found")
                }
            } catch (e: Exception) {
                val s = e.message
                Log.e("Error",s)
            }
        }
    }

    private fun process(
        trim: String,
        trim1: String,
        trim2: String,
        trim3: String,
        trim4: String,
        toString: String,
        trim5: String,
        imagePath: String?
    ) {

        var userId by stringPref("userId", null)

        var petid by stringPref("petid", null)

        var useremail by stringPref("useremail", null)


        var username by stringPref("name", null)

        AsyncTask.execute {

            var user= userId?.toLong()
            var petId= petid?.toLong()
            try {
                val callDetails = splootDB!!.petMasterDao()
                val check=callDetails.ispresent_user(user!!)
                if(check){

                    username=trim


                    Log.e("checkdata","yes")
                    var insert=userprofile(
                        userId = user,
                        userName = trim,
                        number = trim1,
                        email= useremail,
                        address = trim2,
                        country = trim3,
                        state = trim4,
                        city = toString,
                        pin= trim5.toInt(),
                        imagepath = imagePath
                    )

                    Log.e("checkdata","$insert")
                    val update=callDetails.update_user(insert)
                    val viewdata=callDetails.ispresent_user(user)
                }
                else {

                    Log.e("checkdata","no")

                    username=trim

                    var insert=userprofile(
                        userId = user,
                        userName = trim,
                        number = trim1,
                        email= useremail,
                        address = trim2,
                        country = trim3,
                        state = trim4,
                        city = toString,
                        pin= trim5.toInt(),
                        imagepath = imagePath
                    )
                    Log.e("checkdata","$insert")
                    val insert_user=callDetails.user_insert(insert)
                    val viewdata=callDetails.ispresent_user(user)

                }



            } catch (e: Exception) {
                val s = e.message
                Log.e("Errorasdf",s)
            }
        }

    }
}