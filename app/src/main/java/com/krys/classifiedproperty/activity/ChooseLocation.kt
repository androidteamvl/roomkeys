package com.krys.classifiedproperty.activity

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.krys.classifiedproperty.R
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class ChooseLocation : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {


    lateinit var mGoogleMap: GoogleMap
    private var onCameraIdleListener: GoogleMap.OnCameraIdleListener? = null
    var mapFrag: SupportMapFragment? = null
    lateinit var mLocationRequest: LocationRequest
    var mLastLocation: Location? = null
    internal var mCurrLocationMarker: Marker? = null
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    internal lateinit var byId: AutocompleteSupportFragment
    lateinit var location: FloatingActionButton
    lateinit var fablocation: FloatingActionButton
    lateinit var nextbtnpass: FloatingActionButton
    lateinit var fabProgressmap: ProgressBar
    lateinit var newlatLng: LatLng
    lateinit var bottom_sheet: View
    var locals: String = ""
    var locality: String = ""
    var local: String = ""
    var area: String = ""
    var country: String = ""
    var locality_name: String = ""
    var pincode: String = ""
    var city: String = ""
    var latitudes: String = ""
    var longitudes: String = ""
    var string: String = ""
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapcoordinatorlay)
        supportActionBar?.hide()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        location = findViewById(R.id.location)
        textView = findViewById(R.id.resultText)
        fablocation = findViewById(R.id.fabloc)
        fabProgressmap = findViewById(R.id.fabProgressmap)
        val updown: ImageView = findViewById(R.id.updown)
        nextbtnpass = findViewById(R.id.nextbtnpass)
        bottom_sheet = findViewById(R.id.bottom_sheet)
        val edtlandmark: EditText = findViewById(R.id.edtlandmark)
        val bottomSheetBehavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(bottom_sheet)
        mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag?.getMapAsync(this)
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key))
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, resources.getString(R.string.google_maps_key))
        }
        updown.setOnClickListener(View.OnClickListener {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                updown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                updown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
            }
        })
        val prefs: SharedPreferences = getSharedPreferences("MyPrf", Context.MODE_PRIVATE)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
                fablocation.animate().scaleX(1 - p1).scaleY(1 - p1).setDuration(0).start()
            }

            override fun onStateChanged(p0: View, p1: Int) {
            }

        })
        nextbtnpass.setOnClickListener(View.OnClickListener {
            if (edtlandmark.text.toString().isEmpty()) {
                Toast.makeText(
                    this,
                    "Please add a landmark in the given section!",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                val internalObject = JsonObject()
                internalObject.addProperty("app_key", AppUtils.getApiKey())
                internalObject.addProperty("category_id", prefs.getString("category_id", null))
                internalObject.addProperty("sub_category_id", prefs.getString("sub_category_id", null))
                internalObject.addProperty("child_category_id", prefs.getString("child_category_id", null))
                internalObject.addProperty("title", prefs.getString("title", null))
                internalObject.addProperty("amount", prefs.getString("amount", null))
                internalObject.addProperty("description", prefs.getString("description", null))
                internalObject.addProperty("name", prefs.getString("name", null))
                internalObject.addProperty("mobile", prefs.getString("mobile", null))
                internalObject.addProperty("role", prefs.getString("role", null))
                internalObject.addProperty("user_id", prefs.getString("user_id", null))
                internalObject.addProperty("address", string)
                internalObject.addProperty("landmark", edtlandmark.text.toString())
                internalObject.addProperty("latitude", latitudes)
                internalObject.addProperty("longitude", longitudes)
                internalObject.addProperty("post_iamge", intent.getStringExtra("product_image"))

                Log.e("checkvalues", intent.getStringExtra("product_image").toString())

                val appkey: RequestBody = createPart("@sfrtyuop(roomkey)1a8448ee8ty")
                val category_id: RequestBody = createPart(prefs.getString("category_id", null)!!)
                val sub_category_id: RequestBody =
                    createPart(prefs.getString("sub_category_id", null)!!)
                val child_category_id: RequestBody =
                    createPart(prefs.getString("child_category_id", null)!!)
                val title: RequestBody = createPart(prefs.getString("title", null)!!)
                val amount: RequestBody = createPart(prefs.getString("amount", null)!!)
                val name: RequestBody = createPart(prefs.getString("name", null)!!)
                val description: RequestBody = createPart(prefs.getString("description", null)!!)
                val mobile: RequestBody = createPart(prefs.getString("mobile", null)!!)
                val role: RequestBody = createPart(prefs.getString("role", null)!!)
                val user_id: RequestBody = createPart(prefs.getString("user_id", null)!!)
                val address: RequestBody = createPart(string)
                val landmark: RequestBody = createPart(edtlandmark.text.toString())
                val latitude: RequestBody = createPart(latitudes)
                val longitude: RequestBody = createPart(longitudes)
                val post_iamge: RequestBody = createPart(intent.getStringExtra("product_image")!!)


                val call = AppUtils.service.createpost(internalObject)
                call.enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                        val jobject = JSONObject(Gson().toJson(response.body()))
                        Log.e("response", "onResponse: $jobject")
                        val status = jobject.getString("status")
                        val string: String = response.body().toString()
                        Log.e("testqq", "sucess" + status)

                        if (string != null) {


                            val intent = Intent(this@ChooseLocation, AddPostSucces::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                            fabProgressmap.visibility = View.INVISIBLE

                            Log.e("testqq", "sucess6")
                            Toast.makeText(this@ChooseLocation, string, Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            Log.e("testqq2", "fail")
                            Toast.makeText(this@ChooseLocation, "Fail", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Toast.makeText(this@ChooseLocation, t.message, Toast.LENGTH_SHORT).show()
                    }
                })

                // uploadtoserver(intent.getStringExtra("filearray"))


//                if (AppUtils.isNetworkAvalilable(this)) {
//
//                    fabProgressmap.visibility = View.VISIBLE
//                    val call = AppUtils.service.createpost(internalObject)
//                    call.enqueue(object : Callback<JsonObject> {
//                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                            if (response.isSuccessful) {
//                                val jsob = JSONObject(response.body()!!.toString())
//                                if(jsob.getString("status").equals("true")){
//                                    Toast.makeText(this@ChooseLocation, jsob.getString("msg"), Toast.LENGTH_SHORT).show()
//                                    val intent = Intent(this@ChooseLocation, AddPostSucces::class.java)
//                                    startActivity(intent)
//                                    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
//                                    fabProgressmap.visibility = View.INVISIBLE
//                                } else {
//                                    fabProgressmap.visibility = View.INVISIBLE
//                                    Toast.makeText(this@ChooseLocation, "Failed! Please again later.", Toast.LENGTH_SHORT).show()
//
//                                }
//
//                            }
//                        }
//                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                            Toast.makeText(this@ChooseLocation, t.message, Toast.LENGTH_SHORT).show()
//                        }
//                    })
//                }
                Log.e("ERROR22", internalObject.toString())
            }
        })
        configureCameraIdle()

        byId =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        byId.setPlaceFields(Arrays.asList<Place.Field>(Place.Field.LAT_LNG))

        byId.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(@NonNull place: Place) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
            }

            override fun onError(@NonNull status: Status) {
                Log.e("ERROR", "An error occurred: $status")
            }
        })
        fablocation.setOnClickListener(View.OnClickListener {

            val cameraPosition = CameraPosition.Builder()
                .target(LatLng(newlatLng.latitude, newlatLng.longitude))
                .zoom(18f)
                .bearing(90f)
                .tilt(0f)
                .build()
            val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
            mGoogleMap.animateCamera(cu)
        })


    }

    private fun createPart(descp: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), descp)
    }

    override fun onMapLoaded() {

    }

    private fun configureCameraIdle() {
        onCameraIdleListener = GoogleMap.OnCameraIdleListener {

            val latLng = mGoogleMap.getCameraPosition().target
            if (latLng != null) {
                val updates = getUpdates()
                updates.execute(latLng)
            }
        }
    }

    inner class getUpdates : AsyncTask<LatLng, String, String>() {

        override fun doInBackground(vararg strings: LatLng): String {
            val latLng = strings[0]
            val geocoder = Geocoder(this@ChooseLocation)

            try {
                val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                latitudes = latLng.latitude.toString()
                longitudes = latLng.longitude.toString()


                if (addressList != null && addressList.size > 0) {
                    val obj = addressList[0]
                    val add = obj.getAddressLine(0)
                    // locals = obj.subLocality
                    local = obj.locality
                    area = obj.adminArea
                    country = obj.countryName
                    pincode = obj.postalCode
                    locality = obj.getAddressLine(0)
                    //    locality = addressList.get(0).getAddressLine(0);
                    val country = addressList[0].countryName
                    if (!locality.isEmpty()/* && !country.isEmpty()*/)
                        string = locality /*+ " " + country*/

                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return ""
        }

        override fun onPostExecute(result: String) {

            if (locality != null) {
                byId.setText(locality)
                textView.setText(string)
            }
            //
        }


        override fun onPreExecute() {


        }
    }

    internal var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                Log.i(
                    "MapsActivity",
                    "Location: " + location.getLatitude() + " " + location.getLongitude()
                )
                mLastLocation = location
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker?.remove()
                }
                var latLng = LatLng(location.latitude, location.longitude)
                newlatLng = latLng
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title("Current Position")
                //  mCurrLocationMarker = mGoogleMap.addMarker(markerOptions)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0F))
            }
        }
    }

    public override fun onPause() {
        super.onPause()
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        var vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable!!.setBounds(0, 0, 45, 45);
        var bitmap = Bitmap.createBitmap(
            vectorDrawable.getIntrinsicWidth(),
            vectorDrawable.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888
        );
        var canvas = Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 120000 // two minute interval
        mLocationRequest.fastestInterval = 120000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mGoogleMap.setOnCameraIdleListener(onCameraIdleListener)
        mGoogleMap.setOnMapLoadedCallback(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mFusedLocationClient?.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.myLooper()
                )
            } else {
                checkLocationPermission()
            }
        } else {
            mFusedLocationClient?.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                Looper.myLooper()
            )
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this@ChooseLocation,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()


            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        mFusedLocationClient?.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.myLooper()
                        )
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    companion object {
        val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}