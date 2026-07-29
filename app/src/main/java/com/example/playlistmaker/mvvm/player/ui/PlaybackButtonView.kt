package com.example.playlistmaker.mvvm.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet?,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0 ): View(context, attrs, defStyleAttr, defStyleRes) {


    private var imageBitmap: Bitmap? = null
    private val imageBitmapPlay: Bitmap?
    private val imageBitmapPause: Bitmap?
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
                imageBitmapPlay = getDrawable(R.styleable.PlaybackButtonView_playImageResId)?.toBitmap()
                imageBitmapPause = getDrawable(R.styleable.PlaybackButtonView_pauseImageResId)?.toBitmap()
            } finally {
                recycle()
            }
        }
        setPaused()
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle().apply {
            putBoolean(IS_PLAYING, isPlaying)
        }
        bundle.putParcelable(SUPER, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        isPlaying = bundle.getBoolean(IS_PLAYING, false)
        super.onRestoreInstanceState(bundle.getParcelable(SUPER))
        if (isPlaying)
            setPlaying()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        imageBitmap?.let {
            canvas.drawBitmap(it, null, imageRect, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                changeImage()
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun changeImage() {
        if (isPlaying)
            setPaused()
        else
            setPlaying()
    }

    fun setPlaying() {
        isPlaying=true
        imageBitmap = imageBitmapPause
        invalidate()
    }

    fun setPaused() {
        isPlaying=false
        imageBitmap = imageBitmapPlay
        invalidate()
    }

    companion object {
        const val SUPER = "SUPER"
        const val IS_PLAYING = "IS_PlAYING"
    }
}
