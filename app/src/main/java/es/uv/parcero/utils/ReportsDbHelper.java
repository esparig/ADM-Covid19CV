package es.uv.parcero.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import es.uv.parcero.models.ReportsContract;
import es.uv.parcero.models.Report;

public class ReportsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Reports.db";
    Context context;

    public ReportsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("ReportsDbHelper -> OnCreate", "Called!");
        db.execSQL("CREATE TABLE " + ReportsContract.ReportEntry.TABLE_NAME + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id TEXT NOT NULL, " +
                "symptoms_start_date TEXT, " +
                "fever INTEGER, " +
                "cough INTEGER, " +
                "breath_shortness INTEGER, " +
                "fatigue INTEGER, " +
                "body_aches INTEGER, " +
                "headache INTEGER, " +
                "loss_smell INTEGER, " +
                "sore_throat INTEGER, " +
                "congestion INTEGER, " +
                "nausea INTEGER, " +
                "diarrhea INTEGER, " +
                "close_contact INTEGER, " +
                "municipality TEXT, " +
                "UNIQUE (id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + ReportsContract.ReportEntry.TABLE_NAME);
        } catch (SQLiteException e) {
            Log.e("ReportsDbHelper.onUpgrade.SQLiteException: ", e.toString());
        }
        onCreate(db);
    }

    public void insertReport(Report report) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO reports VALUES ( null, " +
                "'" + report.getId() + "', " +
                "'" + report.getSymptoms_start_date().toString() + "', " +
                report.isFever() + ", " +
                report.isCough() + ", " +
                report.isBreath_shortness() + ", " +
                report.isFatigue() + ", " +
                report.isBody_aches() + ", " +
                report.isHeadache() + ", " +
                report.isLoss_smell() + ", " +
                report.isSore_throat() + ", " +
                report.isCongestion() + ", " +
                report.isNausea() + ", " +
                report.isDiarrhea() + ", " +
                report.isClose_contact() + ", " +
                "'" + report.getMunicipality() + "')";
        Log.d("ReportsDbHelper.insertReport: ", query);
        db.execSQL(query);
    }

    public void updateReport(Report report) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE reports SET " +
                "symptoms_start_date = '" + report.getSymptoms_start_date() + "', " +
                "fever = " + report.isFever() + ", " +
                "cough = " + report.isCough() + ", " +
                "breath_shortness = " + report.isBreath_shortness() + ", " +
                "fatigue = " + report.isFatigue() + ", " +
                "body_aches = " + report.isBody_aches() + ", " +
                "headache = " + report.isHeadache() + ", " +
                "loss_smell = " + report.isLoss_smell() + ", " +
                "sore_throat = " + report.isSore_throat() + ", " +
                "congestion = " + report.isCongestion() + ", " +
                "nausea = " + report.isNausea() + ", " +
                "diarrhea = " + report.isDiarrhea() + ", " +
                "close_contact = " + report.isClose_contact() + ", " +
                "municipality = '" + report.getMunicipality() + "' " +
                "WHERE id = '" + report.getId() + "'");
    }

    public Report getReport(int id) {
        Report report = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, symptoms_start_date, fever, cough, " +
                "breath_shortness, fatigue, body_aches, headache, " +
                "loss_smell, sore_throat, congestion, nausea, diarrhea, close_contact, " +
                "municipality FROM reports WHERE id=" + id, null);
        try {
            if (cursor.moveToNext())
                report = new Report(cursor.getString(0),
                        new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(1)),
                        cursor.getInt(2) == 1, cursor.getInt(3) == 1,
                        cursor.getInt(4) == 1, cursor.getInt(5) == 1,
                        cursor.getInt(6) == 1, cursor.getInt(7) == 1,
                        cursor.getInt(8) == 1, cursor.getInt(9) == 1,
                        cursor.getInt(10) == 1, cursor.getInt(11) == 1,
                        cursor.getInt(12) == 1, cursor.getInt(13) == 1,
                        cursor.getString(14));
            else
                throw new SQLException("Error al acceder al elemento: " + id);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return report;
    }

    public ArrayList<Report> getReports() {
        ArrayList<Report> reports = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, symptoms_start_date, fever, cough, " +
                "breath_shortness, fatigue, body_aches, headache, " +
                "loss_smell, sore_throat, congestion, nausea, diarrhea, close_contact, " +
                "municipality FROM reports", null);
        try {
            while (cursor.moveToNext()) {
                reports.add(new Report(cursor.getString(0),
                        new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(cursor.getString(1)),
                        cursor.getInt(2) == 1, cursor.getInt(3) == 1,
                        cursor.getInt(4) == 1, cursor.getInt(5) == 1,
                        cursor.getInt(6) == 1, cursor.getInt(7) == 1,
                        cursor.getInt(8) == 1, cursor.getInt(9) == 1,
                        cursor.getInt(10) == 1, cursor.getInt(11) == 1,
                        cursor.getInt(12) == 1, cursor.getInt(13) == 1,
                        cursor.getString(14)));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return reports;
    }

    public Cursor findReportsByMunicipality(String municipality) {
        ArrayList<Report> reports = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, id, symptoms_start_date, fever, cough, " +
                "breath_shortness, fatigue, body_aches, headache, " +
                "loss_smell, sore_throat, congestion, nausea, diarrhea, close_contact, " +
                "municipality FROM reports WHERE municipality = '" + municipality +"'", null);
        return cursor;
    }

    public boolean deleteReport(String id){
        SQLiteDatabase db = getWritableDatabase();
        boolean success = true;
        try {
            db.execSQL("DELETE FROM reports WHERE id = '" + id + "'");
        } catch (Exception e) {
            Log.e("deleteReport -> Exception", e.toString());
            success = false;
        } finally {
            return success;
        }
    }
}
