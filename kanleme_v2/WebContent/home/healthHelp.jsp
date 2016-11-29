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
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<meta name="description" content="国立看了么是一个真实可靠的网络互助平台。看了么健康互助计划是由看了么公益基金发起的，为弱势群体定制的专属保障方案。在这里，人们守望互助，共同抵御重大疾病、癌症的风险。"/>
<meta name="keywords" content="看了么官网,国立看了么,看了么，国立看了么，健康互助，网络互助,互助平台,互助组织"/>
<title>国立看了么</title>
<link href="css/kanleme_web.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
</head>

<body>
<!--top-->
<div class="top">
	<div class="topc clearfix">
    	<a href="../index.html" class="topc_logo fl">
        	<img src="images/logo.png" />
            <span>国立看了么</span>
        </a>
        <div class="fr topc_list">
        	<a href="../index.html">首页</a>
            <a class="active" href="healthHelp.jsp">健康互助</a>
            <a href="appDownload.html">APP下载</a>
            <a href="helpCentre.html">帮助中心</a>
            <a href="contact.html">联系我们</a>
        </div>
    </div>
</div>
<!--第一部分-->
<div class="hHp1">
	<div class="hHp1c hHp1c_change1">
    	<p class="hHp1cp1">中青年健康互助</p>
    	<img class="hHp3img1" src="images/helthhlpe03.png" />
    	<img class="xin_jiaru1" src="images/helthhlpe04.png" />
        <p class="hHp1cp2">预存9元，最高可获得30万保障</p>
    </div>
    <div class="hHp1c hHp1c_change2">
    	<p class="hHp1cp1">中青年健康互助</p>
    	<img class="hHp3img1" src="images/helthhlpe03.png" />
        <img class="hHp1cimg xin_jiaru1img" src="images/app_code.png" />
        <p class="hHp1cp2">预存9元，最高可获得30万保障</p>
    </div>
</div>
<!--第二部分-->
<div class="hHp2">
	<div class="hHp2c">
        <div class="hHp2cd1 clearfix">
            <span id="young_persons" class="hHp2cd1s1 fl">保障人群：年龄为18-50周岁</span>
            <span class="hHp2cd1s2 fr" id="young_scope">保障范围：恶性肿瘤等六十种重大疾病</span>
        </div>
        <ul class="hHp2cd2 clearfix">
            <li class="hHp2cd2l1 fl">总金额：<span id="young_has_amount">0元</span></li>
            <li class="hHp2cd2l2 fl">已加入会员：<span id="young_all_member">0元</span></li>
            <li class="hHp2cd2l3 fl">救助均摊金额：<span id="young_share_amount">0元</span></li>
        </ul>
    </div>
</div>
<!--第三部分-->
<div class="hHp3">
	<div class="hHp3c hHp1c_change1_1">
        <p class="hHp3cp1">少年儿童健康互助</p>
        <img class="hHp3img1" src="images/helthhlpe03.png" />
        <img class="xin_jiaru2" src="images/helthhlpe04.png" />
        <p class="hHp3cp2">预存9元，最高可获得30万保障</p>
    </div>
    <div class="hHp3c hHp1c_change2_1">
        <p class="hHp3cp1">少年儿童健康互助</p>
        <img class="hHp3img1" src="images/helthhlpe03.png" />
        <img class="hHp1cimg xin_jiaru2img" src="images/app_code.png" />
        <p class="hHp3cp2">预存9元，最高可获得30万保障</p>
    </div>
</div>
<!--第四部分-->
<div class="hHp2">
	<div class="hHp2c">
        <div class="hHp2cd1 clearfix">
            <span id="children_persons" class="hHp2cd1s1 fl">保障人群：出生28天-17周岁</span>
            <span class="hHp2cd1s2 fr" id="children_scope">保障范围：白血病、癌症等五十种大病</span>
        </div>
        <ul class="hHp2cd2 clearfix">
            <li class="hHp2cd2l1 fl">总金额：<span id="children_has_amount">0元</span></li>
            <li class="hHp2cd2l2 fl">已加入会员：<span id="children_all_member">0元</span></li>
            <li class="hHp2cd2l3 fl">救助均摊金额：<span id="children_share_amount">0元</span></li>
        </ul>
    </div>
