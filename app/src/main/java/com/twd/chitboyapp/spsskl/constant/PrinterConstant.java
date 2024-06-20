package com.twd.chitboyapp.spsskl.constant;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.twd.chitboyapp.spsskl.R;
import com.twd.chitboyapp.spsskl.pojo.SlipBeanR;
import com.twd.chitboyapp.spsskl.printer.Bluetooth;
import com.twd.chitboyapp.spsskl.printer.PrintMultilingualText;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrinterConstant {

    public static int REQUEST_ENABLE_BT = 4, REQUEST_CONNECT_DEVICE = 6;
    public static int effectivePrintWidth = 48;
    public static String printContent;

    public String getPageHeader() {
        StringBuilder printheader = new StringBuilder();
        printheader.append("<!DOCTYPE html><html><head><style>.center { margin-left: auto; margin-right: auto; } .txtcenter { text-align: center;} .txtright { text-align: right;} .head {font-size:8vw;margin:0px;padding:0px;} .subhead {font-size:7vw;margin:0px;padding:0px;} .cbody {font-size:6vw;margin:0px;padding:0px;} .cbodysmall {font-size:5vw;margin:0px;padding:0px;} .cfoot {font-size:4vw;margin:0px;padding:0px;text-align:left;float:left;} .cfoot2 {font-size:4vw;margin:0px;padding:0px;text-align:right;float:right;} .borderTop {border-top:1px solid #000;} .borderBottom {border-bottom:1px solid #000;}</style>");
        printheader.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /></head><body>");
        return printheader.toString();
    }

    public String getPrintHeader(String head, String head2) {
        StringBuilder printheader = new StringBuilder();
        printheader.append("<center><label class=\"cbody\"><b>कर्मयोगी सुधाकरपंत परिचारक<br>पांडुरंग सह.सा.का.लि.श्रीपूर</b></label></center>");
        if (head != null)
            printheader.append("<center><label class=\"subhead\"><b>").append(head).append("</b></label></center>");
        if (head2 != null)
            printheader.append("<center><label class=\"subhead\"><b>").append(head2).append("</b></label></center>");
        return printheader.toString();
    }

    public String getPrintFooter(Activity context) {
        ConstantFunction cf = new ConstantFunction();
        String[] data = cf.getSharedPrefValue(context, new String[]{context.getResources().getString(R.string.chitboyprefname)}, new String[]{""});
        return "<br><table style=\"width:100%\"><tr><td class=\"cfoot\">&copy;3WDSoft</td><td class=\"cfoot2\">सही (" + data[0] + ")</td></tr></table><br>";
    }

    public String getPageFooter() {
        return "<br><br><br><span>.</span></body></html>";
    }

    public void print(Activity activity) {
        if (Bluetooth.isPrinterConnected(activity, activity)) {
            if (printContent != null && !printContent.trim().equalsIgnoreCase("")) {
                // htmlContent += new ConstantFunction().getPrintFooter(HomeActivity.this);
                if (Bluetooth.isPrinterConnected(activity, activity)) {
                    String dirpath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/BluetoothPrint";
                    BluetoothService mService = Bluetooth.getServiceInstance();
                    PrintMultilingualText pml = new PrintMultilingualText(activity, activity, mService, dirpath, PrinterConstant.effectivePrintWidth);
                    pml.startPrinting(printContent);
                    //ConstantFunction.putLastRecipt("", HomeActivity.this);
                    printContent = "";
                } else {
                    //Printer not connected and send request for connecting printer
                    Bluetooth.connectPrinter(activity, activity);
                }
            } else {
                Constant.showToast(activity.getResources().getString(R.string.billnotavailable), activity, android.R.drawable.ic_delete);
            }
        } else {
            Bluetooth.connectPrinter(activity, activity);
            Constant.showToast(activity.getResources().getString(R.string.printererror), activity, android.R.drawable.ic_delete);
        }
    }


    public ArrayList<String> generatePrintView(Activity activity, List<SlipBeanR> slipBeanRList, String date, String head2, boolean print, boolean currentDate) {
        ArrayList<String> dataPrint = new ArrayList<>();
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{activity.getResources().getString(R.string.prefharvslipdata)};
        String[] value = new String[]{"{}"};
        String[] data = cf.getSharedPrefValue(activity, key, value);
        StringBuilder printView = new StringBuilder();
        int size = slipBeanRList.size();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Date curdate = new Date();
        String curdatetime = sdf.format(curdate);
        if (size > 0) {
            try {
                JSONObject job = new JSONObject(data[0]);
                String printheader = getPrintHeader(activity.getResources().getString(R.string.uspavati), head2);
                String printfooter = getPrintFooter(activity);
                String lineBreak = "<hr style=\"height:1px; background-color:#000; border:none;\">";

                String plotno = job.has("nplotNo") ? String.valueOf(job.getInt("nplotNo")) : "";
                String village = job.has("villageName") ? job.getString("villageName") : "";
                String code = job.has("nentityUniId") ? job.getString("nentityUniId") : "";
                String name = job.has("farmername") ? job.getString("farmername") : "";
                String vehicalTypeId = job.has("vehicleType") ? job.getString("vehicleType") : "";
                String vehicalTypeName = job.has("vehicalTypeName") ? job.getString("vehicalTypeName") : "";
                String harvType = job.has("harvetortype") ? job.getString("harvetortype") : "";
                String vvarietyName = job.has("vvarietyName") ? job.getString("vvarietyName") : "";
                String harvester = (job.has("nharvestorId") ? job.getString("nharvestorId") : "") + " " + (job.has("harvetorname") ? job.getString("harvetorname") : "");
                String transpoter = (job.has("ntransportorId") ? job.getString("ntransportorId") : "") + " " + (job.has("transportername") ? job.getString("transportername") : "");
                String mukadam = (job.has("nbulluckcartMainId") ? job.getString("nbulluckcartMainId") : "") + " " + (job.has("bulluckcartMainName") ? job.getString("bulluckcartMainName") : "");
                String wirerope = job.has("wireropeName") ? job.getString("wireropeName") : "";
                String fronttrailer = job.has("tailorFrontName") ? job.getString("tailorFrontName") : "";
                String backtrailer = job.has("tailorBackName") ? job.getString("tailorBackName") : "";
                String remark = job.has("remarkName") ? job.getString("remarkName") : "";
                String slipboy = job.has("slipboyname") ? job.getString("slipboyname") : "";
                JSONArray bulluckdata = job.has("bulluckdata") ? job.getJSONArray("bulluckdata") : new JSONArray();

                boolean groupEnd = true;
                printView.append(getPageHeader());
                for (int i = 0; i < size; i++) {
                    if (i != 0) {
                        printView.append(lineBreak);
                    }
                    if (print && i % 5 == 0) {
                        printView = new StringBuilder();
                        printView.append(getPageHeader());
                        groupEnd = false;
                    }
                    SlipBeanR slipBeanR = slipBeanRList.get(i);
                    printView.append(printheader);
                    printView.append("<table style=\"width:100%;\" class=\"center\">").append("<tr><td class=\"cbody\"><b>भरलेली दिनांक </b></td><td class=\"subhead\"> : ").append(slipBeanR.getSlipDate() != null ? slipBeanR.getSlipDate() : date).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>प्रिंट वेळ</b></td><td class=\"subhead\"> : ").append((currentDate) ? curdatetime : slipBeanR.getSlipDate()).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>ऊस जात</b></td><td class=\"subhead\"> : ").append(vvarietyName).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>स्लिप नंबर </b></td><td class=\"subhead\"> : ").append(slipBeanR.getNslipNo()).append(" </td></tr>").append("<tr><td colspan=\"2\" class=\"subhead\"> ").append(" </td></tr>").append("<tr><td class=\"cbody\"><b>प्लॉट नंबर </b></td><td class=\"subhead\"> : ").append(plotno).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>गाव </b></td><td class=\"subhead\"> : ").append(village).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>कोड </b></td><td class=\"subhead\"> : ").append(code).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>नाव </b></td><td class=\"subhead\"> : ").append(name).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>वाहन </b></td><td class=\"subhead\"> : ").append(vehicalTypeName).append(" </td></tr>");
                    if (vehicalTypeId.equals("1") || vehicalTypeId.equals("2")) {
                        printView.append("<tr><td class=\"cbody\"><b>टोळी </b></td><td class=\"subhead\"> : ").append(harvType).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>तोडणीदार </b></td><td class=\"subhead\"> : ").append(harvester).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>वाहतूकदार </b></td><td class=\"subhead\"> : ").append(transpoter).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>वाहन नं. </b></td><td class=\"subhead\"> : ").append(job.has("vvehicleNo") ? job.getString("vvehicleNo") : "").append(" </td></tr>");
                    } else {
                        int bullsize = bulluckdata.length();
                        String gadiwan = slipBeanR.getNbullokCode();
                        String vehicleNo = "";
                        for (int j = 0; j < bullsize; j++) {
                            JSONObject gadiwanObj = bulluckdata.getJSONObject(j);
                            String bulluckString = gadiwanObj.has("object") ? gadiwanObj.getString("object") : "{}";
                            JSONObject bulluck = new JSONObject(bulluckString);
                            if (bulluck.has("code") && gadiwan.equals(bulluck.getString("code"))) {
                                gadiwan = (bulluck.has("name") ? bulluck.getString("name") : "");
                                vehicleNo = bulluck.has("vehicleNo") ? bulluck.getString("vehicleNo") : "";
                                bulluckdata.remove(j);
                                break;
                            }
                        }
                        printView.append("<tr><td class=\"cbody\"><b>मुकादम </b></td><td class=\"subhead\"> : ").append(mukadam).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>गाडीवान </b></td><td class=\"subhead\"> : ").append(gadiwan).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>वाहन नं. </b></td><td class=\"subhead\"> : ").append(vehicleNo).append(" </td></tr>");
                    }
                    if (vehicalTypeId.equals("1"))
                        printView.append("<tr><td class=\"cbody\"><b>वायर रोप</b></td><td class=\"subhead\"> : ").append(wirerope).append(" </td></tr>");
                    else if (vehicalTypeId.equals("2") || vehicalTypeId.equals("4")) {
                        printView.append("<tr><td class=\"cbody\"><b> ट्रेलर पु. </b></td><td class=\"subhead\"> : ").append(fronttrailer).append(" </td></tr>").append("<tr><td class=\"cbody\"><b> ट्रेलर मा.  </b></td><td class=\"subhead\"> : ").append(backtrailer).append(" </td></tr>");
                    }
                    printView.append("<tr><td class=\"cbody\"><b> शेरा </b></td><td class=\"subhead\"> : ").append(remark).append(" </td></tr>").append("<tr><td class=\"cbody\"><b>स्लिप बॉय </b></td><td class=\"subhead\"> : ").append(slipboy).append(" </td></tr>").append("</table>");

                    Bitmap bitmap = encodeAsBitmap(slipBeanR.getNslipNo() + slipBeanR.getExtraQr());
                    if (bitmap != null) {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        String image = "data:image/png;base64," + imgageBase64;
                        // Use image for the img src parameter in your html and load to webview
                        printView.append("<img src='").append(image).append("' />");
                    }

                    printView.append(printfooter);
                    if (print && i % 5 == 4) {
                        printView.append(getPageFooter());
                        dataPrint.add(printView.toString());
                        groupEnd = true;
                    }
                }
                if (print && !groupEnd) {
                    printView.append(getPageFooter());
                    dataPrint.add(printView.toString());
                }
                printView.append(getPageFooter());
                if (!print) {
                    /*PrinterConstant.printContent = printView.toString();
                    print(activity);*/
                    dataPrint.add(printView.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataPrint;
    }

    private Bitmap encodeAsBitmap(String str) {
        Bitmap bitmap = null;
        QRCodeWriter writer = new QRCodeWriter();
        // Code128Writer writer = new Code128Writer();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, effectivePrintWidth * 4, effectivePrintWidth * 4);
            // bitMatrix = writer.encode(str, BarcodeFormat.CODE_128, effectivePrintWidth * 8, (int) Math.round(effectivePrintWidth * 8 * 0.15));
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        int w = bitMatrix.getWidth();
        int h = bitMatrix.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixels[y * w + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;

    }

    public boolean connectPrinter(Activity activity) {
        if (!Bluetooth.isPrinterConnected(activity, activity)) {
            ConstantFunction cf = new ConstantFunction();
            String[] data = cf.getSharedPrefValue(activity, new String[]{activity.getResources().getString(R.string.preflastprinter)}, new String[]{""});
            Bluetooth.mService = new BluetoothService(activity.getApplicationContext(), Bluetooth.mHandler);
            if (!data[0].equals("")) {
                if (Bluetooth.mService.isBTopen()) {
                    Bluetooth.pairedPrinterAddress(activity.getApplicationContext(), activity, data[0]);
                } else {
                    Bluetooth.enableBluetooth(activity.getApplicationContext());
                }
            } else {
                Bluetooth.pairPrinter(activity.getApplicationContext(), activity);
            }
            return false;
        } else {
            return true;
        }
    }

    public String generatePrintByHTML(Activity activity, String htmlContent, String head, String head2) {
        String html = getPageHeader();
        html += getPrintHeader(head, head2);
        html += htmlContent;
        html += getPrintFooter(activity);
        html += getPageFooter();
        return html;
    }
}