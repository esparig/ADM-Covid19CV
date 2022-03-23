package es.uv.parcero.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.uv.parcero.R;
import es.uv.parcero.models.Report;
import es.uv.parcero.utils.ReportsDbHelper;
import es.uv.parcero.utils.Utils;

public class ReportActivity extends AppCompatActivity {

    // UI
    EditText dateSymptoms;
    CheckBox fever;
    CheckBox cough;
    CheckBox breath_shortness;
    CheckBox fatigue;
    CheckBox body_aches;
    CheckBox headache;
    CheckBox loss_smell;
    CheckBox sore_throat;
    CheckBox congestion;
    CheckBox nausea;
    CheckBox diarrhea;
    RadioGroup close_contact;
    EditText municipalityName;
    Button bCancel;
    Button bAccept;
    ImageButton bDelete;

    // DB
    ReportsDbHelper dbHelper = new ReportsDbHelper(this);

    private Report report = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        setupUI();
        formatDate();
        setupListeners();

        try {
            Intent intent = getIntent();
            report = (Report) intent.getSerializableExtra("Report");
            if (report != null) {
                Log.d("ReportActivity -> onCreate: ", report.toString());
                bDelete.setVisibility(View.VISIBLE);
                setReportValuesUI();
            }
            String currentMunicipalityName = (String) intent.getSerializableExtra("Municipality name");
            if (currentMunicipalityName != null) {
                Log.d("ReportActivity -> onCreate - currentMunicipalityName", currentMunicipalityName);
                municipalityName.setText(currentMunicipalityName);
            }
            String location = getIntent().getExtras().getString("Location");
            if (location != null) {
                Log.d("Report Activity -> onCreate - location", location);
                municipalityName.setText(location); //TODO: convertir a nombre válido, ej: València -> Valencia
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getStringExtra_EX", e + "");
        }

    }

    private void setupUI() {
        dateSymptoms = findViewById(R.id.etDate);
        fever = findViewById(R.id.cbFever);
        cough = findViewById(R.id.cbCough);
        breath_shortness = findViewById(R.id.cbBreath);
        fatigue = findViewById(R.id.cbFatigue);
        body_aches = findViewById(R.id.cbAches);
        headache = findViewById(R.id.cbHeadache);
        loss_smell = findViewById(R.id.cbLossSmell);
        sore_throat = findViewById(R.id.cbSoreThroat);
        congestion = findViewById(R.id.cbCongestion);
        nausea = findViewById(R.id.cbNausea);
        diarrhea = findViewById(R.id.cbDiarrhea);
        close_contact = findViewById(R.id.rgContact);
        municipalityName = findViewById(R.id.etMunicipality);
        bCancel = findViewById(R.id.bCancel);
        bAccept = findViewById(R.id.bAccept);
        bDelete = findViewById(R.id.bDelete);
        bDelete.setVisibility(View.GONE);
    }

