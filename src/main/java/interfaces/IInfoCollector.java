package interfaces;

import java.io.File;

public interface IInfoCollector {
    /**
     * gets the rewards and takes a screenshot
     * @return returns the rewards screenshot as a File object
     */
    public File getRewardsScreenshot();
}
