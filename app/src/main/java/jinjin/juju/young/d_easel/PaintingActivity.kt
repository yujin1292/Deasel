package jinjin.juju.young.d_easel
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_painting.*
import java.io.ByteArrayOutputStream


class PaintingActivity : AppCompatActivity() {

    var drawLine:DrawLine? = null
    var rect:Rect? = null
    val realm = Realm.getDefaultInstance()

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        setContentView(R.layout.activity_painting)

        //intent로 이미지 정보 전달받아서 배경에 넣기
        val intent = intent
        val id = getIntent().getIntExtra("id",-1)
        var canvasImage = realm.where<ImageDB>().equalTo("id",id).findFirst()
        var backgroundimage = BitmapFactory.decodeByteArray(canvasImage?.background, 0, canvasImage?.background!!.size)
        val background = findViewById<ImageView>(R.id.backgroundView)
        background.setImageBitmap(backgroundimage)

        delete_btn.setOnClickListener {

        }
        eraser_btn.setOnClickListener {
            drawLine?.line?.setErase()
        }
        pen_setting_btn.setOnClickListener {
            drawLine?.line?.setPen()
            var intent: Intent = Intent(this,PensettingActivity::class.java)
            startActivity(intent)

        }

        background_switch.isChecked = true
        background_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked == true) {
                //백그라운드 체크하면 이미지 띄우기
                background.imageAlpha = 255
            }
            else {
                //백그라운드 해제하면 배경흰색으로 설정하고 투명도를 0으로
                background.setBackgroundColor(Color.WHITE)
                background.imageAlpha = 0
            }
        }
        var container = findViewById<FrameLayout>(R.id.Frame)
        result_btn.setOnClickListener {
            //뷰 내용 캡쳐해서 ByteArray로 변환
            container.buildDrawingCache()
            var captureView = container.drawingCache
            val sendBitmap = captureView
            val stream = ByteArrayOutputStream()
            sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()

            //데이터베이스에 이미지 업데이트
            if(id!=-1){
                realm.beginTransaction()
                var updateImage = realm.where<ImageDB>().equalTo("id",id).findFirst()
                updateImage?.image = byteArray
                realm.commitTransaction()
            }

            //결과 액티비티에 결과 이미지 전달
            var intent:Intent = Intent(this,ResultActivity::class.java)
            intent.putExtra("image", byteArray)
            startActivity(intent)
        }

    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus && drawLine == null) {
            //그리기 뷰가 보여질(나타날) 레이아웃 찾기..
            val Canvas = findViewById<FrameLayout>(R.id.Canvas)
            if (Canvas != null)
            //그리기 뷰가 보여질 레이아웃이 있으면...
            {
                //그리기 뷰 레이아웃의 넓이와 높이를 찾아서 Rect 변수 생성.
                rect = Rect(
                    0, 0,
                    Canvas.measuredWidth, Canvas.measuredHeight
                )
                //그리기 뷰 초기화..
                drawLine = DrawLine(this, rect!!)
                //그리기 뷰를 그리기 뷰 레이아웃에 넣기 -- 이렇게 하면 그리기 뷰가 화면에 보여지게 됨.
                Canvas.addView(drawLine)
            }

        }

        super.onWindowFocusChanged(hasFocus)
    }
}
