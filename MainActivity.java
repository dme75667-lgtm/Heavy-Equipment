package com.mehdi.heavyequipment;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    private Game3DView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new Game3DView(this);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.onResume();
    }
}
