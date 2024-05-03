package de.fraunhofer.sit.sse.valbench.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class MainActivity extends android.app.Activity {
    TextView view;

    public static int androidres = R.raw.androidres;
    public static Resources resources;
    boolean isFalse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resources = getResources();
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.txtOutput);
        view.setText("Starting app");
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                startTests();

            }
        });

        //Just so we have an edge for tools which are not
        //capable to support threading
        if (isFalse)
            startTests();
    }

    private void startTests() {

    }

    private void handleException(Throwable t) {
        String s;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(os)) {
            t.printStackTrace(ps);
            ps.flush();
            s = new String(os.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addText(s);
    }

    private void addText(String s) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                view.setText(view.getText() + "\n" + s);
            }
        });
    }
}