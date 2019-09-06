package emarge.project.caloriecaffe.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class District : Serializable  {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    lateinit var name: String



}


