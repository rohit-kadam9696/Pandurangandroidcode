package com.twd.chitboyapp.spsskl.interfaces;

import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.multispinnerfilter.KeyPairBoolData;

import java.util.List;

public interface MultiReturn {

    void onItemsSelected(List<KeyPairBoolData> selectedItems, DataSetter dataSetter);

    void onOkClickLister(List<KeyPairBoolData> selectedItems, DataSetter dataSetter);
}
