package jinjin.juju.young.d_easel
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.graphics.alpha
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_painting.*
import java.io.ByteArrayOutputStream
import android.net.Uri
import android.os.Environment
import android.widget.*
import androidx.core.content.FileProvider
import org.jetbrains.anko.alert
import org.jetbrains.anko.longToast
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.io.IOException


class PaintingActivity : BaseActivity(), ColorPickerDialogListener {

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


        actList.add(this)


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
        zoomView.maxZoom = 5f // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.

        val zoomFrame = findViewById<View>(R.id.Frame) as FrameLayout
        zoomFrame.addView(zoomView)


        //intent로 이미지 정보 전달받아서 배경에 넣기
        val intent = intent
        var id = getIntent().getIntExtra("id",-1)

        //id로 db에서 찾아냄
        canvasImage = realm.where<ImageDB>().equalTo("id",id).findFirst()!!

        color_check.scaleType = ImageView.ScaleType.CENTER


        //이미지를 가져와서 바꿈
        backgroundimage = BitmapFactory.decodeByteArray(canvasImage.background, 0, canvasImage.background.size)
        background = findViewById<ImageView>(R.id.backgroundView)


        eraser_btn.setOnClickListener {
            drawLine?.line?.setErase()
        }
        pen_setting_btn.setOnClickListener {
            drawLine?.line?.setPen()
        }
        brush_setting_btn.setOnClickListener {
            drawLine?.line?.setBrush()
        }
        color_check.setOnClickListener{

            ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(true)
                .setDialogId(DIALOG_DEFAULT_ID)
                .setColor(drawLine?.line?.color!!)
                .setShowAlphaSlider(false)
                .show(this)


        }
        spoid_btn.setOnClickListener {
            drawLine?.isSpoid = true
        }
        var container = findViewById<FrameLayout>(R.id.Frame)
        result_btn.setOnClickListener {

            //뷰 내용 캡쳐해서 ByteArray로 변환

            //캡처 준비
            zoomView.zoomTo(1.0f,0f,0f)
            container.buildDrawingCache()

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



            alert("갤러리에도 저장하시겠습니까?") {

                positiveButton("네"){

                    saveBitmaptoPNG(sendBitmap,"/D-easel/my_painting","DEASEL_${id}")
                    longToast("갤러리에도 저장했습니다!")
                }
                negativeButton("아니요"){

                    longToast("저장했습니다!")
                }
            }.show()


        }
        pen_thickness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                drawLine?.line?.setLineWidth(p1.toFloat())

