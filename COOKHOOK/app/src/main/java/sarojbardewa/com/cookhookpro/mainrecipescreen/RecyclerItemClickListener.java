package sarojbardewa.com.cookhookpro.mainrecipescreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * This is the onClick listenere method.
 */
public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    /**
     * OnClick listener for the recycler view
     * @param context
     * @param listener
     */

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    /**
     * Implement this method to intercept all touch screen motion events.
     * This allows you to watch events as they are dispatched to your children,
     * and take ownership of the current gesture at any point.
     * @param view
     * @param e
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        if(mGestureDetector.onTouchEvent(e)) {
            View theView = view.findChildViewUnder(e.getX(), e.getY());
            if(mListener != null && theView != null)
                mListener.onItemClick(theView, view.getChildAdapterPosition(theView));
            return true;
        }
        return false;
    }

}