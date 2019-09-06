package emarge.project.caloriecaffe.ui.adaptor


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import emarge.project.caloriecaffe.R
import emarge.project.caloriecaffe.model.DietType


import java.util.ArrayList


/**
 * Created by Himanshu on 4/10/2015.
 */
 class DietTypeAdapter(internal var dietItems: ArrayList<DietType>) : RecyclerView.Adapter<DietTypeAdapter.MyViewHolder>() {


    private var clickListener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listview_diettype, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.type.text = dietItems[position].name

    }

    override fun getItemCount(): Int {
        return dietItems.size
    }

    interface ClickListener {
        fun onSongClick(v: View, position: Int)
    }

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        var cardView: RelativeLayout = itemView.findViewById<View>(R.id.card_view) as RelativeLayout
        var type: TextView = itemView.findViewById<View>(R.id.textview_spec) as TextView

        init {
            cardView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (clickListener != null) {
                clickListener!!.onSongClick(view, adapterPosition)
            }
        }
    }


}
