package com.example.fivecontacts.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.Contato;
import com.example.fivecontacts.main.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ListaDeContatos_ListView extends AppCompatActivity implements UIEducacionalPermissao.NoticeDialogListener, BottomNavigationView.OnNavigationItemSelectedListener {

    ListView lv;
    BottomNavigationView bnv;
    User user;
    String[] itens ={"Filha", "Filho", "Netinho"};
    String[] numeros ={"tel:000000003435","tel:2000348835","tel:1003435888" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contatos);

        lv= findViewById(R.id.listView1);
        preencherListaDeContatos(); //Montagem do ListView

        bnv = findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(this);


        //Dados da Intent Anterior
        Intent quemChamou=this.getIntent();
        if (quemChamou!=null) {
            Bundle params = quemChamou.getExtras();
            if (params!=null) {
                //Recuperando o Usuario
                user = (User) params.getSerializable("usuario");
                if (user != null) {
                    Log.v("PDM", user.getContactList().size()+"");
                    if (user.getContactList().size() > 0){

                    }
                    Log.v("pdm", user.getNome());
                } else {
                    Log.v("PDM", "User is null");
                }
            }
        }

    }

    protected void preencherListaDeContatos (){
        //Vamos montar o ListView
        final ArrayList<Contato> contactsList = new ArrayList<>();
        SharedPreferences recoverContacts = getSharedPreferences("contatos2", Activity.MODE_PRIVATE);
        int num = recoverContacts.getInt("numContatos", 0);
        Log.v("PDM", "Num listacontatos: "+num);
        Contato contact;
        for (int i=1;i<=num;i++) {
            String serializedObject = recoverContacts.getString("contato0"+i,"");
            Log.v("PDM", "Objeto Serializado: "+ serializedObject);
            if (serializedObject.compareTo("") != 0) {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject.getBytes(StandardCharsets.ISO_8859_1.name()));
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    contact = (Contato) ois.readObject();
                    if (contact!=null) {
                        Log.v("PDM", contact.getName());
                        contactsList.add(contact);
                    } else {
                        Log.v("PDM", "Contato é null");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        ArrayList<String> contactNames = new ArrayList<>();
        for(int j = 0;j<num;j++){
            if (contactsList.size() > j) {
                contactNames.add(contactsList.get(j).getName());
            }
        }
        String[] names = contactNames.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ListaDeContatos_ListView.this, "No item: "+i, Toast.LENGTH_LONG).show();
                Intent intent;
                Log.v("PDM", contactsList.get(i).getPhoneNumber());
                Uri uri = Uri.parse("tel:+"+contactsList.get(i).getPhoneNumber());
                if (isCallAllowed()) {
                    intent = new Intent(Intent.ACTION_CALL, uri);
                } else {
                    intent = new Intent(Intent.ACTION_DIAL, uri);
                }
                startActivity(intent);
            }
        });

    }

    protected  boolean isCallAllowed() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            Log.v("PDM", "Tenho Permissão");
            return true;
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                Log.v("PDM", "Primeira vez");

                String message = "Nossa aplicação precisa acessar o telefone para discagem automática. Uma janela de permisão será apresentada em seguida";
                String title = "Permissão de acesso a chamadas";
                int code = 1;
                UIEducacionalPermissao permissionMessage = new UIEducacionalPermissao(message, title, code);
                permissionMessage.onAttach((Context)this);
                permissionMessage.show(getSupportFragmentManager(), "primeiravez2");
            } else {
                Log.v("PDM", "Segunda vez");

                String message = "Nossa aplicação precisa acessar o telefone para discagem automática. Uma janela de permisão será apresentada em seguida";
                String title = "Permissão de acesso a chamadas";
                int code = 1;
                UIEducacionalPermissao permissionMessage = new UIEducacionalPermissao(message, title, code);
                permissionMessage.onAttach((Context)this);
                permissionMessage.show(getSupportFragmentManager(), "segundavez2");
            }

        }
        return false;
    }

    public void onDialogPositiveClick(int codigo) {
        Log.v("PDM", "Clicou no OK");
        if(codigo==1) {
            String[] permissions={Manifest.permission.CALL_PHONE};
            ActivityCompat.requestPermissions(this, permissions, 2222);
        }
    }


    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2222:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Valeu!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Que pena!", Toast.LENGTH_LONG).show();
                    String message =  "Seu aplicativo pode ligar diretamente, mas sem permissão não funciona. Se você marcou não permitir o seu aplicativo não poderá ligar diretamente.";
                    String title = "Porque precisamos da permissão?";
                    int code = 2;
                    UIEducacionalPermissao permissionMessage = new UIEducacionalPermissao(message, title, code);
                    permissionMessage.onAttach( (Context) this);
                    permissionMessage.show(getSupportFragmentManager(), "primeiravez");
                }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.anvLigar) {
            Intent newUserIntent = new Intent(ListaDeContatos_ListView.this, ListaDeContatos_ListView.class);
            startActivity(newUserIntent);
        }
        if (item.getItemId()==R.id.anvPerfil) {
            Intent newUserIntent = new Intent(ListaDeContatos_ListView.this, MudarDadosUsuario.class);
            startActivity(newUserIntent);
        }
        if(item.getItemId()==R.id.anvMudar) {
            Intent newUserIntent = new Intent(ListaDeContatos_ListView.this, Pick_Contacts.class);
            startActivity(newUserIntent);
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}