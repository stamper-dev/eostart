package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResBlock {

    @SerializedName("timestamp")
    String timestamp;

    @SerializedName("block_num")
    int block_num;

    @SerializedName("ref_block_prefix")
    Long ref_block_prefix;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getBlock_num() {
        return block_num;
    }

    public void setBlock_num(int block_num) {
        this.block_num = block_num;
    }

    public Long getRef_block_prefix() {
        return ref_block_prefix;
    }

    public void setRef_block_prefix(Long ref_block_prefix) {
        this.ref_block_prefix = ref_block_prefix;
    }

    /*
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("producer")
    @Expose
    private String producer;
    @SerializedName("confirmed")
    @Expose
    private Integer confirmed;
    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("transaction_mroot")
    @Expose
    private String transactionMroot;
    @SerializedName("action_mroot")
    @Expose
    private String actionMroot;
    @SerializedName("schedule_version")
    @Expose
    private Integer scheduleVersion;
    @SerializedName("new_producers")
    @Expose
    private Object newProducers;
    @SerializedName("header_extensions")
    @Expose
    private List<Object> headerExtensions = null;
    @SerializedName("producer_signature")
    @Expose
    private String producerSignature;
    @SerializedName("transactions")
    @Expose
    private List<Object> transactions = null;
    @SerializedName("block_extensions")
    @Expose
    private List<Object> blockExtensions = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("block_num")
    @Expose
    private Integer blockNum;
    @SerializedName("ref_block_prefix")
    @Expose
    private Long refBlockPrefix;

    public ResBlock(Integer blockNum, Long refBlockPrefix, String timestamp) {
        super();
        this.blockNum = blockNum;
        this.refBlockPrefix = refBlockPrefix;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getTransactionMroot() {
        return transactionMroot;
    }

    public void setTransactionMroot(String transactionMroot) {
        this.transactionMroot = transactionMroot;
    }

    public String getActionMroot() {
        return actionMroot;
    }

    public void setActionMroot(String actionMroot) {
        this.actionMroot = actionMroot;
    }

    public Integer getScheduleVersion() {
        return scheduleVersion;
    }

    public void setScheduleVersion(Integer scheduleVersion) {
        this.scheduleVersion = scheduleVersion;
    }

    public Object getNewProducers() {
        return newProducers;
    }

    public void setNewProducers(Object newProducers) {
        this.newProducers = newProducers;
    }

    public List<Object> getHeaderExtensions() {
        return headerExtensions;
    }

    public void setHeaderExtensions(List<Object> headerExtensions) {
        this.headerExtensions = headerExtensions;
    }

    public String getProducerSignature() {
        return producerSignature;
    }

    public void setProducerSignature(String producerSignature) {
        this.producerSignature = producerSignature;
    }

    public List<Object> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Object> transactions) {
        this.transactions = transactions;
    }

    public List<Object> getBlockExtensions() {
        return blockExtensions;
    }

    public void setBlockExtensions(List<Object> blockExtensions) {
        this.blockExtensions = blockExtensions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Integer blockNum) {
        this.blockNum = blockNum;
    }

    public Long getRefBlockPrefix() {
        return refBlockPrefix;
    }

    public void setRefBlockPrefix(Long refBlockPrefix) {
        this.refBlockPrefix = refBlockPrefix;
    }
    */
}
