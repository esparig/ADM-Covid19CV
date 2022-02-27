package parcero.uv.es;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MunicipalitiesAdapter extends RecyclerView.Adapter<MunicipalitiesAdapter.ViewHolder> {
    private ArrayList<Municipality> municipalities; //data to visualize
    Context context;

    public MunicipalitiesAdapter(Context c) {
        context = c;
        Init();
    }

    public void Init() {
        municipalities = new ArrayList<>();
        // We read the JSON file and fill the “municipios” ArrayList
        String jsonString = Utils.getJsonFromAssets(context.getApplicationContext(), "covid19cv.json");
        try {
            assert jsonString != null;
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray records = jsonObject.getJSONObject("result").getJSONArray("records");
            for (int i = 0; i < records.length(); i++) {
                municipalities.add(new Municipality(records.getJSONObject(i).getInt("_id"),
                        records.getJSONObject(i).getInt("CodMunicipio"),
                        records.getJSONObject(i).getString("Municipi"),
                        records.getJSONObject(i).getInt("Casos PCR+"),
                        records.getJSONObject(i).getString("Incidència acumulada PCR+"),
                        records.getJSONObject(i).getInt("Casos PCR+ 14 dies"),
                        records.getJSONObject(i).getString("Incidència acumulada PCR+14"),
                        records.getJSONObject(i).getInt("Defuncions"),
                        records.getJSONObject(i).getString("Taxa de defunció")));
            }
            Collections.sort(municipalities);
            for (int i=0; i < 10; i++) {
                System.out.println(municipalities.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView municipality;
        private final TextView cases;
        private final TextView deaths;
        private final TextView cases_14;

        public ViewHolder(View view) {
            super(view);
            municipality = view.findViewById(R.id.mun_name);
            cases = view.findViewById(R.id.cases);
            deaths = view.findViewById(R.id.deaths);
            cases_14 = view.findViewById(R.id.cases_14);
        }

        public TextView getMunicipality() {
            return municipality;
        }

        public TextView getCases() {
            return cases;
        }

        public TextView getDeaths() {
            return deaths;
        }

        public TextView getCases_14() {
            return cases_14;
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_municipality, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override // Customize the ViewHolder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
         holder.getMunicipality().setText(String.valueOf(municipalities.get(position).getNameMunicipality()));
         holder.getCases().setText(String.valueOf(municipalities.get(position).getNumPCR()));
         holder.getDeaths().setText(String.valueOf(municipalities.get(position).getDeaths()));
         holder.getCases_14().setText(String.valueOf(municipalities.get(position).getNumPCR14()));
    }
    @Override
    public int getItemCount() {
        return municipalities.size();
    }
}
