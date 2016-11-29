<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
         language="java" %>
<%
    pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
	String invite_code = request.getParameter("invite_code");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>邀请注册</title>
<link href="css/invite_register.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="ir_wrap">
	<h1 class="ir_titie">注册</h1>
    <form class="ir_form">
    	<ul class="ir_formul">
        	<li class="ir_formul1 clearfix">
            	<span class="ir_formul1s1 fl"><img src="images/invite_phone.png" /></span>
                <input class="fl ir_pone" type="text" placeholder="请输入注册手机号码" />
            </li>
            <!--验证-->
            <li class="yan_phone">请填写手机号</li>
            <li class="ir_formul2 clearfix">
            	<span class="ir_formul2s1 fl"><img src="images/invite_pwd.png" /></span>
                <input class="ir_formul2s2 fl ir_pwd" type="password" placeholder="请输入密码" />
                <span class="ir_formul2s3 fl"><img src="images/ic_look.png" /></span>
            </li>
            <!--验证-->
            <li class="yan_pwd">请输入密码</li>
            <li class="ir_formul3 clearfix">
            	<div class="ir_formul3d1 fl clearfix">
                	<span class="ir_formul3s1 fl"><img src="images/invite_message.png" /></span>
                    <input class="fl ir_yza" type="text" placeholder="短信验证码" />
                </div>
                <div class="ir_formul3d2 fr">
                	<input class="ir_formul3d2in" type="button" value="获取验证码"  />
                </div>
            </li>
            <!--验证-->
            <li class="yan_yza">请输入短信验证码</li>
            <li class="ir_formul4">
            	<div>邀请码：<span id='inviteCode'><%=invite_code %></span></div>
            </li>
            <li class="ir_formul5">
            	<button class="ir_formul5bt" type="button">注册</button>
            </li>
        </ul>
    </form>
    <div class="ir_xieyi clearfix">
    	<span class="ir_xieyispan"><img src="images/ic_agree_blue.png" /></span>同意<a href="${ctx}/explain/agreement.html">《用户注册协议》</a>
    </div>
    <div class="ir_down clearfix">
    	<a class="ir_down1 fl" href="${ctx}/android/kanleme.apk"><img src="images/register_android_download.png" /></a>
        <a class="ir_down2 fr" href="javascript:;"><img src="images/register_ios_download.png" /></a>
    </div>
    <div class="ir_wechat">
    	<img src="images/ic_code.png" />
        <p>关注微信公众号</p>
    </div>
</div>
<div class="zhezhao"></div>
<div class="tu_yan">
    <div class="clearfix">
        <img class="tu_yanicon fl" src="images/tu_yan_icon.png" />
        <input class="tu_yantext fl" type="text" placeholder="输入图中验证码" />
        <img id='imageVerifyCode' class="tu_yanimg fr" src="${ctx}/imagecode" />
    </div>
    <p class="tu_yanp clearfix">
    <input class="tu_yanp1 fl" type="button" value="确定"/>
    <i class="yan_tuyan">验证码错误</i>
    <span class="tu_yanp2 fr" onclick="loadImageCode()">换一张</span></p>
</div>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.placeholder.min.js"></script>
<script type="text/javascript" src="js/md5.js"></script>
<script type="text/javascript">
function jisuan(){
	var document_h=$(document).height();
	$('.zhezhao').css('height',document_h)
}
jisuan();

/*兼容*/
$('input, textarea').placeholder();

/*点击“同意《注册协议》”按钮*/
$('.ir_xieyispan').click(function(){
	if($('.ir_xieyispan img').attr('src')=='images/ic_agree_gray.png'){
		$('.ir_xieyispan img').attr('src','images/ic_agree_blue.png')
	}else{
		$('.ir_xieyispan img').attr('src','images/ic_agree_gray.png')
	}
})

/*点击遮罩层*/
$('.zhezhao').click(function(){
	$(this).hide();
	/*图形验证码消失*/
	$('.tu_yan').hide();
})

