package it.casaricci.bpmcounter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

public class BeaterActivity extends Activity {

    private static final int MAX_WAIT = 2000;

    private TextView mBeats;

    private long mPreviousBeat;
    // 10 last differences
    private long[] mLastBeats = new long[10];
    private int mCurrentBeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beater);

        mBeats = (TextView) findViewById(R.id.text_beat);

        findViewById(R.id.button_beat).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (mPreviousBeat > 0) {
                        long beat = System.currentTimeMillis();
                        long diff = beat - mPreviousBeat;

                        // reset
                        if (diff >= MAX_WAIT) {
                            Arrays.fill(mLastBeats, 0);
                        }

                        else {
                            mLastBeats[mCurrentBeat] = diff;
                            mCurrentBeat++;
                            if (mCurrentBeat >= mLastBeats.length)
                                mCurrentBeat = 0;
                        }

                    }

                    if (mLastBeats[0] > 0) {
                        long avgDiff = avg(mLastBeats);
                        final int bpm = (int) (60000 / avgDiff);
                        mBeats.setText(String.format("%d BPM", bpm));
                    }

                    mPreviousBeat = System.currentTimeMillis();

                    return true;
                }

                return false;
            }
        });
    }

    private long avg(long[] values) {
        long sum = 0;
        long count = 0;
        for (long v : values) {
            if (v > 0) {
                sum += v;
                count++;
            }
        }

        return sum / count;
    }
}
