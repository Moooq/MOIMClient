package com.jammy.mchsclient.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jammy.mchsclient.R;
import com.jammy.mchsclient.activity.CircleActivity;
import com.jammy.mchsclient.model.Moment;
import com.jammy.mchsclient.url.API;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.jammy.mchsclient.MyApplication.userOnLine;

/**
 * Created by moqiandemac on 2018/3/11.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private PopupWindow mMorePopupWindow;
    private int mShowMorePopupWindowWidth;
    private int mShowMorePopupWindowHeight;
    private List<Moment> mMoments;
    private Context mContext;
    private LayoutInflater inflater;

    public MyRecyclerAdapter(Context context, List<Moment> datas){
        this. mContext=context;
        this. mMoments=datas;
        inflater=LayoutInflater. from(mContext);
    }

    @Override
    public int getItemCount() {

        return mMoments.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvMomentName.setText( mMoments.get(mMoments.size()-position-1).getUsername());
        holder.tvMomentContent.setText(mMoments.get(mMoments.size()-position-1).getMomentcontent());
        holder.tvMomentLocation.setText(mMoments.get(mMoments.size()-position-1).getLocation());
        holder.tvMomentTime.setText(mMoments.get(mMoments.size()-position-1).getTime());
        Picasso.with(mContext)
                .load(API.HEAD_PATH+mMoments.get(mMoments.size()-position-1).getUsername()+"_Head.png")
                .fit()
                .error(R.drawable.head)
                .into(holder.ivMomentHead);
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_circle,parent, false);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMomentName;
        TextView tvMomentContent;
        TextView tvMomentTime;
        TextView tvMomentLocation;
        ImageView ivMomentHead;
        ImageButton ibtnMomentMore;

        public MyViewHolder(View view) {
            super(view);
            tvMomentName=(TextView) view.findViewById(R.id. tv_moment_name);
            tvMomentContent=(TextView)view.findViewById(R.id.tv_moment_content);
            tvMomentTime=(TextView)view.findViewById(R.id.tv_moment_time);
            tvMomentLocation=(TextView)view.findViewById(R.id.tv_moment_location);
            ivMomentHead = (ImageView)view.findViewById(R.id.iv_moment_head);
            ibtnMomentMore = (ImageButton)view.findViewById(R.id.ibtm_moment_more);
            ibtnMomentMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMore(v);
                }
            });

        }

    }

    private void showMore(View moreBtnView) {
        if (mMorePopupWindow == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = li.inflate(R.layout.layout_moment_more, null, false);
            mMorePopupWindow = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mMorePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mMorePopupWindow.setOutsideTouchable(true);
            mMorePopupWindow.setTouchable(true);
            content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mShowMorePopupWindowWidth = content.getMeasuredWidth();
            mShowMorePopupWindowHeight = content.getMeasuredHeight();
            View parent = mMorePopupWindow.getContentView();
            TextView like = (TextView) parent.findViewById(R.id.tv_moment_more_like);
            TextView comment = (TextView) parent.findViewById(R.id.tv_moment_more_comment);
            // 点赞的监听器
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    alert.setTitle(R.string.like);
                    alert.setNegativeButton(R.string.cancel, null);
                    alert.show();
                }
            });
            // 评论的监听器
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    alert.setTitle(R.string.comment);
                    alert.setNegativeButton(R.string.cancel, null);
                    alert.show();
                }
            });
        }
        if (mMorePopupWindow.isShowing()) {
            mMorePopupWindow.dismiss();
        } else {
            int heightMoreBtnView = moreBtnView.getHeight();
            mMorePopupWindow.showAsDropDown(moreBtnView, -mShowMorePopupWindowWidth,
                    -(mShowMorePopupWindowHeight + heightMoreBtnView) / 2);
        }
    }

    public String DateToString(Date date){
        String sdate=null;
        DateFormat format1= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat format2= new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        long  between = curDate.getTime() - date.getTime();
        if(between > (24* 3600000)){
            sdate = format1.format(date);
        }else{
            sdate = format2.format(date);
        }
            return sdate;
    }
}