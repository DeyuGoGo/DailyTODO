package go.deyu.dailytodo.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmModule;

/**
 * Created by huangeyu on 15/10/4.
 */
public class NotificationMessageRM extends RealmObject implements NotificationMessage {

    @PrimaryKey
    private int id;

    private String message;

    private int hour = 0;

    private int min = 0;

    private int state = STATE_NOT_FINISH;

    //    0 = finish
//    1 = not finish
    public static final int STATE_FINISH = 0;
    public static final int STATE_NOT_FINISH = 1;


    public NotificationMessageRM() {
    }

    public NotificationMessageRM(String message) {
        this.message = message;
        this.state = STATE_NOT_FINISH;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

}

