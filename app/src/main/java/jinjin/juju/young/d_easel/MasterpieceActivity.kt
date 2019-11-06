package jinjin.juju.young.d_easel
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_masterpiece.*


class MasterpieceActivity : AppCompatActivity() {

    var realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masterpiece)

        val realmResults = realm.where<ImageDB>().findAll()
        val gridAdapter = MasterpieceListAdapter(realmResults)
        MasterpieceList.adapter = gridAdapter

        //리스트에서 아이템 선택시,,해당 이미지 정보 넘겨서 페인팅 액티비티에 전달
        MasterpieceList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this,PaintingActivity::class.java)
            var imageInfo = gridAdapter.getItem(position)
            var backgroundimage = imageInfo?.image
            var id = imageInfo?.id
            intent.putExtra("image", backgroundimage)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }
}
