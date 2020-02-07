package com.work.sploot.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.EditText
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
import com.work.sploot.R
import com.work.sploot.api.ApiProduction
import com.work.sploot.api.request.GmailFbsignupReq
import com.work.sploot.api.request.SendotpRequest
import com.work.sploot.api.request.VerifiedOtpRequest
import com.work.sploot.api.response.LoginResponse
import com.work.sploot.api.response.SendotpResponse
import com.work.sploot.api.response.VerifiedOtpResponse
import com.work.sploot.api.service.CommonServices
import com.work.sploot.data.ConstantMethods
import com.work.sploot.data.stringPref
import com.work.sploot.rx.RxAPICallHelper
import com.work.sploot.rx.RxAPICallback
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_forgetpassword.*
import kotlinx.android.synthetic.main.activity_forgetpassword.forgetpassword
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class Forgetpassword : AppCompatActivity() {

    var ifcolour=true

    var otpclick=true

    var check:Boolean=true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forgetpassword)

        sendotpforforget.setOnClickListener {

            val validate=ConstantMethods().emailvalidation(forgetemailid.text.toString().trim())
            when {
                forgetemailid.text.isNullOrEmpty() -> forgetemailid?.error ="Email cannot be empty."
                !validate-> forgetemailid?.error ="Invalid email format"
                ConstantMethods().checkNetwork(this) -> {

                    val normaldate=forgetemailid.text.toString().trim()

                    var modifiedString=""

                    for (c in normaldate) {
                        modifiedString += when {

                            c.isUpperCase() -> c.toLowerCase()
                            else -> c
                        }
                    }

                    Log.e("mod","$modifiedString")

                    otpgendrate(modifiedString)

                    forgetotp.isEnabled=true
                }
                else -> Toast.makeText(this@Forgetpassword, getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show()
            }
        }
        createpassword.setOnClickListener {

            val validate=ConstantMethods().emailvalidation(forgetemailid.text.toString().trim())

            when {
                forgetemailid.text.isNullOrEmpty() -> forgetemailid?.error ="Email field cannot be empty."
                !validate-> forgetemailid?.error ="Invalid email format"
                otpclick-> forgetemailid?.error ="Please send otp first"
                forgetotp.text.isNullOrEmpty() -> forgetotp?.error ="OTP field cannot be empty"
                forgetotp.text.length<6 -> forgetotp?.error ="Invalid otp"

                else -> {

                    val normaldate=forgetemailid.text.toString().trim()

                    var modifiedString=""

                    for (c in normaldate) {
                        modifiedString += when {
//                            c.isLowerCase() -> c.toUpperCase()
                            c.isUpperCase() -> c.toLowerCase()
                            else -> c
                        }
                    }

                    Log.e("mod","$modifiedString")

                    otpsubmit(forgetotp.text.toString(), modifiedString)
                }
            }
        }
        forgetreset.setOnClickListener {
            if(ifcolour){
                forgetpassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                forgetreset.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, // Context
                        R.drawable.cateye // Drawable
                    )
                )
                ifcolour=false
            }
            else{
                forgetpassword.transformationMethod = PasswordTransformationMethod.getInstance()
                forgetreset.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, // Context
                        R.drawable.password_hide // Drawable
                    )
                )
                ifcolour=true
            }
        }
        var ischeck=true
        forget_reset.setOnClickListener {
            if(ischeck){
                forgetconfirmps.transformationMethod = HideReturnsTransformationMethod.getInstance()
                forget_reset.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, // Context
                        R.drawable.cateye
                        // Drawable
                    )
                )
                ischeck=false
            }
            else{
                forgetconfirmps.transformationMethod = PasswordTransformationMethod.getInstance()
                forget_reset.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, // Context
                        R.drawable.password_hide // Drawable
                    )
                )
                ischeck=true
            }
        }
        submitnewpassword.setOnClickListener {
            when {
                forgetpassword.text.trim().isNullOrEmpty() -> forgetpassword?.error ="Password is empty."
                forgetpassword.text.trim().length < 8 ->forgetpassword?.error ="Invalid format"
                forgetconfirmps.text.trim().isNullOrEmpty() -> forgetconfirmps?.error ="Confirm  Password is empty."
                forgetconfirmps.text.trim().length < 8 ->forgetconfirmps?.error ="Invalid format"
                forgetpassword.text.trim().toString() == forgetconfirmps.text.toString() -> {



                    val normaldate=forgetemailid.text.toString().trim()

                    var modifiedString=""

                    for (c in normaldate) {
                        modifiedString += when
                        {
                            c.isUpperCase() -> c.toLowerCase()
                            else -> c
                        }
                    }

                    Log.e("mod","$modifiedString")

                    passwordsubmit(modifiedString,forgetpassword.text.toString())
                }
                else -> {
                    forgetconfirmps.text.clear()
                    forgetconfirmps?.error ="The password does not match"
                }
            }
        }
        gotosignup.setOnClickListener {
            startActivity(Intent(this,Signup::class.java))

        }
        forgetfacebook.setOnClickListener {

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
                            Toast.makeText(this@Forgetpassword, "Cancel", Toast.LENGTH_SHORT).show()
                            LoginManager.getInstance().logOut()
                        }
                        override fun onError(exception: FacebookException) {

                            Log.e("message","Error"+exception)

                            Toast.makeText(this@Forgetpassword, "Facebook Error $exception",Toast.LENGTH_SHORT).show()

                            LoginManager.getInstance().logOut()
                            // App code
                        }
                    })

                }
                else -> Toast.makeText(
                    this@Forgetpassword,
                    getString(R.string.network_connection_error),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        forgetgmail.setOnClickListener {

            when {

                ConstantMethods().checkNetwork(this) -> {


                    val signInIntent = mGoogleSignInClient.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)
                    mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this) {
                        }



                }
                else -> Toast.makeText(
                    this@Forgetpassword,
                    getString(R.string.network_connection_error),
                    Toast.LENGTH_SHORT
                ).show()
            }






        }
    }
    private fun passwordsubmit(emailid: String, newpassword: String) {
        Log.e("resetpassword","ONclicked $emailid &&&& $newpassword")
        var nextpage=Intent(this, MainActivity::class.java)
        var alertDialog = ConstantMethods().setProgressDialog(this)
        alertDialog.show()
        var commService: CommonServices = ApiProduction(this).provideService(CommonServices::class.java)
        val loginRequest = GmailFbsignupReq()
        loginRequest.setloginType("1")
        loginRequest.setuserId(newpassword)
        loginRequest.setemail(emailid)
        var apiCall: Observable<SendotpResponse> = commService.passwordChange(loginRequest)
        RxAPICallHelper().call(apiCall, object : RxAPICallback<SendotpResponse> {
            override fun onSuccess(loginResponse: SendotpResponse) {
                if(loginResponse.getStatus()!!)
                {
                    Toast.makeText(this@Forgetpassword, loginResponse.getMsg(), Toast.LENGTH_SHORT).show()
                    this@Forgetpassword.finish()
                    startActivity(nextpage)
                }
                else
                {
                    Toast.makeText(this@Forgetpassword, loginResponse.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }
            override fun onFailed(throwable: Throwable) {
                Log.e("Forget_Screen", " clicked Throwable:$throwable")
                Toast.makeText(this@Forgetpassword, "Error $throwable", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        })
    }

    private fun otpsubmit(otp: String, emailid: String) {
        var alertDialog = ConstantMethods().setProgressDialog(this)
        alertDialog.show()
        var commService: CommonServices = ApiProduction(this).provideService(CommonServices::class.java)
        val verifiedotprequest = VerifiedOtpRequest()
        verifiedotprequest.setemail(emailid)
        verifiedotprequest.setotp(otp)
        var apiCall: Observable<VerifiedOtpResponse> = commService.verifiedotp(verifiedotprequest)
        RxAPICallHelper().call(apiCall, object : RxAPICallback<VerifiedOtpResponse> {
            override fun onSuccess(Response: VerifiedOtpResponse) {
                if(Response.getStatus()!!)
                {
                    Toast.makeText(this@Forgetpassword, Response.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                    emaillayoutforget.visibility= View.GONE
                    forgetotp.visibility= View.GONE
                    createpassword.visibility= View.GONE
                    submitnewpassword.visibility= View.VISIBLE
                    passwordlayoutforget.visibility= View.VISIBLE
                    confirm_forget.visibility= View.VISIBLE
                }
                else
                {
                    Toast.makeText(this@Forgetpassword, Response.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }
            override fun onFailed(throwable: Throwable) {
                Log.e("otp", " clicked Throwable:$throwable")
                Toast.makeText(this@Forgetpassword, "Error", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        })
    }
    private fun otpgendrate(emailid: String) {
        Log.e("Emailid","ONclicked $emailid")
        Log.e("gendrateapi","Sucess")
        var alertDialog = ConstantMethods().setProgressDialog(this)
        alertDialog.show()
        var commService: CommonServices = ApiProduction(this).provideService(CommonServices::class.java)
        val otpRequest = SendotpRequest()
        otpRequest.setemail(emailid)
        var apiCall: Observable<SendotpResponse> = commService.forgetpasswordotp(otpRequest)
        RxAPICallHelper().call(apiCall, object : RxAPICallback<SendotpResponse> {
            override fun onSuccess(Response: SendotpResponse) {

                if(Response.getStatus()!!)
                {
                    if(check) {
                        Toast.makeText(this@Forgetpassword, "OTP has been sent to your registered mail ID $emailid", Toast.LENGTH_SHORT)
                            .show()
                        check=false
                    }else{
                        Toast.makeText(this@Forgetpassword, "OTP has been Resent to your registered mail ID $emailid", Toast.LENGTH_SHORT)
                            .show()
                    }
                    alertDialog.dismiss()
                    sendotpforforget.text = "Resend"
                    otpclick=false
                }
                else
                {
                    Toast.makeText(this@Forgetpassword, Response.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
            }
            override fun onFailed(throwable: Throwable) {
                Log.e("otp", " clicked Throwable:$throwable")
                Toast.makeText(this@Forgetpassword, "Error", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        })
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
                    stringPref("token",loginResponse.getToken().toString())
                    stringPref("name",loginResponse.getUserDetails()?.userName.toString())
                    stringPref("useremail",loginResponse.getUserDetails()?.emailId.toString())
                    stringPref("active",loginResponse.getUserDetails()?.active.toString())
                    stringPref("userId",loginResponse.getUserDetails()?.userId.toString())
                    stringPref("loginTime", Calendar.getInstance().timeInMillis.toString())
                    stringPref("petid", null)
                    stringPref("petimage","")

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
                    this@Forgetpassword.finish()
                    startActivity(nextpage)
                }
                else
                {
                    Toast.makeText(this@Forgetpassword, loginResponse.getMsg(), Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                    LoginManager.getInstance().logOut()
                }
            }
            override fun onFailed(throwable: Throwable) {
                Log.e("addjourney_dategrid"," clicked Throwable:"+throwable.toString())
                Toast.makeText(this@Forgetpassword, "Error $throwable", Toast.LENGTH_SHORT).show()
                LoginManager.getInstance().logOut()
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
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            //  val personPhoto = acct.photoUrl
            var str:String= "Psersonal name $personName given name $personGivenName familyname $personFamilyName email $personEmail id $personId "//$personPhoto"
            authsignup(personEmail!!, personId!!, personName!!,"2")
        }
    }
}
