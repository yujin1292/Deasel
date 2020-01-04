package jinjin.juju.young.d_easel
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_masterpiece.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.progressDialog
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.bumptech.glide.Glide


class MasterpieceActivity : BaseActivity() {

    private var mHandler: Handler? = null
    private var logonum: Int = 0

    var realm = Realm.getDefaultInstance()
    private val SELECT = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masterpiece)

        val realmResults = realm.where<ImageDB>().findAll()
        if( realmResults.isEmpty()){

            alert("샘플 이미지를 불러오시겠습니까? ") {

                positiveButton("네"){
                    realm.close()
                    val dialog = progressDialog(message = "사진을 불러오는 중이에요.", title = "조금만 기다려주세요!")
                    dialog.show()
                    Thread(Runnable {
                        // TODOAuto-generated method stub
                        //sample 1
                        realm = Realm.getDefaultInstance()
                        realm?.beginTransaction()
                        dialog.progress = 10
                        val newImage = realm?.createObject(ImageDB::class.java, nextId())
                        dialog.progress = 12
                        val sendBitmap = BitmapFactory.decodeResource(resources,R.drawable.sample1)
                        val stream = ByteArrayOutputStream()
                        sendBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        dialog.progress = 15
                        val byteArray = stream.toByteArray()

                        newImage?.image = byteArray


                        newImage?.background = byteArray

                        dialog.progress = 20
                        //lines 투명 값

                        val line1 = ByteArrayOutputStream()
                        val temp1 = Bitmap.createBitmap(
                            sendBitmap.width,
                            sendBitmap.height,
                            Bitmap.Config.ARGB_8888
                        )
                        temp1.compress(Bitmap.CompressFormat.PNG, 100, line1)
                        val bytes1 = line1.toByteArray()
                        newImage?.lines = bytes1

                        realm?.commitTransaction()

                        dialog.progress = 25

                        // sample 2
                        realm?.beginTransaction()
                        val newImage2 = realm?.createObject(ImageDB::class.java, nextId())
                        val sendBitmap2 = BitmapFactory.decodeResource(resources,R.drawable.sample2)
                        val stream2 = ByteArrayOutputStream()
                        sendBitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2)
                        val byteArray2 = stream2.toByteArray()
                        newImage2?.image = byteArray2
                        newImage2?.background=byteArray2
                        dialog.progress = 40
                        //lines 투명 값

                        val line2 = ByteArrayOutputStream()
                        val temp2 = Bitmap.createBitmap(
                            sendBitmap2.width,
                            sendBitmap2.height,
                            Bitmap.Config.ARGB_8888
                        )
                        temp2.compress(Bitmap.CompressFormat.PNG, 100, line2)
                        val bytes2 = line2.toByteArray()
                        newImage2?.lines = bytes2

                        realm?.commitTransaction()

                        dialog.progress = 50

                        // sample 3

                        realm?.beginTransaction()
                        val newImage3 = realm?.createObject(ImageDB::class.java, nextId())
                        val sendBitmap3 = BitmapFactory.decodeResource(resources,R.drawable.sample3)
                        val stream3 = ByteArrayOutputStream()
                        dialog.progress = 55
                        sendBitmap3.compress(Bitmap.CompressFormat.PNG, 100, stream3)
                        val byteArray3 = stream3.toByteArray()
                        newImage3?.image = byteArray3
                        newImage3?.background=byteArray3
                        //lines 투명 값
                        dialog.progress = 65
                        val line3 = ByteArrayOutputStream()
                        val temp3 = Bitmap.createBitmap(
                            sendBitmap3.width,
                            sendBitmap3.height,
                            Bitmap.Config.ARGB_8888
                        )
                        temp3.compress(Bitmap.CompressFormat.PNG, 100, line3)
                        val bytes3 = line3.toByteArray()
                        newImage3?.lines = bytes3


                        realm?.commitTransaction()
                        dialog.progress = 75


                        realm?.beginTransaction()
                        val newImage4 = realm?.createObject(ImageDB::class.java, nextId())
                        val sendBitmap4 = BitmapFactory.decodeResource(resources,R.drawable.sample4)
                        val stream4 = ByteArrayOutputStream()
                        dialog.progress = 80
                        sendBitmap4.compress(Bitmap.CompressFormat.PNG, 100, stream4)
                        val byteArray4 = stream4.toByteArray()
                        newImage4?.image = byteArray4
                        newImage4?.background=byteArray4
                        //lines 투명 값
                        dialog.progress = 89
                        val line4 = ByteArrayOutputStream()
                        val temp4 = Bitmap.createBitmap(
                            sendBitmap4.width,
                            sendBitmap4.height,
                            Bitmap.Config.ARGB_8888
                        )
                        temp4.compress(Bitmap.CompressFormat.PNG, 100, line4)
                        val bytes4 = line4.toByteArray()
                        newImage4?.lines = bytes4
                        dialog.progress = 99

                        realm?.commitTransaction()
                        dialog.progress = 100
                        realm.close()
                        dialog.dismiss()
                    }).start()

                    realm= Realm.getDefaultInstance()
                }
                negativeButton("아니요"){

                }
            }.show()

        }
        val gridAdapter = MasterpieceListAdapter(realmResults!!)
        MasterpieceList.adapter = gridAdapter


        actList.add(this)





        //리스트에서 아이템 선택시,,해당 이미지 정보 넘겨서 페인팅 액티비티에 전달
        MasterpieceList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

           // val intent = Intent(this,PaintingActivity::class.java)
/*

            alert("색칠 하시겠습니까?") {

                positiveButton("네"){
                    var imageInfo = gridAdapter.getItem(position)
                    var id = imageInfo?.id
                    intent.putExtra("id",id)
                    startActivity(intent)

                }
                negativeButton("아니요"){

                }
            }.show()
*/


            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.selection,null)

            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.MATCH_PARENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )

            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 10.0F
            }


            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.exitTransition = slideOut

            }

            // Get the widgets reference from custom view
            val buttonPopup = view.findViewById<Button>(R.id.pop_ok)
            val buttonClose = view.findViewById<ImageButton>(R.id.dismiss_btn)

            var info = gridAdapter.getItem(position)

            val iv = view.findViewById<ImageView>(R.id.pop_img)

            iv.setImageBitmap( BitmapFactory.decodeByteArray(info!!.image, 0, info.image.size))


            // Set a click listener for popup's button widget
            buttonPopup.setOnClickListener{
                // Dismiss the popup window


                val intent = Intent(this,PaintingActivity::class.java)
                var imageInfo = gridAdapter.getItem(position)
                var id = imageInfo?.id
                intent.putExtra("id",id)
                popupWindow.dismiss()
                startActivity(intent)


            }

            buttonClose.setOnClickListener {
                popupWindow.dismiss()
            }


            // Finally, show the popup window on app
            TransitionManager.beginDelayedTransition(masterpiece_activity)
            popupWindow.showAtLocation(
                masterpiece_activity, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )

        }


        MasterpieceList.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val intent = Intent(this,DelMasterpieceActivity::class.java)
            startActivity(intent)
            true
        }


    }

    override fun onBackPressed() {
        actFinish()
    }

    override fun onDestroy() {

        Log.d("List","distroy"+ actList.toString())
        super.onDestroy()
        actList.remove(this)
        // realm.close() //인스턴스 해제
    }

    fun nextId():Int
    {
        val maxid = realm?.where<ImageDB>()?.max("id")
        if(maxid!=null){
            return maxid.toInt()+1
        }
        return 0
    }

}
