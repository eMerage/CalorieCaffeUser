package emarge.project.caloriecaffe.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DietPlan : Serializable {

    @SerializedName("dietPlanID")
    var dietPlanID: Int? = null

    @SerializedName("dietPlanRequestID")
    var dietPlanRequestID: Int? = null

    @SerializedName("dietPlanUrl")
    var dietPlanImage: String? = null

    @SerializedName("submitDate")
    var submitDate: String? = null

    @SerializedName("expiryDate")
    var expiryDate: String? = null

    @SerializedName("dietPlanStatus")
    var dietPlanStatus: Boolean = false

    @SerializedName("error")
    var dietPlanError: Error? = null



}