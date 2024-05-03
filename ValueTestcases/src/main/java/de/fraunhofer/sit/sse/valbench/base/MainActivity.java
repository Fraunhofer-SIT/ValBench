package de.fraunhofer.sit.sse.valbench.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class gets replaced from the ValBench-Base app.
 */
public class MainActivity extends android.app.Activity {
    TextView view;
    
    public static int androidres = R.raw.androidres;
    public static Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resources = getResources();
        setContentView(R.layout.activity_main);

        view = (TextView) findViewById(R.id.txtOutput);
        view.setText("Starting app");
        
    }

    private void startTests() {

    }

    private void setText(String s) {
    }
}