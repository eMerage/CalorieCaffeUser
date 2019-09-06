package emarge.project.caloriecaffe.ui.activity.singup

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import emarge.project.caloriecaffe.R
import emarge.project.caloriecaffe.databinding.ActivitySingupBinding
import emarge.project.caloriecaffe.model.LoginUser
import emarge.project.caloriecaffe.ui.activity.dietRequest.ActivityDietRequest
import emarge.project.caloriecaffe.ui.activity.login.ActivityLogin
import emarge.project.caloriecaffe.viewModel.SingUpViewModel
import kotlinx.android.synthetic.main.activity_singup.*
import kotlinx.android.synthetic.main.bottom_sheet_email_verified.*

class ActivitySingup : AppCompatActivity() {

    private lateinit var binding: ActivitySingupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)

        binding = DataBindingUtil.setContentView<ActivitySingupBinding>(this, R.layout.activity_singup)
        binding.singup = ViewModelProviders.of(this).get(SingUpViewModel::class.java)
        binding.singup!!.setViewListener(applicationContext)

        button3.setOnClickListener {
            binding.singup!!.onVerifiyClick(editText_verfycode.text.toString())
        }



        binding.singup!!.singUpValidationError.observe(this, Observer<String> {
            it?.let { result ->
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        })


        binding.singup!!.showProgressbar.observe(this, Observer<Boolean> {
            it?.let { result ->
                if(result){
                    progressBar.visibility= View.VISIBLE
                    button_singup.isEnabled= false
                    button3.isEnabled= false
                }else{
                    progressBar.visibility= View.GONE
                    button_singup.isEnabled= true
                    button3.isEnabled= true
                }
            }
        })




        binding.singup!!.singUpUserStatus.observe(this, Observer<LoginUser> {
            it?.let { result ->
                if(result.isSingUpSuccess!!){
                    val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_reset)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    textView3.text = "We have sent verification code to your email ${result.email}"
                }else{
                    Toast.makeText(this, result.userSingUpErrorMsg, Toast.LENGTH_LONG).show()
                }
            }
        })


        binding.singup!!.singUpVerificationError.observe(this, Observer<String> {
            it?.let { result ->
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        })


        binding.singup!!.singUpVerificationStatus.observe(this, Observer<LoginUser> {
            it?.let { result ->
                if(result!!.isSingUpVerificationSuccess!!){
                    val intent = Intent(this@ActivitySingup, ActivityDietRequest::class.java)
                    val bndlanimation = ActivityOptions.makeCustomAnimation(this@ActivitySingup, R.anim.fade_in, R.anim.fade_out).toBundle()
                    startActivity(intent, bndlanimation)
                    finish()
                }else{
                    Toast.makeText(this, result.userSingUpVerificationError, Toast.LENGTH_LONG).show()
                }
            }
        })


    }

    override fun onBackPressed() {
        val intent = Intent(this@ActivitySingup, ActivityLogin::class.java)
        val bndlanimation = ActivityOptions.makeCustomAnimation(this@ActivitySingup, R.anim.fade_in, R.anim.fade_out).toBundle()
        startActivity(intent, bndlanimation)
        finish()
    }


}


