package com.yp.payment.order.layer;

import android.app.Activity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 86139
 */
public class TimerLayer {

    public interface CallbackGetCommonTimer{
        void callback();
    }

    public static Timer getCommonTimer(Activity activity, CallbackGetCommonTimer callbackGetCommonTimer){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(() -> {
                    callbackGetCommonTimer.callback();
                });
            }
        }, 1000, 1000);
        return timer;
    }

}
