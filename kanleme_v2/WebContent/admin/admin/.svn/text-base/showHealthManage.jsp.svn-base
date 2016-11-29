<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
	long ctime = System.currentTimeMillis();
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>国立看了么管理后台</title>
<link href="${ctx}/admin/css/adminv1.css" type="text/css" rel="stylesheet" />
</head>

<body>
<div class="wrap">
    <!--首页右边-->
    <div class="wrap_r fl">
        <!--正文开始-->
        <div class="actin">
        	<!--头部-->
        	<div class="actin_top">
            	<h2>健康互助管理</h2>
                <p>首页/健康互助管理</p>
            </div>
            <!--列表-->
            <div class="actin_list">
            	<p class="actin_list1">所有用户</p>
                <div class="actin_list2">
                	<a class="actin_list2_1" onclick="window.location.reload();">刷新</a>
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
						  <!-- <th style="width:8%">角色名</th> -->       
                          <th style="width:10%">姓名</th>
                          <th style="width:10%">类型</th>
                          <th style="width:10%">推荐人用户名</th>
                          <!-- <th style="width:12%">地点</th> -->
                          <th style="width:13%">状态</th>
                          <th style="width:13%">账户余额(元)</th>
                          <th style="width:10%">观察期剩余天数</th>
                          <th style="width:12%">加入时间</th>
                          <th style="width:12%">编辑</th>
                        </tr>
                      </thead>
                      <!--中间内容-->
                      <tbody class="actin_tableb clearfix">
                      </tbody>
                      <tbody id="loading"></tbody>
                </table>
                	<div class="yhe_morew"><button class="yhe_more" style="display:none;">查看更多</button></div>
                </div>
            </div>
        </div> 
    </div>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/adminv1.js"></script>
<script type="text/javascript">
	function disables(e) {
	    var name = $(e).attr("name");
	    var healthProjectMemberId = $(e).parent().parent().find("[name=health_project_member_id]").text();
	    var requestParams = {
	        "type": "admin_health_project_members_manage",
	        'data': '{' + '"health_project_member_id":"' + healthProjectMemberId
	        + '"}'
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
	};

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
	            "type": "admin_get_health_project_members",
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
	        var datas = data.projectMembersList;
	        var len = datas.length;
	        if (datas != null && datas != undefined && len > 0) {
	            begin = begin+len;
	            for (var i = 0; i < len; i++) {
	                data += "<tr><td style='display:none' name='health_project_member_id'>" + datas[i].health_project_member_id + "</td><td>" + datas[i].nickname + "</td>";
	                data += "<td>" + datas[i].name + "</td>";
	                data += "<td>" + datas[i].health_project_id + "</td>";
	                if(datas[i].invite_name!=null  && datas[i].invite_name!='undefined')
	                	data += "<td>" + datas[i].invite_name + "</td>";
	                else
	                	data += "<td></td>";
	                var type = "";
	                if (datas[i].status == 0) {
	                    type = '观察期';
	                } else if (datas[i].status == 1) {
	                    type = '失效会员';
	                } else if (datas[i].status == 2) {
	                    type = '正式会员';
	                } else if (datas[i].status == 3) {
	                    type = '超出保障范围';
	                }
	                data += "<td>" + type + "</td>";
	                data += "<td>" + datas[i].balance + "</td>";
	                if (datas[i].status == 0 || datas[i].status == 1) 
	                	data += "<td>"+diffDay(datas[i].effect_time)+"</td>";
	                else
	                	data += "<td></td>";
	                data += "<td>" + localFormat(datas[i].join_time) + "</td>";
	                if (datas[i].status != 1) {
	                    data += "<td style='text-align:center'>" +
	                            "<button id='btn_disables' class='btn_disables' href='javascript:void(0)' name='禁用' onclick='disables(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>禁用</button> " +
	                            "</td>";
	                } else {
	                    data += "<td style='text-align:center'>" +
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
	    
	    function diffDay(end_time) {
	    	var daytime=24*60*60*1000;
	        return Math.floor((end_time-new Date())/daytime);
	    }
	});
</script>
</body>
</html>
