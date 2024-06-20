package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.twd.chitboyapp.spsskl.ImageViewActivity;
import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.retrofit.APIClient;

import java.io.File;

public class PhotoViewer {

    public void openPhoto(String photopath, Activity activity, String internal) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_photo);

        final ProgressBar imgprogress = dialog.findViewById(R.id.imgprogress);
        final AppCompatImageView imgphoto = dialog.findViewById(R.id.imgphoto);

        if (photopath != null) {
            if (internal != null && internal.equals("1")) {
                File root = activity.getExternalFilesDir("");
                File myDir = new File(root + Constant.foldername);
                File file = new File(myDir, photopath);
                photopath = file.getPath();
                Picasso.get().load(file).into(imgphoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        imgphoto.setVisibility(View.VISIBLE);
                        imgprogress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        ConstantFunction cf = new ConstantFunction();
                        cf.showCustomAlert(activity, R.drawable.ic_wrong, activity.getResources().getString(R.string.errorphotonotfound));
                        dialog.cancel();
                    }
                });
            } else {
                String BaseURL = APIClient.getCurrentUrl(activity);
                photopath = BaseURL + APIClient.imagePath + photopath;
                Picasso.get().load(photopath)
                        .error(R.mipmap.ic_action_error)
                        .placeholder(R.mipmap.ic_launcher)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(imgphoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                imgphoto.setVisibility(View.VISIBLE);
                                imgprogress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                ConstantFunction cf = new ConstantFunction();
                                cf.showCustomAlert(activity, R.drawable.ic_wrong, activity.getResources().getString(R.string.errorphotonotfound));
                                dialog.cancel();
                            }
                        });
            }
        }


        String finalPhotopath = photopath;
        imgphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ImageViewActivity.class);
                intent.putExtra("url", finalPhotopath);
                intent.putExtra("internal", internal);
                activity.startActivity(intent);
                dialog.cancel();
            }
        });

        dialog.show();

    }
}
