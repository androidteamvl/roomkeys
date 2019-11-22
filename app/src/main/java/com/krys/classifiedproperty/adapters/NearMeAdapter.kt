package com.krys.classifiedproperty.adapters

import android.graphics.Color
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
import com.krys.classifiedproperty.model.NearMeModel
import com.krys.classifiedproperty.model.WishlistModel
import java.util.*
import kotlin.collections.ArrayList


class NearMeAdapter(var catarray:ArrayList<NearMeModel>? = null) : RecyclerView.Adapter<NearMeAdapter.ViewHolder>() {

    var rowindex:Int = -1
    private var onItemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.wishrview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wishlistModel:NearMeModel = catarray!![position]
        Glide.with(holder.imageView.context)
            .load(wishlistModel.images)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)
        holder.intypecategory.text = "In "+wishlistModel.category_name
        holder.location.text = wishlistModel.distance+"km"
        holder.price.text =  "Rs. "+wishlistModel.amount+" /m"
        holder.title.text = wishlistModel.add_title
        if(wishlistModel.wishlist_status.toString().equals("1")){
            holder.fav.visibility = View.VISIBLE
            holder.notfav.visibility = View.GONE
        } else {
            holder.fav.visibility = View.GONE
            holder.notfav.visibility = View.VISIBLE
        }
        holder.fav.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.fav, position, wishlistModel.post_id,"fav")
            holder.fav.visibility = View.GONE
            holder.notfav.visibility = View.VISIBLE

        })
        holder.notfav.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.notfav, position,wishlistModel.post_id,"notfav")
            holder.fav.visibility = View.VISIBLE
            holder.notfav.visibility = View.GONE

        })
       holder.click.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(holder.click, position,wishlistModel.post_id,"check")
          // rowindex=position;
          // notifyDataSetChanged();
       })

    }

    fun randomcolor():Int{
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
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
            postId: String,
            check:String
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val click: RelativeLayout = itemView.findViewById(R.id.click)

        val imageView:ImageView = itemView.findViewById(R.id.image)
        val title:TextView = itemView.findViewById(R.id.text112)
        val price:TextView = itemView.findViewById(R.id.text212)
        val location:TextView = itemView.findViewById(R.id.text312)
        val intypecategory:TextView = itemView.findViewById(R.id.ffd)
        val fav:ImageView = itemView.findViewById(R.id.fav)
        val notfav:ImageView = itemView.findViewById(R.id.notfav)
    }
}