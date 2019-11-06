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
import androidx.core.app.ActivityCompat
import io.realm.Realm
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance()

    private var permissionsRequired = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_CALLBACK_CONSTANT = 987


    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        var realmResults = realm.where<ImageDB>().findAll()
        if(realmResults.isEmpty()){

            realm.beginTransaction()


            val newImage = realm.createObject(ImageDB::class.java, nextId())
            val sendBitmap = BitmapFactory.decodeResource(resources,R.drawable.hs)
            val stream = ByteArrayOutputStream()
            sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            newImage.image = byteArray
            realm.commitTransaction()


            realm.beginTransaction()
            val newImage2 = realm.createObject(ImageDB::class.java, nextId())
            val sendBitmap2 = BitmapFactory.decodeResource(resources,R.drawable.b)
            val stream2 = ByteArrayOutputStream()
            sendBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2)
            val byteArray2 = stream2.toByteArray()
            newImage2.image = byteArray2
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

        }


        ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)


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
