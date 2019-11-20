package jinjin.juju.young.d_easel
import android.annotation.SuppressLint
import android.app.ActionBar
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
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.graphics.alpha
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_painting.*
import java.io.ByteArrayOutputStream


class PaintingActivity : AppCompatActivity(), ColorPickerDialogListener {
    val DIALOG_DEFAULT_ID = 100
    var drawLine:DrawLine? = null
    var rect:Rect? = null
    val realm = Realm.getDefaultInstance()
    var backgroundimage:Bitmap? = null
    var background:ImageView? = null
    var canvasImage : ImageDB = ImageDB()

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
        var id = getIntent().getIntExtra("id",-1)

        //id로 db에서 찾아냄
        canvasImage = realm.where<ImageDB>().equalTo("id",id).findFirst()!!


        //이미지를 가져와서 바꿈
        backgroundimage = BitmapFactory.decodeByteArray(canvasImage?.image, 0, canvasImage?.image!!.size)
        background = findViewById<ImageView>(R.id.backgroundView)
        //background.setImageBitmap(backgroundimage)

        delete_btn.setOnClickListener {

        }
        eraser_btn.setOnClickListener {
            drawLine?.line?.setErase()
        }
        pen_setting_btn.setOnClickListener {
            drawLine?.line?.setPen()

            ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(false)
                .setDialogId(DIALOG_DEFAULT_ID)
                .setColor(Color.BLACK)
                .setShowAlphaSlider(true)
                .show(this)

        }

        background_switch.isChecked = true
        background_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked == true) {
                //백그라운드 체크하면 이미지 띄우기
                background?.imageAlpha = 255
            }
            else {
                //백그라운드 해제하면 배경흰색으로 설정하고 투명도를 0으로
                background?.setBackgroundColor(Color.WHITE)
                background?.imageAlpha = 0
            }
        }
        var container = findViewById<FrameLayout>(R.id.Frame)
        result_btn.setOnClickListener {
            //뷰 내용 캡쳐해서 ByteArray로 변환

            //캡처
            container.buildDrawingCache()

            var captureView = container.drawingCache



            val sendBitmap = captureView
            val stream = ByteArrayOutputStream()
            sendBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            //데이터베이스에 이미지 업데이트
            if(id!=-1){
                realm.beginTransaction()
                var updateImage = realm.where<ImageDB>().equalTo("id",id).findFirst()
                updateImage?.lines = byteArray
                realm.commitTransaction()
            }

            //결과 액티비티에 결과 이미지 전달
            var intent:Intent = Intent(this,ResultActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }


        pen_thickness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                drawLine?.line?.setLineWidth(p1.toFloat())
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })



    }

//고쳐지긴했는데 그거 그냥 painting activity에서 제일바깥에 framerlayout을 match parent로 고쳐서 그런건지
//코드 순서바꿔서 그런건진 모르겠다 ㅋㅎㅋㅎ....
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        background?.setImageBitmap(backgroundimage)
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

                var temp = BitmapFactory.decodeByteArray(canvasImage?.lines, 0, canvasImage?.lines!!.size)
                var temp2  = temp.copy(temp.getConfig(), true)
                drawLine = DrawLine(this, rect!! , temp2 )

                //그리기 뷰를 그리기 뷰 레이아웃에 넣기 -- 이렇게 하면 그리기 뷰가 화면에 보여지게 됨.
                Canvas.addView(drawLine)
            }

        }

        super.onWindowFocusChanged(hasFocus)
    }


    override fun onColorSelected(dialogId: Int, color: Int) {
        //Todo
        color_check.setBackgroundColor(color)
        drawLine?.line?.setLineColor(color)
        drawLine?.line?.setLineAlpha(color.alpha)
    }

    /*
    * @brif : Color picker dismiss 호출되는 리스너
    * @param dialogld : 종료된 대화상자 고유아이디
    */
    override fun onDialogDismissed(dialogld: Int) {
        //Todo..
    }
}
