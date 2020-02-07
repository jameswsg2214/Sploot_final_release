package com.work.sploot.activities

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import io.reactivex.Observable
import com.work.sploot.R
import com.work.sploot.SplootAppDB
import com.work.sploot.data.ConstantMethods
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.password
import kotlinx.android.synthetic.main.activity_signup.reset
import com.work.sploot.api.ApiProduction
import com.work.sploot.api.request.GmailFbsignupReq
import com.work.sploot.api.request.LoginRequest
import com.work.sploot.api.request.SendotpRequest
import com.work.sploot.api.request.VerifiedOtpRequest
import com.work.sploot.api.response.VerifiedOtpResponse
import com.work.sploot.api.response.LoginResponse
import com.work.sploot.api.response.SendotpResponse
import com.work.sploot.api.service.CommonServices
import com.work.sploot.data.stringPref
import com.work.sploot.rx.RxAPICallHelper
import com.work.sploot.rx.RxAPICallback
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class Signup : AppCompatActivity() {
    var ifcolour=true
    var ifcheck=false
    var otpclick=true
    var check:Boolean=true

    private var splootDB: SplootAppDB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        splootDB = SplootAppDB.getInstance(this)

        //gmail athtndication
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signupwithgmail.setOnClickListener {

            when {

                ConstantMethods().checkNetwork(this) ->{

                    val signInIntent = mGoogleSignInClient.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)

                    mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this) {
                        }
                }
                else -> Toast.makeText(this@Signup, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show()
            }



        }




