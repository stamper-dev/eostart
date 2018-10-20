package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

public class ReqParkAction {

    @SerializedName("account_name")
    String account_name;

    @SerializedName("page_num")
    int page_num;

    @SerializedName("page_size")
    int page_size;

    @SerializedName("tab_name")
    String tab_name;

    @SerializedName("symbol")
    String symbol;

    @SerializedName("issue_account")
    String issue_account;

    @SerializedName("interface_name")
    String interface_name;

    public ReqParkAction(String account_name, int page_num, int page_size, String tab_name, String symbol, String issue_account, String interface_name) {
        this.account_name = account_name;
        this.page_num = page_num;
        this.page_size = page_size;
        this.tab_name = tab_name;
        this.symbol = symbol;
        this.issue_account = issue_account;
        this.interface_name = interface_name;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public String getTab_name() {
        return tab_name;
    }

    public void setTab_name(String tab_name) {
        this.tab_name = tab_name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getIssue_account() {
        return issue_account;
    }

    public void setIssue_account(String issue_account) {
        this.issue_account = issue_account;
    }

    public String getInterface_name() {
        return interface_name;
    }

    public void setInterface_name(String interface_name) {
        this.interface_name = interface_name;
    }
}
