package emarge.project.caloriecaffe.network.api


import com.google.gson.JsonObject
import emarge.project.caloriecaffe.model.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

import java.util.ArrayList

/**
 * Created by kamal on 8/2/18.
 */
interface APIInterface{

    @GET("District/GetAllDistricts")
    fun getAllDistricts(@Query("TokenID") tokenID: String): Observable<ArrayList<District>>

    @GET("DietPlan/GetAllDietTypes")
    fun getAllDiteType(@Query("TokenID") tokenID: String): Observable<ArrayList<DietType>>


    @POST("User/SaveUser")
    fun saveUser(@Body userInfo: JsonObject): Observable<LoginUser>


    @GET("User/ValidateSignupCode")
    fun validateSignupCode(@Query("NIC") nic: String,@Query("signupCode") code: String): Observable<LoginUser>

/*

    @GET("User/ValidateUser")
    fun validateUser(@Query("NIC") nic: String,@Query("PushTokenID") push: String): Observable<LoginUser>
*/

    @GET("User/ValidateUser")
    fun validateUser(@Query("NIC") nic: String,@Query("PushTokenID") push: String): Observable<JsonObject>


    @GET("User/ValidateLoginCode")
    fun validateLoginCode(@Query("NIC") nic: String,@Query("loginCode") code: String): Observable<JsonObject>


    @POST("DietPlan/SaveDietPlanRequest")
    fun saveDietPlanRequest(@Body dietInfo: JsonObject): Observable<DietRequest>


  //  @GET("DietPlan/GetCurrentPlanForUser")
   // fun getCurrentPlanForUser(@Query("userID") nic: String): Observable<DietRequest>

    @GET("DietPlan/GetCurrentPlanForUser")
    fun getCurrentPlanForUser(@Query("userID") nic: String): Observable<JsonObject>

}
