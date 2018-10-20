package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

import wannabit.io.eoswallet.model.WBAction;

public class ResActions {

    @SerializedName("last_irreversible_block")
    Long last_irreversible_block;

    @SerializedName("time_limit_exceeded_error")
    Boolean time_limit_exceeded_error;

    @SerializedName("actions")
    ArrayList<WBAction> actions;

    public Long getLast_irreversible_block() {
        return last_irreversible_block;
    }

    public void setLast_irreversible_block(Long last_irreversible_block) {
        this.last_irreversible_block = last_irreversible_block;
    }

    public Boolean getTime_limit_exceeded_error() {
        return time_limit_exceeded_error;
    }

    public void setTime_limit_exceeded_error(Boolean time_limit_exceeded_error) {
        this.time_limit_exceeded_error = time_limit_exceeded_error;
    }

    public ArrayList<WBAction> getActions() {
        return actions;
    }

    public void setActions(ArrayList<WBAction> actions) {
        this.actions = actions;
    }
}
