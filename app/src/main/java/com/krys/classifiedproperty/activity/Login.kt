package com.krys.classifiedproperty.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.adapters.FeatureAdapter
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class Login : AppCompatActivity() {

    lateinit var listDialog: Dialog
    lateinit var login:RelativeLayout
    lateinit var firstlay:RelativeLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val edtphone:EditText = findViewById(R.id.edtphone)
        val btn:FloatingActionButton = findViewById(R.id.nextbtn)
        val progresslay:RelativeLayout = findViewById(R.id.progresslay)
        val fabProgress:ProgressBar = findViewById(R.id.fabProgress)
        login = findViewById(R.id.login)
        supportActionBar?.hide()
        val number:String? = intent.getStringExtra("number")
        if(number!=null){
            edtphone.setText(number)
        }
        fabProgress.visibility = View.INVISIBLE
        val preference:SharedPreferences = getSharedPreferences("MyPrf", Context.MODE_PRIVATE)
        val back:String = preference.getString("back",null).toString()
//        if(back.equals("fromotp")){
//            fabProgress.visibility = View.INVISIBLE
//        } else {
//            fabProgress.visibility = View.VISIBLE
//        }
        btn.setOnClickListener {

            if (edtphone.text.isEmpty()) {
                Toast.makeText(this@Login, "TextField can't be empty!", Toast.LENGTH_SHORT).show()
            } else {
                if(edtphone.text.length == 10){
                    if (AppUtils.isNetworkAvalilable(this)) {
                        fabProgress.visibility = View.VISIBLE
                        val internalObject = JsonObject()
                        internalObject.addProperty("app_key", AppUtils.getApiKey())
                        internalObject.addProperty("mobile", edtphone.text.toString())
                        val call = AppUtils.service.checklogin(internalObject)
                        call.enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                    val editor: SharedPreferences.Editor = this@Login.getSharedPreferences("MyPrf", Context.MODE_PRIVATE).edit()
                                    editor.putString("mobileno", edtphone.text.toString())
                                    editor.apply()
                                    editor.commit()
                                    val jsob = JSONObject(response.body()!!.toString())
                                    if(jsob.getString("status").equals("true")){
                                        val intent = Intent(this@Login, OTPandCreateProfile::class.java)
                                        intent.putExtra("exist","no")
                                        intent.putExtra("login_otp",jsob.getString("login_otp"))
                                        startActivity(intent)
                                        fabProgress.visibility = View.INVISIBLE
                                        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                                        finish()
                                    } else {
                                        val intent = Intent(this@Login, OTPandCreateProfile::class.java)
                                        intent.putExtra("exist","yes")
                                        startActivity(intent)
                                        fabProgress.visibility = View.INVISIBLE
                                        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                                        finish()
                                    }

                                }
                            }
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                Toast.makeText(this@Login, t.message, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } else {
                    Toast.makeText(this@Login, "Invalid Number!", Toast.LENGTH_SHORT).show()
                }

            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

    fun otpnuserdetails(string: String) {
        listDialog = Dialog(this@Login, R.style.DialogSlideAnim)
        val li = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val va = li.inflate(R.layout.otpdialog, null, false)
        val lay2:RelativeLayout = va.findViewById(R.id.lay2)
        val tvquestion:RelativeLayout = va.findViewById(R.id.tvquestion)
        val back:Button = va.findViewById(R.id.back)
        val next:Button = va.findViewById(R.id.next)
        val text3:TextView = va.findViewById(R.id.text3)
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = listDialog.getWindow()
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        va.setBackground(getResources().getDrawable(R.drawable.roundlay))
        if (string.equals("new")) {
            lay2.visibility = View.VISIBLE
            tvquestion.visibility = View.GONE
            text3.text = resources.getText(R.string.createprofile)
        } else {
            lay2.visibility = View.GONE
            tvquestion.visibility = View.VISIBLE
            text3.text = resources.getText(R.string.wehavesent)
        }
        back.setOnClickListener(View.OnClickListener {
            listDialog.dismiss()
        })
        next.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@Login, OTPandCreateProfile::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            listDialog.dismiss()
        })
        listDialog.setContentView(va)
        listDialog.setCancelable(true)
        listDialog.show()
    }
}
