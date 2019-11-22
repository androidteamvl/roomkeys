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
import com.krys.classifiedproperty.model.Postlist


class SeeAllDataAdapter(val namearray: java.util.ArrayList<Postlist>) :
    RecyclerView.Adapter<SeeAllDataAdapter.ViewHolder>() {

    private var onItemClickListener: ItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.wishrview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.intypecategory.text = "In "+namearray.get(position).category_id
        holder.location.text = namearray.get(position).corresspondance_add
        holder.price.text =  "Rs. "+namearray.get(position).amount+" /m"
        holder.title.text = namearray.get(position).add_title
        if(namearray.get(position).wishliststatus.toString().equals("1")){
            holder.fav.visibility = View.VISIBLE
            holder.notfav.visibility = View.GONE
        } else {
            holder.fav.visibility = View.GONE
            holder.notfav.visibility = View.VISIBLE
        }
        holder.fav.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.fav, position, namearray.get(position).post_id,"fav")
            holder.fav.visibility = View.GONE
            holder.notfav.visibility = View.VISIBLE

        })
        holder.notfav.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.notfav, position, namearray.get(position).post_id,"notfav")
            holder.fav.visibility = View.VISIBLE
            holder.notfav.visibility = View.GONE

        })
        holder.click.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.click, position,namearray.get(position).post_id,"check")
            // rowindex=position;
            // notifyDataSetChanged();
        })

        Glide.with(holder.imageView.context)
            .load(namearray.get(position).images)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val click: RelativeLayout = itemView.findViewById(R.id.click)
        //  val mPager:ViewPager = itemView.findViewById(R.id.pager)
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.text112)
        val price: TextView = itemView.findViewById(R.id.text212)
        val location: TextView = itemView.findViewById(R.id.text312)
        val intypecategory: TextView = itemView.findViewById(R.id.ffd)
        val fav: ImageView = itemView.findViewById(R.id.fav)
        val notfav: ImageView = itemView.findViewById(R.id.notfav)
    }

    fun setItemClickListener(clickListener: ItemClickListener) {
        onItemClickListener = clickListener
    }


    interface ItemClickListener {
        fun onItemClick(
            view: View,
            position: Int,
            postId: String,
            type: String
        )
    }

    override fun getItemCount(): Int {
        return namearray.size
    }


}