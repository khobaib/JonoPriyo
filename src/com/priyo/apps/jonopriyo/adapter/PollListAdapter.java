package com.priyo.apps.jonopriyo.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.priyo.apps.jonopriyo.R;
import com.priyo.apps.jonopriyo.model.Poll;
import com.priyo.apps.lazylist.ImageLoader;

public class PollListAdapter extends ArrayAdapter<Poll> {
    
    private Context mContext;
    private List<Poll> mPolls;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;

    public PollListAdapter(Context context, List<Poll> polls) {
        super(context, R.layout.row_polls);
        this.mContext = context;
        this.mPolls = polls;
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
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_polls, null);

            holder = new ViewHolder();
            holder.PollImage = (ImageView)convertView.findViewById(R.id.iv_poll_pic);;
            holder.PollNumber = (TextView) convertView.findViewById(R.id.tv_poll_number);
            holder.PollQuestion = (TextView) convertView.findViewById(R.id.tv_poll_question);
            holder.ParticipationCount = (TextView) convertView.findViewById(R.id.tv_participation_count);
            holder.Category = (TextView) convertView.findViewById(R.id.tv_category);
            holder.ReleaseDate = (TextView) convertView.findViewById(R.id.tv_release_date);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Poll item = getItem(position);
        
        String imageUrl = item.getImageUrl();
        imageLoader.DisplayImage(imageUrl, holder.PollImage);
        
        holder.PollNumber.setText("Poll#:" + item.getNumber());
        holder.PollQuestion.setText(item.getQuestion());
        holder.ParticipationCount.setText("" + item.getParticipationCount());
        holder.Category.setText(item.getCategory());
        holder.ReleaseDate.setText(item.getReleaseDate());

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
