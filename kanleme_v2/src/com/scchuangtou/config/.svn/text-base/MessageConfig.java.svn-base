package com.scchuangtou.config;

import java.text.MessageFormat;

import com.scchuangtou.model.UserMessageInfo;
import com.scchuangtou.utils.StringUtils;

public class MessageConfig {
	public static final String TITLE = "看了么公益";

	public static String getMessageDescription(UserMessageInfo info, String actionUsername) {
		switch (info.message_type) {
		case Config.MessageType.MESSAGE_TYPE_COMMENT:
			if (info.source_type == Config.MessageSourceType.MESSAGE_SOURCE_TYPE_ARTICLE) {
				return MessageFormat.format("[{0}]评论了你的帖子《{1}》", actionUsername, info.source_content);
			} else if (info.source_type == Config.MessageSourceType.MESSAGE_SOURCE_TYPE_SUBJECT) {
				return MessageFormat.format("[{0}]评论了你的专题《{1}》", actionUsername, info.source_content);
			} else if (info.source_type == Config.MessageSourceType.MESSAGE_SOURCE_TYPE_PROJECT) {
				return MessageFormat.format("[{0}]评论了你的项目《{1}》", actionUsername, info.source_content);
			}
		case Config.MessageType.MESSAGE_TYPE_COMMENT_REPLY:
			return MessageFormat.format("[{0}]回复了你的评论:{1}", actionUsername, info.action_content);
		case Config.MessageType.MESSAGE_TYPE_WITHDRAWALS_SUCCESS:
			String account = info.source_content;
			if (account != null && account.length() >= 5) {
				account = account.substring(account.length() - 4);
			}
			return MessageFormat.format("你的{0}元提现成功,零钱已打到尾号{1}的账户,手续费{2}元", info.message_content, account,
					info.action_content);
		case Config.MessageType.MESSAGE_TYPE_WITHDRAWALS_FAILD:
			return MessageFormat.format("你的{0}元提现失败:{1}", info.action_content, info.message_content);
		case Config.MessageType.MESSAGE_TYPE_USER_CERTIFICATION_PASS:
			return "你的认证申请已经通过啦";
		case Config.MessageType.MESSAGE_TYPE_USER_CERTIFICATION_NO_PASS:
			return MessageFormat.format("你的认证申请未通过:{0}", info.message_content);
		case Config.MessageType.MESSAGE_TYPE_PROJECT_PASS:
			return MessageFormat.format("你的互助项目\"{0}\"已经审核通过了", info.source_content);
		case Config.MessageType.MESSAGE_TYPE_PROJECT_NO_PASS:
			return MessageFormat.format("你的互助项目\"{0}\"申请未通过:{1}", info.source_content, info.message_content);
		case Config.MessageType.MESSAGE_TYPE_REWARD:
			if(StringUtils.emptyString(actionUsername)){
				return MessageFormat.format("你收到{0}元打赏", info.message_content);
			}else{
				return MessageFormat.format("\"{0}\"打赏了你:{1}元", actionUsername, info.message_content);
			}
		case Config.MessageType.MESSAGE_TYPE_JOIN_HEALTH_PROJECT:
			return MessageFormat.format("恭喜你,邀请好友{0},成功添加{1}人加入健康互助,获得{2}个红包", info.action_content,info.source_content,info.source_content);
		}
		return null;
	}
}
