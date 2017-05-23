package com.droid.base.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.CallbackListener;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by wungko on 16/1/14.
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 页面跟布局
     **/
    private LinearLayout contentContainer;
    /**
     * 布局层级
     **/
    private final int VIEW_LOADING_INDEX = 0, VIEW_CONTENT_INDEX = 1, VIEW_EMPTY_INDEX = 2, VIEW_BERROR_INDEX = 3, VIEW_HERROR_INDEX = 4;
    /**
     * view 注解
     **/
    Unbinder butterKnife;
    /**
     * 请求队列
     **/
    private Stack<Call> mCalls = new Stack<>();
    private boolean mIs1RequestData = true; //首次加载数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**设置屏幕方向**/
        setOrientation();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(createLayout(inflater));
        butterKnife = ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        //activity 初始化
        initActivity(extras);
    }

    /**
     * 设置屏幕方向
     **/
    protected void setOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        butterKnife.unbind();
    }

    @Override
    public void finish() {
        super.finish();
        removeAllCalls();
    }

    /**
     * 布局初始化
     *
     * @param inflater
     */
    private LinearLayout createLayout(LayoutInflater inflater) {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        contentContainer = new LinearLayout(this);
        if (getToolbarLayout() != 0) {
            inflater.inflate(getToolbarLayout(), root, true);
            root.addView(contentContainer, layoutParams);
        } else {
            L.i("",BaseActivity.this.getClass().getSimpleName() + " has no toolbar layout");
        }

        // 进度条
        View loading = inflater.inflate(BaseApplication.getInstance().getLoadingLayout(), null, false);
        contentContainer.addView(loading, VIEW_LOADING_INDEX, layoutParams);
        loading.setVisibility(GONE);

        // 内容
        View content = inflater.inflate(getLayout(), null, false);
        contentContainer.addView(content, VIEW_CONTENT_INDEX, layoutParams);

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

    /**
     * 空布局 可重写此方法
     */
    protected int getEmptyLayout() {
        return 0;
    }

    protected abstract int getToolbarLayout();

    /**
     * 返回activity 布局
     *
     * @return
     */
    protected abstract int getLayout();

    /**
     * 初始化Activity
     *
     * @param extras
     */
    protected abstract void initActivity(Bundle extras);

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

        Animation anim = AnimationUtils.loadAnimation(this, animId);
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
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public <T> T requestNoloading(final retrofit2.Call<T> call, Callback<T> callback) {
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

    public <T> T request(final retrofit2.Call<T> call, Callback<T> callback,String tipMsg) {
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

    protected String getServerError() {
        return "服务器异常";
    }

    protected String getHttpError() {
        return "网络不给力,请检查网络设置";
    }


    MaterialDialog materialDialog;

    protected void loadingShow(String msg) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(this)
                    .content(TextUtils.isEmpty(msg) ? "加载中" : msg)
                    .progress(true, 0)
                    .autoDismiss(false).show();
        } else {
            materialDialog.setContent(TextUtils.isEmpty(msg) ? "加载中" : msg);
            materialDialog.show();
        }
    }

    protected void loadingClose() {
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
    }

    protected void setIs1Request(boolean bool) {
        this.mIs1RequestData = bool;
    }

    private void removeAllCalls() {
        for (Call call : mCalls) {
            call.cancel();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (autoHideKeyBoard()) {
            handleKeyBoardVisible(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 键盘自动隐藏或者展示
     * @return
     */
    protected boolean autoHideKeyBoard(){
        return true;
    }

    /**
     * 处理键盘隐藏和显示
     * @param ev
     */
    private void handleKeyBoardVisible(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (!getKeyBoardStatus()) onSoftKeyBoardHide();
            } else {
                if (getKeyBoardStatus()) onSoftKeyBoardShow();
            }
        }
    }

    /**
     * 输入法收起
     */
    protected void onSoftKeyBoardHide(){

    }

    /**
     * 输入法打开
     */
    protected void onSoftKeyBoardShow(){

    }

    /**
     * 获取输入法状态
     *
     * @return
     */
    private boolean getKeyBoardStatus() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }


    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }


    public static void main(String[] args) {
        for(int i = 0 ; i < 100;i++){

        }

    }
}
