package jinjin.juju.young.d_easel
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.widget.ImageButton

class Line { // 선에 대한 정보
    var paint: Paint? = null
    var color: Int = Color.BLACK
    var width: Float = 10F
    var alpha: Int = 255
    var imageButton:ImageButton? = null
    constructor(imageButton: ImageButton){
        this.imageButton = imageButton
    }
    fun setLineColor(color: Int) {
        this.color = color
        this.alpha = 255
        this.setLine()
        imageButton?.setBackgroundColor(color)

    }

    fun setLineWidth(width: Float) {
        this.width = width
        this.setLine()
    }

    fun setLineAlpha(alpha: Int) {
        this.alpha = alpha
        this.setLine()
    }

    fun setPen() {
        paint = Paint()
        alpha = 255
        paint?.isAntiAlias = true
        paint?.color = color
        paint?.alpha = alpha
        paint?.isDither = true
        paint?.strokeWidth = width
        paint?.strokeJoin = Paint.Join.ROUND
        paint?.style = Paint.Style.STROKE
        paint?.strokeCap = Paint.Cap.ROUND
    }

    fun setLine() {
        paint?.color = color
        paint?.alpha = alpha
        paint?.strokeWidth = width
    }

    fun setBrush() {
        paint = Paint()
        alpha = 15
        paint?.color = color
        paint?.alpha = alpha
        paint?.isDither = true
        paint?.strokeWidth = width
        paint?.strokeJoin = Paint.Join.ROUND
        paint?.style = Paint.Style.STROKE
        paint?.strokeCap = Paint.Cap.ROUND
        paint?.isAntiAlias = true
    }
    fun setErase() {
        paint?.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
    }

}