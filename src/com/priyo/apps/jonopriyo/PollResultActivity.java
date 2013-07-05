package com.priyo.apps.jonopriyo;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PollResultActivity extends Activity {




    private static long[] VALUES = new long[] { 10, 11, 12, 13, 10, 10, 10, 10};            // this controls the array size

    private static String[] NAME_LIST = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };

    private CategorySeries mSeries = new CategorySeries("");

    private DefaultRenderer mRenderer = new DefaultRenderer();

    private GraphicalView mChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_result);

        int[] COLORS = new int[] {
                getResources().getColor(R.color.crimson),
                getResources().getColor(R.color.lightpink1),
                getResources().getColor(R.color.palevioletred1),
                getResources().getColor(R.color.violetred1),
                getResources().getColor(R.color.orchid),
                getResources().getColor(R.color.violetred),
                getResources().getColor(R.color.plum2),
                getResources().getColor(R.color.magenta)
        };


        mRenderer.setApplyBackgroundColor(true);
        //        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setBackgroundColor(Color.argb(255, 255, 255, 255));
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(30);
        mRenderer.setLabelsColor(getResources().getColor(R.color.red));
        mRenderer.setLegendTextSize(30);
        mRenderer.setMargins(new int[] { 0, 0, 0, 0 });
        //        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setStartAngle(90);

        for (int i = 0; i < VALUES.length; i++) {
            mSeries.add(NAME_LIST[i] + " " + VALUES[i], VALUES[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
            mChartView.repaint();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.my_chart);
            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);

            mChartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();

                    if (seriesSelection == null) {
                        Toast.makeText(PollResultActivity.this,"No chart element was clicked",Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < mSeries.getItemCount(); i++) {
                            mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
                        }
                        mChartView.repaint();
                        Toast.makeText(PollResultActivity.this,"Chart element data point index "+ (seriesSelection.getPointIndex()+1) + " was clicked" + " point value="+ seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mChartView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                    if (seriesSelection == null) {
                        Toast.makeText(PollResultActivity.this,"No chart element was long pressed", Toast.LENGTH_SHORT).show();
                        return false; 
                    } else {
                        Toast.makeText(PollResultActivity.this,"Chart element data point index "+ seriesSelection.getPointIndex()+ " was long pressed",Toast.LENGTH_SHORT).show();
                        return true;       
                    }
                }
            });
            layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }
        else {
            mChartView.repaint();
        }
    }

}
