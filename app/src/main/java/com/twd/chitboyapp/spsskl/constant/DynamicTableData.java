package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.interfaces.TableOperations;
import com.twd.svalidation.ServerValidation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class DynamicTableData {

    private String deleteMsg;
    static Context context;
    ServerValidation sv = new ServerValidation();
    TableOperations tableOperations;

    public TextView ConfigureTextView(String text, Context cnt, int color, boolean headRow, String flval, boolean marathi, boolean footRow, Integer borderid, Integer align) {
        if (text == null) {
            text = " ";
        }
        TextView textview = new TextView(cnt);
        textview.setTextColor(color);
        boolean isFloat = false;
        if (align != null) {
            if (align == 1) textview.setGravity(Gravity.START);
            else if (align == 2) textview.setGravity(Gravity.CENTER);
            else textview.setGravity(Gravity.END);
        } else if (headRow) textview.setGravity(Gravity.CENTER);
        else if (sv.checkFloat(text)) {
            isFloat = true;
            textview.setGravity(Gravity.END);
        } else if (footRow) textview.setGravity(Gravity.CENTER);
        else textview.setGravity(Gravity.START);
        if (flval != null && isFloat) {
            DecimalFormat df = new DecimalFormat("#0" + flval.trim());
            text = df.format(Double.parseDouble(text));
        }
        if (marathi) {
            text = MarathiHelper.convertEnglishtoMarathi(text);
        }
        textview.setText(text);
        if (borderid == 1) {
            textview.setBackgroundResource(R.drawable.border);
            textview.setTag(R.drawable.borderex);
        } else if (borderid == 2) {
            textview.setBackgroundResource(R.drawable.borderltr);
            textview.setTag(R.drawable.borderexltr);
        } else if (borderid == 3) {
            textview.setBackgroundResource(R.drawable.borderlr);
            textview.setTag(R.drawable.borderexlr);
        } else if (borderid == 4) {
            textview.setBackgroundResource(R.drawable.borderlbr);
            textview.setTag(R.drawable.borderexlbr);
        }
        return textview;
    }

    private ImageView CreateImageView(int drawable, int j) {
        ImageView imageView = new ImageView(context);
        imageView.setAdjustViewBounds(true);
        imageView.setBackgroundResource(R.drawable.border);
        imageView.setImageDrawable(context.getResources().getDrawable(drawable));
        if (j == 0) imageView.setColorFilter(context.getResources().getColor(R.color.white));
        return imageView;
    }

    public void addTable(Context context, ArrayList<ArrayList<String>> tableData, TableLayout tbl, HashMap<String, Integer> colspan, int headrow, boolean isFooter, HashMap<String, Boolean> boldIndicator, boolean isMgmt, HashMap<String, String> floatings, boolean marathi, HashMap<String, Integer> rowSpan, HashMap<String, Integer> textAlign, HashMap<String, Boolean> visibility, boolean edit, boolean delete, TableOperations tboperation, boolean checkable) {
        DynamicTableData.context = context;
        this.tableOperations = tboperation;
        tbl.removeAllViews();
        int length = tableData.size();
        boolean headRow;
        boolean footRow;
        for (int j = 0; j < length; j++) {
            int color;
            if (isMgmt) color = context.getResources().getColor(R.color.colorMgmtText);
            else color = Color.BLACK;
            TableRow row = new TableRow(context);
            if (j < headrow) {
                row.setBackgroundColor(context.getResources().getColor(R.color.tablehead));
                color = Color.WHITE;
                headRow = true;
                footRow = false;
                row.setTag("th");
                // header colour
            } else if (j + 1 == length && isFooter) {
                // footer colour
                row.setBackgroundColor(context.getResources().getColor(R.color.tablefoot));
                color = Color.WHITE;
                headRow = false;
                footRow = true;
                row.setTag("tf");
            } else if (j % 2 == 0) {
                row.setBackgroundColor(context.getResources().getColor(R.color.tableeven));
                headRow = false;
                footRow = false;
                row.setTag("te");
            } else {
                // odd row colour
                row.setBackgroundColor(context.getResources().getColor(R.color.tableodd));
                headRow = false;
                footRow = false;
                row.setTag("to");
            }
            ArrayList<String> innerData = tableData.get(j);
            int length1 = innerData.size();
            if (!headRow && !footRow && (edit || delete || checkable)) {
                length1 -= 1;
            }
            if (checkable) {
                CheckBox chk = ConfigureCheckBox();
                if (headRow || innerData.get(length1).equals("0"))
                    chk.setVisibility(View.INVISIBLE);
                else
                    chk.setTag(innerData.get(length1));
                row.addView(chk);
            }
            for (int k = 0; k < length1; k++) {
                String key = j + "-" + k;  // 0-0
                String colKey = "*-" + k;  // *-0
                String rowKey = j + "-*";  // 0-*
                String flval = null;
                if (!headRow && floatings != null) {
                    if (floatings.containsKey(key)) flval = floatings.get(key);
                    else if (floatings.containsKey(colKey)) flval = floatings.get(colKey);
                    else if (floatings.containsKey(rowKey)) flval = floatings.get(rowKey);
                }
                Integer borderid = 1;
                if (rowSpan != null) {
                    if (rowSpan.containsKey(key)) borderid = rowSpan.get(key);
                    else if (rowSpan.containsKey(colKey)) borderid = rowSpan.get(colKey);
                    else if (rowSpan.containsKey(rowKey)) borderid = rowSpan.get(rowKey);
                }
                if (borderid == null) {
                    borderid = 1;
                }
                Integer align = null;
                if (textAlign != null) {
                    if (textAlign.containsKey(key)) align = textAlign.get(key);
                    else if (textAlign.containsKey(colKey)) align = textAlign.get(colKey);
                    else if (textAlign.containsKey(rowKey)) align = textAlign.get(rowKey);
                }

                TextView textview = ConfigureTextView(innerData.get(k), context, color, headRow, flval, marathi, footRow, borderid, align);

                if (colspan != null && colspan.containsKey(key)) {
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    params.span = colspan.get(key);
                    textview.setLayoutParams(params);
                }
                if (boldIndicator != null) {
                    if (boldIndicator.containsKey(key)) {
                        if (boldIndicator.get(key)) textview.setTypeface(null, Typeface.BOLD);
                    } else if (boldIndicator.containsKey(colKey)) {
                        if (boldIndicator.get(colKey)) textview.setTypeface(null, Typeface.BOLD);
                    } else if (boldIndicator.containsKey(rowKey)) {
                        if (boldIndicator.get(rowKey)) textview.setTypeface(null, Typeface.BOLD);
                    }
                }
                if (visibility != null) {
                    if (visibility.containsKey(key)) {
                        if (!visibility.get(key)) textview.setVisibility(View.GONE);
                    } else if (visibility.containsKey(colKey)) {
                        if (!visibility.get(colKey)) textview.setVisibility(View.GONE);
                    } else if (visibility.containsKey(rowKey)) {
                        if (!visibility.get(rowKey)) textview.setVisibility(View.GONE);
                    }
                }
                row.addView(textview);
            }
            if (!headRow && !footRow && edit) {
                ImageView btn = ConfigureEditButton(tbl, innerData.get(length1), j);
                row.addView(btn);
            }

            if (!headRow && !footRow && delete) {
                ImageView btn = ConfigureDeleteButton(tbl, innerData.get(length1), j);
                row.addView(btn);
            }
            tbl.addView(row);
        }
    }

    private CheckBox ConfigureCheckBox() {
        CheckBox checkBox = new CheckBox(context);
        checkBox.setBackgroundResource(R.drawable.border);
        checkBox.setTag(true);
        return checkBox;
    }

    private ImageView ConfigureDeleteButton(TableLayout tbl, String id, int position) {
        ImageView imgDelete = CreateImageView(R.drawable.ic_delete, position);
        imgDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.popup_delete);

                int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
                TextView message = dialog.findViewById(R.id.message);
                if (getDeleteMsg() == null)
                    message.setText(context.getResources().getString(R.string.messageactivedeactive));
                else message.setText(deleteMsg);

                dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

                Button btncanceldelete = dialog.findViewById(R.id.btncanceldelete);
                Button btnconfirmdelete = dialog.findViewById(R.id.btnconfirmdelete);

                btncanceldelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                btnconfirmdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        if (tableOperations != null) {
                            tableOperations.onDeleteClick(tbl, id, position);
                        } else {
                            Constant.showToast(context.getResources().getString(R.string.editCallerNotFound), (Activity) context, R.drawable.ic_info);
                        }
                    }
                });

                dialog.show();
            }
        });
        return imgDelete;
    }

    private ImageView ConfigureEditButton(TableLayout tbl, String id, int position) {
        ImageView imgedit = CreateImageView(R.drawable.ic_edit, position);
        imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tableOperations != null) {
                    tableOperations.onEditClick(tbl, id, position);
                } else {
                    Constant.showToast(context.getResources().getString(R.string.editCallerNotFound), (Activity) context, R.drawable.ic_info);
                }
            }
        });
        return imgedit;
    }

    public String getCheckedId(TableLayout tbl) {
        StringBuilder selecedId = new StringBuilder();
        int size = tbl.getChildCount();
        for (int i = 1; i < size; i++) {
            TableRow tableRow = (TableRow) tbl.getChildAt(i);
            View checked = tableRow.getChildAt(0);
            if (checked instanceof CheckBox) {
                CheckBox checkBox = ((CheckBox) checked);
                if (checkBox.isChecked()) {
                    if (!selecedId.toString().equals("")) {
                        selecedId.append(",");
                    }
                    selecedId.append(checkBox.getTag());
                }
            }
        }
        return selecedId.toString();
    }

    public String getDeleteMsg() {
        return deleteMsg;
    }

    public void setDeleteMsg(String deleteMsg) {
        this.deleteMsg = deleteMsg;
    }


}
