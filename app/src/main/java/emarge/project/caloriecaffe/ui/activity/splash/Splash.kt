package emarge.project.caloriecaffe.ui.activity.splash

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import emarge.project.caloriecaffe.R
import emarge.project.caloriecaffe.databinding.ActivitySplashBinding
import emarge.project.caloriecaffe.ui.activity.dietPlan.ActivityDietPlan
import emarge.project.caloriecaffe.ui.activity.login.ActivityLogin
import emarge.project.caloriecaffe.viewModel.SplashViewModel

class Splash : AppCompatActivity() {


    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        binding.splash = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        binding.splash!!.setViewListener()

        binding.splash!!.checkUser()


        binding.splash!!.isUserLogin.observe(this, Observer<Boolean> {
            it?.let { result ->
                if(result){
                    Handler().postDelayed(Runnable {
                        val intent = Intent(this@Splash, ActivityDietPlan::class.java)
                        val bndlanimation = ActivityOptions.makeCustomAnimation(this@Splash, R.anim.fade_in, R.anim.fade_out).toBundle()
                        startActivity(intent, bndlanimation)
                        finish()
                    }, 2000)
                }else{
                    Handler().postDelayed(Runnable {
                        val intent = Intent(this@Splash, ActivityLogin::class.java)
                        val bndlanimation = ActivityOptions.makeCustomAnimation(this@Splash, R.anim.fade_in, R.anim.fade_out).toBundle()
                        startActivity(intent, bndlanimation)
                        finish()
                    }, 2000)
                }
            }
        })
    }
}
