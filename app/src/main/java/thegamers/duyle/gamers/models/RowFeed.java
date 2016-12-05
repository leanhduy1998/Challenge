package thegamers.duyle.gamers.models;

import android.os.Environment;

import java.io.File;

/**
 * Created by Duy Le on 11/30/2016.
 */

public class RowFeed {
    private String habitName;
    private String habitPath;
    private String username;

    public String getUsername() {
        return username;
    }

    public int getAmountOfDay() {
        return amountOfDay;
    }

    public String getTypeOfLength() {
        return typeOfLength;
    }

    public String getDescription() {
        return description;
    }

    public String getPublicity() {
        return publicity;
    }

    public Long getAddedTime() {
        return addedTime;
    }

    private int amountOfDay;
    private String typeOfLength;
    private String description;
    private String publicity;
    private Long addedTime;

    public String getHabitName() {
        return habitName;
    }

    public String getHabitPath() {
        return habitPath;
    }
    public String getHabitSavePath(){
        return habitPath+"/";
    }

    public RowFeed(String habitName,int amountOfDay, String typeOfLength, String description, String publicity, Long addedTime){
        this.habitName=habitName;
        habitPath= Environment.getExternalStorageDirectory().toString()+"/camera_app/save/"+habitName;
        this.username=username;
        this.amountOfDay=amountOfDay;
        this.typeOfLength=typeOfLength;
        this.description=description;
        this.publicity=publicity;
        this.addedTime=addedTime;
        this.username="admin";

        File file = new File(habitPath);
        if(!file.exists()){
            file.mkdir();
        }
    }
}
