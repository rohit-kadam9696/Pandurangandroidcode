package com.twd.chitboyapp.spsskl.printer;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.zj.btsdk.BluetoothService;

public class Bluetooth extends Application {
    private static final int REQUEST_ENABLE_BT = 4;
    private static final int REQUEST_CONNECT_DEVICE = 6;
    static Activity act = null;
    static Context context = null;
    static String bluetootMacAddress = null;
    static boolean printerConnected = false, btOnOffConnect = false, printerConnecting = false;
    public static BluetoothService mService = null;
    static BluetoothDevice con_dev = null;
    public final static Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //������
                            printerConnected = true;
					/*					Toast.makeText(context, "Printer connectined",
							Toast.LENGTH_SHORT).show();*/
                            printerConnecting = false;
                            break;
                        case BluetoothService.STATE_CONNECTING:  //��������
                            printerConnected = false;
                            printerConnecting = true;
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            printerConnected = false;
                            printerConnecting = false;
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //�����ѶϿ�����
				/*Toast.makeText(context, R.string.printer_connection_was_lost,
						Toast.LENGTH_SHORT).show();*/
                    printerConnected = false;
                    disconnect();
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //�޷������豸
                    printerConnected = false;
                    Toast.makeText(context, "Cannot connect printer",
                            Toast.LENGTH_SHORT).show();
                    disconnect();
                    break;
            }
        }
    };
    private static SharedPreferences pref = null;

    public static boolean isPrinterConnected(Context con, Activity act1) {
        context = con;
        act = act1;
        //return true;
        return printerConnected;
    }

    public static boolean isPrinterConnected() {
        return printerConnected;
    }

    public static BluetoothService getServiceInstance() {
        return mService;
    }

    public static void connectPrinter(Context con, Activity act1) {
        Log.v("HyperMan", "connect printer");
        context = con;
        act = act1;
		/*		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		act.registerReceiver(mReceiver, filter);*/
        if (printerConnecting) {
            Toast.makeText(context, "Trying to connect printer!!", Toast.LENGTH_LONG).show();
        } else {
            mService = new BluetoothService(context, mHandler);

            if (!mService.isAvailable()) {
                Toast.makeText(context, "Bluetooth not available", Toast.LENGTH_LONG).show();
            } else if (mService.isBTopen()) {
                if (bluetootMacAddress == null)
                    pairPrinter(con, act1);
                else {
					/*if(!btOnOffConnect){
					btOnOffConnect = true;
					IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
					context.registerReceiver(mReceiver, filter);
				}*/
                    con_dev = mService.getDevByMac(bluetootMacAddress);
                    mService.connect(con_dev);
                }
            } else if (!mService.isBTopen()) {
                enableBluetooth(con);
            }
        }
    }

    public static void enableBluetooth(Context con) {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(con, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        act.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    public static void pairPrinter(Context con, Activity act1) {
        context = con;
        act = act1;
		/*if(!btOnOffConnect){
			btOnOffConnect = true;
			IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			context.registerReceiver(mReceiver, filter);
		}*/
        Intent serverIntent = new Intent(context, DeviceListActivity.class);
        act.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public static void pairedPrinterAddress(Context con, Activity act1, String mac_address) {
        context = con;
        act = act1;

        bluetootMacAddress = mac_address;
        con_dev = mService.getDevByMac(mac_address);
        mService.connect(con_dev);
    }

    public static void disconnect() {
        if (mService != null && mService.isAvailable())
            mService.stop();
        mService = null;
        bluetootMacAddress = null;
        con_dev = null;
        btOnOffConnect = false;
        printerConnected = false;
        //context.unregisterReceiver(mReceiver);
    }

    //this is another extra receiver to check if BT is turned off suddenly to avoid force close dialog
	/*private final static BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
						BluetoothAdapter.ERROR);
				switch (state) {
				case BluetoothAdapter.STATE_OFF:
					disconnect();
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					disconnect();
					break;
				case BluetoothAdapter.STATE_ON:
					//setButtonText("Bluetooth on");
					break;
				case BluetoothAdapter.STATE_TURNING_ON:
					//setButtonText("Turning Bluetooth on...");
					break;
				}
			}
			else if(action.equals(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)){
				disconnect();
			}
			else if(action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)){
				disconnect();
			}
		}
	};*/
    protected void onDestroy() {
        if (mService != null)
            mService.stop();
        mService = null;
        //unregisterReceiver(mReceiver);
    }
}
