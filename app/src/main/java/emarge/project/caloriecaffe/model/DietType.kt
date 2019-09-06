package emarge.project.caloriecaffe.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DietType : Serializable {

    @SerializedName("dietTypeID")
    var id: Int = 0

    @SerializedName("dietTypeName")
    lateinit var name: String



}