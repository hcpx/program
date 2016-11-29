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
#loadMoreBtn{cursor: pointer;display: block;width: 300px;height: 30px;line-height: 30px;text-align: center;font-size: 14px;color: #fff;
    background: #1ab394;margin: 0 auto;margin-top: 20px; margin-top: 30px;border-radius: 5px; border:none; }
</style>
</head>
<body>

	<div class="container">
		<div class="yongjie_div2">
			<h1>专题管理</h1>
		</div>
		<table class="data-table">
			<thead class="data-header">
				<tr>
					<th style="display: none">ID</th>
					<th style="width: 10%">头像</th>
					<th style="width: 20%;">标题</th>
					<th style="width: 50%">内容</th>
					<th style="width: 10%">评论数</th>
					<th style="width: 10%">浏览数</th>
				</tr>
			</thead>
			<tbody class="data-body"></tbody>
			<tbody id="loading"></tbody>

		</table>
		<div><button id="loadMoreBtn" class="yhe_more" style="display:none;" onClick="loadMore()">查看更多</button></div>
		<div class="data-footer">
			<div class="data-operator">
				<a onclick="addBanner();" class="add"> <img
					src="${ctx}/admin/images/sale_add_logo.png" /> <span>添加专题</span>
				</a>
			</div>
			<div class="cls"></div>
		</div>
	</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script src="${ctx}/js/getQueryString.js"></script>

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

	function addBanner() {
		window.location.href = "${ctx}/admin/admin/addSubject.jsp";
	};

	function loadDataCallback(json) {
		var datas = json.subjects;
		if (datas != null && datas != undefined) {
			var dlen = datas.length;
			begin = begin+dlen;
			for (var i = 0; i < datas.length; ++i) {
				var dataHtml="<tr>";
				dataHtml += "<td name='circle_id' style='text-align:center;display:none'>"
					+ datas[i].subject_id + "</td>";
				dataHtml += "<td style='text-align:center'><img style='width:100%;height:48px;' src="+datas[i].subject_icon +"></td>";
				dataHtml += "<td style='text-align:center'>" + datas[i].title + "</td>";
				var content="";
				if(datas[i].subject_content.length>=200)
					content=datas[i].subject_content.substring(0,200)+"......";
				else
					content=datas[i].subject_content;
				content=content.replace(/\r\n/g,"</br>");
				content=content.replace(/\n/g,"</br>");
				content=content.replace(/\r/g,"</br>");
				dataHtml += "<td style='width:50%;'><a href='${ctx}/subject/subject_detail_share.jsp?subject_id="+datas[i].subject_id+"'>" + content + "</td>";
				dataHtml += "<td style='text-align:center'>" + datas[i].comment_count + "人</td>";
				dataHtml += "<td style='text-align:center'>" + datas[i].browse_count + "个</td>";
				dataHtml += "</tr>";
				$(".data-body").append(dataHtml);
			}
		}

		var container_h=$('.data-table').height()+200;
		$('.container').css('height',container_h)
		$(window).resize(function() {
			var container_h=$('.data-table').height()+200;
			$('.container').css('height',container_h)
		});
		hasMoreData = json.has_more_data;
		if (hasMoreData) {
			$("#loadMoreBtn").fadeToggle();
		} else {
			// $("#loadMoreBtn").fadeToggle();
			$("#loadMoreBtn").style = "display:none";
		}
	}

	function loadData(begin, callback) {
		if (lock)
			return;
		lock = true;
		var requestParam = {
			"type" : "admin_get_subjects",
			'data' : '{' + '"begin":"' + begin + '"' + '}'
		};
		$("#loading").html("<center><td colspan='5' style='text-align:center'>加载数据中，请稍后......</td></center>");
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
	loadData(begin, loadDataCallback);
	Date.prototype.Format = function(fmt) { //author: meizz
		var o = {
			"M+" : this.getMonth() + 1, //月份
			"d+" : this.getDate(), //日
			"h+" : this.getHours(), //小时
			"m+" : this.getMinutes(), //分
			"s+" : this.getSeconds(), //秒
			"q+" : Math.floor((this.getMonth() + 3) / 3), //季度
			"S" : this.getMilliseconds()
		//毫秒
		};
		if (/(y+)/.test(fmt))
			fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(fmt))
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
						: (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	}
	function localFormat(time) {
		return new Date(time).Format("yyyy-MM-dd");
	}
</script>