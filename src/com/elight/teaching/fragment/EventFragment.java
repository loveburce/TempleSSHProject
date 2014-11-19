package com.elight.teaching.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.elight.teaching.R;

/**
 * Created by dawn on 2014/10/20.
 */
public class EventFragment extends BaseFragment implements View.OnClickListener{

    public static BaseFragment newInstance(int index){
        BaseFragment fragment = new EventListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index",index);
        fragment.setArguments(bundle);
        fragment.setIndex(index);
        return fragment;
    }

    private View layoutView;
    private View viewEventList;

    private TextView textViewTitle;
    private ImageView imageViewAdd;

    private boolean flagAdd = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_event, null);
        viewEventList = layoutView.findViewById(R.id.fragment_event_list_topbar);

        layoutView.findViewById(R.id.fragment_event_list_topbar_addEvent).setOnClickListener(this);
        textViewTitle = (TextView) layoutView.findViewById(R.id.fragment_event_list_topbar_title);
        imageViewAdd = (ImageView) layoutView.findViewById(R.id.fragment_event_list_topbar_addEvent);

        if(savedInstanceState == null){
            BaseFragment fragment = (BaseFragment) getChildFragmentManager().findFragmentByTag(0+"");
            if(fragment == null){
                initFragment(0);
            }
        }

        return layoutView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fragment_event_list_topbar_addEvent){
            if(!flagAdd){
                flagAdd = true;
                textViewTitle.setText("添加新动态");
                imageViewAdd.setImageResource(R.drawable.fragment_event_add_head_publish);
                placeView(1);
            }else{
                flagAdd = false;
                textViewTitle.setText("好友新动态");
                imageViewAdd.setImageResource(R.drawable.ic_action_edit);
                placeView(0);
            }
        }
        v.setSelected(true);
    }

    private void initFragment(int index){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.fragment_event_content, EventListFragment.newInstance(index), index+"");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void placeView(int index){
        BaseFragment fragment = (BaseFragment) getChildFragmentManager().findFragmentByTag(index+"");
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        if(fragment == null){
            switch (index) {
                case 0:
                    fragment = EventListFragment.newInstance(index);
                    break;
                case 1:
                    fragment = EventAddFragment.newInstance(index);
                    break;
                default:
                    return;
            }
        }
        ft.replace(R.id.fragment_event_content, fragment, index+"");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

}
