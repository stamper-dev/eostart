package wannabit.io.eoswallet.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

import wannabit.io.eoswallet.network.ResActions;
import wannabit.io.eoswallet.utils.WLog;

public class WBAction {

    @SerializedName("global_action_seq")
    public Long global_action_seq;

    @SerializedName("account_action_seq")
    public Long account_action_seq;

    @SerializedName("block_num")
    public Long block_num;

    @SerializedName("block_time")
    public String block_time;

    @SerializedName("action_trace")
    public ActionTrace action_trace;

    /*
    public String getFrom() {
        String result = "";
        if(getAction_trace().getAct() == null ||
                getAction_trace().getAct().getData() == null ||
                getAction_trace().getAct().getData().getFrom() == null) {
            return result;
        }
        result = getAction_trace().getAct().getData().getFrom();
        return result;
    }

    public String getTo() {
        String result = "";
        if(getAction_trace().getAct() == null ||
                getAction_trace().getAct().getData() == null ||
                getAction_trace().getAct().getData().getTo() == null) {
            return result;
        }
        result = getAction_trace().getAct().getData().getTo();
        return result;
    }

    public String getMemo() {
        String result = null;
        if(getAction_trace().getAct() == null ||
                getAction_trace().getAct().getData() == null ||
                getAction_trace().getAct().getData().getMemo() == null) {
            return result;
        }
        result = getAction_trace().getAct().getData().getMemo();
        return result;
    }

    public String getQuantity() {
        String result = "";
        if(getAction_trace().getAct() == null ||
                getAction_trace().getAct().getData() == null ||
                getAction_trace().getAct().getData().getQuantity() == null) {
            return result;
        }
        result = getAction_trace().getAct().getData().getQuantity();
        return result;
    }


    public String getAcionAccount() {
        String result = "";
        if(getAction_trace().getAct() == null ||
                getAction_trace().getAct().getAccount() == null) {
            return result;
        } else {
            result = getAction_trace().getAct().getAccount();
        }
        return result;
    }
    */


    public class ActionTrace {
        @SerializedName("receipt")
        public Receipt receipt;

        @SerializedName("act")
        public Act act;

        @SerializedName("elapsed")
        public Long elapsed;

        @SerializedName("cpu_usage")
        public Long cpu_usage;

        @SerializedName("console")
        public String console;

        @SerializedName("total_cpu_usage")
        public Long total_cpu_usage;

        @SerializedName("trx_id")
        public String trx_id;

//        @SerializedName("inline_traces")
//        ArrayList<ActionTrace> inline_traces;

        public class Receipt {
            @SerializedName("receiver")
            public String receiver;

            @SerializedName("act_digest")
            public String act_digest;

            @SerializedName("global_sequence")
            public Long global_sequence;

            @SerializedName("recv_sequence")
            public Long recv_sequence;

            @SerializedName("auth_sequence")
            public ArrayList<JsonArray> auth_sequence;

            @SerializedName("code_sequence")
            public Long code_sequence;

            @SerializedName("abi_sequence")
            public Long abi_sequence;

        }

        public class Act {
            @SerializedName("account")
            public String account;

            @SerializedName("name")
            public String name;

            @SerializedName("authorization")
            public ArrayList<Author> authorization;

            @SerializedName("data")
            @Expose
            public Object data;

            @SerializedName("hex_data")
            public String hex_data;


            public Data getData() {
                Data result = null;
                try {
                    result = new Gson().fromJson(new Gson().toJson(data), Data.class);
                } catch (Exception e) {
                } finally {
                    return result;
                }
            }

            public class Author {
                @SerializedName("actor")
                String actor;

                @SerializedName("permission")
                String permission;
            }

            public class Data {
                @SerializedName("voter")
                public String voter;

                @SerializedName("proxy")
                public String proxy;

                @SerializedName("producers")
                public ArrayList<String> producers;

                @SerializedName("from")
                public String from;

                @SerializedName("to")
                public String to;

                @SerializedName("quantity")
                public String quantity;

                @SerializedName("memo")
                public String memo;

                @SerializedName("receiver")
                public String receiver;

                @SerializedName("unstake_net_quantity")
                public String unstake_net_quantity;

                @SerializedName("unstake_cpu_quantity")
                public String unstake_cpu_quantity;

//                @SerializedName("owner")
//                String owner;

                @SerializedName("transfer")
                Long transfer;
            }

        }
    }
}

