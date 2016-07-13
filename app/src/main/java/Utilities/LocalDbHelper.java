package Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import Models.Route;

//Source: https://www.youtube.com/watch?v=T0ClYrJukPA
public class LocalDbHelper extends SQLiteOpenHelper {
    //SQLite is case sensative
    private static final String databaseName = "iBaleka.db"; //Name of the database
    private static final String routeTable = "Route"; //The name of the mapped route table
    private static final String routeCheckpointTable = "Route_Checkpoint"; //route points
    private static String [] routeColumns= new String [] {"RouteID", "StartPoint", "EndPoint",
            "Distance", "DateRecorded", "MappedImage"};
    private static String [] routeCheckpointColumns = new String[] {"CheckpointID", "Latitude",
            "Longitude"};
    public LocalDbHelper(Context context) {
        super(context, databaseName, null, 1);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+routeTable + "( "+routeColumns[0] +" TEXT PRIMARY KEY, " +
                ""+routeColumns[1]
                +" TEXT, " +
                ""+routeColumns[2] +" TEXT, "+routeColumns[3] +", "+routeColumns[4] +" TEXT, " +
                ""+routeColumns[5]+" TEXT);");
        db.execSQL("CREATE TABLE "+routeCheckpointTable +"("+routeCheckpointColumns[0] +"TEXT " +
                "PRIMARY KEY, "+routeCheckpointColumns[1] +" TEXT, "+routeCheckpointColumns[2]+")" +
                " TEXT" +
                ";");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+routeTable);
        db.execSQL("DROP TABLE IF EXISTS "+routeCheckpointTable);
        onCreate(db);
    }
    public boolean insertRoute(Route newRoute) {
        //Add the new route to the table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(routeColumns[0], newRoute.getRouteID()); //Route ID
        values.put(routeColumns[1], newRoute.getStartPoint()); //Route Start Point
        values.put(routeColumns[2], newRoute.getEndPoint()); //Route End point
        values.put(routeColumns[3], newRoute.getDistance()); //Route distance
        values.put(routeColumns[4], newRoute.getDateRecorded()); //Route Date recorded
        byte [] compressedImage = compressImage(newRoute.getMapImage());
        values.put(routeColumns[5], compressedImage); //The mapped Image (converted)

        long insertResult = db.insert(routeTable, null, values);
        if (insertResult < 0) {
            return false;
        } else {
            return true;
        }
    }

    private byte [] compressImage(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
