package tr.xip.wanikani.db.tasks.callbacks;

import tr.xip.wanikani.api.response.SRSDistribution;

/**
 * Created by Hikari on 1/7/15.
 */
public interface SrsDistributionLoadTaskCallbacks {
    public void onSrsDistributionLoaded(SRSDistribution distribution);
}
