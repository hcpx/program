<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${ctx}/admin/images/logo.ico" rel="shortcut icon">

    <!-- CSS -->
    <link rel="stylesheet" href="${ctx}/admin/css/global.css">
    <link rel="stylesheet" href="${ctx}/admin/css/data-list.css">
    <link rel="stylesheet" href="${ctx}/js/jqueryui/jquery-ui.min.css">

    <style>
    	a{color:#000;}
        .data-body th a {
            color: #008000;
        }

        #dialog-content {
            padding: 5px;
            font-size: 14px;
        }
        input,button{border:none;background:none;}
		.yhe_more{ cursor:pointer;display:block; width:300px; height:30px; line-height:30px; text-align:center; font-size:14px; color:#fff; background:#1ab394;margin:0 auto;margin-top:20px; border-radius:5px;}
    	.red_status{ height: 40px; padding-top: 10px; background: #fff;width: 88%; margin: 0 auto;padding: 10px;
    margin-top: 20px;border-top: 4px solid #e7eaec;margin-bottom:30px;}
		.red_status a{ display:inline-block;padding:4px 20px; font-size:000; background:#fff; margin-left:14px; font-size:15px;border-radius:5px; border:1px solid #e8e8e8; }
		.red_status a:hover{background:#1ab394;color:#fff;}
		.red_status a.active{background:#1ab394;color:#fff;}
    </style>
</head>
<body>
<div class="actin_top">
	<h2>认证管理</h2>
    <p>首页/认证管理</p>
</div>
<div class="container" style="height:auto">
	<div class="red_status">
		状态：<a name="red_status" value="1" href="javascript:;" class="active">认证中</a><a
			name="red_status" value="-1" href="javascript:;">认证失败</a><!--  <a value="0" name="red_status" href="javascript:;">未认证</a>-->
			<a name="red_status" value="2" href="javascript:;">认证通过</a>
	</div>
	<div class="yongjie_div2">
		<h1>认证管理</h1>
	</div>
    <table class="data-table" style="margin-bottom:0;">
        <thead class="data-header">
        <tr>
        	<th style="width: 10%">用户id</th>
        	<th style="width: 10%">组织名或姓名</th>
        	<th style="width: 10%">身份证</th>
       		<th style="width: 10%">组织地址</th>
        	<th style="width: 10%">电话号码</th>
       		<th style="width: 10%">认证时间</th>
       		<th style="width: 10%">认证类别</th>
       	    <th style="width: 20%">认证失败原因</th>
       	    <th style="width: 10%">图片</th>
       	    <!--<th style="width: 10%">操作</th>-->
        </tr>
        </thead>
        <tbody class="data-body">
        </tbody>
         <tbody id="loading"/>
    </table>
	<button id="loadMoreBtn" class="yhe_more" style="display:none">查看更多</button>
</div>
<div id="dialog" title="认证图片">
    <div id="dialog-content"></div>
    <p id="identificationId" style="display:none"></p>
</div>
<div id="see" title="认证图片">
    <div id="dialog-see-content"></div>
</div>
<div id="no_pass_dialog" title="不通过审核" style="display:none">
     <p>审核失败原因:</p><br>
     <textarea style="padding: 5px;" id="faildesc" rows="7" cols="53"></textarea>
</div>
</body>
</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript">
	var begin = 0;
	var status = 1;
    var hasMoreData = false;
    var $window = $(window);
    var $document = $(document);
    var lock = false;

    loadData(begin,status, loadDataCallback);
    
	$("[name='red_status']").click(
			function() {
				$("[name='red_status']").each(function(){
					$(this).removeClass("active");
				});
				$(this).addClass("active");
				status=$(this).attr("value");
				begin=0;
				data="";
				$(".data-body").empty();
				loadData(begin,status, loadDataCallback);
			});
    $("#loadMoreBtn").click(function() {
    	$("#loadMoreBtn").css('display', 'none');
		loadData(begin,status, loadDataCallback);
	});

    function loadDataCallback(json) {
        var datas = json.datas;
        var length = datas.length;

        // 数据回显
        if (datas != null && datas != undefined && length > 0) {
        	begin = begin+length;
            for (var i = 0; i < length; ++i) {
                var dataHtml = "<tr>";
                dataHtml += "<th name='user_id'>" + datas[i].user_id + "</th>";
                dataHtml += "<th name='user_name'>" + datas[i].name + "</th>";
                if(datas[i].id_num != null && datas[i].id_num != undefined)
                	dataHtml += "<th name='id_num'>" + datas[i].id_num + "</th>";
                else
                	dataHtml += "<th name='id_num'></th>";
               	if(datas[i].address != null && datas[i].address != undefined)
                    dataHtml += "<th name='address'>" + datas[i].address + "</th>";
                else
                	dataHtml += "<th name='address'></th>";
                dataHtml += "<th name='phone_num'>" + datas[i].phone_num + "</th>";
                dataHtml += "<th name='time'>" + localFormat(datas[i].time) + "</th>";
                var type="";
                if(datas[i].type==0)
                	type='个人认证';
                else if(datas[i].type==1)
                	type='组织认证';
                dataHtml += "<th name='type'>" + type + "</th>";
                if(datas[i].failure_causes != null && datas[i].failure_causes != undefined)
                    dataHtml += "<th name='failure_causes'>" + datas[i].failure_causes + "</th>";
                else
                	dataHtml += "<th name='failure_causes'></th>";
               	dataHtml += "<th style='text-align:center;display:none;' name='certification'>" + datas[i].certification + "</th>";
                dataHtml += "<th style='text-align:center;display:none;' name='photos'>" + datas[i].photos + "</th>";
                dataHtml += "<th name='imgs'><a onclick='showModal(this);' href='javascript:void(0)'>"+"查看图片"+"</a></th>";
                dataHtml += "</tr>";
                $(".data-body").append(dataHtml);
            }
        }

        hasMoreData = json.has_more_data;
        if (hasMoreData) {
			$("#loadMoreBtn").css('display', 'block');
		} else {
			$("#loadMoreBtn").css('display', 'none');
		}
        var container_h=$('.data-table').height()+200;
		$('.container').css('height',container_h)
        $(window).resize(function() {
			var container_h=$('.data-table').height()+200;
			$('.container').css('height',container_h)
		});
    }

    function loadData(requestLimit,status, callback) {
    	if(lock){
    		return;
    	}
        lock = true;

        var requestParam = {
            "type": "admin_get_identifications",
            'data': '{'
            + '"begin":"' + requestLimit + '",'
            + '"status":"' + status + '"'
            + '}'
        };
        $("#loading").html("<center><td colspan='10' style='text-align:center'>加载数据中，请稍后......</td></center>");
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
                } else if (data.status == "permission_error") {
                    alert("数据加载失败，没有访问权限!");
                } else {
                    alert("服务器内部错误，数据加载失败");
                }

                lock = false;
            },
            error: function () {
            	$("#loading").empty();
                alert("服务器内部错误，数据加载失败");

                lock = false;
            }
        });
    }
    $("#dialog").dialog({
        autoOpen: false,
        width: 1000,
        height: 600,
        buttons: [
            {
                text: "通过",
                click: function () {
                    var id = $("#identificationId").attr("nid");
                    handling(id,"");
                }
            },
            {
                text: "不通过",
            	click: function () {
            		$("#faildesc").val("");
            		$("#no_pass_dialog").dialog("open");
           		}
            }
        ]
    });
    
    $("#see").dialog({
        autoOpen: false,
        width: 1000,
        height: 600,
        buttons: [
            {
                text: "关闭",
                click: function () {
                    $(this).dialog("close");
                }
            }
        ]
    });


    function showModal(e) {
        var photos =$(e).parent().parent().find("[name=photos]").text().split(",");
        var userid = $(e).parent().parent().find("[name=user_id]").text();
        var certification = $(e).parent().parent().find("[name=certification]").text();
        $("#identificationId").attr("nid", userid);
        var img="";
        for(var i=0;i<photos.length;i++){
        	img+="<img src='"+photos[i]+"'/></br>";
        }
        if(certification==1){
       		$("#dialog").dialog("open");
       		$("#dialog-content").html(img);
       		}
        else{
        	$("#see").dialog("open");
        	$("#dialog-see-content").html(img);
        }
    }
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
    
    $("#no_pass_dialog").dialog({
        autoOpen: false,
        width: 420,
        height: 300,
        title: "不通过",
        buttons: [
            {
                text: "确定",
                click: function () {
                    var faildesc = $("#faildesc").val();
                    if (faildesc == "") {
                    	alert("请输入不通过原因..");
                    	return;
                    }
                    var id = $("#identificationId").attr("nid");
                    $("#no_pass_dialog").dialog("close");
                    handling(id,faildesc);
                }
            },
            {
                text: "取消",
                click: function () {
                    $(this).dialog("close");
                }
            }
        ]
    });
   
   function handling(userid,faildesc) {
       var requestParam = {
           "type": "admin_action_identification",
           'data': '{'
           + '"user_id":"' + userid + '",'
           + '"failure_causes":"' + faildesc + '"'
           + '}'
       };
       $.ajax({
           url: "${ctx}/adminapi",
           type: "post",
           data: requestParam,
           dataType: "json",
           success: function (data) {
               if (data != null && data.status == "ok") {
            	   $("#dialog").dialog("close");
                   alert("已经成功处理...");
                   begin=0;
                   $(".data-body").html("");
                   loadData(begin,status, loadDataCallback);
               } else if (data.status == "parameter_error") {
                   alert("数据加载失败，参数错误!");
               } else if (data.status == "token_error") {
                   alert("数据加载失败，用户令牌不正确!");
               } else if (data.status == "permission_error") {
                   alert("数据加载失败，没有访问权限!");
               } else {
               	  alert("服务器内部错误，数据加载失败"+data.status);
               }
           },
           error: function () {
               alert("服务器内部错误，数据加载失败");
           }
       });
   }
</script>