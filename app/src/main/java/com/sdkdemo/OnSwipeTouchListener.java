package com.sdkdemo;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {
   private final GestureDetector gestureDetector;

   public OnSwipeTouchListener(Context ctx) {
      gestureDetector = new GestureDetector(ctx, new GestureListener());
   }

   @Override
   public boolean onTouch(View v, MotionEvent event) {
      return gestureDetector.onTouchEvent(event);
   }

   private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
      private static final int SWIPE_THRESHOLD = 100;
      private static final int SWIPE_VELOCITY_THRESHOLD = 100;

//      @Override
//      public boolean onSingleTapUp(MotionEvent e) {
//         onClick();
//         return super.onSingleTapUp(e);
//      }

      @Override
      public boolean onDown(MotionEvent e) {
         return true;
      }

      @Override
      public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
         boolean result = false;
         try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
               if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                  if (diffX > 0) {
                     onSwipeRight();
                  } else {
                     onSwipeLeft();
                  }
                  result = true;
               }
            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
               if (diffY > 0) {
                  onSwipeBottom();
               } else {
                  onSwipeTop();
               }
               result = true;
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }
         return result;
      }



      @Override
      public void onLongPress(MotionEvent e) {
         super.onLongPress(e);
      }

      @Override
      public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
         return super.onScroll(e1, e2, distanceX, distanceY);

      }

      @Override
      public void onShowPress(MotionEvent e) {
         super.onShowPress(e);
      }

      @Override
      public boolean onDoubleTap(MotionEvent e) {
         return super.onDoubleTap(e);
      }

      @Override
      public boolean onDoubleTapEvent(MotionEvent e) {
         return super.onDoubleTapEvent(e);
      }

      @Override
      public boolean onSingleTapConfirmed(MotionEvent e) {
         return super.onSingleTapConfirmed(e);
      }

      @Override
      public boolean onContextClick(MotionEvent e) {
         return super.onContextClick(e);
      }
   }

   public void onClick() {
   }

   public void onSwipeRight() {
   }

   public void onSwipeLeft() {
   }

   public void onSwipeTop() {
   }

   public void onSwipeBottom() {
   }
   public void onScroll() {
   }
}


/*

class OnSwipeTouchListener implements View.OnTouchListener {
   private GestureDetector gestureDetector;

   OnSwipeTouchListener(Context c) {
      gestureDetector = new GestureDetector(c, new GestureListener());
   }

   public boolean onTouch(final View view, final MotionEvent motionEvent) {
      return gestureDetector.onTouchEvent(motionEvent);
   }

   private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
      private static final int SWIPE_THRESHOLD = 100;
      private static final int SWIPE_VELOCITY_THRESHOLD = 100;

      @Override
      public boolean onDown(MotionEvent e) {
         return true;
      }

      @Override
      public boolean onSingleTapUp(MotionEvent e) {
         onClick();
         return super.onSingleTapUp(e);
      }

      @Override
      public boolean onDoubleTap(MotionEvent e) {
         onDoubleClick();
         return super.onDoubleTap(e);
      }

      @Override
      public void onLongPress(MotionEvent e) {
         onLongClick();
         super.onLongPress(e);
      }

      @Override
      public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
         try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
               if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                  if (diffX > 0) {
                     onSwipeRight();
                  } else {
                     onSwipeLeft();
                  }
               }
            } else {
               if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                  if (diffY > 0) {
                     onSwipeDown();
                  } else {
                     onSwipeUp();
                  }
               }
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }
         return false;
      }
   }

   public void onSwipeRight() {
   }

   public void onSwipeLeft() {
   }

   private void onSwipeUp() {
   }

   private void onSwipeDown() {
   }

   private void onClick() {
   }

   private void onDoubleClick() {
   }

   private void onLongClick() {
   }
}
*/
