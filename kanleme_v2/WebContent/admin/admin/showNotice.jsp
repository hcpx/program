<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
	long ctime = System.currentTimeMillis();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>看了么公益后台管理</title>
<link href="${ctx}/admin/css/jquery-ui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/admin/css/adminv1.css" type="text/css" rel="stylesheet" />
</head>

<body>
<div class="wrap">
    <!--首页右边-->
    <div class="wrap_r fl">
        <div class="actin">
        	<!--头部-->
        	<div class="actin_top">
            	<h2>公告管理</h2>
                <p>首页/公告管理</p>
            </div>
            <!--列表-->
            <div class="actin_list">
            	<p class="actin_list1">系统消息</p>
                <div class="actin_list2">
                    <span class="actin_list2_2 xin_gongzi">新增</span>
                    <a class="actin_list2_1" href="javascript:;">删除</a>
                    <input class="actin_list2_3" type="text" placeholder="请输入关键字" />
                    <button class="actin_list2_4">查询</button>
                </div>
                <!--table内容-->
                <div class="actin_tabdiv">
                <table cellpadding="0" cellspacing="0" class="actin_table">
                	<!--头部-->
                	<thead class="actin_tableh">
                        <tr>
                          <th style="width:10%"></th>
                          <th style="width:70%; text-align:left;">名称</th>
                          <th style="width:20%">时间</th>
                        </tr>
                      </thead>
                      <!--中间内容-->
                      <tbody class="actin_tableb clearfix tuisong"></tbody>
                </table>                 
                </div>
                <div class="clearfix">
                    <p class="fl tuisong_bei">注释：修改时只能选择一条记录修改，删除时可以多选</p>
                </div>
                <div class="yhe_morew"><button id="loadMoreBtn" class="yhe_more" style="display: none">查看更多</button></div>
            </div>
        </div> 
    </div>
</div>
<!--点击"新建"——>'推送消息'弹层-->
<div class="zhezhao"></div>
<div class="xin_gong">
	<div class="actinttop clearfix"><span class="fl">新建公告</span><img class="fr" src="${ctx}/admin/images/close.png" /></div>
    <form class="xin_gongf">
    	<!--标题-->
    	<div class="xin_tuif1 clearfix xin_gongf1">
        	<label class="fl">标题</label>
            <input id="title" type="text" placeholder="请输入标题" />
        </div>
        <!--内容-->
        <div class="xin_tuif2 clearfix">
        	<label>内容</label>
            <textarea id="content" placeholder="请输入详情内容"></textarea>
        </div>
        <div class="actintf_caozuo">
        	<input class="actintf_caozuo1 xin_gong_que" type="button" value="确定" />
            <input class="actintf_caozuo2 xin_gong_qu"type="button" value="取消" />
        </div>
    </form>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-ui.js"></script>
