package com.krys.classifiedproperty.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.model.HostelModel
import java.util.ArrayList


class HostelAdapter(var catarray: ArrayList<HostelModel>? =null) : RecyclerView.Adapter<HostelAdapter.ViewHolder>() {

    var rowindex:Int = -1
    private var onItemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.homecatrview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val catModel:HostelModel = catarray!![position]
        Glide.with(holder.imgpg.context)
            .load(catModel.images)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imgpg)
        holder.textcatname.text = catModel.add_title
        holder.text21267.text= "Rs. "+catModel.amount+" /m"
        holder.text31267.text  = catModel.contact_name
        if(catModel.wishlist_status.toString().equals("1")){
            holder.fav.visibility = View.GONE
            holder.notfav.visibility = View.VISIBLE
        } else {
            holder.fav.visibility = View.VISIBLE
            holder.notfav.visibility = View.GONE
        }
        holder.fav.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.fav, position, catModel.post_id,"fav",catModel.category_id)
            holder.fav.visibility = View.GONE
            holder.notfav.visibility = View.VISIBLE

        })
        holder.notfav.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.notfav, position,catModel.post_id,"notfav",catModel.category_id)
            holder.fav.visibility = View.VISIBLE
            holder.notfav.visibility = View.GONE

        })
        holder.mainlay.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.mainlay, position,catModel.category_id,"main",catModel.category_id)
        })

    }

    override fun getItemCount(): Int {
        return catarray!!.size
    }

    fun setItemClickListener(clickListener: ItemClickListener) {
        onItemClickListener = clickListener
    }


    interface ItemClickListener {
        fun onItemClick(
            view: View,
            position: Int,
            post_id: String,
            type: String,
            categoryId: String
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgpg: ImageView = itemView.findViewById(R.id.imgpg)
        val textcatname:TextView = itemView.findViewById(R.id.text11267)
        val text21267: TextView = itemView.findViewById(R.id.text21267)
        val text31267: TextView = itemView.findViewById(R.id.text31267)
        val price: TextView = itemView.findViewById(R.id.price)
        val fav:ImageView = itemView.findViewById(R.id.fav)
        val notfav:ImageView = itemView.findViewById(R.id.notfav)
        val mainlay: RelativeLayout = itemView.findViewById(R.id.mainlay)

    }
}