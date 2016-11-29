<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>国立看了么财务后台</title>
<link href="css/base.css" type="text/css" rel="stylesheet" />
<link href="css/jquery-ui.css" type="text/css" rel="stylesheet" />
<link href="css/perfect-scrollbar.css" type="text/css" rel="stylesheet" />
</head>

<body>
<!--首页左边-->
<div class="wrap clearfix">
    <!--首页右边-->
    <div class="wrap_r fl">
    	<!--头部-->
    	
        <!--首页开始正文内容-->
        <div class="index">
        	<!--正文头部-->
        	<div class="index_t clearfix">
            	<!--左-->
       			<div class="index_tl fl">
		           	<h1 class="index_tl1">Hello,国立看了么</h1> 
		           	<div class="xluoz">
		           		<img class="xluoz1" src="images/17.png" />
		           		<span id="zhzye" class="xluoz2">0</span>
		           		<span class="xluoz3">账户总余额</span>
		           	</div>            
		        </div>
                <!--中-->
                <div class="index_tm fl">
                	<div class="index_tm_t clearfix">
                		<input type="text" class="time_start fl" onClick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd'})"/>
                        <span class="index_tm_t1 fl"></span>
                        <input type="text" class="time_end fl" onClick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd'})"/>
                        <input type="button" class="time_cha fl" value="查询"/>
                    </div>
                    <!--动态曲线图-->
                    <div class="index_tm_m">
                    	<div id="container" style="width:100%;"></div>
                    </div>
                   <!--统计总量-->
                	<ul class="index_tm_b clearfix">
                    	<li class="fl">
                        	<span class="index_tm_b1"><img src="images/12.png" /><b id="pay_sum">0</b></span>
                            <span class="index_tm_b2">充值总额</span>
                        </li>
                        <li class="fl">
                        	<span class="index_tm_b1"><img src="images/18.png" /><b id="user_sum">0</b></span>
                            <span class="index_tm_b2">用户总人数</span>
                        </li>
                        <li class="fl">
                        	<span class="index_tm_b1"><img src="images/14.png" /><b id="withdraw_sum">0</b></span>
                            <span class="index_tm_b3">提现总额</span>
                        </li>
                    </ul>
                </div>
                <!--右-->
                <div class="index_tr fl">
                	<div class="index_trt clearfix">
                    	<h1 class="fl">健康互助资金池</h1><a class="fr" href="javascript:;">全部项目>></a>
					</div>
                    <div class="index_trjin clearfix">
                        <div class="index_trjin1 fl">
                            <div class="index_trjin1dd">
                            	<img src="images/15.png" />
                            	<span id="youth">0</span>
                            </div>    
                            <p>中青少年互助基金池</p>
                        </div>
                        <div class="index_trjin2 fl">
                            <div class="index_trjin1dd">
                           		<img src="images/16.png" />
                            	<span id="children">0</span>
                           	</div>
                            <p>儿童健康互助基金池</p>
                        </div>
                   </div>
                </div>            
            </div>
            <!--正文底部-->
            <div class="index_b clearfix">
            	<!--正文底部-左-->
            	<div class="index_bl fl contentHolder" id="Default">
                	<!--头部-->
                	<div class="index_blh1 clearfix"><h1 class="fl">发布红包最新状态</h1><span class="fr">NEW</span></div>
                    <ul class="index_bl_ul" id="red_infomation">
                    </ul>
                    <p class="index_bmli_more1" style="display: none">加载更多</p>
                </div>
                <!--正文底部-中-->
            	<div class="index_bm fl">
                	<!--头部-->
                	<div class="index_blh1 clearfix"><h1 class="fl">提现最新消息</h1><span id="message" class="fr" style="background:#f8ac59;">0条消息未读</span></div>
                	<ul class="index_bmul">
                    </ul>
                    <p class="index_bmli_more" style="display: none">加载更多</p>
                </div>
                <!--正文底部-右-->
            	<div class="index_br fl">
                	<!--头部-->
                	<div class="index_blh1 clearfix"><h1 class="fl">健康互助资金动态</h1></div>
                    <ul class="index_brul">
                    	
                    </ul>
                    <p class="index_bmli_more2" style="display: none">加载更多</p>
                </div>
            	<div class="CL"></div>
            </div>
        </div> 
    </div>
