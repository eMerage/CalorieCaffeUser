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

class SingUpViewModel : ViewModel() {

    val editTextMobile = MutableLiveData<String>()
    val editTextEmail = MutableLiveData<String>()
    val editTextNIC = MutableLiveData<String>()
    val editTextName = MutableLiveData<String>()

    var showProgressbar = MutableLiveData<Boolean>()


    var apiInterface: APIInterface? = null
    var cm: ConnectivityManager? = null


    var singUpUserStatus = MutableLiveData<LoginUser>()
    var singUpValidationError = MutableLiveData<String>()

    var singUpVerificationStatus = MutableLiveData<LoginUser>()
    var singUpVerificationError = MutableLiveData<String>()


     lateinit var realm: Realm
     lateinit var user : LoginUser

    fun setViewListener(con: Context) {
        apiInterface = ApiClient.client()
        cm = con.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    fun onSingUpClick() {

        val activeNetwork: NetworkInfo? = cm!!.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true


        when {
            editTextName.value.isNullOrEmpty() -> singUpValidationError.value = "Please fill the Name"
            editTextNIC.value.isNullOrEmpty() -> singUpValidationError.value = "Please fill the NIC Number"
            nicNumberValidation(editTextNIC.value.toString()) -> singUpValidationError.value = "Please enter valid NIC"
            editTextEmail.value.isNullOrEmpty() -> singUpValidationError.value = "Please fill the Email"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail.value.toString()).matches() -> singUpValidationError.value =
                "Please enter valid Email"
            !isConnected -> singUpValidationError.value = "No internet connection !"
            else -> {

                showProgressbar.value = true
                FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {

                            return@OnCompleteListener
                        }

                        singUpValidationWithServer(task.result?.token!!)

                    })

            }
        }


    }

    fun nicNumberValidation(nic: String): Boolean {
        var result: Boolean = false
        result = if (nic.length != 10 && nic.length != 12) {
            true
        } else {
            if (nic.length == 10) {
                if (nic.substring(0, 8).matches(("[0-9]+").toRegex())) {
                    !(nic[9].toString().equals("V", true) || nic[9].toString().equals("X", true))
                } else {
                    true
                }
            } else {
                !nic.matches(("[0-9]+").toRegex())
            }
        }
        return result
    }


    fun singUpValidationWithServer(pushToken: String) {


        var logUserDetails: LoginUser? = null
        val jsonObject = JsonObject()
        user = LoginUser()

        jsonObject.addProperty("FullName", editTextName.value.toString())
        jsonObject.addProperty("MobileNo", editTextMobile.value.toString())
        jsonObject.addProperty("Email", editTextEmail.value.toString())
        jsonObject.addProperty("NIC", editTextNIC.value)
        jsonObject.addProperty("PushTokenID", pushToken)



        apiInterface!!.saveUser(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LoginUser> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(userResult: LoginUser) {
                    logUserDetails = userResult

                    if (userResult!!.userStatus==false) {
                        user.isSingUpSuccess = false
                        user.userSingUpErrorMsg = "NIC already exists"
                        singUpUserStatus.value=user
                    } else {
                        user.email = logUserDetails!!.email
                        user.userID = logUserDetails!!.userID
                        user.nic = logUserDetails!!.nic

                        user.isSingUpSuccess = true
                        singUpUserStatus.value=user

                    }


                }

                override fun onError(e: Throwable) {
                    user.isSingUpSuccess = false
                    user.userSingUpErrorMsg = "Something went wrong, Please try again"
                    singUpUserStatus.value=user
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
            singUpVerificationError.value = "No internet connection !"
        } else if (code.isNullOrEmpty()) {
            singUpVerificationError.value = "Please enter the code!"
        } else {
            singUpVerificationWithServer(code)
        }

    }

    fun singUpVerificationWithServer(code: String) {

        showProgressbar.value = true

        var logUserVerifiyDetails: LoginUser? = null

        apiInterface!!.validateSignupCode(user.nic!!,code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LoginUser> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(user: LoginUser) {
                    logUserVerifiyDetails = user
                }

                override fun onError(e: Throwable) {
                    user.isSingUpVerificationSuccess = false
                    user.userSingUpErrorMsg = "Something went wrong, Please try again"
                    singUpVerificationStatus.value=user
                    showProgressbar.value = false
                }

                override fun onComplete() {
                    if (logUserVerifiyDetails!!.userStatus==false) {
                        user.isSingUpVerificationSuccess = false
                        user.userSingUpVerificationError = "Incorrect Sing Up code"
                        singUpVerificationStatus.value=user
                    } else {
                        user.isSingUpVerificationSuccess = true
                        singUpVerificationStatus.value=user
                        saveUser()
                    }
                    showProgressbar.value = false

                }
            })


    }

    private fun saveUser() {
        realm = Realm.getDefaultInstance()
        realm.executeTransaction(Realm.Transaction { bgRealm ->
            val userTB = bgRealm.createObject(UserTB::class.java,  1)
            userTB.userID=user.userID
            userTB.email = user.email
            userTB.nic= user.nic
        })
    }

}