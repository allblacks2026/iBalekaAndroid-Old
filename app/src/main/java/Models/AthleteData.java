package Models;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Okuhle on 7/5/2016.
 */
public class AthleteData  {

    private String athleteDataTitle;
    private String athleteDataValue;
    private String athleteDataFooter;

    public AthleteData(String athleteDataTitle, String athleteDataValue, String athleteDataFooter) {
        this.athleteDataTitle = athleteDataTitle;
        this.athleteDataValue = athleteDataValue;
        this.athleteDataFooter = athleteDataFooter;
    }

    public String getAthleteDataTitle() {
        return athleteDataTitle;
    }

    public void setAthleteDataTitle(String athleteDataTitle) {
        this.athleteDataTitle = athleteDataTitle;
    }

    public String getAthleteDataValue() {
        return athleteDataValue;
    }

    public void setAthleteDataValue(String athleteDataValue) {
        this.athleteDataValue = athleteDataValue;
    }

    public String getAthleteDataFooter() {
        return athleteDataFooter;
    }

    public void setAthleteDataFooter(String athleteDataFooter) {
        this.athleteDataFooter = athleteDataFooter;
    }
}
