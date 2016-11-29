<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
	long ctime = System.currentTimeMillis();
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

<style>
.data-table .data-body tr td {
	vertical-align: middle;
}
input,button{border:none;background:none;}
.yhe_more{ cursor:pointer;display:block; width:300px; height:30px; line-height:30px; text-align:center; font-size:14px; color:#fff; background:#1ab394;margin:0 auto;margin-top:20px; border-radius:5px;}
</style>
</head>
<body>
	<div class="container" style="height:auto">
		<div class="yongjie_div2">
			<h1>帖子管理</h1>
		</div>
		<table class="data-table" style="margin-bottom:0;">
			<thead class="data-header">
				<tr>
					<th style="display:none;width: 0%">ID</th>
					<th style="width: 15%">帖子标题</th>
					<th style="width: 30%">帖子详情</th>
					<th style="width: 10%">评论数</th>
					<th style="width: 10%">浏览数</th>
					<th style="width: 10%">点赞数</th>
					<th style="width: 10%">状态</th>
					<th style="width: 15%">操作</th>
				</tr>
			</thead>
			
			<tbody class="data-body"/></tbody>
			<tbody id="loading"></tbody>
		</table>
		<button id="loadMoreBtn" class="yhe_more" style="display:none">查看更多</button>
	</div>
	
</body>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js"
	type="text/javascript"></script>
<script type="text/javascript">
	var begin = 0;
	var has_more_data = false;
	var $window = $(window);
	var $document = $(document);
	var lock = false;
	
	$("#loadMoreBtn").click(function() {
		$("#loadMoreBtn").css('display', 'none');
		loadData(begin, loadDataCallback);
	});

	function loadDataCallback(json) {
		var datas = json.article;
		if (datas != null && datas != undefined) {
			var dlen = datas.length;
			begin = begin+dlen;
			for (var i = 0; i < dlen; ++i) {
				var dataHtml = "<tr>";
				dataHtml += "<td name='article_id' style='text-align:center;display:none'>"
						+ datas[i].article_id + "</td>";
				dataHtml += "<td style='text-align:center'>" + datas[i].article_title + "</td>";
				
				var content=datas[i].article_content;
				var re=/\r\n/g;
				content=content.replace(/\r\n/g,"</br>");
				content=content.replace(/\n/g,"</br>");
				content=content.replace(/\r/g,"</br>");
				dataHtml += "<td style='text-align:center'><a href='${ctx}/article/article_details_share.jsp?article_id="+datas[i].article_id+"'>" + content + "</a></td>";
				dataHtml += "<td style='text-align:center'>" + datas[i].comment_num + "人</td>";
				dataHtml += "<td style='text-align:center'>" + datas[i].view_count + "个</td>";
				dataHtml += "<td style='text-align:center'>" + datas[i].article_praise_num + "个</td>";
				var status="普通帖子";
				var action="<a name='delArticle' href=''>禁止访问</a>";
				if(datas[i].article_status==-1){
					status="不可查看帖子";
					action="";
				}
				dataHtml += "<td style='text-align:center'>" + status + "</td>";
				dataHtml += "<td style='text-align:center'>"+action+"</td>";
				dataHtml += "</tr>";
				$(".data-body").append(dataHtml);
				
			}
			var container_h=$('.data-table').height()+200;
			$('.container').css('height',container_h)
			$(window).resize(function() {
				var container_h=$('.data-table').height()+200;
				$('.container').css('height',container_h)
			});
			
		}
		hasMoreData = json.has_more_data;
		if (hasMoreData) {
			$("#loadMoreBtn").css('display', 'block');
		} else {
			$("#loadMoreBtn").css('display', 'none');
		}
		$("[name='delArticle']").click(
				function() {
					var article_id=$(this).parent().parent().find("[name=article_id]").text();
					changeArticleStatus(article_id,loadDataCallback);
				});
	}
	function changeArticleStatus(article_id, callback) {
		alert("是否将此贴置为禁止访问状态");
		if(lock){
			return;
		}
		lock = true;
        var requestParam = {
                "type": "admin_manager_article",
                'data': '{'
                + '"article_id":"' + article_id + '",'
                + '"action":0'
                + '}'
        };
		$("#loading").html("<center><td colspan='7' style='text-align:center'>加载数据中，请稍后......</td></center>");
		$("#loadMoreBtn").style = "display:none";
		$.ajax({
			url : "${ctx}/adminapi",
			type : "post",
			data : requestParam,
			dataType : "json",
			success : function(data) {
				$("#loading").empty();
				if (data != null && data.status == "ok") {
					begin = requestParam.data.begin;
					callback(data);
				} 
				lock = false;
			},
			error : function() {
				$("#loading").empty();
				lock = false;
			}
		});
	}
	function loadData(begin, callback) {
		if(lock){
			return;
		}
		lock = true;
        var requestParam = {
                "type": "admin_get_articles",
                'data': '{'
                + '"begin":"' + begin + '"'
                + '}'
            };
		$("#loading").html("<center><td colspan='8' style='text-align:center'>加载数据中，请稍后......</td></center>");
		$("#loadMoreBtn").style = "display:none";
		$.ajax({
			url : "${ctx}/adminapi",
			type : "post",
			data : requestParam,
			dataType : "json",
			success : function(data) {
				$("#loading").empty();
				if (data != null && data.status == "ok") {
					begin = requestParam.data.begin;
					callback(data);
				} else if (data.status == "parameter_error") {
                   alert("数据加载失败，参数错误!");
               	} else if (data.status == "token_error") {
                   alert("数据加载失败，用户令牌不正确!");
               	}else if (data.status == "permission_error") {
                   alert("数据加载失败，没有访问权限!");
               	} else {
               	  alert("服务器内部错误，数据加载失败"+data.status);
              	}
				lock = false;
			},
			error : function() {
				$("#loading").empty();
				lock = false;
			}
		});
	}
	loadData(begin, loadDataCallback);
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
</script>
</html>