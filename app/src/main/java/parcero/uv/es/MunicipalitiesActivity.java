package parcero.uv.es;

import android.content.Intent;
import android.net.Uri;
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

import java.io.Serializable;

public class MunicipalitiesActivity extends AppCompatActivity implements MunicipalitiesAdapter.ItemClickListener {
    MunicipalitiesAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    FloatingActionButton addReport;

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

        //Set up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.rvMunicipalities);
        adapter = new MunicipalitiesAdapter(this);
        adapter.setClickListener(MunicipalitiesActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
}