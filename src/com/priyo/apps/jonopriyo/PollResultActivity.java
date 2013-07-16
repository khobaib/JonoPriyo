package com.priyo.apps.jonopriyo;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.model.PollResult;
import com.priyo.apps.jonopriyo.model.ServerResponse;
import com.priyo.apps.jonopriyo.parser.JsonParser;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PollResultActivity extends Activity {




//    private static long[] VALUES = new long[] { 10, 11, 12, 13, 10, 10, 10, 10};            // this controls the array size
//
//    private static String[] NAME_LIST = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };

    private CategorySeries mSeries = new CategorySeries("");

    private DefaultRenderer mRenderer = new DefaultRenderer();

    private GraphicalView mChartView;

    private ProgressDialog pDialog;
    JsonParser jsonParser;
    JonopriyoApplication appInstance;

    List<PollResult> pollResultList;
    Poll thisPoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_result);

        jsonParser = new JsonParser();
        pDialog = new ProgressDialog(PollResultActivity.this);

        appInstance = (JonopriyoApplication) getApplication();
        thisPoll = appInstance.getSelectedPoll();
        
        new RetrievePollResult().execute();

//        int[] COLORS = new int[] {
//                getResources().getColor(R.color.crimson),
//                getResources().getColor(R.color.lightpink1),
//                getResources().getColor(R.color.palevioletred1),
//                getResources().getColor(R.color.violetred1),
//                getResources().getColor(R.color.orchid),
//                getResources().getColor(R.color.violetred),
//                getResources().getColor(R.color.plum2),
//                getResources().getColor(R.color.magenta)
//        };
//
//
//        mRenderer.setApplyBackgroundColor(true);
//        //        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
//        mRenderer.setBackgroundColor(Color.argb(255, 255, 255, 255));
//        mRenderer.setChartTitleTextSize(20);
//        mRenderer.setLabelsTextSize(30);
//        mRenderer.setLabelsColor(getResources().getColor(R.color.red));
//        mRenderer.setLegendTextSize(30);
//        mRenderer.setMargins(new int[] { 0, 0, 0, 0 });
//        //        mRenderer.setZoomButtonsVisible(true);
//        mRenderer.setStartAngle(90);
//
//        for (int i = 0; i < VALUES.length; i++) {
//            mSeries.add(NAME_LIST[i] + " " + VALUES[i], VALUES[i]);
//            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
//            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
//            mRenderer.addSeriesRenderer(renderer);
//        }
//
//        if (mChartView != null) {
//            mChartView.repaint();
//        }
    }


    private void createChart(){
        List<String> pollAnswerList = new ArrayList<String>();
        List<Long> pollCastCount = new ArrayList<Long>();

        int resultNumberCount = pollResultList.size();
        for(int i = 0; i< resultNumberCount; i++){
            pollAnswerList.add(pollResultList.get(i).getAnswer());
            pollCastCount.add(pollResultList.get(i).getCastCount());
        }

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

        for (int i = 0; i < resultNumberCount; i++) {
            mSeries.add(pollAnswerList.get(i) + " " + pollCastCount.get(i), pollCastCount.get(i));
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
            mChartView.repaint();
        }



        if (mChartView == null) {
            Log.d("<<<<<<<<<<<", "drawing my chart");
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


    //    @Override
    //    protected void onResume() {
    //        super.onResume();
    //        if (mChartView == null) {
    //            LinearLayout layout = (LinearLayout) findViewById(R.id.my_chart);
    //            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
    //            mRenderer.setClickEnabled(true);
    //            mRenderer.setSelectableBuffer(10);
    //
    //            mChartView.setOnClickListener(new View.OnClickListener() {
    //                @Override
    //                public void onClick(View v) {
    //                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
    //
    //                    if (seriesSelection == null) {
    //                        Toast.makeText(PollResultActivity.this,"No chart element was clicked",Toast.LENGTH_SHORT).show();
    //                    } else {
    //                        for (int i = 0; i < mSeries.getItemCount(); i++) {
    //                            mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
    //                        }
    //                        mChartView.repaint();
    //                        Toast.makeText(PollResultActivity.this,"Chart element data point index "+ (seriesSelection.getPointIndex()+1) + " was clicked" + " point value="+ seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
    //                    }
    //                }
    //            });
    //
    //            mChartView.setOnLongClickListener(new View.OnLongClickListener() {
    //                @Override
    //                public boolean onLongClick(View v) {
    //                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
    //                    if (seriesSelection == null) {
    //                        Toast.makeText(PollResultActivity.this,"No chart element was long pressed", Toast.LENGTH_SHORT).show();
    //                        return false; 
    //                    } else {
    //                        Toast.makeText(PollResultActivity.this,"Chart element data point index " + seriesSelection.getPointIndex()+ " was long pressed",Toast.LENGTH_SHORT).show();
    //                        return true;       
    //                    }
    //                }
    //            });
    //            layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    //        }
    //        else {
    //            mChartView.repaint();
    //        }
    //    }


    public class RetrievePollResult extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("Retrieving poll data, Please wait...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("MARKER", "reached this point");
            String rootUrl = Constants.URL_ROOT;

            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
            urlParam.add(new BasicNameValuePair("method", Constants.METHOD_GET_POLL_RESULT));
            urlParam.add(new BasicNameValuePair("poll_id", "" + thisPoll.getId()));

            String token = appInstance.getAccessToken();

            ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, rootUrl,
                    urlParam, null, token);
            if(response.getStatus() == 200){
                Log.d(">>>><<<<", "success in login");
                JSONObject responsObj = response.getjObj();
                try {
                    JSONArray resultArray = responsObj.getJSONArray("poll_result");
                    pollResultList = PollResult.parsePollResultList(resultArray.toString());
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean success) {
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(success){
                Log.d(">>>>>>>>>>>", "success");
                if(pollResultList.size() == 0){
                    Toast.makeText(PollResultActivity.this, "No vote cast yet.", Toast.LENGTH_SHORT).show();
                }
                else{
                    createChart();
                }

            }
            else{
            }

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PollResultActivity.this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
