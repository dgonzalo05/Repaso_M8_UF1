package com.example.repaso;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.ArrayList;
import java.util.zip.Inflater;


public class Lista_Fragment extends Fragment {

    private ListaListener mListener;

    RecyclerView recyclerView;
    miAdapter adapter;
    ArrayList<Libro> libros;


    public Lista_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lista_, container, false);
        libros = new ArrayList<Libro>();
        recyclerView = vista.findViewById(R.id.recycler_view);
        adapter = new miAdapter(libros);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return vista;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListaListener) {
            mListener = (ListaListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ListaListener {

    }

    public void addLibro(Libro libro){
        libros.add(libro);
        adapter.notifyDataSetChanged();
    }


    class miAdapter extends RecyclerView.Adapter<miAdapter.miViewHolder>{

        ArrayList<Libro> libros;
        public miAdapter(ArrayList<Libro> libros){
            this.libros = libros;
        }

        @NonNull
        @Override
        public miViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mi_view_holder,viewGroup,false);
            return new miViewHolder(vista);
        }

        @Override
        public void onBindViewHolder(@NonNull miViewHolder miViewHolder, int i) {
            miViewHolder.titulo.setText(libros.get(i).getTitulo());
            miViewHolder.autor.setText(libros.get(i).getAutor());
            miViewHolder.categoria.setText(libros.get(i).getCategoria());
        }

        @Override
        public int getItemCount() {
            return libros.size();
        }

        class miViewHolder extends RecyclerView.ViewHolder{
            TextView titulo, autor, categoria;

            public miViewHolder(@NonNull View itemView) {
                super(itemView);
                titulo = itemView.findViewById(R.id.book_title);
                autor = itemView.findViewById(R.id.book_author);
                categoria = itemView.findViewById(R.id.book_cat);

            }
        }
    }
}
