package Utilities;

import android.app.Application;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import Models.Checkpoint;

/**
 * Created by Okuhle on 7/24/2016.
 */
public class DataContainer {

    private static DataContainer dataContainer;
    private List<Checkpoint> checkpointList;

    public static DataContainer getDataContainerInstance()
    {
        if (dataContainer == null) {
            dataContainer = new DataContainer();
        }
        return dataContainer;
    }

    public List<Checkpoint> getCheckpointList() {
        return this.checkpointList;
    }

    public void setCheckpointList(List<Checkpoint> list) {
        this.checkpointList = list;
    }

    private DataContainer()
    {
        checkpointList = new ArrayList<>();
    }
}
