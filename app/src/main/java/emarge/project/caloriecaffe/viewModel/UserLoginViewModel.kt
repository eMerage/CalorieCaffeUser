package emarge.project.caloriecaffe.viewModel


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import emarge.project.caloriecaffe.db.UserTB
import emarge.project.caloriecaffe.model.LoginUser
import emarge.project.caloriecaffe.network.api.APIInterface
import emarge.project.caloriecaffe.network.api.ApiClient
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import org.json.JSONObject


class UserLoginViewModel : ViewModel() {

    val editTextNic = MutableLiveData<String>()
    var showProgressbar = MutableLiveData<Boolean>()


    var loginUserStatus = MutableLiveData<LoginUser>()
    var loginValidationErorr = MutableLiveData<String>()

    var loginVerificationStatus = MutableLiveData<LoginUser>()
    var loginVerificationErorr = MutableLiveData<String>()


    var isDietPlanAlreadyAvailable = MutableLiveData<Boolean>()

    var apiInterface: APIInterface? = null

    lateinit var realm: Realm
    lateinit var user: LoginUser
    var cm: ConnectivityManager? = null

    fun setViewListener(con: Context) {
        apiInterface = ApiClient.client()
        cm = con.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun onLoginClick() {
        val activeNetwork: NetworkInfo? = cm!!.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        when {
            !isConnected -> loginValidationErorr.value = "No internet connection !"
            editTextNic.value.isNullOrEmpty() -> loginValidationErorr.value = "Please Enter NIC number!"
            nicNumberValidation(editTextNic.value.toString()) -> loginValidationErorr.value = "Please Enter valid NIC number!"
            else -> {
                showProgressbar.value = true
                FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    loginValidationWithServer(editTextNic.value!!, task.result?.token!!)
                })

            }
        }

    }

    fun nicNumberValidation(nic: String): Boolean {

        var result: Boolean = false

        if (nic.length != 10 && nic.length != 12) {
            result = true
        } else {
            result = if (nic.length == 10) {
                if (nic.substring(0, 8).matches(("[0-9]+").toRegex())) {
                    !(nic[9].toString().equals(
                        "V",
                        true
                    ) || nic[9].toString().equals("X", true))
                } else {
                    true
                }
            } else {
                !nic.matches(("[0-9]+").toRegex())
            }
        }
        return result
    }

    fun loginValidationWithServer(nic: String, push: String) {



        user = LoginUser()

        apiInterface!!.validateUser(nic, push)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<JsonObject> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(log: JsonObject) {
                    var registration: JSONObject? = null
                    registration = JSONObject(log.toString())
                    if (!registration.getBoolean("userStatus")) {
                        user.isLoginSuccess = false
                        val businessObject = registration.getJSONObject("error")
                        user.userLoginErrorMsg = businessObject.getString("description")
                        loginUserStatus.value = user
                    } else {
                        user.isLoginSuccess = true
                        user.email = registration.getString("email")
                        user.userID = registration.getString("userID")
                        user.nic = registration.getString("nic")
                        loginUserStatus.value = user

                    }

                }

                override fun onError(e: Throwable) {
                    user.isLoginSuccess = false
                    user.userLoginErrorMsg = "Something went wrong, Please try again"
                    loginUserStatus.value = user
                    showProgressbar.value = false
                }

                override fun onComplete() {
                    showProgressbar.value = false
                }
            })
    }


    fun onVerifiyClick(code: String) {
        val activeNetwork: NetworkInfo? = cm!!.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (!isConnected) {
            loginVerificationErorr.value = "No internet connection !"
        } else if (code.isNullOrEmpty()) {
            loginVerificationErorr.value = "Please enter the code!"
        } else {

            loginVerificationWithServer(code)
        }
    }


    fun loginVerificationWithServer(code: String) {
        showProgressbar.value = true

        var logUserDetails: LoginUser? = null

        apiInterface!!.validateLoginCode(user.nic!!, code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<JsonObject> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(log: JsonObject) {

                    var registration: JSONObject? = null
                    registration = JSONObject(log.toString())

                    if (!registration.getBoolean("userStatus")) {
                        user.isLoginCodeVerificationSuccess = false
                        val businessObject = registration.getJSONObject("error")
                        user.userLoginCodeErrorMsg =businessObject.getString("description")
                        loginVerificationStatus.value = user
                        showProgressbar.value = false

                    } else {
                        user.isLoginCodeVerificationSuccess = true

                        user.email =  registration.getString("email")
                        user.userID =  registration.getString("userID")
                        user.nic = registration.getString("nic")


                        loginVerificationStatus.value = user
                        saveUser()

                    }

                }

                override fun onError(e: Throwable) {
                    user.isLoginCodeVerificationSuccess = false
                    user.userLoginCodeErrorMsg = "Something went wrong, Please try again"
                    loginVerificationStatus.value = user
                    showProgressbar.value = false

                }

                override fun onComplete() {
                    showProgressbar.value = false
                }
            })


    }


    fun checkDietPlanAlreadyAvailableWithServer() {
        isDietPlanAlreadyAvailable.value = false
    }

    private fun saveUser() {
        realm = Realm.getDefaultInstance()

        val userdb = realm.where(UserTB::class.java).count()

        when {
            userdb.toInt() == 0 -> realm.executeTransaction(Realm.Transaction { bgRealm ->
                val userTB = bgRealm.createObject(UserTB::class.java, 1)
                userTB.userID = user.userID
                userTB.email = user.email
                userTB.nic = user.nic
            })
            else -> {

            }
        }


    }


}