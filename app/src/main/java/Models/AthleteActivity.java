package Models;

/**
 * Created by Okuhle on 4/3/2016.
 */
public class AthleteActivity {

    private String title;
    private double distanceCovered;
    private String dateCovered;

    public AthleteActivity(String dateCovered, double distanceCovered, String title) {
        this.dateCovered = dateCovered;
        this.distanceCovered = distanceCovered;
        this.title = title;
    }

    public String getDateCovered() {
        return dateCovered;
    }

    public void setDateCovered(String dateCovered) {
        this.dateCovered = dateCovered;
    }

    public double getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(double distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
