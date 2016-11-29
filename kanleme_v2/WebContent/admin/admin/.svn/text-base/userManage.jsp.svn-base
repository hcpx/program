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
            	<h2>用户管理</h2>
                <p>首页/用户管理</p>
            </div>
            <!--列表-->
            <div class="actin_list">
            	<p class="actin_list1">所有用户</p>
                <div class="actin_list2">
                	<a class="actin_list2_1" href="javascript:;" onclick="window.location.reload();">刷新</a>
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
                          <th style="width:5%">三方登陆</th>
                          <th style="width:10%">昵称</th>
                          <th style="width:10%">看币余额（元）</th>
                          <th style="width:10%">推荐人用户名</th>
                          <th style="width:10%">电话号码</th>
                          <th style="width:10%">地点</th>
                          <th style="width:5%">状态</th>
                          <th style="width:10%">解封时间</th>
                          <th style="width:10%">最后登陆时间</th>
                          <th style="width:10%">编辑</th>
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
<!--"封禁"弹窗-->
<div class="zhezhao"></div>
<div class="fenhaodiv">
	<div class="actinttop clearfix"><span class="fl">修改内容</span><img class="fr" src="${ctx}/admin/images/close.png" /></div>
    <select class="sefjjy" onchange="jyfj_way();">
    	<option value="jyy">禁用</option>
    	<option value="fjj">封禁</option>
    	<option value="zcc">正常</option>
    </select>
    <form class="jiaoxiu_form" style="display:none;">
        <!--封号时间-->
        <div class="jiaoxiu_form7">
        	<p>封号</p>
        	<p id="user_id" style="display:none;"></p>
            <div class="jiaoxiu_form7ch">
            	<span><input type="radio" name="shiijan" value="1"/> 1天</span>
                <span><input type="radio" name="shiijan" value="3"/> 3天</span>
                <span><input type="radio" name="shiijan" value="7"/> 7天</span>
                <span><input type="radio" name="shiijan" value="30"/> 30天</span>
                <span><input type="radio" name="shiijan" value="-1"/> 其他<input class="jiaoxiu_form7chtex" type="text" readonly="readonly"/>天</span>
            </div>
        </div>
        <!--封号原因-->
        <div class="jiaoxiu_form8">
        	<label>封号原因</label>
            <select>
            	<option value="a1">垃圾广告</option>
                <option value="a2">侮辱谩骂</option>
                <option value="a3">色情低俗</option>
                <option value="a4">政治敏感</option>
                <option value="a5">违法暴力</option>
            </select>
        </div>
        <!--其他-->
        <div class="jiaoxiu_form9">
        	<label>其他</label>
            <textarea></textarea>
        </div>
    </form>
    <div class="actintf_caozuo">
    	<input class="actintf_caozuo1 jiaoxiu_que no_focus" type="button" value="确定" />
        <input class="actintf_caozuo2 jiaoxiu_qu no_focus"type="button" value="取消" />
    </div>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/adminv1.js"></script>
