package jinjin.juju.young.d_easel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : AppCompatActivity() {

    private var original_p: Bitmap? = null
    private var canvas_p: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        // 파일 두개받기
        val intent = intent

        val s = intent.getStringExtra("where")


      /*  var bytes = intent.getByteArrayExtra("original")
        original_p = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
*/

        var bytes2 = intent.getByteArrayExtra("image")
        canvas_p = BitmapFactory.decodeByteArray(bytes2, 0, bytes2.size)


        mean_shift.setImageBitmap(original_p)
        edge_canvas.setImageBitmap(canvas_p)


        // imageView setting

        where.text = s



        start_btn.setOnClickListener{view->
            //intent 전달후 startActivity

        }
    }
}
