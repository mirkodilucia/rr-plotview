package it.dlmrk.rrplotview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class RRTPlotView extends RRPlotView {

    public RRTPlotView(Context context, int lines, boolean maxScale) {
        super(context, lines, maxScale);
    }

    public RRTPlotView(Context context, int lines) {
        super(context, lines);
    }

    public RRTPlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RRTPlotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setLines(int lines) {
        super.setLines(lines);

        timeCount = new int[lines];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float scaleY = (maxValOff - minValOff) / gridCount;

        int width = getWidth();
        int height = getHeight();

        float horizontalOffset = (width - w_offset) / (float) gridCount;
        float verticalOffset = (height - h_offset - t_offset) / (float) gridCount;

        for (int i=0; i<legends.length; i++) {
            canvas.drawCircle((width / 5) + (i * 160) + 32, 20, 12, legends[i].paint);
            canvas.drawText(legends[i].label, (width / 5) + (i * 160) + 60, 30, blackPaint);
        }

        for (int i=0; i<gridCount; i++) {
            // VERTICALI
            if (i % gridDensity == 0) {
                canvas.drawLine(w_offset + (i * horizontalOffset), t_offset, w_offset + (i * horizontalOffset), height - h_offset, blackPaint);
            }

            // ORIZZONTALI
            canvas.drawLine(w_offset, (i + 1)  * verticalOffset + t_offset, width, (i + 1) * verticalOffset + t_offset, blackPaint);

            // VALORI TESTUALI

            // VALORI SULL'ASSE Y
            if (gridCount <= 20) {
                float value = minValOff + scaleY * i;

                // TESTO VERTICALE

                String val;
                if (value / 1000 > 1 || value * 1000 < 1) {
                    val = String.format("%1.2e", value);
                }else{
                    val = String.valueOf(Math.round(value * 100) / 100.0f);
                }
                canvas.drawText(val, 20, (gridCount - i) * verticalOffset + 12 , blackPaint);

                // VALORI SULL'ASSE X
                if (maxTimeCount > gridCount) {
                    String horValue = String.valueOf(maxTimeCount - gridCount + i);
                    canvas.drawText(String.valueOf(maxTimeCount - gridCount + i), w_offset + (i) * horizontalOffset, getHeight() - (h_offset / 2) - 12, blackPaint);
                }else{
                    String horValue = String.valueOf(i);
                    canvas.drawText(String.valueOf(i), w_offset + (i) * horizontalOffset - (15 * horValue.length() / 2f), getHeight() - (h_offset / 2) - 12, blackPaint);
                }
            }else{
                if (i % gridDensity == 0) {
                    float value = minValOff + scaleY * i;
                    String val;
                    if (value / 100 > 1 || value * 100 < 1) {
                        val = String.format("%1.2e", value);
                    }else{
                        val = String.valueOf(Math.round(value * 100) / 100.0f);
                    }
                    canvas.drawText(val, 20, (gridCount - i) * verticalOffset + 12, blackPaint);
                }


                // VALORI SULL'ASSE X
                if (maxTimeCount > gridCount) {
                    String val = String.valueOf(maxTimeCount - gridCount + i);
                    if (val.length() > 3 && i % gridDensity == 0) {
                        canvas.drawText(String.valueOf(maxTimeCount - gridCount + i), w_offset + (i) * horizontalOffset, getHeight() - (h_offset / 2) - 12, blackPaint);
                    } else if (val.length() <= 3 && i%4 == 0) {
                        canvas.drawText(String.valueOf(maxTimeCount - gridCount + i), w_offset + (i) * horizontalOffset, getHeight() - (h_offset / 2) - 12, blackPaint);
                    }
                } else {
                    String val = String.valueOf(maxTimeCount - gridCount + i);
                    if (val.length() > 3 && i % gridDensity == 0) {
                        canvas.drawText(String.valueOf(i), w_offset + (i) * horizontalOffset, getHeight() - (h_offset / 2) - 12, blackPaint);
                    } else if (val.length() <= 3 && i%4 == 0) {
                        canvas.drawText(String.valueOf(i), w_offset + (i) * horizontalOffset, getHeight() - (h_offset / 2) - 12, blackPaint);
                    }
                }
            }
        }

        // Plot point and then connect points
        for (int k=0; k< dataLines.length; k++) {
            if (dataLines[k] != null) {
                for (int j = 0; j < dataLines[k].size(); j++) {

                    float value = dataLines[k].get(j);
                    float ymed = Math.abs(minValOff * (height - h_offset) / ((maxValOff - minValOff)));
                    float y = value * (height - h_offset) / ((maxValOff - minValOff));

                    canvas.drawCircle(w_offset + (j * horizontalOffset), height - h_offset - (y) - ymed, 1, legends[k].paint);


                }
                connectPoints(canvas, dataLines[k], legends[k].paint, width, height);
            }
        }
    }

    public void addPoint(float f, int pos) {
        if (dataLines[pos].size() > gridCount) {
            dataLines[pos].remove(0);
        }
        timeCount[pos]++;
        dataLines[pos].add(f);

        calculatePointSpacing();

        for (int i = 0; i < dataLines.length; i++) {
            if (dataLines[i].isEmpty()) { continue; }

            if (maxTimeCount < timeCount[i]) {
                maxTimeCount = timeCount[i];
            }
        }

        invalidate();
    }

}
