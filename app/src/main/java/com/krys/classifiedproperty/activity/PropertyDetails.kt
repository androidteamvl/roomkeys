package com.krys.classifiedproperty.activity

import android.content.Intent
import android.icu.text.Transliterator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.JsonObject
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.adapters.CardAdapter
import com.krys.classifiedproperty.adapters.SliderAdapter
import com.krys.classifiedproperty.adapters.ViewPagerAdapter
import com.krys.classifiedproperty.model.CatModel
import com.krys.classifiedproperty.model.Postdetail
import com.krys.classifiedproperty.utils.CircleIndicator
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class PropertyDetails : AppCompatActivity() {

    lateinit var viewPager2:ViewPager2
    var namearray: ArrayList<String> = ArrayList()
    val catarray:ArrayList<Postdetail> = ArrayList()
    var Indexpage1:Int = 0
    var Posi:Int = 0
    lateinit var postid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_propery_details)
        viewPager2 = findViewById(R.id.viewpager2)
       // mPager = findViewById(R.id.pager)
        supportActionBar?.hide()
        namearray.add("43")
        namearray.add("12")
        namearray.add("2")
        namearray.add("12")
        namearray.add("6")
        namearray.add("28")
        namearray.add("12")
        namearray.add("2")
        namearray.add("12")
        namearray.add("6")
        namearray.add("28")
        val intent = getIntent()

         postid = intent.getStringExtra("postid")



//        val recyclerView:RecyclerView = findViewById(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
//        val cardAdapter = CardAdapter(namearray)
//        val pagerhelper = PagerSnapHelper()
//        pagerhelper.attachToRecyclerView(recyclerView)
//        recyclerView.adapter = cardAdapter





        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)


                if(position==catarray.lastIndex){
                    Indexpage1++
                    Posi = position
                    callGetHomeData()

                    Log.e("position"," 1      "+position+"               "+catarray.lastIndex)
                }else{
                    Log.e("position"," 2      "+position+"               "+catarray.lastIndex)
                }

            }
        })
        Indexpage1  = 1

        callGetHomeData()
//        val swipeableTouchHelperCallback: SwipeableTouchHelperCallback =
//                object : SwipeableTouchHelperCallback(object : OnItemSwiped {
//                    override fun onItemSwiped() {
//                        cardAdapter.removeTopItem()
//                    }
//
//                    override fun onItemSwipedLeft() {
//                        Log.e("SWIPE", "LEFT")
//                    }
//
//                    override fun onItemSwipedRight() {
//                        Log.e("SWIPE", "RIGHT")
//                    }
//
//                    override fun onItemSwipedUp() {
//                        Log.e("SWIPE", "UP")
//                    }
//
//                    override fun onItemSwipedDown() {
//                        Log.e("SWIPE", "DOWN")
//                    }
//                }) {
//                    override fun getAllowedSwipeDirectionsMovementFlags(viewHolder: RecyclerView.ViewHolder?): Int {
//                        return ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
//                    }
//                }


//        val itemTouchHelper = ItemTouchHelper(swipeableTouchHelperCallback)
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//        recyclerView.layoutManager = SwipeableLayoutManager().setAngle(10)
//                .setAnimationDuratuion(450)
//                .setMaxShowCount(3)
//                .setScaleGap(0.1f)
//                .setTransYGap(0)
//        recyclerView.adapter = cardAdapter

//        val button = findViewById<AppCompatButton>(R.id.swipe)
//        button.setOnClickListener {
//            itemTouchHelper.swipe(
//                recyclerView.findViewHolderForAdapterPosition(
//                    0
//                ), ItemTouchHelper.DOWN
//            )
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        }
       // init()
    }

    fun callGetHomeData() {
        if (AppUtils.isNetworkAvalilable(this@PropertyDetails!!)) {
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("page",Indexpage1)
            internalObject.addProperty("post_id",postid)



            val call = AppUtils.service.Getpostdetail(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())


                        if(jsob.getString("status").equals("true")){
                            Log.e("jsob   ", "addlist:ggh   "+ jsob)
                            val jsonArray = jsob.getJSONArray("post_detail")
                            for (i in 0 until jsonArray.length()) {

                                val jobj = jsonArray.getJSONObject(i)
                                catarray.add(
                                    Postdetail(
                                        jobj.getString("category_name"),
                                        jobj.getString("subcategory_name"),
                                        jobj.getString("sub_subcategory_name"),
                                        jobj.getString("add_title"),
                                        jobj.getString("description"),
                                        jobj.getString("contact_mobile"),
                                        jobj.getString("contact_name"),
                                        jobj.getString("product_cover_db"),
                                        jobj.getString("corresspondance_add"),
                                        jobj.getString("landmark"),
                                        jobj.getString("latitude"),
                                        jobj.getString("longitude"),
                                        jobj.getString("amount"),
                                        jobj.getString("product_images"))
                                    )

                            }

                            val viewPagerAdapter = ViewPagerAdapter(catarray)
                            viewPager2.adapter = viewPagerAdapter
                            viewPager2.setCurrentItem(Posi)
                            viewPagerAdapter.setItemClickListener(object :ViewPagerAdapter.ItemClickListener{
                                override fun onItemClick(view: View, position: Int) {

                                    onBackPressed()
                                }

                            })

                        } else {
                           // Toast.makeText(this@PropertyDetails,jsob.getString("msg"), Toast.LENGTH_SHORT).show()

                        }

                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@PropertyDetails, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }





    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }
}
