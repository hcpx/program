<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>看了么公益财务管理</title>
    <link href="css/jquery-ui.css" type="text/css" rel="stylesheet"/>
    <link href="css/redEnvelopeList.css" type="text/css" rel="stylesheet"/>
</head>

<body>
<!--首页左边-->
<div class="wrap">
    <!--首页右边-->
    <div class="wrap_r fl">
        <!--红包管理-->
        <div class="actin">
            <!--头部-->
            <div class="actin_top">
                <h2>健康互助管理</h2>
                <p>首页/健康互助管理</p>
            </div>
            <div class="actin_list2 yongjie_div1">
                <div class="red1 clearfix">
                    <span class="red1s2 fl"
                          style="margin-right:50px;">中青年健康互助余额：<b id="youth">0元</b></span>
                    <span class="red1s2 fl">少年儿童健康互助余额：<b id="child">0元</b></span>
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
                        最近：<span id="one_month" class="yue one_month">1个月</span><span id="three_month" class="yue ">3个月</span><span id="one_year" class="yue">1年</span><span
                          id="excel_btn"  style="background:#1985b3; margin-left:50px; color:#fff;">导出EXCEL</span>
                    </div>
                    <!--加载更多-->
                </div>
            </div>
            <div class="actin_list">
                <div class="yongjie_div2">
                    <h1>健康互助</h1>
                </div>
                <!--table内容-->
                <div class="actin_tabdiv">
                    <table cellpadding="0" cellspacing="0" class="actin_table">
                        <!--头部-->
                        <thead class="actin_tableh">
                        <tr>
                            <th style="width:8%">用户名</th>
                            <th style="width:8%">ID</th>
                            <th style="width:10%">时间</th>
                            <th style="width:8%">交易种类</th>
                            <th style="width:8%">交易号</th>
                            <th style="width:8%">金额（元）</th>
                            <th style="width:8%">互助类别</th>
                            <th style="width:10%">健康互助金额（￥）</th>
                            <th style="width:11%">操作</th>
                        </tr>
                        </thead>
                        <!--中间内容-->
                        <tbody id="healthMutual" class="actin_tableb clearfix"></tbody>
                        <tbody id="loading"></tbody>
                    </table>
                    <div class="yhe_morew">
                        <button id="loadMoreData" style="display:none;" class="yhe_more">查看更多</button>
                    </div>
                </div>
            </div>
        </div>

    </div>

</div>

