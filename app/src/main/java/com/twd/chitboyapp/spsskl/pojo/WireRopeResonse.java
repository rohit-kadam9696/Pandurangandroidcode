package com.twd.chitboyapp.spsskl.pojo;

import com.twd.multispinnerfilter.KeyPairBoolData;

import java.util.HashMap;
import java.util.List;

public class WireRopeResonse extends MainResponse {
    private List<KeyPairBoolData> wirerope;
    private List<KeyPairBoolData> wireropefront;
    private List<KeyPairBoolData> wireropeback;
    private HashMap<String, String> combos;

    public List<KeyPairBoolData> getWirerope() {
        return wirerope;
    }

    public void setWirerope(List<KeyPairBoolData> wirerope) {
        this.wirerope = wirerope;
    }

    public List<KeyPairBoolData> getWireropefront() {
        return wireropefront;
    }

    public void setWireropefront(List<KeyPairBoolData> wireropefront) {
        this.wireropefront = wireropefront;
    }

    public List<KeyPairBoolData> getWireropeback() {
        return wireropeback;
    }

    public void setWireropeback(List<KeyPairBoolData> wireropeback) {
        this.wireropeback = wireropeback;
    }

    public HashMap<String, String> getCombos() {
        return combos;
    }

    public void setCombos(HashMap<String, String> combos) {
        this.combos = combos;
    }
}
