package com.priyo.apps.jonopriyo.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.priyo.apps.jonopriyo.NewPollDetailsActivity;
import com.priyo.apps.jonopriyo.NewPollsActivity;
import com.priyo.apps.jonopriyo.R;
import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.jonopriyo.utility.Constants;
import com.priyo.apps.jonopriyo.utility.JonopriyoApplication;
import com.priyo.apps.lazylist.ImageLoader;

public class PollListAdapter extends ArrayAdapter<Poll> {
    
    private Context mContext;
//    private List<Poll> mPolls;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private int listType;

    public PollListAdapter(Context context, List<Poll> polls, int listType) {
        super(context, R.layout.row_polls);
        this.mContext = context;
        this.listType = listType;
//        this.mPolls = polls;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader((Activity) mContext);
    }
    
    private static class ViewHolder {
        ImageView PollImage;
        TextView PollNumber;
        TextView PollQuestion;
        TextView ParticipationCount;
        TextView Category;
        TextView ReleaseDate;
        TextView PrizeValue;
        TextView VoteCasted;
        Button VoteNow;
    }
    
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_polls, null);

            holder = new ViewHolder();
            holder.PollImage = (ImageView)convertView.findViewById(R.id.iv_poll_pic);
//            holder.PrizeImage = (ImageView)convertView.findViewById(R.id.iv_prize);
            holder.PollNumber = (TextView) convertView.findViewById(R.id.tv_poll_number);
            holder.PollQuestion = (TextView) convertView.findViewById(R.id.tv_poll_question);
            holder.ParticipationCount = (TextView) convertView.findViewById(R.id.tv_participation_count);
            holder.Category = (TextView) convertView.findViewById(R.id.tv_category);
            holder.ReleaseDate = (TextView) convertView.findViewById(R.id.tv_release_date);
            holder.PrizeValue = (TextView) convertView.findViewById(R.id.tv_prize);
            holder.VoteCasted = (TextView) convertView.findViewById(R.id.tv_voted);            
            holder.VoteNow = (Button) convertView.findViewById(R.id.b_vote_now);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.VoteNow.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                JonopriyoApplication appInstance = (JonopriyoApplication) ((Activity) mContext).getApplication();
                appInstance.setSelectedPoll(getItem(position));
                
                Intent i = new Intent(mContext, NewPollDetailsActivity.class);
                ((Activity) mContext).startActivity(i);
                ((Activity) mContext).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                
            }
        });
        
        
        
        
        Poll item = getItem(position);
        
        String imageUrl = item.getImageUrl();
        imageLoader.DisplayImage(imageUrl, holder.PollImage);
//        imageLoader.DisplayImage(item.getPollPrize().getImageUrl(), holder.PrizeImage);
        
        holder.PollNumber.setText("" + item.getNumber());
        holder.PollQuestion.setText(item.getQuestion());
        holder.ParticipationCount.setText("" + item.getParticipationCount());
        holder.Category.setText(item.getCategory() + ",");
        holder.ReleaseDate.setText(item.getReleaseDate());
        holder.PrizeValue.setText(item.getPollPrize().getValue() + " " + item.getPollPrize().getType());
        
        if(listType == Constants.PARENT_ACTIVITY_MY_POLLS){
            // if my-poll list, we wont show "VOted" message & "Vote now" button
            holder.VoteCasted.setVisibility(View.GONE);
            holder.VoteNow.setVisibility(View.GONE);
        }
        else{
        if(item.getIsCastByMe()){
            holder.VoteCasted.setVisibility(View.VISIBLE);
            holder.VoteNow.setVisibility(View.GONE);
        }
        else{
            holder.VoteCasted.setVisibility(View.GONE);
            if(item.getIsNew())
                holder.VoteNow.setVisibility(View.VISIBLE);
            else
                holder.VoteNow.setVisibility(View.GONE);
        }
        }

        return convertView;
    }    
    
    public void setData(List<Poll> pollList) {
        clear();
        if (pollList != null) {
            for (int i = 0; i < pollList.size(); i++) {
                add(pollList.get(i));
            }
        }
    }

}
