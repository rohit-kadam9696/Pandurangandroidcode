package com.twd.chitboyapp.spsskl.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.adapter.SubTabAdapter;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubMenuFragment extends Fragment {

    private static final String GROUPID = "groupId";
    private String groupId;

    public static SubMenuFragment newInstance(String groupId) {
        SubMenuFragment fragment = new SubMenuFragment();
        Bundle args = new Bundle();
        args.putString(GROUPID, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getString(GROUPID);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_menu, container, false);
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefpermissiondt)};
        String[] values = new String[]{"{}"};
        String[] result = cf.getSharedPrefValue(getActivity(), key, values);
        if (result[0].equals("{}")) {

        } else {
            try {
                JSONObject job = new JSONObject(result[0]);
                if (job.has(groupId)) {
                    JSONArray jar = job.getJSONArray(groupId);
                    RecyclerView mmlist = view.findViewById(R.id.mmlist);
                    GridLayoutManager mLayoutManager1 = new GridLayoutManager(getActivity(), 3);
                    mmlist.setLayoutManager(mLayoutManager1);
                    mmlist.setItemAnimator(new DefaultItemAnimator());
                    SubTabAdapter fm1 = new SubTabAdapter(jar, getActivity(), groupId);
                    mmlist.setAdapter(fm1);
                    cf.putSharedPrefValue(new String[]{getResources().getString(R.string.preflastgroup)}, new String[]{groupId}, getActivity());

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}