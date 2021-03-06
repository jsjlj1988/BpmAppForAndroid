package com.eazytec.bpm.app.home.userhome;

import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.data.NoticeListDataTObject;
import com.eazytec.bpm.app.home.data.app.AppIconDataTObject;
import com.eazytec.bpm.app.home.data.app.tobject.AppsDataTObject;
import com.eazytec.bpm.app.home.data.commonconfig.ImgDataTObject;
import com.eazytec.bpm.app.home.updatepwd.UpdatePwdContract;
import com.eazytec.bpm.app.home.webservice.WebApi;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.authentication.Token;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;
import com.eazytec.bpm.lib.utils.StringUtils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @author Administrator
 * @version Id: UserHomeAppPresenter, v 0.1 2017/7/13 15:57 Administrator Exp $$
 */
public class UserHomeAppPresenter extends RxPresenter<UserHomeAppContract.View> implements UserHomeAppContract.Presenter<UserHomeAppContract.View> {

    @Override public void loadApps() {

        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).menuList(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        mView.showProgress();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override public void call() {
                        mView.dismissProgress();
                    }
                })
                .subscribe(new Observer<AppsDataTObject>() {
                    @Override public void onNext(AppsDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadAppsSuccess(data);
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR, "远程应用列表加载失败:" + data.getErrorMsg());
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR, "远程应用列表加载失败:网络错误,请稍后再试");
                        mView.dismissProgress();
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void loadAllApps() {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).menuALLList("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        mView.showProgress();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override public void call() {
                        mView.dismissProgress();
                    }
                })
                .subscribe(new Observer<AppsDataTObject>() {
                    @Override public void onNext(AppsDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadAllAppsSuccess(data);
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR, "远程应用列表加载失败:" + data.getErrorMsg());
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR, "远程应用列表加载失败:网络错误,请稍后再试");
                        mView.dismissProgress();
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void commonconfig() {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).commonConfig(Token.createDefaultSysToken().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override public void call() {
                    }
                })
                .subscribe(new Observer<ImgDataTObject>() {
                    @Override public void onNext(ImgDataTObject data) {
                        if (data.isSuccess()) {
                            mView.getImgUrl(data);
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override public void loadNoticeList(int pageNo, int pageSize, String title) {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).loadNoticeList(title, String.valueOf(pageNo), String.valueOf(pageSize))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NoticeListDataTObject>() {
                    @Override public void onNext(NoticeListDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadSuccess(data);
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"公告加载失败" + data.getErrorMsg());
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"公告加载失败:网络错误,请稍后再试");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void loadAppIcon() {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).menuIcon()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override public void call() {
                    }
                })
                .subscribe(new Observer<AppIconDataTObject>() {
                    @Override public void onNext(AppIconDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadIconSuccess(data);
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                    }
                });
        addSubscrebe(rxSubscription);
    }
}


