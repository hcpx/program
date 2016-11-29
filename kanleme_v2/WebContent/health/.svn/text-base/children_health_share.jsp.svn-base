<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>儿童健康互助</title>
<link href="css/health.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="yhn_wrap">
<!--1-->
<ul class="yhn1 clearfix">
    <li class="fl yhn1_mb">
    	<img class="yhnli1_img" src="images/ic_indemnif.png" />
        <p class="yhnli1_p">已有保障金额</p>
        <div id="has_amount" class="yhnli1_div">0<span>元</span></div>
    </li>
    <li class="fl yhn1_mb">
    	<img class="yhnli1_img" src="images/ic_allmember.png" />
        <p class="yhnli1_p">总会员</p>
        <div id="all_member" class="yhnli1_div">0<span>人</span></div>
    </li>
    <li class="fl">
    	<img class="yhnli1_img" src="images/ic_average.png" />
        <p class="yhnli1_p">每位会员均摊金额为</p>
        <div id="share_amount" class="yhnli1_div">0<span>元</span></div>
    </li>
    <li class="fl">
    	<img class="yhnli1_img" src="images/ic_newmember.png" />
        <p class="yhnli1_p">今日新加入会员数</p>
        <div id="join_today_num" class="yhnli1_div">0<span>人</span></div>
    </li>
</ul>
<!--互助事件-->
<div class="yhn2 clearfix">
	<a id="event" class="yhn2_xina" href="young_health_event.jsp">
        <img class="yhn2_img fl" src="images/ic_help.png" />
        <span class="yhn2_span fl">互助事件</span>
    	<span id="health_event_num" class="yhn2_a fr">0<span>件></span></span>
   	</a>
