package jinjin.juju.young.d_easel
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode

class Line { // 선에 대한 정보
    var paint: Paint? = null
    var color: Int = Color.BLACK
    var width: Float = 10F
    var alpha: Int = 255
    fun setLineColor(color: Int) {
        this.color = color
        this.setLine()
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
        paint?.color = color
        paint?.alpha = alpha
        paint?.isDither = true
        paint?.strokeWidth = width
        paint?.strokeJoin = Paint.Join.ROUND
        paint?.style = Paint.Style.STROKE
        paint?.strokeCap = Paint.Cap.ROUND
        paint?.isAntiAlias = true
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