</div>
<!--第五部分-->
<div class="hHp5">
	<div class="hHp5c">
        <div class="hHp5cd">
            <h1 class="hHp5cdh1">信用背书</h1>
            <p class="hHp5cdp">CREDIT ENDORSEMENT</p>
            <span class="hHp5cds"></span>
        </div>
        <ul class="hHp5cu clearfix">
            <li class="hHp5culi1 fl">
            	<div class="hHp5culi1dd">
                    <div class="hHp5culid">
                        <h1 class="hHp5culidh1">资金透明</h1>
                        <p class="hHp5culidp">TRANSPARENT CAPITAL</p>
                    </div>
                    <div class="hHp5culib">
                        <p class="hHp5culibp">看了么健康互助计划的所有资金将由成都市慈善总会全程监管，每个互助项目都将于国立看了么APP和看了么新媒体平台（微信、微博、今日头条等）全程公示，并定期公布财务报表，自愿接受看了么全体会员的监督。</p>
                    </div>
                </div>
            </li>
            <li class="hHp5culi2 fl active">
            	<div class="hHp5culi2dd">
                    <div class="hHp5culid">
                        <h1 class="hHp5culidh1">合作单位</h1>
                        <p class="hHp5culidp">COOPERATIVE UNIT</p>
                    </div>
                    <div class="hHp5culib">
                        <p class="hHp5culibp">成都市慈善总会成立于1995年5月12日（原名成都慈善会），是经成都市人民政府批准依法注册成立的全市性非营利公益社会组织，有赖社会各界热心人士和团体的支持，成都市慈善总会以扶老、助残、救孤、济困、赈灾为宗旨，坚持务实创新，依法实施各项慈善救助，打造"阳光"系列慈善救助品牌，初步形成了以帮困助学为主、涵盖建房、助医、扶贫等方面的慈善救助体系，充分发挥了慈善事业在社会保障体系中的重要补充作用，为构建和谐社会做出了积极贡献，得到社会各界的普遍好评。</p>
                    </div>
                </div>
            </li>
            <li class="hHp5culi3 fr">
            	<div class="hHp5culi3dd">
                    <div class="hHp5culid">
                        <h1 class="hHp5culidh1">合作方式</h1>
                        <p class="hHp5culidp">COOPERATION MODE</p>
                    </div>
                    <div class="hHp5culib">
                        <p class="hHp5culibp">看了么健康互助计划的所有资金将由成都市慈善总会全程监管，每个互助项目都将于国立看了么APP和看了么新媒体平台（微信、微博、今日头条等）全程公示，并定期公布财务报表，自愿接受看了么全体会员的监督。</p>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</div>
<!--联系我们-->
<div class="conta">
	<div class="contac">
    	<div class="repoc_top">
            <h1 class="repoc_toph1">联系我们</h1>
            <p class="repoc_topp">MEDIA COVERAGE</p>
            <span class="repoc_tops"></span>
        </div>
        <div class="contacul_wrap">
        <ul class="contacul clearfix">
        	<li class="fl" style="margin-right:1%;">
            	<img class="contaculimg" src="images/con_01.png" />
                <p class="contaculp1">公司地址</p>
                <span class="contaculs"></span>
                <p class="contaculp2">成都市武侯区小天竺街75号财富国际16F</p>
            </li>
            <li class="fl" style="margin-right:1%;">
            	<img class="contaculimg" src="images/con_02.png" />
                <p class="contaculp1">联系方式</p>
                <span class="contaculs"></span>
                <p class="contaculp2">028-85590791(周一至周日9:00—17:30)</p>
            </li>
            <li class="fr">
            	<img class="contaculimg" src="images/con_03.png" />
                <p class="contaculp1">电子邮件</p>
                <span class="contaculs"></span>
                <p class="contaculp2">2597420394@qq.com</p>
            </li>
        </ul>
        </div>
    </div>
