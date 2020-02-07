package com.work.sploot.activities

import android.Manifest
import android.app.Activity
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
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.work.sploot.Entity.photoagalley
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.stringPref
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.photo_view.view.*
import kotlinx.android.synthetic.main.photofeild.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class photo_update_fragement:Fragment()  {

    private var empty: Bitmap? = null

    var image1: ImageView?=null
    var image2: ImageView?=null
    var image3: ImageView?=null
    var image4: ImageView?=null
    var image5: ImageView?=null

    var defult: ImageView?=null

    var gridView:RecyclerView?=null

    var path=""

    var postion=0

    private var splootDB: SplootAppDB? = null

    private var imageview: ImageView? = null


    private lateinit var viewdata:View

    private val GALLERY = 1

    private val CAMERA = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val views = inflater.inflate(R.layout.photofeild, container, false)

        splootDB = SplootAppDB.getInstance(views.context)

        gridView=views.findViewById(R.id.photo_viewer)

        Log.e("type..","$type_layout")

        var select_photo by stringPref("select_photo", null)

        select_photo="0"


        if(type_layout!=1){

            views.photo_heading_view.visibility=View.VISIBLE

            views.upload_layout.visibility=View.GONE

            var select_date by stringPref("select_date", null)

            select_date=date_select

        }
        else{


            var select_date by stringPref("select_date", null)

            var calenderdate by stringPref("calenderdate", null)

            select_date=date_select

            views.photo_heading_view.visibility=View.GONE

            views.upload_layout.visibility=View.VISIBLE

        }

        views.photo_fragment_close.setOnClickListener {

            val mContext = activity

            val manager = mContext?.supportFragmentManager

            val transaction = manager?.beginTransaction()

            val ct=manager!!.backStackEntryCount


            for (i in 0 until ct) {

                manager!!.popBackStack()

            }

           /* transaction?.addToBackStack(null)*/

            try {


                transaction?.replace(R.id.view_pager, calanderLayout.newInstance())

                transaction?.commit()

            }
            catch (e:Exception){


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

            showDialog(postion,views)

        }
/*
        views.upload_image.setOnClickListener{

            val mContext = activity

            val fm = mContext?.supportFragmentManager

            val transaction = fm?.beginTransaction()

            transaction?.replace(R.id.frag_for_view, view_pager_fragement.newInstance(
                date_select!!,
                2
            ))

            transaction?.commit()
        }*/

        views.imageshare.setOnClickListener {

           // var outputFile: File? =null

            var userId by stringPref("userId", null)

            var user = userId?.toInt()

            var petid by stringPref("petid", null)

            var perId = petid!!.toLong()

            var select_date by stringPref("select_date", null)

            var select_photoid by stringPref("select_photoid", null)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            Log.e("selected","delete page $select_photoid")

            val output = formatter.parse(select_date)

            AsyncTask.execute {

                var select_photoid by stringPref("select_photoid", null)

                try {

                    val callDetails = splootDB!!.petMasterDao()

                        val data=callDetails.get_selected_image(output,user!!,perId)

                        val share = Intent(Intent.ACTION_SEND)

                    share.setType("image/jpeg");

                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(data[postion].photopath));

                        startActivity(Intent.createChooser(share, "Share Image"))

                }
                catch (e:Exception){
                    Log.e("photodelete","$e")
                }
            }


        }

        views.upload_image.setOnClickListener {


                try {


                    val mContext = activity

                    val fm = mContext?.supportFragmentManager

                    val transaction = fm?.beginTransaction()

                    transaction?.replace(
                        R.id.frag_for_view, view_pager_fragement.newInstance(
                            date_select!!,
                            2
                        )
                    )

                    transaction?.commit()

                } catch (e: Exception) {


                }
        }

        views.imagecapture.setOnClickListener {

            Log.e("postiom","$postion")

            if (postion<4) {


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
                Toast.makeText(views.context,"Five photos only to upload", Toast.LENGTH_LONG).show()
            }

        }
        views.galleryphotoupload.setOnClickListener {

            if (postion<4) {

                var RECORD_REQUEST_CODE = 101


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
            else{
                Toast.makeText(views.context,"Five photos only to upload", Toast.LENGTH_LONG).show()
            }

        }
        return views
    }

    private fun delete_image() {

        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var user = userId?.toInt()

            var petid by stringPref("petid", null)

            var perId = petid!!.toLong()

            var select_date by stringPref("select_date", null)

            var select_photoid by stringPref("select_photoid", null)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            Log.e("selected","delete page $select_photoid")

            val output = formatter.parse(select_date)

            try {

                val callDetails = splootDB!!.petMasterDao()

                val data=callDetails.get_selected_image(output,user!!,perId)

                Log.e("postiondata","data $postion")

                val delete_imge=callDetails.image_delete(data[postion].PhotoId!!)

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

        if (!wallpaperDirectory.exists()) {

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

        if (requestCode == GALLERY) {
            if (data != null)
            {

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

                        path=f.absolutePath

                    }catch (e:Exception)
                    {

                    }


                    val diff1=Uri.parse(path)

                    openCropActivity(contentURI, diff1)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        else if (requestCode == CAMERA && resultCode== Activity.RESULT_OK)
        {
            var photoUri = Uri.fromFile(File(currentPhotoPath))

            path=currentPhotoPath

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

                path=f.absolutePath

            }catch (e:Exception)
            {

            }


            val diff1=Uri.parse(path)

            openCropActivity(photoUri, diff1)


        }

        else if (requestCode == UCrop.REQUEST_CROP  && resultCode==Activity.RESULT_OK) {

            val uri = UCrop.getOutput(data!!);

            Log.e("request","check $uri")

            process(path,postion)

            Glide.with(this).load((path)).into(imageview!!)

        }


    }

    private fun openCropActivity(contentURI: Uri?, contentURI1: Uri?) {

        UCrop.of(contentURI!!, contentURI1!!)
            .withMaxResultSize(700  , 400)
            .withAspectRatio(4f, 3f)
            .start(viewdata!!.context,this, UCrop.REQUEST_CROP);
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


        var date_select:String?=null

        fun newInstance(type: Int, dateSelect: String?): photo_update_fragement {

            date_select=dateSelect

            type_layout=type

            return photo_update_fragement()
        }
    }

    private fun showDialog(postions: Int,views: View) {

        var dialog: AlertDialog

        val builder = AlertDialog.Builder(views.context)

        builder.setTitle("Delete Photo")

        builder.setMessage("Are you sure you to Delete?")

        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->

            when(which){

                DialogInterface.BUTTON_POSITIVE ->
                {

                    Log.e("position","$postions")


                    delete_image()


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





    private fun process(filepath: String?,Image_postion: Int) {

        viewdata.photofunlayout.visibility=View.VISIBLE

        AsyncTask.execute {

            var userId by stringPref("userId", null)

            var user= userId?.toInt()

            var petid by stringPref("petid", null)

            var perId= petid!!.toLong()

            var select_date by stringPref("select_date", null)

            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val output = formatter.parse(date_select)

            var select_photo by stringPref("select_photo", null)


            try {

                val callDetails = splootDB!!.petMasterDao()

                if(postion<5){


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
                    //  postion=1

                    postion=viewall.size

                    updateimage()


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

            Log.e("select","$select_date")


            if(select_date==null){

                val cur=Date()

                val formatter = SimpleDateFormat("dd/MM/yyyy")

                val date=formatter.format(cur)

                select_date=date

            }


            val output = formatter.parse(select_date)

            try {

                val callDetails = splootDB!!.petMasterDao()

//              val isempty=callDetails.check_seleted_date(output, user!!,perId)

                val data_check=callDetails.check_selected_image(output,user!!,perId)

                if(data_check){

                    val data=callDetails.get_selected_image(output,user!!,perId)

                    postion=data.size-1

                    Log.e("update","position $postion")

                    gridView?.post(Runnable {

                        gridView?.visibility=View.VISIBLE

                        gridView?.layoutManager = GridLayoutManager(this.context,5)

                        val adapter = PhotoAdapter(data,viewdata)

                        gridView?.adapter = adapter

                    })

                    viewdata.photofunlayout.post(Runnable {

                        viewdata.photofunlayout.visibility=View.VISIBLE

                    })

                }

                else{

                    Log.e("no data","Found")

                    gridView?.post(Runnable {

                        gridView?.visibility=View.INVISIBLE


                    })

                    viewdata.photofunlayout.post(Runnable {

                        viewdata.photofunlayout.visibility=View.GONE

                    })

                    viewdata.uploadimage.post(Runnable {

                        viewdata.uploadimage.setImageBitmap(empty)

                    })


                }


            } catch (e: Exception) {
                val s = e.message;
                Log.e("Error photo",s)
            }
        }
    }


    inner class PhotoAdapter(var userList: List<photoagalley>, viewdata: View) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

        var viewesdata:View?=null

        var viewdata=viewdata

        private var splootDB: SplootAppDB? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val v = LayoutInflater.from(parent.context).inflate(R.layout.photo_view, parent, false)

            viewesdata=v

            return ViewHolder(v)

        }

        override fun onBindViewHolder(holder: ViewHolder, positions: Int) {

            holder.bindItems(userList[positions],viewesdata)

            var select_photo by stringPref("select_photo", null)

            var select_photoid by stringPref("select_photoid", null)


            if(select_photo != null){



                select_photo=null

            }

            val siz=userList.size

            var photoUr: Uri = Uri.fromFile(File(userList[siz-1].photopath))

            Glide.with(viewesdata!!.context).load(photoUr).into(viewdata.uploadimage)


            holder.itemView.photo_viewer_cad.setOnClickListener {

                select_photoid=userList[positions].PhotoId.toString()

                Log.e("selected","delete $select_photoid")

                select_photo=positions.toString()

                Log.e("position","$positions ${userList[positions].PhotoId}")

                var photoUri2: Uri = Uri.fromFile(File(userList[positions].photopath))

                Glide.with(viewesdata!!.context).load(photoUri2).into(viewdata.uploadimage)


                postion=positions

            }
        }

        override fun getItemCount(): Int {

            return userList.size

        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(user: photoagalley, viewesdata: View?) {

                val path = itemView.findViewById(R.id.list_image) as ImageView

                val card=itemView.findViewById<CardView>(R.id.photo_viewer_cad)

                var photoUri1: Uri = Uri.fromFile(File(user.photopath))

                Glide.with(viewesdata!!.context).load(photoUri1).into(path!!)

            }
        }


    }

}


