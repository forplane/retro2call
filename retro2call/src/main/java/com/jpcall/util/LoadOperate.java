package com.jpcall.util;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jpcall.R;
import com.jpcall.dao.OnLoadListener;
import com.jpcall.dao.OnOpeListener;


/**
 * Created by voctex on 2016/8/11.
 * 该操作类可以实现在一个View上进行转圈处理，当然也可以显示其他状态（例如没有数据，没有网络，加载失败）
 * 注意：传入的View的父布局暂时不能是RelativeLayout，而且传入的View最好不要设置内边距
 */
public class LoadOperate implements Runnable, View.OnClickListener, OnOpeListener {

    private static int mGlobalLoading = 0;//转圈的图片id
    private static int mGlobalFail = 0;//失败的图片id
    private static int mGlobalNoNet = 0;//没有网络的图片id
    private static int mGlobalNoData = 0;//没有数据的图片id
    private static int mGlobalIcon = 0;//图标的图片id

    private Params params;
    private LinearLayout loadingLayout;
    private RotateAnimation rotateAnimation;

    private final static int TAG_NORMAL = 0;
    private final static int TAG_LOADING = TAG_NORMAL + 1;
    private final static int TAG_NODATA = TAG_LOADING + 1;
    private final static int TAG_NONET = TAG_NODATA + 1;
    private final static int TAG_ERROR = TAG_NONET + 1;
    private final static int TAG_DISMISS = TAG_ERROR + 1;
    private int currentState = TAG_NORMAL;
    private boolean isPost = false;

    private LoadOperate(Params params) {
        this.params = params;
        initView();
    }

    public LoadOperate(Builder builder) {
        this.params = builder.params;
        initView();
    }

    /**
     * 设置加载中.失败.没有数据的图片资源
     */
    public static void setImageResId(int globalLoading, int globalFail, int globalNoData, int globalIcon, int globalNoNet) {
        mGlobalFail = globalFail;
        mGlobalLoading = globalLoading;
        mGlobalNoData = globalNoData;
        mGlobalIcon = globalIcon;
        mGlobalNoNet = globalNoNet;
    }

    /**
     * 在View被绘制出来后才可以进行一系列操作
     */
    private void initView() {
        params.view.post(this);
    }


    @Override
    public void run() {
        ViewGroup viewParent = (ViewGroup) params.view.getParent();
        int position = viewParent.indexOfChild(viewParent);

        ViewGroup.MarginLayoutParams layoutP = (ViewGroup.MarginLayoutParams) params.view.getLayoutParams();
        //获得之前的View的宽高和在父布局当中的外边距
        int width = layoutP.width;
        int height = layoutP.height;
        int leftMargin = layoutP.leftMargin;
        int rightMargin = layoutP.rightMargin;
        int topMargin = layoutP.topMargin;
        int bottomMargin = layoutP.bottomMargin;


        FrameLayout frameLayout = new FrameLayout(params.mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width,
                height);
        layoutParams.gravity = Gravity.CENTER;
        frameLayout.setLayoutParams(layoutParams);

        //获得在父布局的位置
        int top = params.view.getTop();
        int left = params.view.getLeft();


        //之前的ViewGroup的父布局移除掉ViewGroup，并加入新建的帧布局，然后帧布局再加入之前的ViewGroup
        viewParent.removeView(params.view);
        viewParent.addView(frameLayout, position, layoutParams);
        frameLayout.addView(params.view);
        //设置帧布局在父布局的外边距
        ViewGroup.MarginLayoutParams newLayoutP = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
        newLayoutP.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        frameLayout.setLayoutParams(newLayoutP);
        addLoadingView(frameLayout);
        if (currentState == TAG_LOADING) {
            showLoading();
        }
    }


    private View addLoadingView(FrameLayout frameLayout) {
        //转圈图片的父布局，用来装载转圈控件和覆盖原先View的可视范围
        loadingLayout = new LinearLayout(params.mContext);
        loadingLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams loadingLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        loadingLayout.setGravity(Gravity.CENTER);
        loadingLayout.setLayoutParams(loadingLayoutParams);
        frameLayout.addView(loadingLayout);
        loadingLayout.setOnClickListener(this);

        ViewGroup.MarginLayoutParams testParams = (ViewGroup.MarginLayoutParams) loadingLayout.getLayoutParams();
        if (params.isHaveHead) {
            testParams.setMargins(0, getContext().getResources().getDimensionPixelSize(R.dimen.head), 0, 0);
        }
        loadingLayout.setLayoutParams(testParams);

        LinearLayout.LayoutParams lp;


        //转圈控件，实现转圈动画
        ImageView loadView = new ImageView(params.mContext);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        loadView.setLayoutParams(lp);

        //图标
        ImageView iconView = new ImageView(params.mContext);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        iconView.setLayoutParams(lp);

        //桢布局，放转圈以及图标
        FrameLayout loadFragme = new FrameLayout(params.mContext);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        loadFragme.setLayoutParams(lp);
        loadFragme.addView(loadView);
        loadFragme.addView(iconView);

        loadingLayout.addView(loadFragme);
        //转圈动画
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1300);
//        rotateAnimation.setFillAfter(true);
//        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(-1);