</div>
<!--互助规则-->
<div class="yhn3">
	<h1 class="yhn3_h1">互助规则</h1>
    <ul class="yhn3_list">
    	<li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix">
            	<span class="yhn3_listlid1s fl">加入条件</span>
                <div class="yhn3_listlid1d fr clearfix"><span class="fl">28天-17周岁,身体健康</span><img class="fl" src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<p>1.加入年龄：出生后28天-17周岁</p>
                <p>2.保障年龄：出生后28天-17周岁，年满18周岁后将自动转入中青年抗癌计划，继续享受保障。</p>
                <p>3.为保证公平性，加入者还需要保证加入计划时身体健康，未曾患有以下重大疾病：</p>
                <div class="table-c">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="40%">肿瘤<br />原位癌<br />癌前病变</td>
                        <td width="60%">包括恶性肿瘤、白血病，但已确诊为良性<br />息肉、囊肿、结节和赘生物且已治愈的除外</td>
                    </tr>
                    <tr>
                        <td>心脑血管疾病</td>
                        <td>心脏病（心功能2级以上）、冠心病（含心绞痛、心肌梗死等）、高血压（2级或以上）、脑血管/主动脉疾病</td>
                    </tr>
                    <tr>
                        <td>慢性疾病</td>
                        <td>糖尿病、甲亢（未治疗，症状严重）、支气管哮喘（重度或以上）、肝硬化、肾脏疾病</td>
                    </tr>
                    <tr>
                        <td>传染性疾病</td>
                        <td>病毒性肝炎、艾滋病、梅毒等</td>
                    </tr>
                    <tr>
                        <td>其他</td>
                        <td>癫痫等精神科疾病、智力发育不全、身体残障等</td>
                    </tr>
                </table>
                </div>
                <p>4.认同并承诺遵守《会员公约》及计划条款</p>
            </div>
        </li>
        <li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix">
            	<span class="yhn3_listlid1s fl">互助额度</span>
                <div class="yhn3_listlid1d fr"><span class="fl">最高三十万元</span><img class="fl" src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<div class="table-c">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="50%">项目</td>
                        <td width="50%">重度癌症</td>
                    </tr>
                    <tr>
                        <td>50种重大疾病</td>
                        <td>30万元</td>
                    </tr>
                </table>
                </div>
            </div>
        </li>
        <li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix">
            	<span class="yhn3_listlid1s fl">保障范围</span>
                <div class="yhn3_listlid1d fr"><span class="fl">白血病、癌症等50种大病</span><img class="fl" src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
                <p>涵盖恶性肿瘤（俗称癌症，包含白血病）、良性脑肿瘤、严重烧伤、重大器官移植、双耳失聪、双目失明等50种重大疾病。</p>
            </div>
        </li>
        <li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix">
            	<span class="yhn3_listlid1s fl">分摊规则</span>
                <div class="yhn3_listlid1d fr"><span class="fl">单次最高不超过3元</span><img class="fl" src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<p>如有会员不幸患癌，其他会员会均摊帮助，单次均摊不超过3元。</p>
            	<div class="table-c">
	            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	                    <tr>
	                        <td width="25%">会员总数</td>
	                        <td width="25%">均摊金额</td>
	                        <td width="25%">会员互助金</td>
	                        <td width="25%">获得保障金</td>
	                    </tr>
	                    <tr>
	                        <td>5万人</td>
	                        <td>3.0元</td>
	                        <td>15万元</td>
	                        <td>15万元</td>
	                    </tr>
	                    <tr>
	                        <td>10万人</td>
	                        <td>3.0元</td>
	                        <td>30万元</td>
	                        <td>30万元</td>
	                    </tr>
	                    <tr>
	                        <td>30万人</td>
	                        <td>1.0元</td>
	                        <td>30万元</td>
	                        <td>30万元</td>
	                    </tr>
	                    <tr>
	                        <td>100万人</td>
	                        <td>0.3元</td>
	                        <td>30万元</td>
	                        <td>30万元</td>
	                    </tr>
	                </table>
                </div>
                <p>说明：</p>
                <p>(1) 如上表，会员数量越多，单次分摊金额越低。</p>
                <p>(2) 会员账户余额归本人所有，只有发生互助时才会扣除相应金额。</p>
            </div>
        </li>
        <li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix">
            	<span class="yhn3_listlid1s fl">观察期</span>
                <div class="yhn3_listlid1d fr"><span class="fl">180天(防止带病加入)</span><img class="fl" src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<p>目的是为了防止已患病或者即将患病的人加入。</p>
            	<p>在等待期内，若患病不可以申请互助金，但须履行分摊义务。等待期过后，会员如不幸罹患癌症，本计划项下的其他互助会员会为其发起互助。</p>
            </div>
        </li>
        <li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix">
            	<span class="yhn3_listlid1s fl">保障延续条件</span>
                <div class="yhn3_listlid1d fr"><span class="fl">账户余额不低于三元</span><img class="fl" src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<p>为保障会员能及时获得互助保障金，每人的账户余额不得低于3元。</p>
                <div class="table-c">
	            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	                    <tr>
	                        <td width="20%">余额</td>
	                        <td width="20%">保障状态</td>
	                        <td width="60%">余额状态</td>
	                    </tr>
	                    <tr>
	                        <td>余额≥30元</td>
	                        <td>正常</td>
	                        <td>充足</td>
	                    </tr>
	                    <tr>
	                        <td>9元≤余额<30元</td>
	                        <td>正常</td>
	                        <td>正常，建议充值至30元元以上，避免频繁充值</td>
	                    </tr>
	                    <tr>
	                        <td>3元≤余额<9元</td>
	                        <td>警示</td>
	                        <td>警示，建议及时充值，以免因余额不足失去保障</td>
	                    </tr>
	                    <tr>
	                        <td>余额<3元</td>
	                        <td>不足</td>
	                        <td>暂时失去保障，您可以在60天内充值以恢复保障，否则将自动退出，再次加入需重新度过观察期</td>
	                    </tr>
	                </table>
                </div>
                <p>说明：</p>
                <p>(1) 看了么健康互助采取用小额预缴，即收即付的方式，长期保障依赖会员缴费实现。</p>
                <p>(2) 余额过低时，我们会通过app推送或短信提醒充值。</p>
                <p>(3) 互助金由基金会独立托管并定期公示.</p>
            </div>
        </li>
        <li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix none_border">
            	<span class="yhn3_listlid1s fl">健康要求</span>
                <div class="yhn3_listlid1d fr"><span class="fl">无重大疾病史及慢性病史</span><img class="fl" src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<p>加入本互助行动时，需无重大疾病史及 相关症状就诊记录，无慢性病史</p>
            </div>
        </li>
    </ul>
