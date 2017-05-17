package habit.duyle.habit.models;

import java.util.ArrayList;
import java.util.HashMap;

import habit.duyle.habit.services.MissingOrWrongDataException;

/**
 * Created by Duy Le on 1/3/2017.
 */

public class User {
    private ArrayList<String> friendUid;
    private String firstName;
    private String middleName;
    private String lastName;
    private ArrayList<String> profilePicsUrls;
    private String username;
    private HashMap<String, Integer> categoryLevels;

    public ArrayList<String> getFriendUid() {
        return friendUid;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUsername() {
        return username;
    }
    public ArrayList<String> getprofilePicsUrls() {
        return profilePicsUrls;
    }
    public HashMap<String, Integer> getCategoryLevels() {
        return categoryLevels;
    }


    public User(Builder builder){
        this.friendUid=builder.friendUid;
        this.firstName=builder.firstName;
        this.middleName=builder.middleName;
        this.lastName=builder.lastName;
        this.profilePicsUrls=builder.profilePicsUrls;
        this.username=builder.username;
        this.categoryLevels = builder.categoryLevels;
    }
    public User(){

    }
    public boolean isNull(){
        if(username==null||username.isEmpty()){
            return true;
        }
        if(firstName==null||firstName.isEmpty()){
            return true;
        }
        if(lastName==null||lastName.isEmpty()){
            return true;
        }
        if(profilePicsUrls==null||profilePicsUrls.isEmpty()){
            return true;
        }
        if(friendUid==null){
            return true;
        }
        if(categoryLevels==null){
            return true;
        }
        return false;
    }
    public static class Builder{
        private ArrayList<String> friendUid;
        private String firstName;
        private String middleName;
        private String lastName;
        private ArrayList<String> profilePicsUrls;
        private String username;
        private HashMap<String, Integer> categoryLevels;

        public Builder (){
        }
        public Builder friendUid(ArrayList<String> friendUid){
            this.friendUid = friendUid;
            return this;
        }
        public Builder firstName(String firstName){
            this.firstName = firstName;
            return this;
        }
        public Builder middleName(String middleName){
            this.middleName = middleName;
            return this;
        }
        public Builder lastName(String lastName){
            this.lastName = lastName;
            return this;
        }
        public Builder profilePicsUrls(ArrayList<String> profilePicsUrls){
            this.profilePicsUrls = profilePicsUrls;
            return this;
        }
        public Builder username(String username){
            this.username = username;
            return this;
        }
        public Builder categoryLevels(HashMap<String,Integer> categoryLevels){
            this.categoryLevels=categoryLevels;
            return this;
        }
        public Builder getEmptyFriendList(){
            ArrayList<String> friendList = new ArrayList<>();
            friendList.add("No friend");
            this.friendUid=friendList;
            return this;
        }
        public User build(){
            if(isNull()){
                try {
                    throw new MissingOrWrongDataException("User data is not completed!");
                } catch (MissingOrWrongDataException e) {
                    e.printStackTrace();
                }
            }
            else{
                return new User(this);
            }
            return null;
        }
        public boolean isNull(){
            if(username==null||username.isEmpty()){
                return true;
            }
            if(firstName==null||firstName.isEmpty()){
                return true;
            }
            //middle name is not necessary
            if(lastName==null||lastName.isEmpty()){
                return true;
            }
            if(profilePicsUrls==null||profilePicsUrls.isEmpty()){
                return true;
            }
            if(friendUid==null||friendUid.isEmpty()){
                return true;
            }
            return false;
        }

    }
    public String getFullName(){
        return firstName+" "+middleName+" "+lastName;
    }
}
