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
import com.priyo.apps.jonopriyo.model.Winner;
import com.priyo.apps.lazylist.ImageLoader;

public class WinnerListAdapter extends ArrayAdapter<Winner>{
    
    private Context mContext;
    private List<Winner> mWinners;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;

    public WinnerListAdapter(Context context, List<Winner> winners) {
        super(context, R.layout.row_winners);
        this.mContext = context;
        this.mWinners = winners;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader((Activity) mContext);
    }
    
    
    private static class ViewHolder {
        TextView PollQuestion;
        TextView PollNumber;
        TextView PollWinner;
        TextView WinnerAddress;
        ImageView WinnerPic;
        TextView PrizeVal;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_winners, null);

            holder = new ViewHolder();
            holder.PollQuestion = (TextView) convertView.findViewById(R.id.tv_poll);
            holder.PollNumber = (TextView) convertView.findViewById(R.id.tv_poll_number);
            holder.PollWinner = (TextView) convertView.findViewById(R.id.tv_winner_name);
            holder.WinnerAddress = (TextView) convertView.findViewById(R.id.tv_winner_address);
            holder.WinnerPic = (ImageView) convertView.findViewById(R.id.iv_winner_pic);
            holder.PrizeVal = (TextView) convertView.findViewById(R.id.tv_prize);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Winner item = getItem(position);
        
        holder.PollQuestion.setText(item.getPollQuestion());
        holder.PollNumber.setText("" + item.getPollNumber());
        holder.PollWinner.setText(item.getWinnerName());
        holder.WinnerAddress.setText(item.getAddress());
        holder.PrizeVal.setText("Won " + item.getPrizeValue() + " " + item.getPrizeType());
        
        String imageUrl = item.getWinnerPicUrl();
        imageLoader.DisplayImage(imageUrl, holder.WinnerPic);

        return convertView;
    }    
    
    public void setData(List<Winner> winnerList) {
        clear();
        if (winnerList != null) {
            for (int i = 0; i < winnerList.size(); i++) {
                add(winnerList.get(i));
            }
        }
    }

}
