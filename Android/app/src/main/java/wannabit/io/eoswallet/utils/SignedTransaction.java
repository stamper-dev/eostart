package wannabit.io.eoswallet.utils;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import wannabit.io.eoswallet.crypto.digest.Sha256;
import wannabit.io.eoswallet.crypto.ec.EcDsa;
import wannabit.io.eoswallet.crypto.ec.EcSignature;
import wannabit.io.eoswallet.crypto.ec.EosPrivateKey;
import wannabit.io.eoswallet.crypto.util.HexUtils;
import wannabit.io.eoswallet.type.EosByteWriter;
import wannabit.io.eoswallet.type.TypeChainId;

public class SignedTransaction extends Transaction {

    @Expose
    private List<String> signatures = null;

    @Expose
    private List<String> context_free_data = new ArrayList<>();


    public SignedTransaction(){
        super();
    }

    public SignedTransaction( SignedTransaction anotherTxn){
        super(anotherTxn);
        this.signatures = deepCopyOnlyContainer( anotherTxn.signatures );
        this.context_free_data = deepCopyOnlyContainer(anotherTxn.context_free_data);
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void putSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    public int getCtxFreeDataCount() {
        return ( context_free_data == null ) ? 0 : context_free_data.size();
    }

    public List<String> getCtxFreeData() {
        return context_free_data;
    }

    private byte[] getCfdHash() {
        if (context_free_data.size() <= 0 ) {
            return Sha256.ZERO_HASH.getBytes();
        }

        EosByteWriter writer = new EosByteWriter(255);

        writer.putVariableUInt( context_free_data.size());

        for ( String hexData : context_free_data) {
            byte[] rawData = HexUtils.toBytes( hexData);
//            EosType.java => writer interface
            writer.putVariableUInt( rawData.length);
            writer.putBytes( rawData);
        }

        return Sha256.from( writer.toBytes()).getBytes();
    }


    private Sha256 getDigestForSignature(TypeChainId chainId) {
        EosByteWriter writer = new EosByteWriter(255);

        // data layout to sign :
        // [ {chainId}, {Transaction( parent class )}, {hash of context_free_data} ]

        writer.putBytes(chainId.getBytes());
        pack( writer);
        writer.putBytes( getCfdHash());

        return Sha256.from(writer.toBytes());
    }

    private Sha256 getDigestForSignature(String chainId) {
        EosByteWriter writer = new EosByteWriter(255);

        // data layout to sign :
        // [ {chainId}, {Transaction( parent class )}, {hash of context_free_data} ]

        writer.putBytes(chainId.getBytes());
        pack( writer);
        writer.putBytes( getCfdHash());

        return Sha256.from(writer.toBytes());
    }

    public void sign(EosPrivateKey privateKey, TypeChainId chainId) {
        if ( null == this.signatures){
            this.signatures = new ArrayList<>();
        }

        EcSignature signature = EcDsa.sign(getDigestForSignature( chainId ), privateKey);
        this.signatures.add( signature.toString());
    }
}
