package com.yp.baselib.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.yp.baselib.R

/**
 * 用来对方形的View进行圆角塑形，需要和方形View放在FrameLayout中，且位置置前。
 *
 * 自定义属性
 * corner_radius 圆角半径 单位可设置为dp
 */
class RoundShaping @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(context, attrs, defStyleAttr){

    val paint:Paint = Paint()
    val path = Path()
    var mode: PorterDuff.Mode
    private var corner_radius = 15f

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundShaping)
        corner_radius = ta.getDimension(R.styleable.RoundShaping_corner_radius, 15f)
        //一定要加上这句代码，否则有可能绘制不出来
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.isDither = true//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        paint.isFilterBitmap = true//加快显示速度，本设置项依赖于dither和xfermode的设置
        mode = PorterDuff.Mode.DST_OUT
        ta.recycle()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val sc = canvas?.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), paint, Canvas.ALL_SAVE_FLAG)
        canvas?.drawRect(0f,0f,width.toFloat(),height.toFloat(),paint)
        paint.xfermode = PorterDuffXfermode(mode)
        canvas?.drawRoundRect(0f,0f,width.toFloat(),height.toFloat(),
                corner_radius,
                corner_radius, paint)
        paint.xfermode = null
        canvas?.restoreToCount(sc!!)
    }

}