</div>
<!--标签-->
<ul class="yhn4 clearfix">
	<li class="yhn4li_mb fl">
    	<div class="yhn4li_div"><img src="images/ic_finance.png" /><span>财务公开</span></div>
        <p class="yhn4li_p">项目资金定期公开</p>
    </li>
    <li class="yhn4li_mb fl">
    	<div class="yhn4li_div"><img src="images/ic_supervise.png" /><span>第三方监管</span></div>
        <p class="yhn4li_p">成都市慈善基金会托管</p>
    </li>
    <li class="fl">
    	<div class="yhn4li_div"><img src="images/ic_claim.png" /><span>理赔透明</span></div>
        <p class="yhn4li_p">互助事件全程公开透明</p>
    </li>
</ul>
<!--理赔流程-->
<div class="yhn3">
	<h1 class="yhn3_h1">理赔流程</h1>
    <ul class="yhn5_list">
    	<li>
        	<div class="yhn5_listlid clearfix"><img class="fl" src="images/yuan.png" /><span class="fl">申请赔付</span></div>
            <p class="yhn5_listlip">提交相关资料或拨打客服热线: 028-85590791</p>
        </li>
        <li>
        	<div class="yhn5_listlid clearfix"><img class="fl" src="images/yuan.png" /><span class="fl">事件调查</span></div>
            <p class="yhn5_listlip">权威机构调查,并公示调查结果</p>
        </li>
        <li>
        	<div class="yhn5_listlid clearfix"><img class="fl" src="images/yuan.png" /><span class="fl">资金划拨</span></div>
            <p class="yhn5_listlip none_border">公示无异议后，由基金会直接划拨资金</p>
        </li>
    </ul>
</div>
<!--条款-->
<div class="yhn6">
	<div class="yhn6div1 clearfix">
    	<a class="fl" href="member_action_term.html">会员公约</a>
        <a class="fr" href="profession_health_promise.html">健康承诺告知</a>
    </div>
    <div class="clearfix">
    	<a class="fl" href="fifty_sickness.html">50种重大疾病说明</a>
        <a class="fr" href="secret_term.html">隐私条款</a>
    </div>
