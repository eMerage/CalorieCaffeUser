package emarge.project.caloriecaffe.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

  class Error  {

     @SerializedName("code") val code: String = ""
     @SerializedName("description") val description: String = ""


      constructor()


  }