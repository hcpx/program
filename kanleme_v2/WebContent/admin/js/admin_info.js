function admin_login(data) {
	var value = JSON.stringify(data);
	$.cookie("userinfo", value, {
		expires : 7
	});
	sessionStorage.setItem('userinfo', value);
}
function admin_logout() {
	var valu = "";
	$.cookie("userinfo", value, {
		expires : 7
	});
	sessionStorage.setItem('userinfo', value);
}
function getUserInfo() {
	var userinfo = $.cookie("userinfo");
	if (!userinfo || userinfo =='') {
		userinfo = sessionStorage.getItem("userinfo");
	}
//	return eval('(' + userinfo + ')');
	 return new Function("return" + userinfo)();
}
function getUserModes() {
	var userinfo = getUserInfo();
	if (userinfo) {
		return userinfo.modes;
	} else {
		return null;
	}
}