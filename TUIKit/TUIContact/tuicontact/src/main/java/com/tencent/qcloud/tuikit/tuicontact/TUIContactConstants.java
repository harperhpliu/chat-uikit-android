package com.tencent.qcloud.tuikit.tuicontact;

import com.tencent.imsdk.v2.V2TIMGroupAtInfo;

public class TUIContactConstants {
    public static final String FORWARD_SELECT_CONVERSATION_KEY = "forward_select_conversation_key";
    public static final int FORWARD_SELECT_MEMBERS_CODE = 102;
    public static final int FORWARD_CREATE_GROUP_CODE = 103;

    public static final String FORWARD_CREATE_NEW_CHAT = "forward_create_new_chat";
    public static final String IM_PRODUCT_DOC_URL = "https://cloud.tencent.com/product/im";
    public static final String IM_PRODUCT_DOC_URL_EN = "https://www.tencentcloud.com/products/im?lang=en&pg=";
    /**
     * 1: Just a text message with a link
     * 2: The video calling version supported by iOS is no longer compatible
     * 3: unreleased version
     * 4: Android/iOS/Web interoperable version for video call
     */
    public static final int JSON_VERSION_4 = 4;
    public static int version = JSON_VERSION_4;
    public static final String GROUP_PROFILE_BEAN = "groupProfileBean";

    public static final String GROUP_FACE_URL = "https://im.sdk.qcloud.com/download/tuikit-resource/group-avatar/group_avatar_%s.png";
    public static final int GROUP_FACE_COUNT = 24;

    public static class Selection {
        public static final String SELECT_ALL = V2TIMGroupAtInfo.AT_ALL_TAG;
        public static final String CONTENT = "content";
        public static final String TYPE = "type";
        public static final String GROUP_TYPE = "groupType";
        public static final String TITLE = "title";
        public static final String LIST = "list";
        public static final String LIMIT = "limit";

        public static final String USER_ID_SELECT = "user_id_select";
        public static final String USER_NAMECARD_SELECT = "user_namecard_select";
    }

    public static class GroupType {
        public static final String TYPE = "type";
        public static final String GROUP = "isGroup";
        public static final int PRIVATE = 0;
        public static final int PUBLIC = 1;
        public static final int CHAT_ROOM = 2;
        public static final int COMMUNITY = 3;

        public static final String TYPE_PUBLIC = "Public";
        public static final String TYPE_MEETING = "Meeting";
        public static final String TYPE_WORK = "Work";
        public static final String TYPE_COMMUNITY = "Community";
    }

    public static final class Group {

        public static final String GROUP_ID = "group_id";
        public static final String GROUP_INFO = "groupInfo";
        public static final String MEMBER_APPLY = "apply";
    }

}
