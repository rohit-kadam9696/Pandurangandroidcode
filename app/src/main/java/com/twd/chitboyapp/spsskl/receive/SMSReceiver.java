package com.twd.chitboyapp.spsskl.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.twd.chitboyapp.spsskl.OTP_Activity;

public class SMSReceiver extends BroadcastReceiver {

    // SmsManager class is responsible for all SMS related actions
    // final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Get the SMS message received
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                int pushlength = pdusObj.length;
                for (int i = 0; i < pushlength; i++) {
                    // This will create an SmsMessage object from the received pdu
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                    // Get sender phone number
                    //  String phoneNumber = sms.getDisplayOriginatingAddress();

                    //   String sender = phoneNumber;
                    String message = sms.getDisplayMessageBody();

                    OTP_Activity inst = OTP_Activity.instance();
                    if (inst != null)
                        inst.ReciveMessage(message);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
