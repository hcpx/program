package com.scchuangtou.config;

public class Config {
	public static final String CHARSET = "utf-8";

	public static final String REQUEST_PARAMETER_TYPE = "type";
	public static final String REQUEST_PARAMETER_DATA = "data";
	
	public static final boolean BACKGROUND_IS_SINGLE_LOGIN = false;//后台是否单点登录

	public static final String STATUS_OK = "ok";
	public static final String STATUS_USER_SEQUESTRATION = "user_sequestration";// 用户未解封
	public static final String STATUS_SERVER_ERROR = "server_error";// 服务器错误
	public static final String STATUS_PARAMETER_ERROR = "parameter_error";// 请求参数错误
	public static final String STATUS_TOKEN_ERROR = "token_error";// 登录过期
	public static final String STATUS_VERIFY_CODE_ERROR = "verify_code_error";// 验证码错误
	public static final String STATUS_USER_EXITS = "user_exits";// 用户已经存在
	public static final String STATUS_NOT_EXITS = "not_exits";// 不存在
	public static final String STATUS_ACCOUNT_ERROR = "account_error";// 账户错误
	public static final String STATUS_PERMISSION_ERROR = "permission_error";// 权限错误
	public static final String STATUS_PASSWORD_ERROR = "password_error";// 密码错误
	public static final String STATUS_REPEAT_ERROR = "repeat_error";// 重复提交
	public static final String STATUS_PHONE_NUMBER_EXITS = "phone_number_exits";// 电话已被注册
	public static final String STATUS_NOT_BIND_PHONE = "not_bind_phone";// 没有绑定手机
	public static final String STATUS_GOLD_LACK = "gold_lack";// 金币不足
	public static final String STATUS_INVITE_CODE_NOT_EXITS = "invite_code_not_exits";// 邀请码不存在
	public static final String STATUS_CLOSE_TOPUP = "colse_topup";// 充值关闭
	public static final String STATUS_TOPUP_NOT_SUCCESS = "topup_not_success";// 充值未成功
	public static final String STATUS_THIRD_LOGIN_ERROR = "third_login_error";// 第三方登录，不能找回密码
	public static final String STATUS_INTEGRAL_WALL_LESSMIN = "integral_wall_lessmin";// 小于最小兑换值
	public static final String STATUS_INTEGRAL_WALL_MOREMAX = "integral_wall_moremax";// 超过每日最大兑换值
	public static final String STATUS_INTEGRAL_WALL_NOTENOUGH = "integral_not_enough";// 积分不足
	public static final String STATUS_NOT_CORRECT = "not_correct";// 只能是未认证才可进行修改认证
	public static final String STATUS_NOT_IDENTIFICATION = "not_identification";// 没有认证
	public static final String STATUS_RED_PACKET_OVER = "redpacket_over";//红包已领取完
	public static final String STATUS_RED_PACKET_UNABLE = "redpacket_unable";//不能在领取红包
	public static final String STATUS_COMMAND_ERROR = "command_error";// 口令错误
	public static final String STATUS_OVERDUE = "overdue";//过期
	public static final String STATUS_ID_CARD_ERROR = "idcar_error";//身份证不符合加入互助项目要求
	public static final String STATUS_BANNED = "banned";//被禁言
	public static final String STATUS_STRING_INCLUDE_EMOJI = "include_emoji";//字符串带颜文字
	public static final String STATUS_BAN_WITHDRAWALS = "ban_withdrawals";// 禁止提现
	

	public static final long COMMENT_INTERVAL_TIME = 60;// 评论间隔时间(单位秒)
	public static final int ONCE_QUERY_COUNT = 20;// 单次查询数据条数
	public static final int INVITE_CODE_LEN = 6;// 邀请码长度
	public static final int SMS_VERIFY_CODE_LEN = 4;// 短信验证码长度

	public static class CertificationStauts {
		public static final int CERTIFICATION_STATUS_NORMAL = 0;// 未认证
		public static final int CERTIFICATION_STATUS_AUDIT = 1;// 认证中
		public static final int CERTIFICATION_STATUS_PASS = 2;// 认证通过
		public static final int CERTIFICATION_STATUS_NO_PASS = -1;// 认证失败
	}

	public static class ThirdLoginType{
		public static final int THIRD_LOGIN_TYPE_SINA = 1;
		public static final int THIRD_LOGIN_TYPE_WECHAT = 2;
		public static final int THIRD_LOGIN_TYPE_QQ = 3;
	}
	
	public static class RewardObjectType {
		public static int Article = 1;//帖子
	}
	
	public static class PayType {
		public static int AliPay = 0;
		public static int WeChatPay = 1;
		public static int UnionPay = 2;
	}

