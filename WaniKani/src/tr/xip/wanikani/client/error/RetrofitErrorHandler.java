package tr.xip.wanikani.client.error;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.net.SocketTimeoutException;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import tr.xip.wanikani.content.receiver.BroadcastIntents;

/**
 * Created by xihsa_000 on 3/16/14.
 */
public class RetrofitErrorHandler implements ErrorHandler {
    Context context;
    String TAG = "WANIKANI API";

    public static final String RETROFIT_ERROR_TIMEOUT = "timeout";
    public static final String RETROFIT_ERROR_CONNECTION = "connection";
    public static final String RETROFIT_ERROR_UNKNOWN = "unknown";

    public RetrofitErrorHandler(Context context) {
        this.context = context;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        if (cause.isNetworkError()) {
            if (cause.getCause() instanceof SocketTimeoutException) {
                Log.d(TAG, "Timeout Error: " + cause.getResponse());
                Intent intent = new Intent(BroadcastIntents.RETROFIT_ERROR_TIMEOUT());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            } else {
                Log.d(TAG, "Connection Error: " + cause.getResponse());
                Intent intent = new Intent(BroadcastIntents.RETROFIT_ERROR_CONNECTION());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        } else {
            Log.d(TAG, "Unknown Error: " + cause.getResponse());
            Intent intent = new Intent(BroadcastIntents.RETROFIT_ERROR_UNKNOWN());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
        return null;
    }
}