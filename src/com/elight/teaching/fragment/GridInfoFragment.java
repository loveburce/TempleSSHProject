package com.elight.teaching.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.elight.teaching.R;
import com.elight.teaching.adapter.GridShowAdapter;
import com.elight.teaching.entity.IndexGalleryItemData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/9/4.
 */
public class GridInfoFragment extends BaseFragment {

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new GridInfoFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }

    ArrayList<IndexGalleryItemData> dataArrayList = new ArrayList<IndexGalleryItemData>();
    View rootView;
    GridView gridView;
    GridShowAdapter gridShowAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_grid_show,null);
        initData();
        gridView = (GridView) rootView.findViewById(R.id.GridView);
        gridShowAdapter = new GridShowAdapter(getActivity());
        gridShowAdapter.addItems(dataArrayList);
        gridView.setAdapter(gridShowAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void initData(){
        dataArrayList.add(new IndexGalleryItemData(1, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(2, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(3, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(4, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(5, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(6, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(7, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(8, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(9, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(10, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(11, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(12, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(13, R.drawable.head,"三国演义"));
        dataArrayList.add(new IndexGalleryItemData(14, R.drawable.head,"三国演义"));

    }

}