/*
public class WBAction {

    @SerializedName("global_action_seq")
    Long global_action_seq;

    @SerializedName("account_action_seq")
    Long account_action_seq;

    @SerializedName("block_num")
    Long block_num;

    @SerializedName("block_time")
    String block_time;

    @SerializedName("action_trace")
    ActionTrace action_trace;

    public Long getGlobal_action_seq() {
        return global_action_seq;
    }

    public void setGlobal_action_seq(Long global_action_seq) {
        this.global_action_seq = global_action_seq;
    }

    public Long getAccount_action_seq() {
        return account_action_seq;
    }

    public void setAccount_action_seq(Long account_action_seq) {
        this.account_action_seq = account_action_seq;
    }

    public Long getBlock_num() {
        return block_num;
    }

    public void setBlock_num(Long block_num) {
        this.block_num = block_num;
    }

    public String getBlock_time() {
        return block_time;
    }

    public void setBlock_time(String block_time) {
        this.block_time = block_time;
    }

    public ActionTrace getAction_trace() {
        return action_trace;
    }

    public void setAction_trace(ActionTrace action_trace) {
        this.action_trace = action_trace;
    }



    public String getFrom() {
        String result = "";
        if(getAction_trace().getAct() == null ||
                getAction_trace().getAct().getData() == null ||
                getAction_trace().getAct().getData().getFrom() == null) {
            return result;
        }
        result = getAction_trace().getAct().getData().getFrom();
        return result;
    }

    public String getTo() {
        String result = "";
        if(getAction_trace().getAct() == null ||
                getAction_trace().getAct().getData() == null ||
                getAction_trace().getAct().getData().getTo() == null) {
            return result;
        }
        result = getAction_trace().getAct().getData().getTo();
        return result;
    }

    public String getMemo() {
        String result = null;
        if(getAction_trace().getAct() == null ||
                getAction_trace().getAct().getData() == null ||
                getAction_trace().getAct().getData().getMemo() == null) {
            return result;
        }
        result = getAction_trace().getAct().getData().getMemo();
        return result;
    }

    public String getQuantity() {
        String result = "";
        if(getAction_trace().getAct() == null ||
                getAction_trace().getAct().getData() == null ||
                getAction_trace().getAct().getData().getQuantity() == null) {
            return result;
        }
        result = getAction_trace().getAct().getData().getQuantity();
        return result;
    }


    public String getAcionAccount() {
        String result = "";
        if(getAction_trace().getAct() == null ||
                getAction_trace().getAct().getAccount() == null) {
            return result;
        } else {
            result = getAction_trace().getAct().getAccount();
        }
        return result;
    }


    public class ActionTrace {
        @SerializedName("receipt")
        Receipt receipt;

        @SerializedName("act")
        Act act;

        @SerializedName("elapsed")
        Long elapsed;

        @SerializedName("cpu_usage")
        Long cpu_usage;

        @SerializedName("console")
        String console;

        @SerializedName("total_cpu_usage")
        Long total_cpu_usage;

        @SerializedName("trx_id")
        String trx_id;

//        @SerializedName("inline_traces")
//        ArrayList<ActionTrace> inline_traces;

        public Receipt getReceipt() {
            return receipt;
        }

        public void setReceipt(Receipt receipt) {
            this.receipt = receipt;
        }

        public Act getAct() {
            return act;
        }

        public void setAct(Act act) {
            this.act = act;
        }

        public Long getElapsed() {
            return elapsed;
        }

        public void setElapsed(Long elapsed) {
            this.elapsed = elapsed;
        }

        public Long getCpu_usage() {
            return cpu_usage;
        }

        public void setCpu_usage(Long cpu_usage) {
            this.cpu_usage = cpu_usage;
        }

        public String getConsole() {
            return console;
        }

        public void setConsole(String console) {
            this.console = console;
        }

        public Long getTotal_cpu_usage() {
            return total_cpu_usage;
        }

        public void setTotal_cpu_usage(Long total_cpu_usage) {
            this.total_cpu_usage = total_cpu_usage;
        }

        public String getTrx_id() {
            return trx_id;
        }

        public void setTrx_id(String trx_id) {
            this.trx_id = trx_id;
        }

//        public ArrayList<ActionTrace> getInline_traces() {
//            return inline_traces;
//        }
//
//        public void setInline_traces(ArrayList<ActionTrace> inline_traces) {
//            this.inline_traces = inline_traces;
//        }

        public class Receipt {
            @SerializedName("receiver")
            String receiver;

            @SerializedName("act_digest")
            String act_digest;

            @SerializedName("global_sequence")
            Long global_sequence;

            @SerializedName("recv_sequence")
            Long recv_sequence;

            @SerializedName("auth_sequence")
            ArrayList<JsonArray> auth_sequence;

            @SerializedName("code_sequence")
            Long code_sequence;

            @SerializedName("abi_sequence")
            Long abi_sequence;

            public String getReceiver() {
                return receiver;
            }

            public void setReceiver(String receiver) {
                this.receiver = receiver;
            }

            public String getAct_digest() {
                return act_digest;
            }

            public void setAct_digest(String act_digest) {
                this.act_digest = act_digest;
            }

            public Long getGlobal_sequence() {
                return global_sequence;
            }

            public void setGlobal_sequence(Long global_sequence) {
                this.global_sequence = global_sequence;
            }

            public Long getRecv_sequence() {
                return recv_sequence;
            }

            public void setRecv_sequence(Long recv_sequence) {
                this.recv_sequence = recv_sequence;
            }

            public ArrayList<JsonArray> getAuth_sequence() {
                return auth_sequence;
            }

            public void setAuth_sequence(ArrayList<JsonArray> auth_sequence) {
                this.auth_sequence = auth_sequence;
            }

            public Long getCode_sequence() {
                return code_sequence;
            }

            public void setCode_sequence(Long code_sequence) {
                this.code_sequence = code_sequence;
            }

            public Long getAbi_sequence() {
                return abi_sequence;
            }

            public void setAbi_sequence(Long abi_sequence) {
                this.abi_sequence = abi_sequence;
            }
        }

        public class Act {
            @SerializedName("account")
            String account;

            @SerializedName("name")
            String name;

            @SerializedName("authorization")
            ArrayList<Author> authorization;

            @SerializedName("data")
            @Expose
            Object data;

            @SerializedName("hex_data")
            String hex_data;

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

            public ArrayList<Author> getAuthorization() {
                return authorization;
            }

            public void setAuthorization(ArrayList<Author> authorization) {
                this.authorization = authorization;
            }

            public Data getData() {
                Data result = null;

                if(data instanceof String) {
                    return result;
                } else if (data instanceof Object) {
                    try {
                        result = new Gson().fromJson(new Gson().toJson(data), Data.class);
                    } catch (Exception e) {
                    }
                    return result;
                } else {

                }
                return result;
            }

            public void setData(Data data) {
                this.data = data;
            }

            public String getHex_data() {
                return hex_data;
            }

            public void setHex_data(String hex_data) {
                this.hex_data = hex_data;
            }

            public class Author {
                @SerializedName("actor")
                String actor;

                @SerializedName("permission")
                String permission;

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
                @SerializedName("voter")
                String voter;

                @SerializedName("proxy")
                String proxy;

                @SerializedName("producers")
                ArrayList<String> producers;

                @SerializedName("from")
                String from;

                @SerializedName("to")
                String to;

                @SerializedName("quantity")
                String quantity;

                @SerializedName("memo")
                String memo;

                @SerializedName("receiver")
                String receiver;

                @SerializedName("unstake_net_quantity")
                String unstake_net_quantity;

                @SerializedName("unstake_cpu_quantity")
                String unstake_cpu_quantity;

//                @SerializedName("owner")
//                String owner;

                @SerializedName("transfer")
                Long transfer;

                public String getVoter() {
                    return voter;
                }

                public void setVoter(String voter) {
                    this.voter = voter;
                }

                public String getProxy() {
                    return proxy;
                }

                public void setProxy(String proxy) {
                    this.proxy = proxy;
                }

                public ArrayList<String> getProducers() {
                    return producers;
                }

                public void setProducers(ArrayList<String> producers) {
                    this.producers = producers;
                }

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

                public String getReceiver() {
                    return receiver;
                }

                public void setReceiver(String receiver) {
                    this.receiver = receiver;
                }

                public String getUnstake_net_quantity() {
                    return unstake_net_quantity;
                }

                public void setUnstake_net_quantity(String unstake_net_quantity) {
                    this.unstake_net_quantity = unstake_net_quantity;
                }

                public String getUnstake_cpu_quantity() {
                    return unstake_cpu_quantity;
                }

                public void setUnstake_cpu_quantity(String unstake_cpu_quantity) {
                    this.unstake_cpu_quantity = unstake_cpu_quantity;
                }

//                public String getOwner() {
//                    return owner;
//                }
//
//                public void setOwner(String owner) {
//                    this.owner = owner;
//                }

                public Long getTransfer() {
                    return transfer;
                }

                public void setTransfer(Long transfer) {
                    this.transfer = transfer;
                }
            }

        }
    }
}
*/
