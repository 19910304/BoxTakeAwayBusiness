package com.xinzuokeji.boxtakeawaybusiness.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.Attribute;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.util.List;

/**
 * Created by Administrator on 2018/11/9.
 */

public class ChangeTasteAdapter extends BaseAdapter {
    Context context;
    List<Attribute> lists;
    ACache aCache;

    public ChangeTasteAdapter(Context context, List<Attribute> lists) {
        this.context = context;
        this.lists = lists;
        aCache = ACache.get(context);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_change_taste, null);
            vh = new ViewHolder(convertView, position);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final Attribute bean = lists.get(position);
        if (Valid.isNotNullOrEmpty(lists.get(position).getContent())) {
//            Log.i("getContent", bean.getContent());
            vh.mEditText.setText(lists.get(position).getContent());
//            aCache.put(position + "口味", bean.getContent());
        } else {
//            vh.mEditText.setText("");
//            aCache.put(position + "口味", "");
        }
        vh.tvname.setText("分类" + (position + 1));
        //大部分情况下，getview中有if必须有else
//        if (!TextUtils.isEmpty(bean.getContent())) {
//            vh.mEditText.setText(bean.getContent());
//        } else {
//            vh.mEditText.setText("");
//        }

        return convertView;
    }

    class TextSwitcher implements TextWatcher {
        private ViewHolder mHolder;

        public TextSwitcher(ViewHolder mHolder) {
            this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            int position = (int) mHolder.mEditText.getTag();//取tag值
            aCache.put(position + "口味", mHolder.mEditText.getText().toString());
            Log.i("aca", position + "--" + mHolder.mEditText.getText().toString());
        }
    }

    public class ViewHolder {
        TextView tvname;
        EditText mEditText;

        public ViewHolder(View convertView, int pisition) {
            tvname = (TextView) convertView.findViewById(R.id.tv_taste_content);
            mEditText = (EditText) convertView.findViewById(R.id.et_taste_content);
            mEditText.setTag(pisition);//存tag值
            mEditText.addTextChangedListener(new TextSwitcher(this));
        }
    }

}