	public static final class TopUpPurpose {// 充值目的
		public static final int TOP_UP_PURPOSE_NORMAL = 0;// 账户充值
		public static final int TOP_UP_PURPOSE_ARTICLE_REWARD = 1;// 帖子打赏
	}

	public static class IntegralWallStatusType {
		public static int INTEGRAL_WALL_WAIT = 0; // 等待状态
		public static int INTEGRAL_WALL_COMPLETE = 1; // 完成状态
	}

	public static class TopUpOrderStatusType {
		public static int WAIT_BUYER_PAY = 0;// 待支付
		public static int TRADE_FINISHED = 2;// 交易成功
		public static int TRADE_CLOSED = -1;// 交易关闭
	}

	public static class IdentificationStatusType {
		public static int IDENTIFICATION_PERSON = 0; // 个人认证
		public static int IDENTIFICATION_ORGANIZATION = 1; // 组织认证
	}

	public static final class BannerType {
		public static final int BANNER_TYPE_MAIN = 0;
	}

	public static final class BannerSourceType {
		public static final int BANNER_SOURCE_TYPE_WEB = 1;
		public static final int BANNER_SOURCE_TYPE_ARTICLE = 2;
		public static final int BANNER_SOURCE_TYPE_SUBJECT = 3;
		public static final int BANNER_source_TYPE_PROJECT = 4;
	}

	public static final class VerifyCodeType {
		public static final int FIND_PASSWORD = 1;
		public static final int VERIFY_USER = 2;
		public static final int REGISTER = 3;
		public static final int VERIFY = 4;
	}

	public static class Os {
		public static final int ALL = 0;
		public static final int ANDROID = 1;
		public static final int IOS = 2;
	}

	public static final class GoldChangeType {
		public static final int GOLD_CHANGE_TYPE_TOPUP = 0;//充值
		public static final int GOLD_CHANGE_TYPE_WITHDRAWALS = 1;// 提现
		public static final int GOLD_CHANGE_TYPE_WITHDRAWALS_FAILD = 2;// 提现失败金币返回
		public static final int GOLD_CHANGE_TYPE_INTEGRAL = 3;// 积分兑换
		public static final int GOLD_CHANGE_TYPE_PROJECT_DONATIONS = 4;// 项目捐助
		public static final int GOLD_CHANGE_TYPE_PROJECT_WITHDRAWALS = 5;// 项目提现
		public static final int GOLD_CHANGE_TYPE_PUBLISH_RED_PACKET = 6;// 发布红包
		public static final int GOLD_CHANGE_TYPE_GET_RED_PACKET = 7;// 获取红包
		public static final int GOLD_CHANGE_TYPE_REWARD = 8;// 打赏
		public static final int GOLD_CHANGE_TYPE_HEALTH_PAY = 9;// 健康互助扣款
		public static final int GOLD_CHANGE_TYPE_SIGIN = 10;// 签到奖励
	}

	public static final class GoldType {
		public static final int GOLD_TYPE_NORMAL = 0;
		public static final int GOLD_TYPE_BIND = 1;
	}

	public static final class MessageType {
		public static final int MESSAGE_TYPE_SYSTEM = -1;// 系统消息
		public static final int MESSAGE_TYPE_WITHDRAWALS_SUCCESS = 1;// 提现成功
		public static final int MESSAGE_TYPE_WITHDRAWALS_FAILD = 2;// 提现失败
		public static final int MESSAGE_TYPE_COMMENT = 3;// 评论
		public static final int MESSAGE_TYPE_COMMENT_REPLY = 4;// 评论回复
		public static final int MESSAGE_TYPE_USER_CERTIFICATION_PASS = 5;// 认证通过
		public static final int MESSAGE_TYPE_USER_CERTIFICATION_NO_PASS = 6;// 认证失败
		public static final int MESSAGE_TYPE_PROJECT_PASS = 7;// 项目审核通过
		public static final int MESSAGE_TYPE_PROJECT_NO_PASS = 8;// 项目审核失败
		public static final int MESSAGE_TYPE_REWARD = 9;// 打赏
		public static final int MESSAGE_TYPE_JOIN_HEALTH_PROJECT = 10;// 加入健康互助邀请
	}

	public static class MessageSourceType {
		public static int MESSAGE_SOURCE_TYPE_ARTICLE = 1;// 帖子
		public static int MESSAGE_SOURCE_TYPE_SUBJECT = 2;// 专题
		public static int MESSAGE_SOURCE_TYPE_WITHDRAWALS = 3;// 提现
		public static int MESSAGE_SOURCE_TYPE_PROJECT = 4;// 互助项目
		public static int MESSAGE_SOURCE_TYPE_RED_PACKET = 5;// 红包
		public static int MESSAGE_SOURCE_TYPE_ACTIVITY = 6;// 活动
	}

