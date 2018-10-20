package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

public class ResVersion {
    @SerializedName("acceptableVersion")
    String acceptableVersion;

    @SerializedName("lastVersion")
    String lastVersion;

    @SerializedName("latestVersion")
    String latestVersion;

    public String getAcceptableVersion() {
        return acceptableVersion;
    }

    public void setAcceptableVersion(String acceptableVersion) {
        this.acceptableVersion = acceptableVersion;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }
}
