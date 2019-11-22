package com.krys.classifiedproperty.ui.nearme

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.activity.PropertyDetails
import com.krys.classifiedproperty.adapters.NearMeAdapter
import com.krys.classifiedproperty.adapters.WishListAdapter
import com.krys.classifiedproperty.model.NearMeModel
import com.krys.classifiedproperty.model.WishlistModel
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NearmeFragment : Fragment() {

    val nearmearray: ArrayList<NearMeModel> = ArrayList()
    lateinit var nothing: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_nearme, container, false)
        val wishlistRview: RecyclerView = viewRoot.findViewById(R.id.wishlist)
        val llProgressBar: View = viewRoot.findViewById(R.id.llProgressBar)
        nothing = viewRoot.findViewById(R.id.nothing)
        val preference: SharedPreferences = activity!!.getSharedPreferences("MyPrf", Context.MODE_PRIVATE)
        val userid = preference.getString("userid", null)!!
        val lat = preference.getString("lat", null)
        val lng = preference.getString("lng", null)
        val nearMeAdapter = NearMeAdapter(nearmearray)
        wishlistRview.layoutManager = LinearLayoutManager(activity)
        wishlistRview.adapter = nearMeAdapter
        callNearME(nearMeAdapter, userid, llProgressBar,lat,lng)
        return viewRoot
    }

    fun callNearME(wishAdapter: NearMeAdapter, userid: String, llProgressBar: View, lat: String?, lng: String?) {
        nearmearray.clear()
        llProgressBar.visibility = View.VISIBLE
        if (AppUtils.isNetworkAvalilable(activity!!)) {
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("user_id", userid)
            internalObject.addProperty("lat", lat)
            internalObject.addProperty("long", lng)
            Log.e("check",internalObject.toString())
            val call = AppUtils.service.getnearme(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        if (jsob.getString("status").equals("true")) {
                            val jsonArray = jsob.getJSONArray("post_list")
                            for (i in 0 until jsonArray.length()) {
                                val jobj = jsonArray.getJSONObject(i)
                                Log.e("checkdata", jobj.toString())
                                nearmearray.add(
                                    NearMeModel(
                                        jobj.getString("post_id"),
                                        jobj.getString("category_name"),
                                        jobj.getString("add_title"),
                                        jobj.getString("amount"),
                                        jobj.getString("product_cover_db"),
                                        jobj.getString("city"),
                                        jobj.getString("wishlist_status"),
                                        jobj.getString("distance")))
                            }
                            wishAdapter.setItemClickListener(object :
                                NearMeAdapter.ItemClickListener {
                                override fun onItemClick(view: View, position: Int, postId: String,check:String) {
                                    if(check.equals("check")){
                                        val intent = Intent(activity, PropertyDetails::class.java)
                                        intent.putExtra("postid",postId)
                                        startActivity(intent)
                                    }else{
                                        callAddWish(postId, userid)
                                    }


//                                    nearmearray.removeAt(position)
//                                    wishAdapter.notifyItemRemoved(position)
//                                    wishAdapter.notifyItemRangeChanged(position, nearmearray.size)

                                }
                            })
                            wishAdapter.notifyDataSetChanged()
                            llProgressBar.visibility = View.GONE

                        } else {
                            if(nearmearray.size==0){
                                nothing.visibility = View.VISIBLE
                            } else {
                                nothing.visibility = View.GONE
                            }
                            Toast.makeText(activity, jsob.getString("msg"), Toast.LENGTH_SHORT).show()
                            llProgressBar.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun callAddWish(postId: String, userid: String) {

        if (AppUtils.isNetworkAvalilable(activity!!)) {
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("user_id", userid)
            internalObject.addProperty("post_id", postId)
            val call = AppUtils.service.addwishlist(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        if (jsob.getString("status").equals("true")) {

                            //     llProgressBar.visibility = View.GONE
                            Toast.makeText(activity, jsob.getString("msg"), Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(activity, jsob.getString("msg"), Toast.LENGTH_SHORT)
                                .show()
                            //  llProgressBar.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}