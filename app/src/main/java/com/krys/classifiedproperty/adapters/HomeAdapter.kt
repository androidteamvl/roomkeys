//package com.krys.classifiedproperty.adapters
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.RelativeLayout
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.krys.classifiedproperty.R
//
//class HomeAdapter(val userList: ArrayList<HomeCatModel>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.rviewlayout, parent, false)
//        return ViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
//        holder.bindItems(userList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return userList.size
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
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
//    }
//}