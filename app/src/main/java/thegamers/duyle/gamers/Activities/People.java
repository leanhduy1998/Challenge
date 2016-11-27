package thegamers.duyle.gamers.Activities;

/**
 * Created by Duy Le on 11/23/2016.
 */

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = "FeedData")
public class People {
    private String habitName;
    private String username;
    private int amountOfDay;
    private String typeOfLength;
    private String description;
    private String publicity;
    private Long addedTime;


    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @DynamoDBRangeKey(attributeName = "addedTime")
    public Long getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(Long addedTime) {
        this.addedTime = addedTime;
    }

    @DynamoDBIndexHashKey(attributeName = "habitName")
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

