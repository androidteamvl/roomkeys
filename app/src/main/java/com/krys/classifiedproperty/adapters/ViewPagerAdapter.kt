package com.krys.classifiedproperty.adapters

import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.model.CatModel
import com.krys.classifiedproperty.model.Postdetail
import com.krys.classifiedproperty.utils.CircleIndicator
import java.util.*
import kotlin.collections.ArrayList


class ViewPagerAdapter(val namearray: ArrayList<Postdetail>) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    var rowindex:Int = -1
    private var onItemClickListener: ItemClickListener? = null
    private var currentPage = 0
    private val images = arrayOf<Int>(
        R.drawable.appbg,
        R.drawable.cardlogo,
        R.drawable.cardlogo2,
        R.drawable.cardlogo3,
        R.drawable.bgbehind
    )
    private val imageaarray = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = "Rs. "+namearray.get(position).amount
        holder.title.text = ""+namearray.get(position).add_title
        holder.address.text = ""+namearray.get(position).corresspondance_add
        holder.destitle.text = "Want to know more about "+namearray.get(position).add_title+" ?"
        holder.description.text = ""+namearray.get(position).description
        Glide.with(holder.Image.context)
            .load(namearray.get(position).product_cover_db)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.Image)

        holder.back.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.back, position)
        })
       // init(holder)

    }

    fun randomcolor():Int{
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    override fun getItemCount(): Int {
        return namearray.size
    }

    fun setItemClickListener(clickListener: ItemClickListener) {
        onItemClickListener = clickListener
    }


    interface ItemClickListener {
        fun onItemClick(
            view: View,
            position: Int
        )
    }

/*    private fun init(holder: ViewHolder) {
        for (i in 0 until images.size)
            imageaarray.add(images[i])
        holder.mPager.adapter = SliderAdapter(holder.mPager.context, imageaarray)
        holder.indicator.setViewPager(holder.mPager)
        val handler = Handler()
        val Update = Runnable {
            if (currentPage.equals(images.size)) {
                currentPage = 0
            }
            holder.mPager.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 2500, 2500)
    }*/

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView:TextView = itemView.findViewById(R.id.text4)
      //  var mPager: ViewPager = itemView.findViewById(R.id.pager)
       // val indicator = itemView.findViewById<View>(R.id.indicator) as CircleIndicator
        val back:ImageView = itemView.findViewById(R.id.back)
        val title:TextView = itemView.findViewById(R.id.text1)
        val address:TextView = itemView.findViewById(R.id.text3)
        val Image:ImageView = itemView.findViewById(R.id.pager)
        val destitle:TextView = itemView.findViewById(R.id.text12)
        val description:TextView = itemView.findViewById(R.id.text3a)
        //val title:TextView = itemView.findViewById(R.id.text1)
        //val title:TextView = itemView.findViewById(R.id.text1)
    }
}