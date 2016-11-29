<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>国立看了么财务后台</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link href="${ctx}/financial/images/logo.ico" rel="shortcut icon">

    <!-- CSS -->
    <link rel="stylesheet" href="${ctx}/financial/css/global.css">
    <link rel="stylesheet" href="${ctx}/financial/css/data-list.css">
    <link rel="stylesheet" href="${ctx}/js/jqueryui/jquery-ui.min.css">
    <link href="css/redEnvelopeList.css" type="text/css" rel="stylesheet"/>
    <style>
        #dialog-content {
            padding: 5px;
            font-size: 14px;
        }

        a {
            color: #000;
        }

        .red_status {
            height: 40px;
            padding-top: 10px;
            background: #fff;
            width: 89%;
            margin: 0 auto;
            padding: 0px;
            margin-top: 20px;
            border-top: 4px solid #e7eaec;
        }

        .red_status {
            font-size: 15px;
        }

        .red_status a {
            display: inline-block;
            padding: 4px 20px;
            background: #fff;
            margin-left: 14px;
            font-size: 15px;
            border-radius: 5px;
            border: 1px solid #e8e8e8;
        }

        .red_status a:hover {
            background: #1ab394;
            color: #fff;
        }

        .red_status a.active {
            background: #1ab394;
            color: #fff;
        }

        .yongjie_div2 {
            width: 86%;
            height: 39px;
            line-height: 46px;
            margin: 0 auto;
            margin-top: 28px;
            border-bottom: 1px solid #eaeaea;
            background: #fff;
            border-top: 4px solid #e7eaec;
        }

        .yongjie_div2 h1 {
            font-size: 16px;
            font-weight: 400;
        }
        .redpacketzhezhao{display:none;z-index:99; width:100%; height:100%; background-color:rgba(0,0,0,0.2); position:absolute; top:0; left:0;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#19000000,endColorstr=#19000000);}
    </style>
</head>
<body>
<div id="redpacketzhezhao" class="redpacketzhezhao"></div>
<div class="actin_top">
    <h2>提现管理</h2>
    <p>首页/提现管理</p>
</div>
<div class="actin_list2 yongjie_div1" style="width:94%; background:none;">
    <div class="clearfix" style="background:#fff;">
        <div class="red1 clearfix">
            <span class="red1s2 fl" style="margin-right:50px;">所有账户看币余额：<b
                    id="all_amount">0元</b></span>
            <span class="red1s2 fl">健康互助看币余额：<b id="health_amount">0元</b></span>
        </div>
        <div class="clearfix">
            <div class="index_tm_t clearfix fl">
                <span class="fl red_time">时间：</span>
                <input type="text" class="time_start fl"
                       onclick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd'})">
                <span class="index_tm_t1 fl"></span>
                <input type="text" class="time_end fl"
                       onclick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd'})">
                <input type="button" class="time_cha fl" value="查询">
            </div>
            <div class="fl re_tserch">
                最近：<span id="one_month" class="one_month yue">1个月</span><span id="three_month" class="yue">3个月</span><span id="one_year" class="yue">1年</span><span
                 id="excel_btn" style="background:#1985b3; margin-left:50px; color:#fff;">导出EXCEL</span>
            </div>
        </div>
        <div class="red_status" style="border:none; margin-left:2%;">
            状态：<a name="red_status" value="0" class="active" href="javascript:;">待审核</a><a value="1" name="red_status"
                                                                                           href="javascript:;">已通过</a>
            <a name="red_status" value="2" href="javascript:;">未通过</a><a name="red_status" value="-1"
                                                                         href="javascript:;">所有</a>
        </div>
    </div>
    <div class="container">
        <div class="yongjie_div2" style="width:96%;">
            <h1>提现管理</h1>
        </div>
        <table class="data-table" style="width:100%;">
            <thead class="data-header">
            <tr>
                <th style="width: 15%">ID</th>
                <th style="width: 15%">申请提现账户</th>
                <th style="width: 15%">提现方式</th>
                <th style="width: 8%">申请提现金额</th>
                <th style="width: 8%">手续费</th>
                <th style="width: 8%">实际提现金额</th>
                <th style="display:none;">申请提现申请人</th>
                <th style="width: 15%">申请提现时间</th>
                <th style="width: 26%">操作</th>
            </tr>
            </thead>
            <tbody id="data_body" class="data-body"/>
            <tbody id="loading"/>
        </table>
        <div class="yhe_morew">
            <button id="loadMoreData" style="display: none" class="yhe_more">查看更多</button>
        </div>
        <div id="no_pass_dialog" title="Dialog Title" style="display: none">
            <div class="luo1 clearfix">
                <span class="luo1span fl">快捷回复：</span>
                <div class="luo1se fl">
                    <span><input value="one" name="faildesc" type="radio">账号异常</span>
                    <span><input value="two" name="faildesc" type="radio">支付宝信息不完整</span>
                    <span><input value="two" name="faildesc" type="radio">其他</span>
                </div>
            </div>
            <div class="luo3 clearfix">
                <textarea id="failmessage" class="luo3te fl" style="margin-left:133px;" disabled="disabled"></textarea>
            </div>
            <div class="luo4">
                <select id="delayday" class="luo4se">
                    <option value="1">1天</option>
                    <option value="2">2天</option>
                    <option value="3">3天</option>
                    <option value="4">4天</option>
                    <option value="5">5天</option>
                </select>
                <span class="luo4s">日之内不再接受该用户申请提现</span>
            </div>
            <!--<textarea style="padding: 5px;" id="faildesc" rows="7" cols="53"></textarea>
            <p></p>
            <input style="text-align:center;" type="radio" name="green" value="0" checked="checked"/>不返还钱
              <input style="text-align:center;" type="radio" name="green" value="1" />返钱-->
        </div>
        <div id="pass_dialog" title="Dialog Title" style="display: none">
            <label id="pass_tip">确定提现吗?</label>
        </div>
    </div>
</div>
</body>
</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="../js/datePicker/WdatePicker.js"></script>
<script src="${ctx}/js/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/base64.js"></script>
<script type="text/javascript" src="${ctx}/js/tableExport.js"></script>
<script type="text/javascript">

        var begin = 0;
        var hasMoreData = false;
        var status = 0;
        var end_time = 0;
        var start_time = 0;
        var data = "";
        var $window = $(window);
        var $document = $(document);
        var lock = false;
        var noPassId;
        $("#all_amount").html($(window.parent.document).find("#all_amount").html() + "元");
        $("#health_amount").html($(window.parent.document).find("#health_amount").html() + "元");
        loadData(begin, start_time, status, end_time, loadDataCallback);

        /*点击时间查询*/
        $('.time_cha').click(function () {
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
            begin = 0;
            data = "";
            start_time = time_start;
            end_time = time_end;
            $("#data_body").empty();
            loadData(begin, start_time, end_time, status, loadDataCallback);
        });

        $('.re_tserch').find('.yue').click(function () {
            $('.re_tserch').find('.yue').removeClass('active')
            $(this).addClass('active');
        });

        /*点击"一个月"查询*/
        $('#one_month').click(function () {
            /*当前时间*/
            var curDate = new Date();
            var curDateStr = curDate.getFullYear() + "/" + (curDate.getMonth() + 1) + "/" + curDate.getDate();
            var curDateTime = new Date(curDateStr).getTime();
            /*最近一个月*/
            var one_month_time = curDateTime - 30 * 24 * 60 * 60 * 1000;
            start_time = one_month_time;
            end_time = curDateTime;
            begin = 0;
            data = "";
            $("#data_body").empty();
            loadData(begin, start_time, end_time, status, loadDataCallback);
        });

        /*点击"三个月"查询*/
        $('#three_month').click(function () {
            /*当前时间*/
            var curDate = new Date();
            var curDateStr = curDate.getFullYear() + "/" + (curDate.getMonth() + 1) + "/" + curDate.getDate();
            var curDateTime = new Date(curDateStr).getTime();
            /*最近三个月*/
            var one_month_time = curDateTime - 30 * 24 * 60 * 60 * 1000 * 3;
            start_time = one_month_time;
            end_time = curDateTime;
            begin = 0;
            data = "";
            $("#data_body").empty();
            loadData(begin, start_time, end_time, status, loadDataCallback);
        });
        /*点击"一年"查询*/
        $('#one_year').click(function () {
            /*当前时间*/
            var curDate = new Date();
            var curDateStr = curDate.getFullYear() + "/" + (curDate.getMonth() + 1) + "/" + curDate.getDate();
            var curDateTime = new Date(curDateStr).getTime();
            /*最近一年*/
            var one_month_time = curDateTime - 30 * 24 * 60 * 60 * 1000 * 12;
            start_time = one_month_time;
            end_time = curDateTime;
            begin = 0;
            data = "";
            $("#data_body").empty();
            loadData(begin, start_time, end_time, status, loadDataCallback);
        });

        $("input[name='faildesc']").click(function () {
        	if($("input[name='faildesc']:checked").parent().text()=='其他'){
        		$("#failmessage").attr("disabled",false); 
        	}else{
        		$("#failmessage").attr("disabled",true); 
        	}
        });
        
        $("#no_pass_dialog").dialog({
            autoOpen: false,
            width: 420,
            height: 400,
            title: "不通过原因",
            buttons: [
                {
                    text: "确定",
                    click: function () {
                    	var val=$("input[name='faildesc']:checked").parent().text();
                    	var id = $("#failmessage").attr("nid");
                    	var days=$("#delayday").find("option:selected").val();
                    	
                    	var delay_time=days*24*60*60*1000;
                    	if (val != null && val != undefined && val != '') {
                    		if(val=='其他'){
                    			val=$("#failmessage").val();
                    			if(val==''){
                    				alert("请填写其他原因！");
                    			}else{
                    				handling(id, val, 1,delay_time);
    	                    		$("#no_pass_dialog").dialog("close");
    	                    		$("#failmessage").val("");
    	                    		$("input[name='faildesc']").removeAttr("checked");
    	                    		$("#failmessage").attr("disabled",true); 
    	                    		$('.redpacketzhezhao').hide()
                    			}
                    		}else{
	                    		handling(id, val, 1,delay_time);
	                    		$("#no_pass_dialog").dialog("close");
	                    		$("#failmessage").val("");
	                    		$("input[name='faildesc']").removeAttr("checked");
	                    		$("#failmessage").attr("disabled",true); 
	                    		$('.redpacketzhezhao').hide()
                    		}
                    	}else{
                    		alert("请至少选择一个不予提现的原因！");
                    	}
                    }
                },
                {
                    text: "取消",
                    click: function () {
                        $(this).dialog("close");
                        $('.redpacketzhezhao').hide()
                    }
                },
            ]
        });

        $("#pass_dialog").dialog({
            autoOpen: false,
            width: 420,
            height: 300,
            title: "通过",
            buttons: [
                {
                    text: "确定",
                    click: function () {
                        $("#pass_dialog").dialog("close");
                        var id = $("#pass_tip").attr("nid");
                        handling(id, "", 0,0);
                        $('.redpacketzhezhao').hide()
                    }
                },
                {
                    text: "取消",
                    click: function () {
                        $(this).dialog("close");
                        $('.redpacketzhezhao').hide()
                    }
                }
            ]
        });

        $("[name='red_status']").click(
                function () {
                    $("[name='red_status']").each(function () {
                        $(this).removeClass("active");
                    });
                    $(this).addClass("active");
                    status = $(this).attr("value");
                    begin = 0;
                    data = "";
                    $(".data-body").empty();
                    loadData(begin, start_time, end_time, status, loadDataCallback);
                });

        function pass(e) {
            $("#pass_dialog").dialog("open");
            $('.redpacketzhezhao').show()

            noPassId = $(e).parent().parent().find("[name=withdrawals_id]").text();
            $("#pass_tip").attr("nid", noPassId);
        }

        function noPass(e) {
            $("#no_pass_dialog").dialog("open");
            $('.redpacketzhezhao').show()

            noPassId = $(e).parent().parent().find("[name=withdrawals_id]").text();
            $("#failmessage").attr("nid", noPassId);
        }
        
        $('.ui-dialog-titlebar-close').click(function(){
        	$('.redpacketzhezhao').hide()
        })

        function getStatus(status) {
            if (status == 1) {
                return "通过";
            } else if (status == 2) {
                return "不通过";
            } else if (status == 3) {
                return "异常";
            } else {
                return "待处理";
            }
        }

        function handling(userWithdrawalsID, faildesc, isRepay,ban_withdrawals_time) {
            var requestParam = {
                "type": "audit_user_withdrawals",
                'data': '{'
                + '"withdrawals_id":"' + userWithdrawalsID + '",'
                + '"faild_desc":"' + faildesc + '",'
                + '"faild_is_return_money":"' + isRepay + '",'
                + '"ban_withdrawals_time":"' + ban_withdrawals_time + '"'
                + '}'
            };
            $.ajax({
                url: "${ctx}/financialapi",
                type: "post",
                data: requestParam,
                dataType: "json",
                success: function (data) {
                    if (data != null && data.status == "ok") {
                        begin = 0;
                        $(".data-body").html("");
                        loadData(begin, start_time, end_time, status, loadDataCallback);
                        alert("已经成功处理...");
                    } else if (data.status == "parameter_error") {
                        alert("数据加载失败，参数错误!");
                    } else if (data.status == "token_error") {
                        alert("数据加载失败，用户令牌不正确!");
                    } else {
                        alert("服务器内部错误，数据加载失败" + data.status);
                    }
                },
                error: function () {
                    alert("服务器内部错误，数据加载失败");
                }
            });
        }

        function getDetail(e) {
            var userid = $(e).parent().parent().find("[name=user_id]").text();
            var time = $(e).parent().parent().find("[name=time]").text();
            var url = "${ctx}/financial/showGoldInfo.jsp?user_id=" + userid + "&time=" + time;
            window.location.href=url;
        }

        function loadData(begin, start_time, end_time, status, callback) {
            if (lock) {
                return;
            }
            lock = true;
            var requestParam = {
                "type": "get_user_withdrawals",
                'data': '{'
                + '"begin":"' + begin + '",'
                + '"start_time":"' + start_time + '",'
                + '"end_time":"' + end_time + '",'
                + '"statu":"' + status + '"'
                + '}'
            };
            $("#loading").html("<center><td colspan='8' style='text-align:center'>加载数据中，请稍后......</td></center>");
            $.ajax({
                url: "${ctx}/financialapi",
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
                        alert("服务器内部错误，数据加载失败" + data);
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

        function loadDataCallback(json) {
            var datas = json.datas;
            var length = datas.length;
            begin = begin + length;
            if (datas != null && datas != undefined && length > 0) {
                for (var i = 0; i < length; ++i) {
                    var dataHtml = "<tr id='" + datas[i].withdrawals_id + "''>";
                    dataHtml += "<td name='withdrawals_id'>" + datas[i].withdrawals_id + "</td>";
                    dataHtml += "<td style='text-align:center'>" + datas[i].withdrawals_account + "</td>";
                    var accountType = "";
                    if (datas[i].withdrawals_account_type == 1) {
                        accountType = "支付宝";
                    } else {
                        accountType = datas[i].withdrawals_account_extra;
                    }
                    dataHtml += "<td style='text-align:center'>" + accountType + "</td>";
                    dataHtml += "<td style='text-align:center'>" + datas[i].withdrawals_total + "元</td>";
                    dataHtml += "<td style='text-align:center'>" + datas[i].poundage + "元</td>";
                    dataHtml += "<td style='text-align:center'>" + datas[i].withdrawals_gold + "元</td>";
                    dataHtml += "<td style='text-align:center;display:none;' name='user_id'>" + datas[i].withdrawals_user + "</td>";
                    dataHtml += "<td style='text-align:center;display:none;' name='time'>" + datas[i].withdrawals_time + "</td>";
                    dataHtml += "<td style='text-align:center'>" + localFormat(datas[i].withdrawals_time) + "</td>";

                    if (datas[i].withdrawals_status == 0) {
                        dataHtml += "<td style='text-align:center'>" +
                                "<a href='javascript:void(0)' onclick='pass(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'><span>通过</span></a> " +
                                "<a href='javascript:void(0)' onclick='noPass(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'><span>不通过</span></a> " +
                                "<a href='javascript:void(0)' onclick='getDetail(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'><span>详情</span></a> " +
                                "</td>";
                    } else {
                        dataHtml += "<td style='text-align:center'>" + getStatus(datas[i].withdrawals_status) + "</td>";
                    }
                    dataHtml += "</tr>";
                    $(".data-body").append(dataHtml);
                }
                var container_h = $('.data-table').height() + 200;
                $('.container').css('height', container_h);
                $(window).resize(function () {
                    var container_h = $('.data-table').height() + 200;
                    $('.container').css('height', container_h);
                });
            }

            hasMoreData = json.has_more_data;
            if (hasMoreData) {
                $("#loadMoreData").css('display', 'block');
            } else {
                $("#loadMoreData").css('display', 'none');
            }
        }

        /**
         * 点击加载更多
         */
        $("#loadMoreData").click(function () {
            $("#loadMoreData").css('display', 'none');
            loadData(begin, start_time, end_time, status, loadDataCallback);
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
            return new Date(time).Format("yyyy-MM-dd hh:mm:ss");
        }
        
        $('#excel_btn').click(function(){
        	var navigatorName = "Microsoft Internet Explorer"; 
    		if( navigator.appName == navigatorName ){ 
    			alert("请选择谷歌或者火狐浏览器打开") ;
    		}else{
           $('.data-table').tableExport({type:'excel',escape:'false', tableName:'提现信息'});
    		}
        });
</script>