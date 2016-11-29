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
<div class="actin_top">
	<h2>banner管理</h2>
    <p>首页/banner管理</p>
</div>
	<div class="container" style="height:auto">
		<div class="yongjie_div2">
			<h1>banner管理</h1>
		</div>
		<table class="data-table" style="margin-bottom:0;">
			<thead class="data-header">
				<tr>
					<th style="display: none">ID</th>
					<th style="width: 10%">图片</th>
					<th style="width: 18%">资源类型</th>
					<th style="width: 18%">banner类型</th>
					<th style="width: 18%">资源</th>
					<th style="width: 18%">开始时间</th>
					<th style="width: 18%">结束时间</th>
				</tr>
			</thead>
			<tbody class="data-body">
			</tbody>
			<tbody id="loading"/>
		</table>
		<button id="loadMoreBtn" class="yhe_more" style="display:none">查看更多</button>
		<div class="data-footer">
			<div class="data-operator">
				<a onclick="addBanner();" class="add"> <img
					src="${ctx}/admin/images/sale_add_logo.png" /> <span>添加广告</span>
				</a>
			</div>
			<div class="cls"></div>
		</div>
	</div>
</body>
</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js"
	type="text/javascript"></script>
<script src="${ctx}/js/getQueryString.js"></script>

<script type="text/javascript">
	var begin = 0;
	var has_more_data = false;
	var $window = $(window);
	var $document = $(document);
	var lock = false;

	loadData(begin, loadDataCallback);

	$("#loadMoreBtn").click(function() {
		$("#loadMoreBtn").css('display', 'none');
		loadData(begin, loadDataCallback);
	});

	function addBanner() {
		window.location.href = "${ctx}/admin/admin/addBanner.jsp";
	};

	function loadDataCallback(json) {
		var datas = json.data;
		var length = datas.length;
		var sourceType = "";
		var bannerType = "";
		if (datas != null && datas != undefined && length > 0) {
			begin = begin+length;
			for (var i = 0; i < length; ++i) {
				var dataHtml = "<tr>";
				dataHtml += "<td name='banner_id' style='text-align:center;display:none'>"+ datas[i].banner_id + "</td>";
				dataHtml += "<td style='text-align:center'><img style='width:120px;height:48px;' src="+datas[i].img_url +"></td>";
				//资源类型,//1:网页,2:店铺,3：商品
				if (datas[i].source_type == 1) {
					sourceType = "网页";
				} else if (datas[i].source_type == 2) {
					sourceType = "店铺";
				} else {
					sourceType = "商品";
				}
				dataHtml += "<td style='text-align:center'>" + sourceType
						+ "</td>";

				//1:主界面banner，2：广告界面banner，3：商品界面banner
				if (datas[i].banner_type == 1) {
					bannerType = "主界面";
				} else if (datas[i].banner_type == 2) {
					bannerType = "广告界面";
				} else {
					bannerType = "商品界面";
				}
				dataHtml += "<td style='text-align:center'>" + bannerType
						+ "</td>";
				dataHtml += "<td style='text-align:center'>" + datas[i].source
						+ "</td>";
				if(<%=ctime%> >= datas[i].end_time){
					dataHtml += "<td style='text-align:center'><font color='#FF0000'>"
						+ localFormat(datas[i].start_time) + "</font></td>";
					dataHtml += "<td style='text-align:center'><font color='#FF0000'>"
						+ localFormat(datas[i].end_time) + "</font></td>";
				}else{
					dataHtml += "<td style='text-align:center'>"
						+ localFormat(datas[i].start_time) + "</td>";
					dataHtml += "<td style='text-align:center'>"
						+ localFormat(datas[i].end_time) + "</td>";
				}
				dataHtml += "</tr>";
				$(".data-body").append(dataHtml);
			}
			
			var container_h=$('.data-table').height()+200;
			$('.container').css('height',container_h)
			$(window).resize(function() {
				var container_h=$('.data-table').height()+200;
				$('.container').css('height',container_h)
			});
			
			hasMoreData = json.has_more_data;
			if (hasMoreData) {
				$("#loadMoreBtn").css('display', 'block');
			} else {
				$("#loadMoreBtn").css('display', 'none');
			}
		}
	}
	function loadData(begin, callback) {
		lock = true;
		var requestParam = {
			"type" : "admin_get_bananers",
			'data': '{'
                + '"begin":"' + begin + '"'
                + '}'
		};
		 $("#loading").html("<center><td colspan='7' style='text-align:center'>加载数据中，请稍后......</td></center>");
		$.ajax({
			url : "${ctx}/adminapi",
			type : "post",
			data : requestParam,
			dataType : "json",
			success : function(data) {
				$("#loading").empty();
				if (data != null && data.status == "ok") {
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