package jinjin.juju.young.d_easel
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val intent = intent
        val arr = getIntent().getByteArrayExtra("image")
        var image = BitmapFactory.decodeByteArray(arr, 0, arr.size)
        val imageView = findViewById(R.id.resultImage) as ImageView
        imageView.setImageBitmap(image)

        close_btn.setOnClickListener {
            //여기서 main이나 내 작품 액티비티로 가야될 것 같음 이유는..회의 때 회의,,
            finish()
        }
        save_btn.setOnClickListener {
            //갤러리에 이미지 저장하는 코드 넣어줘야됨
            var intent: Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
