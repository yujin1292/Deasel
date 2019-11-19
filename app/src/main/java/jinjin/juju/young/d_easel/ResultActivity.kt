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

class ResultActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance()

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

            // 저장명 설정
            val imageFileName = "DEASEL_${id}_saved"
            // 이미지 저장
            val imageSaveUri = MediaStore.Images.Media.insertImage(contentResolver,image,imageFileName,"saved from d-easel")
            val uri = Uri.parse(imageSaveUri)
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri))



            var intent: Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
    }
}
