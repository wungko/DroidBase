package retrofit2;

/**
 * Created by wungko on 16/7/18.
 */
public interface CallbackListener {
    void onOk(Response response);
    void onError(Throwable t);
}
