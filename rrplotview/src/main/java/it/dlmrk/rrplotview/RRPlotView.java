package it.dlmrk.rrplotview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

public class RRPlotView extends View {

    protected boolean maxScale;

    protected Paint blackPaint;
    protected int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.RED };

    protected ArrayList<Float>[] dataLines;
    protected int[] timeCount;

    protected int gridCount = 80;
    protected int gridDensity = 10;

    protected float t_offset = 40;

    protected Legend[] legends = new Legend[]{
            new Legend("Data 1", new Paint()),
            new Legend("Data 2", new Paint()),
    };

    protected float maxVal;
    protected float minVal;

    protected float minValOff;
    protected float maxValOff;

    protected float w_offset = 130;
    protected float h_offset = 80;
    protected int maxTimeCount = 0;

    public RRPlotView(Context context) {
        this(context, null);
    }

    public RRPlotView(Context context, int lines, boolean maxScale) {
        this(context, null);
    }

    public RRPlotView(Context context, int lines) {
        this(context, null);

        commonInit(lines);
    }

    public RRPlotView(Context context, AttributeSet attrs) {
        super(context, attrs);

        commonInit(1);
    }

    public RRPlotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        commonInit(1);
    }

    private void commonInit(int lines) {

        legends = new Legend[lines];
        timeCount = new int[lines];
        dataLines = new ArrayList[lines];

        for (int i=0; i<lines; i++) {

            Paint p = new Paint();
            p.setColor(colors[ i % colors.length ]);
            p.setStrokeWidth(2);
            p.setTextSize(12);

            legends[i] = new Legend("Data 1", p);

            dataLines[i] = new ArrayList<>();
        }

        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStrokeWidth(1);
        blackPaint.setTextSize(24);

    }

    public void setLines(int lines) {

        dataLines = new ArrayList[lines];
        legends = new Legend[lines];
        for (int i=0; i<lines; i++) {

            Paint p = new Paint();
            p.setColor(colors[ i % colors.length ]);
            p.setStrokeWidth(2);
            p.setTextSize(12);

            legends[i] = new RRStaticPlotView.Legend("Data " + i + 1, p);

            dataLines[i] = new ArrayList<>();
        }

    }

    public void applyLegend(RRStaticPlotView.Legend legend[]) {
        this.legends = legend;

        int lines = legend.length;

        dataLines = new ArrayList[lines];

        for (int i=0; i<lines; i++) {
            if (legend[i].paint == null) {
                Paint p = new Paint();
                p.setColor(colors[i % colors.length]);
                p.setStrokeWidth(2);
                p.setTextSize(12);

                legend[i].paint = p;
            }else{
                legend[i].paint.setStrokeWidth(2);
                legend[i].paint.setTextSize(12);
            }

            dataLines[i] = new ArrayList<>();
            //addPoints(legend[i].values, i);
        }

        calculatePointSpacing();
        invalidate();
    }

    protected void calculatePointSpacing() {

        maxVal = Collections.max(dataLines[0]);
        minVal = Collections.min(dataLines[0]);

        for (int i=0; i<dataLines.length; i++) {
            if (dataLines[i].size() == 0) { continue; }

            float tmpMaxVal = Collections.max(dataLines[i]);
            float tmpMinVal = Collections.min(dataLines[i]);


            if (maxVal < tmpMaxVal) {
                maxVal = tmpMaxVal;
            }

            if (minVal > tmpMinVal) {
                minVal = tmpMinVal;
            }

            if (Math.abs(maxVal - minVal) == 0) {
                maxVal += 1;
                minVal -= 1;
            }

        }
        minValOff = minVal - ((maxVal - minVal) / 4);
        maxValOff = maxVal + ((maxVal - minVal) / 4);

    }

    protected void connectPoints(Canvas canvas, ArrayList<Float> point, Paint paint, int width, int height) {

        float horizontalOffset = (width - w_offset) / (float) gridCount;

        for (int i = 0; i < point.size() - 1; i++) {

            float value1 = point.get(i);

            float ymed = Math.abs(minValOff * (height - h_offset) / ((maxValOff - minValOff)));

            float x1 = w_offset + (i * horizontalOffset);
            float y1 = 0;

            if (maxScale) {
                y1 = height - h_offset - ((value1 * (height - h_offset) / ((maxValOff - minValOff))) - ymed);
            }else{
                y1 = height - h_offset - (value1 * (height - h_offset) / ((maxValOff - minValOff))) - ymed;
            }

            float value2 = point.get(i + 1);

            float x2 = w_offset + ((i + 1) * horizontalOffset);
            float y2 = 0;
            if (maxScale) {
                y2 = height - h_offset - ((value2 * (height - h_offset) / ((maxValOff - minValOff))) - ymed);
            }else{
                y2 = height - h_offset - (value2 * (height - h_offset) / ((maxValOff - minValOff))) - ymed;
            }

            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }


    public static class Legend {
        String label;
        Paint paint;
        double[] values;

        public Legend(String label, Paint paint) {
            this.label = label;
            this.paint = paint;
        }

        public Legend(String label) {
            this.label = label;
        }

        public Legend(String label, double[] values) {
            this.label = label;
            this.values = values;
        }

        public Legend(String label, double[] values, int color) {
            this.label = label;

            Paint p = new Paint();
            p.setColor(color);

            this.paint = p;
            this.values = values;
        }
    }

}
