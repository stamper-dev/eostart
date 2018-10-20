package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReqPushTxn {

    @SerializedName("compression")
    @Expose
    private String compression;
    @SerializedName("transaction")
    @Expose
    private Transaction transaction;
    @SerializedName("signatures")
    @Expose
    private List<String> signatures = null;

    /**
     *
     * @param transaction
     * @param signatures
     */
    public ReqPushTxn( Transaction transaction, List<String> signatures) {
        super();
        this.compression = "none";
        this.transaction = transaction;
        this.signatures = signatures;
    }

    public String getCompression() {
        return compression;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }


    public static class Transaction {
        @SerializedName("expiration")
        @Expose
        private String expiration;
        @SerializedName("ref_block_num")
        @Expose
        private Integer refBlockNum;
        @SerializedName("ref_block_prefix")
        @Expose
        private Long refBlockPrefix;
        @SerializedName("context_free_actions")
        @Expose
        private List<Object> contextFreeActions = null;
        @SerializedName("actions")
        @Expose
        private List<Action> actions = null;
        @SerializedName("transaction_extensions")
        @Expose
        private List<Object> transactionExtensions = null;

        /**
         *
         * @param refBlockPrefix
         * @param expiration
         * @param refBlockNum
         * @param actions
         */
        public Transaction(String expiration, Integer refBlockNum, Long refBlockPrefix, List<Action> actions) {
            super();
            this.expiration = expiration;
            this.refBlockNum = refBlockNum;
            this.refBlockPrefix = refBlockPrefix;
            this.contextFreeActions = null;
            this.actions = actions;
            this.transactionExtensions = null;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public Integer getRefBlockNum() {
            return refBlockNum;
        }

        public void setRefBlockNum(Integer refBlockNum) {
            this.refBlockNum = refBlockNum;
        }

        public Long getRefBlockPrefix() {
            return refBlockPrefix;
        }

        public void setRefBlockPrefix(Long refBlockPrefix) {
            this.refBlockPrefix = refBlockPrefix;
        }

        public List<Object> getContextFreeActions() {
            return contextFreeActions;
        }

        public void setContextFreeActions(List<Object> contextFreeActions) {
            this.contextFreeActions = contextFreeActions;
        }

        public List<Action> getActions() {
            return actions;
        }

        public void setActions(List<Action> actions) {
            this.actions = actions;
        }

        public List<Object> getTransactionExtensions() {
            return transactionExtensions;
        }

        public void setTransactionExtensions(List<Object> transactionExtensions) {
            this.transactionExtensions = transactionExtensions;
        }

    }


    public static class Action {

        @SerializedName("account")
        @Expose
        private String account;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("authorization")
        @Expose
        private List<Authorization> authorization = null;
        @SerializedName("data")
        @Expose
        private String data;

        /**
         *
         * @param name
         * @param data
         * @param account
         * @param authorization
         */
        public Action(String account, String name, List<Authorization> authorization, String data) {
            super();
            this.account = account;
            this.name = name;
            this.authorization = authorization;
            this.data = data;
        }


        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Authorization> getAuthorization() {
            return authorization;
        }

        public void setAuthorization(List<Authorization> authorization) {
            this.authorization = authorization;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

    }


    public static class Authorization {
        @SerializedName("actor")
        @Expose
        private String actor;
        @SerializedName("permission")
        @Expose
        private String permission;

        /**
         *
         * @param permission
         * @param actor
         */
        public Authorization(String actor, String permission) {
            super();
            this.actor = actor;
            this.permission = permission;
        }


        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public String getPermission() {
            return permission;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }

    }
}
