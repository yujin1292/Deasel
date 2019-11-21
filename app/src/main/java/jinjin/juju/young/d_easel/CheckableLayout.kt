package jinjin.juju.young.d_easel

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.checkmasterpiece.view.*

class CheckableLayout(context: Context, attributeSet: AttributeSet) : LinearLayout(context,attributeSet), Checkable {

    override fun isChecked(): Boolean{

        return checkBox.isChecked
    }

    override fun toggle() {
        isChecked = !checkBox.isChecked
    }

    override fun setChecked(checked: Boolean) {
        if(checkBox.isChecked != checked ) checkBox.isChecked = checked
    }

    //checkBox.isChecked 는 레이아웃 안에 존재하는 체크박스의 체크여부를 말하고
    //isChecke 홀로 있는 것은 체크박스를 담고 있는 뷰그룹 전체를 가리킴

}