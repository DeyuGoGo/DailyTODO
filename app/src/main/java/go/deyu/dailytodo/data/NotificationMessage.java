package go.deyu.dailytodo.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by huangeyu on 15/5/19.
 */
@DatabaseTable
public class NotificationMessage {

    @DatabaseField(generatedId = true)
    private Integer id ;

    @DatabaseField
    private String message ;

    @DatabaseField(defaultValue = "0")
    private Integer hour ;

    @DatabaseField(defaultValue = "0")
    private Integer min ;

//    0 = finish
//    1 = not finish
    public static final int STATE_FINISH = 0;
    public static final int STATE_NOT_FINISH = 1;

    @DatabaseField
    private Integer state ;

    public NotificationMessage(){
    }

    public NotificationMessage(String message){
        this.message = message;
        this.state = STATE_NOT_FINISH;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    @Override
    public String toString() {
        return "this is id : " + id +" , message : " + message + " , state : " + state;
    }
}
