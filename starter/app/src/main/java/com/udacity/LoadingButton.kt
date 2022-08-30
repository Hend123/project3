package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var textColor = 0
    private var backGroundColor = 0
    private var circleColor = 0
    private var textSize = 0
    private var buttonProgress: Float = 0f

    private var text = context.getString(R.string.download)
    private var animator: ValueAnimator = ValueAnimator()
    private val rect: RectF = RectF(80f, 30f, 200f, 150f)
    private var value = 0.0f
    private var progressBtn: Float = 0f


     var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when (new) {
            ButtonState.Loading -> {
                startAnimation()
                isEnabled = false
            }

            ButtonState.Completed -> {
                animator.cancel()
                invalidate()
                isEnabled = true
                buttonState = ButtonState.Clicked
            }

            else -> {
                isEnabled = true
                invalidate()
                return@observable
            }
        }


    }

    private val paintText = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        typeface = Typeface.create("", Typeface.BOLD)
        textSize = 20.0f * resources.displayMetrics.density
        textAlign = Paint.Align.CENTER
        color = textColor
    }

    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = backGroundColor
    }

    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = circleColor
        style = Paint.Style.FILL
    }


    init {
        buttonState = ButtonState.Clicked
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            backGroundColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)
            textSize = getDimensionPixelSize(R.styleable.LoadingButton_textSize, 0)

        }
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                progressBtn = animatedValue as Float
                invalidate()
            }
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            duration = 2000
            start()
        }
    }
    private fun drawBackgroundButton(canvas: Canvas?, progress: Float? = null) {
        canvas?.apply {
            if (progress != null) {
                paintBackground.alpha = 220
            }
            drawRect(0f, 0f, width.toFloat() * (progress ?: 1f), height.toFloat(), paintBackground)
        }
    }

    private fun drawTextButton(canvas: Canvas?, text: String) {
        canvas?.apply {
            val posTextX = (width / 2).toFloat()
            val posTextY = ((height - (paintText.descent() + paintText.ascent())) / 2)
            drawText(text, posTextX, posTextY, paintText)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawBackgroundButton(canvas)
            when (buttonState) {
                ButtonState.Loading -> {
                    drawBackgroundButton(canvas, buttonProgress)
                    drawArc(
                        rect,
                        -180f,
                        buttonProgress * 360,
                        true,
                        paintProgress
                    )

                    drawTextButton(canvas, resources.getString(R.string.button_loading))
                }

                ButtonState.Completed -> {
                    drawTextButton(canvas, resources.getString(R.string.complete))
                }

                else -> {
                    drawTextButton(canvas, text)
                }
            }
            invalidate()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }




}