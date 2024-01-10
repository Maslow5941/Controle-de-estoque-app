package br.com.afinal.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import br.com.afinal.R;

public class EmpresaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);
        configCliques();
    }

    private void configCliques() {
        View voltarButton = findViewById(R.id.imageButton_voltar_voltar);
        if (voltarButton != null) {
            voltarButton.setOnClickListener(view -> finish());
        }
    }

    public void ligar(View view) {
        String numeroTelefoneFixo = "+55031982493117";

        if (!numeroTelefoneFixo.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + numeroTelefoneFixo));

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Nenhuma atividade disponível para lidar com a chamada.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Número de telefone não disponível.", Toast.LENGTH_SHORT).show();
        }
    }
}
