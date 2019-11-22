package com.krys.classifiedproperty.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krys.classifiedproperty.R
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class FeatureAdapter(val imagearray:ArrayList<Int>,val namearray: ArrayList<String>) : RecyclerView.Adapter<FeatureAdapter.ViewHolder>() {

    var rowindex:Int = -1
    private var onItemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.featurelistrview, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = imagearray[position]
        val data1 = namearray[position]
        holder.textView.text = data1
    }

    fun randomcolor():Int{
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    override fun getItemCount(): Int {
        return imagearray.size
    }

    fun setItemClickListener(clickListener: ItemClickListener) {
        onItemClickListener = clickListener
    }


    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView:ImageView = itemView.findViewById(R.id.imagea)
        val textView:TextView = itemView.findViewById(R.id.texta)

//        val maincontainer: RelativeLayout = itemView.findViewById(R.id.maincontainer)
//        val containerinside: RelativeLayout = itemView.findViewById(R.id.containerinside)


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