/*点击图形验证码*/
$('.tu_yanp1').click(function(){
	/* $('.yan_tuyan').show() */
	var inputVal = $('.tu_yantext').val().trim();
	if(inputVal == ''){
		$('.yan_tuyan').html("验证码不能为空");
		$('.yan_tuyan').show();
		return;
	}
	$('.zhezhao').hide();
	$('.tu_yan').hide();
	
	var ir_pone=$('.ir_pone').val().trim();
	var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 

	if(ir_pone==''){
		$('.yan_phone').css('display','block');
		$('.yan_phone').html('请填写手机号')
		return true;
	}else if(!myreg.test(ir_pone)){
		$('.yan_phone').css('display','block');
		$('.yan_phone').html('请填写正确的手机号')
		return true;
	}
	
	var requestParam = {
			"type" : "web_send_verify_code",
			'data' : '{' + '"type":3' + ',"account":' + '"'
            + ir_pone + '"'+ ',"phone_number":"", "web_verify_code" : "' + inputVal + '"' + '}'
		};
	
	$.ajax({
        url: "${ctx}/api",
        type: "get",
        data: requestParam,
        dataType: "json",
        success: function (data) {
        	if (data.status == "verify_code_error") {
        		$('.yan_yza').html("验证码错误");
        		$('.yan_yza').css('display','block');
                loadImageCode();
                return;
            }
            if (data.status == "ok") {
                $(".ir_formul3d2in").prop("disabled", true)
                        .css("background", "#A0A0A0");

                var time = 60;

                $(".ir_formul3d2in").html(--time + "秒后可以重新获取").show();

                var intervalVariable = setInterval(function () {
                    if (time == 0) {
                        clearInterval(intervalVariable);
                        $(".ir_formul3d2in").prop("disabled",
                                false).css("background",
                                "#47b663").html("获取验证码");
                    } else {
                        $(".ir_formul3d2in").html(time-- + "秒后可以重新获取").show();
                    }
                }, 1000);
            }else{
            	loadImageCode();
            	if (data.status == "phone_number_exits") {
                    $(".yan_phone").html("该电话号码已经被使用！").show();
                } else if (data.status == "not_exits") {
                    $(".yan_phone").html("电话号码不存在！").show();
                } else if (data.status == "repeat_error") {
                    alert("短时间内重复发送错误");
                } else {
                    alert("服务器内部错误，获取验证码失败"+data);
                }
            }
        },
        error: function () {
        	 alert("请求错误，请重试");
        }
    });
})

/**
 * 点击下一张
 */
function loadImageCode(){
	document.getElementById("imageVerifyCode").src=
		document.getElementById("imageVerifyCode").src+"?nocache="+new Date().getTime();
};

/*60秒倒计时获取验证码*/
var wait=60;  
function time(o) {  
	if (wait == 0) {  
		o.removeAttribute("disabled");            
		o.value="获取验证码";  
		wait = 60;  
	} else {  
		o.setAttribute("disabled", true);  
		o.value="重新发送(" + wait + ")";  
		wait--;  
		setTimeout(function() {  
			time(o)  
		},  
		1000)  
	}  
}
/*点击获取验证码*/
$('.ir_formul3d2in').click(function(){
	$('.zhezhao').show();
	$('.tu_yan').show();
	/*验证码输入成功后*/
	/*time(this);*/
})

/*密码字样显示隐藏*/
$('.ir_formul2s3 img').click(function(){
	if($(this).attr('src')=='images/ic_look.png'){
		$(this).attr('src','images/ic_lookac.png')
		$('input.ir_formul2s2').prop("type", "text");
	}else{
		$(this).attr('src','images/ic_look.png')
		$('input.ir_formul2s2').prop("type", "password");
	}
})
/*表单验证*/
$('.ir_formul5bt').click(function(){
	var ir_pone=$('.ir_pone').val().trim();
	var ir_pwd=$('.ir_pwd').val().trim();
	var ir_yza=$('.ir_yza').val().trim();
	var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 

	if(ir_pone==''){
		$('.yan_phone').css('display','block');
		$('.yan_phone').html('请填写手机号')
		return true;
	}else if(!myreg.test(ir_pone)){
		$('.yan_phone').css('display','block');
		$('.yan_phone').html('请填写正确的手机号')
		return true;
	}
	if(ir_pwd==''){
		$('.yan_pwd').css('display','block');
		$('.yan_pwd').html('请设置密码')
		return true;
	}
	if(ir_yza==''){
		$('.yan_yza').css('display','block');
		$('.yan_yza').html('请输入验证码')
		return true;
	}
	
	var invite_code = $("#inviteCode").val();
	var requestParam = {
            'type': 'register',
            'data': '{' + '"phone_number":' + '"'
            + ir_pone + '"'
            + ', "verify_code":' + '"' + ir_yza
            + '"' + ', "login_password":' + '"'
            + hex_md5(ir_pwd) + '"'
            + ', "invite_code":' + '"'
            + invite_code + '"' + '}'
        };
	
	  $.ajax({
          url: "${ctx}/api",
          type: "post",
          data: requestParam,
          dataType: "json",
          success: function (data) {
        	  
             if (data.status == "ok") {
              	alert("注册成功");    
              } else if (data.status == "verify_code_error") {
              	$(".yan_yza").html("验证码错误!");
              	$(".yan_yza").show();
              } else if (data.status == "invite_code_not_exits") {
              	 alert("邀请码不存在");
              } else if (data.status == "user_exits") {
                  alert("用户已存在");
              } else {
                  alert("服务器内部错误，注册失败"+data);
              }
          },
          error: function () {
              alert("请求错误，请重试");
          }
      });
	
})
$('.ir_pone').focus(function(){
	$('.yan_phone').css('display','none');
})
$('.ir_pwd').focus(function(){
	$('.yan_pwd').css('display','none');
})
$('.ir_yza').focus(function(){
	$('.yan_yza').css('display','none');
})
$('.tu_yantext').focus(function(){
	$('.yan_tuyan').css('display','none');
})
</script>
</body>
</html>
