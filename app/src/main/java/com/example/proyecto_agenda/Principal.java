package com.example.proyecto_agenda;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_agenda.ObjetosYDaos.DaoFicha;
import com.example.proyecto_agenda.ObjetosYDaos.Ficha;
import com.example.proyecto_agenda.ObjetosYDaos.FichaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;

public class Principal extends AppCompatActivity {

    private ArrayList<Ficha> lista=new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText txtBuscar;

    NotificationManager nm;
    Notification notif;
    static String ns = Context.NOTIFICATION_SERVICE;
    int icono_v = R.drawable.audio;
    int icono_r = R.drawable.video;


    public void notificacion(int icon, CharSequence textoEstado, CharSequence titulo, CharSequence texto) {
        // Capturo la hora del evento
        long hora = System.currentTimeMillis();

        // Definimos la accion de la pulsacion sobre la notificacion (esto es opcional)
        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(this, Principal.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        recyclerView=findViewById(R.id.rcclrFicha);
        FloatingActionButton agregar = (FloatingActionButton)findViewById(R.id.fabAgregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte=new Intent(getApplicationContext(), com.example.proyecto_agenda.Agregar.class);
                startActivity(inte);
            }
        });


        TextView acerca = (TextView)findViewById(R.id.lblacercade);
        acerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Proyecto final Movil 1", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Jesus Esquipulas Lopez Ariza", Toast.LENGTH_LONG).show();

            }
        });
        ActualizarRecycler();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_principal,menu);
        return true;
    }

    public void ActualizarRecycler(){
        DaoFicha dao=new DaoFicha(this);
        lista=dao.SeleccionarTodos();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        FichaAdapter adapter=new FichaAdapter(this,lista);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View n=v;
                AlertDialog.Builder menu=new AlertDialog.Builder(v.getContext());
                Resources res=getResources();
                CharSequence[] opciones= {res.getString(R.string.ver),res.getString(R.string.modificar),res.getString(R.string.eliminar)};
                menu.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int opcion) {
                        switch (opcion){
                            case 0:
                                Intent intent=new Intent(getApplicationContext(),mostrar.class);
                                intent.putExtra("titulo",lista.get(recyclerView.getChildAdapterPosition(n)).getTitulo());
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1=new Intent(getApplicationContext(),actualizar.class);
                                intent1.putExtra("titulo",lista.get(recyclerView.getChildAdapterPosition(n)).getTitulo());
                                startActivity(intent1);
                                break;
                            case 2:
                                DaoFicha daonuevo=new DaoFicha(getApplicationContext());
                                if(daonuevo.eliminar(lista.get(recyclerView.getChildAdapterPosition(n)))){
                                    Toast.makeText(getApplicationContext(),"Se elimino con exito", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),"No se elimino", Toast.LENGTH_LONG).show();
                                }
                                ActualizarRecycler();
                                break;
                        }
                    }
                });
                menu.create().show();
                return true;
            }
        });
    }
}
