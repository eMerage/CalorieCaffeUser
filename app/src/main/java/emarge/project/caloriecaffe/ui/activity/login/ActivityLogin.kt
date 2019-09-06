package emarge.project.caloriecaffe.ui.activity.login


import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import emarge.project.caloriecaffe.R
import emarge.project.caloriecaffe.databinding.ActivityLoginBinding

import emarge.project.caloriecaffe.viewModel.UserLoginViewModel
import kotlinx.android.synthetic.main.bottom_sheet_email_verified.*
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import emarge.project.caloriecaffe.model.LoginUser
import emarge.project.caloriecaffe.ui.activity.dietPlan.ActivityDietPlan
import emarge.project.caloriecaffe.ui.activity.dietRequest.ActivityDietRequest
import emarge.project.caloriecaffe.ui.activity.singup.ActivitySingup
import kotlinx.android.synthetic.main.activity_login.*


class ActivityLogin : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)




        binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.user = ViewModelProviders.of(this).get(UserLoginViewModel::class.java)
        binding.user!!.setViewListener(applicationContext)




        button3.setOnClickListener {
            binding.user!!.onVerifiyClick(editText_verfycode.text.toString())
        }


        button.setOnClickListener {
            val intent = Intent(this@ActivityLogin, ActivitySingup::class.java)
            val bndlanimation = ActivityOptions.makeCustomAnimation(this@ActivityLogin, R.anim.fade_in, R.anim.fade_out).toBundle()
            startActivity(intent, bndlanimation)
            finish()
        }




        binding.user!!.showProgressbar.observe(this, Observer<Boolean> {
            it?.let { result ->
                if(result){
                    progressBar.visibility= View.VISIBLE
                    button2.isEnabled= false
                    button3.isEnabled= false
                }else{
                    progressBar.visibility= View.GONE
                    button2.isEnabled= true
                    button3.isEnabled= true
                }
            }
        })


        binding.user!!.loginValidationErorr.observe(this, Observer<String> {
            it?.let { result ->
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        })


        binding.user!!.loginUserStatus.observe(this, Observer<LoginUser> {
            it?.let { result ->
                if(result.isLoginSuccess!!){
                    val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_reset)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

                    textView3.text = "We have sent verification code to your email ${result.email}"
                }else{
                    Toast.makeText(this, result.userLoginErrorMsg, Toast.LENGTH_LONG).show()
                }
            }
        })


        binding.user!!.loginVerificationErorr.observe(this, Observer<String> {
            it?.let { result ->
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        })


        binding.user!!.loginVerificationStatus.observe(this, Observer<LoginUser> {
            it?.let { result ->
                if(result.isLoginCodeVerificationSuccess!!){
                    binding.user!!.checkDietPlanAlreadyAvailableWithServer()
                }else{
                    Toast.makeText(this, result.userLoginCodeErrorMsg, Toast.LENGTH_LONG).show()
                }
            }
        })

        binding.user!!.isDietPlanAlreadyAvailable.observe(this, Observer<Boolean> {
            it?.let { result ->
                if(result){
                    val intent = Intent(this@ActivityLogin, ActivityDietPlan::class.java)
                    val bndlanimation = ActivityOptions.makeCustomAnimation(this@ActivityLogin, R.anim.fade_in, R.anim.fade_out).toBundle()
                    startActivity(intent, bndlanimation)
                    finish()
                }else{
                    val intent = Intent(this@ActivityLogin, ActivityDietRequest::class.java)
                    val bndlanimation = ActivityOptions.makeCustomAnimation(this@ActivityLogin, R.anim.fade_in, R.anim.fade_out).toBundle()
                    startActivity(intent, bndlanimation)
                    finish()
                }
            }
        })


    }
    


    override fun onBackPressed() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Exit!")
        alertDialogBuilder.setMessage("Do you really want to exit ?")
        alertDialogBuilder.setPositiveButton("YES"
        ) { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        alertDialogBuilder.setNegativeButton("NO", DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
        alertDialogBuilder.show()
    }


}


