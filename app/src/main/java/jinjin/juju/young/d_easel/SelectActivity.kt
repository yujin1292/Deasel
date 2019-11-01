package jinjin.juju.young.d_easel

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class SelectActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_CAMERA = 100
    private val MY_PERMISSIONS_REQUEST_GALLERY = 200
    private val REQ_CODE_CAMERA_IMAGE = 123
    private val REQ_CODE_SELECT_IMAGE = 456

    private var original: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        var permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if (permssionCheck != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_LONG).show()

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA
                )
                Toast.makeText(this, "카메라 권한이 필요합니다..", Toast.LENGTH_LONG).show()

            }
        }

        permssionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permssionCheck != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "권한 승인이 필요합니다", Toast.LENGTH_LONG).show()

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(this, "사용을 위해 읽기 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_GALLERY
                )
                Toast.makeText(this, "사용을 위해 읽기 권한이 필요합니다.", Toast.LENGTH_LONG).show()

            }
        }




    }
    //버튼 리스너
    fun TakePicClicked(view: View) {
        //카메라 열고 파일 처리.
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQ_CODE_CAMERA_IMAGE)

    }

    fun GetPicClicked(view: View) {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE)

    }


    fun goPreview(string: String) {


        val intent2 = Intent(this, PreviewActivity::class.java)
        intent2.putExtra("where", string)
        /*

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        intent2.putExtra("original",bytes);*/

        val stream2 = ByteArrayOutputStream()
        original?.compress(Bitmap.CompressFormat.JPEG, 100, stream2)
        val bytes2 = stream2.toByteArray()
        intent2.putExtra("image", bytes2)

        startActivity(intent2)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQ_CODE_CAMERA_IMAGE && resultCode == Activity.RESULT_OK) {

            try {
                val extras = data!!.extras
                val imageBitmap = extras!!.get("data") as Bitmap

                original = imageBitmap

                if (original!= null) {

                    goPreview("From camera")

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        if (requestCode == REQ_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            try {

                val path = getImagePathFromURI(data!!.data)
                val options = BitmapFactory.Options()
                options.inSampleSize = 5
                original = BitmapFactory.decodeFile(path, options)

                if (original != null) { // 가져온 이미지가 null 이 아니면 엣지 디텍팅
                    goPreview("From Gallery")
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun getImagePathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor == null) {
            return contentUri.path
        } else {
            val idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val imgPath = cursor.getString(idx)
            cursor.close()
            return imgPath
        }
    }



}
