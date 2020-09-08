package com.example.fivecontacts.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.ArraySet;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.Contato;
import com.example.fivecontacts.main.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

public class Pick_Contacts extends AppCompatActivity implements UIEducacionalPermissao.NoticeDialogListener, BottomNavigationView.OnNavigationItemSelectedListener {

    boolean wasNameClicked = true;
    ArrayList<Contato> contactList = new ArrayList<>();
    ArrayList<Contato> searchList = new ArrayList<>();

    TextView tv;
    Button btSalvar;
    EditText name;
    User user;
    BottomNavigationView btn;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contacts);

        //Dados da Intent Anterior
        Intent quemChamou=this.getIntent();
        if (quemChamou!=null) {
            Bundle params = quemChamou.getExtras();
            if (params!=null) {
                //Recuperando o Usuario
                user = (User) params.getSerializable("usuario");
                if (user != null) {
                    contactList = user.getContactList();
                }
            }
        }

        btSalvar = findViewById(R.id.btSalvar);
        name = findViewById(R.id.editTextTextPersonName);
        btn = findViewById(R.id.bnv);
        lv = findViewById(R.id.listview);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences salvaContatos = getSharedPreferences("contatos", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = salvaContatos.edit();
                editor.putInt("numContatos", contactList.size());
                Log.v("PDM", "Num Pick_contatos: "+contactList.size());

                 try {
                     ByteArrayOutputStream dt;
                     ObjectOutputStream oos;
                     String contatoSerializado;
                     for (int i=0;i<contactList.size();i++){
                         Contato contact = contactList.get(i);
                         dt = new ByteArrayOutputStream();
                         oos = new ObjectOutputStream(dt);
                         oos.writeObject(contact);
                         contatoSerializado = dt.toString(StandardCharsets.ISO_8859_1.name());
                         Log.v("PDM", "Colocando contato "+ contatoSerializado +" no shared");
                         editor.putString("contato"+i, contatoSerializado);
                     }
                     if (user != null) {
                         user.setContactList(contactList);
                     }
                 } catch (UnsupportedEncodingException e) {
                     e.printStackTrace();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                Log.v("PDM", "commit");
                editor.commit();

                Intent intent = new Intent(Pick_Contacts.this, ListaDeContatos_ListView.class);
                intent.putExtra("usuario", user);
                startActivity(intent);
                //finish();
            }
        });
        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (wasNameClicked) {
                    wasNameClicked = (false);
                    name.setText("");
                }
                return false;
            }
        });

        btn.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("PDM","Matando a Activity Lista de Contatos");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("PDM","Matei a Activity Lista de Contatos");
    }

    public void onCLickSearch(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Log.v("PDM", "Tenho permissão");
            ContentResolver cr = getContentResolver();
            String query = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
            String [] queryArgs = {"%"+ name.getText() +"%"};
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, query, queryArgs, null);
            Contato contact;
            String [] listPhones = new String[0];
            final ArrayList<Contato> searchedContacts = new ArrayList<>();
            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
                int contactIdIndex = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
                String contactId = cursor.getString(contactIdIndex);
                String contactName = cursor.getString(nameIndex);

                contact = new Contato();
                contact.setName(contactName);

                String phonesQuery = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId;
                Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, phonesQuery, null, null);
                int j = 0;
                assert phones != null;
                listPhones= new String[phones.getCount()];
                while(phones.moveToNext()) {
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    listPhones[j] = phoneNumber;
                    j++;
                }
                contact.setPhoneNumbers(listPhones);
                searchedContacts.add(contact);
                searchList.add(contact);
            }
            ArrayList<String> contactNames = new ArrayList<>();
            for(int j = 0;j<searchList.size();j++){
                contactNames.add(searchList.get(j).getName());
            }
            String[] names = contactNames.toArray(new String[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final String[] finalListPhones = searchedContacts.get(i).getPhoneNumbers();
                    final String[] phoneNumber = {""};
                    Log.v("PDM", finalListPhones.length+"");
                    if (finalListPhones.length > 0) {
                        phoneNumber[0] = finalListPhones[0];
                    }
                    Log.v("PDM", "Clicou no " + i + " " + searchList.get(i).getName());
                    if (finalListPhones.length > 1) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Pick_Contacts.this);
                        mBuilder.setTitle("Escolha um número: ");
                        mBuilder.setSingleChoiceItems(finalListPhones, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.v("PDM", "Clicou no número " + finalListPhones[i]);
                                phoneNumber[0] = finalListPhones[i];
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                    }
                    Contato contact = searchList.get(i);
                    Log.v("PDM", phoneNumber[0]);
                    contact.setPhoneNumbers(phoneNumber);
                    contact.setSelectedPhoneNumber(phoneNumber[0]);
                    contactList.add(contact);
                    searchList = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Pick_Contacts.this, android.R.layout.simple_list_item_1, new String[0]);
                    lv.setAdapter(adapter);

                }
            });
            cursor.close();
        } else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                Log.v("PDM", "Pedir permissão read contacts");
                String message = "Nossa aplicação precisa acessar a lista de contatos";
                String title = "Permissão de acesso à lista de contatos";
                int code = 1;
                UIEducacionalPermissao permissionMessage = new UIEducacionalPermissao(message, title, code);
                permissionMessage.onAttach((Context)this);
                permissionMessage.show(getSupportFragmentManager(), "read_contacts");
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 3333);
            }

        }
    }

    @Override
    public void onDialogPositiveClick(int codigo) {
        Log.v("PDM", "Clicou no OK");
        if(codigo==1) {
            String[] permissions={Manifest.permission.READ_CONTACTS};
            ActivityCompat.requestPermissions(this, permissions, 3333);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.v("PDM", Integer.toString(item.getItemId()));
        Log.v("PDM", "test");
        if(item.getItemId()==R.id.anvLigar) {
            Intent newUserIntent = new Intent(Pick_Contacts.this, ListaDeContatos_ListView.class);
            startActivity(newUserIntent);
        }
        if (item.getItemId()==R.id.anvPerfil) {
            Intent newUserIntent = new Intent(Pick_Contacts.this, MudarDadosUsuario.class);
            startActivity(newUserIntent);
        }
        if(item.getItemId()==R.id.anvMudar) {
            Intent newUserIntent = new Intent(Pick_Contacts.this, Pick_Contacts.class);
            startActivity(newUserIntent);
        }
        finish();
        return true;
    }
}