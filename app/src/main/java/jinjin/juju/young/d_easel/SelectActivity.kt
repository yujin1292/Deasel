package jinjin.juju.young.d_easel

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SelectActivity : AppCompatActivity() {

    private val REQUEST_SELECT_IMAGE = 456
    private val REQUEST_TAKE_PHOTO = 123

    private var mCurrentPhotoPath: String = ""
    private var original: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

    }



    //버튼 리스너
    fun TakePicClicked(view: View) {
        //카메라 열고 파일 처리.
        dispatchTakePictureIntent()
    }
    fun GetPicClicked(view: View) {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQUEST_SELECT_IMAGE)

    }



    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    this,
                    "jinjin.juju.young.d_easel.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }



    fun goPreview(string: String) {


        val intent2 = Intent(this, PreviewActivity::class.java)
        intent2.putExtra("where", string)

        if(string.equals("From Camera")){

            val stream2 = ByteArrayOutputStream()
            original?.compress(Bitmap.CompressFormat.JPEG, 60, stream2)
            val bytes2 = stream2.toByteArray()
            intent2.putExtra("image", bytes2)

        }
        else{

            val stream2 = ByteArrayOutputStream()
            original?.compress(Bitmap.CompressFormat.JPEG, 60, stream2)
            val bytes2 = stream2.toByteArray()
            intent2.putExtra("image", bytes2)
        }

        startActivity(intent2)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode === Activity.RESULT_OK){
            val file = File(mCurrentPhotoPath)
            val temp = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
            // original = temp

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "DEASEL_" + timeStamp + "_"

            val imageSaveUri = MediaStore.Images.Media.insertImage(contentResolver,temp,imageFileName,"saved from d-easel")
            val uri = Uri.parse(imageSaveUri)
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri))

            val path = getImagePathFromURI(uri)
            val options = BitmapFactory.Options()
            options.inSampleSize = 2
            original = BitmapFactory.decodeFile(path, options)



            if (original!= null) {

                goPreview("From Camera")

            }
        }

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            try {

                val path = getImagePathFromURI(data!!.data)
                val options = BitmapFactory.Options()
                options.inSampleSize = 2
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
