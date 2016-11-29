<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
	long ctime = System.currentTimeMillis();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>国立看了么管理后台</title>
<link href="${ctx}/admin/css/adminv1.css" type="text/css" rel="stylesheet" />
</head>

<body>
<!--首页左边-->
<div class="wrap">
    <!--首页右边-->
    <div class="wrap_r fl">
        <div class="actin">
        	<!--头部-->
        	<div class="actin_top">
            	<h2>管理员管理</h2>
                <p>首页/管理员管理</p>
            </div>
            <!--列表-->
            <div class="actin_list">
            	<p class="actin_list1">所有管理员</p>
                <div class="actin_list2">
                	<a class="actin_list2_1" href="javascript:;" onclick="window.location.reload();">刷新</a>
                    <a class="actin_list2_2 xin_guanzi" href="javascript:;">新增管理员</a>
                    <input class="actin_list2_3" type="text" placeholder="请输入关键字" />
                    <button class="actin_list2_4">查询</button>
                </div>
                <!--table内容-->
                <div class="actin_tabdiv">

                <table cellpadding="0" cellspacing="0" class="actin_table">
                	<!--头部-->
                	<thead class="actin_tableh">
                        <tr>
                          <th style="width:10%">用户名</th>
                          <th style="width:20%">电话号码</th>
                          <th style="width:20%">邮箱</th>
                           <th style="width:35%">权限</th>
                          <th style="width:15%">编辑</th>
                        </tr>
                      </thead>
                      <!--中间内容-->
                      <tbody class="actin_tableb clearfix">
                       		
                      </tbody>
                </table>
                </div>
				<div class="yhe_morew"><button class="yhe_more" style="display:none">查看更多</button></div>
            </div>
        </div> 
    </div>
</div>
<!--弹层-->
<div class="zhezhao"></div>
<!--点击"新增管理员"——>'新增管理员'弹层-->
<div class="xin_guan" style="height:auto;">
	<div class="actinttop clearfix"><span class="fl" id="admin_title">添加管理员</span><img class="fr" src="../images/close.png" /></div>
    <form class="xin_guanf">
    	<div class="clearfix">
        	<div class="xin_guanf1 fl">
            	角色：<span>用户管理员</span>
            </div>
        </div>
        <div class="clearfix">
        	<div class="xin_guanf3 fl">
            	<label>用户名</label>
                <input id="user_name" type="text"/>
            </div>
            <div class="xin_guanf4 fl">
            	<label>密码</label>
                <input id="pwd" type="password"/>
            </div>
            <div class="xin_guanf3 fl">
            	<label>电话号码</label>
                <input id="phone_number" type="text"/>
            </div>
            <div class="xin_guanf4 fl">
            	<label>邮箱</label>
                <input id="email" type="text"/>
            </div>
        </div>
        <p class="xin_guanf11">注：用户名验证唯一性<br />推荐人默认为当前登录用户</p>
        <!--权限列表-->
        <div class="actin_list">
        	<p class="actin_list1">权限列表</p>
            <div class="qx_div1 clearfix qx_div">
            	
            </div>
        </div>
        <div class="actintf_caozuo">
        	<input class="actintf_caozuo1 xinguan_que" type="button" value="确定" />
            <input class="actintf_caozuo2 xinguan_qu"type="button" value="关闭" />
        </div>
    </form>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/adminv1.js"></script>