                when (p1){
                    1->color_check.setImageDrawable(getDrawable(R.drawable.one))
                    2->color_check.setImageDrawable(getDrawable(R.drawable.two))
                    3->color_check.setImageDrawable(getDrawable(R.drawable.three))
                    4->color_check.setImageDrawable(getDrawable(R.drawable.four))
                    5->color_check.setImageDrawable(getDrawable(R.drawable.five))
                    6->color_check.setImageDrawable(getDrawable(R.drawable.six))
                    7->color_check.setImageDrawable(getDrawable(R.drawable.seven))
                    8->color_check.setImageDrawable(getDrawable(R.drawable.eight))
                    9->color_check.setImageDrawable(getDrawable(R.drawable.nine))

                    10->color_check.setImageDrawable(getDrawable(R.drawable.ten))
                    11->color_check.setImageDrawable(getDrawable(R.drawable.eleven))
                    12->color_check.setImageDrawable(getDrawable(R.drawable.twleve))
                    13->color_check.setImageDrawable(getDrawable(R.drawable.thirteen))
                    14->color_check.setImageDrawable(getDrawable(R.drawable.fourteen))
                    15->color_check.setImageDrawable(getDrawable(R.drawable.fifteen))
                    16->color_check.setImageDrawable(getDrawable(R.drawable.sixteen))
                    17->color_check.setImageDrawable(getDrawable(R.drawable.seventeen))
                    18->color_check.setImageDrawable(getDrawable(R.drawable.eighteen))
                    19->color_check.setImageDrawable(getDrawable(R.drawable.nineteen))

                    21->color_check.setImageDrawable(getDrawable(R.drawable.twentyone))
                    22->color_check.setImageDrawable(getDrawable(R.drawable.twentytwo))
                    23->color_check.setImageDrawable(getDrawable(R.drawable.twentytrhee))
                    24->color_check.setImageDrawable(getDrawable(R.drawable.twentyfour))
                    25->color_check.setImageDrawable(getDrawable(R.drawable.twentyfive))
                    26->color_check.setImageDrawable(getDrawable(R.drawable.twentysix))
                    27->color_check.setImageDrawable(getDrawable(R.drawable.twentyseven))
                    28->color_check.setImageDrawable(getDrawable(R.drawable.twentyeight))
                    29->color_check.setImageDrawable(getDrawable(R.drawable.twentynine))

                    30->color_check.setImageDrawable(getDrawable(R.drawable.thirty))
                    31 ->color_check.setImageDrawable(getDrawable(R.drawable.thirtyone))
                    32->color_check.setImageDrawable(getDrawable(R.drawable.thirtytwo))
                    33->color_check.setImageDrawable(getDrawable(R.drawable.thirtythree))
                    34->color_check.setImageDrawable(getDrawable(R.drawable.thirtyfour))
                    35->color_check.setImageDrawable(getDrawable(R.drawable.thryfive))
                    36->color_check.setImageDrawable(getDrawable(R.drawable.thirtysix))
                    37->color_check.setImageDrawable(getDrawable(R.drawable.thirtyseven))
                    38->color_check.setImageDrawable(getDrawable(R.drawable.thirtyeight))
                    39->color_check.setImageDrawable(getDrawable(R.drawable.thirtynine))

                    40->color_check.setImageDrawable(getDrawable(R.drawable.fourty))
                    41->color_check.setImageDrawable(getDrawable(R.drawable.fourtyone))
                    42->color_check.setImageDrawable(getDrawable(R.drawable.fourty))
                    43->color_check.setImageDrawable(getDrawable(R.drawable.fourtythree))
                    44->color_check.setImageDrawable(getDrawable(R.drawable.fourtyfour))
                    45->color_check.setImageDrawable(getDrawable(R.drawable.fourtyfive))
                    46->color_check.setImageDrawable(getDrawable(R.drawable.fourtysix))
                    47->color_check.setImageDrawable(getDrawable(R.drawable.fourtyseven))
                    48->color_check.setImageDrawable(getDrawable(R.drawable.fourtyeight))
                    49->color_check.setImageDrawable(getDrawable(R.drawable.fourtynine))
                    50->color_check.setImageDrawable(getDrawable(R.drawable.fifty))
                }



            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        insta_btn.setOnClickListener {

            //뷰 내용 캡쳐해서 ByteArray로 변환

            //캡처 준비
            zoomView.zoomTo(1.0f,0f,0f)
            container.buildDrawingCache()

            //캡처
            var captureView = container.drawingCache
            //바이트 어레이로 변환
            val stream = ByteArrayOutputStream()
            captureView.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()


            //데이터베이스에 이미지 업데이트
            if(id!=-1){
                realm.beginTransaction()
                var updateImage = realm.where<ImageDB>().equalTo("id",id).findFirst()
                updateImage?.lines = byteArray
                updateImage?.image = byteArray
                realm.commitTransaction()
            }

            // 흰바탕 깔기
            val sendBitmap = Bitmap.createBitmap(captureView.width, captureView.height, captureView.config)
            val canvas = Canvas(sendBitmap)
            canvas.drawColor(Color.WHITE)
            canvas.drawBitmap(captureView, 0f, 0f ,null)

            var bmpUri = getLocalBitmapUri(sendBitmap)

            if (bmpUri != null) {
                goInsta(bmpUri)
            }

        }
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

                drawLine = DrawLine(this, rect!! , temp2,color_check )

                //그리기 뷰를 그리기 뷰 레이아웃에 넣기 -- 이렇게 하면 그리기 뷰가 화면에 보여지게 됨.
                Canvas.addView(drawLine)
            }

        }

        super.onWindowFocusChanged(hasFocus)
    }
    override fun onColorSelected(dialogId: Int, color: Int) {
        //Todo
        drawLine?.line?.setLineColor(color)
        drawLine?.line?.setLineAlpha(color.alpha)
    }
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
    override fun onDialogDismissed(dialogld: Int) {
        //Todo..
    }
    override fun onDestroy() {

        Log.d("List","distroy"+ actList.toString())
        super.onDestroy()
        actList.remove(this)
        realm.close() //인스턴스 해제
    }
    override fun onBackPressed() {

        alert(" 정말 나가시겠습니까? ") {

            positiveButton("네"){
                super.onBackPressed()
            }
            negativeButton("아니요"){

            }
        }.show()

    }
    private fun goInsta(uri: Uri){
        var intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null)
        {

            var shareIntent = Intent()
            shareIntent.setAction(Intent.ACTION_SEND)
            shareIntent.setPackage("com.instagram.android")
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.setType("image/jpeg")

            startActivity(shareIntent)
        }
        else
        {
            // bring user to the market to download the app.
            // or let them choose an app?
            intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setData(Uri.parse("market://details?id="+"com.instagram.android"))
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
