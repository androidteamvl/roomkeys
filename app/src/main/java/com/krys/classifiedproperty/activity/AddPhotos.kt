package com.krys.classifiedproperty.activity

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.Paint.FILTER_BITMAP_FLAG
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.krys.classifiedproperty.R
import com.krys.classifiedproperty.adapters.PhotosAdapter
import com.krys.classifiedproperty.utils.FileUtils
import com.krys.classifiedproperty.utils.ProgressRequestBody
import com.krys.kotlinamritlife.NetworkCall.AppUtils
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class AddPhotos : AppCompatActivity(),ProgressRequestBody.UploadCallbacks {

    private val MY_PERMISSIONS_REQUEST_STORAGE_CODE = 197
    private val MY_PERMISSIONS_REQUEST_CAMERA_CODE = 341
    private val GALLERY = 1
    private val CAMERA = 2
    lateinit var imageview: ImageView
    lateinit var imagesrview: RecyclerView
    lateinit var fabaddphoto: FloatingActionButton
    var imageFilePath: String = ""
    var imageFilePath2: String = ""
    var product_image: String = ""
    var imagesPathList: MutableList<String> = arrayListOf()
    var filearray: ArrayList<File> = arrayListOf()
    var checkvalue: String = ""

    var photoUri: Uri? = null
    val maxHeight:Float = 1280.0f
    val maxWidth:Float = 1280.0f

    var photolist:ArrayList<Uri> = arrayListOf()
    var progressDoalog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photos)
        supportActionBar?.hide()
        val addphoto: RelativeLayout = findViewById(R.id.addphoto)
        val addphotoempty: RelativeLayout = findViewById(R.id.addphotoempty)
        val mylocation: TextView = findViewById(R.id.mylocation)
        val imageAdd: ImageView = findViewById(R.id.imageAdd)
        val back:ImageView = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        imageAdd.visibility = View.GONE
        fabaddphoto = findViewById(R.id.fabaddphoto)
        fabaddphoto.hide()
        imagesrview = findViewById(R.id.imagesrview)
        imageview = findViewById(R.id.imageview)
        progressDoalog = ProgressDialog(this@AddPhotos)
        progressDoalog!!.setCanceledOnTouchOutside(false)
        requestCameraRuntimePermissions()
        requestReadStorageRuntimePermission()
        val fadeout: Animation = AnimationUtils.loadAnimation(this@AddPhotos, R.anim.fadeout);
        addphoto.setOnClickListener(View.OnClickListener {
            addphotoempty.animation = fadeout
            addphotoempty.visibility = View.GONE
            imageAdd.visibility = View.VISIBLE
            mylocation.text = "Choose Photos"
            val camera: RelativeLayout
            val gallery: RelativeLayout
            val dialog = Dialog(this@AddPhotos)
            val li =
                this@AddPhotos.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v = li.inflate(R.layout.picselector, null, false)
            val window = dialog.getWindow()
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            v.setBackground(resources.getDrawable(R.drawable.roundalert))
            dialog.setContentView(v)
            camera = dialog.findViewById(R.id.camera)
            gallery = dialog.findViewById(R.id.gallery)
            dialog.show()
            camera.setOnClickListener {
                takePhotoFromCamera()
                checkvalue = "camera"
                dialog.dismiss()
            }
            gallery.setOnClickListener {
                choosePhotoFromGallary()
                checkvalue = "gallery"
                dialog.dismiss()
            }
        })
        fabaddphoto.setOnClickListener(View.OnClickListener {


            if(photolist.size==10){
                upload()
            }else if(photolist.size==9){
                photolist.add(Uri.EMPTY)
                upload()
            }else if(photolist.size==8){
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                upload()
            }else if(photolist.size==7){
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                upload()
            }else if(photolist.size==6){
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                upload()
            }else if(photolist.size==5){
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                upload()
            }else if(photolist.size==4){
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                upload()
            }else if(photolist.size==3){
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                upload()
            }else if(photolist.size==2){
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                upload()
            }else if(photolist.size==1){
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                photolist.add(Uri.EMPTY)
                upload()
            }




            //            calApi(imagesPathList)
//            upload(imagesPathList)
//            val intent = Intent(this@AddPhotos, ChooseLocation::class.java)
//            intent.putExtra("filearray",filearray.toString())
//
//            startActivity(intent)
//            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        })
        imageAdd.setOnClickListener(View.OnClickListener {
            addphotoempty.animation = fadeout
            addphotoempty.visibility = View.GONE
            imageAdd.visibility = View.VISIBLE
            mylocation.text = "Choose Photos"
            val camera: RelativeLayout
            val gallery: RelativeLayout
            val dialog = Dialog(this@AddPhotos)
            val li =
                this@AddPhotos.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v = li.inflate(R.layout.picselector, null, false)
            val window = dialog.getWindow()
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            v.setBackground(resources.getDrawable(R.drawable.roundalert))
            dialog.setContentView(v)
            camera = dialog.findViewById(R.id.camera)
            gallery = dialog.findViewById(R.id.gallery)
            dialog.show()
            camera.setOnClickListener {
                takePhotoFromCamera()
              //  checkvalue = "camera"
                dialog.dismiss()
            }
            gallery.setOnClickListener {
                choosePhotoFromGallary()
               // checkvalue = "gallery"
                dialog.dismiss()
            }

           /* if (checkvalue.equals("camera")) {
                takePhotoFromCamera()
            } else {
                choosePhotoFromGallary()
            }*/

        })


    }

    fun choosePhotoFromGallary() {
        var intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null){
            if (requestCode == GALLERY) {
                if (data.getClipData() != null) {
                    var count = data.clipData!!.itemCount
                    for (i in 0..count - 1) {
                        var imageUri: Uri = data.clipData!!.getItemAt(i).uri
                        photoUri = imageUri
                        //  photolist.add(imageUri)
                        getPathFromURI(imageUri)
                    }
                } else if (data.getData() != null) {
                    var imagePath: Uri = data.getData()!!
                    Log.e("imagePath", imagePath.toString())
                    photoUri = imagePath
                    // photolist.add(imagePath)
                    getPathFromURI(imagePath)
                }
            } else if (requestCode == CAMERA) {

                val thumbnail = data.extras!!.get("data") as Bitmap
                var imageUri: Uri
                imageUri = getUriFromBit(this@AddPhotos, thumbnail)
                photoUri = imageUri
                // photolist.add(imageUri)
                getPathFromURI(imageUri)
                //Toast.makeText(thiscontext, "Image Saved!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(
                this@AddPhotos,
                "No Image Found!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun getUriFromBit(context: Context, bitmap: Bitmap): Uri {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val path: String = MediaStore.Images.Media.insertImage(
            context.getContentResolver(),
            bitmap,
            "Title",
            null
        );
        return Uri.parse(path)
    }

    private fun getPathFromURI(uri: Uri):String {
        var path: String? = uri.path // uri = any content Uri

        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (path!!.contains("/document/image:")) {
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else {
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATE_TAKEN
            ) // some example data you can query
            val cursor = this@AddPhotos.contentResolver.query(
                databaseUri,
                projection, selection, selectionArgs, null
            )
            if (cursor!!.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(projection[0])
                imageFilePath = cursor.getString(columnIndex)


                // Log.e("path", imagePath);
                imagesrview.layoutManager = GridLayoutManager(this@AddPhotos, 2)
                if (imagesPathList.size < 10) {
//                    val uri: Uri = Uri.parse(imageFilePath)
//                    var file = File(uri.toString())
//                    var newfile = File(cacheDir.toString() + "/" + File.separator + "test.jpg")
//                    try {
//                        file.createNewFile()
//                        val data1 = byteArrayOf(1, 1, 0, 0)
//                        if (file.exists()) {
//                            val fo = FileOutputStream(file)
//                            fo.write(data1)
//                            fo.close()
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                    filearray.add(newfile)
//
//                    imagesPathList.add(imageFilePath)
//
                    if (imagesPathList.contains(imageFilePath)) {
                        Toast.makeText(this@AddPhotos, "Duplicate image exist", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        imagesPathList.add(imageFilePath)
                    }
                    if (photolist.contains(uri)) {
//                        Toast.makeText(this@AddPhotos, "Duplicate image exist", Toast.LENGTH_SHORT)
//                            .show()
                    } else {
                        photolist.add(uri)
                    }
                } else {
                    Toast.makeText(
                        this@AddPhotos,
                        "You can select 10 images only.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                val photoAdapter = PhotosAdapter(imagesPathList)
                imagesrview.adapter = photoAdapter
                photoAdapter.setItemClickListener(object:PhotosAdapter.ItemClickListener{
                    override fun onItemClick(view: View, position: Int) {
                        photolist.removeAt(position)
                        imagesPathList.removeAt(position)
                        Log.e("size", photolist.size.toString())
                        photoAdapter.notifyItemRemoved(position)
                        photoAdapter.notifyItemRangeChanged(position, imagesPathList.size)
                    }
                })
                fabaddphoto.show()
                Log.e("size", imagesPathList.size.toString())
                photoAdapter.notifyDataSetChanged()
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("error", e.message, e)
        }
        return imageFilePath
    }

    private fun getPathFromURI2(uri: Uri):String {
        var path: String? = uri.path // uri = any content Uri

        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (path!!.contains("/document/image:")) {
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else {
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATE_TAKEN
            ) // some example data you can query
            val cursor = this@AddPhotos.contentResolver.query(
                databaseUri,
                projection, selection, selectionArgs, null
            )
            if (cursor!!.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(projection[0])
                imageFilePath2 = cursor.getString(columnIndex)
                // Log.e("path", imagePath);
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("error", e.message, e)
        }
        return imageFilePath2
    }

//    fun calApi(imagear:MutableList<String>) {
//
//        if (AppUtils.isNetworkAvalilable(this)){
//        val internalObject = JsonObject()
//        internalObject.addProperty("app_key", AppUtils.getApiKey())
//            internalObject.addProperty("product_image", )
//            for(i in 0 until imagear.size){
//                internalObject.addProperty("product_image"+i, imagear.get(i))
//            }
//        val call = AppUtils.service.createimage(internalObject)
//        call.enqueue(object : Callback<JsonObject> {
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                if (response.isSuccessful) {
//                    val jsob = JSONObject(response.body()!!.toString())
//                    if (jsob.getString("status").equals("true")) {
//                        Toast.makeText(this@AddPhotos, jsob.getString("msg"), Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this@AddPhotos, jsob.getString("msg"), Toast.LENGTH_SHORT).show()
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                Toast.makeText(this@AddPhotos, t.message, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}


    fun requestCameraRuntimePermissions() {
        if (ContextCompat.checkSelfPermission(
                this@AddPhotos,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this@AddPhotos,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@AddPhotos,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                MY_PERMISSIONS_REQUEST_CAMERA_CODE
            )
        } else {
        }

    }


    private fun createPart(descp: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), descp)
    }


    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val newfile:File
        var value:Int
        var compressedImageFile:File?=null
        if(fileUri == Uri.EMPTY){
            value = 0
            newfile = File(cacheDir.toString() + "/" + File.separator + "blank.jpg")
            try {
                newfile.createNewFile()
                val data1 = byteArrayOf(1, 1, 0, 0)
                if (newfile.exists()) {
                    val fo = FileOutputStream(newfile)
                    fo.write(data1)
                    fo.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }else{
            value = 1
            val path:String = getPathFromURI2(fileUri)
            newfile = File(path)
            compressedImageFile = Compressor(this).setQuality(75).compressToFile(newfile)
        }
        val tempFile:File
        if(value.equals(0)){
            tempFile = newfile
        } else {
            tempFile = compressedImageFile!!
        }
        Log.e("fileUri","   "+fileUri)

        // val file: File = FileUtils.getFile(this, fileUri)
        val fileBody = ProgressRequestBody(tempFile, this)
        // val requestBody: RequestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), newfile)
        return MultipartBody.Part.createFormData(partName, tempFile.getName(), fileBody)
    }

//    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
//
//        if(fileUri== Uri.EMPTY){
//
//        }
//    //    val path:String = getPathFromURI(fileUri)
//    //    val newfile = File(path)
//        val file: File = FileUtils.getFile(this, fileUri)
//        val fileBody = ProgressRequestBody(file, this)
//        // val requestBody: RequestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), newfile)
//        return MultipartBody.Part.createFormData(partName, file.getName(), fileBody)
//    }

//    fun genEmptyFile(file:File):File{
//        var emFile = file
//        var int:Int = 0
//        file = File(cacheDir.toString() + "/" + File.separator + "test$int.jpeg")
//        try {
//            emFile.createNewFile()
//            val data1 = byteArrayOf(1, 1, 0, 0)
//            if (file.exists()) {
//                val fo = FileOutputStream(emFile)
//                fo.write(data1)
//                fo.close()
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return emFile
//    }

    fun upload() {
        // MultipartBody.Part is used to send also the actual file name
        Log.e("testqq", "sucess12"+ photolist)

        val description: RequestBody = createPart("@sfrtyuop(roomkey)1a8448ee8ty")
        val Body1 = prepareFilePart("product_image1", photolist[0]);
        val Body2 = prepareFilePart("product_image2", photolist[1]);
        val Body3 = prepareFilePart("product_image3", photolist[2]);
        val Body4 = prepareFilePart("product_image4", photolist[3]);
        val Body5 = prepareFilePart("product_image5", photolist[4]);
        val Body6 = prepareFilePart("product_image6", photolist[5]);
        val Body7 = prepareFilePart("product_image7", photolist[6]);
        val Body8 = prepareFilePart("product_image8", photolist[7]);
        val Body9 = prepareFilePart("product_image9", photolist[8]);
        val Body10 = prepareFilePart("product_image10", photolist[9]);

        val call = AppUtils.service.uploadMultipleFiles(description,Body1,Body2,Body3,Body4,Body5
            ,Body6,Body7,Body8,Body9,Body10)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                val jobject = JSONObject(Gson().toJson(response.body()))
                Log.e("response", "onResponse: $jobject")
                val status = jobject.getString("status")
              //  val string: String = response.body().toString()

                if(status.equals("true")){
                   // product_image =
                    val intent = Intent(this@AddPhotos, ChooseLocation::class.java)
                    intent.putExtra("product_image",jobject.getString("product_image"))
                    startActivity(intent)
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                    Toast.makeText(this@AddPhotos, "Uploaded..!", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this@AddPhotos, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show()
                }

                Log.e("testqq", "sucess"+status)

            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@AddPhotos, t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }



    fun requestReadStorageRuntimePermission() {
        if (ContextCompat.checkSelfPermission(
                this@AddPhotos, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@AddPhotos,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_STORAGE_CODE
            )
        } else {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_STORAGE_CODE -> {
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return
            }
            MY_PERMISSIONS_REQUEST_CAMERA_CODE -> {
                if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                } else {
                }
                return
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

    override fun onFinish() {
        progressDoalog?.setProgress(100)
        progressDoalog?.dismiss()
    }

    override fun onProgressUpdate(percentage: Int) {
        progressDoalog?.setProgress(percentage)
    }

    override fun uploadStart() {
        Log.e("kishan","     kishan")
        progressDoalog?.setMessage("Uploading....")
        progressDoalog?.setProgress(0)
        progressDoalog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDoalog?.show()
    }

    override fun onError() {

    }


    fun compressImage(context: Context, imagePath: String): String? {
        var scaledBitmap: Bitmap?

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        @Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER")
        var bmp: Bitmap? = BitmapFactory.decodeFile(imagePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio = maxWidth / maxHeight

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
            return null
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
            return null
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        var canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bmp, middleX - bmp!!.width / 2, middleY - bmp.height / 2, Paint(FILTER_BITMAP_FLAG))

        bmp.recycle()

        val exif: ExifInterface
        try {
            exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap!!, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val out: FileOutputStream?
        val filepath = getFilename(context)
        try {
            out = FileOutputStream(filepath)
            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filepath
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }

    private fun getFilename(context: Context): String {
        val mediaStorageDir = File("${Environment.getExternalStorageDirectory()}/Android/data/${context.applicationContext.packageName}/Files/Compressed")
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs()
        }

        val mImageName = "IMG_" + System.currentTimeMillis().toString() + ".jpg"
        return mediaStorageDir.getAbsolutePath() + "/" + mImageName
    }



}
