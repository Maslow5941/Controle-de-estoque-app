package br.com.afinal.Model;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.afinal.Activity.LoginActivity;
import br.com.afinal.Helper.DBHelper;
import br.com.afinal.R;

public class SignUp extends AppCompatActivity {

    EditText username, password, repassword, email;
    Button signup, login;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.conpassword);
        signup = (Button) findViewById(R.id.btn);
        login= (Button) findViewById(R.id.btnLogin);

        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if(user.equals("")||pass.equals("")||mail.equals("")||repass.equals(""))
                    Toast.makeText(SignUp.this, "Por favor digite todos os campos", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser==false){
                            Boolean insert = DB.insertData(user, pass);
                            if(insert==true){
                                Toast.makeText(SignUp.this, "Registrado com sucesso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUp.this, "Registro falhou", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(SignUp.this, "Usuario já cadastrado! Favor logar", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignUp.this, "Senha invalida", Toast.LENGTH_SHORT).show();
                    }
                } }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}