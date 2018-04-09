package com.keyboardheight.demo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keyboardheight.library.KeyboardHeightObserver;
import com.keyboardheight.library.KeyboardHeightProvider;

public class MainActivity extends AppCompatActivity implements KeyboardHeightObserver {

    /**
     * Tag for logging
     */
    private final static String TAG = "sample_MainActivity";

    /**
     * The keyboard height provider
     */
    private KeyboardHeightProvider keyboardHeightProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyboardHeightProvider = new KeyboardHeightProvider(this);

        // make sure to start the keyboard height provider after the onResume
        // of this activity_main. This is because a popup window must be initialised
        // and attached to the activity_main root view.
        View view = findViewById(R.id.activitylayout);
        view.post(new Runnable() {
            @Override
            public void run() {
                keyboardHeightProvider.start();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
        keyboardHeightProvider.setKeyboardHeightObserver(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        keyboardHeightProvider.setKeyboardHeightObserver(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        keyboardHeightProvider.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

        String or = orientation == Configuration.ORIENTATION_PORTRAIT ? "portrait" : "landscape";
        Log.i(TAG, "onKeyboardHeightChanged in pixels: " + height + " " + or);

        TextView tv = (TextView) findViewById(R.id.height_text);
        tv.setText(height + "  |  " + or);

        // color the keyboard height view, this will stay when you close the keyboard
        View view = findViewById(R.id.keyboard);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.height = height;
    }

}