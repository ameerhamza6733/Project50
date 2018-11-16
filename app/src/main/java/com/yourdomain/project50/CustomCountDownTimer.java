package com.yourdomain.project50;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public abstract class CustomCountDownTimer {

    private final String TAG = "CustomCountDownTimer";
    /**
     * Millis since epoch when alarm should stop.
     */

    private final long mMillisInFuture;
    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountdownInterval;

    /**
     * The time in millis when the timer was paused
     */
    private long mTimePaused;

    /**
     * The final time when the timer must to stop(actual hour + countdown in millis)
     */
    private long mStopTimeInFuture;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;

    /**
     * boolean representing if the timer is paused
     */
    private boolean mPause = false;


    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CustomCountDownTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * Pause the countdown.
     */
    public synchronized final void pause() {
        cancel();
        //Save the time and hour to resume the timer correctly later.
        mTimePaused = SystemClock.elapsedRealtime();
        mPause = true;
    }

    /**
     * Resume the countdown.
     */
    public synchronized final void resume() {
        //Booleans back to false value
        mPause = false;
        mCancelled = false;
        //We set the time to a new one cause the elapsedTime as change
        mStopTimeInFuture = mStopTimeInFuture + (SystemClock.elapsedRealtime() - mTimePaused);
        Log.d(TAG, "mStopTimeInFuture: " + mStopTimeInFuture);
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
    }

    /**
     * Start the countdown.
     */
    public synchronized final CustomCountDownTimer start() {
        mCancelled = false;
        mPause = false;
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }


    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();


    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CustomCountDownTimer.this) {
                if (mCancelled || mPause) {
                    return;
                }

                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();
                Log.d(TAG, "millisLeft: " + millisLeft);
                if (millisLeft <= 0) {
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    // take into account user's onTick taking time to execute
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();

                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval;

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}