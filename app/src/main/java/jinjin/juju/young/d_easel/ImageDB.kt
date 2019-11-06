package jinjin.juju.young.d_easel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ImageDB :RealmObject(){

    @PrimaryKey
    open var id: Int = 0


    var image:ByteArray = ByteArray(0)
    var background : ByteArray = ByteArray(0)
    var mean_shift : ByteArray =  ByteArray(0)
    var lines : ByteArray = ByteArray(0)



}