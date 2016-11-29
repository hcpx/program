<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>国立看了么财务后台</title>
<link href="css/redEnvelopeList.css" type="text/css" rel="stylesheet" />
</head>
<style>
.redpacketzhezhao{display:none;z-index:99; width:100%; height:100%; background-color:rgba(0,0,0,0.2); position:absolute; top:0; left:0;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#19000000,endColorstr=#19000000);}
.loading{display:none;position:fixed;width:200px;height:200px; z-index:999; text-align:center;top:50%; margin-top:-100px;left:50%; margin-left:-100px;}
.loading img{width:100px; height:auto; margin:0 auto;}
.loading span{color:#000;display:block;width:100%;height:40px;line-height:40px;font-size:14px; text-align:center;}
</style>
<body>
<div id="redpacketzhezhao" class="redpacketzhezhao"></div>
<div class="loading"><img src="images/loading.gif" /><span>发红包中...</span></div>
	<!--首页左边-->
	<div class="wrap clearfix">
		<!--首页右边-->
		<div class="wrap_r fl">
			<!--红包管理-->
			<div class="actin">
				<!--头部-->
	        	<div class="actin_top">
	            	<h2>红包管理</h2>
	                <p>首页/红包管理</p>
	            </div>
				<div class="actin_list2 yongjie_div1">
					<div class="red1 clearfix">
						<span class="red1s1 fl add_red">添加红包</span> <span
							class="red1s2 fl">已发放金额：<b id="amount">0元</b></span>
					</div>
					<div class="clearfix">
						<div class="index_tm_t clearfix fl">
							<span class="fl red_time">时间：</span> <input type="text"
								class="time_start fl" onClick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd HH:mm:ss'})" /> <span
								class="index_tm_t1 fl"></span> <input type="text"
								class="time_end fl" onClick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd HH:mm:ss'})" /> <input
								type="button" class="time_cha fl" value="查询" />
						</div>
						<div class="fl re_tserch">
							最近：<span id="one_month" class="yue one_month">1个月</span><span id="three_month" class="yue ">3个月</span><span id="one_year" class="yue">1年</span>
						</div>
					</div>
					<div class="red_status">
						状态：<a name="red_status" value="0" href="javascript:;" class="active">全部</a><a
							name="red_status" value="2" href="javascript:;">待发放</a><a value="3" name="red_status" href="javascript:;">进行中</a>
							<a name="red_status" value="4" href="javascript:;">已发放</a>
					</div>
				</div>
				<div class="actin_list">
					<div class="yongjie_div2">
						<h1>红包管理</h1>
					</div>
					<!--table内容-->
					<div class="actin_tabdiv">
						<table cellpadding="0" cellspacing="0" class="actin_table">
							<!--头部-->
							<thead class="actin_tableh">
								<tr>
									<th style="width: 12%">ID</th>
									<th style="width: 15%">发放时间</th>
									<th style="width: 10%">总金额</th>
									<th style="width: 12%">剩余/数量</th>
									<th style="width: 10%">类型</th>
									<th style="width: 10%">红包类型</th>
									<th style="width: 13%">状态</th>
									<th style="width: 10%">创建时间</th>
									<th style="width:8%">操作</th>
								</tr>
							</thead>
							<!--中间内容-->
							<tbody id="redPackets" class="actin_tableb clearfix">
							</tbody>
							<tbody id="loading"></tbody>
						</table>
					</div>
				</div>
				<!--加载更多-->
				<div class="yhe_morew"><button id="loadMoreBtn" class="yhe_more" onClick="loadMore()" style="display:none">查看更多</button>
				</div>
			</div>
			
		</div>
		
	</div>
	<!--点击"新建"——>'系统消息'弹层-->
	<div class="zhezhao"></div>
	<div class="xin_red">
		<div class="actinttop clearfix">
			<span class="fl">添加红包</span><img class="red_close fr"
				src="images/close.png" />
		</div>
		<form class="xin_xif">
			<!--红包所属模块-->
			<div class="fl jiaoxiu_form3">
				<label>红包所属模块</label> <select class="red_mokuan">
					<option value="3">健康互助邀请好友送红包</option>
					<option value="2">系统红包</option>
				</select>
			</div>
			<!--红包金额-->
			<div class="xin_xif1 clearfix">
				<label class="fl">红包金额</label> <input class="red_money" type="text"
					placeholder="0" />
			</div>
			<!--红包数量-->
			<div class="xin_xif1 clearfix">
				<label class="fl">红包数量</label> <input class="red_mum" type="text"
					placeholder="0" />
			</div>
			<!--发放时间-->
			<div class="xin_xif1 clearfix">
				<label class="fl">发放时间</label> <input type="text" class="fa_time fl"
					onClick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
			</div>
			<!--登陆密码-->
			<div class="xin_xif1 clearfix">
				<label class="fl">登陆密码</label> <input id="pwd" type="password" />
			</div>
			<div class="actintf_caozuo">
				<input id="OK" class="actintf_caozuo1 red_sure" type="button" value="确定" />
				<input class="actintf_caozuo2 red_cancel" type="button" value="取消" />
			</div>
		</form>
	</div>
	<script type="text/javascript" src="../js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="../js/datePicker/WdatePicker.js"></script>
	<script src="${ctx}/js/md5.js"></script>
	<script type="text/javascript">
		var count = 20;
		var status = 0;
		var begin_time = 0;
		var end_time = 0;
		var begin = 0;
		var data="";

		$(function() {
			$('.re_tserch').find('.yue').click(function(){
				$('.re_tserch').find('.yue').removeClass('active')
				$(this).addClass('active');
			})
			
			/*点击"添加红包"按钮*效果*/
			$('.add_red').click(function() {
				$('.zhezhao').fadeIn();
				$('.xin_red').fadeIn();
				/*"清空表单数据*/
				$('.xin_xif')[0].reset();
			})
			/*点击"X"按钮*效果*/
			$('.red_close').click(function() {
				$('.zhezhao').fadeOut();
				$('.xin_red').fadeOut();
			})
			/****************************************************/
			/*点击"取消"按钮*效果*/
			$('.red_cancel').click(function() {
				$('.zhezhao').fadeOut();
				$('.xin_red').fadeOut();
			})
			/*校验*/
			/*点击时间查询*/
			$('.time_cha').click(function() {
				$('.re_tserch').find('.yue').each(function(){
					$('.re_tserch').find('.yue').removeClass('active')
				});
				$("#loadMoreBtn").css('display', 'none');
				var time_start = $(".time_start").val().replace(/-/gm, "/");
				var time_end = $(".time_end").val().replace(/-/gm, "/");
				if (time_start == '') {
					alert('请选择开始时间')
					return;
				} else {
					time_start = new Date(time_start).getTime();
				}
				if (time_end == '') {
					alert('请选择结束时间')
					return;
				} else {
					time_end = new Date(time_end).getTime();
				}
				if (time_start > time_end) {
					alert("开始时间大于结束时间,请重新选择..");
					return;
				}
				begin=0;
				data="";
				begin_time=time_start;
				end_time=time_end;
				$("#redPackets").empty();
				loadData(status, begin_time, begin,count, end_time, loadDataCallback);
			})

			/*点击“一个月”查询*/
			$('#one_month').click(
					function() {
						$(".time_start").val("");
			        	$(".time_end").val("");
			        	$("#loadMoreBtn").css('display', 'none');
						/*当前时间*/
						var curDate = new Date();
						var curDateStr = curDate.getFullYear() + "/"
								+ (curDate.getMonth() + 1) + "/"
								+ curDate.getDate();
						var curDateTime = new Date(curDateStr).getTime();
						/*最近一个月*/
						var one_month_time = curDateTime - 30 * 24 * 60 * 60 * 1000;
						begin=0;
						data="";
						begin_time=one_month_time;
						end_time=curDateTime;
						$("#redPackets").empty();
						loadData(status, begin_time, begin,count, end_time, loadDataCallback);
					})
			/*点击"三个月"查询*/
			$('#three_month').click(
					function() {
						$(".time_start").val("");
			        	$(".time_end").val("");
			        	$("#loadMoreBtn").css('display', 'none');
						/*当前时间*/
						var curDate = new Date();
						var curDateStr = curDate.getFullYear() + "/"
								+ (curDate.getMonth() + 1) + "/"
								+ curDate.getDate();
						var curDateTime = new Date(curDateStr).getTime();
						/*最近三个月*/
						var one_month_time = curDateTime - 30 * 24 * 60 * 60
								* 1000 * 3;
						begin=0;
						data="";
						begin_time=one_month_time;
						end_time=curDateTime;
						$("#redPackets").empty();
						loadData(status, begin_time, begin,count, end_time, loadDataCallback);
					})
			/*点击"一年"查询*/
			$('#one_year').click(
					function() {
						$(".time_start").val("");
			        	$(".time_end").val("");
			        	$("#loadMoreBtn").css('display', 'none');
						/*当前时间*/
						var curDate = new Date();
						var curDateStr = curDate.getFullYear() + "/"
								+ (curDate.getMonth() + 1) + "/"
								+ curDate.getDate();
						var curDateTime = new Date(curDateStr).getTime();
						/*最近一年*/
						var one_month_time = curDateTime - 30 * 24 * 60 * 60
								* 1000 * 12;
						begin=0;
						data="";
						begin_time=one_month_time;
						end_time=curDateTime;
						$("#redPackets").empty();
						loadData(status, begin_time, begin,count, end_time, loadDataCallback);
					})
			/*点击“表单”确定按钮*/
			$('.red_sure').click(
					function() {
						var red_mokuan = $('.red_mokuan').val();
						var red_money = $('.red_money').val();
						var red_mum = $('.red_mum').val();
						var pwd = $('#pwd').val();
						var fa_time = $(".fa_time").val().replace(/-/gm, "/");
						/*当前时间*/
						var curDate = new Date();
						var curDateStr = curDate.getFullYear() + "/"
								+ (curDate.getMonth() + 1) + "/"
								+ curDate.getDate() + " "+curDate.getHours()
								+ ":"+curDate.getMinutes()
								+ ":"+curDate.getSeconds();
						var curDateTime = new Date(curDateStr).getTime();
						var fa_time_getTime = new Date(fa_time).getTime();

						if (red_mokuan == '') {
							alert('请选择红包所属模块');
							return;
						}
						if (red_money == '') {
							alert('请输入红包金额');
							return;
						}
						if (red_mum == '') {
							alert('请输入红包数量');
							return;
						}
						if (pwd == '') {
							alert('请填写登陆密码');
							return;
						}
						if (fa_time == '') {
							fa_time_getTime=curDateTime;
						}
						publish_red_packet(pwd,fa_time_getTime,red_mokuan,red_money,red_mum);
					});
			$("[name='red_status']").click(
					function() {
						$("#loadMoreBtn").css('display', 'none');
						$("[name='red_status']").each(function(){
							$(this).removeClass("active");
						});
						$(this).addClass("active");
						status=$(this).attr("value");
						begin=0;
						data="";
						$("#redPackets").empty();
						loadData(status, begin_time, begin,count, end_time, loadDataCallback);
					});
			loadData(status, begin_time, begin,count, end_time, loadDataCallback);
		});

		function loadData(status, begin_time, begin,count, end_time,
				callback) {
			var requestParam = {
				"type" : "search_red_packets",
				'data' : '{' + '"status":' + status + ',' + '"begin_time":'
						+ begin_time + ','+ '"begin":' + begin+ ','
						+ '"count":' + count + ',' + '"end_time":' + end_time
						+ '}'
			};
			$("#loading").html("<center><td colspan='8' style='text-align:center'>加载数据中，请稍后......</td></center>");
			$.ajax({
				url : "${ctx}/api",
				type : "post",
				data : requestParam,
				dataType : "json",
				success : function(data) {
					$("#loading").empty();
					if (data != null && data.status == "ok") {
						callback(data);
					}
				},
				error : function() {
					$("#loading").empty();
				}
			});
		}
		
		function loadMore(){
			$("#loadMoreBtn").css('display', 'none');
			loadData(status, begin_time, begin,count, end_time,loadDataCallback);
		}
		
		function loadDataCallback(json) {
			var datas = json.redPackets;
			var length = datas.length;
			if (datas != null && datas != undefined && length > 0) {
				//对红包列表进行填充
				var timestamp = Date.parse(new Date());
				$("#amount").text(json.total_amount+'元');
				for (var i = 0; i < length; ++i) {
					data += "<tr><td name='red_id'>"+datas[i].red_packet_id+"</td><td>"+localFormat(datas[i].begin_time)+"</td>";
					data += "<td>"+datas[i].total_amount+"</td>";
					data += "<td>"+datas[i].remainSize+"/"+datas[i].count+"</td>";
					var type='';
					if(datas[i].type==0)
						type='口令红包';
					if(datas[i].type==1)
						type='普通红包';
					data += "<td>"+type+"</td>";
					var packettype='';
					if(datas[i].packet_type==1)
						packettype='企业红包';
					if(datas[i].packet_type==2)
						packettype='系统红包';
					if(datas[i].packet_type==3)
						packettype='健康互助红包';
					data += "<td>"+packettype+"</td>";
					var status='';
					if(timestamp<datas[i].begin_time)
						status='待发放';
					if(timestamp>=datas[i].begin_time && datas[i].remainSize>0)
						status='进行中';
					if(timestamp>=datas[i].begin_time && datas[i].remainSize<=0)
						status='已发放';
					data += "<td>"+status+"</td>";
					data += "<td>"+localFormat(datas[i].time)+"</td>";
					if (datas[i].status == 0) {
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
				$("#redPackets").empty();
			   	$("#redPackets").append(data);
				begin=begin+count;
			    
			    hasMoreData = json.has_more_data;
				if (hasMoreData) {
					$("#loadMoreBtn").css('display', 'block');
				} else {
					$("#loadMoreBtn").css('display', 'none');
				}
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
	        return new Date(time).Format("yyyy-MM-dd hh:mm");
	    }
	    
	    function publish_red_packet(pwd,fa_time_getTime,red_mokuan,red_money,red_mum) {
	    	$('#redpacketzhezhao').show();
			$('.loading').show();
			var requestParam = {
				"type" : "financial_publish_red_packet",
				'data' : '{' + '"begin_time":' + fa_time_getTime + ',' + '"packet_type":'
						+ red_mokuan + ',' + '"count":' + red_mum + ','+ '"password":"' + hex_md5(pwd) + '",'
						+ '"total_amount":' + red_money+ '}'
			};
			
			$.ajax({
				url : "${ctx}/api",
				type : "post",
				data : requestParam,
				dataType : "json",
				success : function(data) {
					$('.zhezhao').fadeOut();
					$('#redpacketzhezhao').fadeOut();
					$('.loading').hide();
					$('.xin_red').hide();
					if (data != null && data.status == "ok") {
						alert("红包发送成功。");
						location.reload();
					}else if (data != null && data.status == "server_error") {
						alert("服务器内部错误。");
					}else if (data != null && data.status == "parameter_error") {
						alert("发送参数错误。");
					}else if (data != null && data.status == "password_error") {
						alert("密码错误。");
					}else if (data != null && data.status == "token_error") {
						alert("登陆超时,请重新登陆。");
					}
				},
				error : function() {
					alert("红包发送失败。");
				}
			});
		}
	    
	    function disables(e) {
	        var name = $(e).attr("name");
	        var red_id = $(e).parent().parent().find("[name=red_id]").text();
	        var requestParams = {
	                "type": "red_packets_manage",
	                'data': '{' + '"financial_token":""' + ',' + '"red_id":"' + red_id
	                + '"}'
	            };
            $.ajax({
                     url: "${ctx}/api",
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
            )
	    };
	</script>
</body>
</html>
