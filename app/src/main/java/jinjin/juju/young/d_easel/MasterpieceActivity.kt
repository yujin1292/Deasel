package jinjin.juju.young.d_easel
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import kotlin.concurrent.thread


class MasterpieceActivity : BaseActivity() {

    private var mHandler: Handler? = null
    private var logonum: Int = 0

    var realm = Realm.getDefaultInstance()
    private val SELECT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masterpiece)


        val realmResults = realm.where<ImageDB>().findAll()
        val gridAdapter = MasterpieceListAdapter(realmResults!!)
        MasterpieceList.adapter = gridAdapter


        actList.add(this)

        //logo thread
        @SuppressLint("HandlerLeak")
        mHandler = object: Handler() {
            override fun handleMessage(msg: Message?) {

                var logoview = findViewById<ImageView>(R.id.coloring_logo)

                when (logonum) {
                    0 -> logoview?.setImageDrawable(getDrawable(R.drawable.cb0))
                    1 -> logoview?.setImageDrawable(getDrawable(R.drawable.cb1))
                    2 -> logoview?.setImageDrawable(getDrawable(R.drawable.cb2))
                    3 -> logoview?.setImageDrawable(getDrawable(R.drawable.cb3))
                    4 -> logoview?.setImageDrawable(getDrawable(R.drawable.cb4))
                    5 -> logoview?.setImageDrawable(getDrawable(R.drawable.cb5))
                    6 -> logoview?.setImageDrawable(getDrawable(R.drawable.cb6))
                    7 -> logoview?.setImageDrawable(getDrawable(R.drawable.cb7))
                    8 -> logoview?.setImageDrawable(getDrawable(R.drawable.cb8))
                    9 -> logoview?.setImageDrawable(getDrawable(R.drawable.cb9))
                    10-> logoview?.setImageDrawable(getDrawable(R.drawable.cb10))
                    11-> logoview?.setImageDrawable(getDrawable(R.drawable.cb11))
                    12-> logoview?.setImageDrawable(getDrawable(R.drawable.cb12))
                    13-> logoview?.setImageDrawable(getDrawable(R.drawable.cb13))
                    14-> logoview?.setImageDrawable(getDrawable(R.drawable.cb14))
                    15-> logoview?.setImageDrawable(getDrawable(R.drawable.cb15))
                }
            }
        }

        thread(start = true){

            var forward :Boolean  = true
            while(true){
                Thread.sleep(100)
                mHandler?.sendEmptyMessage(logonum)
                if(forward){
                    logonum++
                    if(logonum>16){ //logonum == 10
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



        //리스트에서 아이템 선택시,,해당 이미지 정보 넘겨서 페인팅 액티비티에 전달
        MasterpieceList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->


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
            val buttonClose = view.findViewById<Button>(R.id.pop_close)

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


            buttonClose.setOnClickListener{
                popupWindow.dismiss()
            }
            // Set a dismiss listener for popup window
            popupWindow.setOnDismissListener {
              //  Toast.makeText(applicationContext,"Popup closed",Toast.LENGTH_SHORT).show()
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
        realm.close() //인스턴스 해제
    }


}
