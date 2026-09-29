package com.mehdi.heavyequipment;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Game3DView gameView = new Game3DView(this);
        setContentView(gameView);
    }
}
