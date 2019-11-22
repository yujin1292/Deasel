package jinjin.juju.young.d_easel
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_result.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*



class ResultActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance()

    fun saveBitmaptoPNG(bitmap:Bitmap , folder : String , name : String) {
        val format1 = SimpleDateFormat("MM-dd HH:mm:ss")
        val time = Date()
        val time1 = format1.format(time)

        val ex_stroage = Environment.getExternalStorageDirectory().absolutePath
        val folder_name = "/"+folder +"/"
        val file_name  = name +"-"+time1+".png"
        val string_path  = ex_stroage+folder_name

        var file_path :File


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
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://"+pathname)))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val intent = intent

        //intent로 이미지 정보 전달받아서 배경에 넣기
        val id = getIntent().getIntExtra("id",-1)



        //id로 db에서 찾아냄
        var savedDB = realm.where<ImageDB>().equalTo("id",id).findFirst()



        var lines = BitmapFactory.decodeByteArray(savedDB?.lines, 0, savedDB?.lines?.size!!)


        val imageView = findViewById<ImageView>(R.id.resultImage)
        imageView.setImageBitmap(lines)

        close_btn.setOnClickListener {
            //여기서 main이나 내 작품 액티비티로 가야될 것 같음 이유는..회의 때 회의,,
            finish()
        }
        save_btn.setOnClickListener {
            //갤러리에 이미지 저장하는 코드 넣어줘야됨
            saveBitmaptoPNG(lines,"/D-easel/my_painting","DEASEL_${id}")

            var intent: Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
    }


}