</div>
<!--常见问题-->
<div class="yhn3" style="margin-bottom:0;">
	<h1 class="yhn3_h1">常见问题</h1>
    <ul class="yhn3_list">
    	<li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix">
            	<span class="yhn3_listlid1s fl">Q1 2岁可以加入吗？</span>
                <div class="yhn3_listlid1d fr"><img src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<p>本计划加入年龄为出生后28天-17周岁，包含17周岁。如果孩子今年17周岁，可以放心加入。</p>
            	<p>孩子年满18周岁时，会自动转入中青年抗癌计划，继续享受保障。</p>
            </div>
        </li>
        <li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix">
            	<span class="yhn3_listlid1s fl">Q2 小孩还没有身份证，怎么加入？</span>
                <div class="yhn3_listlid1d fr"><img src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<p>还户口本上有小朋友的身份证号，您可以通过户口本查找</p>
            </div>
        </li>
        <li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix">
            	<span class="yhn3_listlid1s fl">Q3 怎么申请互助，以什么为凭证？</span>
                <div class="yhn3_listlid1d fr"><img src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<p>如果患病了，可按照以下流程申请互助：</p>
                <p>1.拨打客服电话028-85590791发起申请；</p>
                <p>2.通过app平台发起申请；</p>
                <p>3.第三方专业机构核实后，全平台公示；</p>
                <p>4.公示通过无异议，成都市慈善总会从健康互助金中划款；</p>
                <p>5.国立看了是采用网络互助方式实现会员之间的相互保障，并非传统的商业保险，所以不需要保单。但加入后会有加入的电子凭证。请下载国立看了么app，点击我的‘我的健康互助’页面中可以详细看到您的姓名，身份证号，加入时间，生效时间，账户余额等；</p>
            </div>
        </li>
        <li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix">
            	<span class="yhn3_listlid1s fl">Q4 9元能保障多少时间？</span>
                <div class="yhn3_listlid1d fr"><img src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<p>您加入时所缴纳的9元并非是一次性支付，也不是按年或月缴纳的，而是预存到您国立看了么健康互助计划账户中的。</p>
                <p>如果有会员发生互助事件，申请互助金，那么其他会员将本着对每次事件最高捐助3元的规则来义务均摊互助金。每位会员应均摊的金额是从个人账户中扣除的（也就是从您缴纳的9元中）。</p>
                <p>您后期要向账户中进行充值，以保证您的账户余额不低于3元，这样您就能在保障年龄范围内一直享受保障。当您的账户余额快到3元的警戒线时，我们会通过app推送消息，和手机短信提示您充值。</p>
            </div>
        </li>
        <li class="yhn3_listli">
        	<div class="yhn3_listlid1 clearfix none_border">
            	<span class="yhn3_listlid1s fl">Q5 已有医保、商业保险，还可以加入吗？</span>
                <div class="yhn3_listlid1d fr"><img src="images/ic_open.png" /></div>
            </div>
            <div class="yhn3_listlid2">
            	<img src="images/frame_id.png" />
            	<p>还是需要的。患病后，治疗费用18-30万元。加入互助，医保和商业保险之外，又多了一份保障。</p>
                <p>三者相同点和不同点如下：</p>
                <div class="table-c">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="20%"></td>
                        <td width="20%">相同点</td>
                        <td width="60%">不同点</td>
                    </tr>
                    <tr>
                        <td>医保</td>
                        <td>提供保障√</td>
                        <td>部分医药费不在报销范围</td>
                    </tr>
                    <tr>
                        <td>商业保险</td>
                        <td>提供保障√</td>
                        <td>价格较高，每年1000~4000元</td>
                    </tr>
                    <tr>
                        <td>看了么健康互助</td>
                        <td>提供保障√</td>
                        <td>性价比高，每年花费约150元，得最高30万元保障金<br />可与医保、商业保险叠加</td>
                    </tr>
                </table>
                </div>
            </div>
        </li>
    </ul>
    <div class="yhn7_div"><a href="member_action_term.html">《会员公约》</a><a href="childre_health_term.html">《少儿健康互助计划条款》</a></div>
</div>
</div>
<div class="liukong"></div>
<a class="liji_join" href="http://kanleme.scchuangtou.com/">马上加入</a>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/health.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		loadPage();
	});

	function loadPage() {
		var requestParam = {
				"type" : "web_get_health_project_detail",
				'data' : '{"health_project_id":"0"}'
			};
			$.ajax({
			url : "${ctx}/api",
			type : "post",
			data : requestParam,
			dataType : "json",
			success : function(data) {
				if (data != null && data.status == "ok") {
					var $children = $("#join_today_num").children();
		            $("#join_today_num").html(data.join_today_num).append($children);
		            $children = $("#has_amount").children();
		            $("#has_amount").html(data.has_amount).append($children);
		            $children = $("#share_amount").children();
		            $("#share_amount").html(data.share_amount).append($children);
		            $children = $("#all_member").children();
		            $("#all_member").html(data.all_member).append($children);
		            $children = $("#health_event_num").children();
		            $("#health_event_num").html(data.health_event_num).append($children);
		            
		            $('#event').attr('href','young_health_event.jsp?health_project_id=0'); 
				}
			},
			error : function() {
			}
		});
	}
</script>
</body>
</html>
