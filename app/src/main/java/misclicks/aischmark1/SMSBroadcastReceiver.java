package misclicks.aischmark1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Gourab Singha on 29-04-2017.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                    String message_cap = message.toUpperCase();
                    String[] key={"TODAY","TOMORROW","TONIGHT","AM","PM","CLOCK"};
                    int check = 0;

                    for (int j=0;j<3;j++)
                    {
                        int intIndex = message_cap.indexOf(key[j]);
                        if (intIndex != -1)
                            check++;
                    }
                    if (check!=0) {
                        // Show Alert
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context,
                                "senderNum: " + senderNum + ", message: " + message, duration);
                        toast.show();
                        NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.cal)
                                        .setContentTitle("Event detected")
                                        .setContentText(message);

                        Intent notificationIntent = new Intent(context, event_create.class);
                        notificationIntent.putExtra("message","");
                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(contentIntent);

                        // Add as notification
                        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(0, builder.build());
                    }
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

}