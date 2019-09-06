package emarge.project.caloriecaffe.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

import java.io.Serializable



 class LoginUser {


    @SerializedName("userID")
    var userID: String? = null

    @SerializedName("nic")
    var nic: String? = null


    @SerializedName("email")
    var email: String? = null


    @SerializedName("userStatus")
    var userStatus: Boolean? = null


    var isLoginSuccess: Boolean? = null

    var userLoginErrorMsg: String? = null


    var isSingUpSuccess: Boolean? = null
    var isSingUpVerificationSuccess: Boolean? = null
    var userSingUpErrorMsg: String? = null



    var isLoginCodeVerificationSuccess: Boolean? = null
    var userLoginCodeErrorMsg: String? = null


    var userSingUpVerificationError: String? = null





 }