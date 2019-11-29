package jinjin.juju.young.d_easel

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.view.Window
import androidx.core.content.FileProvider
import io.realm.Realm
import io.realm.kotlin.where
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SharePopUpActivity : Activity() {

    var id = 0

    val realm = Realm.getDefaultInstance()
    var image : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_share_pop_up)

        val intent = intent
        id = intent.getIntExtra("id" , 0)


        //id로 db에서 찾아냄
        val saveImageDB = realm.where<ImageDB>().equalTo("id",id).findFirst()!!

        image  = BitmapFactory.decodeByteArray(saveImageDB.lines, 0,saveImageDB.lines.size)

    }

    fun instaClick(v: View) {

        // 흰바탕 깔기
        val sendBitmap = Bitmap.createBitmap(image!!.width, image!!.height, image!!.config)
        val canvas = Canvas(sendBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(image, 0f, 0f ,null)

        var bmpUri = getLocalBitmapUri(sendBitmap)

        if (bmpUri != null) {
            goInsta(bmpUri)
        }

        //액티비티(팝업) 닫기
        finish()
    }

    fun gallaryClick(v: View) {

        // 흰바탕 깔기
        val sendBitmap = Bitmap.createBitmap(image!!.width, image!!.height, image!!.config)
        val canvas = Canvas(sendBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(image, 0f, 0f ,null)

        saveBitmaptoPNG(sendBitmap,"/D-easel/my_painting","DEASEL_${id}")
        toast("갤러리에 저장되었습니다!")
        finish()
    }

    fun closeClick(v:View){
        finish()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //바깥레이어 클릭시 안닫히게
        return event.action != MotionEvent.ACTION_OUTSIDE
    }

    /* @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }*/
    fun saveBitmaptoPNG(bitmap:Bitmap , folder : String , name : String) : String  {
        val format1 = SimpleDateFormat("MM-dd HH:mm:ss")
        val time = Date()
        val time1 = format1.format(time)

        val ex_stroage = Environment.getExternalStorageDirectory().absolutePath
        val folder_name = "/"+folder +"/"
        val file_name  = name +"-"+time1+".png"
        val string_path  = ex_stroage+folder_name

        var file_path : File


        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f ,null)



        file_path = File(string_path)
        if( !file_path.isDirectory){
            file_path.mkdirs()
        }
        val pathname = string_path + file_name
        val out : FileOutputStream = FileOutputStream(pathname)
        newBitmap?.compress(Bitmap.CompressFormat.PNG, 100, out) // out 위치에 PNG로 저장
        out.close()
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+pathname)))

        return pathname

    }
    private fun goInsta(uri: Uri){
        var intent = packageManager.getLaunchIntentForPackage("com.instagram.android")
        if (intent != null)
        {

            var shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.setPackage("com.instagram.android")
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type = "image/jpeg"

            startActivity(shareIntent)
        }
        else
        {
            // bring user to the market to download the app.
            // or let them choose an app?
            intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("market://details?id="+"com.instagram.android")
            startActivity(intent)
        }
    }

    fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(this,"jinjin.juju.young.d_easel.fileprovider",file)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bmpUri
    }

}
