package com.krys.classifiedproperty.adapters

import android.app.LauncherActivity.ListItem
import androidx.annotation.NonNull
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.Arrays.asList
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.utils.CircleIndicator
import java.util.*
import kotlin.collections.ArrayList


class CardAdapter(val namearray: ArrayList<String>) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

//    private var currentPage = 0
//    private val images = arrayOf<Int>(
//        R.drawable.appbg,
//        R.drawable.cardlogo,
//        R.drawable.cardlogo2,
//        R.drawable.cardlogo3,
//        R.drawable.bgbehind
//    )

    private val imageaarray = ArrayList<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardrviewlay, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.e("checkas",namearray.get(position))
        holder.textView.text = "Rs. "+namearray.get(position)+"Lacs"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

      //  val mPager:ViewPager = itemView.findViewById(R.id.pager)
        val textView:TextView = itemView.findViewById(R.id.text4)

    }

//    private fun init(holder: ViewHolder) {
//        for (i in 0 until images.size)
//            imageaarray.add(images[i])
//        holder.mPager.adapter = SliderAdapter(holder.mPager.context, imageaarray)
//        val indicator = holder.itemView.findViewById<View>(R.id.indicator) as CircleIndicator
//        indicator.setViewPager(holder.mPager)
//        val handler = Handler()
//        val Update = Runnable {
//            if (currentPage.equals(images.size)) {
//                currentPage = 0
//            }
//            holder.mPager.setCurrentItem(currentPage++, true)
//            notifyDataSetChanged()
//        }
//        val swipeTimer = Timer()
//        swipeTimer.schedule(object : TimerTask() {
//            override fun run() {
//                handler.post(Update)
//            }
//        }, 2500, 2500)
//    }

    override fun getItemCount(): Int {
        return namearray.size
    }
}