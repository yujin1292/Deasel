package jinjin.juju.young.d_easel
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_result.*
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment
import android.provider.LiveFolders
import android.widget.TextView
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream


class ResultActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance()

    fun saveBitmaptoPNG(bitmap:Bitmap , folder : String , name : String) {
        val ex_stroage = Environment.getExternalStorageDirectory().getAbsolutePath();
        val folder_name = "/"+folder +"/"
        val file_name  = name +".png"
        val string_path  = ex_stroage+folder_name

        var file_path :File

        val sW = bitmap.getWidth()
        val sH = bitmap.getHeight()
        val pixels = IntArray(sW * sH)
        bitmap.getPixels(pixels, 0, sW, 0, 0, sW, sH)
        for (jj in pixels.indices) {
            if (pixels[jj] == Color.TRANSPARENT)
                pixels[jj] = Color.WHITE
        }
        val newbitmap = Bitmap.createBitmap(pixels, 0, sW, sW, sH, Bitmap.Config.ARGB_8888)


        file_path = File(string_path)
        if( !file_path.isDirectory){
            file_path.mkdirs()
        }
        val pathname = string_path + file_name
        val out : FileOutputStream = FileOutputStream(pathname)
        newbitmap?.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.close()
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,pathname.toUri()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val intent = intent

        //intent로 이미지 정보 전달받아서 배경에 넣기
        val id = getIntent().getIntExtra("id",-1)

        //id로 db에서 찾아냄
        var saveImage = realm.where<ImageDB>().equalTo("id",id).findFirst()

        var image = BitmapFactory.decodeByteArray(saveImage?.image, 0, saveImage?.image?.size!!)
        val imageView = findViewById(R.id.resultImage) as ImageView
        imageView.setImageBitmap(image)

        close_btn.setOnClickListener {
            //여기서 main이나 내 작품 액티비티로 가야될 것 같음 이유는..회의 때 회의,,
            finish()
        }
        save_btn.setOnClickListener {
            //갤러리에 이미지 저장하는 코드 넣어줘야됨
            saveBitmaptoPNG(image,"DCIM/My_Painting","DEASEL_${id}")

            var intent: Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
    }


}
