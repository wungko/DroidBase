package com.droid.base.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.droid.base.BaseApplication;
import com.droid.base.utils.log.L;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.CallbackListener;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by wungko on 16/1/14.
 */
public abstract class BaseFragment extends Fragment implements DialogInterface.OnDismissListener {

    /**
     * fragment 初始化
     *
     * @param arguments
     */
    protected abstract void initFragment(Bundle arguments);

    /**
     * 页面跟布局
     **/
    private LinearLayout contentContainer;
    /**
     * 布局层级
     **/
    private final int VIEW_LOADING_INDEX = 0, VIEW_CONTENT_INDEX = 1, VIEW_EMPTY_INDEX = 2, VIEW_BERROR_INDEX = 3, VIEW_HERROR_INDEX = 4;//布局层级
    /**
     * view 注解
     **/
    boolean mIs1RequestData = true; //首次加载数据
    /**
     * 请求队列
     **/
    private Stack<Call> mCalls = new Stack<>();

    /**
     * 返回具体的View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createLayout(inflater);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragment(getArguments());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeAllCalls();
    }

    protected abstract int getToolbarLayout();

    /**
     * 内容布局ID
     *
     * @return
     */
    protected abstract int getLayout();

    /**
     * 框架内封装请求网络方法 网络异常,服务器异常,空数据 点击时会调用此方法
     */
    protected abstract void requestData();

    /**
     * 请求返回  可以在这里取消listView的下拉 或者 加载更多的复位操作
     */
    protected abstract void requestDone();

    /**
     * 页面loading
     */
    protected void showLoading() {
        layoutLoading();
    }

    /**
     * 展示内容
     */
    protected void showContent() {
        layoutContent();
    }

    /**
     * 空布局展示
     */
    protected void showEmpty() {
        layoutEmpty();
    }

    /**
     * 业务报错
     */
    protected void showBizError() {
        layoutBizError();
    }

    /**
     * http 报错
     */
    protected void showHttpError() {
        layoutHttpError();
    }

