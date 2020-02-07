package com.work.sploot.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.work.sploot.R
import com.work.sploot.api.ApiProduction
import com.work.sploot.api.request.LoginRequest
import com.work.sploot.api.response.LoginResponse
import com.work.sploot.api.service.CommonServices
import com.work.sploot.data.ConstantMethods
import com.work.sploot.data.stringPref
import com.work.sploot.rx.RxAPICallHelper
import com.work.sploot.rx.RxAPICallback
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.reset
import java.util.*
import com.google.android.gms.tasks.Task
import com.work.sploot.Entity.petMasterEntity
import com.work.sploot.SplootAppDB
import com.work.sploot.api.request.GmailFbLoginReq
import com.work.sploot.api.request.GmailFbsignupReq
import com.work.sploot.data.PrefDelegate
import kotlinx.android.synthetic.main.activity_forgetpassword.*
import kotlinx.android.synthetic.main.activity_login.forgetpassword
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.Exception as Exception1

const val RC_SIGN_IN=123
var callbackManager: CallbackManager? = null
class LoginActivity : AppCompatActivity() {
    var ifcolour=true

    private var splootDB: SplootAppDB? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        splootDB = SplootAppDB.getInstance(this)


        loginbtn.setOnClickListener {

            val validate=ConstantMethods().emailvalidation(username.text.toString().trim())


            val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            when {
                username.text.isNullOrEmpty() -> username?.error ="Email cannot be empty."
                !validate-> username?.error ="Invalid email format"
                password.text.isNullOrEmpty() -> password?.error ="Password cannot be empty."
                ConstantMethods().checkNetwork(this) ->

                {

                    val normaldate=username.text.toString().trim()

                    var modifiedString=""

                    for (c in normaldate) {
                        modifiedString += when {
//                            c.isLowerCase() -> c.toUpperCase()
                            c.isUpperCase() -> c.toLowerCase()
                            else -> c
                        }
                    }

                    Log.e("mod","$modifiedString")



                    Process1(modifiedString,password.text.toString())
                }
                else -> Toast.makeText(this@LoginActivity, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show()
            }
        }
        signup.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
            //finish()
        }
        reset.setOnClickListener {
            if(ifcolour){
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                reset.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, // Context
                        R.drawable.cateye // Drawable
                    )
                )
                ifcolour=false
            }
            else{
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                reset.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, // Context
                        R.drawable.password_hide // Drawable
                    )
                )
                ifcolour=true
            }
        }
        forgetpassword.setOnClickListener {
            startActivity(Intent(this,Forgetpassword::class.java))
        }
        callbackManager = CallbackManager.Factory.create()
        facebook.setOnClickListener {
            Log.e("Worked","Check1")




            when {
                ConstantMethods().checkNetwork(this) -> {

                    LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
                    LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                        override fun onSuccess(loginResult: LoginResult) {
                            Log.e("Worked","Check2 Sucesss")
                            val request = GraphRequest.newMeRequest(loginResult.accessToken) {
                                    `object`, response ->
                                Log.e("LoginActivity", ".........................................$response")
                                //   responce.text=response.toString()
                                try {
                                    val email = response.jsonObject.getString("email")
                                    val id = response.jsonObject.getString("id")
                                    val name = response.jsonObject.getString("name")
                                    // responce.text.setText("Login Success \n$email")
                                    Log.e("LoginActivity email",email)
                                    authlogin(email,id,name,"3")
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            val parameters = Bundle()
                            //parameters.putString("fields","id,name,email,gender,birthday")
                            parameters.putString("fields", "id,name,email")
                            request.parameters = parameters
                            request.executeAsync()
                        }
                        override fun onCancel() {
                            // App code
                            //  responce.text= "cancel"
                            Log.e("message","Cancel")
                            LoginManager.getInstance().logOut()
                        }
                        override fun onError(exception: FacebookException) {


                            Toast.makeText(this@LoginActivity, "Facebook Error :$exception", Toast.LENGTH_SHORT).show()


                            Log.e("message","Error"+exception)
                            //     responce.text= "cancel"+exception
                            LoginManager.getInstance().logOut()
                            // App code
                        }
                    })



                }
                else -> Toast.makeText(this@LoginActivity, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show()
            }





        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        gmail.setOnClickListener {



            when {

                ConstantMethods().checkNetwork(this) ->  {

                    mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this) {
                            // ...
                            //    Toast.makeText(this,"Signout",Toast.LENGTH_LONG).show()
                        }
                    val signInIntent = mGoogleSignInClient.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)


                }
                else -> Toast.makeText(this@LoginActivity, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show()
            }



        }




    }

    private fun responcedata() {
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            var personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            //  val personPhoto = acct.photoUrl
            var str:String= "name:$personName $personGivenName $personFamilyName mail :$personEmail id:$personId "//$personPhoto"
          //  responce.text=str

            Log.e("logindata","$str")


            if(personName==null){

                personName="no name"

            }


            authlogin(personEmail!!, personId!!,personName!!,"2")


        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("maessage",""+requestCode)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        else{
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
           // responce.text= account!!.displayName
            responcedata()
        } catch (e: ApiException) {
            Log.e("error", "signInResult:failed code=" + e.statusCode)
        }
    }
    private fun Process1(username: String, passwords: String) {
        var nextpage=Intent(this, firstpage::class.java)

        var alertDialog = ConstantMethods().setProgressDialog(this)
        alertDialog.show()
        var commService: CommonServices = ApiProduction(this).provideService(CommonServices::class.java)
        val loginRequest = LoginRequest()
        loginRequest.setPassword(passwords)
        loginRequest.setUserName(username)
        var apiCall: Observable<LoginResponse> = commService.login(loginRequest)
        RxAPICallHelper().call(apiCall, object : RxAPICallback<LoginResponse> {
            override fun onSuccess(loginResponse: LoginResponse) {
                if(loginResponse.getStatus()!!)
                {
                    Log.e("Sucess","Sucess")
                    stringPref("token",loginResponse.getToken().toString())
                    stringPref("name",loginResponse.getUserDetails()?.userName.toString())
                    stringPref("useremail",loginResponse.getUserDetails()?.emailId.toString())
                    stringPref("active",loginResponse.getUserDetails()?.active.toString())
                    stringPref("userId",loginResponse.getUserDetails()?.userId.toString())
                    stringPref("loginTime", Calendar.getInstance().timeInMillis.toString())
                    stringPref("petid", null)

                    stringPref("petimage",null)
                    val android_id = Settings.Secure.getString(
                        applicationContext.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    var accessToken by stringPref("access_token", null)
                    var username by stringPref("name", null)
                    var useremail by stringPref("useremail", null)
                    var userdesignation by stringPref("userdesignation", null)
                    var active by stringPref("active", null)
                    var userId by stringPref("userId", null)
                    var loginTime by stringPref("loginTime", null)
                    var petid by stringPref("petid", null)
                    var petimage by stringPref("petimage", null)

                    accessToken =  loginResponse.getToken().toString();
                    username = loginResponse.getUserDetails()?.userName.toString()
                    useremail = loginResponse.getUserDetails()?.emailId.toString()
                    active = loginResponse.getUserDetails()?.active.toString()
                    userId =  loginResponse.getUserDetails()?.userId.toString()
                    loginTime = Calendar.getInstance().timeInMillis.toString()

                    AsyncTask.execute {

                        try {
                            val callDetails = splootDB!!.petMasterDao()

                            val check=callDetails.checkAll(userId!!)

                            if(check) {

                                val data = callDetails.view_one(userId!!)

                                Log.e("data_logon","$data")

                                petid = data.petId.toString()

                                petimage = data.photo

                                this@LoginActivity.finish()
                                startActivity(nextpage)

                            }
                            else{

                                Log.e("data_logon","no")

                                var user= userId?.toInt()

                                var pet = petMasterEntity(
                                    userId= user
                                )
                                var repocecreate=callDetails.insertAll(pet)

                                Log.e("rsponceof create","pet id"+repocecreate)

                                val viewdata = splootDB!!.petMasterDao().getparticulerpet()

                                Log.e("INseted", "worked   ${viewdata.petId}")

                                petid= viewdata.petId.toString()

                                nextpage=  Intent(this@LoginActivity, Petregister::class.java)

                                this@LoginActivity.finish()

                                startActivity(nextpage)

                            }


                        } catch (e: Exception) {
                            val s = e.message
                            Log.e("Error",s)
                        }
                    }



                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()

                }
                else
                {
                    Toast.makeText(this@LoginActivity, loginResponse.getMsg(), Toast.LENGTH_SHORT).show()
                   password.text.clear()
                    alertDialog.dismiss()
                }
            }
            override fun onFailed(throwable: Throwable) {
                Log.e("addjourney_dategrid"," clicked Throwable:"+throwable.toString())
                Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        })
    }
    private fun authlogin(email: String, id: String, name: String,type:String) {
        //signupUser
        var nextpage=Intent(this, firstpage::class.java)
        var alertDialog = ConstantMethods().setProgressDialog(this)
        alertDialog.show()
        var commService: CommonServices = ApiProduction(this).provideService(CommonServices::class.java)
        val signupRequest = GmailFbsignupReq()
        signupRequest.setloginType(type)
        signupRequest.setuserId(id)
        signupRequest.setemail(email)
        signupRequest.setname(name)
        var apiCall: Observable<LoginResponse> = commService.userLogin(signupRequest)
        RxAPICallHelper().call(apiCall, object : RxAPICallback<LoginResponse> {
            override fun onSuccess(loginResponse: LoginResponse) {
                if(loginResponse.getStatus()!!)
                {
                    Log.e("Sucess","Sucess")

                    stringPref("token",loginResponse.getToken().toString())

                    stringPref("name",loginResponse.getUserDetails()?.userName.toString())

                    stringPref("useremail",loginResponse.getUserDetails()?.emailId.toString())

                    stringPref("active",loginResponse.getUserDetails()?.active.toString())

                    stringPref("userId",loginResponse.getUserDetails()?.userId.toString())

                    stringPref("loginTime", Calendar.getInstance().timeInMillis.toString())

                    stringPref("petid", null)

                    stringPref("petimage",null)
                    val android_id = Settings.Secure.getString(
                        applicationContext.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    var accessToken by stringPref("access_token", null)
                    var username by stringPref("name", null)
                    var useremail by stringPref("useremail", null)
                    var userdesignation by stringPref("userdesignation", null)
                    var active by stringPref("active", null)
                    var userId by stringPref("userId", null)
                    var loginTime by stringPref("loginTime", null)
                    var petid by stringPref("petid", null)
                    var petimage by stringPref("petimage", null)



                    accessToken =  loginResponse.getToken().toString();
                    username = loginResponse.getUserDetails()?.userName.toString()
                    useremail = loginResponse.getUserDetails()?.emailId.toString()
                    active = loginResponse.getUserDetails()?.active.toString()
                    userId =  loginResponse.getUserDetails()?.userId.toString()
                    loginTime = Calendar.getInstance().timeInMillis.toString()
                    petid=null
                    petimage=null


                    AsyncTask.execute {

                        try {
                            val callDetails = splootDB!!.petMasterDao()

                            val check=callDetails.checkAll(userId!!)

                            if(check) {

                                val data = callDetails.view_one(userId!!)

                                Log.e("data_logon","$data")

                                petid = data.petId.toString()

                                petimage = data.photo

                                this@LoginActivity.finish()
                                startActivity(nextpage)

                            }
                            else{

                                Log.e("data_logon","no")

                                var user= userId?.toInt()

                                var pet = petMasterEntity(
                                    userId= user
                                )
                                var repocecreate=callDetails.insertAll(pet)

                                Log.e("rsponceof create","pet id"+repocecreate)

                                val viewdata = splootDB!!.petMasterDao().getparticulerpet()

                                Log.e("INseted", "worked   ${viewdata.petId}")

                                petid= viewdata.petId.toString()

                                nextpage=  Intent(this@LoginActivity, Petregister::class.java)

                                this@LoginActivity.finish()

                                startActivity(nextpage)

                            }


                        } catch (e: Exception) {
                            val s = e.message
                            Log.e("Error",s)
                        }
                    }

                    alertDialog.dismiss()

                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this@LoginActivity, loginResponse.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                    LoginManager.getInstance().logOut()
                }
            }
            override fun onFailed(throwable: Throwable) {
                Log.e("addjourney_dategrid"," clicked Throwable:"+throwable.toString())
                Toast.makeText(this@LoginActivity, "Error $throwable", Toast.LENGTH_SHORT).show()
                LoginManager.getInstance().logOut()
                alertDialog.dismiss()
            }
        })
    }

}