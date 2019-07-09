package com.gbksoft.debugview.xyrecycler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.gbksoft.debugview.xyrecycler.adapters.BaseRecyclerAdapter;
import com.gbksoft.debugview.xyrecycler.databinding.FragmentMainBinding;
import com.gbksoft.debugview.xyrecycler.template.XYRecyclerAdapter;


public class MainFragment extends Fragment {

    private FragmentMainBinding layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        BaseRecyclerAdapter xyRecyclerAdapter = new XYRecyclerAdapter(getContext());
        layout.xyRecycler.setAdapter(xyRecyclerAdapter);
        xyRecyclerAdapter.setAllItems(
                RandomContent.getRandomColumnHeaderList(),
                RandomContent.getRandomRowHeaderList(),
                RandomContent.getRandomCellList());

        return layout.getRoot();
    }

}
