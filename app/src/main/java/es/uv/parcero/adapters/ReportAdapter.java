package es.uv.parcero.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


import es.uv.parcero.R;
import es.uv.parcero.models.Report;
import es.uv.parcero.models.ReportsContract;

public class ReportAdapter extends CursorAdapter {

    public ReportAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.row_report, viewGroup, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        Report report = null;
        try {
            report = getReportFromCursor(cursor);

            startDate.setText( new SimpleDateFormat("dd/MM/yyyy").format(report.getSymptoms_start_date()));
            im1.setImageResource(R.drawable.ic_fever);

            if (!report.isFever())
                im1.setAlpha((float) 0.3);

            im2.setImageResource(R.drawable.ic_cough);

            if (!report.isCough())
                im2.setAlpha((float) 0.3);

            im3.setImageResource(R.drawable.ic_breath_shortness);

            if (!report.isBreath_shortness())
                im3.setAlpha((float) 0.3);

            im4.setImageResource(R.drawable.ic_fatigue);

            if (!report.isFatigue())
                im4.setAlpha((float) 0.3);

            im5.setImageResource(R.drawable.ic_body_aches);

            if (!report.isBody_aches())
                im5.setAlpha((float) 0.3);

            im6.setImageResource(R.drawable.ic_headache);

            if (!report.isHeadache())
                im6.setAlpha((float) 0.3);

            im7.setImageResource(R.drawable.ic_loss_smell);

            if (!report.isLoss_smell())
                im7.setAlpha((float) 0.3);

            im8.setImageResource(R.drawable.ic_sore_throat);

            if (!report.isSore_throat())
                im8.setAlpha((float) 0.3);

            im9.setImageResource(R.drawable.ic_congestion);

            if (!report.isCongestion())
                im9.setAlpha((float) 0.3);

            im10.setImageResource(R.drawable.ic_nausea);

            if (!report.isNausea())
                im10.setAlpha((float) 0.3);

            im11.setImageResource(R.drawable.ic_diarrhea);

            if (!report.isDiarrhea())
                im11.setAlpha((float) 0.3);

            im12.setImageResource(R.drawable.ic_close_contact);

            if (!report.isClose_contact())
                im12.setAlpha((float) 0.3);

            Log.d("bindView ", report.getId());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Report getReportFromCursor(Cursor cursor) throws ParseException {
        String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
        String date_string = cursor.getString(cursor.getColumnIndexOrThrow("symptoms_start_date"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E MMM d H:m:s z u", Locale.ENGLISH);
        ZonedDateTime date = ZonedDateTime.parse(date_string, dtf);
        //Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(date_string);
        boolean fever = cursor.getInt(cursor.getColumnIndexOrThrow("fever")) == 1;
        boolean cough = cursor.getInt(cursor.getColumnIndexOrThrow("cough")) == 1;
        boolean breath_shortness = cursor.getInt(
                cursor.getColumnIndexOrThrow("breath_shortness")) == 1;
        boolean fatigue = cursor.getInt(cursor.getColumnIndexOrThrow("fatigue")) == 1;
        boolean body_aches = cursor.getInt(cursor.getColumnIndexOrThrow("body_aches")) == 1;
        boolean headache = cursor.getInt(cursor.getColumnIndexOrThrow("headache")) == 1;
        boolean loss_smell = cursor.getInt(cursor.getColumnIndexOrThrow("loss_smell")) == 1;
        boolean sore_throat = cursor.getInt(cursor.getColumnIndexOrThrow("sore_throat")) == 1;
        boolean congestion = cursor.getInt(cursor.getColumnIndexOrThrow("congestion")) == 1;
        boolean nausea = cursor.getInt(cursor.getColumnIndexOrThrow("nausea")) == 1;
        boolean diarrhea = cursor.getInt(cursor.getColumnIndexOrThrow("diarrhea")) == 1;
        boolean close_contact = cursor.getInt(cursor.getColumnIndexOrThrow("close_contact")) == 1;
        String municipality = cursor.getString(
                cursor.getColumnIndexOrThrow(ReportsContract.ReportEntry.MUNICIPALITY));

        return new Report(id, Date.from(date.toInstant()), fever, cough, breath_shortness, fatigue, body_aches, headache,
                loss_smell, sore_throat, congestion, nausea, diarrhea, close_contact, municipality);
    }
}
