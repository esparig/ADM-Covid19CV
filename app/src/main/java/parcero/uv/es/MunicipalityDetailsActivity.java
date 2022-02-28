package parcero.uv.es;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MunicipalityDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView mun_name;
        TextView mun_code;
        TextView cases;
        TextView incidence;
        TextView cases_14;
        TextView incidence_14;
        TextView deaths;
        TextView death_rate;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality_details);

        try {
            Intent intent = getIntent();
            Municipality municipality = (Municipality) intent.getSerializableExtra("Municipality");
            System.out.println(municipality.toString());

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


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getStringExtra_EX", e + "");
        }

    }
}