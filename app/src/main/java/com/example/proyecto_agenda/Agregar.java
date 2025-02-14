package com.example.proyecto_agenda;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_agenda.Notificacion.AlarmReceiver;
import com.example.proyecto_agenda.Notificacion.PlanificarAlarma;
import com.example.proyecto_agenda.Notificacion.Utils;
import com.example.proyecto_agenda.ObjetosYDaos.Archivo;
import com.example.proyecto_agenda.ObjetosYDaos.ArchivoAdapter;
import com.example.proyecto_agenda.ObjetosYDaos.DaoArchivo;
import com.example.proyecto_agenda.ObjetosYDaos.DaoFicha;
import com.example.proyecto_agenda.ObjetosYDaos.DaoRecordatorio;
import com.example.proyecto_agenda.ObjetosYDaos.Ficha;
import com.example.proyecto_agenda.ObjetosYDaos.Recordatorio;
import java.util.ArrayList;
import java.util.Calendar;

public class Agregar extends AppCompatActivity {
    public Button btnAgregarAlarma;
    private TextView lblAlarma;
    private TextView txtTitulo;
    private TextView txtDescripcion;
    private RadioGroup rdg;
    private RadioButton rdNota;
    private RadioButton rdTarea;
    private ImageView imagen;
    private Button Archivos;
    private RecyclerView recyclerView;
    private TextView notificationsTime;
    private int alarmID = 1;
    private SharedPreferences settings;
    //private LinearLayout lnrRecordatorio;
    private String tipo;
    private ArrayList<Archivo> lista=new ArrayList<>();
    private ArrayList<Recordatorio> recordatorios=new ArrayList<>();
    private Button btnverrecordatorio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);



        btnAgregarAlarma=findViewById(R.id.btnAgregarAlarma);
        lblAlarma=findViewById(R.id.lblRecordatorio);
        txtTitulo=findViewById(R.id.txtTitulo);
        txtDescripcion=findViewById(R.id.txtDescripcion);
        imagen=findViewById(R.id.Imagen);
        recyclerView=findViewById(R.id.rcclcArchivoLista);
        rdg=findViewById(R.id.rdgTipo);
        rdNota=findViewById(R.id.rdNota);
        rdTarea=findViewById(R.id.rdTarea);
        btnAgregarAlarma.setEnabled(false);
        Archivos=findViewById(R.id.btnArchivos);


        btnverrecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               if(rdNota.isChecked()){
                   //lnrRecordatorio.setVisibility(View.INVISIBLE);
                   tipo="nota";
                   btnAgregarAlarma.setEnabled(false);
                   Archivos.setEnabled(true);
               }else{
                   //lnrRecordatorio.setVisibility(View.VISIBLE);
                   btnAgregarAlarma.setEnabled(true);
                   Archivos.setEnabled(false);
                   tipo="tarea";
               }


            }
        });
    }

    public void btnAgregarAlarmaOnClick(View v){
        Calendar fechaActual= Calendar.getInstance();
        Calendar fechaActual1= Calendar.getInstance();
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                lblAlarma.setText(lblAlarma.getText().toString()+"-"+hourOfDay+":"+minute);
            }
        },fechaActual1.get(Calendar.HOUR),fechaActual1.get(Calendar.MINUTE),false);
        timePickerDialog.show();
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                lblAlarma.setText(dayOfMonth+"-"+month+"-"+year);

            }
        },fechaActual.get(Calendar.YEAR),fechaActual.get(Calendar.MONTH),fechaActual.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    public void btnGuardarOnClick(View v){
        String titulo=txtTitulo.getText().toString();
        String descripcion=txtDescripcion.getText().toString();
        Calendar fechaActual= Calendar.getInstance();
        String fechaRegistro=fechaActual.get(Calendar.DAY_OF_MONTH)+"-"+fechaActual.get(Calendar.MONTH)+"-"+fechaActual.get(Calendar.YEAR);
        if(tipo.equals("nota")){
            Ficha ficha=new Ficha(titulo,descripcion,tipo,fechaRegistro,"","true");
            DaoFicha dao=new DaoFicha(getApplicationContext());
            DaoArchivo daoArchivo=new DaoArchivo(getApplicationContext());
            DaoRecordatorio daoRecordatorio=new DaoRecordatorio(getApplicationContext());
            if(dao.Insert(ficha)>0){
                Toast.makeText(this,"Nueva Nota Agregada", Toast.LENGTH_LONG).show();
                if(daoArchivo.insertarVariosArchivos(lista)){
                    for (int i=0; i<recordatorios.size();i++){
                        recordatorios.get(i).setTitulo(txtTitulo.getText().toString());
                        if(daoRecordatorio.Insert(recordatorios.get(i))>0) {
                            String cadena = recordatorios.toString();
                            int numero = cadena.length();
                            String hora, minuto;
                            int Hora, Minuto;
                            Toast.makeText(this,"el numero es:  "+ numero, Toast.LENGTH_LONG).show();
                            if(numero == 17){
                                hora = cadena.substring(11,13);
                                minuto = cadena.substring(14,16);
                            }else{
                                hora = cadena.substring(11,12);
                                minuto = cadena.substring(13,15);
                            }
                            Hora =Integer.parseInt(hora);
                            Minuto =Integer.parseInt(minuto);
                        }
                    }
                    Intent intent=new Intent(getApplicationContext(),Principal.class);
                    startActivity(intent);
                }
            }

        }else if(tipo.equals("tarea")){
            Ficha tarea=new Ficha(titulo,descripcion,tipo,fechaRegistro,lblAlarma.getText().toString(),"true");
            DaoFicha dao=new DaoFicha(getApplicationContext());
            DaoArchivo daoArchivo=new DaoArchivo(getApplicationContext());
            DaoRecordatorio daoRecordatorio=new DaoRecordatorio(getApplicationContext());
            if(dao.Insert(tarea)>0){
                Toast.makeText(this,"Nueva Nota Agregada", Toast.LENGTH_LONG).show();
                if(daoArchivo.insertarVariosArchivos(lista)){
                    for (int i=0; i<recordatorios.size();i++){
                        recordatorios.get(i).setTitulo(txtTitulo.getText().toString());
                        if(daoRecordatorio.Insert(recordatorios.get(i))>0) {
                            String cadena = recordatorios.toString();
                            String[] parts = cadena.split("-");
                            String part1 = parts[0];
                            Toast.makeText(this,"cadena" + part1, Toast.LENGTH_LONG).show();
                        }
                    }
                    Intent intent=new Intent(getApplicationContext(),Principal.class);
                    startActivity(intent);
                }
            }
        }
    }

    public void actualizarArchivos(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        final ArchivoAdapter adapter=new ArchivoAdapter(this,lista);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final View n=v;
                AlertDialog.Builder menu=new AlertDialog.Builder(v.getContext());
                Resources res=getResources();
                CharSequence[] opciones= {res.getString(R.string.eliminar)};
                menu.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int opcion) {
                        switch (opcion){
                            case 0:
                                lista.remove(lista.get(recyclerView.getChildAdapterPosition(n)));
                                actualizarArchivos();
                                break;
                        }
                    }
                });
                menu.create().show();
                return true;
            }
        });
    }

    public void btnArchivosOnClick(View v){
        final View vista=v;
        AlertDialog.Builder menu=new AlertDialog.Builder(v.getContext());
        Resources res=getResources();

        CharSequence[] opciones= {res.getString(R.string.audio),res.getString(R.string.video),res.getString(R.string.foto),res.getString(R.string.archivo)};
        menu.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int opcion) {
                switch (opcion){
                    case 0:
                        grabarAudio();
                        break;
                    case 1:
                        tomarVideo();
                        break;
                    case 2:
                        TomarImagen();
                        break;
                    case 3:
                        BuscarImagenes();
                        break;
                }
            }
        });
        menu.create().show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 1:
                    imagen.setImageURI(uri);
                    Archivo archivo=new Archivo(1,txtDescripcion.getText().toString(),"imagen",uri.toString(),txtTitulo.getText().toString());
                    lista.add(archivo);
                    actualizarArchivos();
                    break;
                case 2:
                    Uri ima=data.getData();
                    String cadena=ima.toString();
                    //Toast.makeText(this.getApplicationContext(),cadena,Toast.LENGTH_LONG).show();
                    Uri r= Uri.parse(cadena);
                    imagen.setImageURI(r);
                    Archivo archivo2=new Archivo(1,txtDescripcion.getText().toString(),"imagen",cadena,txtTitulo.getText().toString());
                    lista.add(archivo2);
                    actualizarArchivos();
                    break;
                case 3:
                    Uri vi=data.getData();
                    String cadena1=vi.toString();
                    Archivo archivo1=new Archivo(1,txtDescripcion.getText().toString(),"video",cadena1,txtTitulo.getText().toString());
                    lista.add(archivo1);
                    actualizarArchivos();
                    break;
                case 4:
                    Uri audio=data.getData();
                    String cadena4=audio.toString();
                    Archivo archivo4=new Archivo(1,txtDescripcion.getText().toString(),"audio",cadena4,txtTitulo.getText().toString());
                    lista.add(archivo4);
                    actualizarArchivos();
                    break;
            }
        }
    }



    Uri uri;
    public void TomarImagen(){
        Intent intent =new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        startActivityForResult(intent,1);
            Toast.makeText(getApplicationContext(),"Tome su foto", Toast.LENGTH_LONG).show();

    }
    public void tomarVideo(){
        Intent intent =new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        startActivityForResult(intent,3);
        Toast.makeText(getApplicationContext(),"Grabe su video", Toast.LENGTH_LONG).show();
    }
    public void grabarAudio(){
        Toast.makeText(getApplicationContext(),"Comience a grabar su audio", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent,4);
    }

    private void BuscarImagenes() {
        Toast.makeText(getApplicationContext(),"Seleccione la imagen", Toast.LENGTH_LONG).show();
        Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }

    public void MostarRecordatorios(View v){
        AlertDialog.Builder menu=new AlertDialog.Builder(v.getContext());
        CharSequence[] opciones= new CharSequence[recordatorios.size()];
        for (int i=0; i<opciones.length;i++){
            opciones[i]=recordatorios.get(i).toString();
        }
        menu.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int opcion){
                //Para eliminarlas
            }
        });
        menu.create().show();
    }

    public void AgregarRecordatorio(View v){
        Calendar fechaActual= Calendar.getInstance();
        Calendar fechaActual1= Calendar.getInstance();
        final Recordatorio recordatorio=new Recordatorio();
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                recordatorio.setHora(hourOfDay);
                recordatorio.setMinuto(minute);
                recordatorios.add(recordatorio);
            }
        },fechaActual1.get(Calendar.HOUR),fechaActual1.get(Calendar.MINUTE),false);
        timePickerDialog.show();
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                recordatorio.setDia(dayOfMonth);
                recordatorio.setMes(month);
                recordatorio.setAnio(year);
            }
        },fechaActual.get(Calendar.YEAR),fechaActual.get(Calendar.MONTH),fechaActual.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
