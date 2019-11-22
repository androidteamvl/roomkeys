package com.krys.classifiedproperty.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.model.SubCatModel
import java.util.*
import kotlin.collections.ArrayList


class SubCategoriesAdapter(val subcategory:ArrayList<SubCatModel>?=null) : RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>() {

    var rowindex:Int = -1
    private var onItemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.subcategory, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subCatModel:SubCatModel = subcategory!![position]
        Glide.with(holder.imageView.context)
            .load(subCatModel.subcategory_icon)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)
        holder.textView.text = subCatModel.subcategory_name
        holder.maincontainer.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.maincontainer, position,holder.textView.text.toString(),subCatModel.subcategory_id)
            rowindex=position;
            notifyDataSetChanged();
        })
        if(rowindex==position){
            holder.containerinside.setBackgroundResource(R.drawable.shineborderempty)
            holder.imageView.setColorFilter(ContextCompat.getColor(holder.imageView.context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            holder.textView.setTextColor(ContextCompat.getColor(holder.imageView.context,R.color.red))
        } else {
            holder.containerinside.setBackgroundResource(R.drawable.shineborder)
            holder.imageView.setColorFilter(ContextCompat.getColor(holder.imageView.context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
            holder.textView.setTextColor(ContextCompat.getColor(holder.imageView.context,R.color.black))
        }

    }

    fun randomcolor():Int{
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    override fun getItemCount(): Int {
        return subcategory!!.size
    }

    fun setItemClickListener(clickListener: ItemClickListener) {
        onItemClickListener = clickListener
    }


    interface ItemClickListener {
        fun onItemClick(
            view: View,
            position: Int,
            title: String,
            subcategoryId: String
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView:ImageView = itemView.findViewById(R.id.image)
        val textView:TextView = itemView.findViewById(R.id.text)
        val maincontainer: RelativeLayout = itemView.findViewById(R.id.maincontainer)
        val containerinside: RelativeLayout = itemView.findViewById(R.id.containerinside)


//        fun bindItems(modelClass: HomeCatModel) {
//            val rlOverlay = itemView.findViewById(R.id.rlOverlay) as RelativeLayout
//            val title  = itemView.findViewById(R.id.title) as TextView
//            val result  = itemView.findViewById(R.id.result) as TextView
//            val pimage = itemView.findViewById(R.id.pimage) as ImageView
//            title.text = modelClass.catname;
//            result.text = "[ "+modelClass.catcount+" ]"
////            Glide.with(itemView.context).load(modelClass.cbanner)
////                .diskCacheStrategy(DiskCacheStrategy.ALL)
////                .into(pimage)
//
//
//        }
    }
}