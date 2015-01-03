package tr.xip.wanikani.tasks.callbacks;

import tr.xip.wanikani.api.response.SRSDistribution;

/**
 * Created by Hikari on 1/3/15.
 */
public interface SRSDistributionGetTaskCallbacks {
    public void onSRSDistributionGetTaskPreExecute();

    public void onSRSDistributionGetTaskPostExecute(SRSDistribution distribution);
}