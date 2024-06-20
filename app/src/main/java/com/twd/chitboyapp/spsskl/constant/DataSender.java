package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.asynctask.DeletePlantation;
import com.twd.chitboyapp.spsskl.enums.RequestEnum;
import com.twd.chitboyapp.spsskl.interfaces.APIInterface;
import com.twd.chitboyapp.spsskl.interfaces.RetrofitResponse;
import com.twd.chitboyapp.spsskl.interfaces.UpdateRemain;
import com.twd.chitboyapp.spsskl.pojo.CanePlantationResponse;
import com.twd.chitboyapp.spsskl.pojo.PlantationBean;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class DataSender implements RetrofitResponse {

    UpdateRemain updateRemain = null;

    public void sendData(List<PlantationBean> plantationBeanList, Activity activity, UpdateRemain updateRemain) {
        this.updateRemain = updateRemain;
        if (plantationBeanList.size() > 0) {
            ArrayList<MultipartBody.Part> fileToUpload = getAllImageInMultipart(plantationBeanList, activity);
            ConstantFunction cf = new ConstantFunction();
            PartOperation partOperation = new PartOperation();

            String action = activity.getResources().getString(R.string.actionsavelagan);
            String msg = activity.getResources().getString(R.string.senddatatoserver);

            String[] key = new String[]{activity.getResources().getString(R.string.chitboyprefuniquestring), activity.getResources().getString(R.string.chitboyprefchit_boy_id)};
            String[] value = new String[]{"", "0"};
            String[] data = cf.getSharedPrefValue(activity, key, value);
            String servlet = activity.getResources().getString(R.string.servletsavePlantation);
            APIInterface apiInterface = APIClient.getClient(activity, servlet).create(APIInterface.class);
            String versioncode = cf.getVersionCode();
            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(plantationBeanList, new TypeToken<List<PlantationBean>>() {
            }.getType());

            if (!element.isJsonArray()) {
// fail appropriately
                return;
            }

            JsonArray jsonArray = element.getAsJsonArray();
            Call<CanePlantationResponse> call2 = apiInterface.appSendLaganNond(fileToUpload, partOperation.createPartFromString(action), partOperation.createPartFromString(MarathiHelper.convertMarathitoEnglish(jsonArray.toString())), partOperation.createPartFromString(cf.getImei(activity)), partOperation.createPartFromString(data[0]), partOperation.createPartFromString(data[1]), partOperation.createPartFromString(cf.getVersionCode()));
            RetrofitHandler<CanePlantationResponse> datasendcaller = new RetrofitHandler<>();
            datasendcaller.handleRetrofit(call2, DataSender.this, RequestEnum.SENDDATA, servlet, activity, versioncode);
        } else {
            Constant.showToast(activity.getResources().getString(R.string.errornoformavailable), activity, R.drawable.ic_wrong);
        }
    }

    private ArrayList<MultipartBody.Part> getAllImageInMultipart(List<PlantationBean> plantationBeanList, Activity activity) {
        int size = plantationBeanList.size();
        ArrayList<MultipartBody.Part> fileToUpload = new ArrayList<>();
        File root = activity.getExternalFilesDir("");
        File myDir = new File(root + Constant.foldername);

        for (int i = 0; i < size; i++) {
            try {
                PlantationBean plantationBean = plantationBeanList.get(i);
                if (plantationBean.getVphotoPath() != null) {
                    MultipartBody.Part part = getImagePart(activity, myDir, plantationBean.getVphotoPath());
                    if (part != null)
                        fileToUpload.add(part);
                }
                if (plantationBean.getVconfirmphotoPath() != null) {
                    MultipartBody.Part part = getImagePart(activity, myDir, plantationBean.getVconfirmphotoPath());
                    if (part != null)
                        fileToUpload.add(part);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileToUpload;
    }

    public MultipartBody.Part getImagePart(Activity activity, File myDir, String vphotoPath) {
        if (myDir == null) {
            File root = activity.getExternalFilesDir("");
            myDir = new File(root + Constant.foldername);
        }
        File imgFile = new File(myDir, vphotoPath);
        if (imgFile.exists()) {
            RequestBody requestBody = RequestBody.create(imgFile, MediaType.parse("*/*"));
            return MultipartBody.Part.createFormData("file", imgFile.getName(), requestBody);
        }
        return null;
    }

    @Override
    public void onResponse(Call call, Response response, RequestEnum requestCaller, Activity activity, Object... obj) {
        ConstantFunction cf = new ConstantFunction();
        if (requestCaller == RequestEnum.SENDDATA) {
            CanePlantationResponse canePlantationResponse = (CanePlantationResponse) response.body();
            ArrayList<String> removeentry = canePlantationResponse.getRemoveEntry();
            if (removeentry != null) {
                cf.showCustomAlert(activity, R.mipmap.ic_action_accept, canePlantationResponse.getSuccessMessage());
                if (removeentry.size() > 0) {
                    DeletePlantation mdp = new DeletePlantation(activity, removeentry);
                    mdp.delegate = updateRemain;
                    mdp.doInBackground();
                }
            } else {
                Constant.showToast(activity.getResources().getString(R.string.errorsomethingwrong), activity, R.drawable.ic_wrong);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, RequestEnum requestCaller, Object... obj) {

    }
}