	public static class CommentObjectType {
		public static int COMMENT_OBJECT_TYPE_ARTICLE = 1;// 帖子
		public static int COMMENT_OBJECT_TYPE_SUBJECT = 2;// 专题
		public static int COMMENT_OBJECT_TYPE_PROJECT = 3;// 互助项目
		public static int COMMENT_OBJECT_TYPE_ACTIVITY = 4;// 活动
	}

	public static class CollectObjectType {
		public static int COLLECT_OBJECT_TYPE_ARTICLE = 1;// 帖子
		public static int COLLECT_OBJECT_TYPE_SUBJECT = 2;// 专题
		public static int COLLECT_OBJECT_TYPE_PROJECT = 3;// 互助项目
		public static int COLLECT_OBJECT_TYPE_ACTIVITY = 4;// 活动
	}

	public static class ReportObjectType {
		public static int REPORT_OBJECT_TYPE_ARTICLE = 1;// 帖子
		public static int REPORT_OBJECT_TYPE_SUBJECT = 2;// 专题
		public static int REPORT_OBJECT_TYPE_PROJECT = 3;// 互助项目
		public static int REPORT_OBJECT_TYPE_ACTIVITY = 4;// 活动
	}

	public static final class BrowseType {
		public static final int BROWSE_TYPE_ARTICLE = 1;// 帖子
		public static final int BROWSE_TYPE_SUBJECT = 2;// 专题
		public static final int BROWSE_TYPE_PROJECT = 3;// 互助项目
		public static final int BROWSE_TYPE_ACTIVITY = 4;// 活动
	}

	public static final class PraiseType {
		public static final int PRAISE_TYPE_ARTICLE = 1;// 帖子
		public static final int PRAISE_TYPE_SUBJECT = 2;// 专题
		public static final int PRAISE_TYPE_PROJECT = 3;// 互助项目
		public static final int PRAISE_TYPE_COMMENT = 4;// 评论
	}

	public static final class CapitalType {
		public static final int CAPITAL_TYPE_PRIVATE = 0;// 对私
		public static final int CAPITAL_TYPE_PUBLIC = 1; // 对公
	}

	public static final class ArticleSearch {
		public static final int ARTICLE_SEARCH_NEW = 0;// 最新
		public static final int ARTICLE_SEARCH_HOT = 1; // 最热
	}

	public static final class CapitalSourceType {
		public static final int CAPITAL_SOURCE_TYPE_BANK = 0; // 银行
		public static final int CAPITAL_SOURCE_TYPE_WECHAT = 1; // 微信
		public static final int CAPITAL_SOURCE_TYPE_ALIPAY = 2; // 支付宝
	}
	
	public static final class PacketType {
		public static final int PACKET_TYPE_COMPANY = 1; // 企业红包
		public static final int PACKET_TYPE_INNER = 2; // 看了么红包
		public static final int PACKET_TYPE_HEALTH = 3; // 加入健康互助红包
	}
	
	public static final class PacketCommandType {
		public static final int PACKET_COMMAND_TYPE_COMMAND = 0; // 口令红包
		public static final int PACKET_COMMAND_TYPE_NORMAL = 1; // 普通红包
	}
	
	public static final class PacketReciveType {
		public static final int PACKET_RECIVE_TYPE_ONCE = 0; // 只能抢一次
		public static final int PACKET_RECIVE_TYPE_DAY = 1; // 每天只能抢一次
	}
	
	public static final class PromoCodePartitionType {
		public static final int PROMO_CODE_PARTITION_TYPE_AVERAGE = 0; // 平均分配
		public static final int PROMO_CODE_PARTITION_TYPE_RANDOM = 1;  // 随机分发
	}
	
	public static final class PromoCodeType {
		public static final int PROMO_CODE_TYPE_VOLUME = 1; // 抵扣卷
	}
			
	public static class CarouselRedPacketType {                   // 红包轮播信息
		public static int CAROUSEL_RED_PACKET_TYPE_GRAD = 0;     // 抢红包信息
		public static int CAROUSEL_RED_PACKET_TYPE_PUBLISH = 1;    // 发红包信息
	}

	public static class WithdrawalsType {
		public static int WITHDRAWALS_NO_PASSED = 2;// 审核未通过
		public static int WITHDRAWALS_PASSED = 1;// 通过
		public static int WITHDRAWALS_APPLYING = 0;// 待审核
	}

	public static class WithdrawalsCashBackOrNot {// 提现审核失败是否返回金币
		public static int WITHDRAWALS_NO_CASH_BACK = 0;// 不返回金币
		public static int WITHDRAWALS_CASH_BACK = 1;// 返回金币
	}

