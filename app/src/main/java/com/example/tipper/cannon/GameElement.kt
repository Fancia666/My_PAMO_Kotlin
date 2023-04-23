// GameElement.java
// Represents a rectangle-bounded game element
package com.example.tipper.cannon

import android.graphics.*

open class GameElement(// the view that contains this GameElement
    protected var view: CannonView?, color: Int, soundId: Int, x: Int,
    y: Int, width: Int, length: Int, velocityY: Float
) {
    protected var paint = Paint() // Paint to draw this GameElement
    protected var shape // the GameElement's rectangular bounds
            : Rect
    var matrix: Matrix
    private var velocityYimg: Float
    private var bitmap: Bitmap? = null
    protected var imgX: Int
    protected var imgY: Int
    private var velocityY // the vertical velocity of this GameElement
            : Float
    private val soundId // the sound associated with this GameElement
            : Int

    constructor(
        view: CannonView, color: Int, soundId: Int, x: Int,
        y: Int, width: Int, length: Int, velocityY: Float, bitmap: Bitmap?
    ) : this(view, color, soundId, x, y, width, length, velocityY) {
        this.bitmap = bitmap
    }

    // public constructor
    init {
        paint.color = color
        shape = Rect(x, y, x + width, y + length) // set bounds
        imgX = x
        imgY = y
        matrix = Matrix()
        matrix.setScale(0.1f, 0.1f)
        matrix.setTranslate(imgX.toFloat(), y.toFloat())
        this.soundId = soundId
        this.velocityY = velocityY
        velocityYimg = velocityY
    }

    // update GameElement position and check for wall collisions
    open fun update(interval: Double) {
        val `var` = (velocityYimg * interval).toFloat()
        if (imgY < 0 && velocityYimg < 0 ||
            imgY + 150 > view!!.screenHeight && velocityYimg > 0
        ) {
            velocityYimg *= -1f
        }
        imgY += `var`.toInt()
        matrix.setTranslate(imgX.toFloat(), imgY.toFloat())

        // update vertical position
        shape.offset(0, (velocityY * interval).toInt())

        // if this GameElement collides with the wall, reverse direction
        if (shape.top < 0 && velocityY < 0 ||
            shape.bottom > view!!.screenHeight && velocityY > 0
        ) velocityY *= -1f // reverse this GameElement's velocity
    }

    // draws this GameElement on the given Canvas
    open fun draw(canvas: Canvas) {
        bitmap = Bitmap.createScaledBitmap(bitmap!!, 150, 150, false)
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap!!, 150, 150, false), matrix, paint)
    }

    // plays the sound that corresponds to this type of GameElement
    fun playSound() {
        view!!.playSound(soundId)
    }
}