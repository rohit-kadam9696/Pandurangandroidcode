package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.interfaces.MultiReturn;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.MultiSpinnerListener;
import com.twd.multispinnerfilter.MultiSpinnerListenerOk;
import com.twd.multispinnerfilter.MultiSpinnerSearch;

import java.util.List;

public class MultiSelectHandler {

    public MultiReturn multiReturn = null;

    public void initMulti(MultiSpinnerSearch multiselect, boolean selectAll, int limit, Activity activity, String searchHint, String selectHint, String customMsg) {
        multiselect.setColorSeparation(true);
        multiselect.setSearchHint(searchHint);
        multiselect.setSearchEnabled(true);
        if (limit > 0) {
            final String custMsg = customMsg;
            multiselect.setLimit(limit, new MultiSpinnerSearch.LimitExceedListener() {
                @Override
                public void onLimitListener(KeyPairBoolData data) {

                    if (custMsg == null)
                        Constant.showToast(String.format(activity.getResources().getString(R.string.maxlimit), "" + limit), activity, R.drawable.ic_wrong);
                    else Constant.showToast(custMsg, activity, R.drawable.ic_wrong);
                }
            });
        } else {
            multiselect.setLimit(-1, new MultiSpinnerSearch.LimitExceedListener() {
                @Override
                public void onLimitListener(KeyPairBoolData data) {
                    //Constant.showToast(String.format(activity.getResources().getString(R.string.maxlimit), "" + limit), activity, R.drawable.ic_wrong);
                }
            });
        }
        multiselect.setShowSelectAllButton(selectAll);
        multiselect.setClearText(activity.getResources().getString(R.string.closeandclear));
        multiselect.setHintText(selectHint);
        multiselect.setEmptyTitle(activity.getResources().getString(R.string.noinformationavailable));
    }

    public void setMultiSpinner(MultiSpinnerSearch mss, List<KeyPairBoolData> list, DataSetter dataSetter) {
        mss.setItems(list, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                if (multiReturn != null) multiReturn.onItemsSelected(selectedItems, dataSetter);
            }
        }, new MultiSpinnerListenerOk() {
            @Override
            public void onOkClickLister(List<Long> selectedIds, List<KeyPairBoolData> selectedItems) {
                if (multiReturn != null) multiReturn.onOkClickLister(selectedItems, dataSetter);
            }
        });

    }
}
