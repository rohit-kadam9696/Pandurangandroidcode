package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.chitboyapp.spsskl.interfaces.SingleReturn;
import com.twd.multispinnerfilter.KeyPairBoolData;
import com.twd.multispinnerfilter.SingleSpinnerListener;
import com.twd.multispinnerfilter.SingleSpinnerSearch;

import java.util.List;

public class SingleSelectHandler {

    public SingleReturn singleReturn = null;

    public void initSingle(SingleSpinnerSearch singleSpinnerSearch, Activity activity, String searchHint) {
        singleSpinnerSearch.setColorseparation(true);
        singleSpinnerSearch.setSearchHint(searchHint);
        singleSpinnerSearch.setSearchEnabled(true);
        singleSpinnerSearch.setEmptyTitle(activity.getResources().getString(R.string.noinformationavailable));
    }

    public void setSingleItems(SingleSpinnerSearch ssp, List<KeyPairBoolData> list, DataSetter dataSetter) {
        if (list.size() == 1) {
            KeyPairBoolData keyPairBoolData = list.get(0);
            keyPairBoolData.setSelected(true);
            list.set(0, keyPairBoolData);
        }
        ssp.setItems(list, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                if (singleReturn != null)
                    singleReturn.onSelectSingle(selectedItem, dataSetter);
            }

            @Override
            public void onClear() {

            }
        });
        if (list.size() == 1) {
            if (singleReturn != null)
                singleReturn.onSelectSingle(list.get(0), dataSetter);
        }
    }

    public KeyPairBoolData selectById(SingleSpinnerSearch ssp, long id, DataSetter dataSetter) {
        List<KeyPairBoolData> ssplist = ssp.getItems();
        KeyPairBoolData keyPairBoolData = null;
        int size = ssplist.size();
        for (int i = 0; i < size; i++) {
            keyPairBoolData = ssplist.get(i);
            if (keyPairBoolData.getId() == id) {
                keyPairBoolData.setSelected(true);
                ssplist.set(i, keyPairBoolData);
                break;
            }
        }
        ssp.setItems(ssplist, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                if (singleReturn != null)
                    singleReturn.onSelectSingle(selectedItem, dataSetter);
            }

            @Override
            public void onClear() {

            }
        });
        return keyPairBoolData;
    }
}
