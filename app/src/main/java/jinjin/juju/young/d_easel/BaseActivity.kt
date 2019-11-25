package jinjin.juju.young.d_easel

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity


open class BaseActivity : AppCompatActivity() {
    companion object{
        var actList = ArrayList<Activity>()
    }

    fun getactList(): ArrayList<Activity> {
        return actList
    }

    fun actFinish() {
        for (i in actList)
            i.finish()

        finish()
    }
}