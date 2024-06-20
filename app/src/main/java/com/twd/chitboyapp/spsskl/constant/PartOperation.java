package com.twd.chitboyapp.spsskl.constant;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PartOperation {

    public RequestBody createPartFromString(String str) {
        if (str != null) {
            MediaType text = MediaType.parse("text/plain");
            return RequestBody.create(str, text);
        } else
            return null;
    }
}