<script src="${ctx}/js/jquery.cookie.js"></script>
<script src="${ctx}/js/md5.js"></script>
<script src="${ctx}/js/json2.js"></script>
<script type="text/javascript">
	/*点击"新增管理员"——>'新增管理员'弹层*/
	$('.xin_guanzi').click(function(){
		$('.zhezhao').fadeIn();
		$('.xin_guan').fadeIn();
		$('.xin_yong').fadeOut();
		/*清空弹窗内的内容*/
		$('.xin_guanf')[0].reset();
		$('#xin_guanf10_img').prop('src','');
		var power=eval($.cookie("menue"));
		var ulhtml="";
		if(power){
			for(var i=0;i<power.length;i++){
				ulhtml+="<ul class='fl'><li class='qx_quan'><input class='d_quansejs' type='checkbox' value='"+power[i].name+"'/>"+power[i].name+"</li></ul>";
    		}
		}
		$(".qx_div1").html(ulhtml);
		$("#user_name").attr("readonly",false);
		$("#pwd").attr("placeholder","");
		$("#admin_title").text("添加管理员");
	})	
	/*点击"确定"关闭弹层-新增管理员*/
	function addAdmin(user_name,pwd,phone_number,email,powers) {
       var requestParams = {
           "type": "add_child_admin",
           'data': '{'
               + '"admin_token":"",'
               + '"user_name":"'+user_name+'",'
               + '"phone_number":"'+phone_number+'",'
               + '"email":"'+email+'",'
               + '"password":"' + hex_md5(pwd) + '",'
               + '"modes":'+powers+','
               + '}'
       };
       $.ajax({
           url: "${ctx}/adminapi",
           type: "post",
           data: requestParams,
           dataType: "json",
           success: function (data) {
           if (data != null && data.status == "ok") {
         	    alert("添加管理员成功。");
         	    location.reload();
            }else if (data != null && data.status == "server_error") {
				alert("服务器内部错误。");
			}else if (data != null && data.status == "parameter_error") {
				alert("发送参数错误。");
			}else if (data != null && data.status == "permission_error") {
				alert("没有权限。");
			}else if (data != null && data.status == "user_exits") {
				alert("此用户已存在，请更换用户名。");
			}else if (data != null && data.status == "token_error") {
				alert("登陆超时,请重新登陆。");
			}
           	$('.zhezhao').fadeOut();
  			$('.xin_guan').fadeOut();
           },
           error: function () {
        	   alert("服务器内部错误。");
        	   $('.zhezhao').fadeOut();
      		   $('.xin_guan').fadeOut();
           }
       });
   	}
	
	function getPowers() {
		var powers = [];
		$('.d_quansejs[type=checkbox]').each(function(){
            if($(this).is(':checked')){
            	powers.push($(this).val());
            }
        });
        return powers;
	};
	
	/*点击"取消"关闭弹层-新增管理员*/
	$('.xinguan_qu').click(function(){
		$('.zhezhao').fadeOut();
		$('.xin_guan').fadeOut();
	})

	function disables(e) {
		var name = $(e).attr("name");
		var status = $(e).parent().parent().find("[name=status]").text();
		if(status==-1){
			status=1;
		}else{
			status=-1;
		}
		var admin_name = $(e).parent().parent().find("[name=admin_name]").text();
		var requestParams = {
		           "type": "edit_child_admin",
		           'data': '{'
		               + '"admin_token":"",'
		               + '"user_name":"'+admin_name+'",'
		               + '"phone_number":"",'
		               + '"email":"",'
		               + '"password":"",'
		               + '"modes":[],'
		               + '"status":'+status
		               + '}'
		       };
		    $.ajax({
		        url: "${ctx}/adminapi",
		        type: "post",
		        data: requestParams,
		        dataType: "json",
		        success: function (data) {
		        if (data != null && data.status == "ok") {
		        	alert("操作成功!!!");
		            if (name == '禁用'){
		                $(e).parent().parent().find("[name='禁用']").html("已禁用");
		                $(e).parent().parent().find("[name='禁用']").css({"background-color":"#cd0a0a","color":"#ffffff"});
		                $(e).attr("name", "已禁用");
		            }else {
		                $(e).parent().parent().find("[name='已禁用']").html("禁用");
		                $(e).parent().parent().find("[name='已禁用']").css({"background-color":"#ffffff", "color":"#000000"});
		                $(e).attr("name", "禁用");
		            }
		         }else if (data != null && data.status == "server_error") {
						alert("服务器内部错误。");
					}else if (data != null && data.status == "parameter_error") {
						alert("发送参数错误。");
					}else if (data != null && data.status == "permission_error") {
						alert("没有权限。");
					}else if (data != null && data.status == "token_error") {
						alert("登陆超时,请重新登陆。");
					}
		        	$('.zhezhao').fadeOut();
					$('.xin_guan').fadeOut();
		        },
		        error: function () {
		     	   alert("服务器内部错误。");
		     	   $('.zhezhao').fadeOut();
		   		   $('.xin_guan').fadeOut();
		        }
		    }); 
	};
	
	function edit(e) {
	    var admin_name = $(e).parent().parent().find("[name=admin_name]").text();
	    var admin_phone = $(e).parent().parent().find("[name=admin_phone]").text();
	    var admin_email = $(e).parent().parent().find("[name=admin_email]").text();
	    var admin_modes = $(e).parent().parent().find("[name=admin_modes]").text();
	    var modes=[];
	    modes=admin_modes.split(",");
		$('.zhezhao').fadeIn();
		$('.xin_guan').fadeIn();
		$('.xin_yong').fadeOut();
		/*清空弹窗内的内容*/
		$('.xin_guanf')[0].reset();
		$('#xin_guanf10_img').prop('src','');
		$("#user_name").attr("readonly",true);
		$("#pwd").attr("placeholder","不填写为保持原密码不变");
		$("#admin_title").text("修改管理员");
		$("#user_name").val(admin_name);
        $("#phone_number").val(admin_phone);
       	$("#email").val(admin_email);
		var power=eval($.cookie("menue"));
		var ulhtml="";
		if(power){
			for(var i=0;i<power.length;i++){
				if(modes){
					var count=0;
					var len=modes.length;
					for(var j=0;j<len;j++){
						if(modes[j]==power[i].name){
							ulhtml+="<ul class='fl'><li class='qx_quan'><input class='d_quansejs' type='checkbox' checked='true' value='"+power[i].name+"'/>"+power[i].name+"</li></ul>";
							break;
						}
						count++;
					}
					if(count==len){
						ulhtml+="<ul class='fl'><li class='qx_quan'><input class='d_quansejs' type='checkbox' value='"+power[i].name+"'/>"+power[i].name+"</li></ul>";
					}
				}
    		}
		}
		$(".qx_div1").html(ulhtml);
	};
	
	$('.xinguan_que').click(function(){
		var admin_title=$("#admin_title").text();
		if(admin_title=='添加管理员'){
			var user_name = $("#user_name").val();
	        if (user_name == '') {
	            alert('请输入用户名')
	            return;
	        }
	        var pwd = $("#pwd").val();
	        if (pwd == '') {
	            alert('请输入密码')
	            return;
	        }
	        var phone_number = $("#phone_number").val();
	        if (phone_number == '') {
	            alert('请输入电话号码')
	            return;
	        }
	        var email = $("#email").val();
	        if (email == '') {
	            alert('请输入电子邮箱')
	            return;
	        }
	        var powers=getPowers();
			addAdmin(user_name,pwd,phone_number,email,JSON.stringify(powers));
		}else if(admin_title=='修改管理员'){
			var user_name = $("#user_name").val();
	        var pwd = $("#pwd").val();
	        var phone_number = $("#phone_number").val();
	        if (phone_number == '') {
	            alert('请输入电话号码')
	            return;
	        }
	        var email = $("#email").val();
	        if (email == '') {
	            alert('请输入电子邮箱')
	            return;
	        }
	        var powers=JSON.stringify(getPowers());
	        modify(user_name,pwd,phone_number,email,powers);
		}
	});
	
	function modify(user_name,pwd,phone_number,email,powers){
		var password="";
		if(pwd)
			password=hex_md5(pwd);
		var requestParams = {
		           "type": "edit_child_admin",
		           'data': '{'
		               + '"admin_token":"",'
		               + '"user_name":"'+user_name+'",'
		               + '"phone_number":"'+phone_number+'",'
		               + '"email":"'+email+'",'
		               + '"password":"' + password + '",'
		               + '"modes":'+powers+','
		               + '}'
		       };
       $.ajax({
           url: "${ctx}/adminapi",
           type: "post",
           data: requestParams,
           dataType: "json",
           success: function (data) {
           if (data != null && data.status == "ok") {
         	    alert("修改子账户成功。");
         	    location.reload();
            }else if (data != null && data.status == "server_error") {
				alert("服务器内部错误。");
			}else if (data != null && data.status == "parameter_error") {
				alert("发送参数错误。");
			}else if (data != null && data.status == "permission_error") {
				alert("没有权限。");
			}else if (data != null && data.status == "token_error") {
				alert("登陆超时,请重新登陆。");
			}
           	$('.zhezhao').fadeOut();
  			$('.xin_guan').fadeOut();
           },
           error: function () {
        	   alert("服务器内部错误。");
        	   $('.zhezhao').fadeOut();
      		   $('.xin_guan').fadeOut();
           }
       });
	}

	$(function () {
	    var begin = 0;
	    var data = "";
	    var key_word="";
	    /****************************************************/
	    /*点击时间查询*/
	    $('.actin_list2_4').click(function () {
	        var kw = $(".actin_list2_3").val();
	        if (kw == '') {
	            alert('请输入查询关键字')
	            return;
	        }
	        begin = 0;
	        data = "";
	        key_word=kw;
	        $(".actin_tableb").empty();
	        loadData(begin, key_word, loadDataCallback);
	    });
	
	    loadData(begin, key_word, loadDataCallback);
	    
	    function loadData(begin,key_word, callBack) {
	        var requestParams = {
	            "type": "list_child_admin",
	            'data': '{' + '"begin":' + begin + ','+ '"key_word":"' + key_word
	            + '"}'
	        };
	        $("#loading").html("<center><td colspan='8' style='text-align:center'>加载数据中，请稍后......</td></center>");
	        $.ajax({
	            url: "${ctx}/adminapi",
	            type: "post",
	            data: requestParams,
	            dataType: "json",
	            success: function (data) {
	                $("#loading").empty();
	                if (data != null && data.status == "ok") {
	                    loadDataCallback(data);
	                }else if (data != null && data.status == "server_error") {
						alert("服务器内部错误。");
					}else if (data != null && data.status == "parameter_error") {
						alert("发送参数错误。");
					}else if (data != null && data.status == "permission_error") {
						alert("没有权限。");
					}else if (data != null && data.status == "token_error") {
						alert("登陆超时,请重新登陆。");
					}
	            },
	            error: function () {
	                $("#loading").empty();
	            }
	        });
	    }
	
	    function loadDataCallback(data) {
	        var hasMoreData = data.has_more_data;
	        var datas = data.datas;
	        var len = datas.length;
	        if (datas != null && datas != undefined && len > 0) {
	            begin = begin+len;
	            for (var i = 0; i < len; i++) {
	                data += "<tr><td name='admin_name'>" + datas[i].user_name + "</td>";
	                data += "<td name='admin_phone'>" + datas[i].phone_number + "</td>";
	                data += "<td name='admin_email'>" + datas[i].email + "</td>";
	                data += "<td name='admin_modes'>" + datas[i].modes + "</td>";
	                data += "<td name='status' style='display:none'>" + datas[i].status + "</td>";
	                if (datas[i].status != -1) {
	                    data += "<td style='text-align:center'><button class='btn_disables' onclick='edit(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>修改</button>" +
	                            "<button id='btn_disables' class='btn_disables' href='javascript:void(0)' name='禁用' onclick='disables(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>禁用</button> " +
	                            "</td>";
	                } else {
	                    data += "<td style='text-align:center'><button class='btn_disables' onclick='edit(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>修改</button>" +
	                            "<button class='btn_disables' href='javascript:void(0)' name='已禁用' onclick='disables(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;background-color: #cd0a0a;color: #ffffff;'>已禁用</button> " +
	                            "</td>";
	                }
	                data += "</tr>";
	            }
	            $(".actin_tableb").append(data);
	        }
	
	        if (hasMoreData) {
	            $(".yhe_more").css('display', 'block');
	        } else {
	            $(".yhe_more").css('display', 'none');
	        }
	    }
	
	    $(".yhe_more").click(function() {
	        $(".yhe_more").css('display', 'none');
	        loadData(begin, count, key_word, loadDataCallback);
	    });
	
	    Date.prototype.Format = function (fmt) { //author: meizz
	        var o = {
	            "M+": this.getMonth() + 1, //月份
	            "d+": this.getDate(), //日
	            "h+": this.getHours(), //小时
	            "m+": this.getMinutes(), //分
	            "s+": this.getSeconds(), //秒
	            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
	            "S": this.getMilliseconds() //毫秒
	        };
	        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	        for (var k in o)
	            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	        return fmt;
	    }
	    function localFormat(time) {
	        return new Date(time).Format("yyyy-MM-dd hh:mm");
	    }
	});
</script>
</body>
</html>
