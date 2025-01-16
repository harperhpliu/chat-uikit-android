package com.tencent.qcloud.tuikit.tuichat.config;

import androidx.annotation.IntDef;

public class FriendConfig {
    private FriendConfig() {}

    private static final class FriendConfigHolder {
        private static final FriendConfig INSTANCE = new FriendConfig();
    }

    private static FriendConfig getInstance() {
        return FriendConfigHolder.INSTANCE;
    }

    public static final int UNDEFINED = -1;

    public static final int ALIAS = 1;
    public static final int MUTE_AND_PIN = 2;
    public static final int BACKGROUND = 3;
    public static final int BLOCK = 4;
    public static final int CLEAR_CHAT_HISTORY = 5;
    public static final int DELETE = 6;
    public static final int ADD_FRIEND = 7;

    @IntDef({ALIAS, MUTE_AND_PIN, BACKGROUND, BLOCK, CLEAR_CHAT_HISTORY, DELETE, ADD_FRIEND})
    private @interface Items {}

    private boolean showAlias = true;
    private boolean showMuteAndPin = true;
    private boolean showBackground = true;
    private boolean showBlock = true;
    private boolean showClearChatHistory = true;
    private boolean showDelete = true;
    private boolean showAddFriend = true;

    /**
     * Hide items in contact config interface.
     * @param items
     */
    public static void hideItemsInContactConfig(@Items int... items) {
        for (int itemType : items) {
            switch (itemType) {
                case ALIAS: {
                    getInstance().showAlias = false;
                    break;
                }
                case MUTE_AND_PIN: {
                    getInstance().showMuteAndPin = false;
                    break;
                }
                case BACKGROUND: {
                    getInstance().showBackground = false;
                    break;
                }
                case BLOCK: {
                    getInstance().showBlock = false;
                    break;
                }
                case CLEAR_CHAT_HISTORY: {
                    getInstance().showClearChatHistory = false;
                    break;
                }
                case DELETE: {
                    getInstance().showDelete = false;
                    break;
                }
                case ADD_FRIEND: {
                    getInstance().showAddFriend = false;
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    /**
     * Get whether to show alias setting.
     * @return
     */
    public static boolean isShowAlias() {
        return getInstance().showAlias;
    }

    /**
     * Get whether to show mute and pin setting.
     * @return
     */
    public static boolean isShowMuteAndPin() {
        return getInstance().showMuteAndPin;
    }

    /**
     * Get whether to show the chat background setting.
     * @return
     */
    public static boolean isShowBackground() {
        return getInstance().showBackground;
    }

    /**
     * Get whether to show block setting.
     * @return
     */
    public static boolean isShowBlock() {
        return getInstance().showBlock;
    }

    /**
     * Get whether to show clear chat history button.
     * @return
     */
    public static boolean isShowClearChatHistory() {
        return getInstance().showClearChatHistory;
    }

    /**
     * Get whether to show delete button.
     * @return
     */
    public static boolean isShowDelete() {
        return getInstance().showDelete;
    }

    /**
     * Get whether to show add friend button.
     * @return
     */
    public static boolean isShowAddFriend() {
        return getInstance().showAddFriend;
    }
}
