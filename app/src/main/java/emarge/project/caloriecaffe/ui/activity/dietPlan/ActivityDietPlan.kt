package emarge.project.caloriecaffe.ui.activity.dietPlan

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import emarge.project.caloriecaffe.R
import emarge.project.caloriecaffe.databinding.ActivityDietPlanBinding
import emarge.project.caloriecaffe.model.DietPlan
import emarge.project.caloriecaffe.model.DietRequest
import emarge.project.caloriecaffe.viewModel.DietPlanViewModel
import kotlinx.android.synthetic.main.activity_diet_plan.*
import android.webkit.WebView
import android.webkit.WebViewClient





class ActivityDietPlan : AppCompatActivity() {


    private lateinit var binding:ActivityDietPlanBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_plan)

        binding = DataBindingUtil.setContentView<ActivityDietPlanBinding>(this, R.layout.activity_diet_plan)
        binding.dietplan = ViewModelProviders.of(this).get(DietPlanViewModel::class.java)
        binding.dietplan!!.setViewListener(this)

        binding.dietplan!!.getDietPlan()





        binding.dietplan!!.showProgressbar.observe(this, Observer<Boolean> {
            it?.let { result ->
                if(result){
                    progressBar.visibility= View.VISIBLE
                }else{
                    progressBar.visibility= View.GONE


                }
            }
        })


        binding.dietplan!!.dietPlanGetingErorr.observe(this, Observer<String> {
            it?.let { result ->
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setMessage(result)
                alertDialogBuilder.setPositiveButton("OK"
                ) { _, _ ->
                    return@setPositiveButton
                }
                alertDialogBuilder.show()
            }
        })




        binding.dietplan!!.dietPlan.observe(this, Observer<String> {
            it?.let { result ->

                webView.settings.javaScriptEnabled = true
                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        progressBar.visibility= View.GONE
                    }
                }
                webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$result.pdf")
                webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$result.pdf")
                webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$result.pdf")

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
