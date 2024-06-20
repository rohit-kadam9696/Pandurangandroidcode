package com.twd.chitboyapp.spsskl;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.twd.pdfcreator.activity.PDFCreatorActivity;
import com.twd.pdfcreator.utils.PDFUtil;
import com.twd.pdfcreator.views.PDFBody;
import com.twd.pdfcreator.views.PDFFooterView;
import com.twd.pdfcreator.views.PDFHeaderView;
import com.twd.pdfcreator.views.basic.PDFHorizontalView;
import com.twd.pdfcreator.views.basic.PDFImageView;
import com.twd.pdfcreator.views.basic.PDFLineSeparatorView;
import com.twd.pdfcreator.views.basic.PDFTextView;
import com.twd.pdfcreator.views.basic.PDFVerticalView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

public class PdfCreatorExampleActivity extends PDFCreatorActivity {

    public static PDFBody pdfBody = null;

    //String header;
    JSONObject job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        /*if (intent.hasExtra("header")) {
            header = intent.getStringExtra("header");
        } else {
            header = getResources().getString(R.string.myfactory);
        }*/

        String json = "{}";
        if (intent.hasExtra("json")) {
            json = intent.getStringExtra("json");
        } else {
            json = String.format(getResources().getString(R.string.defaultJson), getResources().getString(R.string.myfactory));
        }

        try {
            job = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            job = new JSONObject();
        }

        double w, h;
        String or = null;
        try {
            or = job.has("or") ? job.getString("or") : "P";
        } catch (JSONException e) {
            e.printStackTrace();
            or = "P";
        }
        if (or.equalsIgnoreCase("P")) {
            if (job.has("w")) {
                try {
                    w = job.getDouble("w");
                } catch (JSONException e) {
                    e.printStackTrace();
                    w = 8.3;
                }
            } else {
                w = 8.3;
            }
            if (job.has("h")) {
                try {
                    h = job.getDouble("h");
                } catch (JSONException e) {
                    e.printStackTrace();
                    h = 11.7;
                }
            } else {
                h = 11.7;
            }
        } else {
            if (job.has("w")) {
                try {
                    h = job.getDouble("w");
                } catch (JSONException e) {
                    e.printStackTrace();
                    h = 8.3;
                }
            } else {
                h = 8.3;
            }
            if (job.has("h")) {
                try {
                    w = job.getDouble("h");
                } catch (JSONException e) {
                    e.printStackTrace();
                    w = 11.7;
                }
            } else {
                w = 11.7;
            }
        }

        int vm;
        int hm;
        if (job.has("vm")) {
            try {
                vm = job.getInt("vm");
            } catch (JSONException e) {
                e.printStackTrace();
                vm = 20;
            }
        } else {
            vm = 20;
        }