<script type="text/javascript" src="${ctx}/js/adminv1.js"></script>
<script type="text/javascript">
    $(function () {
        var limit = 0;
        var key_word = "";
        var hasMoreData = false;
        loadData(limit, key_word, loadDataCallback);

        function loadData(requestLimit, key_word, callback) {
            var requestParam = {
                "type": "list_system_message",
                'data': '{'
                + '"begin":"' + requestLimit + '",'
                        +'"keyword":"'+ key_word + '"'
                + '}'
            };
            $("#loading").html("<center><td colspan='4' style='text-align:center'>加载数据中，请稍后......</td></center>");
            $.ajax({
                url: "${ctx}/adminapi",
                type: "post",
                data: requestParam,
                dataType: "json",
                success: function (data) {
                    $("#loading").empty();
                    if (data != null && data.status == "ok") {
                        callback(data);
                    } else if (data.status == "parameter_error") {
                        alert("数据加载失败，参数错误!");
                    } else if (data.status == "token_error") {
                        alert("数据加载失败，用户令牌不正确!");
                    } else {
                        alert("服务器内部错误，数据加载失败");
                    }
                    lock = false;
                },
                error: function () {
                    $("#loading").empty();
                    lock = false;
                }
            });
        };

        function loadDataCallback(json) {
            var datas = json.messages;
            var length = datas.length;


            if (datas != null && datas != undefined && length > 0){
                var message = null;
                for (var i = 0; i < length; ++i) {
                    message = datas[i];
                    var dataHtml = "<tr>";
                    dataHtml += "<td name='message_id' style='display: none'>" + message.message_id + "</td>";
                    dataHtml += "<td><input name='items' type='checkbox'></td>";
                    dataHtml += "<td class='tuisong_td2'>" + message.message_title + "</td>";
                    dataHtml += "<td style='text-align:center'>" + localFormat(message.message_time) + "</td>";
                    dataHtml += "</tr>";
                    $(".actin_tableb").append(dataHtml);
                }
            } else {
                if ($(".actin_tableb").find("tr th[colspan='4']:contains('无数据')").length == 0) {
                    $(".actin_tableb").append("<tr><th style='text-align: center' colspan='4'>无数据</th></tr>");
                }
            }

            hasMoreData = json.has_more_data;
            if (hasMoreData) {
                $("#loadMoreBtn").show();
            } else {
                $("#loadMoreBtn").hide();
            }
        };

        $("#loadMoreBtn").click(function() {
            $("#loadMoreBtn").hide();
            loadData(limit, key_word, loadDataCallback);
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
            return new Date(time).Format("yyyy-MM-dd");
        }

        /*点击"新建"按钮出现弹出框'新建公告'*/
        $('.xin_gongzi').click(function(){
            $('.zhezhao').fadeIn();
            $('.xin_gong').fadeIn();
            $('.xin_gongf')[0].reset();

        });

        function addSystemMessage() {

            var $title = $("#title").val();
            if ($title == ''){
                alert("请输入标题");
                return;
            }

            var $content = $("#content").val();
            if ($content == ''){
                alert("请输入内容");
                return;
            }

            var requestParams = {
                "type": "add_system_message",
                'data': '{'
                + '"message_title":"'+$title+'",'
                + '"message_content":"'+$content+'"'
                + '}'
            };

            $.ajax({
                url: "${ctx}/adminapi",
                type: "post",
                data: requestParams,
                dataType: "json",
                success: function (data) {
                    $('.zhezhao').fadeOut();
                    $('.xin_gong').fadeOut();
                    if (data != null && data.status == "ok") {
                        alert("添加成功!!!");
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
        };
        
        $(".actin_list2_1").click(function () {
            var flag = false;
            var array = new Array();
            $("[name = items]:checkbox:checked").each(function () {
                if ($(this).attr("checked")){
                    flag = true;
                }
            });
            if (flag){
                $("[name = items]:checkbox:checked").each(function () {
                    if ($(this).attr("checked")){
                        var healthProjectMemberId = $(this).parent().parent().find("[name=message_id]").text();
                        array.push(healthProjectMemberId);
                    }
                });
                deleteMessage(array);
            }else {
                alert("请至少选择一条公告");
            }

        });

        function deleteMessage(array) {
            var ids = JSON.stringify(array);
            var requestParams = {
                "type": "delete_system_message",
                'data': '{'
                + '"message_ids":'+ids
                + '}'
            };
            $.ajax({
                url: "${ctx}/adminapi",
                type: "post",
                data: requestParams,
                dataType: "json",
                success:function (data) {
                    if (data != null && data.status == 'ok'){
                        alert("删除成功");
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
                },
                error:function () {
                    alert("服务器内部错误。");
                }
            });
        };

        $('.actin_list2_4').click(function () {
            var kw = $(".actin_list2_3").val();
            if (kw == '') {
                alert('请输入查询关键字')
                return;
            }
            limit = 0;
            key_word=kw;
            $(".actin_tableb").empty();
            loadData(limit, key_word, loadDataCallback);
        });

        /*点击"确定"关闭弹层-新建公告*/
        $('.xin_gong_que').click(function(){
            addSystemMessage();
        });
        /*点击"取消"关闭弹层-新建公告*/
        $('.xin_gong_qu').click(function(){
            $('.zhezhao').fadeOut();
            $('.xin_gong').fadeOut();
        });
    });
</script>
</body>
</html>
