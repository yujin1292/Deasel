package jinjin.juju.young.d_easel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

public open class ImageDB():RealmObject(){

    @PrimaryKey
    public open var id: Int = 0


    public var image:ByteArray = ByteArray(0)
    public var background : ByteArray = ByteArray(0)
    public var mean_shift : ByteArray =  ByteArray(0)



}