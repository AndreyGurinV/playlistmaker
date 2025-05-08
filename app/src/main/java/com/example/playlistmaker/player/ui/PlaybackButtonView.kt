package com.example.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val minViewSize = resources.getDimensionPixelSize(
        R.dimen.playbackbutton_min_size_24
    )

    var click: () -> Unit  = {}

    private val imagePause: Bitmap?
    private val imagePlay: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var isPlaying = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {

                imagePause = getDrawable(R.styleable.PlaybackButtonView_imagePauseResId)?.toBitmap()
                imagePlay = getDrawable(R.styleable.PlaybackButtonView_imagePlayResId)?.toBitmap()

            } finally {
                recycle()
            }
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val contentWidth = when (widthMode) {
            MeasureSpec.UNSPECIFIED -> minViewSize

            MeasureSpec.EXACTLY -> widthSize

            MeasureSpec.AT_MOST -> widthSize

            else -> error("Неизвестный режим ширины ($widthMode)")
        }

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val contentHeight = when (heightMode) {
            MeasureSpec.UNSPECIFIED -> minViewSize

            MeasureSpec.EXACTLY -> heightSize

            MeasureSpec.AT_MOST -> heightSize

            else -> error("Неизвестный режим высоты ($heightMode)")
        }

        val size = min(contentWidth, contentHeight)
        setMeasuredDimension(size, size)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        if (!isPlaying){
            imagePlay?.let {
                canvas.drawBitmap(it, null, imageRect, null)
            }
        } else {
            imagePause?.let {
                canvas.drawBitmap(it, null, imageRect, null)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN ->
                return true
            MotionEvent.ACTION_UP -> {
                changeState(!isPlaying)
                click.invoke()
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    fun changeState(isPlayingState: Boolean){
        isPlaying = isPlayingState
        invalidate()
    }
}