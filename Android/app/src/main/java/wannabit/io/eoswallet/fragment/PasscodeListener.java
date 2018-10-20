package wannabit.io.eoswallet.fragment;

public interface PasscodeListener {

    public abstract void onUserInsertKey(char input);

    public abstract void onUserDeleteKey();
}
