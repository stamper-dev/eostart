package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqBlock {
    @SerializedName("block_num_or_id")
    @Expose
    private Integer blockNumOrId;

    /**
     *
     * @param blockNumOrId
     */
    public ReqBlock(Integer blockNumOrId) {
        super();
        this.blockNumOrId = blockNumOrId;
    }

    public Integer getBlockNumOrId() {
        return blockNumOrId;
    }

    public void setBlockNumOrId(Integer blockNumOrId) {
        this.blockNumOrId = blockNumOrId;
    }

}
