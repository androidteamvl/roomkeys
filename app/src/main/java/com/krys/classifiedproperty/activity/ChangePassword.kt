package com.krys.classifiedproperty.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.krys.classifiedproperty.R
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassword : AppCompatActivity() {

    lateinit var fabProcess:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        supportActionBar?.hide()
        val edtpass:EditText = findViewById(R.id.edtpass)
        val edtcpass:EditText = findViewById(R.id.edtcpass)
        val nextbtnpass:FloatingActionButton = findViewById(R.id.nextbtnpass)
        val mobileno: String = intent.getStringExtra("mobileno")
        fabProcess = findViewById(R.id.fabProgress)
        fabProcess.visibility = View.INVISIBLE
        nextbtnpass.setOnClickListener(View.OnClickListener {
            if(edtpass.text.toString().isEmpty()){
                Toast.makeText(this, "Password can't be empty!", Toast.LENGTH_SHORT).show()
            } else if(edtcpass.text.toString().isEmpty()){
                Toast.makeText(this, "Confirm password can't be empty!", Toast.LENGTH_SHORT).show()
            } else {
                if(!edtpass.text.toString().equals(edtcpass.text.toString())){
                    Toast.makeText(this, "Password not matched!", Toast.LENGTH_SHORT).show()
                } else {
                    callUpdatePassApi(mobileno,edtpass.text.toString())
                }
            }
        })
    }

    fun callUpdatePassApi(mobile:String,password:String) {
        if (AppUtils.isNetworkAvalilable(this)) {
            fabProcess.visibility = View.VISIBLE
            val internalObject = JsonObject()
            internalObject.addProperty("app_key", AppUtils.getApiKey())
            internalObject.addProperty("mobile", mobile)
            internalObject.addProperty("password",password)
            val call = AppUtils.service.updateforgotpassword(internalObject)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsob = JSONObject(response.body()!!.toString())
                        if(jsob.getString("status").equals("true")){
                            fabProcess.visibility = View.INVISIBLE
                            val intent = Intent(this@ChangePassword, Login::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                            Toast.makeText(this@ChangePassword, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            fabProcess.visibility = View.INVISIBLE
                            Toast.makeText(this@ChangePassword, "Failed! Please try again later.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@ChangePassword, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}