//        backtologin.setOnClickListener {
//            startActivity(Intent(this,MainActivity::class.java))
//            finish()
//        }
        sendotp.setOnClickListener {

            val validate=ConstantMethods().emailvalidation(emailid.text.toString().trim())
            when {
                emailid.text.isNullOrEmpty() -> emailid?.error ="Email cannot be empty."
                !validate-> emailid?.error ="Invalid email format"
                ConstantMethods().checkNetwork(this) -> {

                    val normaldate=emailid.text.toString().trim()

                    var modifiedString=""

                    for (c in normaldate) {
                        modifiedString += when {
//                            c.isLowerCase() -> c.toUpperCase()
                            c.isUpperCase() -> c.toLowerCase()
                            else -> c
                        }
                    }

                    Log.e("mod","$modifiedString")




                    otpgendrate(modifiedString)
                    otp.isEnabled=true
                }
                else -> Toast.makeText(this@Signup, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show()
            }
        }
        //Verifed otp
        VerifyOTP.setOnClickListener {
            val validate=ConstantMethods().emailvalidation(emailid.text.toString().trim())
            val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            Log.e("msg","size ${otp.text.length}")
            when {
                emailid.text.isNullOrEmpty() -> emailid?.error ="Email field cannot be empty."
                otpclick-> emailid?.error ="Please send otp first"
                !validate-> emailid?.error ="Invalid email format"
                otp.text.isNullOrEmpty() -> otp?.error ="OTP field cannot be empty."
                otp.text.length < 6 -> otp?.error ="Error"
                else -> {

                    val normaldate=emailid.text.toString().trim()

                    var modifiedString=""

                    for (c in normaldate) {
                        modifiedString += when {
//                            c.isLowerCase() -> c.toUpperCase()
                            c.isUpperCase() -> c.toLowerCase()
                            else -> c
                        }
                    }

                    Log.e("mod","$modifiedString")


                    otpsubmit(otp.text.toString(), modifiedString)
                }
            }
        }
        //Show password
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
        var ifckeck2=true
        con_reset.setOnClickListener {
            if(ifckeck2){
                confirmps.transformationMethod = HideReturnsTransformationMethod.getInstance()
                con_reset.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, // Context
                        R.drawable.cateye // Drawable
                    )
                )
                ifckeck2=false
            }
            else{
                confirmps.transformationMethod = PasswordTransformationMethod.getInstance()
                con_reset.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, // Context
                        R.drawable.password_hide // Drawable
                    )
                )
                ifckeck2=true
            }
        }
        submitpassword.setOnClickListener {
            when {
                password.text.trim().isNullOrEmpty() -> password?.error ="Password can't be empty."
                password.text.length < 8  -> password?.error ="Password must be at least 8 characters length"
                confirmps.text.isNullOrEmpty() -> confirmps?.error ="Confirm password can't be empty"
                password.text.toString().trim() != confirmps.text.toString().trim() -> {

                    confirmps?.error ="Password doesn’t match"

                }

                confirmps.text.length < 8  -> confirmps?.error ="Password must be at least 8 characters length"
                else -> {
                    val normaldate=emailid.text.toString().trim()

                    var modifiedString=""

                    for (c in normaldate) {
                        modifiedString += when {
//                            c.isLowerCase() -> c.toUpperCase()
                            c.isUpperCase() -> c.toLowerCase()
                            else -> c
                        }
                    }

                    Log.e("mod","$modifiedString")


                    passwordsubmit( modifiedString,password.text.toString())
                }
            }
        }
        signupwithfb.setOnClickListener {
            when {

                ConstantMethods().checkNetwork(this) -> {


                    LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))

                    LoginManager.getInstance().registerCallback(callbackManager, object :

                        FacebookCallback<LoginResult> {

                        override fun onSuccess(loginResult: LoginResult) {
                            Log.e("Worked","Check2 Sucesss")
                            val request = GraphRequest.newMeRequest(loginResult.accessToken) {
                                    `object`, response ->
                                Log.e("LoginActivity",response.toString())
                                try {
                                    val email = response.jsonObject.getString("email")
                                    val id = response.jsonObject.getString("id")
                                    val name = response.jsonObject.getString("name")
                                    Log.e("LoginActivity email",email)
                                    authsignup(email,id,name,"3")
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            val parameters = Bundle()
                            parameters.putString("fields", "id,name, email")
                            request.parameters = parameters
                            request.executeAsync()
                        }
                        override fun onCancel() {
                            Log.e("message","Cancel")
                            Toast.makeText(this@Signup, "Cancel", Toast.LENGTH_SHORT).show()
                            LoginManager.getInstance().logOut()
                        }
                        override fun onError(exception: FacebookException) {

                            Log.e("message","Error"+exception)

                            Toast.makeText(this@Signup, "Facebook Error $exception", Toast.LENGTH_SHORT).show()

                            LoginManager.getInstance().logOut()
                            // App code
                        }
                    })





                }
                else -> Toast.makeText(this@Signup, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun passwordsubmit(Emailid: String, password: String) {
        var nextpage=Intent(this, MainActivity::class.java)


        var alertDialog = ConstantMethods().setProgressDialog(this)
        alertDialog.show()



        var commService: CommonServices = ApiProduction(this).provideService(CommonServices::class.java)
        val loginRequest = GmailFbsignupReq()
        loginRequest.setloginType("1")
        loginRequest.setuserId(password)
        loginRequest.setemail(Emailid)
        var apiCall: Observable<LoginResponse> = commService.createuser(loginRequest)
        RxAPICallHelper().call(apiCall, object : RxAPICallback<LoginResponse> {
            override fun onSuccess(loginResponse: LoginResponse) {
                if(loginResponse.getStatus()!!)
                {
               /*     Log.e("Sucess","Sucess")
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
                    petimage=null*/
//                    Toast.makeText(this@Signup,
//                        "tokan$accessToken\n username $username \n emailid $useremail$active$userId$loginTime", Toast.LENGTH_SHORT).show()
                    this@Signup.finish()
                    Toast.makeText(this@Signup, "Password created successfully", Toast.LENGTH_SHORT).show()

                    startActivity(nextpage)
                }
                else
                {
                    Toast.makeText(this@Signup, loginResponse.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }
            override fun onFailed(throwable: Throwable) {
                Log.e("addjourney_dategrid"," clicked Throwable:"+throwable.toString())
                Toast.makeText(this@Signup, "Error $throwable", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        })

    }
    private fun otpsubmit(OTP: String, Emailid: String) {
        var alertDialog = ConstantMethods().setProgressDialog(this)
        alertDialog.show()
        var commService: CommonServices = ApiProduction(this).provideService(CommonServices::class.java)
        val verifiedotprequest = VerifiedOtpRequest()
        verifiedotprequest.setemail(Emailid)
        verifiedotprequest.setotp(OTP)
        var apiCall: Observable<VerifiedOtpResponse> = commService.verifiedotp(verifiedotprequest)
        RxAPICallHelper().call(apiCall, object : RxAPICallback<VerifiedOtpResponse> {
            override fun onSuccess(Response: VerifiedOtpResponse) {
                if(Response.getStatus()!!)
                {
                    Toast.makeText(this@Signup, "Verified Successfully", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                    emaillayout.visibility=View.GONE
                    otp.visibility=View.GONE
                    VerifyOTP.visibility=View.GONE
                    submitpassword.visibility=View.VISIBLE
                    passwordlayout.visibility=View.VISIBLE
                    conform_passwordlayout.visibility=View.VISIBLE
                }
                else
                {
                    Toast.makeText(this@Signup, Response.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }
            override fun onFailed(throwable: Throwable) {
                Log.e("addjourney_dategrid", " clicked Throwable:$throwable")
                Toast.makeText(this@Signup, "Error", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        })
     //   Toast.makeText(this, "$OTP $Emailid",Toast.LENGTH _LONG).show()
    }
    private fun otpgendrate(Emailid: String) {
     //   Toast.makeText(this, "$Emailid",Toast.LENGTH_LONG).show()

        Log.e("gendrateapi","Sucess")

        var alertDialog = ConstantMethods().setProgressDialog(this)

        alertDialog.show()

        var commService: CommonServices = ApiProduction(this).provideService(CommonServices::class.java)

        val otpRequest = SendotpRequest()

        otpRequest.setemail(Emailid)

        var apiCall: Observable<SendotpResponse> = commService.sendotp(otpRequest)

        RxAPICallHelper().call(apiCall, object : RxAPICallback<SendotpResponse> {

            override fun onSuccess(Response: SendotpResponse) {

                if(Response.getStatus()!!)
                {

                    if(check) {
                        Toast.makeText(this@Signup, "OTP has been sent to your registered mail ID $Emailid", Toast.LENGTH_SHORT).show()
                        check=false
                    }else{
                        Toast.makeText(this@Signup, "OTP has been Resent to your registered mail ID $Emailid", Toast.LENGTH_SHORT).show()

                    }

                    alertDialog.dismiss()
                    sendotp.text = "Resend"
                    otpclick=false
                }
                else
                {
                    Toast.makeText(this@Signup, Response.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                   // otpclick=false
                }
            }
            override fun onFailed(throwable: Throwable) {
                Log.e("addjourney_dategrid", " clicked Throwable:$throwable")
                Toast.makeText(this@Signup, "Error", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()

            }
        })
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

            responcedata()

        } catch (e: ApiException) {

            Log.e("error", "signInResult:failed code=" + e.statusCode)

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
            var str:String= "Psersonal name $personName given name $personGivenName familyname $personFamilyName email $personEmail id $personId "//$personPhoto"

            if(personName==null){
                personName="no name"
            }

            authsignup(personEmail!!, personId!!, personName!!,"2")
        }
    }
    private fun authsignup(email: String, id: String, name: String,type:String) {
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
                    Toast.makeText(this@Signup, "Password created successfully", Toast.LENGTH_SHORT).show()
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

                                petid = data.petId.toString()

                                petimage = data.photo

                            }


                        } catch (e: java.lang.Exception) {
                            val s = e.message
                            Log.e("Error",s)
                        }
                    }



                    this@Signup.finish()
                    startActivity(nextpage)
                }
                else
                {
                    Toast.makeText(this@Signup, loginResponse.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                    LoginManager.getInstance().logOut()
                }
            }
            override fun onFailed(throwable: Throwable) {

                Log.e("addjourney_dategrid"," clicked Throwable:"+throwable.toString())

                Toast.makeText(this@Signup, "Error $throwable", Toast.LENGTH_SHORT).show()

                LoginManager.getInstance().logOut()

                alertDialog.dismiss()

            }
        })
    }

}
