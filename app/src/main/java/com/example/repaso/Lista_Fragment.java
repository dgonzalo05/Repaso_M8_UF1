package com.example.repaso;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Lista_Fragment extends Fragment {

    private ListaListener mListener;

    RecyclerView recyclerView;

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

        recyclerView = vista.findViewById(R.id.recycler_view);

        


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ListaListener {

    }
}
