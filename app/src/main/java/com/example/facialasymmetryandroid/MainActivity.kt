package com.example.facialasymmetryandroid

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    private val viewModel = MainViewModel()
    val REQUEST_IMAGE_CAPTURE = 1
    val GET_GALLERY_IMAGE =2

    var m_imageFile: File? = null
    val TAG = "test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                camera_btn.setOnClickListener {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        createImageFile()?.let {
                            val photoURI = FileProvider.getUriForFile(this@MainActivity,
                                "com.example.facialasymmetryandroid", it)
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

                submit_btn.setOnClickListener {


                    // convert Bitmap to File
                    // create a file to write bitmap data
                    val f = File(applicationContext.cacheDir, "tmp")
                    f.createNewFile()

                    // convert bitmap to byte array
                    val bos = ByteArrayOutputStream()
                    val bitmap : Bitmap? = null
                    if (bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos)
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


                    val reqFile: RequestBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), f)
                    val body = MultipartBody.Part.createFormData("file", f.getName(), reqFile)
                    viewModel.postImage(body)
                    viewModel.responseBody.observe(this@MainActivity, androidx.lifecycle.Observer {
                      
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
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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
            /*Glide 사용 시
            val imageBitmap = data?.extras?.get("data") as Bitmap
            Glide.with(this)
                .load(imageBitmap)
                .into(imageView);*/
            m_imageFile?.let {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val source = ImageDecoder.createSource(contentResolver, Uri.fromFile(it))
                    ImageDecoder.decodeBitmap(source)?.let {
                        imageView.setImageBitmap(it)
                    }
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(it))?.let {
                        imageView.setImageBitmap(it)
                    }
                }
            }
        }
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK) {
            val uri : Uri? = data?.data
            Glide.with(this)
                .load(uri)
                .into(imageView);
        }
    }
}

