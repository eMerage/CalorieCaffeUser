package emarge.project.caloriecaffe.viewModel


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
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




class SplashViewModel : ViewModel() {

    lateinit var realm: Realm
    var isUserLogin = MutableLiveData<Boolean>()

    fun setViewListener() {
        realm = Realm.getDefaultInstance()
    }


    fun checkUser() {
        realm = Realm.getDefaultInstance()
        val user = realm.where(UserTB::class.java).count()
        isUserLogin.value = user.toInt() != 0

    }


}