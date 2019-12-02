package it.dlmrk.rrplotview.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import it.dlmrk.rrplotview.RRTPlotView;

public class MainActivity extends AppCompatActivity {

    private RRTPlotView plotView1;
    private RRTPlotView plotView2;

    private double[] sinPoint1 = new double[1000];
    private double[] sinPoint2 = new double[1000];

    private double[] resultP1 = new double[1024];
    private double[] resultP2 = new double[1024];

    protected static final int SAMPLE_RATE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plotView1 = findViewById(R.id.sinusoidalPlot);
        plotView1.setLines(3);

        plotView2 = findViewById(R.id.sincPlot);

        final int period = SAMPLE_RATE / 50;
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    for (int i = 0; i<sinPoint1.length; i++) {
                        double angle = 2.0 * Math.PI * i / period;

                        sinPoint1[i] = (Math.sin(angle));
                        sinPoint2[i] = (Math.sin(angle + Math.PI / 2));

                        final double finalPoint1 = sinPoint1[i];
                        final double finalPoint2 = sinPoint2[i];

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //textView.setText(String.valueOf(finalPoint));
                                plotView1.addPoint((float) finalPoint1, 0);
                                plotView1.invalidate();

                                plotView1.addPoint((float) finalPoint2, 1);
                                plotView1.invalidate();

                                plotView1.addPoint((float) finalPoint2, 1);
                                plotView1.invalidate();
                            }
                        });
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }


            }
        }).start();
    }
}
