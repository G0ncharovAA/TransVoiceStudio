package com.gmail.sasha.inverse.transvoicestudio.view.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gmail.sasha.inverse.transvoicestudio.R

class FrequencyView : View {

    private var mWidth = 0
    private var mHeight = 0

    private val minValue = 1f
    private val maxValue = 2205f
    private val duration = maxValue - minValue
    private val wFrequency = 60
    private var rWidth = 0

    // Attributes
    private val paint = Paint()
    private var bitmap: Bitmap? = null
    private lateinit var canvas: Canvas
    private var pos = 0
    private var samplingRate = 0
    private lateinit var magnitudes: FloatArray
    private var scaleCaption: String
    private var colors: IntArray
    private var scaleColor: Int
    private var fontColor: Int
    private var backgroundDefaultColor: Int

    init {
        with(context.resources) {

            scaleCaption = "    ${getString(R.string.herz)}"

            colors = arrayOf(
                getColor(R.color.flag_center),
                getColor(R.color.flag_inner),
                getColor(R.color.flag_outter)
            ).toIntArray()

            scaleColor = getColor(R.color.flag_outter_dark)
            fontColor = getColor(R.color.flag_center)
            backgroundDefaultColor = getColor(R.color.flag_outter)
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
        mHeight = h
        rWidth = mWidth - wFrequency
        pos = w
        bitmap?.recycle()
        bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888).apply {
            canvas = Canvas(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        invalidate()
        return true
    }

    /**
     * Simple sets
     */
    fun setFFTResolution(res: Int) {
        magnitudes = FloatArray(res)
    }

    fun setSamplingRate(sampling: Int) {
        samplingRate = sampling
    }

    fun setMagnitudes(m: FloatArray) {
        System.arraycopy(m, 0, magnitudes, 0, m.size)
    }

    /**
     * Called whenever a redraw is needed
     * Renders spectrogram and scale on the right
     * Frequency scale can be linear or logarithmic
     */
    public override fun onDraw(canvas: Canvas) {
        paint.strokeWidth = 1f

        // Update buffer bitmap
        paint.color = backgroundDefaultColor
        this.canvas.drawLine(
            pos % rWidth.toFloat(),
            0f,
            pos % rWidth.toFloat(),
            mHeight.toFloat(),
            paint
        )

        for (i in 0 until mHeight) {
            var j = getValueFromRelativePosition(
                ((mHeight - i).toFloat() / mHeight) / 10,
                minValue,
                maxValue
            )
            j /= maxValue
            val mag = magnitudes[
                    (j
                            * magnitudes.size
                            / 2
                            ).toInt()
            ]
            val db =
                Math.max(0.0, -20 * Math.log10(mag.toDouble())).toFloat()
            val c = getInterpolatedColor(colors, db * 0.009f)
            paint.color = c
            val x = pos % rWidth
            this.canvas.drawPoint(x.toFloat(), i.toFloat(), paint)
            this.canvas.drawPoint(x.toFloat(), i.toFloat(), paint) // make color brighter
        }

        // Draw bitmap
        bitmap?.let {
            canvas.drawBitmap(it, 0f - pos % rWidth, 0f, paint)
            canvas.drawBitmap(it, (rWidth - pos % rWidth).toFloat(), 0f, paint)
        }

        // Draw frequency scale
        val ratio = 0.7f * resources.displayMetrics.density
        paint.textSize = 12f * ratio
        paint.color = scaleColor
        canvas.drawRect(rWidth.toFloat(), 0f, mWidth.toFloat(), mHeight.toFloat(), paint)
        paint.color = fontColor
        canvas.drawText(
            scaleCaption,
            rWidth.toFloat(),
            12 * ratio, paint
        )
        var i = 0
        while (i < (maxValue - 120)) {
            canvas.drawText(
                "   $i",
                rWidth.toFloat(),
                mHeight * (1f - i.toFloat() / (maxValue)),
                paint
            )
            i += 100
        }

        pos++
    }

    /**
     * Returns a value from its relative position within given boundaries
     */
    private fun getValueFromRelativePosition(
        position: Float,
        minValue: Float,
        maxValue: Float
    ): Float = minValue + position * (maxValue - minValue)

    /**
     * Calculate rainbow colors
     */
    private fun ave(s: Int, d: Int, p: Float): Int =
        s + Math.round(p * (d - s))

    private fun getInterpolatedColor(colors: IntArray, unit: Float): Int {
        if (unit <= 0) return colors[0]
        if (unit >= 1) return colors[colors.size - 1]
        var p = unit * (colors.size - 1)
        val i = p.toInt()
        p -= i.toFloat()

        // now p is just the fractional part [0...1) and i is the index
        val c0 = colors[i]
        val c1 = colors[i + 1]
        val a = ave(Color.alpha(c0), Color.alpha(c1), p)
        val r = ave(Color.red(c0), Color.red(c1), p)
        val g = ave(Color.green(c0), Color.green(c1), p)
        val b = ave(Color.blue(c0), Color.blue(c1), p)
        return Color.argb(a, r, g, b)
    }
}