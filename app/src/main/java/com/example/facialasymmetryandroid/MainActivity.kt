package com.example.facialasymmetryandroid

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.facialasymmetryandroid.model.ReturnString
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.router.cointts.repository.ServerRecieverService
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    val GET_GALLERY_IMAGE = 2

    var bitmap: Bitmap? = null
    var m_imageFile: File? = null
    val TAG = "tag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent = intent
        title_tv.text = intent.getStringExtra("type")
        var permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                camera_btn.setOnClickListener {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        createImageFile()?.let {
                            val photoURI = FileProvider.getUriForFile(
                                this@MainActivity,
                                "com.example.facialasymmetryandroid", it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                            m_imageFile = it
                        }
                    }
                }

                gallery_btn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                    startActivityForResult(intent, GET_GALLERY_IMAGE)
                }

                reset_btn.setOnClickListener {
                    imageView.setImageResource(0)
                    bitmap =null
                }

                submit_btn.setOnClickListener {

                    imageView.setImageResource(0)
                    //todo 파일 만드는 부분 코드 리팩토링하기

                    // convert Bitmap to File
                    // create a file to write bitmap data
                    val f = File(applicationContext.cacheDir, "tmp")
                    f.createNewFile()
                    // convert bitmap to byte array
                    val bos = ByteArrayOutputStream()
                    if (bitmap != null) {
                        Log.d(TAG, "onPermissionGranted: 파일 성공 저장")
                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                    }
                    val bitmapdata = bos.toByteArray()

                    // write the bytes in file
                    var fos: FileOutputStream? = null
                    try {
                        fos = FileOutputStream(f)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                    try {
                        fos!!.write(bitmapdata)
                        fos!!.flush()
                        fos!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }


                    // Call API
                    val service: ServerRecieverService = Retrofit
                        .Builder()
                        .baseUrl("http://220.69.208.242:80")
                        .build()
                        .create(ServerRecieverService::class.java)

                    //MultipartBody에 현재 bitmap 담기
                    val reqFile: RequestBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), f)
                    val body = MultipartBody.Part.createFormData("file", f.getName(), reqFile)

                    //이미지 전송후 콜백받는 부분
                    service.postImage(body).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            //응답오는 과정에서 에러 발생 시
                            if (!response.isSuccessful) {
                                Log.d(TAG, "onResponse: 에러 " + response.code())
                                return
                            }

                            //파이썬 코드로부터 응답받는 부분

                            try {
                                //텍스트와 이미지 가져오기
                                Gson().fromJson(response.body()!!.string(),ReturnString::class.java).also {
                                    val encodeByte = android.util.Base64.decode(it.imageBytes, android.util.Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
                                    imageView.setImageBitmap(bitmap)
                                }
                            } catch (e: IOException) {
                                Log.d(TAG, "onResponse: 텍스트와 이미지 가져오는 부분에서 에러")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d(TAG, "onFailure: 연결 실패")
                        }
                    })
                }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                TODO("Not yet implemented")
            }

        }
        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setRationaleMessage("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Permisson: " + permissions[0] + " was " + grantResults[0])
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "PHOTO_${timeStamp}.jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, imageFileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            m_imageFile?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val source = ImageDecoder.createSource(contentResolver, Uri.fromFile(it))
                    ImageDecoder.decodeBitmap(source)?.let {
                        imageView.setImageBitmap(it)
                        bitmap = it
                    }
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(it))?.let {
                        imageView.setImageBitmap(it)
                        bitmap = it
                    }
                }
            }
        }
        //todo 이미지회전 해결하기
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK) {
            val localBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
            bitmap = localBitmap
            imageView.setImageBitmap(bitmap)
            //Glide.with(this).asBitmap().load(bitmap).into(imageView)

        }
    }
}

