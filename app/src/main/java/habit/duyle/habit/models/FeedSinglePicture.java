package habit.duyle.habit.models;

import java.util.Comparator;

import habit.duyle.habit.services.MissingOrWrongDataException;

/**
 * Created by Duy Le on 12/5/2016.
 */

public class FeedSinglePicture {
    private String pictureName;
    private String pictureStoragePath;
    private String thumbnailStoragePath;
    private String feedName;
    private int day;

    private FeedSinglePicture(FeedSinglePicture.Builder builder){
        this.pictureName=builder.pictureName;
        this.pictureStoragePath=builder.pictureStoragePath;
        this.day=builder.day;
        this.feedName=builder.feedName;
        this.thumbnailStoragePath=builder.thumbnailStoragePath;
    }
    public  FeedSinglePicture(){

    }
    public String getPictureName() {
        return pictureName;
    }
    public String getPictureStoragePath() {
        return pictureStoragePath;
    }
    public int getDay() {
        return day;
    }
    public String getFeedName() {return feedName;}
    public String getThumbnailStoragePath(){
        return thumbnailStoragePath;
    }

    public static class Builder{
        private String pictureName;
        private String pictureStoragePath;
        private String thumbnailStoragePath;
        private String feedName;
        private int day;

        public Builder pictureName(String pictureName){
            this.pictureName=pictureName;
            return this;
        }
        public Builder pictureStoragePath(String pictureStoragePath){
            this.pictureStoragePath=pictureStoragePath;
            return this;
        }
        public Builder thumbnailStoragePath(String thumbnailStoragePath){
            this.thumbnailStoragePath=thumbnailStoragePath;
            return this;
        }
        public Builder feedName (String feedName){
            this.feedName=feedName;
            return this;
        }
        public Builder day(int day){
            this.day=day;
            return this;
        }
        public FeedSinglePicture build(){
            if(isNull()){
                try {
                    throw new MissingOrWrongDataException("Feed data is not completed!");
                } catch (MissingOrWrongDataException e) {
                    e.printStackTrace();
                }
                return null;
            }
            return new FeedSinglePicture(this);
        }
        private boolean isNull(){
            if(pictureName==null||pictureName.isEmpty()){
                return true;
            }
            if(pictureStoragePath==null||pictureStoragePath.isEmpty()){
                return true;
            }
            if(thumbnailStoragePath==null||thumbnailStoragePath.isEmpty()){
                return true;
            }
            if(feedName==null||feedName.isEmpty()){
                return true;
            }
            if(day==0){
                return true;
            }
            return false;
        }
    }


    private static Comparator pictureComparator = new Comparator<FeedSinglePicture>() {
        @Override
        public int compare(FeedSinglePicture o1, FeedSinglePicture o2) {
            if(o1.getDay()>o2.getDay()){
                return 1;
            }
            else{
                return -1;
            }
        }
    };
    public static Comparator getPictureComparator() {
        return pictureComparator;
    }

}
