package es.uv.parcero.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import es.uv.parcero.activities.MunicipalitiesActivity;
import es.uv.parcero.models.Municipality;
import es.uv.parcero.R;
import es.uv.parcero.utils.Utils;

public class MunicipalitiesAdapter extends RecyclerView.Adapter<MunicipalitiesAdapter.ViewHolder> implements Filterable {
    Context context;
    private static ItemClickListener itemClickListener;
    private ArrayList<Municipality> municipalities; //data to visualize
    private ArrayList<Municipality> municipalities_filtered;

    public MunicipalitiesAdapter(MunicipalitiesActivity c, ArrayList<Municipality> municipalities) {
        context = c;
        this.municipalities = municipalities;
        //initMunicipalitiesFromJSON();
    }

    public ArrayList<Municipality> getMunicipalities() {
        return municipalities;
    }
    
    @Deprecated
    public void initMunicipalitiesFromJSON() {
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
            municipalities_filtered = municipalities;
        } catch (JSONException e) {
            e.printStackTrace();
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
        float incidence = Float.parseFloat(municipalities_filtered.get(position).getCumulativePCR14().replace(',', '.'));
        holder.getCard().setBackgroundColor(getColorFromIncidence(incidence));
        holder.getMunicipality().setText(String.valueOf(municipalities_filtered.get(position).getNameMunicipality()));
        holder.getCases().setText(String.valueOf(municipalities_filtered.get(position).getNumPCR()));
        holder.getDeaths().setText(String.valueOf(municipalities_filtered.get(position).getDeaths()));
        holder.getCases_14().setText(String.valueOf(municipalities_filtered.get(position).getNumPCR14()));

    }

    private int getColorFromIncidence(float incidence) {
        int bkg;
        if (incidence < 300.0) {
            bkg = context.getResources().getColor(R.color.riesgo_bajo);
        } else {
            if (incidence < 500.0) {
                bkg = context.getResources().getColor(R.color.riesgo_medio);
            } else {
                bkg = context.getResources().getColor(R.color.riesgo_alto);
            }
        }
        return bkg;
    }

    @Override
    public int getItemCount() {
        return municipalities_filtered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    municipalities_filtered = municipalities;
                } else {
                    Log.v("performFiltering: ", charSequence.toString());
                    ArrayList<Municipality> filteredList = new ArrayList<>();
                    for (Municipality row : municipalities) {
                        if (row.getNameMunicipality().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    municipalities_filtered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = municipalities_filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                municipalities_filtered = (ArrayList<Municipality>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onMunicipalityClick(View view, int position);
        //void onMunicipalityClick(View view);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CardView card;
        private final TextView municipality;
        private final TextView cases;
        private final TextView deaths;
        private final TextView cases_14;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            card = view.findViewById(R.id.card);
            municipality = view.findViewById(R.id.mun_name);
            cases = view.findViewById(R.id.cases);
            deaths = view.findViewById(R.id.deaths);
            cases_14 = view.findViewById(R.id.cases_14);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onMunicipalityClick(view, getAdapterPosition());
        }

        public CardView getCard() {
            return card;
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
}
