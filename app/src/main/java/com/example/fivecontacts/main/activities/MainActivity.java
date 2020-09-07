package com.example.fivecontacts.main.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.ArraySet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.Contato;
import com.example.fivecontacts.main.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    boolean primeiraVezUser=true;
    boolean primeiraVezSenha=true;
    EditText edUser;
    EditText edPass;
    Button btLogar;
    Button btNovo;
    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (automaticLogUser()) {
            User user=montarObjetoUser();
            // Open contacts screen
            Intent intent = new Intent(MainActivity.this, ListaDeContatos_ListView.class);
            intent.putExtra("usuario",user);
            startActivity(intent);
        }

        btLogar=findViewById(R.id.btLogar);
        btNovo=findViewById(R.id.btNovo);
        edUser=findViewById(R.id.edT_Login);
        edPass=findViewById(R.id.edt_Pass);

        //Colocando Underline (Vamos usar esse campo mais na frente com o FireBase)
        mTextView=findViewById(R.id.tvEsqueceuSenha);
        mTextView.setPaintFlags(mTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //Evento de limpar Componente
            edUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(primeiraVezUser){
                    edUser.setText("");
                    primeiraVezUser=false;
                }
                return false;
            }
        });

        //Evento de limpar Componente
        edPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (primeiraVezSenha){
                    primeiraVezSenha=false;
                    edPass.setText("");
                    edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    );
                }
                return false;
            }
        });

        btLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Ao clicar deve-se:
                //1- Checar se existe um SharedPreferences
                //2- Comparar login e senha salvos
                //3- Se tudo der certo, resgatar lista de contatos
                //4- Abrir a Atividade lista de Contatos passando como parametro o objeto User e seus 5 Contatos

                SharedPreferences temUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
                String loginSalvo = temUser.getString("login","");
                String senhaSalva = temUser.getString("senha","");

                if ((loginSalvo!=null) && (senhaSalva!=null)){
                    //Recuperando da tela
                    String senha = edPass.getText().toString();
                    String login = edUser.getText().toString();

                    //Comparando
                    if ((loginSalvo.compareTo(login)==0)
                        &&(senhaSalva.compareTo(senha)==0)){

                        User user = montarObjetoUser();
                        //Abrindo a Lista de Contatos
                        Intent intent = new Intent(MainActivity.this, ListaDeContatos_ListView.class);
                        intent.putExtra("usuario",user);
                        startActivity(intent);



                    }else{
                        Toast.makeText(MainActivity.this,"Login e Senha Incorretos", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(MainActivity.this,"Login e Senha nulos", Toast.LENGTH_LONG).show();

                }

            }
        });

        //Novo Usuário
        btNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NovoUsuario.class);
                startActivity(intent);
            }
        });

   }

    private User montarObjetoUser() {
        SharedPreferences hasuser = getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        String loginSalvo = hasuser.getString("login", "");
        String senhaSalvo = hasuser.getString("senha", "");
        String emailSalvo = hasuser.getString("email", "");
        String nomeSalvo = hasuser.getString("nome", "");
        Set<String> names = hasuser.getStringSet("contactNames", new ArraySet<String>());
        Set<String> numbers = hasuser.getStringSet("contactNumbers", new ArraySet<String>());

        ArrayList<Contato> contactList = new ArrayList<>();

        Iterator nameIterator = names.iterator();
        Iterator numbersIterator = numbers.iterator();

        while(nameIterator.hasNext() && numbersIterator.hasNext()){
            Contato contact = new Contato(nameIterator.next().toString(), numbersIterator.next().toString());
            contactList.add(contact);
        }


        User user=new User(nomeSalvo,loginSalvo,senhaSalvo, emailSalvo, contactList);
        return user;
    }

    private boolean automaticLogUser(){
        Intent quemChamou=this.getIntent();
        if (quemChamou==null) {
            return  quemChamou.getBooleanExtra("automaticLogout", false);
        }
        SharedPreferences hasUser = getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
        return hasUser.getBoolean("manterLogado", false);
    }

}