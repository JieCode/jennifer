package com.jennifer.jennifer.ui;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.nd.slp.tp.sdk.LoginActivity;

public class SLPLoginActivity extends LoginActivity {
    @Override
    public void onClickLogin(View view) {
//        super.onClickLogin(view);
        Log.e("SLPLoginActivity", "onClickLogin: ");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("SLPLoginActivity", "onTouchEvent:" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("SLPLoginActivity", "onTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("SLPLoginActivity", "onTouchEvent: ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("SLPLoginActivity", "onTouchEvent: ACTION_MOVE");
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e("SLPLoginActivity", "dispatchTouchEvent ACTION_DOWN");
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            Log.e("SLPLoginActivity", "dispatchTouchEvent superDispatchTouchEvent");
            return true;
        }
        Log.e("SLPLoginActivity", "dispatchTouchEvent onTouchEvent");
        return onTouchEvent(ev);
    }
}
