package com.github.anicmv.contant;


import lombok.Getter;

/**
 * @author anicmv
 * @date 2025/3/30 17:48
 * @description 枚举
 */
@Getter
public enum XpEnum {
    XP_JK(BotConstant.CALLBACK_XP_JK, "JK"),
    XP_HS(BotConstant.CALLBACK_XP_HS, "黑丝"),
    XP_BS(BotConstant.CALLBACK_XP_BS, "白丝"),
    XP_TWIN_TAIL(BotConstant.CALLBACK_XP_TWIN_TAIL, "双马尾"),
    XP_DEFAULT(BotConstant.CALLBACK_XP_DEFAULT, "妹子");

    private final String callback;
    private final String description;

    XpEnum(String callback, String description) {
        this.callback = callback;
        this.description = description;
    }

    /**
     * 根据回调值查找对应的枚举
     *
     * @param callback 回调常量值
     * @return 找到的枚举，如果不存在则返回 null，可根据需要抛出异常
     */
    public static XpEnum fromCallback(String callback) {
        for (XpEnum xp : values()) {
            if (xp.getCallback().equals(callback)) {
                return xp;
            }
        }
        return XpEnum.XP_DEFAULT;
    }
}