package com.twd.chitboyapp.spsskl.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.MarathiHelper;
import com.twd.chitboyapp.spsskl.constant.RetrofitHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.filter.InputFilterMinMax;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.pojo.CloseTransferResponse;
import com.twd.chitboyapp.spsskl.pojo.CompletePlotDetails;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;
import com.twd.chitboyapp.spsskl.vh.CompletePlotListHolder;
import com.twd.svalidation.ServerValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CompletePlotAdapter extends RecyclerView.Adapter<CompletePlotListHolder> implements RetrofitResponse {


    private final Activity activity;
    private final List<CompletePlotDetails> completePlotDetails;
    private final HashMap<String, String> village;
    private final HashMap<String, String> hangam;
    private final HashMap<String, String> varity;
    private final String isclose;


    public CompletePlotAdapter(List<CompletePlotDetails> completePlotDetails, Activity context, String isclose) {
        this.completePlotDetails = completePlotDetails;
        this.activity = context;
        this.isclose = isclose;
        DBHelper dbHelper = new DBHelper(activity);
        village = dbHelper.getVillageHashMap();
        hangam = dbHelper.getHangamHashMap();
        varity = dbHelper.getVarietyHashMap();
    }

    @NonNull
    @Override
    public CompletePlotListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complete_plot, parent, false);
        return new CompletePlotListHolder(v);
    }

    @Override
    public void onBindViewHolder(CompletePlotListHolder holder, final int position) {
        CompletePlotDetails plotDetails = completePlotDetails.get(holder.getBindingAdapterPosition());

        holder.txtnondvillagetxt.setText(plotDetails.getNvillCode());
        holder.txtplotnotxt.setText(plotDetails.getNplotNo());
        holder.txthangamtxt.setText(hangam.containsKey(plotDetails.getNhangamCode()) ? hangam.get(plotDetails.getNhangamCode()) : "");
        holder.txtareatxt.setText(String.valueOf(plotDetails.getNtentativeArea()));
        holder.txtvaritytxt.setText(varity.containsKey(plotDetails.getNvarietyCode()) ? varity.get(plotDetails.getNvarietyCode()) : "");
        holder.txtplantationdatetxt.setText(plotDetails.getDplantationDate());

        holder.txtexpectedyieldtxt.setText(plotDetails.getNexpectedYield());
        holder.txtharvestedtonnagetxt.setText(plotDetails.getNharvestedTonnage());
        holder.txtstatustxt.setText(plotDetails.getVstatusName());

        if (!plotDetails.getNstatus().equals("1") || isclose.equals("2")) {
            holder.btncomplete.setVisibility(View.GONE);
        } else {
            holder.btncomplete.setVisibility(View.VISIBLE);
        }
        holder.btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(plotDetails.getNhangamCode()) < 6) {
                    Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                    dialog.setContentView(R.layout.popup_transfer);

                    int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);

                    dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

                    AppCompatEditText edtarea = dialog.findViewById(R.id.edtarea);
                    edtarea.setText(plotDetails.getNtentativeArea());

                    InputFilterMinMax inputFilterarea = new InputFilterMinMax(0, Double.parseDouble(plotDetails.getNtentativeArea()), 2);
                    edtarea.setFilters(new InputFilter[]{inputFilterarea});

                    final int[] prevlen = {0};
                    edtarea.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String area = edtarea.getText().toString();
                            if (area.length() == 1) {
                                if (area.charAt(0) == '.') {
                                    edtarea.setText("0.");
                                    edtarea.setSelection(edtarea.getText().toString().length());
                                } else if (prevlen[0] < 1) {
                                    edtarea.setText(edtarea.getText().toString() + ".");
                                    edtarea.setSelection(edtarea.getText().toString().length());
                                }
                            }
                            prevlen[0] = edtarea.length();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    edtarea.setText(holder.txtareatxt.getText().toString());

                    edtarea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            if (!hasFocus) {
                                String area = edtarea.getText().toString();
                                ServerValidation sv = new ServerValidation();
                                if (area.endsWith(".")) {
                                    area = area.replace(".", "");
                                }
                                if (sv.checkFloat(area)) {
                                    double darea = Double.parseDouble(area);
                                    double originalArea = Double.parseDouble(holder.txtareatxt.getText().toString());
                                    if (darea > originalArea) {
                                        edtarea.setError(String.format(activity.getResources().getString(R.string.errorplotarea), String.valueOf(originalArea)));
                                        edtarea.setText("");
                                    } else {
                                        DecimalFormat df = new DecimalFormat("#0.00");
                                        edtarea.setText(MarathiHelper.convertMarathitoEnglish(df.format(darea)));
                                    }
                                }
                            }
                        }
                    });

                    AppCompatButton btncancel = dialog.findViewById(R.id.btncancel);
                    AppCompatButton btnconfirm = dialog.findViewById(R.id.btnconfirm);
                    btnconfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean isValid = true;
                            String area = edtarea.getText().toString();
                            ServerValidation sv = new ServerValidation();
                            if (area.endsWith(".")) {
                                area = area.replace(".", "");
                            }
                            if (sv.checkFloat(area)) {
                                double darea = Double.parseDouble(area);
                                double originalArea = Double.parseDouble(holder.txtareatxt.getText().toString());
                                if (darea > originalArea) {
                                    edtarea.setError(String.format(activity.getResources().getString(R.string.errorplotarea), String.valueOf(originalArea)));
                                    edtarea.setText("");
                                    isValid = false;
                                } else {
                                    DecimalFormat df = new DecimalFormat("#0.00");
                                    edtarea.setText(MarathiHelper.convertMarathitoEnglish(df.format(darea)));
                                }
                            }
                            if (isValid)
                                closePlot("1", holder.getAdapterPosition(), dialog, area);
                        }
                    });
                    btncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            closePlot("2", holder.getAdapterPosition(), dialog, null);
                        }
                    });

                    dialog.show();
                } else {
                    closePlot("2", holder.getAdapterPosition(), null, null);
                }
            }
        });
    }

    private void closePlot(String transfer, int position, Dialog dialog, String area) {
        CompletePlotDetails plotDetails = completePlotDetails.get(position);
        JSONObject job = new JSONObject();
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{activity.getResources().getString(R.string.chitboyprefuniquestring), activity.getResources().getString(R.string.chitboyprefchit_boy_id), activity.getResources().getString(R.string.prefseason)};
        String[] value = new String[]{"", "0", "{}"};
        String[] data = cf.getSharedPrefValue(activity, key, value);

        try {
            job.put("transfer", transfer);
            job.put("nplotNo", plotDetails.getNplotNo());
            job.put("vyearId", data[2]);
            job.put("dmindate", plotDetails.getDmindate());
            job.put("narea", area);

            String action = activity.getResources().getString(R.string.actioncloseandtransfer);
            String servlet = activity.getResources().getString(R.string.servletharvdata);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            RetrofitHandler<CloseTransferResponse> extraplotrequesthandler = new RetrofitHandler<>();
            Call<CloseTransferResponse> call2 = apiInterface.transferPlot(action, MarathiHelper.convertMarathitoEnglish(job.toString()), cf.getImei(activity), data[0], data[1], versioncode);
            extraplotrequesthandler.handleRetrofit(call2, CompletePlotAdapter.this, RequestEnum.CLOSEPLOT, servlet, activity, versioncode, dialog, transfer, position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return completePlotDetails.size();
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        if (requestCaller == RequestEnum.CLOSEPLOT) {
            CloseTransferResponse actionResponse = (CloseTransferResponse) response.body();
            if (actionResponse.isActionComplete()) {
                Constant.showToast(actionResponse.getSuccessMsg() != null ? actionResponse.getSuccessMsg() : activity.getResources().getString(R.string.savesucess), activity, R.drawable.ic_wrong);
                if (obj[0] != null && ((Dialog) obj[0]).isShowing()) {
                    ((Dialog) obj[0]).cancel();
                }
                int pos = (int) obj[2];
                CompletePlotDetails completePlotDetail = completePlotDetails.get(pos);
                completePlotDetail.setNstatus(actionResponse.getNstatusNew());
                completePlotDetail.setVstatusName(actionResponse.getVstatusNameNew());
                completePlotDetails.set(pos, completePlotDetail);
                notifyDataSetChanged();
            } else {
                Constant.showToast(actionResponse.getFailMsg() != null ? actionResponse.getFailMsg() : activity.getResources().getString(R.string.savefail), activity, R.drawable.ic_wrong);
            }
        }

    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}