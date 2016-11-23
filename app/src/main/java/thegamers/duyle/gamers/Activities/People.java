package thegamers.duyle.gamers.Activities;

/**
 * Created by Duy Le on 11/23/2016.
 */

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = "FeedData")
public class People {
    private String habitName;
    private int amountOfDay;
    private String typeOfLength;
    private String description;
    private String publicity;
    private String addedTime;
    private String addedDate;

    @DynamoDBRangeKey(attributeName = "addedDate")
    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    @DynamoDBHashKey(attributeName = "habitName")
    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    @DynamoDBAttribute(attributeName = "amountOfDay")
    public int getAmountOfDay() {
        return amountOfDay;
    }

    public void setAmountOfDay(int amountOfDay) {
        this.amountOfDay = amountOfDay;
    }

    @DynamoDBAttribute(attributeName = "addedTime")
    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    @DynamoDBAttribute(attributeName = "typeOfLength")
    public String getTypeOfLength() {
        return typeOfLength;
    }

    public void setTypeOfLength(String typeOfLength) {
        this.typeOfLength = typeOfLength;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "publicity")
    public String getPublicity() {
        return publicity;
    }

    public void setPublicity(String publicity) {
        this.publicity = publicity;
    }
}

