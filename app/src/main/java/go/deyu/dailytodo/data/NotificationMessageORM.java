package go.deyu.dailytodo.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by huangeyu on 15/5/19.
 */
@DatabaseTable
public class NotificationMessageORM implements NotificationMessage{

    @DatabaseField(generatedId = true)
    private int id ;

    @DatabaseField
    private String message ;

    @DatabaseField(defaultValue = "0")
    private int hour ;

    @DatabaseField(defaultValue = "0")
    private int min ;

//    0 = finish
//    1 = not finish
    public static final int STATE_FINISH = 0;
    public static final int STATE_NOT_FINISH = 1;

    @DatabaseField
    private int state;

    public NotificationMessageORM(){
    }

    public NotificationMessageORM(String message){
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

    @Override
    public String toString() {
        return "this is id : " + id +" , message : " + message + " , state : " + state;
    }
}
