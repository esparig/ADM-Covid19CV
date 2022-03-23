package es.uv.parcero.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.Collections;


import es.uv.parcero.adapters.MunicipalitiesAdapter;
import es.uv.parcero.R;
import es.uv.parcero.models.Municipality;

public class MunicipalitiesActivity extends AppCompatActivity implements MunicipalitiesAdapter.ItemClickListener {
    MunicipalitiesAdapter adapter;
    MunicipalitiesAdapter municipalitiesAdapter;
    String defaultValue = "Orden por indicencia (desc)";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SearchManager searchManager;
    private SearchView searchView;
    private Spinner spinnerOrdering;
    private ArrayAdapter<CharSequence> spinnerAdapter;
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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        municipalitiesAdapter = new MunicipalitiesAdapter(this);
        municipalitiesAdapter.setClickListener(MunicipalitiesActivity.this);
        recyclerView.setAdapter(municipalitiesAdapter);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Set up the Search bar
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.filter_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                municipalitiesAdapter.getFilter().filter(query);
                Log.v("onQueryTextSubmit: ", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                municipalitiesAdapter.getFilter().filter(query);
                return false;
            }
        });

        //Set up the Spinner
        spinnerOrdering = findViewById(R.id.spinner_ordering);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.ordering, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrdering.setAdapter(spinnerAdapter);
        spinnerOrdering.setSelection(spinnerAdapter.getPosition(defaultValue));
        spinnerOrdering.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item " + position, (String) parent.getItemAtPosition(position));
                switch (position) {
                    case 0:
                        Collections.sort(municipalitiesAdapter.getMunicipalities(), Municipality.Comparators.NAME_ASC);
                        break;
                    case 1:
                        Collections.sort(municipalitiesAdapter.getMunicipalities(), Municipality.Comparators.NAME_DESC);
                        break;
                    case 2:
                        Collections.sort(municipalitiesAdapter.getMunicipalities(), Municipality.Comparators.INC_ASC);
                        break;
                    case 3:
                        Collections.sort(municipalitiesAdapter.getMunicipalities(), Municipality.Comparators.INC_DESC);
                        break;
                    default:
                        Collections.sort(municipalitiesAdapter.getMunicipalities());
                }
                municipalitiesAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //not used
            }
        });

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
        Log.d("MunicipalityActivity -> onMunicipalityClick", "CLICK!!" + municipalitiesAdapter.getMunicipalities().get(position).toString());
        Intent intent = new Intent(MunicipalitiesActivity.this, MunicipalityDetailsActivity.class);
        intent.putExtra("Municipality", (Serializable) municipalitiesAdapter.getMunicipalities().get(position));
        startActivity(intent);
    }
}
