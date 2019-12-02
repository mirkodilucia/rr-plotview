package it.dlmrk.rrplotview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

public class RRStaticPlotView extends RRPlotView {

    public RRStaticPlotView(Context context) {
        super(context);
    }

    public RRStaticPlotView(Context context, int lines, boolean maxScale) {
        super(context, lines, maxScale);
    }

    public RRStaticPlotView(Context context, int lines) {
        super(context, lines);
    }

    public RRStaticPlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RRStaticPlotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
            if (i % gridDensity == 0) {
                // VERTICALI
                canvas.drawLine(w_offset + (i * horizontalOffset), t_offset, w_offset + (i * horizontalOffset), height - h_offset - t_offset, blackPaint);

                // ORIZZONTALI
                canvas.drawLine(w_offset, (i + 1) * verticalOffset + t_offset, width, (i + 1) * verticalOffset + t_offset, blackPaint);

                // TESTO VERTICALE
                float value = minValOff + scaleY * i;

                String verValue;
                if (value / 1000 > 1 || value * 1000 < 1) {
                    verValue = String.format("%1.2e", value);
                }else{
                    verValue = String.valueOf(Math.round(value * 100) / 100.0f);
                }
                canvas.drawText(verValue, 20, (gridCount - i) * verticalOffset + 12 + t_offset, blackPaint);

                // TESTO ORIZZONTALE
                String horValue = String.valueOf(i);
                canvas.drawText(horValue, w_offset + (i) * horizontalOffset - (15 * horValue.length() / 2f), getHeight() - (h_offset / 2) - (12), blackPaint);
            }
        }

        // Plot point and then connect points
        for (int k=0; k< dataLines.length; k++) {
            if (dataLines[k] != null) {
                for (int j = 0; j < dataLines[k].size(); j++) {
                    Log.d("POINTED", "POINTED");

                    float value = dataLines[k].get(j);
                    float ymed = Math.abs(minValOff * (height - h_offset) / ((maxValOff - minValOff)));
                    float y = value * (height - h_offset) / ((maxValOff - minValOff));

                    canvas.drawCircle(w_offset + (j * horizontalOffset), height - h_offset - (y) - ymed, 1, legends[k].paint);


                }
                connectPoints(canvas, dataLines[k], legends[k].paint, width, height);
            }
        }
    }

}
