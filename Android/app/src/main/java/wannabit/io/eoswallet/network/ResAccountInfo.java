package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class ResAccountInfo {
    @SerializedName("account_name")
    String account_name;

    @SerializedName("head_block_num")
    String head_block_num;

    @SerializedName("core_liquid_balance")
    String core_liquid_balance;

    @SerializedName("ram_quota")
    Double ram_quota;

    @SerializedName("ram_usage")
    Double ram_usage;

    @SerializedName("cpu_limit")
    ResourceLimit cpu_limit;

    @SerializedName("cpu_weight")
    Double cpu_weight;

    @SerializedName("net_limit")
    ResourceLimit net_limit;

    @SerializedName("net_weight")
    Double net_weight;

    @SerializedName("total_resources")
    TotalResource total_resources;

    @SerializedName("self_delegated_bandwidth")
    SelfDelegatedBandwith self_delegated_bandwidth;

    @SerializedName("refund_request")
    RefundRequest refund_request;

    @SerializedName("voter_info")
    VoterInfo voter_info;


    public Double getTotalAmout() {
        Double result = 0d;
        result = getTotalStakedAmout() + getTotalUnstakedAmout() + getTotalRefundAmout();
        return result;
    }

    public Double getTotalStakedAmout() {
        Double result = 0d;
        if(getVoter_info() != null && getVoter_info().getStaked() > 0 ) {
            result = getVoter_info().getStaked() / 10000d;
        }
//        if(getTotal_resources() != null &&
//                getAccount_name().equals(getTotal_resources().getOwner())) {
//            Double cpuValue = Double.parseDouble(getTotal_resources().getCpu_weight().replace("EOS", "").replace(" ", ""));
//            Double netValue = Double.parseDouble(getTotal_resources().getNet_weight().replace("EOS", "").replace(" ", ""));
//        }
        return result;
    }

    public Double getTotalUnstakedAmout() {
        Double result = 0d;
        if(getCore_liquid_balance() != null) {
            try {
                result = Double.parseDouble(getCore_liquid_balance().replace("EOS", "").replace(" ", ""));
            } catch (Exception e) {
                WLog.w("getTotalUnstakedAmout Parsing Error");
            }
        }
        return result;
    }

    public Double getTotalRefundAmout() {
        Double result = 0d;
        if(getRefund_request() != null) {
            Double netRefundAmout = 0d;
            Double cpuRefundAmout = 0d;
            if(getRefund_request().getNet_amount() != null) {
                try {
                    netRefundAmout = Double.parseDouble(getRefund_request().getNet_amount().replace("EOS", ""));
                    cpuRefundAmout = Double.parseDouble(getRefund_request().getCpu_amount().replace("EOS", ""));
                } catch (Exception e) {
                    WLog.w("getTotalRefundAmout Parsing Error");
                }
            }
            result  =  netRefundAmout + cpuRefundAmout;
        }
        return result;
    }

    public String getRamInfo() {
        return  WUtil.FormatByte(getRam_usage()) + " / " + WUtil.FormatByte(getRam_quota());
    }

    public int getRamProgress() {
        if(getRam_quota() == 0 || getRam_usage() == 0) {
            return 0;
        } else {
             return (int) (getRam_usage() / getRam_quota() * 100);
        }
    }


    public String getCpuInfo() {
        if(getCpu_limit() != null) {
            return WUtil.FormatTime(getCpu_limit().getUsed()) + "/" + WUtil.FormatTime(getCpu_limit().getMax());
        }
        return "";
    }

    public String getCpuAmount() {
        if(getCpu_limit() != null) {
            return "(" + WUtil.EOSAmoutFormat(Double.parseDouble(getTotal_resources().getCpu_weight().replace("EOS", ""))) + ")";
        }
        return "";

    }

    public int getCpuProgress() {
        if(getCpu_limit() == null || getCpu_limit().getUsed() == 0 || getCpu_limit().getMax() == 0 ) {
            return 0;
        } else {
            return (int) (getCpu_limit().getUsed() / getCpu_limit().getMax() * 100);
        }
    }

    public String getNetInfo() {
        if(getNet_limit() != null) {
            return WUtil.FormatByte(getNet_limit().getUsed()) + " / " + WUtil.FormatByte(getNet_limit().getMax());
        }
        return "";
    }

    public String getNetAmount() {
        if(getNet_limit() != null) {
            return "(" + WUtil.EOSAmoutFormat(Double.parseDouble(getTotal_resources().getNet_weight().replace("EOS", ""))) + ")";
        }
        return "";
    }

    public int getNetProgress() {
        if(getNet_limit() == null || getNet_limit().getUsed() == 0 || getNet_limit().getMax() == 0 ) {
            return 0;
        } else {
            return (int) (getNet_limit().getUsed() / getNet_limit().getMax() * 100);
        }
    }

    public Double getMyResourceRam() {
        if(getTotal_resources() != null
            && getTotal_resources().getRam_bytes() != null) {
            return getTotal_resources().getRam_bytes();
        }
        return 0d;
    }

    public String getMyResourceCpuAmount() {
        if(self_delegated_bandwidth != null
                && self_delegated_bandwidth.getCpu_weight() != null) {
            return self_delegated_bandwidth.getCpu_weight().replace("EOS","").replace(" ", "");
        }
        return "0";
    }

    public String getMyResourceNetAmount() {
        if(self_delegated_bandwidth != null
                && self_delegated_bandwidth.getNet_weight() != null) {
            return self_delegated_bandwidth.getNet_weight().replace("EOS","").replace(" ", "");
        }
        return "0";
    }

    public Double getRentedResourceRam() {
        return getRam_quota() - getMyResourceRam();
    }




    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getHead_block_num() {
        return head_block_num;
    }

    public void setHead_block_num(String head_block_num) {
        this.head_block_num = head_block_num;
    }

    public String getCore_liquid_balance() {
        return core_liquid_balance;
    }

    public void setCore_liquid_balance(String core_liquid_balance) {
        this.core_liquid_balance = core_liquid_balance;
    }

    public Double getRam_quota() {
        return ram_quota;
    }

    public void setRam_quota(Double ram_quota) {
        this.ram_quota = ram_quota;
    }

    public Double getRam_usage() {
        return ram_usage;
    }

    public void setRam_usage(Double ram_usage) {
        this.ram_usage = ram_usage;
    }

    public ResourceLimit getCpu_limit() {
        return cpu_limit;
    }

    public void setCpu_limit(ResourceLimit cpu_limit) {
        this.cpu_limit = cpu_limit;
    }

    public Double getCpu_weight() {
        return cpu_weight;
    }

    public void setCpu_weight(Double cpu_weight) {
        this.cpu_weight = cpu_weight;
    }

    public ResourceLimit getNet_limit() {
        return net_limit;
    }

    public void setNet_limit(ResourceLimit net_limit) {
        this.net_limit = net_limit;
    }

    public Double getNet_weight() {
        return net_weight;
    }

    public void setNet_weight(Double net_weight) {
        this.net_weight = net_weight;
    }

    public TotalResource getTotal_resources() {
        return total_resources;
    }

    public void setTotal_resources(TotalResource total_resources) {
        this.total_resources = total_resources;
    }

    public SelfDelegatedBandwith getSelf_delegated_bandwidth() {
        return self_delegated_bandwidth;
    }

    public void setSelf_delegated_bandwidth(SelfDelegatedBandwith self_delegated_bandwidth) {
        this.self_delegated_bandwidth = self_delegated_bandwidth;
    }

    public RefundRequest getRefund_request() {
        return refund_request;
    }

    public void setRefund_request(RefundRequest refund_request) {
        this.refund_request = refund_request;
    }

    public VoterInfo getVoter_info() {
        return voter_info;
    }

    public void setVoter_info(VoterInfo voter_info) {
        this.voter_info = voter_info;
    }

    public class ResourceLimit {
        @SerializedName("used")
        Double used;

        @SerializedName("available")
        Double available;

        @SerializedName("max")
        Double max;

        public Double getUsed() {
            return used;
        }

        public void setUsed(Double used) {
            this.used = used;
        }

        public Double getAvailable() {
            return available;
        }

        public void setAvailable(Double available) {
            this.available = available;
        }

        public Double getMax() {
            return max;
        }

        public void setMax(Double max) {
            this.max = max;
        }
    }

    public class TotalResource {
        @SerializedName("owner")
        String owner;

        @SerializedName("net_weight")
        String net_weight;

        @SerializedName("cpu_weight")
        String cpu_weight;

        @SerializedName("ram_bytes")
        Double ram_bytes;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getNet_weight() {
            return net_weight;
        }

        public void setNet_weight(String net_weight) {
            this.net_weight = net_weight;
        }

        public String getCpu_weight() {
            return cpu_weight;
        }

        public void setCpu_weight(String cpu_weight) {
            this.cpu_weight = cpu_weight;
        }

        public Double getRam_bytes() {
            return ram_bytes;
        }

        public void setRam_bytes(Double ram_bytes) {
            this.ram_bytes = ram_bytes;
        }
    }

    public class SelfDelegatedBandwith {
        @SerializedName("from")
        String from;

        @SerializedName("to")
        String to;

        @SerializedName("net_weight")
        String net_weight;

        @SerializedName("cpu_weight")
        String cpu_weight;


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

        public String getNet_weight() {
            return net_weight;
        }

        public void setNet_weight(String net_weight) {
            this.net_weight = net_weight;
        }

        public String getCpu_weight() {
            return cpu_weight;
        }

        public void setCpu_weight(String cpu_weight) {
            this.cpu_weight = cpu_weight;
        }
    }

    public class RefundRequest {
        @SerializedName("owner")
        String owner;

        @SerializedName("request_time")
        String request_time;

        @SerializedName("net_amount")
        String net_amount;

        @SerializedName("cpu_amount")
        String cpu_amount;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getRequest_time() {
            return request_time;
        }

        public void setRequest_time(String request_time) {
            this.request_time = request_time;
        }

        public String getNet_amount() {
            return net_amount;
        }

        public void setNet_amount(String net_amount) {
            this.net_amount = net_amount;
        }

        public String getCpu_amount() {
            return cpu_amount;
        }

        public void setCpu_amount(String cpu_amount) {
            this.cpu_amount = cpu_amount;
        }
    }

    public class VoterInfo {
        @SerializedName("owner")
        String owner;

        @SerializedName("proxy")
        String proxy;

        @SerializedName("staked")
        Double staked;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getProxy() {
            return proxy;
        }

        public void setProxy(String proxy) {
            this.proxy = proxy;
        }

        public Double getStaked() {
            return staked;
        }

        public void setStaked(Double staked) {
            this.staked = staked;
        }
    }

}
