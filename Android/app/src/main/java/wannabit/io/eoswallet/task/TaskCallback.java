package wannabit.io.eoswallet.task;

public interface TaskCallback {

    public enum TaskType {
        InsertUser, CheckKey, Send, DeleteUser, BuyRamByte, BuyRam, SellRam, Delegate, Undelegate
    }

    public abstract void onStartedTask(TaskType type);

    public abstract void onSuccessTask(TaskType type, String result);

    public abstract void onFailTask(TaskType type, int errorCode);
}
