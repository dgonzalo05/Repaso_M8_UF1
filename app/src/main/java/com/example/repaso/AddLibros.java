package com.example.repaso;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class AddLibros extends Fragment {


    private addLibroListener mListener;
    EditText add_titulo, add_autor, add_categoria;
    Button add_book;
    public AddLibros() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_add_libros, container, false);
        add_titulo = vista.findViewById(R.id.add_titulo);
        add_autor = vista.findViewById(R.id.add_autor);
        add_categoria = vista.findViewById(R.id.add_categoria);
        add_book = vista.findViewById(R.id.add_book);

        add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.añadirLibro(add_titulo.getText().toString(),add_autor.getText().toString(),add_categoria.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("¡Has añadido un libro!").setTitle("Dialogo").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Me la pela", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return vista;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof addLibroListener) {
            mListener = (addLibroListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement addLibroListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface addLibroListener {
        void añadirLibro(String titulo, String autor, String categoria);
    }
}
