package com.krys.classifiedproperty.Presenter

import android.util.Log
import com.krys.classifiedproperty.activity.MainActView
import com.krys.classifiedproperty.model.Postlist
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class MainActPresenter() {


    private var products_arrayList: ArrayList<Postlist> = arrayListOf()

    private lateinit var mactView: MainActView

    constructor(mactView: MainActView) : this() {
        this.mactView = mactView
        products_arrayList = ArrayList<Postlist>()
    }
 /*   fun Main(mactView: MainActView) {
        this.mactView = mactView
        products_arrayList = ArrayList<Postlist>()
    }
*/
    fun setAllData(response: String, pageIndex: Int) {

        try {
            val jsonObject = JSONObject(response)

            val productsJsonArray = jsonObject.getJSONArray("post_list")
            /*    Gson gson = new Gson();
            mDashboardDefaultResponser = gson.fromJson(jsonObject.toString(), DashboardDefaultResponser.class);
            if (mDashboardDefaultResponser != null) {

            }else{

            }*/
            if (productsJsonArray.length() >= 1) {
                if (pageIndex == 0) {
                    products_arrayList.clear()// --> Clear method is used to clear the ArrayList
                }

                for (i in 0 until  productsJsonArray.length()) {
                    val products_jsonObject = productsJsonArray.getJSONObject(i)
                    Log.e("dta", "setAllData: "+products_jsonObject)

                    if (products_jsonObject.has("post_id")) {
                        Log.e("jsob   ", "addlist:   "+  products_jsonObject.getString("post_id"))

                        if (!products_jsonObject.isNull("post_id")) {
                            Log.e("jsob   ", "addlist:   "+  products_jsonObject.getString("post_id"))

                                products_arrayList.add(Postlist(
                                    products_jsonObject.getString("post_id"),
                                    products_jsonObject.getString("category_id"),
                                    products_jsonObject.getString("subcategory_id"),
                                    products_jsonObject.getString("child_category_id"),
                                    products_jsonObject.getString("add_title"),
                                    products_jsonObject.getString("description"),
                                    products_jsonObject.getString("contact_mobile"),
                                    products_jsonObject.getString("contact_name"),
                                    products_jsonObject.getString("city"),
                                    products_jsonObject.getString("landmark"),
                                    products_jsonObject.getString("latitude"),
                                    products_jsonObject.getString("longitude"),
                                    products_jsonObject.getString("amount"),
                                    products_jsonObject.getString("images"),
                                    products_jsonObject.getString("wishlist_status"))
                                )

                            /*  products_pojo.setDistance(products_jsonObject.getString("distance"))
                              products_pojo.setAvg_time(products_jsonObject.getString("avg_time"))
                              products_pojo.setMenuName(products_jsonObject.getString("menu_name"))
                              products_pojo.setPrice(products_jsonObject.getString("price"))
                              products_pojo.setMenuPicss(products_jsonObject.getString("menu_picss"))
                              products_pojo.setVendorId(products_jsonObject.getString("vendor_id"))
                              products_pojo.setVendorName(products_jsonObject.getString("vendor_name"))
                              products_pojo.setVendorMenuId(products_jsonObject.getString("vendor_menu_id"))
                              products_pojo.setMenuCategory(products_jsonObject.getString("menu_category"))
                              products_pojo.setWeekday(products_jsonObject.getString("weekday"))
                              products_pojo.setManuType(products_jsonObject.getString("menu_type"))
                              products_pojo.setCurrency(products_jsonObject.getString("currency"))
                              products_pojo.setTotalRating(products_jsonObject.getString("totalRating"))
                              products_pojo.setVendorService(products_jsonObject.getString("vendor_service"))*/
                       }
                    }

                   // products_arrayList.add(products_pojo)
                }
                mactView.updateData(products_arrayList)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

}