package com.example.proyecto_agenda.Notificacion;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.proyecto_agenda.ObjetosYDaos.Ficha;
import com.example.proyecto_agenda.R;

import java.util.Calendar;

public class PlanificarAlarma extends BroadcastReceiver {
    Ficha ficha;
    String titulo="";
    @Override
    public void onReceive(Context context, Intent intent) {
        lanzarNotificacion(context);
        Bundle bundle=intent.getExtras();
        titulo=bundle.getString("titulo");
    }

    public void Notificacion(Context context, int hora, int minuto){
        AlarmManager alarmMgr;
         PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, com.example.proyecto_agenda.Principal.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);

    }

    public void lanzarNotificacion(Context context){
        Intent intent=new Intent(context, com.example.proyecto_agenda.Principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent= PendingIntent.getActivity(context,0,intent,0);
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context,"ficha")
                .setSmallIcon(R.drawable.nota)
                .setContentTitle("Ficha")
                .setContentText("Descripcion")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        int consecutivo=(int) System.currentTimeMillis()/1000;
        notificationManagerCompat.notify(1001,mBuilder.build());
    }

    public void setMensaje(Ficha ficha) {
        this.ficha = ficha;
    }
}
