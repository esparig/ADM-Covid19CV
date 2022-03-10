package es.uv.parcero.models;

import java.util.Date;
import java.util.UUID;

public class Report {
    private String id;
    private Date symptoms_start_date;
    private boolean fever;
    private boolean cough;
    private boolean breath_shortness;
    private boolean fatigue;
    private boolean body_aches;
    private boolean headache;
    private boolean loss_smell;
    private boolean sore_throat;
    private boolean congestion;
    private boolean nausea;
    private boolean diarrhea;
    private boolean close_contact;
    private String municipality;

    public Report(Date symptoms_start_date, boolean fever, boolean cough,
                  boolean breath_shortness, boolean fatigue, boolean body_aches,
                  boolean headache, boolean loss_smell, boolean sore_throat, boolean congestion,
                  boolean nausea, boolean diarrhea, boolean close_contact, String municipality) {
        this.id = UUID.randomUUID().toString();
        this.symptoms_start_date = symptoms_start_date;
        this.fever = fever;
        this.cough = cough;
        this.breath_shortness = breath_shortness;
        this.fatigue = fatigue;
        this.body_aches = body_aches;
        this.headache = headache;
        this.loss_smell = loss_smell;
        this.sore_throat = sore_throat;
        this.congestion = congestion;
        this.nausea = nausea;
        this.diarrhea = diarrhea;
        this.close_contact = close_contact;
        this.municipality = municipality;
    }

    public Report(String id, Date symptoms_start_date, boolean fever, boolean cough,
                  boolean breath_shortness, boolean fatigue, boolean body_aches,
                  boolean headache, boolean loss_smell, boolean sore_throat, boolean congestion,
                  boolean nausea, boolean diarrhea, boolean close_contact, String municipality) {
        this.id = id;
        this.symptoms_start_date = symptoms_start_date;
        this.fever = fever;
        this.cough = cough;
        this.breath_shortness = breath_shortness;
        this.fatigue = fatigue;
        this.body_aches = body_aches;
        this.headache = headache;
        this.loss_smell = loss_smell;
        this.sore_throat = sore_throat;
        this.congestion = congestion;
        this.nausea = nausea;
        this.diarrhea = diarrhea;
        this.close_contact = close_contact;
        this.municipality = municipality;
    }

    public String getId() {
        return id;
    }

    public Date getSymptoms_start_date() {
        return symptoms_start_date;
    }

    public boolean isFever() {
        return fever;
    }

    public boolean isCough() {
        return cough;
    }

    public boolean isBreath_shortness() {
        return breath_shortness;
    }

    public boolean isFatigue() {
        return fatigue;
    }

    public boolean isBody_aches() {
        return body_aches;
    }

    public boolean isHeadache() {
        return headache;
    }

    public boolean isLoss_smell() {
        return loss_smell;
    }

    public boolean isSore_throat() {
        return sore_throat;
    }

    public boolean isCongestion() {
        return congestion;
    }

    public boolean isNausea() {
        return nausea;
    }

    public boolean isDiarrhea() {
        return diarrhea;
    }

    public boolean isClose_contact() {
        return close_contact;
    }

    public String getMunicipality() {
        return municipality;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", symptoms_start_date=" + symptoms_start_date +
                ", fever=" + fever +
                ", cough=" + cough +
                ", breath_shortness=" + breath_shortness +
                ", fatigue=" + fatigue +
                ", body_aches=" + body_aches +
                ", headache=" + headache +
                ", loss_smell=" + loss_smell +
                ", sore_throat=" + sore_throat +
                ", congestion=" + congestion +
                ", nausea=" + nausea +
                ", diarrhea=" + diarrhea +
                ", close_contact=" + close_contact +
                ", municipality='" + municipality + '\'' +
                '}';
    }
}
