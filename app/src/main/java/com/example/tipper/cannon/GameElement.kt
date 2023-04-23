// GameElement.java
// Represents a rectangle-bounded game element
package com.example.tipper.cannon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.media.Image;

import java.io.IOException;
import java.io.InputStream;

public class   GameElement {
   protected CannonView view; // the view that contains this GameElement
   protected Paint paint = new Paint(); // Paint to draw this GameElement
   protected Rect shape; // the GameElement's rectangular bounds

   protected Matrix matrix;

   private float velocityYimg;

   private Bitmap bitmap = null;
   protected int imgX;

   protected int imgY;
   private float velocityY; // the vertical velocity of this GameElement
   private int soundId; // the sound associated with this GameElement

   public GameElement(CannonView view, int color, int soundId, int x,
                      int y, int width, int length, float velocityY, Bitmap bitmap) {

      this(view, color, soundId, x, y, width, length, velocityY);
      this.bitmap = bitmap;
   }

   // public constructor
   public GameElement(CannonView view, int color, int soundId, int x,
      int y, int width, int length, float velocityY) {
      this.view = view;
      paint.setColor(color);
      shape = new Rect(x, y, x + width, y + length); // set bounds

      imgX = x;
      imgY = y;

      matrix = new Matrix();
      matrix.setScale(0.1f, 0.1f);
      matrix.setTranslate(imgX, y);

      this.soundId = soundId;
      this.velocityY = velocityY;
      this.velocityYimg = velocityY;
   }

   // update GameElement position and check for wall collisions
   public void update(double interval) {
      float var = (float) (velocityYimg * interval);

      if(imgY < 0 && velocityYimg < 0  ||
              imgY + 150 > view.getScreenHeight() && velocityYimg > 0){
         velocityYimg *= -1;
      }

      imgY += var;
      matrix.setTranslate((float) imgX, imgY);

      // update vertical position
      shape.offset(0, (int) (velocityY * interval));

      // if this GameElement collides with the wall, reverse direction
      if (shape.top < 0 && velocityY < 0 ||
         shape.bottom > view.getScreenHeight() && velocityY > 0)
         velocityY *= -1; // reverse this GameElement's velocity
   }

   // draws this GameElement on the given Canvas
   public void draw(Canvas canvas) {
      bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
      canvas.drawBitmap(bitmap, matrix, paint);

   }

   // plays the sound that corresponds to this type of GameElement
   public void playSound() {
      view.playSound(soundId);
   }
}

/*********************************************************************************
 * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and * Pearson Education, *
 * Inc. All Rights Reserved. * * DISCLAIMER: The authors and publisher of this   *
 * book have used their * best efforts in preparing the book. These efforts      *
 * include the * development, research, and testing of the theories and programs *
 * * to determine their effectiveness. The authors and publisher make * no       *
 * warranty of any kind, expressed or implied, with regard to these * programs   *
 * or to the documentation contained in these books. The authors * and publisher *
 * shall not be liable in any event for incidental or * consequential damages in *
 * connection with, or arising out of, the * furnishing, performance, or use of  *
 * these programs.                                                               *
 *********************************************************************************/
