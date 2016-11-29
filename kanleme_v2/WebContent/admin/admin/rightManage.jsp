<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
	long ctime = System.currentTimeMillis();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>国立看了么管理后台</title>
<link href="${ctx}/admin/css/adminv1.css" type="text/css" rel="stylesheet" />
</head>

<body>
<div class="wrap">
    <div class="wrap_r fl">
        <div class="actin">
        	<!--头部-->
        	<div class="actin_top">
            	<h2>权限管理</h2>
                <p>首页/权限管理</p>
            </div>
            <!--角色列表-->
            <div class="actin_list">
            	<p class="actin_list1">角色列表</p>
                <div class="qx_top">
                	<span class="qx_top1"><input type="radio" checked="checked" name="admin" onclick="empty()" />系统管理员</span>
                    <span class="qx_top2"><input type="radio" name="admin" onclick="empty()" />注册用户</span>
                    <span class="qx_top3"><input type="radio" name="admin" onclick="empty()" />合作机构</span>
                </div>
            </div>
            <!--权限列表-->
            <div class="actin_list">
            	<p class="actin_list1">权限列表</p>
                <div class="qx_div1 clearfix qx_div">
                	<ul class="fl">
                    	<li class="qx_quan"><input class="d_quansejs" type="checkbox" />系统管理</li>
                        <li class="qx_fen"><input type="checkbox" />用户管理</li>
                        <li class="qx_fen"><input type="checkbox" />角色管理</li>
                        <li class="qx_fen"><input type="checkbox" />权限管理</li>
                    </ul>
                    <ul class="fl">
                    	<li class="qx_quan"><input class="d_quansejs" type="checkbox" />信息查询</li>
                        <li class="qx_fen"><input type="checkbox" />奖励查询</li>
                        <li class="qx_fen"><input type="checkbox" />推荐查询</li>
                        <li class="qx_fen"><input type="checkbox" />区域查询</li>
                    </ul>
                    <ul class="fl">
                    	<li class="qx_quan"><input class="d_quansejs" type="checkbox" />账务管理</li>
                        <li class="qx_fen"><input type="checkbox" />基本账户</li>
                        <li class="qx_fen"><input type="checkbox" />内部转账</li>
                        <li class="qx_fen"><input type="checkbox" />申请提现</li>
                        <li class="qx_fen"><input type="checkbox" />提现明细</li>
                    </ul>
                </div>
                <div class="qx_div2 clearfix qx_div">
                	<ul class="qx_div2ul fl">
                    	<li class="qx_quan"><input class="d_quansejs" type="checkbox" />资讯管理</li>
                        <li class="qx_fen"><input class="d_quansejs" type="checkbox" />公告管理</li>
                        <li>
                        	<ul>
                            	<li class="qx_fenfen"><input type="checkbox" />查看</li>
                                <li class="qx_fenfen"><input type="checkbox" />新增</li>
                                <li class="qx_fenfen"><input type="checkbox" />修改</li>
                                <li class="qx_fenfen"><input type="checkbox" />删除</li>
                            </ul>
                        </li>
                    </ul>
                    <ul class="qx_div2ul fl">
                    	<li class="qx_quan"><input class="d_quansejs" type="checkbox" />项目管理</li>
                        <li class="qx_fen"><input type="checkbox" />查看</li>
                        <li class="qx_fen"><input type="checkbox" />新增</li>
                        <li class="qx_fen"><input type="checkbox" />修改</li>
                        <li class="qx_fen"><input type="checkbox" />审核（认领）</li>
                        <li class="qx_fen"><input type="checkbox" />删除</li>
                    </ul>
                    <ul class="qx_div2ul fl">
                    	<li class="qx_quan"><input class="d_quansejs" type="checkbox" />活动管理</li>
                        <li class="qx_fen"><input type="checkbox" />查看</li>
                        <li class="qx_fen"><input type="checkbox" />新增</li>
                        <li class="qx_fen"><input type="checkbox" />修改</li>
                        <li class="qx_fen"><input type="checkbox" />审核（认领）</li>
                        <li class="qx_fen"><input type="checkbox" />删除</li>
                    </ul>
                    <ul class="qx_div2ul fl">
                    	<li class="qx_quan qx_danduq"><input class="d_quansejs" type="checkbox" />社区管理</li
                        ><li>
                        	<ul>
                            	<li class="qx_fen qx_dandu"><input class="d_quansejs" type="checkbox" />帖子管理</li>
                            	<li class="qx_fenfen"><input type="checkbox" />查看</li>
                                <li class="qx_fenfen"><input type="checkbox" />新增</li>
                                <li class="qx_fenfen"><input type="checkbox" />修改</li>
                                <li class="qx_fenfen"><input type="checkbox" />删除</li>
                            </ul>
                        </li>
                        <li>
                        	<ul>
                            	<li class="qx_fen qx_dandu"><input class="d_quansejs" type="checkbox" />举报管理</li>
                                <li class="qx_fenfen"><input type="checkbox" />查看</li>
                                <li class="qx_fenfen"><input type="checkbox" />回复</li> 
                            </ul>
                        </li>                  
                    </ul>
                    <ul class="qx_div2ul fl">
                    	<li class="qx_quan"><input class="d_quansejs" type="checkbox" />评论管理</li>
                        <li class="qx_fen"><input type="checkbox" />查看</li>
                        <li class="qx_fen"><input type="checkbox" />留言</li>
                        <li class="qx_fen"><input type="checkbox" />回复</li>
                        <li class="qx_fen"><input type="checkbox" />删除</li>
                    </ul>
                    <ul class="qx_div2ul fl">
                    	<li class="qx_quan"><input class="d_quansejs" type="checkbox" />圈子管理</li>
                        <li class="qx_fen"><input type="checkbox" />查看</li>
                        <li class="qx_fen"><input type="checkbox" />新增</li>
                        <li class="qx_fen"><input type="checkbox" />修改</li>
                        <li class="qx_fen"><input type="checkbox" />删除</li>
                    </ul>
                </div>
                <div class="qx_tijiao"><input type="button" value="确定" /></div>
            </div>
        </div> 
    </div>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/adminv1.js"></script>
<script type="text/javascript">
$(function(){
$('.qx_div input').click(function(){
	if(!$('.qx_top1 input').prop('checked')&&!$('.qx_top2 input').prop('checked')&&!$('.qx_top3 input').prop('checked')){
		alert('请先选择角色！');
		$('.qx_div input').prop('checked',false);
		return false;
	}
})
/*点击全选*/
$('.d_quansejs').on('click',function(){
	if($(this).prop('checked')){
		$(this).parent().nextAll().find('input').prop('checked',true)
	}else{
		$(this).parent().nextAll().find('input').prop('checked',false)
	}
})
/*清空数据*/
function empty(){
   $('.qx_div input').prop('checked',false);
}
/*点下方子内容为false，上面全选为false*/
$('.qx_fen input').click(function(){
	if(!$(this).prop('checked')){
		$(this).parent().siblings().eq(0).find('input').prop('checked',false);
	}
})
$('.qx_fenfen input').click(function(){
	if(!$(this).prop('checked')){
		$(this).parent().siblings().eq(0).find('input').prop('checked',false);
	}
})
$('.qx_dandu').click(function(){
	if(!$(this).prop('checked')){
		$('.qx_danduq').find('input').prop('checked',false);
	}
})
})
</script>
</body>
</html>
