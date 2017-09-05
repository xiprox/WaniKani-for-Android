package tr.xip.wanikani.client.task.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.wanikani.client.error.RetrofitErrorHandler;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.models.Storable;
import tr.xip.wanikani.models.User;

public abstract class ThroughDbCallback<T extends Request<B>, B extends Storable> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        T result = response.body();
        if (result == null) return;

        final User userInfo = result.user_information;
        final B requestedInfo = result.requested_information;

        if (result.error == null && (userInfo != null || requestedInfo != null)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (userInfo != null) {
                        userInfo.save();
                    }
                    if (requestedInfo != null) {
                        requestedInfo.save();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        RetrofitErrorHandler.handleError(t);
    }
}
