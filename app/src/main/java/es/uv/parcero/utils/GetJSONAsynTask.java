package es.uv.parcero.utils;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.uv.parcero.activities.MunicipalitiesActivity;
import es.uv.parcero.models.Municipality;


public class GetJSONAsynTask extends AsyncTask<Void, Void, Void> {

    private final MunicipalitiesActivity municipalitiesActivity;

    public GetJSONAsynTask(MunicipalitiesActivity municipalitiesActivity) {
        this.municipalitiesActivity = municipalitiesActivity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
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
            municipalitiesActivity.setMunicipios(municipios);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        municipalitiesActivity.setUpRecyclerView();
    }

}
