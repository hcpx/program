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
            	<h2>角色管理</h2>
                <p>首页/角色管理</p>
            </div>
            <!--列表-->
            <div class="actin_list">
            	<p class="actin_list1">所有角色</p>
                <div class="actin_list2">
                	<a class="actin_list2_1" href="javascript:;" onclick="window.location.reload();">刷新</a>
                    <a class="actin_list2_2" href="javascript:;">新增</a>
                    <input class="actin_list2_3" type="text" placeholder="请输入关键字" />
                    <button class="actin_list2_4">查询</button>
                </div>
                <!--table内容-->
                <div class="actin_tabdiv">
                <table cellpadding="0" cellspacing="0" class="actin_table">
                	<!--头部-->
                	<thead class="actin_tableh">
                        <tr>
                          <th style="width:12%">角色编号</th>
                          <th style="width:12%">角色名</th>
                          <th style="width:12%">创建人</th>
                          <th style="width:14%">类型</th>
                          <th style="width:15%">机构名称</th>
                          <th style="width:15%">状态</th>
                          <th style="width:20%">操作</th>
                        </tr>
                      </thead>
                      <!--中间内容-->
                      <tbody class="actin_tableb clearfix">
                       	 <tr>
                          <td>SL-sdmin</td>
                          <td>进击的兔字</td>
                          <td>admin</td>
                          <td>管理员</td>
                          <td>慈善总会</td>
                          <td>正常</td>
                          <td>
                            <a class="actin_tableb_a xiugai" href="javascript:;">修改</a>
                            <a class="actin_tableb_a" href="javascript:;">删除</a>
                          </td>
                        </tr>
                         <tr>
                          <td>SL-sdmin</td>
                          <td>进击的兔字</td>
                          <td>admin</td>
                          <td>管理员</td>
                          <td>慈善总会</td>
                          <td class="zhuantai_yellow">停用</td>
                          <td>
                            <a class="actin_tableb_a xiugai" href="javascript:;">修改</a>
                            <a class="actin_tableb_a" href="javascript:;">删除</a>
                          </td>
                        </tr>
                         <tr>
                          <td>SL-sdmin</td>
                          <td>进击的兔字</td>
                          <td>admin</td>
                          <td>管理员</td>
                          <td>慈善总会</td>
                          <td class="zhuantai_red">封禁</td>
                          <td>
                            <a class="actin_tableb_a xiugai" href="javascript:;">修改</a>
                            <a class="actin_tableb_a" href="javascript:;">删除</a>
                          </td>
                        </tr>                        
                      </tbody>
                </table>
                <div class="yhe_morew"><button class="yhe_more">查看更多</button></div>
                </div>
            </div>
        </div> 
    </div>
</div>
<!--点击"修改"出现"角色修改弹层"-->
<div class="zhezhao"></div>
<div class="jiaoxiu">
	<div class="actinttop clearfix"><span class="fl">角色信息修改</span><img class="fr" src="${ctx}/admin/images/close.png" /></div>
    <form class="jiaoxiu_form">
    	<!--用户名-->
    	<div class="fl jiaoxiu_form1">
        	<label>用户名</label>
            <input type="text" value="SD-xin" />
        </div>
        <!--角色名-->
        <div class="fl jiaoxiu_form2">
        	<label>角色名</label>
            <input type="text" value="小幸运" />
        </div>
        <div class="CL"></div>
        <!--角色类型-->
        <div class="fl jiaoxiu_form3">
        	<label>角色类型</label>
            <select>
            	<option value="admin1">系统管理员</option>
                <option value="admin2">合作组织</option>
                <option value="admin3">注册用户</option>
            </select>
        </div>
        <!--机构名称-->
        <div class="fl jiaoxiu_form4">
        	<label>机构名称</label>
            <input type="text" value="慈善机构" />
        </div>
        <div class="CL"></div>
        <!--地点-->
        <div class="fl jiaoxiu_form5">
        	<label>地点</label>
            <input type="text" value="四川省成都市华西坝地铁站" />
        </div>
        <!--状态-->
         <div class="fl jiaoxiu_form6">
        	<label>状态</label>
            <select>
            	<option value="state1">正常</option>
                <option value="state2">停用</option>
                <option value="state3">封禁</option>
            </select>
        </div>
        <div class="CL"></div>
        <!--封号时间-->
        <div class="jiaoxiu_form7">
        	<p>封号</p>
            <div class="jiaoxiu_form7ch">
            	<span><input type="radio" name="shiijan" /> 1天</span>
                <span><input type="radio" name="shiijan" /> 3天</span>
                <span><input type="radio" name="shiijan" /> 7天</span>
                <span><input type="radio" name="shiijan" /> 30天</span>
                <span><input type="radio" name="shiijan" /> 其他<input class="jiaoxiu_form7chtex" type="text" />天</span>
            </div>
        </div>
        <!--封号原因-->
        <div class="jiaoxiu_form8">
        	<label>封号原因</label>
            <select>
            	<option value="a1">垃圾广告</option>
                <option value="a2">侮辱谩骂</option>
                <option value="a3">色情低俗</option>
                <option value="a4">政治敏感</option>
                <option value="a5">违法暴力</option>
            </select>
        </div>
        <!--其他-->
        <div class="jiaoxiu_form9">
        	<label>其他</label>
            <textarea></textarea>
        </div>
        <div class="actintf_caozuo">
        	<input class="actintf_caozuo1 jiaoxiu_que no_focus" type="button" value="确定" />
            <input class="actintf_caozuo2 jiaoxiu_qu no_focus"type="button" value="取消" />
        </div>
    </form>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/adminv1.js"></script>
<script type="text/javascript">
$(function(){
	
	/*获取焦点*/
	 $(".jiaoxiu_form *").not(".no_focus").focus(function(){
		$(this).val('');
	})
	
	
	/*点击"修改"按钮出现弹出框-修改角色*/
	$('.xiugai').click(function(){
		$('.zhezhao').fadeIn();
		$('.jiaoxiu').fadeIn();
	})	
	/*点击"确定"关闭弹层-新建活动*/
	$('.jiaoxiu_que').click(function(){
		$('.zhezhao').fadeOut();
		$('.jiaoxiu').fadeOut();
	})
	/*点击"取消"关闭弹层-新建活动*/
	$('.jiaoxiu_qu').click(function(){
		$('.zhezhao').fadeOut();
		$('.jiaoxiu').fadeOut();
	})
})
</script>
</body>
</html>
