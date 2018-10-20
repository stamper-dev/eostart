package wannabit.io.eoswallet.utils;

import com.google.gson.annotations.Expose;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import wannabit.io.eoswallet.crypto.util.BitUtils;
import wannabit.io.eoswallet.crypto.util.HexUtils;
import wannabit.io.eoswallet.type.EosType;

public class TransactionHeader implements EosType.Packer {
    @Expose
    private String expiration;

    @Expose
    private int ref_block_num = 0; // uint16_t

    @Expose
    private long ref_block_prefix= 0;// uint32_t

    @Expose
    private long max_net_usage_words; // fc::unsigned_int

    @Expose
    private long max_cpu_usage_ms;    // fc::unsigned_int

    @Expose
    private long delay_sec;     // fc::unsigned_int

    public TransactionHeader(){
    }

    public TransactionHeader( TransactionHeader other ){
        this.expiration = other.expiration;
        this.ref_block_num = other.ref_block_num;
        this.ref_block_prefix = other.ref_block_prefix;
        this.max_net_usage_words = other.max_net_usage_words;
        this.max_cpu_usage_ms = other.max_cpu_usage_ms;
        this.delay_sec = other.delay_sec;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public void setReferenceBlock( int block_num, long block_prefix  ) {
        ref_block_num = block_num;
        ref_block_prefix = block_prefix;
    }

    public int getRefBlockNum() {
        return ref_block_num;
    }
    public long getRefBlockPrefix() {
        return ref_block_prefix;
    }


    private Date getExpirationAsDate(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.parse( dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public void putNetUsageWords(long netUsage) {
        this.max_net_usage_words = netUsage;
    }

    public void putKcpuUsage(long kCpuUsage) {
        this.max_cpu_usage_ms = kCpuUsage;
    }

    @Override
    public void pack(EosType.Writer writer) {
        writer.putIntLE( (int)(getExpirationAsDate(expiration).getTime() / 1000) ); // ms -> sec

        writer.putShortLE( (short)(ref_block_num  & 0xFFFF) );  // uint16
        writer.putIntLE( (int)( ref_block_prefix & 0xFFFFFFFF) );// uint32

        // fc::unsigned_int
        writer.putVariableUInt(max_net_usage_words);
        writer.putVariableUInt( max_cpu_usage_ms);
        writer.putVariableUInt( delay_sec);
    }
}
