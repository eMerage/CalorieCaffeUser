package emarge.project.caloriecaffe.viewModel


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import emarge.project.caloriecaffe.model.DietType
import emarge.project.caloriecaffe.model.District
import emarge.project.caloriecaffe.network.api.APIInterface
import emarge.project.caloriecaffe.network.api.ApiClient
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import android.widget.RadioGroup
import com.google.gson.JsonObject
import emarge.project.caloriecaffe.R
import emarge.project.caloriecaffe.db.UserTB
import emarge.project.caloriecaffe.model.DietPlan
import emarge.project.caloriecaffe.model.DietRequest
import io.realm.Realm


class DietRequestViewModel : ViewModel() {


    private var heightType : Int = 0
    private var waistType : Int = 0


    private var sex : String = ""
    var requestValidationErorr = MutableLiveData<String>()

    var showProgressbar = MutableLiveData<Boolean>()

    val editText_age = MutableLiveData<String>()
    val editText_weight = MutableLiveData<String>()
    val editText_height = MutableLiveData<String>()
    val editText_waist = MutableLiveData<String>()
    val editText_code = MutableLiveData<String>()
    val textview_typeid = MutableLiveData<String>()

    var getingDietTypesErorr = MutableLiveData<String>()
    var dietTypeList = MutableLiveData<ArrayList<DietType>>()


    var requestStatus = MutableLiveData<DietRequest>()

    var dietRequestError = MutableLiveData<String>()

  //  var showProgressbar = MutableLiveData<Boolean>()

    var apiInterface : APIInterface? = null
    var cm: ConnectivityManager? = null
    lateinit var realm: Realm

    fun setViewListener(con : Context) {
        apiInterface = ApiClient.client()
        cm = con.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    fun onSplitTypeChanged(radioGroup: RadioGroup,id: Int) {
       sex = if(id == R.id.radioButton1){
            "F"
        }else if(id == R.id.radioButton2){
            "M"
        }else{
            "O"
        }

    }

    fun onSplitHeightTypeChanged(radioGroup: RadioGroup,id: Int) {
        heightType = if(id == R.id.radioButton_kg){
            0
        }else{
            1
        }

    }

    fun onSplitWaistTypeChanged(radioGroup: RadioGroup,id: Int) {
        waistType = if(id == R.id.radioButton_in){
            0
        }else{
            1
        }

    }

    fun onRequestClick() {
        when {
            editText_age.value.isNullOrEmpty() -> requestValidationErorr.value = "Please fill the Age"
            sex == "" -> requestValidationErorr.value = "Please select sex"
            editText_weight.value.isNullOrEmpty() -> requestValidationErorr.value = "Please fill Weight"
            editText_height.value.isNullOrEmpty() -> requestValidationErorr.value = "Please fill Height"
            editText_waist.value.isNullOrEmpty() -> requestValidationErorr.value = "Please fill Waist"
            textview_typeid.value.isNullOrEmpty() -> requestValidationErorr.value = "Please select Diet Type"
            editText_code.value.isNullOrEmpty() -> requestValidationErorr.value = "Please fill payment code"
            else -> sendDietRequestToServer()

        }

    }

    fun sendDietRequestToServer(){

        showProgressbar.value = true


        var dietRequestResult: DietRequest? = null
        val jsonObject = JsonObject()



        realm = Realm.getDefaultInstance()

        val userTB = realm.where(UserTB::class.java).findFirst()

        var weight : Double = 0.0
        var height : Double= 0.0
        var waist : Double= 0.0

        weight = if(heightType==0){ editText_weight.value!!.toDouble() }else{ (editText_weight.value!!.toDouble() * 0.453592) }

        if(waistType==0){
            height = editText_height.value!!.toDouble()
            waist = editText_waist.value!!.toDouble()
        }else{
            waist = (editText_waist.value!!.toDouble() * 0.3937007874)
            height = (editText_height.value!!.toDouble() * 0.3937007874)
        }



        jsonObject.addProperty("UserID", userTB!!.userID)
        jsonObject.addProperty("Age", editText_age.value.toString())
        jsonObject.addProperty("Gender", sex)
        jsonObject.addProperty("Weight", weight.toString())
        jsonObject.addProperty("Height", height.toString())
        jsonObject.addProperty("Waist",  waist.toString())
        jsonObject.addProperty("PaymentCardCode",  editText_code.value)
        jsonObject.addProperty("DietTypeID", textview_typeid.value)


        println("vvv :$jsonObject")

        apiInterface!!.saveDietPlanRequest(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DietRequest> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(request: DietRequest) {
                    dietRequestResult = request
                }
                override fun onError(e: Throwable) {
                    dietRequestError.value = "Something went wrong, Please try again"
                    showProgressbar.value = false
                }
                override fun onComplete() {
                    requestStatus.value = dietRequestResult
                    showProgressbar.value = false

                }
            })






    }


    fun getDietType(){

        val activeNetwork: NetworkInfo? = cm!!.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if(!isConnected){
            getingDietTypesErorr.value="No internet connection !"
        }else{
            var dList: ArrayList<DietType>? = null
            apiInterface!!.getAllDiteType("5050")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<DietType>> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(districts: ArrayList<DietType>) {
                        dList = districts
                    }
                    override fun onError(e: Throwable) {
                        getingDietTypesErorr.value="Something went wrong, Please try again"
                    }
                    override fun onComplete() {
                        if (dList!!.isEmpty()) {
                        } else {
                            dietTypeList.value =dList
                        }
                    }
                })

        }


    }

}