package jinjin.juju.young.d_easel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import io.realm.Realm
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import android.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.preference.PreferenceManager
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance()

    private var permissionsRequired = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_CALLBACK_CONSTANT = 987


    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//  Declare a new thread to do a preference check
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




        var realmResults = realm.where<ImageDB>().findAll()
        if(realmResults.isEmpty()){

            realm.beginTransaction()


            val newImage = realm.createObject(ImageDB::class.java, nextId())
            val sendBitmap = BitmapFactory.decodeResource(resources,R.drawable.hs)
            val stream = ByteArrayOutputStream()
            sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            newImage.image = byteArray
            newImage.background = byteArray
            realm.commitTransaction()


            realm.beginTransaction()
            val newImage2 = realm.createObject(ImageDB::class.java, nextId())
            val sendBitmap2 = BitmapFactory.decodeResource(resources,R.drawable.b)
            val stream2 = ByteArrayOutputStream()
            sendBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2)
            val byteArray2 = stream2.toByteArray()
            newImage2.image = byteArray2
            newImage2.background=byteArray2
            realm.commitTransaction()
        }


        //카메라
        go_sketch.setOnClickListener { view ->
            val intent : Intent = Intent( this, SelectActivity::class.java)
            startActivity(intent)
        }

        //마스터 피스 엑티비티 호출
        go_master.setOnClickListener { view ->
            val intent :Intent  = Intent (this, MasterpieceActivity::class.java)
            startActivity(intent)

        }


        jinjinjarajujuyoung.setOnClickListener{view->
            Toast.makeText(this, "비밀인데,..", Toast.LENGTH_SHORT).show()
        }


        //ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)






    }

    fun nextId():Int
    {
        val maxid = realm.where<ImageDB>().max("id")
        if(maxid!=null){
            return maxid.toInt()+1
        }
        return 0
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
