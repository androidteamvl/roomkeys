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
import com.krys.classifiedproperty.R
import java.util.*
import kotlin.collections.ArrayList


class FilterDataAdapter() : RecyclerView.Adapter<FilterDataAdapter.ViewHolder>() {

    var rowindex:Int = -1
    private var onItemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.filterrview, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }

    fun randomcolor():Int{
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    override fun getItemCount(): Int {
        return 10
    }

    fun setItemClickListener(clickListener: ItemClickListener) {
        onItemClickListener = clickListener
    }


    interface ItemClickListener {
        fun onItemClick(view: View, position: Int, title: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image:ImageView = itemView.findViewById(R.id.image)
        val text112:TextView = itemView.findViewById(R.id.text112)
        val text212:TextView = itemView.findViewById(R.id.text212)
        val text312:TextView = itemView.findViewById(R.id.text312)
        val ffd:TextView = itemView.findViewById(R.id.ffd)

    }
}