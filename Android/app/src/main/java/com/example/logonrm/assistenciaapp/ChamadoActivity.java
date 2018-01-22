package com.example.logonrm.assistenciaapp;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.logonrm.assistenciaapp.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ChamadoActivity extends AppCompatActivity {
    EditText txtCodigoFuncionario;
    ListView lstChamados;
    ProgressDialog progressDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cadastro) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamado);

        setupUI();
    }

    private void setupUI() {
        txtCodigoFuncionario = (EditText) findViewById(R.id.cod);
        lstChamados = (ListView) findViewById(R.id.listView);
    }

    public void pesquisar(View v) {
        int codigoFuncionario = Integer.parseInt(txtCodigoFuncionario.getText().toString());

        PesquisaTask pesquisarTask = new PesquisaTask();
        pesquisarTask.execute(codigoFuncionario);
    }

    private class PesquisaTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ChamadoActivity.this, "Aguarde", "Pesquisando Chamados");
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                URL url = new URL("https://assistenciaapi.herokuapp.com/rest/chamado/funcionario/" + params[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");

                StringBuilder resposta = new StringBuilder();

                if (urlConnection.getResponseCode() == 200) {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    String linha;

                    while ((linha = buffer.readLine()) != null) {
                        resposta.append(linha);
                    }
                }

                urlConnection.disconnect();

                return resposta.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            if (s != null) {
                try {
                    JSONArray arrayJson = new JSONArray(s);

                    List<Item> listaChamados = new ArrayList<Item>();

                    for (int i = 0; i < arrayJson.length(); i++) {
                        JSONObject item = (JSONObject) arrayJson.get(i);

                        int codigo = item.getInt("codigo");
                        int codigoFuncionario = item.getInt("codigoFuncionario");
                        boolean finalizado = item.getBoolean("finalizado");
                        String descricao = item.getString("descricao");

                        listaChamados.add(new Item(codigo, codigoFuncionario, finalizado, descricao));
                    }

                    ListAdapter adapter = new ArrayAdapter<Item>(ChamadoActivity.this, android.R.layout.simple_list_item_1, listaChamados);

                    lstChamados.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ChamadoActivity.this, "Funcionario nao encontrado", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(s);
        }
    }
}