        if (job.has("hm")) {
            try {
                hm = job.getInt("hm");
            } catch (JSONException e) {
                e.printStackTrace();
                hm = 20;
            }
        } else {
            hm = 20;
        }
        createPDF("pdfreport", w, h, vm, hm, new PDFUtil.PDFUtilListener() {
            @Override
            public void pdfGenerationSuccess(File savedPDFFile) {
                Toast.makeText(PdfCreatorExampleActivity.this, "PDF Created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void pdfGenerationFailure(Exception exception) {
                Toast.makeText(PdfCreatorExampleActivity.this, "PDF NOT Created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected PDFHeaderView getHeaderView(int pageIndex) {
        try {
            if (job.has("phyn") && job.getString("phyn").equalsIgnoreCase("Y")) {
                PDFHeaderView headerView = new PDFHeaderView(getApplicationContext());

                PDFHorizontalView horizontalView = new PDFHorizontalView(getApplicationContext());
                if (job.has("hrl") && job.getString("hrl").equalsIgnoreCase("Y")) {
                    PDFImageView imageView = new PDFImageView(getApplicationContext());
                    LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(60, 60, 1);
                    imageView.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    imageLayoutParam.setMargins(0, 0, 10, 0);
                    imageView.setLayout(imageLayoutParam);
                    imageView.getView().setVisibility(View.INVISIBLE);

                    horizontalView.addView(imageView);
                }
                PDFVerticalView pdfVerticalView = new PDFVerticalView(getApplicationContext());
                pdfVerticalView.setLayout(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 8));
                if (job.has("ph")) {

                    PDFTextView pdfTextView;
                    if (job.has("hf"))
                        pdfTextView = new PDFTextView(getApplicationContext(), job.getInt("hf"));
                    else
                        pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);

            /*SpannableString word = new SpannableString("INVOICE");
            word.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/

                    pdfTextView.setText(job.getString("ph"));
                    if (job.has("htc")) {
                        String htc = job.getString("htc");
                        pdfTextView.setTextColor(Color.parseColor(htc));
                    }

                    pdfTextView.setLayout(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    pdfTextView.getView().setGravity(Gravity.CENTER_HORIZONTAL);
                    pdfTextView.getView().setTypeface(pdfTextView.getView().getTypeface(), Typeface.BOLD);

                    pdfVerticalView.addView(pdfTextView);
                }

                if (job.has("psh")) {
                    PDFTextView pdfTextViewsh = null;
                    if (job.has("shf"))
                        pdfTextViewsh = new PDFTextView(getApplicationContext(), job.getInt("shf"));
                    else
                        pdfTextViewsh = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);

            /*SpannableString word = new SpannableString("INVOICE");
            word.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/

                    pdfTextViewsh.setText(job.getString("psh"));
                    if (job.has("shtc")) {
                        String shtc = job.getString("shtc");
                        pdfTextViewsh.setTextColor(Color.parseColor(shtc));
                    }

                    pdfTextViewsh.setLayout(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    pdfTextViewsh.getView().setGravity(Gravity.CENTER_HORIZONTAL);
                    pdfTextViewsh.getView().setTypeface(pdfTextViewsh.getView().getTypeface(), Typeface.BOLD);

                    pdfVerticalView.addView(pdfTextViewsh);
                }
                horizontalView.addView(pdfVerticalView);
                if (job.has("hrl") && job.getString("hrl").equalsIgnoreCase("Y")) {
                    PDFImageView imageView = new PDFImageView(getApplicationContext());
                    LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(60, 60, 1);
                    imageView.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    imageLayoutParam.setMargins(0, 0, 10, 0);
                    imageView.setLayout(imageLayoutParam);


                    horizontalView.addView(imageView);
                }

                headerView.addView(horizontalView);

                PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
                headerView.addView(lineSeparatorView1);
                return headerView;
            } else return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected PDFBody getBodyViews() {
        /*PDFBody pdfBody = new PDFBody();
        //int HEIGHT_ALLOTTED_PER_PAGE = (getResources().getDimensionPixelSize(com.twd.pdfcreator.R.dimen.pdf_height) - (getResources().getDimensionPixelSize(com.twd.pdfcreator.R.dimen.pdf_margin_vertical) * 2));
        PDFTextView pdfCompanyNameView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        pdfCompanyNameView.setText("Company Name" + " क्षितीज");
        pdfBody.addView(pdfCompanyNameView);
        PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        pdfBody.addView(lineSeparatorView1);
        PDFTextView pdfAddressView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        pdfAddressView.setText("Address Line 1\nCity, State - 123456");
        pdfBody.addView(pdfAddressView);

        PDFLineSeparatorView lineSeparatorView2 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        lineSeparatorView2.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                8, 0));
        pdfBody.addView(lineSeparatorView2);

        PDFLineSeparatorView lineSeparatorView3 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        pdfBody.addView(lineSeparatorView3);

        int[] widthPercent = {20, 20, 20, 40}; // Sum should be equal to 100%
        String[] textInTable = {"1 किशोर", "2 सुरेंद्र", "3 समीर", "4 स्वप्निल"};
        PDFTextView pdfTableTitleView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        pdfTableTitleView.setText("Table Example");
        pdfBody.addView(pdfTableTitleView);

        final PDFPageBreakView pdfPageBreakView = new PDFPageBreakView(getApplicationContext());
        pdfBody.addView(pdfPageBreakView);

        PDFTableView.PDFTableRowView tableHeader = new PDFTableView.PDFTableRowView(getApplicationContext());
        for (String s : textInTable) {
            PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
            pdfTextView.setText("Header Title: " + s);
            //pdfTextView.setTextColor(getResources().getColor(R.color.white));
            pdfTextView.getView().setBackground(getResources().getDrawable(R.drawable.borderhead));
            tableHeader.addToRow(pdfTextView);
        }

        PDFTableView.PDFTableRowView tableRowView1 = new PDFTableView.PDFTableRowView(getApplicationContext());
        for (String s : textInTable) {
            PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
            pdfTextView.setText("Row 1 : " + s);
            //pdfTextView.setTextColor(getResources().getColor(R.color.white));
            pdfTextView.getView().setBackground(getResources().getDrawable(R.drawable.borderodd));
            tableRowView1.addToRow(pdfTextView);
        }

        PDFTableView tableView = new PDFTableView(getApplicationContext(), tableHeader, tableRowView1);

        for (int i = 0; i < 200; i++) {
            // Create 10 rows
            PDFTableView.PDFTableRowView tableRowView = new PDFTableView.PDFTableRowView(getApplicationContext());
            for (String s : textInTable) {
                PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
                pdfTextView.setText("Row " + (i + 2) + ": " + s);
                //pdfTextView.setTextColor(getResources().getColor(R.color.white));
                if (i == 200 - 1) {
                    pdfTextView.getView().setBackground(getResources().getDrawable(R.drawable.borderfoot));
                } else if (i % 2 == 0) {
                    pdfTextView.getView().setBackground(getResources().getDrawable(R.drawable.bordereven));
                } else {
                    pdfTextView.getView().setBackground(getResources().getDrawable(R.drawable.borderodd));
                }
                tableRowView.addToRow(pdfTextView);

            }
            tableView.addRow(tableRowView);
        }
        tableView.setColumnWidth(widthPercent);
        pdfBody.addView(tableView);

        PDFLineSeparatorView lineSeparatorView4 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.BLACK);
        pdfBody.addView(lineSeparatorView4);

        PDFTextView pdfIconLicenseView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        Spanned icon8Link = HtmlCompat.fromHtml("Icon from <a href='https://icons8.com'>https://icons8.com</a>", HtmlCompat.FROM_HTML_MODE_LEGACY);
        pdfIconLicenseView.getView().setText(icon8Link);
        pdfBody.addView(pdfIconLicenseView);*/

        return pdfBody;
    }

    @Override
    protected PDFFooterView getFooterView(int pageIndex) {

        PDFFooterView footerView = new PDFFooterView(getApplicationContext());
        PDFHorizontalView horizontalView = new PDFHorizontalView(getApplicationContext());

        PDFTextView pdfTextViewPage = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfTextViewPage.setText("संगणक प्रत");
        pdfTextViewPage.setLayout(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        pdfTextViewPage.getView().setGravity(Gravity.LEFT);
        horizontalView.addView(pdfTextViewPage);

        pdfTextViewPage = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfTextViewPage.setText(String.format(Locale.getDefault(), "पान नं. : %d", pageIndex + 1));
        pdfTextViewPage.setLayout(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        pdfTextViewPage.getView().setGravity(Gravity.CENTER_HORIZONTAL);
        horizontalView.addView(pdfTextViewPage);

        pdfTextViewPage = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfTextViewPage.setText("\u00a9 3WD Software");
        pdfTextViewPage.setLayout(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        pdfTextViewPage.getView().setGravity(Gravity.RIGHT);

        horizontalView.addView(pdfTextViewPage);
        footerView.addView(horizontalView);
        return footerView;
    }

    @Nullable
    @Override
    protected PDFImageView getWatermarkView(int forPage) {
        PDFImageView pdfImageView = new PDFImageView(getApplicationContext());
        FrameLayout.LayoutParams childLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200, Gravity.CENTER);
        pdfImageView.setLayout(childLayoutParams);

        pdfImageView.setImageResource(R.drawable.ic_pdf);
        pdfImageView.setImageScale(ImageView.ScaleType.FIT_CENTER);
        pdfImageView.getView().setAlpha(0.3F);

        return pdfImageView;
    }

    @Override
    protected void onNextClicked(final File savedPDFFile) {
        Uri pdfUri = Uri.fromFile(savedPDFFile);

        Intent intentPdfViewer = new Intent(PdfCreatorExampleActivity.this, PdfViewerExampleActivity.class);
        intentPdfViewer.putExtra(PdfViewerExampleActivity.PDF_FILE_URI, pdfUri);

        startActivity(intentPdfViewer);
    }
}
