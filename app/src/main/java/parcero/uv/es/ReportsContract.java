package parcero.uv.es;

import android.provider.BaseColumns;

public class ReportsContract {
    public static abstract class ReportEntry implements BaseColumns {
        public static final String TABLE_NAME = "reports";
        public static final String _ID = "_id";
        public static final String ID = "id";
        public static final String SYMPTOMS_START_DATE = "symptoms_start_date";
        public static final String FEVER = "fever";
        public static final String COUGH = "cough";
        public static final String BREATH_SHORTNESS = "breath_shortness";
        public static final String FATIGUE = "fatigue";
        public static final String BODY_ACHES = "body_aches";
        public static final String HEADACHE = "headache";
        public static final String LOSS_SMELL = "loss_smell";
        public static final String SORE_THROAT = "sore_throat";
        public static final String CONGESTION = "congestion";
        public static final String NAUSEA = "nausea";
        public static final String DIARRHEA = "diarrhea";
        public static final String CLOSE_CONTACT = "close_contact";
        public static final String MUNICIPALITY = "municipality";

    }
}