</div>
<script type="text/javascript" src="../js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.js"></script>
<script type="text/javascript" src="../js/highcharts.js"></script>
<script type="text/javascript" src="../js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="../js/perfect-scrollbar.js"></script>
<script type="text/javascript" src="../js/cycle.js"></script>
<script type="text/javascript" src="../js/raphael.js"></script>
<script type="text/javascript" src="../js/financial.js"></script>
<script type="text/javascript" src="../js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
	var begin_time;
	var end_time;
	
	var dates;
	var paydatas;
	var withdrawdatas;
	
	var red_begin=0;
	var red_data="";
	
	var withdrawals_begin=0;
	var withdrawals_data="";
	
	var health_begin=0;
	var health_data="";
	
	$('#side_nav').find('li').eq(0).addClass('focus');
	/*为了兼容在页面加载前获取一个动态曲线图*/
	/* 动态曲线图*/
	$(function(){
		$("#message").html($(window.parent.document).find("#message_num").html()+"条未读消息");
		loadData();
		loadRedData(red_begin);
		loadWithdrawalsData(withdrawals_begin);
		loadHealthProjectDetailsData(health_begin);
		var len=dates.length;
		if(len==1){
			$(".time_start").val(dates[0]);
		}else if(len>0){
			$(".time_start").val(dates[0]);
			$(".time_end").val(dates[len-1]);
		}
		/* 动态曲线图*/
		$('#container').highcharts({
			title: {
				text: '',
				x: -20 //center
			},
			subtitle: {
				text: '',
				x: -20
			},
			xAxis: {
				categories: dates
			},
			yAxis: {
				title: {
					text: '元'
				},
				plotLines: [{
					value: 0,
					width: 1,
					color: '#808080'
				}]
			},
			tooltip: {
				valueSuffix: '元'
			},
			legend: {
				layout: 'vertical',
				align: 'right',
				verticalAlign: 'middle',
				borderWidth: 0
			},
			series: [{
				name: '充值金额',
				data: paydatas
			}, {
				name: '提现总额',
				data: withdrawdatas
			}]
		});
	
		/*点击"发布红包最新状态"的加载更多按钮*/
		$('.index_bmli_more1').click(function(){
			loadRedData(red_begin);
		});

		/*点击"提现最新消息"的加载更多按钮*/
		$('.index_bmli_more').click(function(){
			loadWithdrawalsData(withdrawals_begin);
		});
	
		/*点击"健康互助资金动态"的加载更多按钮*/
		$('.index_bmli_more2').click(function(){
			loadHealthProjectDetailsData(health_begin);
		})
	});


	function loadData(begin_time,end_time) {
		var requestParam = {
			"type" : "get_graph_data",
			'data' : '{' + '"financial_token":"",' 
				+ '"begin_time":'+begin_time+','
			    + '"end_time":'+end_time+'}'
		};
		$.ajax({
			url : "${ctx}/api",
			type : "post",
			data : requestParam,
			dataType : "json",
			async:false,
			success : function(data) {
				if (data != null && data.status == "ok") {
					fillData(data);
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
				alert("服务器内部错误。");
			}
		});
	}
	
	function fillData(data) {
		dates=data.dates;
		paydatas=formatData(data.payList);
		withdrawdatas=formatData(data.withdrawList);
		$("#zhzye").html($(window.parent.document).find("#all_amount").html()+"元");
		$("#user_sum").html($(window.parent.document).find("#all_user").html());
		$('#pay_sum').html(data.pay_gold);
		$('#withdraw_sum').html(data.withdraw_gold);
		var datas = data.healthList;
		var length = datas.length;
		if (datas != null && datas != undefined && length > 0) {
			for (var i = 0; i < length; ++i) {
				if(datas[i].date_str==1){
					$('#youth').html(datas[i].gold+"元");
				}else if(datas[i].date_str==0){
					$('#children').html(datas[i].gold+"元");
				}
			}
		}
	}
	
	function formatData(data) {
		var texty='';
		var result=[];
		var len=data.length;
		for(var i=0;i<len;i++){  	
			texty +=data[i]+',';
			if(i==len-1){
				texty =texty.substr(0, texty.length - 1)
				var obj2 = texty.split(","); //字符串转化为数组
				for(var j=0;j<obj2.length;j++){//去掉双引号
					result[j]=parseFloat(obj2[j])
				}
			}
		}
		return result;
	}
	
	/*校验*/
	/*点击时间查询*/
	$('.time_cha').click(function() {
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
			if(time_end>new Date().getTime()){
				alert("结束时间不能选择以后的时间！");
				return;
			}
		}
		if (time_start > time_end) {
			alert("开始时间大于结束时间,请重新选择..");
			return;
		}
		begin_time=time_start;
		end_time=time_end;
		loadData(begin_time,end_time);
		/* 动态曲线图*/
		$('#container').highcharts({
			title: {
				text: '',
				x: -20 //center
			},
			subtitle: {
				text: '',
				x: -20
			},
			xAxis: {
				categories: dates
			},
			yAxis: {
				title: {
					text: '元'
				},
				plotLines: [{
					value: 0,
					width: 1,
					color: '#808080'
				}]
			},
			tooltip: {
				valueSuffix: '元'
			},
			legend: {
				layout: 'vertical',
				align: 'right',
				verticalAlign: 'middle',
				borderWidth: 0
			},
			series: [{
				name: '充值金额',
				data: paydatas
			}, {
				name: '提现总额',
				data: withdrawdatas
			}]
		});
	});
	
	function loadRedData(begin) {
		var requestParam = {
			"type" : "search_red_packets",
			'data' : '{' + '"begin":' + begin+ '}'
		};
		$.ajax({
			url : "${ctx}/api",
			type : "post",
			data : requestParam,
			dataType : "json",
			success : function(data) {
				$("#loading").empty();
				if (data != null && data.status == "ok") {
					var red=data.redPackets;
					var len=red.length;
					if(len>4){
						$('.index_bl_ul').css('height','auto')
						$('.index_bl').perfectScrollbar();
					}
					if (red != null && red != undefined && len > 0) {
						for (var i = 0; i < len; ++i) {
							red_data+=" <li class='index_blli clearfix'><div class='index_bld1 fl'>";
							red_data+=" <span class='index_bld1s1'><b>";
							if(red[i].username !=null && red[i].username != undefined)
								red_data+=red[i].username;
							red_data+=" </b>发布";
							if(red[i].packet_type==1)
								red_data+="企业红包:";
							else if(red[i].packet_type==2)
								red_data+="内部红包:";
							else if(red[i].packet_type==3)
								red_data+="健康互助红包:";
							red_data+="</span><span class='index_bld1s2'>";
							if(red[i].time !=null && red[i].time != undefined)
								red_data+=getDateDiff(red[i].time);
							red_data+="</span></div><div class='index_bld2 fl'>";
							red_data+="<a href='javascript:;'>总金额："+red[i].total_amount+"元</a></div></li>";
						}
					}
					$("#red_infomation").empty();
				   	$("#red_infomation").append(red_data);
				   	red_begin=red_begin+20;
				    hasMoreData = data.has_more_data;
					if (hasMoreData) {
						$(".index_bmli_more1").css('display', 'block');
					} else {
						$(".index_bmli_more1").css('display', 'none');
					}
				}
			},
			error : function() {
				$("#loading").empty();
			}
		});
	}
	
	function loadWithdrawalsData(begin) {
		var requestParam = {
	            "type": "get_user_withdrawals",
	            'data': '{'
	            + '"begin":"' + begin + '",'
	            + '"statu":0'
	            + '}'
	    };
		$.ajax({
			url : "${ctx}/api",
			type : "post",
			data : requestParam,
			dataType : "json",
			success : function(data) {
				$("#loading").empty();
				if (data != null && data.status == "ok") {
					var withdrawals=data.datas;
					var len=withdrawals.length;
					if(len>4){
						$('.index_bmul').css('height','auto')
						$('.index_bm').perfectScrollbar();
					}
					if (withdrawals != null && withdrawals != undefined && len > 0) {
						for (var i = 0; i < len; ++i) {
							withdrawals_data+=" <li> <a href='javascript:;' class='clearfix'>";
							var head_pic="";
							if(withdrawals[i].head_pic !=null && withdrawals[i].head_pic != undefined)
								head_pic=withdrawals[i].head_pic;
							withdrawals_data+=" <img name='head_pic' class='fl index_bmli1' src='"+head_pic+"'></img>";
							var nickname="";
							if(withdrawals[i].nickname !=null && withdrawals[i].nickname != undefined)
								nickname=withdrawals[i].nickname;
							withdrawals_data+="  <div class='fl index_bmli2'><p>"+nickname+"申请提现</p>";
							withdrawals_data+="  <span>"+localFormat(withdrawals[i].withdrawals_time)+"</span>";
							withdrawals_data+="   </div><b class='fr index_bmli3'>"+getDateDiff(withdrawals[i].withdrawals_time)+"</b></a> </li>";
						}
					}
					$(".index_bmul").empty();
				   	$(".index_bmul").append(withdrawals_data);
				   	$(".index_bmli1").attr("onerror","javascript:this.src='images/06.png'");
				   	withdrawals_begin=withdrawals_begin+20;
				    hasMoreData = data.has_more_data;
					if (hasMoreData) {
						$(".index_bmli_more").css('display', 'block');
					} else {
						$(".index_bmli_more").css('display', 'none');
					}
				}
			},
			error : function() {
				$("#loading").empty();
			}
		});
	}
	
	function loadHealthProjectDetailsData(begin) {
		var requestParams = {
            "type": "search_health_project_details",
            'data': '{'  + '"begin":' + begin +'}'
        };
		$.ajax({
			url : "${ctx}/api",
			type : "post",
			data : requestParams,
			dataType : "json",
			success : function(data) {
				if (data != null && data.status == "ok") {
					var healthprojectdetails=data.healthprojectdetails;
					var len=healthprojectdetails.length;
					if(len>4){
						$('.index_brul').css('height','auto')
						$('.index_br').perfectScrollbar();
					}
					if (healthprojectdetails != null && healthprojectdetails != undefined && len > 0) {
						for (var i = 0; i < len; ++i) {
							health_data+=" <li class='clearfix'><div class='index_brulz fl'>";
							health_data+=" <span class='index_brulz1'></span><span class='index_brulz2'>"+localFormat(healthprojectdetails[i].time)+"</span><span class='index_brulz3'>"+getDateDiff(healthprojectdetails[i].time)+"</span>";
							var name="";
							if(healthprojectdetails[i].name !=null && healthprojectdetails[i].name != undefined)
								name=healthprojectdetails[i].name;
							var project_name="";
							if(healthprojectdetails[i].health_project_id==0)
								project_name="在项目《少年儿童健康互助》";
							else if(healthprojectdetails[i].health_project_id==1)
								project_name="在项目《中青年健康互助》";
							var type="";
							if(healthprojectdetails[i].type==1)
								type="扣款";
							else if(healthprojectdetails[i].health_project_id==1)
								type="充值";
							health_data+=" </div><div class='index_bruly fl'><p>"+name+project_name+type+"<span>"+healthprojectdetails[i].gold+"元</span></p></div></li>";	
						}
					}
					$(".index_brul").empty();
				   	$(".index_brul").append(health_data);
				   	health_begin=health_begin+20;
				    hasMoreData = data.has_more_data;
					if (hasMoreData) {
						$(".index_bmli_more2").css('display', 'block');
					} else {
						$(".index_bmli_more2").css('display', 'none');
					}
				}
			},
			error : function() {
				$("#loading").empty();
			}
		});
	}
	
	function getDateDiff(dateTimeStamp){
		var minute = 1000 * 60;
		var hour = minute * 60;
		var day = hour * 24;
		var halfamonth = day * 15;
		var month = day * 30;
		var now = new Date().getTime();
		var diffValue = now - dateTimeStamp;
		if(diffValue < 0){return;}
		var monthC =diffValue/month;
		var weekC =diffValue/(7*day);
		var dayC =diffValue/day;
		var hourC =diffValue/hour;
		var minC =diffValue/minute;
		if(monthC>=1){
			result="" + parseInt(monthC) + "月前";
		}
		else if(weekC>=1){
			result="" + parseInt(weekC) + "周前";
		}
		else if(dayC>=1){
			result=""+ parseInt(dayC) +"天前";
		}
		else if(hourC>=1){
			result=""+ parseInt(hourC) +"小时前";
		}
		else if(minC>=1){
			result=""+ parseInt(minC) +"分钟前";
		}else
		result="刚刚";
		return result;
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
        return new Date(time).Format("yyyy-MM-dd hh:mm:ss");
    }
</script>
</body>
</html>
