package com.work.sploot.api.service


import com.work.sploot.api.request.*
import com.work.sploot.api.response.*
import retrofit2.http.Body
import retrofit2.http.POST
import io.reactivex.Observable

interface CommonServices {

    @POST("auth/login")
    fun login(@Body body: LoginRequest): Observable<LoginResponse>
    //auth/sendOtp

    @POST("auth/sendOtp")
    fun sendotp(@Body body: SendotpRequest): Observable<SendotpResponse>

   @POST("auth/verifyOtp")
   fun verifiedotp(@Body body: VerifiedOtpRequest): Observable<VerifiedOtpResponse>

    @POST("auth/createuserprofile")
    fun userprofile(@Body body: profilereq): Observable<profileres>

    @POST("auth/createUser")
    fun createuser(@Body body: GmailFbsignupReq): Observable<LoginResponse>

    @POST("auth/signupUser")
    fun signupUser(@Body body: GmailFbsignupReq): Observable<LoginResponse>

    @POST("auth/createAndLoginUser")
    fun userLogin(@Body body: GmailFbsignupReq): Observable<LoginResponse>

    @POST("auth/forgetPasswordSendOtp")
    fun forgetpasswordotp(@Body body: SendotpRequest): Observable<SendotpResponse>

    @POST("auth/passwordChange")
    fun passwordChange(@Body body: GmailFbsignupReq): Observable<SendotpResponse>

    @POST("apim/getCountry")
    fun getcountry(): Observable<CountryResponse>

    @POST("apim/getStateByCountryId")
    fun getstatebycountryid(@Body body: StateRequest): Observable<StateResponse>

    @POST("apim/getCityByStateId")
    fun getcitybystateid(@Body body: CityRequest): Observable<CityResponce>

    @POST("apim/getCMSlist")
    fun getcms(): Observable<Cmsdata>

    @POST("/apim/createPet")
    fun createpet(@Body body: pet_register_req): Observable<pet_create_res>

    @POST("apim/createPet")
    fun petcreate(@Body body: Createreq): Observable<Createres>

    @POST("apim/addAppointment")
    fun appointment(@Body body: appointmentreq): Observable<appontmentres>

 @POST("auth/remainderMail")
 fun reminderapi(@Body body: reminderreq): Observable<SendotpResponse>


}