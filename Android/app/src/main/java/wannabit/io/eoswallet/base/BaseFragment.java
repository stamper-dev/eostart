package wannabit.io.eoswallet.base;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    public BaseApplication getBaseApplication() {
        return getBaseActivity().getBaseApplication();
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity)getActivity();
    }

    public BaseDao getBaseDao() {
        if (getBaseActivity() != null && getBaseActivity().getBaseDao() != null) {
            return getBaseActivity().getBaseDao();

        }  else {
            return getBaseApplication().getBaseDao();

        }
    }

    public BaseApplication.AppStatus getAppStatus() {
        return getBaseApplication().getAppStatus();
    }


    public void onRefreshByMainTab(boolean deep) { }

    public void willBeDisplayed() { }

    public void willBeHidden() { }


}
