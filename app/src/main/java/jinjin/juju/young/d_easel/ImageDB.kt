package jinjin.juju.young.d_easel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ImageDB :RealmObject(){

    @PrimaryKey
    open var id: Int = 0
    //원본 이미지
    var original : ByteArray = ByteArray(0)
    //그림그린 이미지
    var image:ByteArray = ByteArray(0)
    // 엣지 영상
    var background : ByteArray = ByteArray(0)
    //meanshift 영상
    var mean_shift : ByteArray =  ByteArray(0)
    // 그림 그린것 엣지랑 같이 캡처
    var lines : ByteArray = ByteArray(0)



}