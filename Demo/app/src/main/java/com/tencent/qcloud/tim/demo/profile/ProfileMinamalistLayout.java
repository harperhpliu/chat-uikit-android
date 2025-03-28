package com.tencent.qcloud.tim.demo.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.config.AppConfig;
import com.tencent.qcloud.tim.demo.login.StyleSelectActivity;
import com.tencent.qcloud.tim.demo.login.ThemeSelectActivity;
import com.tencent.qcloud.tim.demo.main.MainActivity;
import com.tencent.qcloud.tim.demo.main.MainMinimalistActivity;
import com.tencent.qcloud.tim.demo.utils.Constants;
import com.tencent.qcloud.tim.demo.utils.DemoLog;
import com.tencent.qcloud.tim.demo.utils.ProfileUtil;
import com.tencent.qcloud.tuicore.TUIConfig;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.TUIThemeManager;
import com.tencent.qcloud.tuicore.interfaces.TUIExtensionInfo;
import com.tencent.qcloud.tuicore.util.ErrorMessageConverter;
import com.tencent.qcloud.tuicore.util.SPUtils;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.timcommon.component.MinimalistLineControllerView;
import com.tencent.qcloud.tuikit.timcommon.component.activities.SelectionMinimalistActivity;
import com.tencent.qcloud.tuikit.timcommon.component.gatherimage.ShadeImageView;
import com.tencent.qcloud.tuikit.timcommon.component.impl.GlideEngine;
import com.tencent.qcloud.tuikit.timcommon.util.ScreenUtil;
import com.tencent.qcloud.tuikit.tuichat.config.TUIChatConfigs;
import com.tencent.qcloud.tuikit.tuicontact.TUIContactService;
import com.tencent.qcloud.tuikit.tuicontact.config.TUIContactConfig;
import com.tencent.qcloud.tuikit.tuicontact.config.minimalistui.TUIContactConfigMinimalist;
import com.tencent.qcloud.tuikit.tuicontact.interfaces.ContactEventListener;
import com.tencent.qcloud.tuikit.tuiconversation.TUIConversationService;
import com.tencent.qcloud.tuikit.tuiconversation.config.TUIConversationConfig;
import com.tencent.qcloud.tuikit.tuiconversation.config.minimalistui.TUIConversationConfigMinimalist;
import com.tencent.qcloud.tuikit.tuiconversation.interfaces.ConversationEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProfileMinamalistLayout extends FrameLayout implements View.OnClickListener {
    private static final String TAG = ProfileMinamalistLayout.class.getSimpleName();

    private ShadeImageView userIcon;
    private TextView accountView;
    private TextView nickNameView;
    private TextView signatureView;
    private TextView signatureTagView;

    private MinimalistLineControllerView modifyAllowTypeView;
    private MinimalistLineControllerView showRecentCalls;
    private MinimalistLineControllerView aboutIM;
    private RelativeLayout selfDetailArea;
    private LinearLayout llSettingsContainer;
    private SwitchCompat messageReadStatusSwitch;
    private TextView messageReadStatusSubtitle;
    private SwitchCompat userStatusSwitch;
    private TextView userStatusSubTitle;
    private MinimalistLineControllerView changeStyleView;
    private MinimalistLineControllerView changeThemeView;
    private TextView logoutButton;
    private View profileHeader;
    private SharedPreferences mSharedPreferences;

    private ArrayList<ProfileSetting> settingsList = new ArrayList<>();
    private ArrayList<String> joinTypeTextList = new ArrayList<>();
    private ArrayList<Integer> joinTypeIdList = new ArrayList<>();
    private int mJoinTypeIndex = 2;
    private String mIconUrl;
    private String mSignature;
    private String mNickName;

    private V2TIMSDKListener v2TIMSDKListener = null;

    private ImageView homeView;
    private TextView titleView, rtCubeTitleView;
    private ProfileMinimalistFragment.OnClickListener mClickListener = null;

    public ProfileMinamalistLayout(Context context) {
        super(context);
        init();
    }

    public ProfileMinamalistLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileMinamalistLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.minimalist_profile_layout, this);

        selfDetailArea = findViewById(R.id.self_detail_area);
        selfDetailArea.setOnClickListener(this);

        userIcon = findViewById(R.id.self_icon);
        int radius = ScreenUtil.dip2px(33);
        userIcon.setRadius(radius);
        accountView = findViewById(R.id.self_account);
        nickNameView = findViewById(R.id.self_nick_name);
        signatureView = findViewById(R.id.self_signature);
        signatureTagView = findViewById(R.id.self_signature_tag);
        profileHeader = findViewById(R.id.profile_header_layout);
        llSettingsContainer = findViewById(R.id.ll_settings_container);

        View allowTypeView = inflate(getContext(), R.layout.minimalist_profile_settings_allow_type, null);
        ProfileSetting allowTypeSetting = new ProfileSetting();
        allowTypeSetting.setWeight(700);
        allowTypeSetting.setSettingView(allowTypeView);
        settingsList.add(allowTypeSetting);

        View statusView = inflate(getContext(), R.layout.minimalist_profile_status_layout, null);
        ProfileSetting statusSetting = new ProfileSetting();
        statusSetting.setWeight(600);
        statusSetting.setSettingView(statusView);
        settingsList.add(statusSetting);

        View recentCallsView = inflate(getContext(), R.layout.minimalist_profile_settings_recent_calls, null);
        ProfileSetting recentCallsSetting = new ProfileSetting();
        recentCallsSetting.setWeight(400);
        recentCallsSetting.setSettingView(recentCallsView);
        settingsList.add(recentCallsSetting);

        View selectStyleView = inflate(getContext(), R.layout.minimalist_profile_settings_select_style, null);
        ProfileSetting selectStyleSetting = new ProfileSetting();
        selectStyleSetting.setWeight(300);
        selectStyleSetting.setSettingView(selectStyleView);
        settingsList.add(selectStyleSetting);

        View selectThemeView = inflate(getContext(), R.layout.minimalist_profile_settings_select_theme, null);
        ProfileSetting selectThemeSetting = new ProfileSetting();
        selectThemeSetting.setWeight(200);
        selectThemeSetting.setSettingView(selectThemeView);
        settingsList.add(selectThemeSetting);

        View aboutIMView = inflate(getContext(), R.layout.minimalist_profile_settings_about_im, null);
        ProfileSetting aboutIMSetting = new ProfileSetting();
        aboutIMSetting.setWeight(100);
        aboutIMSetting.setSettingView(aboutIMView);
        settingsList.add(aboutIMSetting);

        settingsList.addAll(getExtensionMoreSettings());
        Collections.sort(settingsList, new Comparator<ProfileSetting>() {
            @Override
            public int compare(ProfileSetting o1, ProfileSetting o2) {
                return o2.getWeight() - o1.getWeight();
            }
        });

        for (ProfileSetting setting : settingsList) {
            llSettingsContainer.addView(setting.getSettingView());
        }

        aboutIM = aboutIMView.findViewById(R.id.about_im);
        aboutIM.setCanNav(true);
        aboutIM.setOnClickListener(this);

        modifyAllowTypeView = allowTypeView.findViewById(R.id.modify_allow_type);
        modifyAllowTypeView.setCanNav(true);
        modifyAllowTypeView.setOnClickListener(this);

        showRecentCalls = recentCallsView.findViewById(R.id.show_recent_calls);
        showRecentCalls.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.getInstance(Constants.DEMO_SETTING_SP_NAME).put(Constants.MINIMALIST_RECENT_CALLS_ENABLE, isChecked, true);
                Intent intent = new Intent();
                intent.setAction(Constants.RECENT_CALLS_ENABLE_ACTION);
                intent.putExtra(Constants.MINIMALIST_RECENT_CALLS_ENABLE, isChecked);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }
        });
        boolean isEnableRecentCalls = SPUtils.getInstance(Constants.DEMO_SETTING_SP_NAME).getBoolean(Constants.MINIMALIST_RECENT_CALLS_ENABLE, true);
        showRecentCalls.setChecked(isEnableRecentCalls);

        changeStyleView = selectStyleView.findViewById(R.id.select_style);
        changeStyleView.setCanNav(true);
        changeStyleView.setOnClickListener(this);
        changeThemeView = selectThemeView.findViewById(R.id.select_theme);
        changeThemeView.setCanNav(true);
        changeThemeView.setOnClickListener(this);

        logoutButton = findViewById(R.id.logout_btn);

        joinTypeTextList.add(getResources().getString(R.string.allow_type_allow_any));
        joinTypeTextList.add(getResources().getString(R.string.allow_type_deny_any));
        joinTypeTextList.add(getResources().getString(R.string.allow_type_need_confirm));
        joinTypeIdList.add(V2TIMUserFullInfo.V2TIM_FRIEND_ALLOW_ANY);
        joinTypeIdList.add(V2TIMUserFullInfo.V2TIM_FRIEND_DENY_ANY);
        joinTypeIdList.add(V2TIMUserFullInfo.V2TIM_FRIEND_NEED_CONFIRM);

        mSharedPreferences = getContext().getSharedPreferences(Constants.DEMO_SETTING_SP_NAME, Context.MODE_PRIVATE);
        messageReadStatusSwitch = statusView.findViewById(R.id.message_read_switch);
        messageReadStatusSubtitle = statusView.findViewById(R.id.message_read_status_subtitle);
        initMessageReadStatus();
        messageReadStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToastUtil.toastShortMessage(getResources().getString(R.string.demo_buying_tips));
                    messageReadStatusSubtitle.setText(R.string.demo_message_read_switch_open_text);
                } else {
                    messageReadStatusSubtitle.setText(R.string.demo_message_read_switch_close_text);
                }
                setMessageReadStatus(isChecked, true);
            }
        });

        userStatusSwitch = statusView.findViewById(R.id.user_status_switch);
        userStatusSubTitle = statusView.findViewById(R.id.user_status_subtitle);
        boolean userStatus = mSharedPreferences.getBoolean(Constants.DEMO_SP_KEY_USER_STATUS, TUIConversationConfigMinimalist.isShowUserOnlineStatusIcon());
        userStatusSwitch.setChecked(userStatus);
        TUIConversationConfigMinimalist.setShowUserOnlineStatusIcon(userStatus);
        TUIContactConfigMinimalist.setShowUserOnlineStatusIcon(userStatus);
        userStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userStatusSubTitle.setText(getResources().getString(R.string.demo_user_status_switch_on_text));
                    ToastUtil.toastLongMessage(getResources().getString(R.string.demo_buying_tips));
                } else {
                    userStatusSubTitle.setText(getResources().getString(R.string.demo_user_status_switch_off_text));
                }

                TUIConversationConfigMinimalist.setShowUserOnlineStatusIcon(isChecked);
                TUIContactConfigMinimalist.setShowUserOnlineStatusIcon(isChecked);
                mSharedPreferences.edit().putBoolean(Constants.DEMO_SP_KEY_USER_STATUS, isChecked).commit();
                refreshFragmentUI();
            }
        });

        String selfUserID = TUILogin.getLoginUser();

        accountView.setText(selfUserID);
        loadSelfInfo();
        setUserInfoListener();

        homeView = findViewById(com.tencent.qcloud.tuikit.tuicontact.R.id.home_rtcube);
        titleView = findViewById(com.tencent.qcloud.tuikit.tuicontact.R.id.title);
        rtCubeTitleView = findViewById(com.tencent.qcloud.tuikit.tuicontact.R.id.title_rtcube);
    }

    public void loadSelfInfo() {
        String selfUserID = TUILogin.getLoginUser();
        List<String> selfIdList = new ArrayList<>();
        selfIdList.add(selfUserID);
        V2TIMManager.getInstance().getUsersInfo(selfIdList, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                if (v2TIMUserFullInfos != null && !v2TIMUserFullInfos.isEmpty()) {
                    setUserInfo(v2TIMUserFullInfos.get(0));
                }
            }

            @Override
            public void onError(int code, String desc) {
                DemoLog.e(TAG, "getUsersInfo failed, code = " + code + ", desc = " + desc);
            }
        });
    }

    private List<ProfileSetting> getExtensionMoreSettings() {
        List<ProfileSetting> settingsList = new ArrayList<>();
        List<TUIExtensionInfo> extensionList = TUICore.getExtensionList(TUIConstants.TIMAppKit.Extension.ProfileSettings.MINIMALIST_EXTENSION_ID, null);
        for (TUIExtensionInfo extensionInfo : extensionList) {
            Map<String, Object> paramMap = extensionInfo.getData();
            Object viewObject = paramMap.get(TUIConstants.TIMAppKit.Extension.ProfileSettings.KEY_VIEW);
            if (viewObject != null && viewObject instanceof View) {
                View view = (View) viewObject;
                ProfileSetting moreSetting = new ProfileSetting();
                moreSetting.setWeight(extensionInfo.getWeight());
                moreSetting.setSettingView(view);
                settingsList.add(moreSetting);
            }
        }

        return settingsList;
    }

    public void setOnClickListener(ProfileMinimalistFragment.OnClickListener listener) {
        mClickListener = listener;
    }


    public void initUI() {
        if (TUIConfig.getTUIHostType() != TUIConfig.TUI_HOST_TYPE_RTCUBE) {
            homeView.setVisibility(GONE);
            titleView.setVisibility(VISIBLE);
            rtCubeTitleView.setVisibility(GONE);

            selfDetailArea.setVisibility(VISIBLE);
            aboutIM.setVisibility(VISIBLE);
            logoutButton.setVisibility(VISIBLE);
        } else {
            homeView.setVisibility(VISIBLE);
            titleView.setVisibility(GONE);
            rtCubeTitleView.setVisibility(VISIBLE);
            homeView.setBackgroundResource(com.tencent.qcloud.tuikit.timcommon.R.drawable.common_title_bar_home_icon);
            homeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TUIConstants.TIMAppKit.BACK_TO_RTCUBE_DEMO_TYPE_KEY, TUIConstants.TIMAppKit.BACK_TO_RTCUBE_DEMO_TYPE_IM);
                    TUICore.startActivity("TRTCMainActivity", bundle);
                    if (mClickListener != null) {
                        mClickListener.finishActivity();
                    }
                }
            });

            if (AppConfig.RT_CUBE_PACKAGE_NAME.equals(getContext().getPackageName())) {
                changeStyleView.setVisibility(VISIBLE);
            }

            selfDetailArea.setVisibility(GONE);
            aboutIM.setVisibility(GONE);
            logoutButton.setVisibility(GONE);

            int theme = TUIThemeManager.getInstance().getCurrentTheme();
            if (theme == TUIThemeManager.THEME_LIGHT) {
                changeThemeView.setContent(getResources().getString(R.string.light_theme));
            } else if (theme == TUIThemeManager.THEME_LIVELY) {
                changeThemeView.setContent(getResources().getString(R.string.lively_theme));
            } else if (theme == TUIThemeManager.THEME_SERIOUS) {
                changeThemeView.setContent(getResources().getString(R.string.serious_theme));
            } else {
                changeThemeView.setContent("");
            }

            if (AppConfig.DEMO_UI_STYLE == AppConfig.DEMO_UI_STYLE_MINIMALIST) {
                changeStyleView.setContent(getResources().getString(R.string.style_minimalist));
            } else {
                changeStyleView.setContent(getResources().getString(R.string.style_classic));
            }
        }
    }

    private void refreshFragmentUI() {
        List<ConversationEventListener> conversationEventListenerList = TUIConversationService.getInstance().getConversationEventListenerList();
        for (ConversationEventListener conversationEventListener : conversationEventListenerList) {
            if (conversationEventListener != null) {
                conversationEventListener.refreshUserStatusFragmentUI();
            }
        }

        List<ContactEventListener> contactEventListenerList = TUIContactService.getInstance().getContactEventListenerList();
        for (ContactEventListener contactEventListener : contactEventListenerList) {
            contactEventListener.refreshUserStatusFragmentUI();
        }
    }

    private void initMessageReadStatus() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.DEMO_SETTING_SP_NAME, Context.MODE_PRIVATE);
        boolean messageReadStatus = sharedPreferences.getBoolean(Constants.DEMO_SP_KEY_MESSAGE_READ_STATUS,
                ProfileUtil.DEFAULT_IS_OPEN_MESSAGE_READ_RECEIPT);
        setMessageReadStatus(messageReadStatus, false);
        messageReadStatusSwitch.setChecked(messageReadStatus);
    }

    private void setMessageReadStatus(boolean isShowReadStatus, boolean needUpdate) {
        TUIChatConfigs.getConfigs().getGeneralConfig().setMsgNeedReadReceipt(isShowReadStatus);
        if (needUpdate) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.DEMO_SETTING_SP_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.DEMO_SP_KEY_MESSAGE_READ_STATUS, isShowReadStatus);
            editor.commit();
        }
    }

    private void setUserInfo(V2TIMUserFullInfo info) {
        mIconUrl = info.getFaceUrl();
        int radius = getResources().getDimensionPixelSize(R.dimen.demo_profile_face_radius);
        GlideEngine.loadUserIcon(userIcon, mIconUrl, radius);
        mNickName = info.getNickName();
        if (TextUtils.isEmpty(mNickName)) {
            nickNameView.setText(info.getUserID());
        } else {
            nickNameView.setText(mNickName);
        }
        accountView.setText(info.getUserID());

        mSignature = info.getSelfSignature();
        if (TextUtils.isEmpty(mSignature)) {
            signatureTagView.setText(getResources().getString(R.string.demo_no_status));
        } else {
            signatureTagView.setText(getResources().getString(R.string.demo_signature_tag));
        }
        signatureView.setText(mSignature);
        modifyAllowTypeView.setContent(getResources().getString(R.string.allow_type_need_confirm));
        int allowType = info.getAllowType();
        if (allowType == V2TIMUserFullInfo.V2TIM_FRIEND_ALLOW_ANY) {
            modifyAllowTypeView.setContent(getResources().getString(R.string.allow_type_allow_any));
            mJoinTypeIndex = 0;
        } else if (allowType == V2TIMUserFullInfo.V2TIM_FRIEND_DENY_ANY) {
            modifyAllowTypeView.setContent(getResources().getString(R.string.allow_type_deny_any));
            mJoinTypeIndex = 1;
        } else if (allowType == V2TIMUserFullInfo.V2TIM_FRIEND_NEED_CONFIRM) {
            modifyAllowTypeView.setContent(getResources().getString(R.string.allow_type_need_confirm));
            mJoinTypeIndex = 2;
        } else {
            modifyAllowTypeView.setContent("");
        }
    }

    private void setUserInfoListener() {
        v2TIMSDKListener = new V2TIMSDKListener() {
            @Override
            public void onSelfInfoUpdated(V2TIMUserFullInfo info) {
                setUserInfo(info);
            }
        };
        V2TIMManager.getInstance().addIMSDKListener(v2TIMSDKListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        V2TIMManager.getInstance().removeIMSDKListener(v2TIMSDKListener);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.self_detail_area) {
            Intent intent = new Intent(getContext(), SelfDetailMinamalistActivity.class);
            getContext().startActivity(intent);
        }
        if (v.getId() == R.id.modify_allow_type) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.Selection.TITLE, getResources().getString(R.string.add_rule));
            bundle.putStringArrayList(Constants.Selection.LIST, joinTypeTextList);
            bundle.putInt(Constants.Selection.DEFAULT_SELECT_ITEM_INDEX, mJoinTypeIndex);
            SelectionMinimalistActivity.startListSelection((Activity) getContext(), bundle, new SelectionMinimalistActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object text) {
                    mJoinTypeIndex = (Integer) text;
                    updateProfile();
                }
            });
        } else if (v.getId() == R.id.about_im) {
            Intent intent = new Intent(getContext(), AboutIMMinamalistActivity.class);
            getContext().startActivity(intent);
        } else if (v.getId() == R.id.select_style) {
            StyleSelectActivity.OnResultReturnListener listener = new StyleSelectActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object res) {
                    int style = (int) res;
                    selectedBackToFragment(style);
                    StyleSelectActivity.finishActivity();
                }
            };
            StyleSelectActivity.startStyleSelectActivity(getContext(), listener);
        } else if (v.getId() == R.id.select_theme) {
            ThemeSelectActivity.OnResultReturnListener listener = new ThemeSelectActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object res) {
                    selectedBackToFragment(AppConfig.DEMO_UI_STYLE);
                    ThemeSelectActivity.finishActivity();
                }
            };
            ThemeSelectActivity.startSelectTheme(getContext(), listener);
        }
    }

    private void selectedBackToFragment(int style) {
        Intent intent;
        MainActivity.finishMainActivity();
        MainMinimalistActivity.finishMainActivity();
        if (style == AppConfig.DEMO_UI_STYLE_CLASSIC) {
            intent = new Intent(getContext(), MainActivity.class);
        } else {
            intent = new Intent(getContext(), MainMinimalistActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    private void updateProfile() {
        V2TIMUserFullInfo v2TIMUserFullInfo = new V2TIMUserFullInfo();

        int allowType = joinTypeIdList.get(mJoinTypeIndex);
        v2TIMUserFullInfo.setAllowType(allowType);

        V2TIMManager.getInstance().setSelfInfo(v2TIMUserFullInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                DemoLog.e(TAG, "modifySelfProfile err code = " + code + ", desc = " + ErrorMessageConverter.convertIMError(code, desc));
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + ErrorMessageConverter.convertIMError(code, desc));
            }

            @Override
            public void onSuccess() {
                DemoLog.i(TAG, "modifySelfProfile success");
            }
        });
    }
}