</div>

<!--foot-->
<div class="foot">
	<div class="footc clearfix">
    	<ul class="footclist clearfix fl">
        	<li class="fl">
            	<h1>关于看了么</h1>
                <a href="../index.html">国立看了么介绍</a>
                <a href="healthHelp.jsp">健康互助</a>
            </li>
            <li class="fl">
            	<h1>用户服务</h1>
                <a href="helpCentre.html">帮助中心</a>
                <a href="member_action_term.html">会员公约</a>
                <a href="secret_term.html">隐私条款</a>
            </li>
            <li class="fl">
            	<h1>关注我们</h1>
                <a target="_blank" href="http://weibo.com/scchuangtou?refer_flag=1001030102_&is_hot=1">新浪微博</a>
                <a href="showProjectState.html">项目动态</a>
            </li>
        </ul>
        <ul class="footclist2 fr">
        	<li class="fl">
            	<img src="images/life_code.jpg" />
                <span>看了么优生活<br />(订阅号)</span>
            </li>
            <li class="fl">
            	<img src="images/wei_code.jpg" />
                <span>看了么公益<br />(服务号)</span>
            </li>
        </ul>
    </div>
</div>
<!--bottom-->
<div class="bottom">
<p>Copyright © 2016-2026 guolichuangtou. All Rights Resreved.</p>
</div>
<a class="back-to-top"><img src="images/ic_top.png" /></a>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/base.js"></script>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		loadYoung();
		loadChildren();
	});

	function loadYoung() {
		var requestParam = {
				"type" : "web_get_health_project_detail",
				'data' : '{"health_project_id":"1"}'
			};
			$.ajax({
			url : "${ctx}/api",
			type : "post",
			data : requestParam,
			dataType : "json",
			success : function(data) {
				if (data != null && data.status == "ok") {
					/*var inner="保障人群：年龄为";
					if(data.min_age!=null && data.min_age!=undefined)
						inner+=data.min_age;
					if(data.min_type==0)
						inner+="天-";
					else
						inner+="周岁-";
					if(data.max_age!=null && data.max_age!=undefined)
						inner+=data.max_age;
					if(data.max_type==0)
						inner+="天";
					else
						inner+="周岁";
					$("#young_persons").html(inner);*/
					if(data.has_amount!=null && data.has_amount!=undefined)
						$("#young_has_amount").html(data.has_amount+"元");
					if(data.all_member!=null && data.all_member!=undefined)
						$("#young_all_member").html(data.all_member+"人");
					if(data.share_amount!=null && data.share_amount!=undefined)
						$("#young_share_amount").html(data.share_amount+"元");
					if(data.scope!=null && data.scope!=undefined)
						$("#young_scope").html("保障范围："+data.scope);
				}
			},
			error : function() {
			}
		});
	}
	
	function loadChildren() {
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
					/*inner="保障人群：年龄为";
					if(data.min_age!=null && data.min_age!=undefined)
						inner+=data.min_age;
					if(data.min_type==0)
						inner+="天-";
					else
						inner+="周岁-";
					if(data.max_age!=null && data.max_age!=undefined)
						inner+=data.max_age;
					if(data.max_type==0)
						inner+="天";
					else
						inner+="周岁";
					$("#children_persons").html(inner);*/
					if(data.has_amount!=null && data.has_amount!=undefined)
						$("#children_has_amount").html(data.has_amount+"元");
					if(data.all_member!=null && data.all_member!=undefined)
						$("#children_all_member").html(data.all_member+"人");
					if(data.share_amount!=null && data.share_amount!=undefined)
						$("#children_share_amount").html(data.share_amount+"元");
					if(data.scope!=null && data.scope!=undefined)
						$("#children_scope").html("保障范围："+data.scope);
				}
			},
			error : function() {
			}
		});
	}
</script>
</html>