    /**
     * 布局初始化
     *
     * @param inflater
     */
    private LinearLayout createLayout(LayoutInflater inflater) {
        LinearLayout root = new LinearLayout(getActivity());
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        contentContainer = new LinearLayout(getActivity());
        if (getToolbarLayout() != 0) {
            inflater.inflate(getToolbarLayout(), root, true);
            root.addView(contentContainer, layoutParams);
        } else {
            L.i("",BaseFragment.this.getClass().getSimpleName() + " has no toolbar layout");
        }

        // 进度条
        View loading = inflater.inflate(BaseApplication.getInstance().getLoadingLayout(), null, false);
        contentContainer.addView(loading, VIEW_LOADING_INDEX, layoutParams);
        loading.setVisibility(GONE);

        // 内容
        View content = inflater.inflate(getLayout(), null, false);
        contentContainer.addView(content, VIEW_CONTENT_INDEX, layoutParams);

        // 空布局

        // 空布局
        int emptyLayout = getEmptyLayout();
        if (emptyLayout == 0) {
            emptyLayout = BaseApplication.getInstance().getEmptyLayout();
        }
        View empty = inflater.inflate(emptyLayout, null, false);
        contentContainer.addView(empty, VIEW_EMPTY_INDEX, layoutParams);
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIs1RequestData = true;
                requestData();
            }
        });
        empty.setVisibility(GONE);

        // 业务错误布局
        View berror = inflater.inflate(BaseApplication.getInstance().getBErrorLayout(), null, false);
        contentContainer.addView(berror, VIEW_BERROR_INDEX, layoutParams);
        berror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIs1RequestData = true;
                requestData();
            }
        });
        berror.setVisibility(GONE);

        // 网络错误布局
        View herror = inflater.inflate(BaseApplication.getInstance().getHttpErrorLayout(), null, false);
        contentContainer.addView(herror, VIEW_HERROR_INDEX, layoutParams);
        herror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIs1RequestData = true;
                requestData();
            }
        });
        herror.setVisibility(GONE);

        if (getToolbarLayout() == 0) {
            return contentContainer;
        }
        return root;
    }

    // 功能
    protected void layoutContent() {
        changeShowAnimation(VIEW_LOADING_INDEX, GONE);
        changeShowAnimation(VIEW_EMPTY_INDEX, GONE);
        changeShowAnimation(VIEW_BERROR_INDEX, GONE);
        changeShowAnimation(VIEW_HERROR_INDEX, GONE);
        changeShowAnimation(VIEW_CONTENT_INDEX, VISIBLE);
    }

    private void layoutLoading() {
        changeShowAnimation(VIEW_EMPTY_INDEX, GONE);
        changeShowAnimation(VIEW_BERROR_INDEX, GONE);
        changeShowAnimation(VIEW_HERROR_INDEX, GONE);
        changeShowAnimation(VIEW_CONTENT_INDEX, GONE);
        changeShowAnimation(VIEW_LOADING_INDEX, VISIBLE);
    }

    private void layoutEmpty() {
        changeShowAnimation(VIEW_BERROR_INDEX, GONE);
        changeShowAnimation(VIEW_HERROR_INDEX, GONE);
        changeShowAnimation(VIEW_CONTENT_INDEX, GONE);
        changeShowAnimation(VIEW_LOADING_INDEX, GONE);
        changeShowAnimation(VIEW_EMPTY_INDEX, VISIBLE);
    }

    private void layoutBizError() {
        changeShowAnimation(VIEW_EMPTY_INDEX, GONE);
        changeShowAnimation(VIEW_HERROR_INDEX, GONE);
        changeShowAnimation(VIEW_CONTENT_INDEX, GONE);
        changeShowAnimation(VIEW_LOADING_INDEX, GONE);
        changeShowAnimation(VIEW_BERROR_INDEX, VISIBLE);
    }

    private void layoutHttpError() {
        changeShowAnimation(VIEW_EMPTY_INDEX, GONE);
        changeShowAnimation(VIEW_BERROR_INDEX, GONE);
        changeShowAnimation(VIEW_CONTENT_INDEX, GONE);
        changeShowAnimation(VIEW_LOADING_INDEX, GONE);
        changeShowAnimation(VIEW_HERROR_INDEX, VISIBLE);
    }


    /**
     * 空布局 可重写此方法
     */
    protected int getEmptyLayout() {
        return 0;
    }

    /**
     * 动画切换
     *
     * @param visible
     */
    private void changeShowAnimation(int childrenIndex, int visible) {
        if(contentContainer == null){
            L.e("页面布局 还未初始化完成");
            return;
        }
        int animId = 0;
        View view = contentContainer.getChildAt(childrenIndex);

        switch (visible) {
            case View.VISIBLE:
                if (view.getVisibility() == View.VISIBLE) {
                    return;
                }
                view.setVisibility(View.VISIBLE);
                animId = android.R.anim.fade_in;
                break;
            case View.GONE:
                if (view.getVisibility() == View.GONE) {
                    return;
                }
                view.setVisibility(View.GONE);

                animId = android.R.anim.fade_out;
                break;
        }
        if (getActivity() == null) {
            return;
        }
        Animation anim = AnimationUtils.loadAnimation(BaseApplication.getInstance(), animId);
        anim.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        view.startAnimation(anim);
    }

    /**
     * t 弹出提示
     *
     * @param msg
     */
    protected void t(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 没有任何提示的网络请求
     *
     * @param call
     * @param callback
     * @param <T>
     * @return
     */
    public <T> T requestNoLoading(final retrofit2.Call<T> call, Callback<T> callback) {
        mCalls.add(call);
        callback.setCallbackListener(new CallbackListener() {
            @Override
            public void onOk(Response response) {
                requestDone();
                mCalls.remove(call);
            }

            @Override
            public void onError(Throwable t) {
                requestDone();
                mCalls.remove(call);
            }
        });
        call.enqueue(callback);
        return null;
    }

    /**
     * 弹窗loading 和 整页loading 自动切换
     *
     * @param call
     * @param callback
     * @param tipMsg
     * @param <T>
     * @return
     */
    public <T> T request(final retrofit2.Call<T> call, Callback<T> callback, String tipMsg) {
        if (mIs1RequestData) {
            showLoading();
        } else {
            loadingShow(tipMsg);
        }
        mCalls.add(call);
        callback.setCallbackListener(new CallbackListener() {

            @Override
            public void onOk(Response response) {
                requestDone();
                mCalls.remove(call);
                if (mIs1RequestData) {
                    showContent();
                    mIs1RequestData = false;
                } else {
                    loadingClose();
                }
            }

            @Override
            public void onError(Throwable t) {
                requestDone();
                mCalls.remove(call);
                loadingClose();
                L.e(t.getMessage());
                if (t instanceof ConnectException) {
                    if (mIs1RequestData) {
                        showHttpError();
                    } else {
                        t(getHttpError());
                    }
                } else if (t instanceof MalformedJsonException) {
                    if (mIs1RequestData) {
                        showBizError();
                    } else {
                        t(getServerError());
                    }
                } else if(t instanceof UnknownHostException) {
                    if (mIs1RequestData) {
                        showHttpError();
                    } else {
                        t(getHttpError());
                    }
                } else if (t instanceof IOException) {
                    if (mIs1RequestData) {
                        showHttpError();
                    } else {
                        t(getHttpError());
                    }
                }
                mIs1RequestData = false;
            }
        });
        call.enqueue(callback);
        return null;
    }

    MaterialDialog materialDialog;

    public void loadingShow(String msg) {
        if (getActivity() == null) {
            return;
        }
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(getActivity())
                    .content(TextUtils.isEmpty(msg) ? "加载中" : msg)
                    .progress(true, 0)
                    .dismissListener(this)
                    .autoDismiss(false).show();
        } else {
            materialDialog.setContent(TextUtils.isEmpty(msg) ? "加载中" : msg);
            materialDialog.show();
        }
    }

    public void loadingClose() {
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
    }

    protected void removeAllCalls() {
        for (Call call : mCalls) {
            call.cancel();
        }
    }

    protected String getHttpError() {
        return "网络不给力,请检查网络设置";
    }

    protected String getServerError() {
        return "服务器异常";
    }

    protected void setIs1Request(boolean bool) {
        this.mIs1RequestData = bool;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }
}
