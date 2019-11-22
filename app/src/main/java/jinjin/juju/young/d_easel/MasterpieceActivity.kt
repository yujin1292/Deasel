package jinjin.juju.young.d_easel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_masterpiece.*
import org.jetbrains.anko.act


class MasterpieceActivity : BaseActivity() {

    var realm = Realm.getDefaultInstance()
    private val SELECT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masterpiece)

        val realmResults = realm.where<ImageDB>().findAll()
        val gridAdapter = MasterpieceListAdapter(realmResults!!)
        MasterpieceList.adapter = gridAdapter


        Log.d("List","before " + actList.toString())
        actList.add(this)
        Log.d("List",actList.toString())

        //리스트에서 아이템 선택시,,해당 이미지 정보 넘겨서 페인팅 액티비티에 전달
        MasterpieceList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this,PaintingActivity::class.java)
            var imageInfo = gridAdapter.getItem(position)
            var id = imageInfo?.id
            intent.putExtra("id",id)
            startActivity(intent)
        }

        val intent = Intent(this,DelMasterpieceActivity::class.java)
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