    public void formatDate() {
        dateSymptoms.setText(Utils.getStringFromDate(new Date()));        // Initialize date with current date

        dateSymptoms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String ss = s.toString();
                if (after == 0) {         // when a single character is deleted
                    if (s.charAt(start) == '/') {       // if the character is '/' , restore it and put the cursor at correct position
                        dateSymptoms.setText(s);
                        dateSymptoms.setSelection(start);
                    } else if (s.charAt(start) == '-') {  // if the character is '-' , restore it and put the cursor at correct position
                        dateSymptoms.setText(s);
                        dateSymptoms.setSelection(start);
                    } else if (ss.charAt(start) >= '0' && ss.charAt(start) <= '9') {  // if the character is a digit, replace it with '-'
                        ss = ss.substring(0, start) + "-" + ss.substring(start + 1, ss.length());
                        dateSymptoms.setText(ss);
                        dateSymptoms.setSelection(start);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = s.toString();
                if (before == 0) {    // when a single character is added
                    if (dateSymptoms.getSelectionStart() == 3 || dateSymptoms.getSelectionStart() == 6) {
                        // if the new character was just before '/' character
                        // getSelection value gets incremented by 1, because text has been changed and hence cursor position updated
                        // Log.d("test", ss);
                        ss = ss.substring(0, start) + "/" + ss.substring(start, start + 1) + ss.substring(start + 3, ss.length());
                        // Log.d("test", ss);
                        dateSymptoms.setText(ss);
                        dateSymptoms.setSelection(start + 2);
                    } else {
                        if (dateSymptoms.getSelectionStart() == 11) {
                            // if cursor was at last, do not add anything
                            ss = ss.substring(0, ss.length() - 1);
                            dateSymptoms.setText(ss);
                            dateSymptoms.setSelection(10);
                        } else {
                            // else replace the next digit with the entered digit
                            ss = ss.substring(0, start + 1) + ss.substring(start + 2, ss.length());
                            dateSymptoms.setText(ss);
                            dateSymptoms.setSelection(start + 1);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupListeners() {
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(ReportActivity.this, R.string.opCancel, Toast.LENGTH_SHORT);
                t.show();
                finish();
            }
        });

        bAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (report == null) {
                        report = createReportFromForm();
                        Log.d("ReportActivity -> bAccept.setOnClickListener -> Created report to insert", report.toString());
                        dbHelper.insertReport(report);
                        String str = dbHelper.getReports().toString();
                        Log.d("ReportActivity -> bAccept.setOnClickListener -> getReports from DB", str);
                        Toast t = Toast.makeText(ReportActivity.this, R.string.opAccept, Toast.LENGTH_SHORT);
                        t.show();
                    }
                    else {
                        updateReportFromForm();
                        Log.d("ReportActivity -> bAccept.setOnClickListener -> Report to update", report.toString());
                        dbHelper.updateReport(report);
                        Toast t = Toast.makeText(ReportActivity.this, R.string.opUpdate, Toast.LENGTH_SHORT);
                        t.show();
                    }
                    finish();
                } catch (Exception e) {
                    Log.d("ReportActivity -> bAccept.setOnClickListener -> Exception", e.toString());
                    Toast t = Toast.makeText(ReportActivity.this, e.toString(), Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteReport(report.getId());
                Toast t = Toast.makeText(ReportActivity.this, R.string.opDelete, Toast.LENGTH_SHORT);
                t.show();
                finish();
            }
        });
    }

    private void setReportValuesUI() {
        dateSymptoms.setText(Utils.getStringFromDate(report.getSymptoms_start_date()));
        fever.setChecked(report.isFever());
        cough.setChecked(report.isCough());
        breath_shortness.setChecked(report.isBreath_shortness());
        fatigue.setChecked(report.isFatigue());
        body_aches.setChecked(report.isBody_aches());
        headache.setChecked(report.isHeadache());
        loss_smell.setChecked(report.isLoss_smell());
        sore_throat.setChecked(report.isSore_throat());
        congestion.setChecked(report.isCongestion());
        nausea.setChecked(report.isNausea());
        diarrhea.setChecked(report.isDiarrhea());
        close_contact.check((report.isClose_contact()) ? R.id.rbYes : R.id.rbNo);
        municipalityName.setText(report.getMunicipality());
    }

    private Report createReportFromForm() {
        Date symptomsDate = null;
        try {
            symptomsDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateSymptoms.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Report(symptomsDate, fever.isChecked(), cough.isChecked(),
                breath_shortness.isChecked(), fatigue.isChecked(), body_aches.isChecked(),
                headache.isChecked(), loss_smell.isChecked(), sore_throat.isChecked(),
                congestion.isChecked(), nausea.isChecked(), diarrhea.isChecked(),
                close_contact.getCheckedRadioButtonId() == R.id.rbYes,
                municipalityName.getText().toString());
    }

    private void updateReportFromForm() {
        try {
            report.setSymptoms_start_date(Utils.getDateFromString(dateSymptoms.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        report.setFever(fever.isChecked());
        report.setCough(cough.isChecked());
        report.setBreath_shortness(breath_shortness.isChecked());
        report.setFatigue(fatigue.isChecked());
        report.setBody_aches(body_aches.isChecked());
        report.setHeadache(headache.isChecked());
        report.setLoss_smell(loss_smell.isChecked());
        report.setSore_throat(sore_throat.isChecked());
        report.setCongestion(congestion.isChecked());
        report.setNausea(nausea.isChecked());
        report.setDiarrhea(diarrhea.isChecked());
        report.setClose_contact(close_contact.getCheckedRadioButtonId() == R.id.rbYes);

    }
}
