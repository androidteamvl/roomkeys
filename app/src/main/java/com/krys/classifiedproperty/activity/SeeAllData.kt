package com.krys.classifiedproperty.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.krys.classifiedproperty.Presenter.MainActPresenter
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.adapters.SeeAllDataAdapter
import com.krys.classifiedproperty.model.Postlist
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class SeeAllData : AppCompatActivity(), MainActView {

    lateinit var rvdata: RecyclerView
    val internalObject = JsonObject()
    lateinit var progressBars: RelativeLayout
    internal var pageIndex = 1
    internal var pageIndex1 = 1
    internal var pageIndex12 = 1
    lateinit var mainActPresenter: MainActPresenter
    lateinit var products_arrayList: ArrayList<Postlist>
    private var isLoading = false
    internal lateinit var mAadapter: SeeAllDataAdapter
    var categoryId:String = ""
    var userid:String = ""
    lateinit var llProgressBar: View
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_data)
        supportActionBar?.hide()
        rvdata = findViewById(R.id.rvdata)
        progressBars = findViewById(R.id.rltv_progressBar)
        llProgressBar = findViewById(R.id.llProgressBar)
        val back:ImageView = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        categoryId = intent.getStringExtra("categoryId")
        userid = intent.getStringExtra("userid")
        val title:TextView = findViewById(R.id.title)
        title.text = "All Posts In "+intent.getStringExtra("title")
        llProgressBar.visibility = View.VISIBLE
        rvdata.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.e("pageIndex", "onScrolled: $pageIndex")
                val visibleItemCount = mLayoutManager.getChildCount()
                val totalItemCount = mLayoutManager.getItemCount()
                val firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        isLoading = true
                        pageIndex++
                        pageIndex12 = pageIndex
                        internalObject.addProperty("app_key", AppUtils.getApiKey())
                        internalObject.addProperty("page", pageIndex)
                        internalObject.addProperty("category_id", categoryId)
                        internalObject.addProperty("userid", userid)
                        callGetHomeData1(internalObject,pageIndex,llProgressBar)
                        progressBars.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        mainActPresenter = MainActPresenter(this@SeeAllData)
        internalObject.addProperty("app_key", AppUtils.getApiKey())
        internalObject.addProperty("page", pageIndex1)
        internalObject.addProperty("category_id", categoryId)
        internalObject.addProperty("userid", userid)

        callGetHomeData()
    }

    fun callGetHomeData() {
        if (AppUtils.isNetworkAvalilable(this@SeeAllData)) {
            val pageNumber = this.pageIndex1


            val call = AppUtils.service.postbycategory(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        llProgressBar.visibility = View.VISIBLE
                        if(jsob.getString("status").equals("true")){
                            Log.e("jsob   ", "addlist:ggh   "+ jsob)

                            if (jsob.length() >= 1) {

                                mainActPresenter.setAllData(jsob.toString(), pageIndex1)

                            } else {
                                //swipeRefreshLayout.setRefreshing(false);
                                if (pageNumber != 0) {
                                    pageIndex1 = pageNumber - 1
                                }
                            }

                            if (progressBars != null) {

                                progressBars.setVisibility(View.GONE)
                                llProgressBar.visibility = View.GONE
                            }

                        } else {
                            llProgressBar.visibility = View.GONE
                            Toast.makeText(this@SeeAllData,jsob.getString("msg"), Toast.LENGTH_SHORT).show()

                        }

                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@SeeAllData, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    fun callGetHomeData1(internalObject: JsonObject, pageIndex: Int, llProgressBar: View) {
        if (AppUtils.isNetworkAvalilable(this@SeeAllData)) {
            val pageNumber = pageIndex


            val call = AppUtils.service.postbycategory(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())


                        if(jsob.getString("status").equals("true")){
                            Log.e("pageIndex   ", "df   "+ jsob)
                            llProgressBar.visibility = View.GONE
                            if (jsob.length() >= 1) {

                                mainActPresenter.setAllData(jsob.toString(), pageIndex12)

                            } else {
                                //swipeRefreshLayout.setRefreshing(false);
                                if (pageNumber != 0) {
                                    pageIndex12 = pageNumber - 1
                                }
                            }

                            if (progressBars != null) {
                                llProgressBar.visibility = View.GONE
                                progressBars.setVisibility(View.GONE)
                            }

                        } else {
                            llProgressBar.visibility = View.GONE
                            Toast.makeText(this@SeeAllData,jsob.getString("msg"), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@SeeAllData, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

     override fun updateData(list: ArrayList<Postlist>) {
         products_arrayList = list
         Log.e("jsob   ", "addlist:fw   "+list)

         if (pageIndex == 1) {
             mAadapter = SeeAllDataAdapter(products_arrayList)
             rvdata.adapter = mAadapter
             mAadapter.setItemClickListener(object :SeeAllDataAdapter.ItemClickListener{
                 override fun onItemClick(view: View, position: Int, postId: String, type: String) {
                     if(type.equals("fav")){
                         callAddWish(postId, userid)
                     } else if(type.equals("notfav")){
                         callAddWish(postId, userid)
                     } else {
                         val intent = Intent(this@SeeAllData, PropertyDetails::class.java)
                         intent.putExtra("postid",postId)
                         startActivity(intent)
                     }
                 }
             })

             mLayoutManager = LinearLayoutManager(this@SeeAllData)
           //  mLayoutManager.orientation = VERTICAL
             mLayoutManager = LinearLayoutManager(this@SeeAllData,
                 LinearLayoutManager.VERTICAL,false)
             rvdata.setLayoutManager(mLayoutManager)
         }
         else {
             Log.e("hi", "updateData: " + "hi")
             isLoading = false
             if (mAadapter != null) {
                 mAadapter.notifyDataSetChanged()
             }
         }

    }

    fun callAddWish(postId: String, userid: String) {

        if (AppUtils.isNetworkAvalilable(this)) {
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
                            Toast.makeText(this@SeeAllData, jsob.getString("msg"), Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this@SeeAllData, jsob.getString("msg"), Toast.LENGTH_SHORT)
                                .show()
                            //  llProgressBar.visibility = View.GONE

                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@SeeAllData, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

}
