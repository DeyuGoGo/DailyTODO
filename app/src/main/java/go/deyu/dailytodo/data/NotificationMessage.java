package go.deyu.dailytodo.data;

/**
 * Created by huangeyu on 15/10/5.
 */
public interface NotificationMessage {
    public String getMessage();

    public void setMessage(String message);

    public int getId();

    public void setId(int id);

    public int getState();

    public void setState(int state);

    public int getHour();

    public void setHour(int hour);

    public int getMin() ;

    public void setMin(int min);
}
