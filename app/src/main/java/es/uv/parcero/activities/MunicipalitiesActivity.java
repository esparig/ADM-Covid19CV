package es.uv.parcero.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import es.uv.parcero.R;
import es.uv.parcero.adapters.MunicipalitiesAdapter;
import es.uv.parcero.models.Municipality;
import es.uv.parcero.utils.Utils;

public class MunicipalitiesActivity extends AppCompatActivity implements MunicipalitiesAdapter.ItemClickListener {
    MunicipalitiesAdapter adapter;
    FloatingActionButton addReport;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.gva_web:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://coronavirus.san.gva.es/"));
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_municipalities);

        // Get Json from HTTP GET request
        GetJSONAsynTask getJSONAsynTask = new GetJSONAsynTask();
        getJSONAsynTask.execute();

        //Set up Floating Button
        addReport = findViewById(R.id.fab);
        addReport.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MunicipalitiesActivity.this, "Click!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MunicipalitiesActivity.this, ReportActivity.class);
                        startActivity(i);
                    }
                }
        );

    }

    @Override
    public void onMunicipalityClick(View view, int position) {
        Log.d("MunicipalityActivity -> onMunicipalityClick", "CLICK!!" + adapter.getMunicipalities().get(position).toString());
        Intent intent = new Intent(MunicipalitiesActivity.this, MunicipalityDetailsActivity.class);
        intent.putExtra("Municipality", (Serializable) adapter.getMunicipalities().get(position));
        startActivity(intent);
    }

    class GetJSONAsynTask extends AsyncTask<String, Void, ArrayList<Municipality>> {
        @Override
        protected ArrayList<Municipality> doInBackground(String... params) {
            ArrayList<Municipality> municipios = new ArrayList<Municipality>();
            //Perform the request and get the answer
            String jsonString = Utils.getJsonFromHttp();
            try {
                assert jsonString != null;
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray records = jsonObject.getJSONObject("result").getJSONArray("records");
                for (int i = 0; i < records.length(); i++) {
                    municipios.add(new Municipality(records.getJSONObject(i).getInt("_id"),
                            records.getJSONObject(i).getInt("CodMunicipio"),
                            records.getJSONObject(i).getString("Municipi"),
                            records.getJSONObject(i).getInt("Casos PCR+"),
                            records.getJSONObject(i).getString("Incidència acumulada PCR+"),
                            records.getJSONObject(i).getInt("Casos PCR+ 14 dies"),
                            records.getJSONObject(i).getString("Incidència acumulada PCR+14"),
                            records.getJSONObject(i).getInt("Defuncions"),
                            records.getJSONObject(i).getString("Taxa de defunció")));
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println(municipios.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return municipios;
        }

        @Override
        protected void onPostExecute(ArrayList municipios) {
            //Set up the RecyclerView
            recyclerView = (RecyclerView) findViewById(R.id.rvMunicipalities);
            adapter = new MunicipalitiesAdapter(MunicipalitiesActivity.this, municipios);
            adapter.setClickListener(MunicipalitiesActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(MunicipalitiesActivity.this);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}