package emarge.project.caloriecaffe.ui.activity.dietRequest

import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import emarge.project.caloriecaffe.R
import emarge.project.caloriecaffe.databinding.ActivityDietRequestBinding
import emarge.project.caloriecaffe.model.DietRequest
import emarge.project.caloriecaffe.model.DietType
import emarge.project.caloriecaffe.ui.activity.dietPlan.ActivityDietPlan
import emarge.project.caloriecaffe.ui.adaptor.DietTypeAdapter
import emarge.project.caloriecaffe.viewModel.DietRequestViewModel
import kotlinx.android.synthetic.main.activity_diet_request.*

import java.util.ArrayList

class ActivityDietRequest : AppCompatActivity(), DietTypeAdapter.ClickListener {


    override fun onSongClick(v: View, position: Int) {
        dialogFilter.dismiss()
        textView6.text= dietTypeList[position].name
        textview_typeid.text= dietTypeList[position].id.toString()
    }


    private lateinit var binding: ActivityDietRequestBinding

    lateinit var diteTypeAdapter: DietTypeAdapter
    var dietTypeList = ArrayList<DietType>()
    lateinit var dialogFilter: Dialog


    var isButtonSelectTypeClick : Boolean = false

    lateinit var recyclerviewDiteplane : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_request)

        binding = DataBindingUtil.setContentView<ActivityDietRequestBinding>(this, R.layout.activity_diet_request)
        binding.dietrequest = ViewModelProviders.of(this).get(DietRequestViewModel::class.java)
        binding.dietrequest!!.setViewListener(applicationContext)


        binding.dietrequest!!.getDietType()



        binding.dietrequest!!.getingDietTypesErorr.observe(this, Observer<String> {
            it?.let { result ->
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        })





        binding.dietrequest!!.dietTypeList.observe(this, Observer<ArrayList<DietType>> {
            it?.let { result ->
                dietTypeList = result
                button_select_type.isEnabled= true
                if(isButtonSelectTypeClick)
                    ditePlneDialog()

            }
        })


        binding.dietrequest!!.requestValidationErorr.observe(this, Observer<String> {
            it?.let { result ->
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        })





        binding.dietrequest!!.showProgressbar.observe(this, Observer<Boolean> {
            it?.let { result ->
                if(result){
                    progressBar.visibility= View.VISIBLE
                    button_request.isEnabled= false
                }else{
                    progressBar.visibility= View.GONE
                    button_request.isEnabled= true

                }
            }
        })




        binding.dietrequest!!.dietRequestError.observe(this, Observer<String> {
            it?.let { result ->
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            }
        })


        binding.dietrequest!!.requestStatus.observe(this, Observer<DietRequest> {
            it?.let { result ->
                if(result.dietPlanRequestStatus){
                    Toast.makeText(this, "Diet plan successfully requested", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@ActivityDietRequest, ActivityDietPlan::class.java)
                    val bndlanimation = ActivityOptions.makeCustomAnimation(this@ActivityDietRequest, R.anim.fade_in, R.anim.fade_out).toBundle()
                    startActivity(intent, bndlanimation)
                    finish()
                }else{
                    Toast.makeText(this, "Incorrect payment code", Toast.LENGTH_LONG).show()
                }

            }
        })


        button_select_type.setOnClickListener {
            if(dietTypeList.isEmpty()){
                isButtonSelectTypeClick = true
                binding.dietrequest!!.getDietType()
            }else{
                ditePlneDialog()
            }


        }


    }


    fun ditePlneDialog(){

        dialogFilter = Dialog(this)
        dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFilter.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialogFilter.setContentView(R.layout.dialog_diet_types)
        dialogFilter.setCancelable(true)


        recyclerviewDiteplane = dialogFilter.findViewById<RecyclerView>(R.id.recyclerview_diteplane)


        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerviewDiteplane.layoutManager = layoutManager
        recyclerviewDiteplane.itemAnimator = DefaultItemAnimator()
        recyclerviewDiteplane.isNestedScrollingEnabled = false


        diteTypeAdapter = DietTypeAdapter(dietTypeList)

        recyclerviewDiteplane.adapter = diteTypeAdapter


        diteTypeAdapter.setClickListener(this)

        dialogFilter.show()

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
