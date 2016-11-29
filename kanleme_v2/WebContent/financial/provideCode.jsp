<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>国立看了么财务后台</title>
<link href="css/provide_code.css" type="text/css" rel="stylesheet" />
<style>
.redpacketzhezhao{display:none;z-index:99; width:100%; height:100%; background-color:rgba(0,0,0,0.2); position:absolute; top:0; left:0;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#19000000,endColorstr=#19000000);}
.loading{display:none;position:fixed;width:200px;height:200px; z-index:999; text-align:center;top:50%; margin-top:-100px;left:50%; margin-left:-100px;}
.loading img{width:100px; height:auto; margin:0 auto;}
.loading span{color:#000;display:block;width:100%;height:40px;line-height:40px;font-size:14px; text-align:center;}
</style>
</head>

<body>
<div id="redpacketzhezhao" class="redpacketzhezhao"></div>
<div class="loading"><img src="images/loading.gif" /><span>发放优惠码中...</span></div>
	<!--首页左边-->
	<div class="wrap clearfix">
		<!--首页右边-->
		<div class="wrap_r fl">
			<!--发放码管理-->
			<div class="actin">
				<div class="actin_top">
	            	<h2>发放码管理</h2>
	                <p>首页/发放码管理</p>
	            </div>
				<div class="actin_list2 yongjie_div1">
					<div class="red1 clearfix">
						<span class="red1s1 fl add_code">添加发放码</span>
					</div>
					<div class="red_status">
						状态：<a name="red_status" href="javascript:;" class="active" value="1">已抢</a>
						 <a name="red_status" href="javascript:;" value="0">未抢</a>
					</div>
				</div>
				<div class="actin_list">
					<div class="yongjie_div2">
						<h1>发放码管理</h1>
					</div>
					<!--table内容(已抢)-->
					<div class="actin_tabdiv code_finish">
						<table cellpadding="0" cellspacing="0" class="actin_table">
							<!--头部-->
							<thead class="actin_tableh">
								<tr>
									<th style="width: 32%">发放时间</th>
									<th style="width: 32%">编号</th>
									<th style="width: 32%">总金额</th>
								</tr>
							</thead>
							<!--中间内容-->
							<tbody id="promoCodes" class="actin_tableb clearfix"></tbody>
							<tbody id="loading"></tbody>
						</table>
					</div>
					<div class="yhe_morew"><button id="loadMoreBtn" class="yhe_more" style="display:none" onClick="loadMore()">查看更多</button></div>
				</div>
			</div>
		</div>
		
			
		
	</div>
	<!--点击"添加发放码"——>弹层-->
	<div class="zhezhao"></div>
	<div class="xin_code">
		<div class="actinttop clearfix">
			<span class="fl">添加发放码</span><img class="code_close fr"
				src="images/close.png" />
		</div>
		<form class="xin_xif code_form">
			<!--发放金额-->
			<div class="xin_xif1 clearfix">
				<label class="fl">总金额</label> <input class="code_money" type="text"
					placeholder="3000.00" />
			</div>
			<p class="codemo_yan"></p>
			<!--发放数量-->
			<div class="xin_xif1 clearfix">
				<label class="fl">总数量</label> <input class="code_num" type="text"
					placeholder="100" />
			</div>
			<p class="codesum_yan"></p>
			<!--发放方式-->
			<div class="xin_xif1 clearfix">
				<label class="fl">发放方式</label> <span class="code_way"><input
					name="code_wa" type="radio" value="0" checked="checked" />平均</span> <span
					class="code_way"><input name="code_wa" type="radio"
					value="1" />随机</span>
			</div>
			<!-- 开始时间 -->
			<div class="xin_xif1 clearfix">
				<label class="fl">开始时间</label> <input type="text"
					class="start_time fl" onClick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
			</div>
			<p class="starttime_yan"></p>
			<!-- 结束时间 -->
			<div class="xin_xif1 clearfix">
				<label class="fl">结束时间</label> <input type="text"
					class="end_time fl" onClick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
			</div>
			<p class="endtime_yan"></p>

			<!-- 登录密码 -->
			<div class="xin_xif1 clearfix">
				<label class="fl">登录密码</label> <input class="password"
					type="password" />
			</div>
			<p class="password_yan"></p>

			<div class="actintf_caozuo">
				<input class="actintf_caozuo1 code_sure" type="button" value="确定" />
				<input class="actintf_caozuo2 code_qu" type="button" value="取消" />
			</div>
		</form>
	</div>
	<script type="text/javascript" src="../js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="../js/datePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="../js/md5.js"></script>
	<script type="text/javascript">
		var type = 1;
		var begin = 0;
		var count = 20;
		var data = "";

		$(function() {
		
			/*点击“添加发放码“*/
			$('.add_code').click(function() {
				$('.zhezhao').fadeIn();
				$('.xin_code').fadeIn();
			})
			/*点击右边"X"效果*/
			$('.code_close').click(function() {
				$('.zhezhao').fadeOut();
				$('.xin_code').fadeOut();
			})
			/*点击右边"X"效果*/
			$('.code_qu').click(function() {
				$('.zhezhao').fadeOut();
				$('.xin_code').fadeOut();
				$('.code_form')[0].reset();
			})

			/*点击“弹框”的“确定”按钮-提交表单*/
			$('.code_sure').click(function() {
								var code_money = $('.code_money').val().trim();
								var code_num = $('.code_num').val().trim();
								var code_type = $(
										"input[name='code_wa']:checked").val();
								var start_time = $(".start_time").val()
										.replace(/-/gm, "/");
								var end_time = $(".end_time").val().replace(
										/-/gm, "/");

								/*当前时间*/
								var curDate = new Date();
								var curDateStr = curDate.getFullYear() + "/"
										+ (curDate.getMonth() + 1) + "/"
										+ curDate.getDate() + " "
										+ curDate.getHours() + ":"
										+ curDate.getMinutes() + ":"
										+ curDate.getSeconds();
								var curDateTime = new Date(curDateStr)
										.getTime();
								var start_time_getTime = new Date(start_time)
										.getTime();
								var end_time_getTime = new Date(end_time)
										.getTime();

								var password = $(".password").val().trim();
								/* var curDate = new Date();
								var curDateStr = curDate.getFullYear() + "-" + (curDate.getMonth() + 1) + "-" + curDate.getDate()+" "+curDate.getHours()+":"+curDate.getMinutes(); */
								if (code_money == '') {
									$('.codemo_yan').show();
									$('.codemo_yan').html('请填写发放金额');
									return true;
								}
								if (code_num == '') {
									$('.codesum_yan').show();
									$('.codesum_yan').html('请填写发放数量');
									return true;
								}

								if (start_time == '') {
									start_time_getTime = curDateTime;
								} 

								if (end_time == '') {
									$('.endtime_yan').show();
									$('.endtime_yan').html('请选择结束时间')
									return;
								}

								if (end_time < curDateTime) {
									$('.endtime_yan').show();
									$('.endtime_yan').html('结束日期不能为过去日期')
									return;
								}

								if (password == '') {
									$('.password_yan').show();
									$('.password_yan').html("登录密码不能为空");
									return;
								}
								
								publicProvideCode(code_money, code_num,
										code_type, start_time_getTime,
										end_time_getTime, password);

							});
			
			$("[name='red_status']").click(function() {
				$("[name='red_status']").each(function() {
					$(this).removeClass("active");
				});
				$(this).addClass("active");
				type = $(this).attr("value");
				begin = 0;
				data = "";
				$("#redPackets").empty();
				loadData(type, begin, count, loadDataCallback);
			});
			loadData(type, begin, count, loadDataCallback);
		});
		function publicProvideCode(code_money, code_num, code_type,
				start_time_getTime, end_time_getTime, password) {
			$('#redpacketzhezhao').show();
			$('.loading').show();
			var requestParams = {
				"type" : "add_promo_code",
				"data" : '{' + '"count":' + code_num + ',' + '"total_amount":'
						+ code_money + ',' + '"type":' + code_type + ','
						+ '"begin_time":' + start_time_getTime + ','
						+ '"end_time":' + end_time_getTime + ','
						+ '"password":"' + hex_md5(password) + '"}'
			}

			$.ajax({
						url : "${ctx}/api",
						type : "post",
						data : requestParams,
						dataType : "json",
						success : function(data) {	
							if (data != null && data.status == "ok") {
								$('.loading').fadeOut();
								$('#redpacketzhezhao').fadeOut();
								alert("优惠码发放成功");
								$('.zhezhao').fadeOut();
								$('.xin_code').fadeOut();
								loadData(type, begin, count, loadDataCallback);
							} else if (data != null
									&& data.status == "server_error") {
								alert("服务器内部错误");
							} else if (data != null
									&& data.status == "parameter_error") {
								alert("发送参数错误");
							} else if (data != null
									&& data.status == "token_error") {
								alert("登陆超时,请重新登陆");
							}
						},
						error : function() {
							alert("优惠码发放失败");
						}
					});
		};
	
		function loadMore(){
			$("#loadMoreBtn").css('display', 'none');
			loadData(type, begin, count, loadDataCallback);
		}
		
		function loadData(type, begin, count, loadDataCallback) {
			var requestParams = {
				"type" : "search_promo_codes",
				'data' : '{' + '"type":' + type + ',' + '"begin":' + begin
						+ ',' + '"count":' + count + '}'
			};
			$("#loading").html("<center><td colspan='3' style='text-align:center'>加载数据中，请稍后......</td></center>");
			$.ajax({
						url : "${ctx}/api",
						type : "post",
						data : requestParams,
						dataType : "json",
						success : function(data) {
							$("#loading").empty();
							if (data != null && data.status == "ok") {
								loadDataCallback(data);
							} else if (data != null
									&& data.status == "server_error") {
								alert("服务器内部错误");
							} else if (data != null
									&& data.status == "parameter_error") {
								alert("发送参数错误");
							} else if (data != null
									&& data.status == "token_error") {
								alert("登陆超时,请重新登陆");
							}
						},
						error : function() {
							$("#loading").empty();
							alert("获取数据失败");
						}
					});
		}

		function loadDataCallback(json) {
			var datas = json.promoCodes;
			var length = datas.length;
			if (datas != null && datas != undefined && length > 0) {
				var timestamp = Date.parse(new Date());
				for (var i = 0; i < length; ++i) {
					data += "<tr><td>" + localFormat(datas[i].time) + "</td>";
					data += "<td>" + datas[i].no + "</td>";
					data += "<td>" + datas[i].money + "</td>";
				}
				$("#promoCodes").empty();
				$("#promoCodes").append(data);
				begin = begin + count;

				hasMoreData = json.has_more_data;
				if (hasMoreData) {
					$("#loadMoreBtn").css('display', 'block');
				} else {
					$("#loadMoreBtn").css('display', 'none');
				}
			}
		}

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
					fmt = fmt.replace(RegExp.$1,
							(RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k])
									.substr(("" + o[k]).length)));
			return fmt;
		}
		function localFormat(time) {
			return new Date(time).Format("yyyy-MM-dd hh:mm");
		}

		$('.code_money').focus(function() {
			$('.codemo_yan').css('display', 'none');
		});

		$('.code_num').focus(function() {
			$('.codesum_yan').css('display', 'none');
		});

		$('.start_time').focus(function() {
			$('.starttime_yan').css('display', 'none');
		});

		$('.end_time').focus(function() {
			$('.endtime_yan').css('display', 'none');
		});
	</script>
</body>
</html>