<script type="text/javascript">
	
	$(function () {
	    var begin = 0;
	    var count = 20;
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
	        loadData(begin, count, key_word, loadDataCallback);
	    });
	
	    loadData(begin, count, key_word, loadDataCallback);
	    
	    function loadData(begin, count, key_word, callBack) {
	        var requestParams = {
	            "type": "admin_get_users",
	            'data': '{' + '"begin":' + begin + ','
	            + '"count":' + count + ',' + '"key_word":"' + key_word
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
	        var datas = data.users;
	        var len = datas.length;
	        if (datas != null && datas != undefined && len > 0) {
	            begin = begin+len;
	            for (var i = 0; i < len; i++) {
	                data += "<tr><td style='display:none' name='user_id'>" + datas[i].user_id + "</td>";
	                data += "<td>" + datas[i].user_name + "</td>";
	                var isthree="否";
	                if(datas[i].third_login==1)
	                	isthree="是";
	                data += "<td>" + isthree + "</td>";
	                data += "<td>" + datas[i].nickname + "</td>";
	                data += "<td>" + datas[i].gold + "</td>";
	                if(datas[i].invite_user_name!=null && datas[i].invite_user_name!='undefined')
	                	data += "<td>" + datas[i].invite_user_name + "</td>";
	                else
	                	data += "<td></td>";
	                if(datas[i].phone_number!=null && datas[i].phone_number!='undefined')
	               		data += "<td>" + datas[i].phone_number + "</td>";
	               	else
		                data += "<td></td>";
		            if(datas[i].address!=null && datas[i].address!='undefined')    
	                	data += "<td>" + datas[i].address + "</td>";
	                else
			            data += "<td></td>";
			        var status="正常";
			        if(datas[i].status==-1)
			        	status="停用";
			        else if(datas[i].status==1)
			        	status="封禁";
			        data += "<td>"+status+"</td>";
			        
			        if(datas[i].status==1)    
	                	data += "<td>" + diffDay(datas[i].close_time) + "</td>";
	                else
			            data += "<td></td>";
			        
	                data += "<td>" + localFormat(datas[i].login_time) + "</td>";
	                var status="<td style='text-align:center'><a class='actin_tableb_a' href='userManageDetail.jsp?user_id="+datas[i].user_id+"'>详情</a><a class='actin_tableb_a' onclick='fenghao(this)'>修改</a>";
	                
	                /*if (datas[i].status == 0) {
	                	status +="<button id='btn_disables' class='btn_disables' href='javascript:void(0)' name='禁用' onclick='disables(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>禁用</button> " +
	                            "<button id='btn_disables' class='btn_disables' onclick='fenghao(this)' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>封禁</button> " +
	                            "</td>";
	                } else if(datas[i].status == -1){
	                	status +="<button class='btn_disables' href='javascript:void(0)' name='已禁用' onclick='disables(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;background-color: #cd0a0a;color: #ffffff;'>已禁用</button> " +
	                            "<button id='btn_disables' class='btn_disables' onclick='fenghao(this)' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>封禁</button> " +
	                            "</td>";
	                } else if(datas[i].status == 1){
	                	status +="<button id='btn_disables' class='btn_disables' href='javascript:void(0)' name='禁用' onclick='disables(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>禁用</button> " +
                        "<button class='btn_disables' href='javascript:void(0)' name='已禁用' onclick='fenghao(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;background-color: #cd0a0a;color: #ffffff;'>已封禁</button> " +
                        "</td>";
	                }*/
	                data += status+"</tr>";
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
	/*封号*/
	function fenghao(e){
		var user_id = $(e).parent().parent().find("[name=user_id]").text();
		$("#user_id").text(user_id);
		$('.fenhaodiv').show();
		$('.zhezhao').show();
	}
	
	$(".actintf_caozuo1").click(function() {
		var user_id=$("#user_id").text();
	    var requestParams;
	    var event_name=$('.sefjjy').val();
	    if(event_name=='jyy'){
			requestParams = {
			        "type": "admin_edit_user",
			        'data': '{' + '"user_id":"' + user_id+ '",'
			        	+'"type":1'
			        	+'}'
			};
		}
		if(event_name=='fjj'){
			var close_time=$("input[name='shiijan']:checked").val();
			if(!close_time){
				alert("请选择封禁天数");
				return;
			}
			if(close_time==-1){
				close_time=$(".jiaoxiu_form7chtex").val();
				if(close_time){ 
				    if(!(/^(\+|-)?\d+$/.test(close_time)) || close_time < 0){  
				        alert("请输入正整数！");
				        return;
				    }
				}else{
					alert("请填写封禁天数");
					return;
				}
			}
			requestParams = {
			        "type": "admin_edit_user",
			        'data': '{' + '"user_id":"' + user_id+ '",'
			        	+'"type":2,'
			        	+'"close_time":'+close_time
			        	+'}'
			};
		}
		if(event_name=='zcc'){
			var close_time=$("input[name='shijian']:checked").val();
			requestParams = {
			        "type": "admin_edit_user",
			        'data': '{' + '"user_id":"' + user_id+ '",'
			        	+'"type":3'
			        	+'}'
			};
		}
	    $.ajax({
	          url: "${ctx}/adminapi",
	          type: "post",
	          data: requestParams,
	          dataType: "json",
	          success: function (data) {
	              if (data != null && data.status == "ok") {
	                  alert("操作成功!!!");
	           		  location.reload();
	              } else if (data != null && data.status == "server_error") {
	                  alert("服务器内部错误。");
	              } else if (data != null && data.status == "parameter_error") {
	                  alert("发送参数错误。");
	              } else if (data != null && data.status == "not_exits") {
	                  alert("该条数据不存在");
	              } else if (data != null && data.status == "token_error") {
	                  alert("登陆超时,请重新登陆。");
	              }
	          },
	          error: function () {
	              alert("服务器内部错误。");
	          }
	      }
	    );
	});
	
	$(".actintf_caozuo2").click(function() {
		$('.fenhaodiv').fadeOut();
		$('.zhezhao').fadeOut();
	});
	
	/*点击触发“禁用”“封禁”事件*/
	function jyfj_way(){
		var event_name=$('.sefjjy').val();
		if(event_name=='jyy' || event_name=='zcc'){
			$('.jiaoxiu_form').hide();
		}
		if(event_name=='fjj'){
			$('.jiaoxiu_form').show();
		}
	}
	
	function diffDay(end_time) {
    	var daytime=24*60*60*1000;
        return Math.floor((end_time-new Date())/daytime)+"天";
    }
	
	$("input[name=shiijan]").change(function() {
		var selectedvalue = $("input[name=shiijan]:checked").val();
		if(selectedvalue!=-1)
			$(".jiaoxiu_form7chtex").attr("readonly",true);
		else
			$(".jiaoxiu_form7chtex").attr("readonly",false);
	});
</script>
</body>
</html>
