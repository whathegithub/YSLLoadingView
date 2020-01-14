package com.jbw

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.jbw.yslloadingview.R


class YSLLoadingView : View {

    var circleBaseColor = Color.GRAY
    var mLoadingViewSize: Float = 0f
    var mNormalCircleRadius: Float = 1f
    var mAllCirleRadius = FloatArray(12) //kotlin定义数组
    var mAllCirleColor = IntArray(12)
    var mLoadViewRadius: Float = 0f
    var mLoadViewRadiusX: Float = 0f
    var mLoadViewRadiusY: Float = 0f


    val circleAllAround = 30f //间隔角度
    lateinit var mPaint: Paint //延后初始化
    lateinit var animator: ValueAnimator

    var mCurrentAnimValue = 0
    var mDuration = 1f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        initAttrs(context, attrs)
        initParams()
        initPaint()
        initCircle()
        initAnimator()
    }

    private fun initParams() {
        mLoadViewRadius = mLoadingViewSize / 2 - mNormalCircleRadius * 2
        mLoadViewRadiusX = mLoadingViewSize / 2
        mLoadViewRadiusY = mLoadingViewSize / 2
    }

    init {


    }

    private fun initAnimator() {
        animator = ValueAnimator.ofInt(0, 12)//包头不包尾
        animator.addUpdateListener {
            val value = it.animatedValue as Int
            if (mCurrentAnimValue != value) {
                mCurrentAnimValue = value
                invalidate()
            }

        }

        animator.duration = (mDuration * 1000).toLong()
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()

        animator.start()
    }

    private fun initAttrs(context: Context?, attrs: AttributeSet?) {
        val obtainStyledAttributes = context?.obtainStyledAttributes(attrs, R.styleable.YSLLoadingView)

        circleBaseColor = obtainStyledAttributes?.getColor(R.styleable.YSLLoadingView_jbw_color, Color.GRAY)!!
        mLoadingViewSize = obtainStyledAttributes.getDimension(R.styleable.YSLLoadingView_jbw_loading_view_size, dp2px(context,50f))
        mDuration = obtainStyledAttributes.getFloat(R.styleable.YSLLoadingView_jbw_duration, 1f)

        mNormalCircleRadius = mLoadingViewSize / 20 * 1f
        obtainStyledAttributes.recycle()
    }

    /**
     * dp转px
     */
    private fun dp2px(context: Context, dp: Float): Float {
        val density = context.resources.displayMetrics.density
        return (density * dp + 0.5f)
    }


    /**
     * 定义每个位置小园的大小和颜色
     */
    private fun initCircle() {
        //0-6的都是正常小球
        for (index in 0..11) {
            //kotlin中用when替代switch
            when (index) {
                7 -> {
                    mAllCirleRadius[index] = mNormalCircleRadius * 1.2f
                    mAllCirleColor[index] = getColorWithAlpha(0.6f,circleBaseColor)
                }
                8 -> {
                    mAllCirleRadius[index] = mNormalCircleRadius * 1.4f
                    mAllCirleColor[index] = getColorWithAlpha(0.7f,circleBaseColor)
                }
                9 -> {
                    mAllCirleRadius[index] = mNormalCircleRadius * 1.6f
                    mAllCirleColor[index] = getColorWithAlpha(0.8f,circleBaseColor)
                }
                10 -> {
                    mAllCirleRadius[index] = mNormalCircleRadius * 1.8f
                    mAllCirleColor[index] = getColorWithAlpha(1f,circleBaseColor)
                }
                11 -> {
                    mAllCirleRadius[index] = mNormalCircleRadius * 1.6f
                    mAllCirleColor[index] = getColorWithAlpha(0.8f,circleBaseColor)
                }
                else -> {
                    mAllCirleRadius[index] = mNormalCircleRadius * 1f
                    mAllCirleColor[index] = getColorWithAlpha(0.4f,circleBaseColor)
                }
            }
        }
    }

    /**
     * 给color添加透明度
     * @param alpha 透明度 0f～1f
     * @param baseColor 基本颜色
     * @return
     */
    fun getColorWithAlpha(alpha: Float, baseColor: Int): Int {
        val a = Math.min(255, Math.max(0, (alpha * 255).toInt())) shl 24
        val rgb = 0x00ffffff and baseColor
        return a + rgb
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mLoadingViewSize.toInt(), mLoadingViewSize.toInt())
    }


    private fun initPaint() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = circleBaseColor
        mPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (index in 0..11) {
            val i = circleAllAround * (index + mCurrentAnimValue)

            val toRadians = Math.toRadians(i.toDouble())
            val x = (mLoadViewRadiusX + Math.cos(toRadians) * mLoadViewRadius).toFloat()
            val y = (mLoadViewRadiusY + Math.sin(toRadians) * mLoadViewRadius).toFloat()

            mPaint.color = mAllCirleColor[index]
            canvas.drawCircle(x, y, mAllCirleRadius[index], mPaint)
        }
    }


}