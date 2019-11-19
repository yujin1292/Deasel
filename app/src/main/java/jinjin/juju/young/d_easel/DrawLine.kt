package jinjin.juju.young.d_easel
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import org.opencv.android.Utils
import org.opencv.core.Mat

//그림 그리는 거를 관리하는 클래스

class DrawLine : View
{
    var line:Line? = null
    var bitmap: Bitmap? = null
    var canvas: Canvas? = null
    var path: Path? = null
    var oldX:Float = 0F
    var oldY:Float = 0F
    var rect:Rect? = null
    var isBrush:Boolean = false
    var isSpoid:Boolean = false

    constructor(context: Context, rect: Rect):super(context){
        bitmap = Bitmap.createBitmap(rect.width(), rect.height(),Bitmap.Config.ARGB_8888)
        //setmatrix 이용해서
        canvas = Canvas(bitmap)

        path = Path()
        line = Line()
        line?.setPen()
        this.rect = rect
    }

    override fun onDetachedFromWindow()
    {
        if(bitmap!=null)
            bitmap?.recycle()
        bitmap=null
        super.onDetachedFromWindow()
    }



    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(bitmap,0F,0F,null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var x: Float = event!!.getX()
        var y: Float = event!!.getY()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                path?.reset()
                path?.moveTo(x, y)
                oldX = x
                oldY = y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                var dx: Float = Math.abs(x - oldX)
                var dy: Float = Math.abs(y - oldY)
                if (dx >= 4 || dy >= 4) {
                    path?.quadTo(oldX, oldY, x, y)
                    oldX = x
                    oldY = y
                    canvas?.drawPath(path, line?.paint)
                }
                invalidate()
                return true
            }
        }
        return false
    }
}