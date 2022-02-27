package parcero.uv.es;

public class Municipality {
    private int id;
    private int codMunicipality;
    private String nameMunicipality;
    private int numPCR;
    private String cumulativePCR;
    private int numPCR14;
    private String cumulativePCR14;
    private int deaths;
    private String deathRate;

    public Municipality(int id, int codMunicipality, String nameMunicipality, int numPCR, String cumulativePCR, int numPCR14, String cumulativePCR14, int deaths, String deathRate) {
        this.id = id;
        this.codMunicipality = codMunicipality;
        this.nameMunicipality = nameMunicipality;
        this.numPCR = numPCR;
        this.cumulativePCR = cumulativePCR;
        this.numPCR14 = numPCR14;
        this.cumulativePCR14 = cumulativePCR14;
        this.deaths = deaths;
        this.deathRate = deathRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodMunicipality() {
        return codMunicipality;
    }

    public void setCodMunicipality(int codMunicipality) {
        this.codMunicipality = codMunicipality;
    }

    public String getNameMunicipality() {
        return nameMunicipality;
    }

    public void setNameMunicipality(String nameMunicipality) {
        this.nameMunicipality = nameMunicipality;
    }

    public int getNumPCR() {
        return numPCR;
    }

    public void setNumPCR(int numPCR) {
        this.numPCR = numPCR;
    }

    public String getCumulativePCR() {
        return cumulativePCR;
    }

    public void setCumulativePCR(String cumulativePCR) {
        this.cumulativePCR = cumulativePCR;
    }

    public int getNumPCR14() {
        return numPCR14;
    }

    public void setNumPCR14(int numPCR14) {
        this.numPCR14 = numPCR14;
    }

    public String getCumulativePCR14() {
        return cumulativePCR14;
    }

    public void setCumulativePCR14(String cumulativePCR14) {
        this.cumulativePCR14 = cumulativePCR14;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public String getDeathRate() {
        return deathRate;
    }

    public void setDeathRate(String deathRate) {
        this.deathRate = deathRate;
    }

    @Override
    public String toString() {
        return "Municipality{" +
                "id=" + id +
                ", codMunicipality=" + codMunicipality +
                ", nameMunicipality='" + nameMunicipality + '\'' +
                ", numPCR=" + numPCR +
                ", cumulativePCR='" + cumulativePCR + '\'' +
                ", numPCR14=" + numPCR14 +
                ", cumulativePCR14='" + cumulativePCR14 + '\'' +
                ", deaths=" + deaths +
                ", deathRate='" + deathRate + '\'' +
                '}';
    }
}
