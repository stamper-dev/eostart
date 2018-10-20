package wannabit.io.eoswallet.model;

public class WBUser {
    private Long    id;
    private String  account;
    private String  userinfo;

    public WBUser() {
    }

    public WBUser(String account, String userinfo) {
        this.account = account;
        this.userinfo = userinfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

//    private Long    id;
//    private String  account;
//    private String  signature;
//    private String  userinfo;
//    private int     order;
//
//    public WBUser() {
//    }
//
//    public WBUser(String account, String signature, String userinfo) {
//        this.account = account;
//        this.signature = signature;
//        this.userinfo = userinfo;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getAccount() {
//        return account;
//    }
//
//    public void setAccount(String account) {
//        this.account = account;
//    }
//
//    public String getSignature() {
//        return signature;
//    }
//
//    public void setSignature(String signature) {
//        this.signature = signature;
//    }
//
//    public String getUserinfo() {
//        return userinfo;
//    }
//
//    public void setUserinfo(String userinfo) {
//        this.userinfo = userinfo;
//    }
//
//    public int getOrder() {
//        return order;
//    }
//
//    public void setOrder(int order) {
//        this.order = order;
//    }
}
