package com.example.logonrm.assistenciaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONStringer;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText cod;
    private Spinner spinner;
    private String spinnerSelect;
    private boolean isChecked;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_chamado) {
            Intent intent = new Intent(this, ChamadoActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cod = (EditText) findViewById(R.id.cod);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerSelect = spinner.getSelectedItem().toString();
        isChecked = ((CheckBox) findViewById(R.id.checkbox)).isChecked();

    }

    public void cadastrar(View view){
        Cadastro task = new Cadastro();

        task.execute(cod.getText().toString(), spinnerSelect, isChecked);

    }

    private class Cadastro extends AsyncTask<Object, Object, Integer> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, "Aguarde...", "Enviando dados para o servidor");
        }

        @Override
        protected Integer doInBackground(Object... params) {

            try {
                URL url = new URL("https://assistenciaapi.herokuapp.com/rest/chamado");
                //Obter uma conexão
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Configurar a requisição
                connection.setRequestMethod("POST");
                //Tipo de dado que será devolvido pelo webservice (JSON)
                connection.setRequestProperty("Content-Type","application/json");

                JSONStringer json = new JSONStringer();
                json.object();
                json.key("codigoFuncionario").value(params[0]);
                json.key("descricao").value(params[1]);
                json.key("finalizado").value(params[2]);
                json.endObject();

                OutputStreamWriter stream = new OutputStreamWriter(connection.getOutputStream());
                stream.write(json.toString());
                stream.close();
                int x = connection.getResponseCode();
                return connection.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer s) {
            progress.dismiss();
            if(s == 201){
                Toast.makeText(MainActivity.this, "Cadastro Realizado", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(MainActivity.this, "Erro ao realizar cadastro", Toast.LENGTH_LONG).show();

            }
        }
    }
}
