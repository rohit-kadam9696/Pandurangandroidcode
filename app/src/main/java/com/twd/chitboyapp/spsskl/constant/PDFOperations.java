package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.twd.chitboyapp.spsskl.PdfViewerExampleActivity;
import com.twd.chitboyapp.spsskl.pojo.PDFTablePOJO;
import com.twd.pdfcreator.views.PDFBody;
import com.twd.pdfcreator.views.PDFFooterView;
import com.twd.pdfcreator.views.PDFTableView;
import com.twd.pdfcreator.views.basic.PDFHorizontalView;
import com.twd.pdfcreator.views.basic.PDFLineSeparatorView;
import com.twd.pdfcreator.views.basic.PDFTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

public class PDFOperations {

    private StringBuilder getText(View myview, StringBuilder data) {
        if (myview instanceof TextView) data.append(((TextView) myview).getText()).append(" ");
        return data;
    }

    private PDFTextView getPDFTextiew(String text, Context context) {
        PDFTextView pdfCompanyNameView = new PDFTextView(context, PDFTextView.PDF_TEXT_SIZE.P);
        pdfCompanyNameView.setText(text);
        return pdfCompanyNameView;
    }

    public PDFBody getBodyFromView(Context context, boolean isEqual, String json, PDFTablePOJO... pdfTablePOJOS) {
        PDFBody pdfBody = new PDFBody();
        if (pdfTablePOJOS == null) return pdfBody;
        int pdfsize = pdfTablePOJOS.length;
        JSONObject job = null;
        try {
            job = new JSONObject(json);
        } catch (JSONException e) {
            job = new JSONObject();
            e.printStackTrace();
        }

        for (int k = 0; k < pdfsize; k++) {
            PDFTablePOJO pdfTablePOJO = pdfTablePOJOS[k];
            if (k > 0) {
                PDFTextView pdfTableTitleView = new PDFTextView(context, PDFTextView.PDF_TEXT_SIZE.P);
                pdfTableTitleView.setText("\n");
                pdfBody.addView(pdfTableTitleView);
            }
            if (pdfTablePOJO.getHeadText() != null) {
                PDFTextView pdfTableTitleView = new PDFTextView(context, PDFTextView.PDF_TEXT_SIZE.P);
                pdfTableTitleView.setText(pdfTablePOJO.getHeadText());
                pdfBody.addView(pdfTableTitleView);
            }

            View view = pdfTablePOJO.getHeadView();
            PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(context).setBackgroundColor(Color.WHITE);
            pdfBody.addView(lineSeparatorView1);
            if (view != null) {
                StringBuilder sb = getText(view, new StringBuilder());
                PDFTextView pdfTableTitleView = new PDFTextView(context, PDFTextView.PDF_TEXT_SIZE.P);
                pdfTableTitleView.setText(sb.toString());
                pdfBody.addView(pdfTableTitleView);
            }

            TableLayout tbl = pdfTablePOJO.getTbl();
            int totSize = tbl.getChildCount();
            int totcol = 0;
            int acttot = 0;
            if (totSize > 0) {
                TableRow tr = (TableRow) tbl.getChildAt(0);
                totcol = tr.getChildCount();
                for (int i = 0; i < totcol; i++) {
                    TextView txt = (TextView) tr.getChildAt(i);
                    if (txt.getVisibility() == View.VISIBLE) {
                        acttot++;
                    }
                }
            }
            if (totcol > 0) {

                int[] widthPercent;
                if (tbl.getTag() == null) {
                    int percolen;
                    int diff;
                    widthPercent = new int[acttot];// Sum should be equal to 100%
                    if (!isEqual) {
                        percolen = 95 / acttot - 1;
                        diff = 95 % acttot - 1;
                        widthPercent[0] = 5;
                    } else {
                        percolen = 100 / acttot - 1;
                        diff = 100 % acttot - 1;
                        widthPercent[0] = percolen;
                    }

                    for (int i = 1; i < acttot; i++) {
                        if (i == 1) {
                            widthPercent[i] = percolen + diff;
                        } else {
                            widthPercent[i] = percolen;
                        }
                    }
                } else {
                    Integer[] widthPercentOb = ((Integer[]) tbl.getTag());
                    int size = widthPercentOb.length;
                    widthPercent = new int[size];// Sum should be equal to 100%
                    for (int i = 0; i < size; i++) {
                        widthPercent[i] = widthPercentOb[i];
                    }
                }

                int widthPercentLen = widthPercent.length;

                int prevCount = 0;
                TableRow tr = (TableRow) tbl.getChildAt(0);
                String prefix = (String) tr.getTag();

                PDFTableView.PDFTableRowView tableHeader = new PDFTableView.PDFTableRowView(context);
                int perPos = 0;
                int widthPercentCopy[] = new int[totcol];
                for (int i = 0; i < totcol; i++) {
                    TextView txt = (TextView) tr.getChildAt(i);
                    if (txt.getVisibility() != View.VISIBLE) {
                        continue;
                    }
                    PDFTextView pdfTextView = preparePdfTextView(txt, context, prefix, job);
                    int rperpos = perPos++;
                    widthPercentCopy[rperpos] = 0;
                    int curTag = (int) pdfTextView.getView().getTag();
                    for (int j = prevCount; j < prevCount + curTag; j++) {
                        widthPercentCopy[rperpos] += widthPercent[j];
                    }
                    prevCount += curTag;
                    tableHeader.addToRow(pdfTextView);
                }
                tableHeader.setColumnWidth(widthPercentCopy);
                setBgColor(tr, tableHeader, job, prefix);

                tr = (TableRow) tbl.getChildAt(1);
                prefix = (String) tr.getTag();
                totcol = tr.getChildCount();
                prevCount = 0;
                perPos = 0;
                widthPercentCopy = new int[totcol];
                PDFTableView.PDFTableRowView tableRowView1 = new PDFTableView.PDFTableRowView(context);
                for (int i = 0; i < totcol; i++) {
                    TextView txt = (TextView) tr.getChildAt(i);
                    if (txt.getVisibility() != View.VISIBLE) {
                        continue;
                    }
                    PDFTextView pdfTextView = preparePdfTextView(txt, context, prefix, job);
                    tableRowView1.addToRow(pdfTextView);
                    int rperpos = perPos++;
                    widthPercentCopy[rperpos] = 0;
                    int curTag = (int) pdfTextView.getView().getTag();
                    for (int j = prevCount; j < prevCount + curTag; j++) {
                        widthPercentCopy[rperpos] += widthPercent[j];
                    }
                    prevCount += curTag;
                }
                tableRowView1.setColumnWidth(widthPercentCopy);
                setBgColor(tr, tableRowView1, job, prefix);
                PDFTableView tableView = new PDFTableView(context, tableHeader, tableRowView1);

                for (int i = 2; i < totSize; i++) {

                    // Create 10 rows
                    tr = (TableRow) tbl.getChildAt(i);
                    prefix = (String) tr.getTag();
                    totcol = tr.getChildCount();
                    prevCount = 0;
                    widthPercentCopy = new int[totcol];
                    perPos = 0;
                    PDFTableView.PDFTableRowView tableRowView = new PDFTableView.PDFTableRowView(context);
                    for (int j = 0; j < totcol; j++) {
                        TextView txt = (TextView) tr.getChildAt(j);
                        if (txt.getVisibility() != View.VISIBLE) {
                            continue;
                        }
                        PDFTextView pdfTextView = preparePdfTextView(txt, context, prefix, job);
                        tableRowView.addToRow(pdfTextView);
                        int rperpos = perPos++;
                        widthPercentCopy[rperpos] = 0;
                        int curTag = (int) pdfTextView.getView().getTag();
                        for (int kk = prevCount; kk < prevCount + curTag && kk < widthPercentLen; kk++) {
                            widthPercentCopy[rperpos] += widthPercent[kk];
                        }
                        prevCount += curTag;
                    }
                    tableRowView.setColumnWidth(widthPercentCopy);
                    setBgColor(tr, tableRowView, job, prefix);
                    tableView.addRow(tableRowView);
                }
                //tableView.setColumnWidth(widthPercent);
                pdfBody.addView(tableView);

                PDFLineSeparatorView lineSeparatorView4 = new PDFLineSeparatorView(context).setBackgroundColor(Color.BLACK);
                pdfBody.addView(lineSeparatorView4);
            }
        }
        return pdfBody;
    }

