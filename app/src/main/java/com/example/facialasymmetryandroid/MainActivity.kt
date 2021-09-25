
package com.example.facialasymmetryandroid

import android.content.ContentValues
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
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    val viewModel = MainViewModel()

    val REQUEST_IMAGE_CAPTURE = 1
    val GET_GALLERY_IMAGE = 2

    var bitmap: Bitmap? = null
    var m_imageFile: File? = null
    val TAG = "tag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //타입 제목 상단 TextView에 입력
        var intent = intent
        val type = intent.getStringExtra("type")
        title_tv.text = type

        //PermissionListener 구현
        var permissionlistener: PermissionListener = object : PermissionListener {
            //Permission 승인될 시
            override fun onPermissionGranted() {

                //사진촬영 OnClickListener 구현
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

                //불러오기 OnClickListener 구현
                gallery_btn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                    startActivityForResult(intent, GET_GALLERY_IMAGE)
                }

                //사진 초기화 OnClickListener 구현
                reset_btn.setOnClickListener {
                    imageView.setImageResource(0)
                    bitmap = null
                }
                //검사 시작 OnClickListener 구현
                //todo loadingbar 구현
                submit_btn.setOnClickListener {

                    val start   = System.currentTimeMillis();

                    //이미지를 입력하지 않았다면
                    if (bitmap == null) {
                        Toast.makeText(this@MainActivity, "이미지를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        imageView.setImageResource(0)

                        val f = File(applicationContext.cacheDir, "tmp")
                        f.createNewFile()

                        val bos = ByteArrayOutputStream()
                        if (bitmap != null) {
                            Log.d(TAG, "onPermissionGranted: 파일 성공 저장")
                            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                        }
                        val bitmapdata = bos.toByteArray()


                        try {
                            var fos = FileOutputStream(f)
                            fos!!.write(bitmapdata)
                            fos!!.flush()
                            fos!!.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }

                        //MultipartBody에 현재 bitmap 담기
                        val reqFile: RequestBody =
                            RequestBody.create(MediaType.parse("multipart/form-data"), f)
                        val body = MultipartBody.Part.createFormData("file", f.getName(), reqFile)

                        //이미지 전송후 콜백받는 부분
                        //todo viewmodel postImage()함수 호출 ,  body 랑 type전하기
                        viewModel.postImage(type!!,body)
                        viewModel.imageUrlLiveData.observe(this@MainActivity,{
                            Glide.with(this@MainActivity)
                                .load(it)
                                .into(imageView)
                            val end = System.currentTimeMillis()
                            Log.d(TAG, "onPermissionGranted: time : ${(end-start)/1000}")
                        })

                        viewModel.loadingLiveData.observe(this@MainActivity,{
                            if (it) {
                                progressBar.bringToFront()
                                progressBar.visibility = View.VISIBLE
                            } else {
                                progressBar.visibility = View.GONE
                            }
                        })

                        viewModel.returnString.observe(this@MainActivity,{
                           /* val intent = Intent(this@MainActivity,ResultActivity::class.java)
                            Log.d(TAG, "onPermissionGranted: ${it.imageBytes}")
                            intent.putExtra("imageBytes",it.imageBytes)
                            intent.putExtra("message",it.message)
                            startActivity(intent)*/
                        })

                    }
                }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                TODO("Not yet implemented")
            }

        }

        /*
        * TedPermission 객체 생성
        * */
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

        }
    }
}

