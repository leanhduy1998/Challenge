package habit.duyle.habit.models;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;

import habit.duyle.habit.services.MissingOrWrongDataException;

/**
 * Created by Duy Le on 11/28/2016.
 */

public class Feed {
    public String getUsername() {
        return username;
    }
    public int getAmountOfDay() {
        return amountOfDay;
    }
    public String getTypeOfLength() {
        return typeOfLength;
    }
    public String getFeedDescription() {
        return feedDescription;
    }
    public String getPublicity() {
        return publicity;
    }
    public long getUpdatedTime() {
        return updatedTime;
    }
    public String getUid() {
        return uid;
    }
    public String getHabitName() {
        return habitName;
    }
    public ArrayList<DayDesciption> getDayDescriptionArrayList() {
        return dayDescriptionArrayList;
    }
    public ArrayList<FeedSinglePicture> getAllFeedPicture() {
        return allFeedPicture;
    }
    public long getCreatedTime() {
        return createdTime;
    }
    public void setAllFeedPicture(ArrayList<FeedSinglePicture> allFeedPicture) {
        this.allFeedPicture = allFeedPicture;
    }

    private String habitName;
    private String username;
    private int amountOfDay;
    private String typeOfLength;
    private String feedDescription;
    private ArrayList<DayDesciption> dayDescriptionArrayList;
    private String publicity;
    private ArrayList<FeedSinglePicture> allFeedPicture = new ArrayList<FeedSinglePicture>();
    private long updatedTime;
    private long createdTime;
    private String uid;
    private ArrayList<Category> categoryArrayList;



    public static class Builder {
        private String habitName;
        private String username;
        private int amountOfDay;
        private String typeOfLength;
        private String feedDescription;
        private ArrayList<DayDesciption> dayDescriptionArrayList;
        private String publicity;
        private ArrayList<FeedSinglePicture> allFeedPicture = new ArrayList<FeedSinglePicture>();
        private long updatedTime;
        private long createdTime;
        private String uid;
        private ArrayList<Category> categoryArrayList;

        public Builder dayDescriptionArrayList(ArrayList<DayDesciption> dayDescriptionArrayList) {
            this.dayDescriptionArrayList = dayDescriptionArrayList;
            return this;
        }

        public Builder allFeedPicture(ArrayList<FeedSinglePicture> allFeedPicture) {
            this.allFeedPicture = allFeedPicture;
            return this;
        }

        public Builder habitName(String habitName) {
            this.habitName = habitName;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder amountOfDay(int amountOfDay) {
            this.amountOfDay = amountOfDay;
            return this;
        }

        public Builder typeOfLength(String typeOfLength) {
            this.typeOfLength = typeOfLength;
            return this;
        }

        public Builder feedDescription(String feedDescription) {
            this.feedDescription = feedDescription;
            return this;
        }

        public Builder publicity(String publicity) {
            this.publicity = publicity;
            return this;
        }

        public Builder updatedTime(long updatedTime) {
            this.updatedTime = updatedTime;
            return this;
        }

        public Builder createdTime(long createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public Builder uid(String uid) {
            this.uid = uid;
            return this;
        }
        public Builder categoryArrayList(ArrayList<Category>categoryArrayList){
            this.categoryArrayList=categoryArrayList;
            return this;
        }

        public Feed build() {
            if (isNull()) {
                try {
                    throw new MissingOrWrongDataException("Feed data is not completed!");
                } catch (MissingOrWrongDataException e) {
                    e.printStackTrace();
                }
            } else {
                return new Feed(this);
            }
            return null;
        }

        public Feed buildEmptyFeed() {
            return new Feed(this);
        }

        public boolean isNull() {
            if (username == null || username.isEmpty()) {
                return true;
            }
            if (habitName == null || habitName.isEmpty()) {
                return true;
            }
            if (amountOfDay == 0) {
                return true;
            }
            if (typeOfLength == null || typeOfLength.isEmpty()) {
                return true;
            }
            if (feedDescription == null || feedDescription.isEmpty()) {
                return true;
            }
            if (publicity == null || publicity.isEmpty()) {
                return true;
            }
            if (allFeedPicture == null) {
                return true;
            }
            if (updatedTime == 0) {
                return true;
            }
            if (dayDescriptionArrayList == null) {
                return true;
            }
            if (createdTime == 0) {
                return true;
            }
            if (uid == null || uid.isEmpty()) {
                return true;
            }
            if(categoryArrayList==null){
                return true;
            }
            return false;
        }
    }

    private Feed(Builder builder) {
        this.habitName = builder.habitName;
        this.username = builder.username;
        this.amountOfDay = builder.amountOfDay;
        this.typeOfLength = builder.typeOfLength;
        this.feedDescription = builder.feedDescription;
        this.publicity = builder.publicity;
        this.allFeedPicture = builder.allFeedPicture;
        this.updatedTime = builder.updatedTime;
        this.dayDescriptionArrayList = builder.dayDescriptionArrayList;
        this.createdTime = builder.createdTime;
        this.uid = builder.uid;
    }

    public static Feed getEmptyFeed() {
        return new Builder().allFeedPicture(null)
                .username(null)
                .amountOfDay(0)
                .createdTime(0)
                .dayDescriptionArrayList(null)
                .feedDescription(null)
                .habitName(null)
                .publicity(null)
                .typeOfLength(null)
                .uid(null)
                .updatedTime(0)
                .username(null).buildEmptyFeed();
    }

    public boolean isNull() {
        if (username == null || username.isEmpty()) {
            return true;
        } else if (habitName == null || habitName.isEmpty()) {
            return true;
        } else if (amountOfDay == 0) {
            return true;
        } else if (typeOfLength == null || typeOfLength.isEmpty()) {
            return true;
        } else if (feedDescription == null || feedDescription.isEmpty()) {
            return true;
        } else if (publicity == null || publicity.isEmpty()) {
            return true;
        } else if (allFeedPicture == null) {
            return true;
        } else if (updatedTime == 0) {
            return true;
        } else if (dayDescriptionArrayList == null) {
            return true;
        } else if (createdTime == 0) {
            return true;
        } else if (uid == null || uid.isEmpty()) {
            return true;
        }
        return false;
    }
    private static Comparator<Feed> feedUpdateTimeComparator = new Comparator<Feed>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(Feed object1, Feed object2) {
            return Long.compare(object2.getUpdatedTime(),object1.getUpdatedTime());
        }
    };
    public static Comparator<Feed> getFeedUpdateTimeComparator(){
        return feedUpdateTimeComparator;
    }
}