	public static class HelpEachType { // 大病互助状态
		public static int HELP_EACH_TYPE_APPLYING = 0; // 待审核
		public static int HELP_EACH_TYPE_NO_PASSED = 1; // 审核不通过
		public static int HELP_EACH_TYPE_PASSED = 2;   // 通过(进行中)
		public static int HELP_EACH_TYPE_END = 3;      // 进行完毕已下架
	}
	
	public static final class HelpEachTSearch {
		public static final int PROJECT_SEARCH_PROCEED = 0;// 进行中
		public static final int PROJECT_SEARCH_SOLDOUT = 1; // 下架
		public static final int PROJECT_SEARCH_DRAFT= 2; // 草稿
	}
	
	public static final class HealthProjectPayType {        
		public static final int HEALTH_PROJECT_PAY_TYPE_KANBI = 0;   // 看币充值
		public static final int HEALTH_PROJECT_PAY_TYPE_PAID = 1;  // 捐助
		public static final int HEALTH_PROJECT_PAY_TYPE_WECHAT = 2;   // 微信充值
		public static final int HEALTH_PROJECT_PAY_TYPE_ALIPAY = 3;   // 支付宝充值
	}
	
	public static final class HealthProjectType {//目前只有两个互助项目写死id，需要新增的时候更改接口,规则不通用
		public static final int HEALTH_PROJECT_TYPE_CHILDREN = 0;   //儿童健康互助
		public static final int HEALTH_PROJECT_TYPE_YOUTH = 1;       //中青年健康互助
		public static final String HEALTH_PROJECT_TYPE_CHILDREN_ID = "0";   //儿童健康互助
		public static final String HEALTH_PROJECT_TYPE_YOUTH_ID = "1";      //中青年健康互助
	}

	public static class EncryptionKey {
		public static String verifyCodeKey = "1a330b9f34e5d5dd9e24c92235156e66";
	}

	public static class GoldGiving {
		public static final float TWITHDRAWALS_SHARE_GIVING = 0.2f;
	}

	public static class GrowthGivingMax {
		public static final int ADD_ARTICLE = 5;// 发表帖子
		public static final int ADD_COMMENT = 10;// 评论
		public static final int ADD_COMMENT_REPLY = 20;// 评论的回复
		public static final int JOIN_CIRCLE = 10;// 加入圈子
		public static final int INTEGRALWALL_CHANGE_MIN = 1000;// 积分墙最低兑换份额
		public static final int INTEGRALWALL_CHANGE_MAX = 10000;// 积分墙每日最大兑换量
	}

	public static class GrowthGiving {
		public static final int INVITE_USER = 500;// 邀请好友
		public static final int REGISTER = 30;// 注册绑定手机
		public static final int UPLOAD_HEAD = 20;// 首次上传头像
		public static final int COMPLETE_USERINFO = 20;// 完善个人资料
		public static final int SIGIN = 10;// 签到
		public static final int ADD_ARTICLE = 10;// 发表帖子
		public static final int FIRST_COMMENT = 50;// 评论沙发
		public static final int ADD_COMMENT = 5;// 评论
		public static final int ADD_COMMENT_REPLY = 1;// 评论的回复
		public static final int FIRST_ARTICLE = 50;// 首次发帖
		public static final int RECEIVED_THUMB_UP = 1;// 收到一个点赞
		public static final int RECEIVED_COMMENT = 1;// 收到一个评论
		public static final int JOIN_CIRCLE = 2;// 加入圈子
	}
	
	public static final class SiginGold {        
		public static final float SIGIN_GOLD_MIN = 0.01f;  // 最小随机金额
		public static final float SIGIN_GOLD_MAX = 0.5f;   // 最大随机金额
	}

	public static final int getLevel(long growth) {
		if (growth <= 1000) {
			return 1;
		} else if (growth <= 4000) {
			return 2;
		} else if (growth <= 20000) {
			return 3;
		} else if (growth <= 50000) {
			return 4;
		} else if (growth <= 80000) {
			return 5;
		} else if (growth <= 120000) {
			return 6;
		} else if (growth <= 160000) {
			return 7;
		} else if (growth <= 2000000) {
			return 8;
		} else if (growth <= 2500000) {
			return 9;
		} else {
			return 10;
		}
	}

	public static final float parseGold(float gold) {
		if (gold == 0) {
			return gold;
		}
		return Float.valueOf(String.format("%.2f", gold));
	}
	
	public static final class Withrawals{
		public static final int WITHRAWALS_MIN_GOLD = 100;//最低提现金额
		public static final float WITHRAWALS_POUNDAGE = 0.03f;//手续费
		public static final float WITHRAWALS_MONTH_COUNT = 2;//当月提现总数
	}

}
