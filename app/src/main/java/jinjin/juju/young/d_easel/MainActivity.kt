package jinjin.juju.young.d_easel

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var permissionsRequired = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_CALLBACK_CONSTANT = 987


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //카메라
        go_sketch.setOnClickListener { view ->
            val intent : Intent = Intent( this, SelectActivity::class.java)
            startActivity(intent)
        }

        //마스터 피스 엑티비티 호출
        go_master.setOnClickListener { view ->

        }


        jinjinjarajujuyoung.setOnClickListener{view->

        }


        ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)


    }


}
