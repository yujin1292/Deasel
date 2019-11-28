package jinjin.juju.young.d_easel
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.core.graphics.*
import kotlinx.android.synthetic.main.activity_painting.*

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

    var pContext:Context? = null

    constructor(context: Context, rect: Rect , edge: Bitmap,imageButton: ImageButton):super(context){
        pContext = context

        bitmap = edge.scale(rect.width(),rect.height(),true)
        //setmatrix 이용해서
        canvas = Canvas(bitmap)

        path = Path()
        line = Line(imageButton)
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
        var x: Float = event!!.x
        var y: Float = event.y
        if(isSpoid) {
            var color:Int = bitmap?.getPixel(x.toInt(),y.toInt())!!
            if(color.alpha == 0)
                color = Color.WHITE
            line?.setLineColor(color)
            line?.setPen()

            (pContext as PaintingActivity).spoid_btn.setImageResource(R.drawable.spoid_icon)
            (pContext as PaintingActivity).pen_setting_btn.setImageResource(R.drawable.pencil_icon2)
            (pContext as PaintingActivity).eraser_btn.setImageResource(R.drawable.eraser_icon)
            (pContext as PaintingActivity).brush_setting_btn.setImageResource(R.drawable.brush_icon)

            isSpoid = false


        }
        else{
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
        }
        return false
    }
}