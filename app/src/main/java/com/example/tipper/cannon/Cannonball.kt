// Cannonball.java
// Represents the Cannonball that the Cannon fires
package com.example.tipper.cannon

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF

class Cannonball     // constructor
    (
    view: CannonView?, color: Int, soundId: Int, x: Int,
    y: Int, radius: Int, private var velocityX: Float, velocityY: Float
) : GameElement(
    view, color, soundId, x, y,
    2 * radius, 2 * radius, velocityY
) {
    // get Cannonball's radius
    private val radius: Int
        private get() = (shape.right - shape.left) / 2

    // test whether Cannonball collides with the given GameElement
    fun collidesWith(element: GameElement): Boolean {
        //return (Rect.intersects(shape, element.shape) && velocityX > 0);
        val rect2 = RectF()
        element.matrix.mapRect(rect2)

//      int left, int top, int right, int bottom
        return Rect.intersects(
            shape,
            Rect(rect2.left.toInt(), rect2.top.toInt(), rect2.right.toInt(), rect2.bottom.toInt())
        ) && velocityX > 0
    }

    // reverses the Cannonball's horizontal velocity
    fun reverseVelocityX() {
        velocityX *= -1f
    }

    // updates the Cannonball's position
    override fun update(interval: Double) {
        super.update(interval) // updates Cannonball's vertical position

        // update horizontal position
        shape.offset((velocityX * interval).toInt(), 0)

        // if Cannonball goes off the screen
        if (shape.top < 0 || shape.left < 0 || shape.bottom > view!!.screenHeight || shape.right > view!!.screenWidth) {
        }
    }

    // draws the Cannonball on the given canvas
    override fun draw(canvas: Canvas) {
        canvas.drawCircle(
            (shape.left + radius).toFloat(),
            (
                    shape.top + radius).toFloat(), radius.toFloat(), paint
        )
    }
}