    private void setBgColor(TableRow tr, PDFTableView.PDFTableRowView tableRow, JSONObject job, String prefix) {

        if (job.has(prefix + "bc")) {
            try {
                String color = job.getString(prefix + "bc");
                tableRow.setBackgroundColor(Color.parseColor(color));
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        } else {
            Drawable background = tr.getBackground();
            if (background instanceof ColorDrawable) {
                int color = ((ColorDrawable) background).getColor();
                tableRow.setBackgroundColor(color);
            } else {
                return;
            }
        }
    }

    private PDFTextView preparePdfTextView(TextView txt, Context context, String prefix, JSONObject job) {
        PDFTextView pdfTextView;
        if (job.has(prefix + "fs")) {
            try {
                pdfTextView = new PDFTextView(context, job.getInt(prefix + "fs"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else pdfTextView = new PDFTextView(context, PDFTextView.PDF_TEXT_SIZE.P);
        TextView textview = pdfTextView.getView();
        try {
            if (job.has(prefix + "tc"))
                textview.setTextColor(Color.parseColor(job.getString(prefix + "tc")));
            else textview.setTextColor(txt.getTextColors());

            int a = job.has(prefix + "a") ? job.getInt(prefix + "a") : 1;
            if (a == 1) textview.setGravity(txt.getGravity());
            else if (a == 2) textview.setGravity(Gravity.START);
            else if (a == 3) textview.setGravity(Gravity.CENTER);
            else if (a == 4) textview.setGravity(Gravity.END);
            textview.setText(txt.getText().toString().trim());
            Object tag = txt.getTag();
            textview.setBackgroundResource((Integer) tag);
            if (txt.getLayoutParams() instanceof TableRow.LayoutParams) {
                TableRow.LayoutParams params = (TableRow.LayoutParams) txt.getLayoutParams();
                textview.setTag(params.span);
                //textview.setLayoutParams(params);
            } else {
                textview.setTag(1);
            }
            // textview.setTypeface(txt.getTypeface());
        } catch (Exception e) {
            e.printStackTrace();
        }
        pdfTextView.setView(textview);
        //textview.setPadding(0, 0, 0, 0);
        return pdfTextView;
    }

    public PDFFooterView getFooterView(int pageIndex, Context context) {
        PDFFooterView footerView = new PDFFooterView(context);
        PDFHorizontalView horizontalView = new PDFHorizontalView(context);

        PDFTextView pdfTextViewPage = new PDFTextView(context, PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfTextViewPage.setText("संगणक प्रत");
        pdfTextViewPage.setLayout(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        pdfTextViewPage.getView().setGravity(Gravity.LEFT);
        horizontalView.addView(pdfTextViewPage);

        pdfTextViewPage = new PDFTextView(context, PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfTextViewPage.setText(String.format(Locale.getDefault(), "पान: %d", pageIndex + 1));
        pdfTextViewPage.setLayout(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        pdfTextViewPage.getView().setGravity(Gravity.CENTER_HORIZONTAL);
        horizontalView.addView(pdfTextViewPage);

        pdfTextViewPage = new PDFTextView(context, PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfTextViewPage.setText("\u00a9 3WD Software");
        pdfTextViewPage.setLayout(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        pdfTextViewPage.getView().setGravity(Gravity.RIGHT);

        horizontalView.addView(pdfTextViewPage);
        footerView.addView(horizontalView);
        return footerView;
    }

    public void showPreview(File savedPDFFile, Activity activity) {
        Uri pdfUri = Uri.fromFile(savedPDFFile);
        Intent intentPdfViewer = new Intent(activity, PdfViewerExampleActivity.class);
        intentPdfViewer.putExtra(PdfViewerExampleActivity.PDF_FILE_URI, pdfUri);
        activity.startActivity(intentPdfViewer);
    }
}
