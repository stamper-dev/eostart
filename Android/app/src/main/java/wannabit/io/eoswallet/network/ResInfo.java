package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResInfo {
    @SerializedName("server_version")
    @Expose
    private String serverVersion;
    @SerializedName("chain_id")
    @Expose
    private String chainId;
    @SerializedName("head_block_num")
    @Expose
    private Integer headBlockNum;
    @SerializedName("last_irreversible_block_num")
    @Expose
    private Integer lastIrreversibleBlockNum;
    @SerializedName("last_irreversible_block_id")
    @Expose
    private String lastIrreversibleBlockId;
    @SerializedName("head_block_id")
    @Expose
    private String headBlockId;
    @SerializedName("head_block_time")
    @Expose
    private String headBlockTime;
    @SerializedName("head_block_producer")
    @Expose
    private String headBlockProducer;
    @SerializedName("virtual_block_cpu_limit")
    @Expose
    private Integer virtualBlockCpuLimit;
    @SerializedName("virtual_block_net_limit")
    @Expose
    private Integer virtualBlockNetLimit;
    @SerializedName("block_cpu_limit")
    @Expose
    private Integer blockCpuLimit;
    @SerializedName("block_net_limit")
    @Expose
    private Integer blockNetLimit;

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public Integer getHeadBlockNum() {
        return headBlockNum;
    }

    public void setHeadBlockNum(Integer headBlockNum) {
        this.headBlockNum = headBlockNum;
    }

    public Integer getLastIrreversibleBlockNum() {
        return lastIrreversibleBlockNum;
    }

    public void setLastIrreversibleBlockNum(Integer lastIrreversibleBlockNum) {
        this.lastIrreversibleBlockNum = lastIrreversibleBlockNum;
    }

    public String getLastIrreversibleBlockId() {
        return lastIrreversibleBlockId;
    }

    public void setLastIrreversibleBlockId(String lastIrreversibleBlockId) {
        this.lastIrreversibleBlockId = lastIrreversibleBlockId;
    }

    public String getHeadBlockId() {
        return headBlockId;
    }

    public void setHeadBlockId(String headBlockId) {
        this.headBlockId = headBlockId;
    }

    public String getHeadBlockTime() {
        return headBlockTime;
    }

    public void setHeadBlockTime(String headBlockTime) {
        this.headBlockTime = headBlockTime;
    }

    public String getHeadBlockProducer() {
        return headBlockProducer;
    }

    public void setHeadBlockProducer(String headBlockProducer) {
        this.headBlockProducer = headBlockProducer;
    }

    public Integer getVirtualBlockCpuLimit() {
        return virtualBlockCpuLimit;
    }

    public void setVirtualBlockCpuLimit(Integer virtualBlockCpuLimit) {
        this.virtualBlockCpuLimit = virtualBlockCpuLimit;
    }

    public Integer getVirtualBlockNetLimit() {
        return virtualBlockNetLimit;
    }

    public void setVirtualBlockNetLimit(Integer virtualBlockNetLimit) {
        this.virtualBlockNetLimit = virtualBlockNetLimit;
    }

    public Integer getBlockCpuLimit() {
        return blockCpuLimit;
    }

    public void setBlockCpuLimit(Integer blockCpuLimit) {
        this.blockCpuLimit = blockCpuLimit;
    }

    public Integer getBlockNetLimit() {
        return blockNetLimit;
    }

    public void setBlockNetLimit(Integer blockNetLimit) {
        this.blockNetLimit = blockNetLimit;
    }
}
