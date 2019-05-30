package com.example.repaso;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity implements Register_Fragment.RegisterListener, Login_Fragment.LoginListener, AddLibros.addLibroListener, Lista_Fragment.ListaListener, webViewFragment.webListener {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Libro> libros;

    ChildEventListener childEventListener;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Firebase para login
        mAuth = FirebaseAuth.getInstance();

        // Firebase de base de datos
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("libros");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Libro libro = dataSnapshot.getValue(Libro.class);
                if (libro != null){
                    libros.add(libro);
                    Lista_Fragment lista_fragment = (Lista_Fragment) getSupportFragmentManager().findFragmentByTag("Lista");
                    if (lista_fragment != null){
                        lista_fragment.addLibro(libro);
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }

    public void updateUI(FirebaseUser currentUser){
        if (currentUser != null){
            displayList();
        } else{
            Fragment fragment = new Register_Fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }

    }

    private void displayList() {
        libros = new ArrayList<Libro>();

        myRef.child("lista").removeEventListener(childEventListener);
        Fragment fragment = new Lista_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "Lista").commit();
        myRef.child("lista").addChildEventListener(childEventListener);


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_list:
                displayList();
                return true;
            case R.id.toolbar_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Register_Fragment()).commit();
                return true;
            case R.id.toolbar_book:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AddLibros()).commit();
                return true;
            case R.id.toolbar_webView:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new webViewFragment(), "webView").commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Registro completado", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error de registro", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login completado", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error en login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void añadirLibro(String titulo, String autor, String categoria) {
        myRef.child("lista").push().setValue(new Libro(titulo,autor,categoria));
    }

    @Override
    public void openWebView() {
        MiThread thread = new MiThread();
        thread.execute("https://bathtubs.com/");
    }

    // El primer argumento es el que pedirá el doInBackground
    // El último argumento es el que pedirá el onPostExecute
    public class MiThread extends AsyncTask<String, Void, String> {

        @Override
        // doInBackground pide String... porque tiene String en el Asynctask
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            URL url;
            String result = "";
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String data = bufferedReader.readLine();

                while(data != null){
                    result += data;
                    data = bufferedReader.readLine();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("RESULT ",result);
            return result;
        }
        @Override

        protected void onPostExecute(String data){
            super.onPostExecute(data);
            webViewFragment webViewFragment = (webViewFragment) getSupportFragmentManager().findFragmentByTag("webView");
            webViewFragment.loadData(data);
        }
    }
}
