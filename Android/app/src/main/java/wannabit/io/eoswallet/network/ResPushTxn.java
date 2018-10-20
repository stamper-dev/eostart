package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResPushTxn {
    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("processed")
    private Processed processed;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Processed getProcessed() {
        return processed;
    }

    public void setProcessed(Processed processed) {
        this.processed = processed;
    }

    public class Processed {
        @SerializedName("id")
        private String id;

        @SerializedName("receipt")
        private Receipt receipt;

        @SerializedName("elapsed")
        private Long elapsed;

        @SerializedName("net_usage")
        private Long net_usage;

//        @SerializedName("except")
//        private String except;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Receipt getReceipt() {
            return receipt;
        }

        public void setReceipt(Receipt receipt) {
            this.receipt = receipt;
        }

        public Long getElapsed() {
            return elapsed;
        }

        public void setElapsed(Long elapsed) {
            this.elapsed = elapsed;
        }

        public Long getNet_usage() {
            return net_usage;
        }

        public void setNet_usage(Long net_usage) {
            this.net_usage = net_usage;
        }

        public class Receipt {
            @SerializedName("status")
            private String status;

            @SerializedName("cpu_usage_us")
            private Long cpu_usage_us;

            @SerializedName("net_usage_words")
            private Long net_usage_words;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Long getCpu_usage_us() {
                return cpu_usage_us;
            }

            public void setCpu_usage_us(Long cpu_usage_us) {
                this.cpu_usage_us = cpu_usage_us;
            }

            public Long getNet_usage_words() {
                return net_usage_words;
            }

            public void setNet_usage_words(Long net_usage_words) {
                this.net_usage_words = net_usage_words;
            }
        }
    }
}
/*
public class ResPushTxn {
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("processed")
    @Expose
    private Processed processed;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Processed getProcessed() {
        return processed;
    }

    public void setProcessed(Processed processed) {
        this.processed = processed;
    }

    public class Act {

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
        private Data data;
        @SerializedName("hex_data")
        @Expose
        private String hexData;

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

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getHexData() {
            return hexData;
        }

        public void setHexData(String hexData) {
            this.hexData = hexData;
        }

    }


    public class Act_ {

        @SerializedName("account")
        @Expose
        private String account;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("authorization")
        @Expose
        private List<Authorization_> authorization = null;
        @SerializedName("data")
        @Expose
        private Data_ data;
        @SerializedName("hex_data")
        @Expose
        private String hexData;

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

        public List<Authorization_> getAuthorization() {
            return authorization;
        }

        public void setAuthorization(List<Authorization_> authorization) {
            this.authorization = authorization;
        }

        public Data_ getData() {
            return data;
        }

        public void setData(Data_ data) {
            this.data = data;
        }

        public String getHexData() {
            return hexData;
        }

        public void setHexData(String hexData) {
            this.hexData = hexData;
        }

    }

    public class ActionTrace {

        @SerializedName("receipt")
        @Expose
        private Receipt_ receipt;
        @SerializedName("act")
        @Expose
        private Act act;
        @SerializedName("elapsed")
        @Expose
        private Integer elapsed;
        @SerializedName("cpu_usage")
        @Expose
        private Integer cpuUsage;
        @SerializedName("console")
        @Expose
        private String console;
        @SerializedName("total_cpu_usage")
        @Expose
        private Integer totalCpuUsage;
        @SerializedName("trx_id")
        @Expose
        private String trxId;
        @SerializedName("inline_traces")
        @Expose
        private List<InlineTrace> inlineTraces = null;

        public Receipt_ getReceipt() {
            return receipt;
        }

        public void setReceipt(Receipt_ receipt) {
            this.receipt = receipt;
        }

        public Act getAct() {
            return act;
        }

        public void setAct(Act act) {
            this.act = act;
        }

        public Integer getElapsed() {
            return elapsed;
        }

        public void setElapsed(Integer elapsed) {
            this.elapsed = elapsed;
        }

        public Integer getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(Integer cpuUsage) {
            this.cpuUsage = cpuUsage;
        }

        public String getConsole() {
            return console;
        }

        public void setConsole(String console) {
            this.console = console;
        }

        public Integer getTotalCpuUsage() {
            return totalCpuUsage;
        }

        public void setTotalCpuUsage(Integer totalCpuUsage) {
            this.totalCpuUsage = totalCpuUsage;
        }

        public String getTrxId() {
            return trxId;
        }

        public void setTrxId(String trxId) {
            this.trxId = trxId;
        }

        public List<InlineTrace> getInlineTraces() {
            return inlineTraces;
        }

        public void setInlineTraces(List<InlineTrace> inlineTraces) {
            this.inlineTraces = inlineTraces;
        }

    }

    public class Authorization {

        @SerializedName("actor")
        @Expose
        private String actor;
        @SerializedName("permission")
        @Expose
        private String permission;

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

    public class Authorization_ {

        @SerializedName("actor")
        @Expose
        private String actor;
        @SerializedName("permission")
        @Expose
        private String permission;

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

    public class Data {

        @SerializedName("from")
        @Expose
        private String from;
        @SerializedName("to")
        @Expose
        private String to;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("memo")
        @Expose
        private String memo;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

    }


    public class Data_ {

        @SerializedName("from")
        @Expose
        private String from;
        @SerializedName("to")
        @Expose
        private String to;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("memo")
        @Expose
        private String memo;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

    }

    public class InlineTrace {

        @SerializedName("receipt")
        @Expose
        private Receipt__ receipt;
        @SerializedName("act")
        @Expose
        private Act_ act;
        @SerializedName("elapsed")
        @Expose
        private Integer elapsed;
        @SerializedName("cpu_usage")
        @Expose
        private Integer cpuUsage;
        @SerializedName("console")
        @Expose
        private String console;
        @SerializedName("total_cpu_usage")
        @Expose
        private Integer totalCpuUsage;
        @SerializedName("trx_id")
        @Expose
        private String trxId;
        @SerializedName("inline_traces")
        @Expose
        private List<Object> inlineTraces = null;

        public Receipt__ getReceipt() {
            return receipt;
        }

        public void setReceipt(Receipt__ receipt) {
            this.receipt = receipt;
        }

        public Act_ getAct() {
            return act;
        }

        public void setAct(Act_ act) {
            this.act = act;
        }

        public Integer getElapsed() {
            return elapsed;
        }

        public void setElapsed(Integer elapsed) {
            this.elapsed = elapsed;
        }

        public Integer getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(Integer cpuUsage) {
            this.cpuUsage = cpuUsage;
        }

        public String getConsole() {
            return console;
        }

        public void setConsole(String console) {
            this.console = console;
        }

        public Integer getTotalCpuUsage() {
            return totalCpuUsage;
        }

        public void setTotalCpuUsage(Integer totalCpuUsage) {
            this.totalCpuUsage = totalCpuUsage;
        }

        public String getTrxId() {
            return trxId;
        }

        public void setTrxId(String trxId) {
            this.trxId = trxId;
        }

        public List<Object> getInlineTraces() {
            return inlineTraces;
        }

        public void setInlineTraces(List<Object> inlineTraces) {
            this.inlineTraces = inlineTraces;
        }

    }
    public class Processed {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("receipt")
        @Expose
        private Receipt receipt;
        @SerializedName("elapsed")
        @Expose
        private Integer elapsed;
        @SerializedName("net_usage")
        @Expose
        private Integer netUsage;
        @SerializedName("scheduled")
        @Expose
        private Boolean scheduled;
        @SerializedName("action_traces")
        @Expose
        private List<ActionTrace> actionTraces = null;
        @SerializedName("except")
        @Expose
        private Object except;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Receipt getReceipt() {
            return receipt;
        }

        public void setReceipt(Receipt receipt) {
            this.receipt = receipt;
        }

        public Integer getElapsed() {
            return elapsed;
        }

        public void setElapsed(Integer elapsed) {
            this.elapsed = elapsed;
        }

        public Integer getNetUsage() {
            return netUsage;
        }

        public void setNetUsage(Integer netUsage) {
            this.netUsage = netUsage;
        }

        public Boolean getScheduled() {
            return scheduled;
        }

        public void setScheduled(Boolean scheduled) {
            this.scheduled = scheduled;
        }

        public List<ActionTrace> getActionTraces() {
            return actionTraces;
        }

        public void setActionTraces(List<ActionTrace> actionTraces) {
            this.actionTraces = actionTraces;
        }

        public Object getExcept() {
            return except;
        }

        public void setExcept(Object except) {
            this.except = except;
        }

    }

    public class Receipt {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("cpu_usage_us")
        @Expose
        private Integer cpuUsageUs;
        @SerializedName("net_usage_words")
        @Expose
        private Integer netUsageWords;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getCpuUsageUs() {
            return cpuUsageUs;
        }

        public void setCpuUsageUs(Integer cpuUsageUs) {
            this.cpuUsageUs = cpuUsageUs;
        }

        public Integer getNetUsageWords() {
            return netUsageWords;
        }

        public void setNetUsageWords(Integer netUsageWords) {
            this.netUsageWords = netUsageWords;
        }

    }

    public class Receipt_ {

        @SerializedName("receiver")
        @Expose
        private String receiver;
        @SerializedName("act_digest")
        @Expose
        private String actDigest;
        @SerializedName("global_sequence")
        @Expose
        private Integer globalSequence;
        @SerializedName("recv_sequence")
        @Expose
        private Integer recvSequence;
        @SerializedName("auth_sequence")
        @Expose
        private List<List<String>> authSequence = null;
        @SerializedName("code_sequence")
        @Expose
        private Integer codeSequence;
        @SerializedName("abi_sequence")
        @Expose
        private Integer abiSequence;

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getActDigest() {
            return actDigest;
        }

        public void setActDigest(String actDigest) {
            this.actDigest = actDigest;
        }

        public Integer getGlobalSequence() {
            return globalSequence;
        }

        public void setGlobalSequence(Integer globalSequence) {
            this.globalSequence = globalSequence;
        }

        public Integer getRecvSequence() {
            return recvSequence;
        }

        public void setRecvSequence(Integer recvSequence) {
            this.recvSequence = recvSequence;
        }

        public List<List<String>> getAuthSequence() {
            return authSequence;
        }

        public void setAuthSequence(List<List<String>> authSequence) {
            this.authSequence = authSequence;
        }

        public Integer getCodeSequence() {
            return codeSequence;
        }

        public void setCodeSequence(Integer codeSequence) {
            this.codeSequence = codeSequence;
        }

        public Integer getAbiSequence() {
            return abiSequence;
        }

        public void setAbiSequence(Integer abiSequence) {
            this.abiSequence = abiSequence;
        }

    }

    public class Receipt__ {

        @SerializedName("receiver")
        @Expose
        private String receiver;
        @SerializedName("act_digest")
        @Expose
        private String actDigest;
        @SerializedName("global_sequence")
        @Expose
        private Integer globalSequence;
        @SerializedName("recv_sequence")
        @Expose
        private Integer recvSequence;
        @SerializedName("auth_sequence")
        @Expose
        private List<List<String>> authSequence = null;
        @SerializedName("code_sequence")
        @Expose
        private Integer codeSequence;
        @SerializedName("abi_sequence")
        @Expose
        private Integer abiSequence;

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getActDigest() {
            return actDigest;
        }

        public void setActDigest(String actDigest) {
            this.actDigest = actDigest;
        }

        public Integer getGlobalSequence() {
            return globalSequence;
        }

        public void setGlobalSequence(Integer globalSequence) {
            this.globalSequence = globalSequence;
        }

        public Integer getRecvSequence() {
            return recvSequence;
        }

        public void setRecvSequence(Integer recvSequence) {
            this.recvSequence = recvSequence;
        }

        public List<List<String>> getAuthSequence() {
            return authSequence;
        }

        public void setAuthSequence(List<List<String>> authSequence) {
            this.authSequence = authSequence;
        }

        public Integer getCodeSequence() {
            return codeSequence;
        }

        public void setCodeSequence(Integer codeSequence) {
            this.codeSequence = codeSequence;
        }

        public Integer getAbiSequence() {
            return abiSequence;
        }

        public void setAbiSequence(Integer abiSequence) {
            this.abiSequence = abiSequence;
        }

    }


}
*/
