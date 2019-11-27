package jinjin.juju.young.d_easel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import android.media.ExifInterface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.facebook.stetho.Stetho
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.RealmResults
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class MainActivity : BaseActivity() {

    private var lastTimeBackPressed: Long = 0

    var music:Boolean = false


    var tempuri : Uri? = null
    var realm : Realm? = null


    private val REQUEST_SELECT_IMAGE = 456
    private val REQUEST_TAKE_PHOTO = 123

    private var mCurrentPhotoPath: String = ""
    private var original: Bitmap? = null

    var logoview: ImageView? = null
    private var mHandler: Handler? = null
    private var logonum: Int = 0



    val id = 100

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()


        val provider = RealmInspectorModulesProvider.builder(this)
            .withDeleteIfMigrationNeeded(true)
            .build()

         Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(provider)
                .build())


        bgm = MediaPlayer.create(this, R.raw.bgm)
        //bgm.prepare();
        bgm!!.isLooping = true

        var musicdb =  realm?.where<MusicDB>()?.equalTo("id",id)?.findFirst()

        if(musicdb == null){

            var newMusicDB = MusicDB()
            newMusicDB.init()

            realm?.beginTransaction()

            realm?.copyToRealm(newMusicDB)

            realm?.commitTransaction()



        }
        musicdb =  realm?.where<MusicDB>()?.equalTo("id",id)?.findFirst()
       // music = musicdb?.switch!!




        if(musicdb?.is_on.equals("on")) {
            bgm!!.start()
            music_btn.setBackgroundResource(R.drawable.music_on)

        }

        else{
            music_btn.setBackgroundResource(R.drawable.music_off)

        }


      //  textView.setText(music.toString())

        //logo thread
        @SuppressLint("HandlerLeak")
        mHandler = object:Handler(){
            override fun handleMessage(msg: Message?) {

                logoview = findViewById(R.id.logo_view)

                if(logonum==0){
                    logoview?.setImageDrawable(getDrawable(R.drawable.deasel0))
                }
                else if(logonum==1){
                    logoview?.setImageDrawable(getDrawable(R.drawable.deasel1))
                }
                else if(logonum==2){
                    logoview?.setImageDrawable(getDrawable(R.drawable.deasel2))
                }
                else if(logonum==3){
                    logoview?.setImageDrawable(getDrawable(R.drawable.deasel3))
                }
                else if(logonum==4){
                    logoview?.setImageDrawable(getDrawable(R.drawable.deasel4))
                }
                else if(logonum==5){
                    logoview?.setImageDrawable(getDrawable(R.drawable.deasel5))
                }
            }
        }

        thread(start = true){

            var forward :Boolean  = true

            while(true){
                Thread.sleep(150)
                mHandler?.sendEmptyMessage(logonum)
                if(forward){
                    logonum++
                    if(logonum>5){
                        Thread.sleep(100)
                        forward = false
                    }
                }
                else{
                    logonum--
                    if(logonum<0){ //logonum == -1 되면
                        Thread.sleep(100)
                        forward = true
                    }
                }
            }
        }




        // Intro thread
        val t = Thread(Runnable {
            //  Initialize SharedPreferences
            val getPrefs = PreferenceManager
                .getDefaultSharedPreferences(baseContext)

            //  Create a new boolean and preference and set it to true
            val isFirstStart = getPrefs.getBoolean("firstStart", true)

            //  If the activity has never started before...
            if (isFirstStart) {

                //  Launch app intro
                val i = Intent(this@MainActivity, MyIntro::class.java)

                runOnUiThread { startActivity(i) }

                //  Make a new preferences editor
                val e = getPrefs.edit()

                //  Edit preference to make it false because we don't want this to run again
                e.putBoolean("firstStart", false)

                //  Apply changes
                e.apply()
            }
        })

        // Start the thread
        t.start()



        var realmResults = realm?.where<ImageDB>()?.findAll()
       /* if(realmResults!!.isEmpty()){

            //sample 1
            realm?.beginTransaction()

            val newImage = realm?.createObject(ImageDB::class.java, nextId())
            val sendBitmap = BitmapFactory.decodeResource(resources,R.drawable.sample1)
            val stream = ByteArrayOutputStream()
            sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()

            newImage?.image = byteArray


            newImage?.background = byteArray

            //lines 투명 값
            val stream11 = ByteArrayOutputStream()
            val temp = Bitmap.createBitmap(
                sendBitmap.getWidth(),
                sendBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
            )
            temp.compress(Bitmap.CompressFormat.PNG, 100, stream11)
            val byteline = stream11.toByteArray()
            newImage?.lines = byteline

            realm?.commitTransaction()


            // sample 2
            realm?.beginTransaction()
            val newImage2 = realm?.createObject(ImageDB::class.java, nextId())
            val sendBitmap2 = BitmapFactory.decodeResource(resources,R.drawable.sample2)
            val stream2 = ByteArrayOutputStream()
            sendBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2)
            val byteArray2 = stream2.toByteArray()
            newImage2?.image = byteArray2
            newImage2?.background=byteArray2

            //lines 투명 값
            val stream22 = ByteArrayOutputStream()
            val temp2 = Bitmap.createBitmap(
                sendBitmap.getWidth(),
                sendBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
            )
            temp2.compress(Bitmap.CompressFormat.PNG, 100, stream22)
            val byteline2 = stream11.toByteArray()
            newImage?.lines = byteline2

            realm?.commitTransaction()


            // sample 3

            realm?.beginTransaction()
            val newImage3 = realm?.createObject(ImageDB::class.java, nextId())
            val sendBitmap3 = BitmapFactory.decodeResource(resources,R.drawable.sample3)
            val stream3 = ByteArrayOutputStream()
            sendBitmap3.compress(Bitmap.CompressFormat.PNG, 100, stream2)
            val byteArray3 = stream3.toByteArray()
            newImage3?.image = byteArray3
            newImage3?.background=byteArray3

            //lines 투명 값
            val stream33 = ByteArrayOutputStream()
            val temp3 = Bitmap.createBitmap(
                sendBitmap.getWidth(),
                sendBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
            )
            temp3.compress(Bitmap.CompressFormat.PNG, 100, stream33)
            val byteline3 = stream11.toByteArray()
            newImage3?.lines = byteline3

            realm?.commitTransaction()


            realm?.beginTransaction()
            val newImage4 = realm?.createObject(ImageDB::class.java, nextId())
            val sendBitmap4 = BitmapFactory.decodeResource(resources,R.drawable.sample4)
            val stream4 = ByteArrayOutputStream()
            sendBitmap4.compress(Bitmap.CompressFormat.JPEG, 100, stream4)
            val byteArray4 = stream3.toByteArray()
            newImage4?.image = byteArray4
            newImage4?.background=byteArray4

            //lines 투명 값
            val stream44 = ByteArrayOutputStream()
            val temp4 = Bitmap.createBitmap(
                sendBitmap.getWidth(),
                sendBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
            )
            temp4.compress(Bitmap.CompressFormat.PNG, 100, stream44)
            val byteline4 = stream11.toByteArray()
            newImage4?.lines = byteline4

            realm?.commitTransaction()


        }*/


        //마스터 피스 엑티비티 호출
        go_master.setOnClickListener { view ->
            val intent: Intent = Intent(this, MasterpieceActivity::class.java)
            startActivity(intent)
        }
        go_master.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    go_master.setBackgroundResource(R.drawable.blue_ppu)
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    go_master.setBackgroundResource(R.drawable.blue)
                    go_master.performClick()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
        get_pic.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    get_pic.setBackgroundResource(R.drawable.yellow_ppu)
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    get_pic.setBackgroundResource(R.drawable.yellow)
                    get_pic.performClick()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
        get_pic.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = Images.Media.CONTENT_TYPE
            intent.data = Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(intent, REQUEST_SELECT_IMAGE)
        }
        take_pic.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    take_pic.setBackgroundResource(R.drawable.red_ppu)
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    take_pic.setBackgroundResource(R.drawable.red)
                    take_pic.performClick()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
        take_pic.setOnClickListener {

            dispatchTakePictureIntent()
        }


        music_btn.setOnClickListener{
            if(musicdb?.is_on.equals("off")){

                bgm!!.start()
                music = !music


                realm?.beginTransaction()

                //textView.setText( updateMusic?.switch.toString())

                musicdb?.is_on = "on"


                realm?.commitTransaction()
                music_btn.setBackgroundResource(R.drawable.music_on)
                toast("BGM ON")

            }

            else{

                bgm!!.pause()
                music = !music


                realm?.beginTransaction()

                musicdb?.is_on = "off"

                realm?.commitTransaction()

                music_btn.setBackgroundResource(R.drawable.music_off)
                toast("BGM OFF")

            }
        }

    }

    fun nextId():Int
    {
        val maxid = realm?.where<ImageDB>()?.max("id")
        if(maxid!=null){
            return maxid.toInt()+1
        }
        return 0
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

    fun goPreview() {


        val intent2 = Intent(this, PreviewActivity::class.java)


        val stream2 = ByteArrayOutputStream()
        original?.compress(Bitmap.CompressFormat.JPEG, 60, stream2)
        val bytes2 = stream2.toByteArray()
        intent2.putExtra("image", bytes2)

        startActivity(intent2)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode === Activity.RESULT_OK){
            val file = File(mCurrentPhotoPath)
            val temp = Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
            // original = temp


            val ei2 : ExifInterface = ExifInterface(mCurrentPhotoPath)
            val orientation2 = ei2.getAttributeInt(ExifInterface.TAG_ORIENTATION , ExifInterface.ORIENTATION_UNDEFINED)
            var rotatedBitmap2 : Bitmap? = null

            when (orientation2) {

                ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap2 = rotateImage(temp!!, 90)

                ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap2 = rotateImage(temp!!,   180)

                ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap2 = rotateImage(temp!!,  270)

                ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap2 = temp!!

                else -> rotatedBitmap2 = temp!!


            }

            // 저장명 설정
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "DEASEL_" + timeStamp + "_"
            // 이미지 저장
            val imageSaveUri = Images.Media.insertImage(contentResolver,rotatedBitmap2,imageFileName,"saved from d-easel")
            val uri = Uri.parse(imageSaveUri)
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri))

            // 저장한 이미지 다시가져오기
            val path = getImagePathFromURI(uri)
            val options = BitmapFactory.Options()
            options.inSampleSize = 2
            original = BitmapFactory.decodeFile(path, options)

            // 회전 확인
            val ei : ExifInterface = ExifInterface(path)
            val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION , ExifInterface.ORIENTATION_UNDEFINED)
            var rotatedBitmap : Bitmap? = null

            if (original!= null) {

                when (orientation) {

                    ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(original!!, 90)

                    ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(original!!,  180)

                    ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(original!!,  270)

                    ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = original!!

                    else -> rotatedBitmap = original!!
                }

                original = rotatedBitmap


                var cropImage_ =  original!!.copy(original!!.config, true)



                tempuri = getLocalBitmapUri(cropImage_)


                CropImage.activity(tempuri)
                    .setInitialCropWindowPaddingRatio(0f)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setBorderLineColor(Color.WHITE)
                    .setGuidelinesColor(Color.WHITE)
                    .setActivityMenuIconColor(Color.WHITE)
                    .setRotationDegrees(90)
                    .start(this)



            }
        }

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            try {

                val path = getImagePathFromURI(data!!.data)
                val options = BitmapFactory.Options()
                options.inSampleSize = 2
                original = BitmapFactory.decodeFile(path, options)

                val ei : ExifInterface = ExifInterface(path)
                val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION , ExifInterface.ORIENTATION_UNDEFINED)
                var rotatedBitmap : Bitmap? = null

                if (original != null) { // 가져온 이미지가 null 이 아니면 엣지 디텍팅

                    when (orientation) {

                        ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(original!!, 90)

                        ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(original!!,  180)

                        ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(original!!,  270)

                        ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = original!!

                        else -> rotatedBitmap = original!!
                    }

                    original = rotatedBitmap

                    var cropImage_ =  original!!.copy(original!!.config, true)


                    tempuri = getLocalBitmapUri(cropImage_)

                    CropImage.activity(tempuri)
                        .setInitialCropWindowPaddingRatio(0f)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setBorderLineColor(Color.WHITE)
                        .setGuidelinesColor(Color.WHITE)
                        .setActivityMenuIconColor(Color.WHITE)
                        .setRotationDegrees(90)
                        .start(this)

                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            var result = CropImage.getActivityResult(data)


            if (resultCode == RESULT_OK) {
                var resultUri = result.uri

				var  bm = Images.Media.getBitmap(contentResolver, resultUri)

                original = bm


                goPreview()
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                var error = result.error

        }
    }


    }
    fun getImagePathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(Images.Media.DATA)
        val cursor = contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor == null) {
            return contentUri.path
        } else {
            val idx = cursor.getColumnIndexOrThrow(Images.Media.DATA)
            cursor.moveToFirst()
            val imgPath = cursor.getString(idx)
            cursor.close()
            return imgPath
        }
    }

    fun rotateImage(source: Bitmap, angle: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            var file = File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png")
            file.parentFile.mkdirs()
            var out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.close()
            bmpUri = Uri.fromFile(file)
           // bmpUri = FileProvider.getUriForFile(this,"jinjin.juju.young.d_easel.fileprovider",file)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bmpUri
    }

    override fun onBackPressed() {
        //2초 이내에 뒤로가기 버튼을 재 클릭 시 앱 종료
        if (System.currentTimeMillis() - lastTimeBackPressed < 2000) {
            if (bgm != null) { // 어플 종료 시 배경음악도 stop
                if (bgm!!.isPlaying) {
                    bgm!!.stop()
                    bgm!!.release()
                    bgm = null
                }
            }

            finish()
            return
        }
        //'뒤로' 버튼 한번 클릭 시 메시지
        longToast(" '뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.")
        //lastTimeBackPressed에 '뒤로'버튼이 눌린 시간을 기록

        lastTimeBackPressed = System.currentTimeMillis()
    }
    override fun onDestroy() {

        if (bgm != null) { // 어플 종료 시 배경음악도 stop
            if (bgm!!.isPlaying) {
                bgm!!.stop()
                bgm!!.release()
                bgm = null
            }
        }

        super.onDestroy()
        actList.remove(this)
        realm?.close() //인스턴스 해제
    }

    companion object {
       var bgm: MediaPlayer? = null
    }

}


