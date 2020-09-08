package com.example.fivecontacts.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MudarDadosUsuario extends AppCompatActivity {

    EditText edUser;
    EditText edPass;
    EditText edNome;
    EditText edEmail;
    Switch swLogado;

    Button btModificar;
    Button btLogout;
    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_usuario);

        btModificar=findViewById(R.id.btCriar);
        btLogout=findViewById(R.id.logoutButton);
        bnv=findViewById(R.id.bnv);
        bnv.setSelectedItemId(R.id.anvPerfil);

        edUser=findViewById(R.id.edT_Login2);
        edPass=findViewById(R.id.edt_Pass2);
        edNome=findViewById(R.id.edtNome);
        edEmail=findViewById(R.id.edEmail);
        swLogado=findViewById(R.id.swLogado);

        SharedPreferences hasuser = getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        String loginSalvo = hasuser.getString("login", "");
        String senhaSalvo = hasuser.getString("senha", "");
        String emailSalvo = hasuser.getString("email", "");
        String nomeSalvo = hasuser.getString("nome", "");
        Boolean manterlogado = hasuser.getBoolean("manterLogado", false);

        edEmail.setText(emailSalvo);
        edNome.setText(nomeSalvo);
        edPass.setText(senhaSalvo);
        edPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edUser.setText(loginSalvo);
        swLogado.setChecked(manterlogado);

        btModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome, login, senha;
                nome = edNome.getText().toString();
                login = edUser.getText().toString();
                senha = edPass.getText().toString();

                //Novos componentes
                String email;
                email = edEmail.getText().toString();
                boolean manterLogado;
                manterLogado= swLogado.isChecked();


                SharedPreferences salvaUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
                SharedPreferences.Editor escritor= salvaUser.edit();

                escritor.putString("nome",nome);
                escritor.putString("senha",senha);
                escritor.putString("login",login);

                //Escrever no SharedPreferences
                escritor.putString("email",email);
                escritor.putBoolean("manterLogado",manterLogado);

                //Falta Salvar o E-mail

                escritor.commit(); //Salva em Disco

                Toast.makeText(MudarDadosUsuario.this,"Modificações Salvas",Toast.LENGTH_LONG).show() ;

                finish();
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MudarDadosUsuario.this,MainActivity.class);
                intent.putExtra("automaticLogout", false);
                startActivity(intent);
            }
        });

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.v("PDM", Integer.toString(item.getItemId()));
                Log.v("PDM", "test");
                if(item.getItemId()==R.id.anvLigar) {
                    Intent newUserIntent = new Intent(MudarDadosUsuario.this, ListaDeContatos_ListView.class);
                    startActivity(newUserIntent);
                }
                if (item.getItemId()==R.id.anvPerfil) {
                    Intent newUserIntent = new Intent(MudarDadosUsuario.this, MudarDadosUsuario.class);
                    startActivity(newUserIntent);
                }
                if(item.getItemId()==R.id.anvMudar) {
                    Intent newUserIntent = new Intent(MudarDadosUsuario.this, Pick_Contacts.class);
                    startActivity(newUserIntent);
                }
                finish();
                return true;
            }
        });
    }

}