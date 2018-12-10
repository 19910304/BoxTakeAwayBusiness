package com.xinzuokeji.boxtakeawaybusiness.entities;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.convenientbanner.holder.Holder;

/**
 *
 */
public class NetworkImageHolderView implements Holder<StoreOperations_Banner> {

//    private BaseServices services = new BaseServices();
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, StoreOperations_Banner data) {
        imageView.setImageResource(R.mipmap.store_main);
        ImageLoader.getInstance().displayImage("", imageView, GSApplication.getInstance().imageOptions);
    }
}
