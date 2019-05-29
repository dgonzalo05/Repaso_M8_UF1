package com.example.repaso;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class Login_Fragment extends Fragment {

    private LoginListener mListener;

    Button loginButton;
    EditText emailInput, passwordInput;


    public Login_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_register_, container, false);

        loginButton = vista.findViewById(R.id.register_button);
        emailInput = vista.findViewById(R.id.email);
        passwordInput = vista.findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.login(emailInput.getText().toString(), passwordInput.getText().toString());
            }
        });
        return vista;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            mListener = (LoginListener) context;
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

    public interface LoginListener {
        void login(String email, String password);
    }
}
