package com.twd.chitboyapp.spsskl.interfaces;

import com.twd.chitboyapp.spsskl.enums.DataSetter;
import com.twd.multispinnerfilter.KeyPairBoolData;

public interface SingleReturn {
    void onSelectSingle(KeyPairBoolData selectedItem, DataSetter dataSetter);
}
