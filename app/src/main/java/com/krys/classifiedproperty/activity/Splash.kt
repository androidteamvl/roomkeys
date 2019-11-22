package com.krys.classifiedproperty.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.krys.classifiedproperty.BuildConfig
import com.krys.classifiedproperty.R
import java.util.*
import kotlin.concurrent.timerTask

class Splash : AppCompatActivity() {

    var userid:String? = ""

    val PERMISSION_ID = 42
    private val SPLASH_TIME_OUT: Long = 3000
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        val prefs:SharedPreferences = getSharedPreferences("MyPrf", Context.MODE_PRIVATE)
        userid = prefs.getString("userid", null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        Timer().schedule(timerTask {
            checkFirstRun()
        }, 3000)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val logo: ImageView = findViewById(R.id.logo)
        logo.animate().alpha(1f).duration = 200

    }

    fun checkFirstRun() {
        var isFirstRun: Boolean = true
        isFirstRun = getSharedPreferences("MyPrf", MODE_PRIVATE).getBoolean("isFirstRun", true)
        if (isFirstRun) {
            getLastLocation()
            val editor: SharedPreferences.Editor = getSharedPreferences("MyPrf", Context.MODE_PRIVATE).edit()
            editor.putBoolean("isFirstRun", false)
            editor.apply()
            editor.commit()
        } else {
            if(userid!=null){
                startActivity(Intent(this, Home::class.java))
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                finish()
            } else {
                startActivity(Intent(this, Login::class.java))
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                finish()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val preference = getSharedPreferences("MyPrf", Context.MODE_PRIVATE)
                        val editor = preference.edit()
                        editor.putString("lat", location.latitude.toString())
                        editor.putString("lng", location.longitude.toString())
                        editor.commit()
                        startActivity(Intent(this, FirstTimeFeature::class.java))
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                        finish()
                    }
                }
            } else {
               // Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            val preference = getSharedPreferences("MyPrf", Context.MODE_PRIVATE)
            val editor = preference.edit()
            editor.putString("lat", mLastLocation.latitude.toString())
            editor.putString("lng", mLastLocation.longitude.toString())
            editor.commit()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

}
