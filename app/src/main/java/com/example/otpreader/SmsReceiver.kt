package com.example.otpreader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.provider.Telephony.Sms.Intents.getMessagesFromIntent
import android.util.Log

class SmsReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "projectlog"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Log.i(TAG, "new sms received")
            val smsMessages = getMessagesFromIntent(intent)
            for (message in smsMessages) {
                Log.i(TAG, "Message from ${message.displayOriginatingAddress} : ${message.messageBody}")
                sendNotification(context, message)
            }
        }
    }
}