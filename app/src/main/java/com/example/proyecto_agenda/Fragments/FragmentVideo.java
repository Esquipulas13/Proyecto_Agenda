package com.example.proyecto_agenda.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyecto_agenda.ObjetosYDaos.Archivo;
import com.example.proyecto_agenda.R;

public class FragmentVideo extends Fragment {
    private Archivo archivo;
    //WIDGETS
    private VideoView videoView;
    private Button btnReproducir;
    private Button btnPausa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_video,container,false);
        videoView=(VideoView) vista.findViewById(R.id.vdvwVideo);
        btnReproducir=(Button)vista.findViewById(R.id.btnReproducir);
        btnPausa=(Button)vista.findViewById(R.id.btnPausa);
        Uri uri= Uri.parse(archivo.getRuta());
        videoView.setVideoURI(uri);
        btnPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
            }
        });
        btnReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
            }
        });
        return vista;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

}
