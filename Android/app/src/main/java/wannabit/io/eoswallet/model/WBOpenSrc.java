package wannabit.io.eoswallet.model;

public class WBOpenSrc {

    int id;
    String openSrcName;
    String licenseType;

    public WBOpenSrc() {}

    public WBOpenSrc(int id, String openSrcName, String licenseType) {
        this.id = id;
        this.openSrcName = openSrcName;
        this.licenseType = licenseType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenSrcName() {
        return openSrcName;
    }

    public void setOpenSrcName(String openSrcName) {
        this.openSrcName = openSrcName;
    }

    public String getLicenseType() { return licenseType; }

    public void setLicenseType(String licenseType) { this.licenseType = licenseType; }

}