<script type="text/javascript" src="../js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="../js/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/js/base64.js"></script>
<script type="text/javascript" src="${ctx}/js/tableExport.js"></script>
<script type="text/javascript">
    function disables(e) {
        var name = $(e).attr("name");
        var healthProjectMemberId = $(e).parent().parent().find("[name=health_project_member_id]").text();
        var requestParams = {
            "type": "admin_health_project_members_manage",
            'data': '{' + '"financial_token":""' + ',' + '"health_project_member_id":"' + healthProjectMemberId
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
        );
    };

    $(function () {
        var end_time = 0;
        var begin_time = 0;
        var begin = 0;
        var count = 20;
        var data = "";
        var $window = $(window);
        var $document = $(document);
        var lock = false;
        $("#youth").html($(window.parent.document).find("#youth_amount").html()+"元");
        $("#child").html($(window.parent.document).find("#children_amount").html()+"元");
        /****************************************************/
        /*点击"取消"按钮*效果*/
        $('.red_cancel').click(function () {
            $('.zhezhao').fadeOut();
            $('.xin_red').fadeOut();
        });
        /*校验*/
        /*点击时间查询*/
        $('.time_cha').click(function () {
            var time_start = $(".time_start").val().replace(/-/gm, "/");
            var time_end = $(".time_end").val().replace(/-/gm, "/");
            $('.re_tserch').find('.yue').each(function(){
                $('.re_tserch').find('.yue').removeClass('active')
            })
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
            begin_time = time_start;
            end_time = time_end;
            $("#healthMutual").empty();
            loadData(begin_time, begin, count, end_time, loadDataCallback);
        });

        $('.re_tserch').find('.yue').click(function(){
            $('.re_tserch').find('.yue').removeClass('active')
            $(this).addClass('active');
        });

        /*点击"一个月"查询*/
        $('#one_month').click(function () {
        	$(".time_start").val("");
        	$(".time_end").val("");
        	$("#loadMoreData").css('display', 'none');
            /*当前时间*/
            var curDate = new Date();
            var curDateStr = curDate.getFullYear() + "/" + (curDate.getMonth() + 1) + "/" + curDate.getDate();
            var curDateTime = new Date(curDateStr).getTime();
            /*最近一个月*/
            var one_month_time = curDateTime - 30 * 24 * 60 * 60 * 1000;
            begin_time = one_month_time;
            end_time = curDateTime;
            begin = 0;
            data = "";
            $("#healthMutual").empty();
            loadData(begin_time, begin, count, end_time, loadDataCallback);
        });

        /*点击"三个月"查询*/
        $('#three_month').click(function () {
        	$(".time_start").val("");
        	$(".time_end").val("");
        	$("#loadMoreData").css('display', 'none');
            /*当前时间*/
            var curDate = new Date();
            var curDateStr = curDate.getFullYear() + "/" + (curDate.getMonth() + 1) + "/" + curDate.getDate();
            var curDateTime = new Date(curDateStr).getTime();
            /*最近三个月*/
            var one_month_time = curDateTime - 30 * 24 * 60 * 60 * 1000 * 3;
            begin_time = one_month_time;
            end_time = curDateTime;
            begin = 0;
            data = "";
            $("#healthMutual").empty();
            loadData(begin_time, begin, count, end_time, loadDataCallback);
        });
        /*点击"一年"查询*/
        $('#one_year').click(function () {
        	$(".time_start").val("");
        	$(".time_end").val("");
        	$("#loadMoreData").css('display', 'none');
            /*当前时间*/
            var curDate = new Date();
            var curDateStr = curDate.getFullYear() + "/" + (curDate.getMonth() + 1) + "/" + curDate.getDate();
            var curDateTime = new Date(curDateStr).getTime();
            /*最近一年*/
            var one_month_time = curDateTime - 30 * 24 * 60 * 60 * 1000 * 12;
            begin_time = one_month_time;
            end_time = curDateTime;
            begin = 0;
            data = "";
            $("#healthMutual").empty();
            loadData(begin_time, begin, count, end_time, loadDataCallback);
        });

        loadData(begin_time, begin, count, end_time, loadDataCallback);
        function loadData(begin_time, begin, count, end_time, callBack) {
            if (lock){
                return;
            }
            lock = true;
            var requestParams = {
                "type": "search_health_project_details",
                'data': '{' + '"begin_time":'
                + begin_time + ',' + '"begin":' + begin + ','
                + '"count":' + count + ',' + '"end_time":' + end_time
                + '}'
            };
            $("#loading").html("<center><td colspan='8' style='text-align:center'>加载数据中，请稍后......</td></center>");
            $.ajax({
                url: "${ctx}/api",
                type: "post",
                data: requestParams,
                dataType: "json",
                success: function (data) {
                    $("#loading").empty();
                    if (data != null && data.status == "ok") {
                        begin = requestParams.data.begin;
                        loadDataCallback(data);
                    }
                    lock = false;
                },
                error: function () {
                    $("#loading").empty();
                    lock = false;
                }
            });
        }

        function loadDataCallback(data) {
            var hasMoreData = data.has_more_data;
            var datas = data.healthprojectdetails;
            var len = datas.length;
            if (datas != null && datas != undefined && len > 0) {
                begin = begin+len;
                for (var i = 0; i < len; i++) {
                    data += "<tr><td>" + datas[i].name + "</td>";
                    data += "<td name='health_project_member_id'>" + datas[i].health_project_member_id + "</td>";
                    data += "<td>" + localFormat(datas[i].time) + "</td>";
                    var type = "";
                    if (datas[i].type == 0) {
                        type = '看币';
                    } else if (datas[i].type == 1) {
                        type = '扣款';
                    } else if (datas[i].type == 2) {
                        type = '微信';
                    } else if (datas[i].type == 3) {
                        type = '支付宝';
                    }
                    data += "<td>" + type + "</td>";
                    data += "<td>" + datas[i].health_project_gold_change_id + "</td>";
                    data += "<td>" + datas[i].gold + "元</td>";
                    var healthType = "";
                    if (datas[i].health_project_id == 0) {
                        healthType = "少儿";
                    } else {
                        healthType = "中青年";
                    }
                    data += "<td>" + healthType + "</td>";
                    data += "<td>" + datas[i].balance + "元</td>";
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
                $("#healthMutual").append(data);
                var container_h = $('.data-table').height() + 200;
                $('.container').css('height', container_h);
                $(window).resize(function () {
                    var container_h = $('.data-table').height() + 200;
                    $('.container').css('height', container_h);
                });
            }

            if (hasMoreData) {
                $("#loadMoreData").css('display', 'block');
            } else {
                $("#loadMoreData").css('display', 'none');
            }
        }

        $("#loadMoreData").click(function() {
            $("#loadMoreData").css('display', 'none');
            loadData(begin_time, begin, count, end_time, loadDataCallback);
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
    });
    
	 $('#excel_btn').click(function(){
	    	var navigatorName = "Microsoft Internet Explorer"; 
			if( navigator.appName == navigatorName ){ 
				alert("请选择谷歌或者火狐浏览器打开") ;
			}else{
	        $('.actin_table').tableExport({type:'excel',escape:'false', tableName:'健康互助信息'});
	    }
	 });
</script>
</body>
</html>
