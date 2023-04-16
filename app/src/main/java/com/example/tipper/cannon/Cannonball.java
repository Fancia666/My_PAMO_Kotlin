// Cannonball.java
// Represents the Cannonball that the Cannon fires
package com.example.tipper.cannon;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

public class Cannonball extends GameElement {
   private float velocityX;

   // constructor
   public Cannonball(CannonView view, int color, int soundId, int x,
      int y, int radius, float velocityX, float velocityY) {
      super(view, color, soundId, x, y,
         2 * radius, 2 * radius, velocityY);
      this.velocityX = velocityX;
   }

   // get Cannonball's radius
   private int getRadius() {
      return (shape.right - shape.left) / 2;
   }

   // test whether Cannonball collides with the given GameElement
   public boolean collidesWith(GameElement element) {
      //return (Rect.intersects(shape, element.shape) && velocityX > 0);

      RectF rect2 = new RectF();
      element.matrix.mapRect(rect2);

//      int left, int top, int right, int bottom
      return (Rect.intersects(shape,
              new Rect((int)rect2.left, (int)rect2.top,
                      (int)rect2.right, (int)rect2.bottom)) && velocityX > 0);
   }


   // reverses the Cannonball's horizontal velocity
   public void reverseVelocityX() {
      velocityX *= -1;
   }

   // updates the Cannonball's position
   @Override
   public void update(double interval) {
      super.update(interval); // updates Cannonball's vertical position

      // update horizontal position
      shape.offset((int) (velocityX * interval), 0);

      // if Cannonball goes off the screen
      if (shape.top < 0 || shape.left < 0 ||
         shape.bottom > view.getScreenHeight() ||
         shape.right > view.getScreenWidth())
      {}
   }

   // draws the Cannonball on the given canvas
   @Override
   public void draw(Canvas canvas) {
      canvas.drawCircle(shape.left + getRadius(),
         shape.top + getRadius(), getRadius(), paint);
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