        isPost = true;

        switch (currentState) {
            case TAG_LOADING: {
                showLoading();
                break;
            }
            case TAG_NODATA: {
                showNoData();
                break;
            }
            case TAG_NONET: {
                showNoNet();
                break;
            }
            case TAG_ERROR: {
                showError();
                break;
            }
            case TAG_NORMAL: {
                loadingLayout.setVisibility(View.GONE);
                break;
            }
            case TAG_DISMISS: {
                dismiss();
                break;
            }
        }

        return loadingLayout;
    }

    /**
     * 没有数据的显示，处理过后的
     */
    public void showNoData() {
        currentState = TAG_NODATA;
        if (!isPost || this.params.isDialog) {
            return;
        }
        ImageView loadView = getLoadView(TAG_NODATA);
        loadView.setImageResource(this.params.noDataPic == 0 ?
                (mGlobalNoData == 0 ? R.mipmap.ic_nodata : mGlobalNoData) : this.params.noDataPic);
        loadView.clearAnimation();
        getIconView().setVisibility(View.GONE);
    }


    //2.0

    /**
     * 加载中的显示
     */
    @Override
    public void showLoading() {
        currentState = TAG_LOADING;
        if (!isPost) {
            return;
        }
        if (params.isDialog) {
            //如果是dialog的情况，那么直接设置0
            loadingLayout.setBackgroundColor(params.isBackGround ? Color.WHITE : Color.argb(0, 0, 0, 0));
        } else {
            loadingLayout.setBackgroundColor(params.isBackGround ? Color.WHITE : Color.argb(120, 0, 0, 0));
        }
        if (mGlobalIcon != 0) {
            ImageView iconView = getIconView();
            iconView.setImageResource(mGlobalIcon);
        }

        //判断是否有网，然后显示不同的图片
        if (isNetworkAvailable(params.mContext)) {
            ImageView loadView = getLoadView(TAG_LOADING);
            loadingLayout.setTag(TAG_LOADING);
            int resid = this.params.loadingPic == 0 ? (mGlobalLoading == 0 ? R.mipmap.ic_circle_gray : mGlobalLoading) : this.params.loadingPic;
            loadView.setImageResource(resid);
            loadView.startAnimation(rotateAnimation);
        } else {
            //只要是没有网络的，就一定显示白色背景的，跟其他因素乜有一点关系
            loadingLayout.setBackgroundColor(Color.WHITE);
            ImageView loadView = getLoadView(TAG_NONET);
            loadingLayout.setTag(TAG_NONET);
            int resid = this.params.noNetPic == 0 ? (mGlobalNoNet == 0 ? R.mipmap.ic_nonet : mGlobalNoNet) : this.params.noNetPic;
            loadView.setImageResource(resid);
            loadView.clearAnimation();

            getIconView().setVisibility(View.INVISIBLE);

        }
    }


    //2.0

    /**
     * 加载失败的显示
     */
    @Override
    public void showError() {
        boolean networkAvailable = isNetworkAvailable(params.mContext);
        currentState = networkAvailable ? TAG_ERROR : TAG_NONET;
        if (!isPost || this.params.isDialog) {
            return;
        }
        if (networkAvailable) {
            getIconView().setVisibility(View.INVISIBLE);
            loadingLayout.setBackgroundColor(Color.WHITE);
            ImageView loadView = getLoadView(TAG_ERROR);
            int resid = this.params.errorPic == 0 ? (mGlobalFail == 0 ? R.mipmap.loading_fail : mGlobalFail) : this.params.errorPic;
            loadView.setImageResource(resid);
            loadView.clearAnimation();
        } else {
            showNoNet();
        }

    }


    /**
     * 没有网络的显示
     */
    public void showNoNet() {
        currentState = TAG_NONET;
        if (!isPost || this.params.isDialog) {
            return;
        }
        getIconView().setVisibility(View.INVISIBLE);
        loadingLayout.setBackgroundColor(Color.WHITE);
        ImageView loadView = getLoadView(TAG_NONET);
        int resid = this.params.noNetPic == 0 ? (mGlobalNoNet == 0 ? R.mipmap.ic_nonet : mGlobalNoNet) : this.params.noNetPic;
        loadView.setImageResource(resid);
        loadView.clearAnimation();
    }

    /**
     * 加载完成的显示
     */
    public void dismiss() {
        currentState = TAG_DISMISS;
        if (!isPost) {
            return;
        }
        loadingLayout.setBackgroundColor(Color.WHITE);
        this.loadingLayout.setVisibility(View.GONE);
    }

    public Context getContext() {
        return this.params.mContext;
    }

    /**
     * 获得转圈的控件，可以设置其他图片，并设置转圈控件的父布局的点击事件的标签
     */
    private ImageView getLoadView(int tag) {
        loadingLayout.setVisibility(View.VISIBLE);
        ViewGroup fragmeView = (ViewGroup) loadingLayout.getChildAt(0);
        ImageView loadView = (ImageView) fragmeView.getChildAt(0);
        loadingLayout.setTag(tag);
        return loadView;
    }

    /**
     * 获得图标的控件，可以设置其他图片，
     */
    private ImageView getIconView() {
        loadingLayout.setVisibility(View.VISIBLE);
        ViewGroup fragmeView = (ViewGroup) loadingLayout.getChildAt(0);
        ImageView iconView = (ImageView) fragmeView.getChildAt(1);
        return iconView;
    }

    @Override
    public void onClick(View v) {
        switch ((int) v.getTag()) {
            case TAG_LOADING: {
                //空事件，只是为了拦截点击事件，避免用户在加载的时候可以点击下层的控件
//                Toast.makeText(params.mContext, "正在加载中", Toast.LENGTH_SHORT).show();
                if (onLoadListener != null) {
                    onLoadListener.loading();
                }
                break;
            }
            case TAG_NONET: {
//                Toast.makeText(params.mContext, "没有网络，点击重试", Toast.LENGTH_SHORT).show();

                if (onLoadListener != null) {
                    onLoadListener.noNet();
                }
                break;
            }
            case TAG_NODATA: {
//                Toast.makeText(params.mContext, "没有数据", Toast.LENGTH_SHORT).show();

                if (onLoadListener != null) {
                    onLoadListener.noData();
                }
                break;
            }
            case TAG_ERROR: {
//                Toast.makeText(params.mContext, "加载失败", Toast.LENGTH_SHORT).show();
                if (onLoadListener != null) {
                    onLoadListener.showError();
                }
                break;
            }
        }
    }

    /**
     * 参数容器类
     */
    private static class Params {
        private Context mContext;
        private View view;
        private int loadingPic = 0;
        private int noDataPic = 0;
        private int noNetPic = 0;
        private int errorPic = 0;

        //是否显示白色背景，默认false，显示透明效果
        //显示白色背景的主要是针对于有些情况下是需要遮挡的时候，此时就可以设置为true
        private boolean isBackGround = false;
        //是否有头部，如果为false那么就会盖住头部，此时头部是不能被点击，默认false
        private boolean isHaveHead = false;
        //是不是在Dialog中请求接口，默认false
        private boolean isDialog = false;

    }

    public static class Builder {

        private Params params;

        public Builder(Context mContext, View view) {
            this.params = new Params();
            this.params.mContext = mContext;
            this.params.view = view;

        }

        public Builder setDialog(boolean isDialog) {
            this.params.isDialog = isDialog;
            return this;
        }

        public Builder setHaveHead(boolean isHave) {
            this.params.isHaveHead = isHave;
            return this;
        }

        public Builder setNoNetPic(int pic) {
            this.params.noNetPic = pic;
            return this;
        }

        public Builder setNoData(int pic) {
            this.params.noDataPic = pic;
            return this;
        }

        public Builder setLoadingPic(int pic) {
            this.params.loadingPic = pic;
            return this;
        }

        /**
         * 是否显示白色显示
         */
        public Builder setBackGround(boolean isBack) {
            this.params.isBackGround = isBack;
            return this;
        }

        public Builder setErrorPic(int pic) {
            this.params.errorPic = pic;
            return this;
        }

        public LoadOperate build() {
            LoadOperate loadTool = new LoadOperate(params);
            return loadTool;
        }
    }


    /**
     * 检查当前网络是否可用
     * <p>
     * context
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    private OnLoadListener onLoadListener;

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }


    //2.0


    @Override
    public Context getOpeContext() {
        return getContext();
    }
}
