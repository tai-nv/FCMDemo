package DataManager;

/**
 * Created by hungbin on 10/21/2016.
 */
public interface OnLoadDataComplete<T> {
    void onComplete(T item);

    void onUpdate(T item);
}
