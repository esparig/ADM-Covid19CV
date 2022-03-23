package es.uv.parcero.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.ParseException;

import es.uv.parcero.models.Municipality;
import es.uv.parcero.R;
import es.uv.parcero.adapters.ReportAdapter;
import es.uv.parcero.models.Report;
import es.uv.parcero.utils.ReportsDbHelper;

public class MunicipalityDetailsActivity extends AppCompatActivity {

    private Municipality municipality;
    private ReportAdapter reportsAdapter;
    private ListView lvReports;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.municipality_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.open_map:
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + municipality.getNameMunicipality().replace(' ', '+'));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality_details);

        try {
            Intent intent = getIntent();
            municipality = (Municipality) intent.getSerializableExtra("Municipality");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getStringExtra_EX", e + "");
        }
        setMunicipalityDetails();
        setupListViewReports();

    }
    @Override
    protected void onResume() {
        Log.d("MunicipalityDetailsActivity -> OnResume", "ENTRA");
        setupListViewReports();
        super.onResume();
    }
    private void setMunicipalityDetails() {
        TextView cases_14;
        TextView mun_name;
        TextView death_rate;
        TextView cases;
        TextView incidence_14;
        TextView deaths;
        TextView mun_code;
        TextView incidence;
        mun_name = findViewById(R.id.mun_name);
        mun_name.setText(municipality.getNameMunicipality());

        mun_code = findViewById((R.id.mun_code));
        mun_code.setText(String.valueOf(municipality.getCodMunicipality()));

        cases = findViewById(R.id.cases);
        cases.setText(String.valueOf(municipality.getNumPCR()));

        incidence = findViewById(R.id.incidence);
        incidence.setText(municipality.getCumulativePCR());

        cases_14 = findViewById(R.id.cases_14);
        cases_14.setText(String.valueOf(municipality.getNumPCR14()));

        incidence_14 = findViewById(R.id.incidence_14);
        incidence_14.setText(municipality.getCumulativePCR14());

        deaths = findViewById(R.id.deaths);
        deaths.setText(String.valueOf(municipality.getDeaths()));

        death_rate = findViewById(R.id.death_rate);
        death_rate.setText(municipality.getDeathRate());
    }

    private void setupListViewReports() {
        ReportsDbHelper db = new ReportsDbHelper(getApplicationContext());
        Cursor reportsByMunicipality = db.findReportsByMunicipality(municipality.getNameMunicipality());
        reportsAdapter = new ReportAdapter(getApplicationContext(), reportsByMunicipality, 0);
        lvReports = findViewById(R.id.lvReports);
        lvReports.setAdapter(reportsAdapter);
        lvReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Report report = null;
                SQLiteCursor cr = (SQLiteCursor) parent.getItemAtPosition(position);
                try {
                    report = reportsAdapter.getReportFromCursor(cr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("MunicipalityDetailsActivity -> setupListViewReports.onItemClick: ",  report.toString());
                Intent i = new Intent(MunicipalityDetailsActivity.this, ReportActivity.class);
                i.putExtra("Report", (Serializable) report);
                startActivity(i);
            }
        });

    }
}