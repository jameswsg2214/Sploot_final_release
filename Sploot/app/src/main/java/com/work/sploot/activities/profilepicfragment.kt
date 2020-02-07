package com.work.sploot.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.MediaScannerConnection
import android.net.Uri
import android.opengl.Visibility
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.profilepicupload.view.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class profilepicfragment : Fragment(){

    var viewsdata: View? = null
    private val GALLERY = 1
    private val CAMERA = 2

    private  val REQUEST_IMAGE = 100;
    var bitmat: Bitmap? =null

    var photo=""
    var profileimage: ImageView? =null


    private  val CAMERA_ACTION_PICK_REQUEST_CODE=105
    private var splootDB: SplootAppDB? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val views=inflater.inflate(R.layout.profilepicupload, container, false)
        viewsdata=views
        splootDB = SplootAppDB.getInstance(views.context)
        profileimage=views.findViewById(R.id.profileimagae)

        views.upload_image.setOnClickListener {


      //      launchCameraIntent()

            var RECORD_REQUEST_CODE=101
            val permission = ContextCompat.checkSelfPermission(views.context,
                Manifest.permission.CAMERA)
            val permission2 = ContextCompat.checkSelfPermission(views.context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            when {
                permission != PackageManager.PERMISSION_GRANTED -> ActivityCompat.requestPermissions(views.context as Activity, arrayOf(Manifest.permission.CAMERA), RECORD_REQUEST_CODE)
                permission2 != PackageManager.PERMISSION_GRANTED -> {
                    ActivityCompat.requestPermissions(views.context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RECORD_REQUEST_CODE
                    )
                    takePhotoFromCamera()

                }
                else -> {

                    takePhotoFromCamera()

                }
            }






        }
        views.upload_gallary.setOnClickListener {



            var MY_PERMISSIONS_REQUEST_READ_CONTACTS=101



            var RECORD_REQUEST_CODE=101

            val permission = ContextCompat.checkSelfPermission(views.context, Manifest.permission.READ_EXTERNAL_STORAGE)
            val permission2 = ContextCompat.checkSelfPermission(views.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)


            when {

                permission != PackageManager.PERMISSION_GRANTED-> ActivityCompat.requestPermissions(views.context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RECORD_REQUEST_CODE)

                permission2 != PackageManager.PERMISSION_GRANTED-> {
                    ActivityCompat.requestPermissions(views.context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),RECORD_REQUEST_CODE)
                    choosePhotoFromGallary()
                }

                else->{

                    choosePhotoFromGallary()

                }

            }

        }




        return views
    }

    fun chose():Boolean{

        var RECORD_REQUEST_CODE=101
        val permission = ContextCompat.checkSelfPermission(this.context!!,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        val permission2 = ContextCompat.checkSelfPermission(this.context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        when {

            permission != PackageManager.PERMISSION_GRANTED-> {
                ActivityCompat.requestPermissions(this.context!! as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RECORD_REQUEST_CODE)

                return false
            }
            //    choosePhotoFromGallary()

            permission2 != PackageManager.PERMISSION_GRANTED->{
                ActivityCompat.requestPermissions(this.context!! as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),RECORD_REQUEST_CODE)
                return false
            }

            else->{

                return true

            }

        }

        return false
    }



    fun open_camera() {
      val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      val file =  createImageFile(); // 1
      var uri:Uri?=null
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
           uri =  FileProvider.getUriForFile(this.context!!, activity?.getPackageName() +".provider", file)
      else
           uri = Uri.fromFile(file); // 3
      pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
      startActivityForResult(pictureIntent, CAMERA_ACTION_PICK_REQUEST_CODE);
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
               // val contentURI = data!!.data
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

                        photo=f.absolutePath

                    }catch (e:Exception)
                    {

                    }


                    val diff1=Uri.parse(photo)

                    openCropActivity(contentURI, diff1)


                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("uploaderror","$e")
                }
            }
        }
        else if (requestCode == CAMERA && resultCode==Activity.RESULT_OK) {

            var photoUri = Uri.fromFile(File(currentPhotoPath))

            photo=currentPhotoPath

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

                photo=f.absolutePath

            }catch (e:Exception)
            {

            }


            val diff1=Uri.parse(photo)



            openCropActivity(photoUri, diff1)




        }

        else if (requestCode == UCrop.REQUEST_CROP  && resultCode==Activity.RESULT_OK) {

            val uri = UCrop.getOutput(data!!);

            Log.e("request","check $uri")

            updatedata(photo)

            Glide.with(this).load((photo)).into(profileimage!!)

        }


       /* if (requestCode == REQUEST_IMAGE) {

            if (resultCode == Activity.RESULT_OK) {

                val uri = data!!.data

                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri);

                    // loading profile image from local cache
                   // loadProfile(uri.toString());
                } catch (e:IOException) {
                    e.printStackTrace();
                }
            }
        }*/


    }

    private fun openCropActivity(contentURI: Uri?, contentURI1: Uri?) {

        UCrop.of(contentURI!!, contentURI1!!)
            .withMaxResultSize(700  , 400)
            .withAspectRatio(4f, 3f)
            .start(viewsdata!!.context,this, UCrop.REQUEST_CROP);
    }


    private fun updatedata(path: String) {

        Log.e("function called...","working")

        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var petid by stringPref("petid", null)

            var user= userId?.toInt()

            var petId= petid?.toLong()

            try {
                //val callDetails = splootDB!!.petMasterDao().getAll()

                val viewdata = splootDB!!.petMasterDao().getSelectdata(petid!!,userId!!)

                var pet = petMasterEntity(
                    petId = petId,
                    userId= user,
                    petName = viewdata.petName,
                    age = viewdata.age,
                    sex = viewdata.sex,
                    petCategoryId = viewdata.petCategoryId,
                    breedId= viewdata.breedId,
                    photo = path,
                    active = 0
                )

                val callDetails = splootDB!!.petMasterDao().update(pet)

                val get = splootDB!!.petMasterDao().getSelect(petid!!)

                Log.e("tabledata",""+get)

            } catch (e: Exception) {

                val s = e.message

                Log.e("Error",s)

            }
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
            MediaScannerConnection.scanFile(
                viewsdata?.context,
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
        val IMAGE_DIRECTORY = "/demonuts"
    }


}