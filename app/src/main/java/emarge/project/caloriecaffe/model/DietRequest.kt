package emarge.project.caloriecaffe.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

class DietRequest : Serializable {

    @SerializedName("userID")
    @Expose
    var userID: String? = null

    @SerializedName("dietPlanRequestID")
    @Expose
    var dietPlanRequestID: Int? = null

    @SerializedName("age")
    @Expose
    var age: Int? = null

    @SerializedName("weight")
    @Expose
    var weight: Double? = null

    @SerializedName("height")
    @Expose
    var height: Double? = null

    @SerializedName("waist")
    @Expose
    var waist: Double? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("paymentCardID")
    @Expose
    var paymentCardID: Long? = null

    @SerializedName("dietTypeID")
    @Expose
    var dietTypeID: Int? = null

    @SerializedName("dietTypeName")
    @Expose
    var dietTypeName: String? = null

    @SerializedName("dateStamp")
    @Expose
    var dateStamp: String? = null


    @SerializedName("dietPlanRequestStatus")
    @Expose
    var dietPlanRequestStatus: Boolean = false


    @SerializedName("isDietPlanSubmitted")
    @Expose
    var isDietPlanSubmitted: Boolean = false


    @SerializedName("error")
    @Expose
    var dietRequestError: Error? = null


    @SerializedName("dietPlanList")
    @Expose
    lateinit var dietPlan: ArrayList<DietPlan>


}