package jinjin.juju.young.d_easel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MusicDB :RealmObject(){
    @PrimaryKey
    open  var id: Int = 0
    var is_on : String=""

    fun init(){
        id = 100
        is_on = "on"
    }

}

