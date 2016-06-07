package com.pk.tagger.maps;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.pk.tagger.activity.MapFragment;

public class TouchableWrapper extends FrameLayout {

    public TouchableWrapper(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                MapFragment.mMapIsTouched = true;
                break;

            case MotionEvent.ACTION_UP:
                MapFragment.mMapIsTouched = false;
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
