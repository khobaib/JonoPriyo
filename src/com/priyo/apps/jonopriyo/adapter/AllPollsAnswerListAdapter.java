package com.priyo.apps.jonopriyo.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.priyo.apps.jonopriyo.R;
import com.priyo.apps.jonopriyo.model.PollAnswer;
import com.priyo.apps.lazylist.ImageLoader;

public class AllPollsAnswerListAdapter extends ArrayAdapter<PollAnswer> {

    private Context mContext;
//    private List<PollAnswer> mPollAnswers;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    
//    private RadioButton mSelectedRB;
//    private int mSelectedPosition;
    
    public AllPollsAnswerListAdapter(Context context, List<PollAnswer> pollAnswers) {
        super(context, R.layout.row_poll_answer, pollAnswers);
        this.mContext = context;
//        this.mPollAnswers = pollAnswers;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader((Activity) mContext);
//        mSelectedRB = null;
//        mSelectedPosition = -1;
    }
    
    private static class ViewHolder {
        ImageView PollAnswerImage;
        TextView PollAnswer;
        RadioButton rbAnswer;
    }
    
//    @Override
//    public boolean areAllItemsEnabled() {
//        return false;
//    }
    
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        View view = convertView;
        ViewHolder holder;

        if(convertView == null){

            convertView = mInflater.inflate(R.layout.row_poll_answer, null);
            holder = new ViewHolder();

            holder.PollAnswerImage = (ImageView)convertView.findViewById(R.id.iv_pollanswer_pic);
            holder.PollAnswer = (TextView)convertView.findViewById(R.id.tv_poll_answer);
            holder.rbAnswer = (RadioButton)convertView.findViewById(R.id.rb_answer);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        

        holder.PollAnswer.setText(getItem(position).getAnswer());
        String imageUrl = getItem(position).getAnswerImage();
        imageLoader.DisplayImage(imageUrl, holder.PollAnswerImage);
        
        holder.rbAnswer.setVisibility(View.GONE);


//        holder.rbAnswer.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if(position != mSelectedPosition && mSelectedRB != null){
//                    mSelectedRB.setChecked(false);
//                }
//
//                mSelectedPosition = position;
//                mSelectedRB = (RadioButton)v;
//            }
//        });


//        if(mSelectedPosition != position){
//            holder.rbAnswer.setChecked(false);
//        }else{
//            holder.rbAnswer.setChecked(true);
//            if(mSelectedRB != null && holder.rbAnswer != mSelectedRB){
//                mSelectedRB = holder.rbAnswer;
//            }
//        }

        return convertView;
    }
}
