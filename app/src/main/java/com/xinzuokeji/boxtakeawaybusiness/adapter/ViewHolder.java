package com.xinzuokeji.boxtakeawaybusiness.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.GSApplication;


public class ViewHolder {
    // 存储View
    private SparseArray<View> mViews;
    // ???
    private View mConvertView;

    private Integer mPosition;

    // 构造函数
    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mViews = new SparseArray<View>();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
        this.mPosition = position;
    }

    // 获取ViewHolder
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            ViewHolder viewHolder = new ViewHolder(context, parent, layoutId, position);
            return viewHolder;
        } else {
            return (ViewHolder) convertView.getTag();
        }
    }

    // 获取ConvertView
    public View getConvertView() {
        return this.mConvertView;
    }

    //获得位置点
    public Integer getPosition() {
        return mPosition;
    }

    // 通过ViewId获取View
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = this.mViews.get(viewId);
        if (view == null) {
            view = this.mConvertView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }

        return (T) view;
    }

    // 设置TextView的文字
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    // 设置ImageView的图片，使用资源文件
    public ViewHolder setImage(int viewId, int resouceId) {
        ImageView view = getView(viewId);
        view.setImageResource(resouceId);
        return this;
    }

    // 设置ImageView的图片，使用Bitmap
    public ViewHolder setImage(int viewId, Bitmap image) {
        ImageView view = getView(viewId);
        view.setImageBitmap(image);
        return this;
    }

    // 设置ImageView的图片，使用ImageLoader
    public ViewHolder setImage(Context context, int viewId, String url) {
        ImageView view = getView(viewId);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url, view, GSApplication.getInstance().imageOptions);//.(view, url);
        return this;
    }
}
