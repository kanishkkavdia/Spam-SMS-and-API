package com.example.smsapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.example.smsapp.R
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONObject
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

import android.widget.TextView
import org.json.JSONArray

class MainActivity : AppCompatActivity()
{
    private val CHANNEL_ID="channel_id_example_01"
    private val notificationId=101

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS),111)
            recieveMsg()
        }
        else
            recieveMsg()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED )
            recieveMsg()
    }

    private fun recieveMsg() {
        var br=object:BroadcastReceiver(){
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onReceive(p0: Context?, p1: Intent?) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                    for(sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)){
//                        Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_LONG).show()

                        createNotification()
                        channelCheck(sms)

                    }
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(){
        val name="Notification Title"
        val descriptionText="Notification Description"
        val importance=NotificationManager.IMPORTANCE_DEFAULT
        val channel=NotificationChannel(CHANNEL_ID,name,importance).apply{
            description=descriptionText
        }
        val notificationManager:NotificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


    }

    private fun sendNotification(contact:String?,text:String){
        val builder=NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(contact)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }
    }

    private fun channelCheck(sms:SmsMessage) {
        val url = "http://192.168.43.101:5000/api/"
        val list = ArrayList<String?>()
        var message=sms.displayMessageBody
        list.add(message)
        val jsArray = JSONArray(list)
        var textbox:TextView=findViewById(R.id.textView3)
//        textbox.text=message
        val request=JsonArrayRequest(Request.Method.POST,url,jsArray,
            { response ->
                Log.d("response",response.toString())
                // Process the json
//                try {
                    Handler().postDelayed({
                        TODO("Do something")
                    }, 20000)
//                    sendNotification(sms.originatingAddress,message)
//                    Log.d("response","something is there buddy")
//
//                }
//                catch(exception: NumberFormatException){
//                    //asd
//                }

            }, {
                // Error in request
                Log.d("error","error")
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(request)

    }
}


