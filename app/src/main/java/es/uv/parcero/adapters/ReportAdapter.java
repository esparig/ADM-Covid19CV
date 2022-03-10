package es.uv.parcero.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.uv.parcero.R;

public class ReportAdapter extends CursorAdapter {

    public ReportAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.row_report, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // UI References
        TextView startDate = view.findViewById(R.id.rowStartDate);
        ImageView im1 = view.findViewById(R.id.imageView);
        ImageView im2 = view.findViewById(R.id.imageView2);
        ImageView im3 = view.findViewById(R.id.imageView3);
        ImageView im4 = view.findViewById(R.id.imageView4);
        ImageView im5 = view.findViewById(R.id.imageView5);
        ImageView im6 = view.findViewById(R.id.imageView6);
        ImageView im7 = view.findViewById(R.id.imageView7);
        ImageView im8 = view.findViewById(R.id.imageView8);
        ImageView im9 = view.findViewById(R.id.imageView9);
        ImageView im10 = view.findViewById(R.id.imageView10);
        ImageView im11 = view.findViewById(R.id.imageView11);
        ImageView im12 = view.findViewById(R.id.imageView12);

        // Get values from Cursor
        String date = cursor.getString(cursor.getColumnIndexOrThrow("symptoms_start_date"));
        startDate.setText(date);

        im1.setImageResource(R.drawable.ic_fever);
        boolean fever = cursor.getInt(cursor.getColumnIndexOrThrow("fever")) == 1;
        if( !fever)
            im1.setAlpha((float) 0.3);

        im2.setImageResource(R.drawable.ic_cough);
        boolean cough = cursor.getInt(cursor.getColumnIndexOrThrow("cough")) == 1;
        if( !cough)
            im2.setAlpha((float) 0.3);

        im3.setImageResource(R.drawable.ic_breath_shortness);
        boolean breath_shortness = cursor.getInt(cursor.getColumnIndexOrThrow("breath_shortness")) == 1;
        if( !breath_shortness)
            im3.setAlpha((float) 0.3);

        im4.setImageResource(R.drawable.ic_fatigue);
        boolean fatigue = cursor.getInt(cursor.getColumnIndexOrThrow("fatigue")) == 1;
        if( !fatigue)
            im4.setAlpha((float) 0.3);

        im5.setImageResource(R.drawable.ic_body_aches);
        boolean body_aches = cursor.getInt(cursor.getColumnIndexOrThrow("body_aches")) == 1;
        if( !body_aches)
            im5.setAlpha((float) 0.3);

        im6.setImageResource(R.drawable.ic_headache);
        boolean headache = cursor.getInt(cursor.getColumnIndexOrThrow("headache")) == 1;
        if( !headache)
            im6.setAlpha((float) 0.3);

        im7.setImageResource(R.drawable.ic_loss_smell);
        boolean loss_smell = cursor.getInt(cursor.getColumnIndexOrThrow("loss_smell")) == 1;
        if( !loss_smell)
            im7.setAlpha((float) 0.3);

        im8.setImageResource(R.drawable.ic_sore_throat);
        boolean sore_throat = cursor.getInt(cursor.getColumnIndexOrThrow("sore_throat")) == 1;
        if( !sore_throat)
            im8.setAlpha((float) 0.3);

        im9.setImageResource(R.drawable.ic_congestion);
        boolean congestion = cursor.getInt(cursor.getColumnIndexOrThrow("congestion")) == 1;
        if( !congestion)
            im9.setAlpha((float) 0.3);

        im10.setImageResource(R.drawable.ic_nausea);
        boolean nausea = cursor.getInt(cursor.getColumnIndexOrThrow("nausea")) == 1;
        if( !nausea)
            im10.setAlpha((float) 0.3);

        im11.setImageResource(R.drawable.ic_diarrhea);
        boolean diarrhea = cursor.getInt(cursor.getColumnIndexOrThrow("diarrhea")) == 1;
        if( !diarrhea)
            im11.setAlpha((float) 0.3);

        im12.setImageResource(R.drawable.ic_close_contact);
        boolean close_contact = cursor.getInt(cursor.getColumnIndexOrThrow("close_contact")) == 1;
        if( !close_contact)
            im12.setAlpha((float) 0.3);

        Log.d("bindView ", date + ", Fever: " + fever + ", Cough: " + cough);



    }
}
