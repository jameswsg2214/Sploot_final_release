package com.work.sploot.activities

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.strictmode.Violation
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk.getApplicationContext
import com.work.sploot.BuildConfig
import com.work.sploot.Entity.photoMaster
import com.work.sploot.Entity.photoagalley
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import kotlinx.android.synthetic.main.photofeild.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class photofragment: Fragment() {


    private var imageid1: Long? = null

    private var imageid2: Long? = null

    private var imageid3: Long? = null

    private var imageid4: Long? = null

    private var imageid5: Long? = null

    private var empty: Bitmap? = null

    private var filepath1:String?=null

    private var filepath2:String?=null

    private var filepath3:String?=null

    private var filepath4:String?=null

    private var filepath5:String?=null

    var image1:ImageView?=null

    var image2:ImageView?=null

    var image3:ImageView?=null

    var image4:ImageView?=null

    var image5:ImageView?=null

    var defult:ImageView?=null

    private var splootDB: SplootAppDB? = null

    private var imageview: ImageView? = null

    private var postion=0

    private lateinit var viewdata:View

    private val GALLERY = 1

    private val CAMERA = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val views = inflater.inflate(R.layout.photofeild, container, false)

        splootDB = SplootAppDB.getInstance(views.context)

        if(type_layout!=1){

            views.photo_heading_view.visibility=View.VISIBLE

            views.upload_layout.visibility=View.GONE

            var select_date by stringPref("select_date", null)



        }
        else{

            views.photo_heading_view.visibility=View.GONE

            views.upload_layout.visibility=View.VISIBLE




        }

        views.photo_fragment_close.setOnClickListener {

                try {

                    val mContext = activity

                    val manager = mContext?.supportFragmentManager

                    val transaction = manager?.beginTransaction()

                    transaction?.addToBackStack(null)

                    transaction?.replace(R.id.view_pager, calanderLayout.newInstance())

                    transaction?.commit()


                } catch (e: Exception) {


                }
        }



        viewdata=views

        imageview=views.findViewById(R.id.uploadimage)

        image1=views.findViewById<ImageView>(R.id.image1)

        image2=views.findViewById<ImageView>(R.id.image2)

        image3=views.findViewById<ImageView>(R.id.image3)

        image4=views.findViewById<ImageView>(R.id.image4)

        image5=views.findViewById<ImageView>(R.id.image5)

        defult=views.findViewById<ImageView>(R.id.uploadimage)

        updateimage()

        val perm=setupPermissions(views.context)

        views.deleteslectimage.setOnClickListener {

            Log.e("clicked","visible")
           // views.deletenotification.visibility=View.VISIBLEwe
            showDialog(postion,views)

        }

        views.imageshare.setOnClickListener {

            var outputFile: File? =null

            if(postion==0){

                outputFile = File(filepath1)

                val share =Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath1));
                //share.setPackage("com.whatsapp");//package name of the app
                startActivity(Intent.createChooser(share, "Share Image"))

            }
            else if(postion==1){

                outputFile = File(filepath2);

                val share =Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath2));
            //    share.setPackage("com.whatsapp");//package name of the app
                startActivity(Intent.createChooser(share, "Share Image"))

            }
            else if(postion==2){

                outputFile = File(filepath3);

                val share =Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath3));
          //      share.setPackage("com.whatsapp");//package name of the app
                startActivity(Intent.createChooser(share, "Share Image"))

            }
            else if(postion==3){

                outputFile = File(filepath4);

                val share =Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath4));
              //  share.setPackage("com.whatsapp");//package name of the app
                startActivity(Intent.createChooser(share, "Share Image"))

            }
            else if(postion==4){

                outputFile = File(filepath5);

                val share =Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath5));
               // share.setPackage("com.whatsapp");//package name of the app
                startActivity(Intent.createChooser(share, "Share Image"))

            }



        }

        views.upload_image.setOnClickListener {

            Toast.makeText(views.context,"Upload Image Successfully",Toast.LENGTH_LONG).show()


        }

        views.image1card.setOnClickListener {
            postion=0

            if (filepath1!=null){
              //  viewdata.uploadimage.setImageBitmap( imagebit1)

                var photoUri2: Uri = Uri.fromFile(File(filepath1))

                Glide.with(viewdata.context).load(photoUri2).into(viewdata.uploadimage)

                viewdata.photofunlayout.visibility=View.VISIBLE
            }

            else
            {
                viewdata.uploadimage.setImageBitmap(empty)

                viewdata.photofunlayout.visibility=View.INVISIBLE
            }

        }
        views.image2card.setOnClickListener {
            postion=1
            if ( filepath2!=null){
               // viewdata.uploadimage.setImageBitmap( imagebit2)

                var photoUri2: Uri = Uri.fromFile(File(filepath2))
                Glide.with(viewdata.context).load(photoUri2).into(viewdata.uploadimage)

                viewdata.photofunlayout.visibility=View.VISIBLE
            }

            else{
                viewdata.uploadimage.setImageBitmap(empty)
                viewdata.photofunlayout.visibility=View.INVISIBLE
            }

        }
        views.image3card.setOnClickListener {
            postion=2
            if ( filepath3!=null){

                var photoUri2: Uri = Uri.fromFile(File(filepath3))

                Glide.with(viewdata.context).load(photoUri2).into(viewdata.uploadimage)

                //viewdata.uploadimage.setImageBitmap(imagebit3)

                viewdata.photofunlayout.visibility=View.VISIBLE
            }
            else{
                viewdata.uploadimage.setImageBitmap(empty)

                viewdata.photofunlayout.visibility=View.INVISIBLE

            }

        }
        views.image4card.setOnClickListener {
            postion=3
            if ( filepath4!=null){

                var photoUri2: Uri = Uri.fromFile(File(filepath4))

                Glide.with(viewdata.context).load(photoUri2).into(viewdata.uploadimage)

              //  viewdata.uploadimage.setImageBitmap( imagebit4)

                viewdata.photofunlayout.visibility=View.VISIBLE

            }
            else{
                viewdata.uploadimage.setImageBitmap(empty)
                viewdata.photofunlayout.visibility=View.INVISIBLE
            }
        }
        views.image5card.setOnClickListener {
            postion=4
            if ( filepath5!=null){

                var photoUri2: Uri = Uri.fromFile(File(filepath5))

                Glide.with(viewdata.context).load(photoUri2).into(viewdata.uploadimage)

               // viewdata.uploadimage.setImageBitmap( imagebit5)
                viewdata.photofunlayout.visibility=View.VISIBLE

            }
            else{
                viewdata.uploadimage.setImageBitmap(empty)
                viewdata.photofunlayout.visibility=View.INVISIBLE
            }

        }
        views.imagecapture.setOnClickListener {

            if (postion!=6) {

                var RECORD_REQUEST_CODE=101
                val permission = ContextCompat.checkSelfPermission(views.context,
                    Manifest.permission.CAMERA)
                val permission2 = ContextCompat.checkSelfPermission(views.context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                when {
                    permission != PackageManager.PERMISSION_GRANTED -> ActivityCompat.requestPermissions(
                        views.context as Activity,
                        arrayOf(Manifest.permission.CAMERA),
                        RECORD_REQUEST_CODE
                    )
                    permission2 != PackageManager.PERMISSION_GRANTED -> ActivityCompat.requestPermissions(
                        views.context as Activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        RECORD_REQUEST_CODE
                    )
                    else -> {

                        takePhotoFromCamera()

                    }
                }
            }
            else{
                Toast.makeText(views.context,"Five photos only to upload",Toast.LENGTH_LONG).show()
            }

        }
        views.galleryphotoupload.setOnClickListener {

            if (postion!=6) {

                var RECORD_REQUEST_CODE = 101
                val permission = ContextCompat.checkSelfPermission(
                    views.context,
                    Manifest.permission.CAMERA
                )
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        views.context as Activity,
                        arrayOf(Manifest.permission.CAMERA),
                        RECORD_REQUEST_CODE
                    )
                } else {
                    choosePhotoFromGallary()
                    //dialog.dismiss()
                }
            }
            else{
                Toast.makeText(views.context,"Five photos only to upload",Toast.LENGTH_LONG).show()
            }

        }
        return views
    }

    private fun delete_image(imageId: Long) {

        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var user = userId?.toInt()

            var petid by stringPref("petid", null)

            var perId = petid!!.toLong()

            var select_date by stringPref("select_date", null)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(select_date)

            try {

                val callDetails = splootDB!!.petMasterDao()

                val delete_imge=callDetails.image_delete(imageId)

                updateimage()


            }
            catch (e:Exception){
                Log.e("photodelete","$e")
            }
        }




    }


    fun setupPermissions(context: Context) :Boolean{
        val permission = ContextCompat.checkSelfPermission(context,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        val permission2 = ContextCompat.checkSelfPermission(context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission3 = ContextCompat.checkSelfPermission(context,
            Manifest.permission.CAMERA)
        return !(permission != PackageManager.PERMISSION_GRANTED && permission2 != PackageManager.PERMISSION_GRANTED  && permission3 != PackageManager.PERMISSION_GRANTED)
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
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
                val contentURI = data!!.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    imageview!!.setImageBitmap(bitmap)
                    fetchdata(postion,path)

                    process(path,postion)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        else if (requestCode == CAMERA && resultCode==Activity.RESULT_OK)
        {


            /*   val contentURI = data!!.data

//                val thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().contentResolver, contentURI)

               val thumbnail = data!!.extras!!.get("data") as Bitmap

               Log.e("bitmap postion", "in galley view $postion")
               */

                var photoUri: Uri = Uri.parse(currentPhotoPath)
                // edit_profile_imagae.setImageURI(photoUri)
                val path =currentPhotoPath

                Glide.with(this).load(File(currentPhotoPath)).centerCrop().into(imageview!!)



            //    imageview!!.setImageBitmap(thumbnail)

                fetchdata(postion, path)
                process(path,postion)

        }
    }


    private fun fetchdata(postion:Int,path:String) {
        if (postion==0){

            //  imagebit1=bimap
            filepath1=path

          //  viewdata.image1.setImageBitmap(bimap)

            var photoUri2: Uri = Uri.fromFile(File(path))

            Glide.with(viewdata.context).load(photoUri2).into(viewdata.image1)

            viewdata.p1.visibility=View.GONE

            }
            else  if ( postion==1){

      //      imagebit2=bimap

            filepath2=path

            var photoUri2: Uri = Uri.fromFile(File(path))

            Glide.with(viewdata.context).load(photoUri2).into(viewdata.image2)

            //viewdata.image2.setImageBitmap( bimap)

            viewdata.p2.visibility=View.INVISIBLE

        }
            else  if ( postion==2){
            filepath3=path
//                viewdata.image3.setImageBitmap(bimap)

            var photoUri2: Uri = Uri.fromFile(File(path))

            Glide.with(viewdata.context).load(photoUri2).into(viewdata.image3)

      //      imagebit3=bimap

            viewdata.p3.visibility=View.INVISIBLE
            }
            else  if ( postion==3){
            filepath4=path
//                viewdata.image4.setImageBitmap( bimap)


            var photoUri2: Uri = Uri.fromFile(File(path))

            Glide.with(viewdata.context).load(photoUri2).into(viewdata.image4)

        //    imagebit4=bimap

            viewdata.p4.visibility=View.INVISIBLE
        }
            else  if ( postion==4){
            filepath5=path
//                viewdata.image5.setImageBitmap( bimap)
          //  imagebit5=bimap



            var photoUri2: Uri = Uri.fromFile(File(path))

            Glide.with(viewdata.context).load(photoUri2).into(viewdata.image5)
            viewdata.p5.visibility=View.INVISIBLE

        }
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
            val f = File(wallpaperDirectory, ((Calendar.getInstance().getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(viewdata.context,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }
    companion object {


        private val IMAGE_DIRECTORY = "/demonuts"

        var type_layout:Int?=null

        fun newInstance(type:Int): photofragment {

            type_layout=type

            return photofragment()
        }
    }

    private fun showDialog(
        postions: Int,
        views: View
    ) {
        var dialog: AlertDialog

        val builder = AlertDialog.Builder(views.context)
        builder.setTitle("Save Weight")

        builder.setMessage("Are you sure you to Delete?")

        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE ->
                {

                    Log.e("position","$postions")

                    if(postions==0){

                        Log.e("id",imageid1.toString())

                        delete_image(imageid1!!)


                    }
                    else if(postions==1){

                        Log.e("imageid","$imageid2")

                        delete_image(imageid2!!)


                    }
                    else if(postions==2){



                        Log.e("imageid","$imageid3")

                        delete_image(imageid3!!)

                    }
                    else if(postions==3){

                        Log.e("imageid","$imageid4")

                        delete_image(imageid4!!)



                    }
                    else if(postions==4){

                        Log.e("imageid","$imageid5")

                        delete_image(imageid5!!)

                    }

                }
                DialogInterface.BUTTON_NEGATIVE ->
                {

                }

            }
        }

        builder.setPositiveButton("YES",dialogClickListener)

        builder.setNegativeButton("NO",dialogClickListener)

        dialog = builder.create()

        dialog.show()


    }


  /*  private fun process(filepath1: String?, filepath2: String?, filepath3: String?, filepath4: String?, filepath5: String?) {

        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var user= userId?.toInt()

            var petid by stringPref("petid", null)

            var perId= petid!!.toLong()

            var select_date by stringPref("select_date", null)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(select_date)

            try {

                val callDetails = splootDB!!.petMasterDao()

                val isempty=callDetails.check_seleted_date(output, user!!,perId)

                if(isempty){
                    Log.e("check","date in there")
                    val insert=photoMaster(
                        userId = user,
                        petId = perId,
                        upload_date = output,
                        photo1 = filepath1,
                        photo2 = filepath2,
                        photo3 = filepath3,
                        prescription1 = filepath4,
                        prescription2 = filepath5
                    )
                    callDetails.update_photo(insert)
                    val view_data=callDetails.getphoto()
                }
                else{
                    Log.e("check","no data")

                    val insert=photoMaster(
                        userId = user,
                        petId = perId,
                        upload_date = output,
                        photo1 = filepath1,
                        photo2 = filepath2,
                        photo3 = filepath3,
                        prescription1 = filepath4,
                        prescription2 = filepath5

                    )

                    callDetails.photo_insert(insert)
                    val view_data=callDetails.getphoto()
                    Log.e("view data","$view_data")

                }


            } catch (e: Exception) {
                val s = e.message;
                Log.e("Error",s)
            }
        }
    }

    private fun updateimage() {
        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var user= userId?.toInt()

            var petid by stringPref("petid", null)

            var perId= petid!!.toLong()

            var select_date by stringPref("select_date", null)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(select_date)

            try {

                val callDetails = splootDB!!.petMasterDao()

                val isempty=callDetails.check_seleted_date(output, user!!,perId)

                if(isempty){

                    Log.e("check","date in there")

                    val isview=callDetails.get_selected_date(output, user!!,perId)

                    if(isview.photo1!=null) {
                        image1?.post(Runnable {
                            var photoUri1: Uri = Uri.fromFile(File(isview.photo1!!))
                            Glide.with(viewdata.context).load(photoUri1).into(image1!!)

                        })
                    }
                    if(isview.photo2!=null) {
                        image2?.post(Runnable {
                            var photoUri2: Uri = Uri.fromFile(File(isview.photo2!!))
                            Glide.with(viewdata.context).load(photoUri2).into(image2!!)

                        })
                    }
                    if(isview.photo3!=null) {
                        image3?.post(Runnable {
                            var photoUri2: Uri = Uri.fromFile(File(isview.photo3!!))
                            Glide.with(viewdata.context).load(photoUri2).into(image3!!)

                        })
                    }
                    if(isview.prescription1!=null) {
                        image4?.post(Runnable {
                            var photoUri2: Uri = Uri.fromFile(File(isview.prescription1!!))
                            Glide.with(viewdata.context).load(photoUri2).into(image4!!)

                        })
                    }
                    if(isview.prescription2!=null) {
                        image5?.post(Runnable {
                            var photoUri2: Uri = Uri.fromFile(File(isview.prescription2!!))
                            Glide.with(viewdata.context).load(photoUri2).into(image5!!)

                        })
                    }
                    if(isview.photo1!=null) {
                        defult?.post(Runnable {
                            var photoUri2: Uri = Uri.fromFile(File(isview.photo1!!))
                            Glide.with(viewdata.context).load(photoUri2).into(defult!!)

                        })
                    }

                    filepath1=isview.photo1
                    filepath2=isview.photo2
                    filepath3=isview.photo3
                    filepath4=isview.prescription1
                    filepath5=isview.prescription2

                    Log.e("viewdata","$isview")
                }
                else{
                    Log.e("check","Image no data")

                    filepath1=null
                    filepath2=null
                    filepath3=null
                    filepath4=null
                    filepath5=null
                }
            } catch (e: Exception) {
                val s = e.message;
                Log.e("Error photo",s)
            }
        }
    }*/



    private fun process(filepath: String?//, filepath2: String?, filepath3: String?, filepath4: String?, filepath5: String?
                        ,Image_postion: Int) {


        viewdata.photofunlayout.visibility=View.VISIBLE

        AsyncTask.execute {
            var userId by stringPref("userId", null)
            var user= userId?.toInt()

            var petid by stringPref("petid", null)

            var perId= petid!!.toLong()

            var select_date by stringPref("select_date", null)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(select_date)

            try {

                val callDetails = splootDB!!.petMasterDao()

               // val isempty=callDetails.check_seleted_date(output, user!!,perId)

                if (Image_postion==0 && filepath!=null){

                    val ischeck=callDetails.check_seleted_Type(output,user!!,perId!!,1)
                    if(ischeck){
                        Log.e("exist","Already")

                        val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,1)

                        val insert= photoagalley(
                            PhotoId = get_image.PhotoId,
                            userId = user,
                            petId = perId,
                            upload_date = output,
                            photoType = 1,
                            photopath=filepath
                        )

                        imageid1=get_image.PhotoId
                        val image_upload=callDetails.update_image(insert)
                        val viewall=callDetails.getimage()
                        Log.e("update","photo1 $viewall")
                        postion=1

                        viewdata.image1card.post(Runnable {
                            viewdata.image1card.visibility=View.VISIBLE
                        })

                    }
                    else{
                        val insert= photoagalley(
                            userId = user,
                            petId = perId,
                            upload_date = output,
                            photoType = 1,
                            photopath=filepath
                        )
                        val image_upload=callDetails.image_insert(insert)
                        val viewall=callDetails.getimage()
                        Log.e("getall","photo1 $viewall")
                        postion=1

                        val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,1)

                        imageid1=get_image.PhotoId

                        viewdata.image1card.post(Runnable {
                            viewdata.image1card.visibility=View.VISIBLE
                        })
                    }



                }
                if (Image_postion==1 && filepath!=null){


                    val ischeck=callDetails.check_seleted_Type(output,user!!,perId!!,2)
                    if(ischeck){

                        Log.e("exist","Already")

                        val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,2)

                        val insert= photoagalley(
                            PhotoId = get_image.PhotoId,
                            userId = user,
                            petId = perId,
                            upload_date = output,
                            photoType = 2,
                            photopath=filepath
                        )
                        val image_upload=callDetails.update_image(insert)
                        val viewall=callDetails.getimage()
                        Log.e("update","photo2 $viewall")
                        postion=2

                        imageid2=get_image.PhotoId

                        viewdata.image2card.post(Runnable {
                            viewdata.image2card.visibility=View.VISIBLE
                        })

                    }
                    else {
                        val insert = photoagalley(
                            userId = user,
                            petId = perId,
                            upload_date = output,
                            photoType = 2,
                            photopath = filepath
                        )
                        val image_upload = callDetails.image_insert(insert)
                        val viewall = callDetails.getimage()
                        Log.e("getall", "photo2 $viewall")
                        val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,2)

                        imageid2=get_image.PhotoId


                        postion=2
                        viewdata.image2card.post(Runnable {
                            viewdata.image2card.visibility=View.VISIBLE
                        })
                    }

                }
                if (Image_postion==2 && filepath!=null){
                    val ischeck=callDetails.check_seleted_Type(output,user!!,perId!!,3)

                    if(ischeck){
                        Log.e("exist","Already")

                        val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,3)

                        val insert= photoagalley(
                            PhotoId = get_image.PhotoId,
                            userId = user,
                            petId = perId,
                            upload_date = output,
                            photoType = 3,
                            photopath=filepath
                        )

                        imageid3=get_image.PhotoId
                        val image_upload=callDetails.update_image(insert)
                        val viewall=callDetails.getimage()
                        Log.e("update","photo1 $viewall")
                        postion=3

                        viewdata.image3card.post(Runnable {
                            viewdata.image3card.visibility=View.VISIBLE
                        })


                    }
                    else {
                        val insert = photoagalley(
                            userId = user,
                            petId = perId,
                            upload_date = output,
                            photoType = 3,
                            photopath = filepath
                        )
                        val image_upload = callDetails.image_insert(insert)
                        val viewall = callDetails.getimage()
                        Log.e("getall", "photo3 $viewall")

                        postion=3


                        val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,3)

                        imageid3=get_image.PhotoId

                        viewdata.image3card.post(Runnable {
                            viewdata.image3card.visibility=View.VISIBLE
                        })
                    }

                }
                if (Image_postion==3 && filepath!=null){

                    val ischeck=callDetails.check_seleted_Type(output,user!!,perId!!,4)

                    if(ischeck){
                        Log.e("exist","Already")

                        val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,4)

                        val insert= photoagalley(
                            PhotoId = get_image.PhotoId,
                            userId = user,
                            petId = perId,
                            upload_date = output,
                            photoType = 4,
                            photopath=filepath
                        )

                        imageid4=get_image.PhotoId
                        val image_upload=callDetails.update_image(insert)
                        val viewall=callDetails.getimage()
                        Log.e("update","photo4 $viewall")
                        postion=4

                        viewdata.image4card.post(Runnable {
                            viewdata.image4card.visibility=View.VISIBLE
                        })

                    }
                    else {
                        val insert = photoagalley(
                            userId = user,
                            petId = perId,
                            upload_date = output,
                            photoType = 4,
                            photopath = filepath
                        )

                        val image_upload = callDetails.image_insert(insert)

                        val viewall = callDetails.getimage()

                        Log.e("getall", "photo4 $viewall")

                        val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,4)

                        imageid4=get_image.PhotoId

                        postion=4

                        viewdata.image4card.post(Runnable {
                            viewdata.image4card.visibility=View.VISIBLE
                        })
                    }

                }
                if (Image_postion==4 && filepath!=null) {

                    val ischeck = callDetails.check_seleted_Type(output, user!!, perId!!, 5)

                    if (ischeck) {
                        Log.e("exist", "Already")
                        val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,5)

                        val insert= photoagalley(
                            PhotoId = get_image.PhotoId,
                            userId = user,
                            petId = perId,
                            upload_date = output,
                            photoType = 5,
                            photopath=filepath
                        )
                        imageid5=get_image.PhotoId
                        val image_upload=callDetails.update_image(insert)
                        val viewall=callDetails.getimage()
                        Log.e("update","photo5 $viewall")
                        postion=6

                        viewdata.image5card.post(Runnable {

                            viewdata.image5card.visibility=View.VISIBLE

                        })

                    } else {
                        val insert = photoagalley(
                            userId = user,
                            petId = perId,
                            upload_date = output,
                            photoType = 5,
                            photopath = filepath
                        )
                        val image_upload = callDetails.image_insert(insert)
                        val viewall = callDetails.getimage()
                        Log.e("getall", "photo5 $viewall")

                        val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,5)

                        imageid5=get_image.PhotoId

                        postion=6

                        viewdata.image5card.post(Runnable {

                            viewdata.image5card.visibility=View.VISIBLE

                        })
                    }
                }

            } catch (e: Exception) {
                val s = e.message;
                Log.e("Error",s)
            }
        }
    }

    private fun updateimage() {



        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var user= userId?.toInt()

            var petid by stringPref("petid", null)

            var perId= petid!!.toLong()

            var select_date by stringPref("select_date", null)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(select_date)

            try {

                val callDetails = splootDB!!.petMasterDao()

//                val isempty=callDetails.check_seleted_date(output, user!!,perId)


                val ischeck = callDetails.check_seleted_Type(output, user!!, perId!!, 1)

                if (ischeck) {

                    viewdata.photofunlayout.post(Runnable {

                        viewdata.photofunlayout.visibility=View.VISIBLE

                    })

                    Log.e("exist", "Already")

                    val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,1)

                    filepath1=get_image.photopath

                    imageid1=get_image.PhotoId

                    defult?.post(Runnable {
                        var photoUri1: Uri = Uri.fromFile(File(filepath1))
                        Glide.with(viewdata.context).load(photoUri1).into(defult!!)
                    })

                    image1?.post(Runnable {
                        var photoUri1: Uri = Uri.fromFile(File(filepath1))
                        Glide.with(viewdata.context).load(photoUri1).into(image1!!)
                        viewdata.p1.visibility=View.GONE
                    })


                    viewdata.image1card.post(Runnable {
                        viewdata.image1card.visibility=View.VISIBLE
                    })

                    Log.e("update","photo5 $get_image")

                } else {
                    filepath1=null

                    imageid1=null

                    viewdata.image1card.post(Runnable {
                        viewdata.image1card.visibility=View.INVISIBLE
                    })



                    Log.e("no data", "no data found")
                }

                val ischeck2 = callDetails.check_seleted_Type(output, user!!, perId!!, 2)

                if (ischeck2) {
                    Log.e("exist", "Already")
                    val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,2)

                    filepath2=get_image.photopath

                    imageid2=get_image.PhotoId

                    image2?.post(Runnable {
                        var photoUri1: Uri = Uri.fromFile(File(filepath2))
                        Glide.with(viewdata.context).load(photoUri1).into(image2!!)
                        viewdata.p2.visibility=View.GONE

                    })

                    viewdata.image2card.post(Runnable {
                        viewdata.image2card.visibility=View.VISIBLE
                    })
                    Log.e("update","photo5 $get_image")

                } else {
                    filepath2=null

                    imageid2=null

                    viewdata.image2card.post(Runnable {

                        viewdata.image2card.visibility=View.INVISIBLE

                    })

                    Log.e("no data", "no data found")
                }

                val ischeck3 = callDetails.check_seleted_Type(output, user!!, perId!!, 3)

                if (ischeck3) {
                    Log.e("exist", "Already")
                    val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,3)

                    filepath3=get_image.photopath

                    imageid3=get_image.PhotoId

                    image3?.post(Runnable {
                        var photoUri1: Uri = Uri.fromFile(File(filepath3))
                        Glide.with(viewdata.context).load(photoUri1).into(image3!!)

                        viewdata.p3.visibility=View.GONE

                    })

                    viewdata.image3card.post(Runnable {
                        viewdata.image3card.visibility=View.VISIBLE
                    })
                    Log.e("update","photo5 $get_image")

                } else {
                    filepath3=null

                    imageid3=null

                    viewdata.image3card.post(Runnable {
                        viewdata.image3card.visibility=View.INVISIBLE
                    })

                    Log.e("no data", "no data found")
                }

                val ischeck4 = callDetails.check_seleted_Type(output, user!!, perId!!, 4)

                if (ischeck4) {
                    Log.e("exist", "Already")
                    val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,4)

                    filepath4=get_image.photopath

                    imageid4=get_image.PhotoId

                    image4?.post(Runnable {
                        var photoUri1: Uri = Uri.fromFile(File(filepath4))
                        Glide.with(viewdata.context).load(photoUri1).into(image4!!)

                        viewdata.p4.visibility=View.GONE

                    })

                    viewdata.image4card.post(Runnable {
                        viewdata.image4card.visibility=View.VISIBLE
                    })
                    Log.e("update","photo5 $get_image")

                } else {
                    filepath4=null

                    imageid4=null

                    viewdata.image4card.post(Runnable {
                        viewdata.image4card.visibility=View.INVISIBLE
                    })

                    Log.e("no data", "no data found")
                }

                val ischeck5 = callDetails.check_seleted_Type(output, user!!, perId!!, 5)

                if (ischeck5) {

                    Log.e("exist", "Already")

                    val get_image=callDetails.get_seleted_Type(output,user!!,perId!!,5)

                    filepath5=get_image.photopath

                    imageid5=get_image.PhotoId

                    image5?.post(Runnable {
                        var photoUri1: Uri = Uri.fromFile(File(filepath5))
                        Glide.with(viewdata.context).load(photoUri1).into(image5!!)
                        viewdata.p5.visibility=View.GONE

                    })

                    viewdata.image5card.post(Runnable {
                        viewdata.image5card.visibility=View.VISIBLE
                    })

                    Log.e("update","photo5 $get_image")

                } else {
                    filepath5=null

                    imageid5=null

                    viewdata.p5.post(Runnable{
                        viewdata.p5.visibility=View.INVISIBLE
                    })

                    Log.e("no data", "no data found")
                }

            } catch (e: Exception) {
                val s = e.message;
                Log.e("Error photo",s)
            }
        }
    }

}