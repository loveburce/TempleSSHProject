package com.elight.teaching.custom.xspinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.elight.teaching.R;

import java.util.List;

/**
 * Created by dawn on 2014/9/26.
 */
public class SpinnerPopWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private AbstractSpinnerAdapter mAdapter;
    private AbstractSpinnerAdapter.IOnItemSelectListener mItemSelectListener;

    public SpinnerPopWindow(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public void setItemSelectListener(AbstractSpinnerAdapter.IOnItemSelectListener listener){
        mItemSelectListener = listener;
    }

    public void setAdapter(AbstractSpinnerAdapter adapter){
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }

    private void init(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.spinner_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        ColorDrawable drawable = new ColorDrawable(0x00);
        setBackgroundDrawable(drawable);

        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setOnItemClickListener(this);
    }

    public <T> void refreshData(List<T> list, int selIndex){
        if(list != null && selIndex != -1){
            if(mAdapter != null){
                mAdapter.refreshData(list, selIndex);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        if(mItemSelectListener != null){
            mItemSelectListener.onItemClick(position);
        }
    }
}
