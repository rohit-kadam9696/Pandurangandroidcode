package com.twd.chitboyapp.spsskl.interfaces;

import android.widget.TableLayout;

public interface TableOperations {

    void onEditClick(TableLayout tbl, String id, int position);

    void onDeleteClick(TableLayout tbl, String id, int position);
}
