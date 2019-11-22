package com.krys.classifiedproperty.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.activity.*
import com.krys.classifiedproperty.adapters.HomeCatAdapter
import com.krys.classifiedproperty.adapters.HostelAdapter
import com.krys.classifiedproperty.adapters.PartnersAdapter
import com.krys.classifiedproperty.model.HomeCatModel
import com.krys.classifiedproperty.model.HostelModel
import com.krys.classifiedproperty.model.PartnersModel
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var city: String = ""
    var address: String = ""
    lateinit var homerviewL: RecyclerView
    lateinit var hostelrview: RecyclerView
    lateinit var partnersrview: RecyclerView
    val homedetarray: ArrayList<HomeCatModel> = arrayListOf()
    val hostelarray: ArrayList<HostelModel> = arrayListOf()
    val partnersarray: ArrayList<PartnersModel> = arrayListOf()
    var homeCatAdapter = HomeCatAdapter()
    var hostelAdapter = HostelAdapter()
    var partnersAdapter = PartnersAdapter()
    lateinit var seeallrent:TextView
    lateinit var seeallpg:TextView
    lateinit var seeallpartners:TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val preference: SharedPreferences = activity!!.getSharedPreferences("MyPrf", Context.MODE_PRIVATE)
        val lat = preference.getString("lat", null)
        val lng = preference.getString("lng", null)
        val userid = preference.getString("userid", null)!!
        val more: ImageView = root.findViewById(R.id.more)
        val filter: ImageView = root.findViewById(R.id.filter)
        seeallrent = root.findViewById(R.id.seeallrent)
        seeallpg = root.findViewById(R.id.seeallpg)
        seeallpartners = root.findViewById(R.id.seeallpartners)
        val click: RelativeLayout = root.findViewById(R.id.click)
        val mylocation: TextView = root.findViewById(R.id.mylocation)
        val llProgressBar: View = root.findViewById(R.id.llProgressBar)
        homerviewL = root.findViewById(R.id.rentrview)
        hostelrview = root.findViewById(R.id.hostelrview)
        partnersrview = root.findViewById(R.id.partnersrview)
        homerviewL.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        hostelrview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        partnersrview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        callGetHomeData(userid, llProgressBar)
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, AddPostCategory::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        })
        click.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, PropertyDetails::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        })
        filter.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, Fillter::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        })
        seeallrent.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, SeeAllData::class.java)
            intent.putExtra("categoryId","1")
            intent.putExtra("userid",userid)
            intent.putExtra("title","Rent")
            startActivity(intent)
        })
        seeallpartners.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, SeeAllData::class.java)
            intent.putExtra("categoryId","3")
            intent.putExtra("userid",userid)
            intent.putExtra("title","Partners")
            startActivity(intent)
        })
        seeallpg.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, SeeAllData::class.java)
            intent.putExtra("categoryId","2")
            intent.putExtra("userid",userid)
            intent.putExtra("title","PG/Hostels")
            startActivity(intent)
        })
        more.setOnClickListener(View.OnClickListener {
            val popup = PopupMenu(activity, more)
            popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu())

            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {


                    if (item.getItemId() === R.id.hindi) {
                        val editor: SharedPreferences.Editor = activity!!.getSharedPreferences("MyPrf", Context.MODE_PRIVATE).edit()
                        editor.clear()
                        editor.commit()
                        val intent = Intent(activity, Login::class.java)
                        startActivity(intent)
                        activity?.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        activity?.finish()
                    }
//                    if (item.getItemId() === R.id.hindi) {
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//                            val locale = Locale("hi")
//                            Locale.setDefault(locale)
//
//                            val resources = context.getResources()
//
//                            val configuration = resources.configuration
//                            configuration.locale = locale
//
//                            resources.updateConfiguration(configuration, resources.displayMetrics)
//                            activity!!.finish()
//                            activity!!.startActivity(activity!!.intent)
//                        } else {
//                            val resources = context.getResources()
//                            val enlocale = Locale("hi")
//                            val config = android.content.res.Configuration()
//                            config.locale = enlocale
//                            resources.updateConfiguration(config, resources.displayMetrics)
//                            activity!!.finish()
//                            activity!!.startActivity(activity!!.intent)
//                        }
//
//
//                    } else if (item.getItemId() === R.id.english) {
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//                            val locale = Locale("en")
//                            Locale.setDefault(locale)
//
//                            val resources = context.getResources()
//
//                            val configuration = resources.configuration
//                            configuration.locale = locale
//
//                            resources.updateConfiguration(configuration, resources.displayMetrics)
//                            activity!!.finish()
//                            activity!!.startActivity(activity!!.intent)
//                        } else {
//                            val resources = context.getResources()
//                            val enlocale = Locale("en")
//                            val config = android.content.res.Configuration()
//                            config.locale = enlocale
//                            resources.updateConfiguration(config, resources.displayMetrics)
//                            activity!!.finish()
//                            activity!!.startActivity(activity!!.intent)
//                        }
//
//
//                    }
                    return true
                }
            })
            popup.show()
        })
        //  mylocation.text = getCompleteAddressString(lat!!.toDouble(),lng!!.toDouble())
        return root
    }

    fun callGetHomeData(userid: String, llProgressBar: View) {
        llProgressBar.visibility = View.VISIBLE
        homedetarray.clear()
        if (AppUtils.isNetworkAvalilable(activity!!)) {
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("userid", userid)

            val call = AppUtils.service.homedata(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        if (jsob.getString("status").equals("true")) {

                            val mainjsonArray = jsob.getJSONArray("result")
                            val jsonArray = mainjsonArray.getJSONObject(0)
                            val newArray = jsonArray.getJSONArray("Rent")

                            Log.e("checkdata", mainjsonArray.toString())
                            Log.e("checkdata", jsonArray.toString())
                            Log.e("checkdata", newArray.toString())
                            // Toast.makeText(activity!!,jsob.getString("msg"), Toast.LENGTH_SHORT).show()

                            for (i in 0 until newArray.length()) {
                                val jobject = newArray.getJSONObject(i)
                                homedetarray.add(
                                    HomeCatModel(
                                        jobject.getString("post_id"),
                                        jobject.getString("category_id"),
                                        jobject.getString("subcategory_id"),
                                        jobject.getString("child_category_id"),
                                        jobject.getString("add_title"),
                                        jobject.getString("description"),
                                        jobject.getString("contact_mobile"),
                                        jobject.getString("contact_name"),
                                        jobject.getString("city"),
                                        jobject.getString("landmark"),
                                        jobject.getString("latitude"),
                                        jobject.getString("longitude"),
                                        jobject.getString("amount"),
                                        jobject.getString("images"),
                                        jobject.getString("wishlist_status")))
                            }

                            llProgressBar.visibility = View.GONE
                            homeCatAdapter = HomeCatAdapter(homedetarray)
                            homerviewL.adapter = homeCatAdapter
                            homeCatAdapter.setItemClickListener(object :
                                HomeCatAdapter.ItemClickListener {
                                override fun onItemClick(view: View, position: Int, postId: String, type: String, categoryId: String) {
                                    if(type.equals("fav")){
                                        callAddWish(postId, userid)
                                    } else if(type.equals("notfav")){
                                        callAddWish(postId, userid)
                                    } else {
                                        val intent = Intent(activity, SeeAllData::class.java)
                                        intent.putExtra("categoryId",categoryId)
                                        intent.putExtra("userid",userid)
                                        startActivity(intent)
                                    }
                                }
                            })

                            val jsonArray1 = mainjsonArray.getJSONObject(0)
                            val newArray1 = jsonArray1.getJSONArray("PG/Hostels")
                            for (i in 0 until newArray1.length()) {
                                val jobject = newArray1.getJSONObject(i)
                                hostelarray.add(
                                    HostelModel(
                                        jobject.getString("post_id"),
                                        jobject.getString("category_id"),
                                        jobject.getString("subcategory_id"),
                                        jobject.getString("child_category_id"),
                                        jobject.getString("add_title"),
                                        jobject.getString("description"),
                                        jobject.getString("contact_mobile"),
                                        jobject.getString("contact_name"),
                                        jobject.getString("city"),
                                        jobject.getString("landmark"),
                                        jobject.getString("latitude"),
                                        jobject.getString("longitude"),
                                        jobject.getString("amount"),
                                        jobject.getString("images"),
                                        jobject.getString("wishlist_status")))
                            }

                            hostelAdapter = HostelAdapter(hostelarray)
                            hostelrview.adapter = hostelAdapter
                            hostelAdapter.setItemClickListener(object : HostelAdapter.ItemClickListener {
                                override fun onItemClick(view: View, position: Int, post_id: String,type:String, categoryId: String) {
                                    if(type.equals("fav")){
                                        callAddWish(post_id, userid)
                                    } else if(type.equals("notfav")){
                                        callAddWish(post_id, userid)
                                    } else {
//                                        val intent = Intent(activity, SeeAllData::class.java)
//                                        intent.putExtra("categoryId",categoryId)
//                                        intent.putExtra("userid",userid)
//                                        intent.putExtra("title","PG/Hostels")
//                                        startActivity(intent)
                                        val intent = Intent(activity, SeeAllData::class.java)
                                        intent.putExtra("categoryId",categoryId)
                                        intent.putExtra("userid",userid)
                                        startActivity(intent)
                                    }
                                }
                            })


//                            val mainjsonArray = jsob.getJSONArray("result")
//                            val jsonArray = mainjsonArray.getJSONObject(0)
//                            val newArray = jsonArray.getJSONArray("Rent")

                            val jsonArray2 = mainjsonArray.getJSONObject(0)
                            val newArray2 = jsonArray2.getJSONArray("Partners")
                            for (i in 0 until newArray2.length()) {
                                val jobject = newArray2.getJSONObject(i)
                                partnersarray.add(
                                    PartnersModel(
                                        jobject.getString("post_id"),
                                        jobject.getString("category_id"),
                                        jobject.getString("subcategory_id"),
                                        jobject.getString("child_category_id"),
                                        jobject.getString("add_title"),
                                        jobject.getString("description"),
                                        jobject.getString("contact_mobile"),
                                        jobject.getString("contact_name"),
                                        jobject.getString("city"),
                                        jobject.getString("landmark"),
                                        jobject.getString("latitude"),
                                        jobject.getString("longitude"),
                                        jobject.getString("amount"),
                                        jobject.getString("images"),
                                        jobject.getString("wishlist_status")))

                            }
                            partnersAdapter = PartnersAdapter(partnersarray)
                            partnersrview.adapter = partnersAdapter
                            partnersAdapter.setItemClickListener(object :
                                PartnersAdapter.ItemClickListener {
                                override fun onItemClick(view: View, position: Int, post_id: String,type:String, categoryId: String) {
                                    if(type.equals("fav")){
                                        callAddWish(post_id, userid)
                                    } else if(type.equals("notfav")){
                                        callAddWish(post_id, userid)
                                    } else {
//                                        val intent = Intent(activity, SeeAllData::class.java)
//                                        intent.putExtra("categoryId",categoryId)
//                                        intent.putExtra("userid",userid)
//                                        intent.putExtra("title","Partners")
//                                        startActivity(intent)
                                        val intent = Intent(activity, SeeAllData::class.java)
                                        intent.putExtra("categoryId",categoryId)
                                        intent.putExtra("userid",userid)
                                        startActivity(intent)
                                    }
                                }
                            })

                        } else {
                            Toast.makeText(activity!!, jsob.getString("msg"), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(activity!!, t.message, Toast.LENGTH_SHORT).show()
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

    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String {
        var strAdd = ""
        val geocoder = Geocoder(activity, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses.get(0)
                val strReturnedAddress = StringBuilder("")

                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    address = addresses.get(0).getAddressLine(i)
                }

                city = addresses.get(0).locality
                val state = addresses.get(0).adminArea
                val country = addresses.get(0).countryName
                val postalCode = addresses.get(0).postalCode
                val knownName = addresses.get(0).featureName

                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.e("Myc3", strReturnedAddress.toString())
            } else {
                Log.e("Myc2", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Myc", "Canont get Address!")
        }

        return city
    }
}