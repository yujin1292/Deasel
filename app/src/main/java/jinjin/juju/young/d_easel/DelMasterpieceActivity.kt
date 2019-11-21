package jinjin.juju.young.d_easel
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_delmasterpiece.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class DelMasterpieceActivity : AppCompatActivity() {

    var realm = Realm.getDefaultInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delmasterpiece)

        val realmResults = realm.where<ImageDB>().findAll()
        val gridAdapter = DelMasterpieceListAdapter(realmResults!!)
        delMasterpieceList.adapter = gridAdapter
        delMasterpieceList.choiceMode = GridView.CHOICE_MODE_MULTIPLE


        delBtn.setOnClickListener {

            var checkeditems = getCheckedImage()
            var cnt = gridAdapter.count

            if(checkeditems.size()<=0){
                alert("삭제할 이미지를 선택해주세요") {
                    yesButton {}
                }.show()
            }
            else {
                alert("정말 삭제하시겠습니까?") {
                    yesButton {
                        for (i in cnt - 1 downTo 0 step 1) {
                            if (checkeditems.get(i)) {
                                var delImageInfo = gridAdapter.getItem(i)
                                var delImageid = delImageInfo?.id
                                if (delImageInfo != null)
                                    delImage(delImageid!!)
                            }
                        }

                        delMasterpieceList.clearChoices()
                        finish()
                    }
                    noButton { }
                }.show()
            }
        }

    }
    private fun delImage(id:Int){
        realm.beginTransaction()
        val deleteItem = realm.where<ImageDB>().equalTo("id",id).findFirst()!!
        deleteItem.deleteFromRealm()
        realm.commitTransaction()
    }

    fun getCheckedImage(): SparseBooleanArray {
        return  delMasterpieceList.checkedItemPositions
    }
}

