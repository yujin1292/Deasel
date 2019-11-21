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
import java.lang.Thread.sleep


class PaintingActivity : AppCompatActivity(), ColorPickerDialogListener {
    val DIALOG_DEFAULT_ID = 100
    var drawLine:DrawLine? = null
    var rect:Rect? = null
    val realm = Realm.getDefaultInstance()
    var backgroundimage:Bitmap? = null
    var background:ImageView? = null
    var canvasImage : ImageDB = ImageDB()
    var stop : Boolean = false

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        setContentView(R.layout.activity_painting)



        //zoom view
        val v = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.zoom_item,
            null,
            false
        )
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val zoomView = ZoomView(this)
        zoomView.addView(v)
        zoomView.layoutParams = layoutParams
        zoomView.maxZoom = 4f // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.

        val zoomFrame = findViewById<View>(R.id.Frame) as FrameLayout
        zoomFrame.addView(zoomView)

        //zoom veiw



        //intent로 이미지 정보 전달받아서 배경에 넣기
        val intent = intent
        var id = getIntent().getIntExtra("id",-1)

        //id로 db에서 찾아냄
        canvasImage = realm.where<ImageDB>().equalTo("id",id).findFirst()!!


        //이미지를 가져와서 바꿈
        backgroundimage = BitmapFactory.decodeByteArray(canvasImage.background, 0, canvasImage.background.size)
        background = findViewById<ImageView>(R.id.backgroundView)
        //backgroundView.setImageBitmap(backgroundimage)

        eraser_btn.setOnClickListener {
            drawLine?.line?.setErase()
        }
        pen_setting_btn.setOnClickListener {
            drawLine?.line?.setPen()
        }

        pen_setting_btn.setOnLongClickListener{

            ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(false)
                .setDialogId(DIALOG_DEFAULT_ID)
                .setColor(Color.BLACK)
                .setShowAlphaSlider(true)
                .show(this)


            true
        }


        var container = findViewById<FrameLayout>(R.id.Frame)
        result_btn.setOnClickListener {
            //뷰 내용 캡쳐해서 ByteArray로 변환

            //캡처 준비
            zoomView.zoomTo(1.0f,0f,0f)
            container.buildDrawingCache()
           // sleep(3)
            //캡처
            var captureView = container.drawingCache
            //바이트 어레이로 변환
            val sendBitmap = captureView
            val stream = ByteArrayOutputStream()
            sendBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()


            //데이터베이스에 이미지 업데이트
            if(id!=-1){
                realm.beginTransaction()
                var updateImage = realm.where<ImageDB>().equalTo("id",id).findFirst()
                updateImage?.lines = byteArray
                updateImage?.image = byteArray
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

                //지금까지 그린걸 추가해서 그리기뷰 생성해야 지울수있음
                var temp = BitmapFactory.decodeByteArray(canvasImage.lines, 0, canvasImage.lines.size)

                var temp2  = temp.copy(temp.config, true)

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
