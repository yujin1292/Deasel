package jinjin.juju.young.d_easel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {




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

    }


    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
