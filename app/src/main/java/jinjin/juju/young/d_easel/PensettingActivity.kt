package jinjin.juju.young.d_easel
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pensetting.*



class PensettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_pensetting)

        ok_btn.setOnClickListener {
            finish()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //바깥레이어 클릭 시 안닫히게
        return if (event?.action == MotionEvent.ACTION_OUTSIDE) {
            false
        } else true

    }

    override fun onBackPressed() {
        //안드로이드 백버튼 막기
        return
    }
}
