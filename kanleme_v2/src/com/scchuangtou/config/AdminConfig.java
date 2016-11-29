package com.scchuangtou.config;

import java.util.HashMap;
import java.util.Map;

public class AdminConfig {
	public static final String LOGIN_URL = "/admin/login.jsp";
	public static final String INDEX_URL = "/admin/index.jsp";

	public static final String USER_NAME = "admin";
	public static final String EMAIL = "359512708@qq.com";
	public static final String PHONE_NUMBER = "028-85551823";
	public static final String DEFAULT_PASSWORD = "25f9e794323b453885f5181f1b624d0b";

	public static final String MODE_MANAGER_USER = "用户管理";
	public static final String MODE_MANAGER_HEALTH_PROJECT_ADMIN = "健康互助管理";
	public static final String MODE_MANAGER_CHILD_ADMIN = "管理员管理";
	public static final String MODE_MANAGER_ARTICLE = "帖子管理";
	public static final String MODE_MANAGER_CIRCLE = "圈子管理";
	public static final String MODE_MANAGER_SUBJECT = "专题管理";
	public static final String MODE_MANAGER_IDENTIFICATION = "认证管理";
	public static final String MODE_MANAGER_MESSAGE = "公告管理";
	public static final String MODE_MANAGER_BANNER = "Banner管理";
	public static final String MODE_MANAGER_FEEDBACK = "反馈信息";

	private static final Map<String, String> modes = new HashMap<>();

	static {
		modes.put(MODE_MANAGER_USER, "/admin/admin/userManage.jsp");
		modes.put(MODE_MANAGER_HEALTH_PROJECT_ADMIN, "/admin/admin/showHealthManage.jsp");
		modes.put(MODE_MANAGER_CHILD_ADMIN, "/admin/admin/adminManage.jsp");
		modes.put(MODE_MANAGER_ARTICLE, "/admin/admin/showArticlesNew.jsp");
		modes.put(MODE_MANAGER_CIRCLE, "/admin/admin/showCirclesNew.jsp");
		modes.put(MODE_MANAGER_SUBJECT, "/admin/admin/showSubjectsNew.jsp");
		modes.put(MODE_MANAGER_IDENTIFICATION, "/admin/admin/showIdentifications.jsp");
		modes.put(MODE_MANAGER_MESSAGE, "/admin/admin/showNotice.jsp");
		modes.put(MODE_MANAGER_BANNER, "/admin/admin/showBanners.jsp");
		modes.put(MODE_MANAGER_FEEDBACK, "/admin/admin/showFeedback.jsp");
	}

	public static final Map<String, String> getAdminModes() {
		return modes;
	}
}
