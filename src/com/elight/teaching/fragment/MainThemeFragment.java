package com.elight.teaching.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.elight.teaching.R;
import com.elight.teaching.activity.ThemeMusicActivity;
import com.elight.teaching.config.Constants;
import com.elight.teaching.custom.DraggableGridViewPager;
import com.elight.teaching.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/9/3.
 */
public class MainThemeFragment extends BaseFragment{

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new MainThemeFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }

    private View layoutView;

    private DraggableGridViewPager draggableGridViewPager;
    private ArrayAdapter<String> arrayAdapter;
    private LayoutInflater layoutInflater;
    private int gridCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_main, null);

        findViewById();
        initData();
        setListener();

        return layoutView;
    }

    private void findViewById(){
        draggableGridViewPager = (DraggableGridViewPager) layoutView.findViewById(R.id.draggable_grid_view_pager);
        arrayAdapter = new ArrayAdapter<String>(getActivity(), 0){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final String text= getItem(position);
                if(convertView == null){
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_main_item,null);
                }
                ((TextView) convertView).setText(text);
                return convertView;
            }
        };
        arrayAdapter.addAll(Constants.listTitle);
    }

    private void initData(){
        draggableGridViewPager.setAdapter(arrayAdapter);

    }

    private void setListener(){
        draggableGridViewPager.setOnPageChangeListener(new DraggableGridViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        draggableGridViewPager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityUtils.showShortToast(getActivity(), ((TextView)view).getText().toString());

                Intent intent = new Intent(getActivity(), ThemeMusicActivity.class);
                intent.putExtra("position",position);
                getActivity().startActivity(intent);

            }
        });

        draggableGridViewPager.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });

        draggableGridViewPager.setOnRearrangeListener(new DraggableGridViewPager.OnRearrangeListener() {
            @Override
            public void onRearrange(int oldIndex, int newIndex) {
                String item = arrayAdapter.getItem(oldIndex);
                arrayAdapter.setNotifyOnChange(false);
                arrayAdapter.remove(item);
                arrayAdapter.insert(item, newIndex);
                arrayAdapter.notifyDataSetChanged();
            }
        });

    }

}
