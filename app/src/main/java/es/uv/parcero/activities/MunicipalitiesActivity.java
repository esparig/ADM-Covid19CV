package es.uv.parcero.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import es.uv.parcero.R;
import es.uv.parcero.adapters.MunicipalitiesAdapter;
import es.uv.parcero.models.Municipality;

public class MunicipalitiesActivity extends AppCompatActivity implements MunicipalitiesAdapter.ItemClickListener {
    MunicipalitiesAdapter adapter;
    MunicipalitiesAdapter municipalitiesAdapter;
    String defaultValue = "Orden por indicencia (desc)";
    FloatingActionButton addReport;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SearchManager searchManager;
    private SearchView searchView;
    private Spinner spinnerOrdering;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private String currentLocation = null;

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

        //Set GPS
        getGPSLocation();

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
                        Log.d("FloatingButtton onClick", "Click!!");
                        Intent i = new Intent(MunicipalitiesActivity.this, ReportActivity.class);
                        if (currentLocation != null) {
                            Log.d("MunicipalitiesActivity -> addReport.setOnClickListener - currentLocation", currentLocation);
                            i.putExtra("Location", currentLocation);
                        }
                        startActivity(i);
                    }
                }
        );

    }

    public void getGPSLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            currentLocation = null;
        } else {
            locationStart();
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GPSLocation local = new GPSLocation();
        local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) local);
        Log.d("MunicipalityActivity -> locationStart", "Localización agregada");
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    currentLocation = address.getLocality(); //address.getAddressLine(0);
                    //Log.d("MunicipalitiesActivity -> setLocation", "Mi direccion es: \n" + currentLocation);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class GPSLocation implements LocationListener {
        MunicipalitiesActivity municipalitiesActivity;

        public MunicipalitiesActivity getMainActivity() {
            return municipalitiesActivity;
        }

        public void setMainActivity(MunicipalitiesActivity municipalitiesActivity) {
            this.municipalitiesActivity = municipalitiesActivity;
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            location.getLatitude();
            location.getLongitude();
            String msg = "Mi ubicacion actual es: " + "\n Lat = "
                    + location.getLatitude() + "\n Long = " + location.getLongitude();
            //Log.d("MunicipalitiesActivity -> GPSLocation.onLocationChanged", msg);
            this.municipalitiesActivity.setLocation(location);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Log.d("MunicipalitiesActivity -> GPSLocation.onProviderDisabled","GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Log.d("MunicipalitiesActivity -> GPSLocation.onProviderEnabled","GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    @Override
    public void onMunicipalityClick(View view, int position) {
        Log.d("MunicipalityActivity -> onMunicipalityClick", "CLICK!!" + municipalitiesAdapter.getMunicipalities().get(position).toString());
        Intent intent = new Intent(MunicipalitiesActivity.this, MunicipalityDetailsActivity.class);
        intent.putExtra("Municipality", (Serializable) municipalitiesAdapter.getMunicipalities().get(position));
        startActivity(intent);
    }


}
