package tr.xip.wanikani.client.task.callback;

import tr.xip.wanikani.models.SRSDistribution;

/**
 * Created by Hikari on 1/3/15.
 */
public interface SRSDistributionGetTaskCallbacks {
    public void onSRSDistributionGetTaskPreExecute();

    public void onSRSDistributionGetTaskPostExecute(SRSDistribution distribution);
}