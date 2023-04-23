// Target.java
// Subclass of GameElement customized for the Target
package com.example.tipper.cannon

import android.graphics.Bitmap

class Target     // constructor
    (
    view: CannonView?, color: Int, // returns the hit reward for this Target
    val hitReward // the hit reward for this target
    : Int, x: Int, y: Int,
    width: Int, length: Int, velocityY: Float, bitmap: Bitmap?
) : GameElement(
    view!!, color, CannonView.TARGET_SOUND_ID, x, y, width, length,
    velocityY, bitmap
)