package tr.xip.wanikani.database.task.callback;

import tr.xip.wanikani.models.SRSDistribution;

/**
 * Created by Hikari on 1/7/15.
 */
public interface SrsDistributionLoadTaskCallbacks {
    public void onSrsDistributionLoaded(SRSDistribution distribution);
}
