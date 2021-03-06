package go.deyu.dailytodo.model;

import java.util.List;

/**
 * Created by huangeyu on 15/8/2.
 */
public interface MessageModelInterface<T> {
    public void addMessage(String message);
    public T findMessageById(int messageid);
    public void deleteMessage(int id);
    public void changeMessage(int id , String message);
    public void changeMessageState(int id , int state);
    public void changeMessageAlarmTime(int id , int hour, int min);
    public void refreshMessage();
    public List<T> getMessages();
    public void checkChangeDay();
    public void close();
    public void registerListener(OnMessageChangeListener listener);
    public void unregisterListener(OnMessageChangeListener listener);
}
