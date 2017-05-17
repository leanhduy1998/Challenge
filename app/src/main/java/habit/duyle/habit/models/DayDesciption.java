package habit.duyle.habit.models;

/**
 * Created by Duy Le on 12/21/2016.
 */

public class DayDesciption {
    private int day;
    private String dayDescription;


    public int getDay() {
        return day;
    }

    public String getDayDescription() {
        return dayDescription;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public void setDayDescription(String dayDescription) {
        this.dayDescription = dayDescription;
    }

    public  DayDesciption(){

    }
    public  DayDesciption(int day, String dayDescription){
        this.day = day;
        this.dayDescription = dayDescription;
    }
}
