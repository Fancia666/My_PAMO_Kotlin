package com.example.tipper.cannon

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

// Cannon.java
// Represents Cannon and fires the Cannonbal
class Cannon(// view containing the Cannon
    private val view: CannonView, // Cannon base's radius
    private val baseRadius: Int, barrelLength: Int,
    barrelWidth: Int
) {
    // returns the Cannonball that this Cannon fired
    var cannonball // the Cannon's Cannonball
            : Cannonball? = null
        private set
    private val paint = Paint() // Paint used to draw the cannon

    // constructor
    init {
        paint.strokeWidth = barrelWidth.toFloat() // set width of barrel
        paint.color = Color.BLACK // Cannon's color is Black
    }

    // creates and fires Cannonball in the direction Cannon points
    fun fireCannonball(touchPoint: Double) {

        // calculate the Cannonball velocity's x component
        val velocityX = (CannonView.CANNONBALL_SPEED_PERCENT *
                view.screenWidth * Math.sin(touchPoint)).toInt()

        // calculate the Cannonball velocity's y component
        val velocityY = (CannonView.CANNONBALL_SPEED_PERCENT *
                view.screenWidth * -Math.cos(touchPoint)).toInt()

        // calculate the Cannonball's radius
        val radius = (view.screenHeight *
                CannonView.CANNONBALL_RADIUS_PERCENT).toInt()

        // construct Cannonball and position it in the Cannon
        cannonball = Cannonball(
            view, Color.BLACK,
            CannonView.CANNON_SOUND_ID, -radius,
            view.screenHeight / 2 - radius, radius, velocityX.toFloat(),
            velocityY.toFloat()
        )
        cannonball!!.playSound() // play fire Cannonball sound
    }

    // draws the Cannon on the Canvas
    fun draw(canvas: Canvas) {
        // draw cannon base
        canvas.drawCircle(
            0f, (view.screenHeight / 2).toFloat(),
            baseRadius.toFloat(), paint
        )
    }

    // removes the Cannonball from the game
    fun removeCannonball() {
        cannonball = null
    }
}