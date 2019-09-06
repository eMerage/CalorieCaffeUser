package emarge.project.caloriecaffe.viewModel



import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject

import emarge.project.caloriecaffe.R
import emarge.project.caloriecaffe.db.UserTB
import emarge.project.caloriecaffe.model.DietRequest
import emarge.project.caloriecaffe.network.api.APIInterface
import emarge.project.caloriecaffe.network.api.ApiClient
import emarge.project.caloriecaffe.ui.activity.dietRequest.ActivityDietRequest
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import io.realm.Realm
import org.json.JSONObject





class DietPlanViewModel : ViewModel() {

    val requestNumber = ObservableField<String>()
    val gender = ObservableField<String>()
    val weight = ObservableField<String>()
    val height = ObservableField<String>()
    val waist = ObservableField<String>()
    val type = ObservableField<String>()
    val date = ObservableField<String>()
    val status = ObservableField<String>()


    var dietPlanGetingErorr = MutableLiveData<String>()


    var dietPlan = MutableLiveData<String>()

    var mContext: Activity? = null
    var cm: ConnectivityManager? = null
    var apiInterface: APIInterface? = null

    var showProgressbar = MutableLiveData<Boolean>()

    var requestStatus = MutableLiveData<DietRequest>()





    lateinit var realm: Realm

    fun setViewListener(con : Activity) {
        mContext = con
        apiInterface = ApiClient.client()
        cm = con.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }


    fun onFabClick(){
        val intenttop = Intent(mContext, ActivityDietRequest::class.java)
        val bndlanimation = ActivityOptions.makeCustomAnimation(mContext, R.anim.fade_in, R.anim.fade_out).toBundle()
        mContext!!.startActivity(intenttop, bndlanimation)
        mContext!!.finish()

    }


    fun getDietPlan(){

        val activeNetwork: NetworkInfo? = cm!!.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if(!isConnected){
            dietPlanGetingErorr.value = "No internet connection !"
        }else{

            realm = Realm.getDefaultInstance()
            val userTB = realm.where(UserTB::class.java!!).findFirst()

            var dietRequestResult: DietRequest? = null

            apiInterface?.getCurrentPlanForUser(userTB?.userID!!)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<JsonObject> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(request: JsonObject) {

                        var registration: JSONObject? = null
                        registration = JSONObject(request.toString())


                        if(registration.getBoolean("dietPlanRequestStatus")){

                            requestNumber.set(registration.getInt("dietPlanRequestID").toString())
                            gender.set(registration.getString("gender"))
                            weight.set(registration.getDouble("weight").toString()+" Kg")
                            height.set(registration.getDouble("height").toString()+" Inch")
                            waist.set(registration.getDouble("waist").toString()+" Inch")

                            type.set(registration.getString("dietTypeName"))
                            date.set(registration.getString("dateStamp").toString().substring(0,10))


                            if(registration.getBoolean("isDietPlanSubmitted")){
                                status.set("Complete")

                                val businessObject = registration.getJSONArray("dietPlanList")
                                val item = businessObject.getJSONObject(0)

                                dietPlan.value=item.getString("dietPlanUrl")

                            }else{
                                status.set("Pending")
                            }




                        }else{

                            dietPlanGetingErorr.value ="No Plan"
                            showProgressbar.value = false
                        }



                    }

                    override fun onError(e: Throwable) {
                        dietPlanGetingErorr.value ="Something went wrong, Please try again"
                        showProgressbar.value = false
                    }

                    override fun onComplete() {

                    }
                })



        }

    }

}





