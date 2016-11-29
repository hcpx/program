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
<title>看了么公益后台管理</title>
<link href="${ctx}/admin/css/adminv1.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/admin/css/tiezi.css" type="text/css" rel="stylesheet" />
<style>
.z_fuadd{width: 24px;height: auto; vertical-align: middle;margin-top: -3px; cursor: pointer;}
.nnee_tiet_yan{color:#f00;font-size:14px; width:280px; margin:0 auto;margin-top:10px;height:20px;}
.nnee_tiec_yan{color:#f00;font-size:14px; width:280px; margin:0 auto;margin-top:10px;height:20px;}
.xin_tiefzi input{margin-top:10px;}
.xin_tiefzi img{width:100px;height:100px; position:inherit; margin:10px 0;}
.nnee_tiese{width:336px; margin:0 auto;}
.nnee_tiese li input{display: inline-block; width: 15px;height: 15px;line-height: 30px;font-size: 14px;
background: #eeeeee;padding: 0 2%; border-radius: 5px; cursor:pointer;vertical-align: middle;
margin-left: 10px;margin-right: 20px;margin-bottom:10px;margin-top:10px;}
.chakan_delar{display:none;width: 600px;height: 700px; background: #fff; position: fixed;top: 50%;margin-top: -352px;
 left: 50%; margin-left: -340px; z-index: 1;border-top: 4px solid #eeeeee;overflow-y: hidden;overflow-x: hidden;}
</style>
</head>

<body>
<div class="wrap">
    <!--首页右边-->
    <div class="wrap_r fl">
        <div class="actin">
        	<!--头部-->
        	<div class="actin_top">
            	<h2>圈子管理</h2>
                <p>首页/圈子管理</p>
            </div>
            <!--列表-->
            <div class="actin_list">
            	<p class="actin_list1">所有圈子</p>
                <div class="actin_list2">
                	<a class="actin_list2_1" href="javascript:;" onclick="reload()">刷新</a>
                    <a class="actin_list2_2 xin_quanzi" href="javascript:;">新增</a>
                    <input class="actin_list2_3" type="text" placeholder="请输入关键字" />
                    <button class="actin_list2_4">查询</button>
                </div>
                <!--table内容-->
                <div class="actin_tabdiv">
                <table cellpadding="0" cellspacing="0" class="actin_table">
                	<!--头部-->
                	<thead class="actin_tableh">
                        <tr>
                          <th style="width:10%">圈子名称</th>
                          <th style="width:10%">创建人</th>
                          <th style="width:30%">圈子签名</th>
                          <th style="width:10%">圈子人数</th>
                          <th style="width:10%">圈子帖子数</th>
                          <th style="width:10%">创建时间</th>
                          <th style="width:20%">操作</th>
                        </tr>
                      </thead>
                      <!--中间内容-->
                      <tbody class="actin_tableb clearfix">
                       	 
                      </tbody>
                      <tbody id="loading"></tbody>
                </table>
                <div class="yhe_morew"><button class="yhe_more" style="display:none">查看更多</button></div>
                </div>
            </div>
        </div> 
    </div>
</div>
<!--点击"新建"——>'新建圈子'弹层-->
<div class="zhezhao"></div>
<div class="xin_quan">
	<div class="actinttop clearfix"><span class="fl" id="add_title">新建圈子</span><img class="fr" src="${ctx}/admin/images/close.png" /></div>
    <span style="display: none" id="circle_id"></span>
    <form class="xin_quanf">
    	<!--圈子封面图片-->
    	<div class="xin_quanf1 clearfix">
        	<label class="fl">封面</label>
            <span><img id="xin_quanf1_img" /><input id="xin_quanf1_ipt" name="circle_pic" type="file" /></span>
        </div>
        <!--圈子背景图片-->
    	<div class="xin_quanf1 clearfix">
        	<label class="fl">背景</label>
            <span><img id="xin_back_img" /><input id="xin_quanf1_back" name="circle_back_img" type="file" /></span>
        </div>
        <!--圈子名称-->
        <div class="xin_quanf2 clearfix">
        	<label>圈子名称</label>
            <input type="text" id="circle_title" placeholder="请输入圈子名称" />
        </div>
        <!--圈子签名-->
        <div class="xin_quanf3 clearfix">
        	<label>圈子签名</label>
            <textarea id="circle_sign"></textarea>
        </div>
        <div class="actintf_caozuo">
        	<input class="actintf_caozuo1 xinquan_que" type="button" value="确定" />
            <input class="actintf_caozuo2 xinquan_qu"type="button" value="取消" />
        </div>
    </form>
</div>
<!--查看详情-->
<div class="circle_detiel">
	<div class="actinttop clearfix"><span class="fl">圈子详情</span><img class="fr" src="${ctx}/admin/images/close.png" /></div>
	<div class="circle_detiel1"><span>封面</span><img id="circle_pic_detail" src="" onerror="javascript:this.src='${ctx}/admin/images/111.png'"/></div>
	<div class="circle_detiel2"><span>圈子名称：</span><b id="circle_name"></b></div>
	<div class="circle_detiel3"><span>圈子详情：</span><b id="circle_content"></b></div>
</div>
<!--发布帖子-->
<!--点击"新建"——>'新建帖子'弹层-->
<div class="zhezhao"></div>
<div class="xin_tie">
	<div class="actinttop clearfix"><span class="fl">新建帖子</span><img class="fr" src="${ ctx}/admin/images/close.png" /></div>
    <span style="display: none" id="add_article_circle_id"></span>
    <form class="xin_tief">
        <!--帖子标题-->
        <div class="xin_tief2 clearfix">
        	<label>标题</label>
            <input class="nnee_tiet" type="text" placeholder="请输入标题" />
            <p class="nnee_tiet_yan"></p>
        </div>
        <!--帖子正文-->
        <div>
        	<div class="xin_tief2 clearfix">
        	<label>正文</label>
            <textarea class="nnee_tiec" style="width:290px; padding:10px;" rows="11" placeholder="请输入正文"></textarea>
             <p class="nnee_tiec_yan"></p>
        </div>
        </div>
        
        <!--内容-->
        <div class="xin_tief3 clearfix">
        	<label class="fl">内容</label>
            <div class="xin_tief3div fl">
            	<span class="xin_tief3span" style="border:none;">添加图文<img class="z_fuadd" src="${ ctx}/admin/images/icon-add.png" /></span>
                <div class="z_fu"></div>
            </div>
        </div>
        <!--内容分类-->
        <div class="xin_tief2 clearfix">
        	<label class="fl">分类</label>
        	<ul class="nnee_tiese fl clearfix nnee_neirong">
        		<li class="fl">动物保护<input type="checkbox" value="动物保护" /></li>
        		<li class="fl">环境保护<input type="checkbox" value="环境保护" /></li>
        		<li class="fl">健康互助<input type="checkbox" value="健康互助" /></li>
        		<li class="fl">传统文化<input type="checkbox" value="传统文化" /></li>
        		<li class="fl">留守儿童<input type="checkbox" value="留守儿童" /></li>
        	</ul>
        </div>
        <!--帖子标题-->
        <div class="xin_tief2 clearfix">
        	<label class="fl">标签</label>
            <ul class="nnee_tiese fl clearfix nnee_biaoqian">
        		<li class="fl">#动物保护#<input type="checkbox" value="#动物保护#" /></li>
        		<li class="fl">#环境保护#<input type="checkbox" value="#环境保护#" /></li>
        		<li class="fl">#健康互助#<input type="checkbox" value="#健康互助#" /></li>
        		<li class="fl">#传统文化#<input type="checkbox" value="#传统文化#" /></li>
        		<li class="fl">#留守儿童#<input type="checkbox" value="#留守儿童#" /></li>
        	</ul>
        </div>
        <div class="actintf_caozuo">
        	<input class="actintf_caozuo1 xintiez_que" type="button" value="确定" />
            <input class="actintf_caozuo0 dian_yutie" type="button" value="预览" />
            <input class="actintf_caozuo2 xintiez_qu"type="button" value="取消" />
        </div>
    </form>
</div>
<!--预览效果-->
<div class="td_warp yulan_tie">
<div class="actinttop clearfix"><span class="fl fanhuibian"><— 返回编辑页</span></div>
<!--上方帖子内容-->
<div class="td_top">
	<h1 class="td_top1">标题</h1>
    <div class="td_top2 clearfix">
    	<div class="td_top2z fl clearfix">
        	<img class="td_top2zimg1 fl" src="${ctx}/admin/images/img1.png" />
            <img class="td_top2zimg2 fl" src="${ctx}/admin/images/touxiang.png" />
            <span class="fl">天空蓝色的</span>
        </div>
        <div class="td_top2y fr">
        	<span class="td_top2ys1"><img src="${ctx}/admin/images/ic_look.png" />0</span>
            <span class="td_top2ys2"><img src="${ctx}/admin/images/ic_say.png" />0</span>
        </div>
    </div>
    <ul class="td_top3 clearfix">
    </ul>
    <div class="td_zhengwen">正文</div>
    <div class="td_top4"> 	
    </div>
</div>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/adminv1.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script type="text/javascript">	
	var begin = 0;
	var key_word = "";
	var has_more_data = false;
	var dataHtml="";
	
	function reload() {
		begin = 0;
		key_word = $(".actin_list2_3").val();
		has_more_data = false;
		dataHtml="";
		$(".actin_tableb").empty();
		loadData(begin,key_word, loadDataCallback);
	}
	
	$(function () {
		loadData(begin,key_word, loadDataCallback);
	});
	
	function loadDataCallback(json) {
		var datas = json.circleList;
		if (datas != null && datas != undefined) {
			var dlen = datas.length;
			begin = begin+dlen;
			for (var i = 0; i < dlen; ++i) {
				dataHtml += "<tr>";
				dataHtml += "<td name='circle_id' style='display:none'>"
						+ datas[i].circle_id + "</td>";
				dataHtml += "<td name='circle_pic_detail' style='display:none'>"
					+ datas[i].circle_pic + "</td>";
				dataHtml += "<td name='circle_back_img_detail' style='display:none'>"
					+ datas[i].circle_back_img + "</td>";
				dataHtml += "<td name='circle_name'>"+datas[i].circle_name+"</td>";
				if(datas[i].create_user)
					dataHtml += "<td>"+datas[i].create_user+"</td>";
				else
					dataHtml += "<td>无</td>";
				var content=datas[i].circle_sign;
				content=content.replace(/\r\n/g,"</br>");
				content=content.replace(/\n/g,"</br>");
				content=content.replace(/\r/g,"</br>");
				dataHtml += "<td name='content'>" + content + "</td>";
				dataHtml += "<td>" + datas[i].circle_user_count + "人</td>";
				dataHtml += "<td>" + datas[i].circle_articles + "个</td>";
				dataHtml += "<td>" + localFormat(datas[i].circle_created_time) + "</td>";
				var control="<td style='text-align:center'><button class='btn_disables' onclick='circle_detiel(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>查看详情</button>";
				control+="<button class='btn_disables' onclick='cicle_tiezi(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>发布帖子</button>";
				control+="<button class='btn_disables' onclick='edit(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>修改</button>";
				if (datas[i].status != -1) {
					dataHtml += control+"<button id='btn_disables' class='btn_disables' href='javascript:void(0)' name='禁用' onclick='disables(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;'>禁用</button> " +
                            "</td>";
                } else {
                	dataHtml += control+"<button class='btn_disables' href='javascript:void(0)' name='已禁用' onclick='disables(this);' style='display: inline-block;height: 26px;line-height: 26px;padding: 0 8px;border: 1px solid #e8e8e8;background-color: #cd0a0a;color: #ffffff;'>已禁用</button> " +
                            "</td>";
                }
				dataHtml += "</tr>";
			}
			$(".actin_tableb").append(dataHtml);
		}
		hasMoreData = json.has_more_data;
		if (hasMoreData) {
            $(".yhe_more").css('display', 'block');
        } else {
            $(".yhe_more").css('display', 'none');
        }
	}
	
	function loadData(begin,key_word, callback) {
        var requestParam = {
                "type": "admin_get_circles",
                'data': '{'
                + '"begin":"' + begin + '",'+ '"key_word":"' + key_word
                + '"}'
            };
		$("#loading").html("<center><td colspan='5' style='text-align:center'>加载数据中，请稍后......</td></center>");
		$(".yhe_more").css('display', 'none');
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
			},
			error : function() {
				$("#loading").empty();
			}
		});
	}
	
	function disables(e) {
	    var name = $(e).attr("name");
	    var circle_id = $(e).parent().parent().find("[name=circle_id]").text();
	    var requestParams = {
   			"type": "edit_circle",
              'data': '{'
              + '"circle_id":"'+circle_id+'",'
              + '"circle_name":"",'
              + '"circle_sign":"",'
              + '"status":1'
              + '}'
	    };
	    $.ajax({
	          url: "${ctx}/adminapi",
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

	/*预览"圈子封面"图片*/
	$("#xin_quanf1_ipt").change(function() {
		var $file = $(this);
		var fileObj = $file[0];
		var windowURL = window.URL || window.webkitURL;
		var dataURL;
		var $img = $("#xin_quanf1_img");
		if(fileObj && fileObj.files && fileObj.files[0]){
			dataURL = windowURL.createObjectURL(fileObj.files[0]);
			$img.attr('src',dataURL);
		}else{
			$file.select(); 
			dataURL = document.selection.createRange().text;
			var imgObj = document.getElementById("xin_quanf1_img");
			imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
			imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;	
			}
		});
	$("#xin_quanf1_back").change(function() {
		var $file = $(this);
		var fileObj = $file[0];
		var windowURL = window.URL || window.webkitURL;
		var dataURL;
		var $img = $("#xin_back_img");
		if(fileObj && fileObj.files && fileObj.files[0]){
			dataURL = windowURL.createObjectURL(fileObj.files[0]);
			$img.attr('src',dataURL);
		}else{
			$file.select(); 
			dataURL = document.selection.createRange().text;
			var imgObj = document.getElementById("xin_back_img");
			imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
			imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;	
			}
		});
	/*********************************************************/
	/*点击"新建"按钮出现弹出框'新建圈子'*/
	$('.xin_quanzi').click(function(){
		$('.zhezhao').fadeIn();
		$('.xin_quan').fadeIn();
		
		/*点击"新建"清空表单数据*/
		$('.xin_quanf')[0].reset();
		$('#xin_quanf1_img').prop('src','');
		$('#xin_back_img').prop('src','');
		
		$("#add_title").text("新建圈子");
	})	

	/*点击"取消"关闭弹层-新建圈子*/
	$('.xinquan_qu').click(function(){
		$('.zhezhao').fadeOut();
		$('.xin_quan').fadeOut();
	})
	/*********************************************************/
	/*点击查询*/
    $('.actin_list2_4').click(function () {
        var kw = $(".actin_list2_3").val();
        if (kw == '') {
            alert('请输入查询关键字')
            return;
        }
        begin = 0;
        dataHtml="";
        key_word=kw;
        $(".actin_tableb").empty();
        loadData(begin, key_word, loadDataCallback);
    });

	$(".yhe_more").click(function() {
		$(".yhe_more").css('display', 'none');
		loadData(begin,key_word, loadDataCallback);
	});
	/*修改*/
	function edit(e){
		var circle_id = $(e).parent().parent().find("[name=circle_id]").text();
		$("#circle_id").text(circle_id);
		var circle_pic = $(e).parent().parent().find("[name=circle_pic_detail]").text();
		var content = $(e).parent().parent().find("[name=content]").text();
		var circle_name = $(e).parent().parent().find("[name=circle_name]").text();
		var circle_back_img = $(e).parent().parent().find("[name=circle_back_img_detail]").text();
		$("#circle_pic").attr("src",circle_pic);
		$("#circle_title").text(circle_name);
		$("#circle_content").text(content);
		$('.zhezhao').fadeIn();
		$('.xin_quan').fadeIn();
		$('.xin_quanf')[0].reset();
		if(circle_pic && circle_pic!='undefined')
			$('#xin_quanf1_img').prop('src',circle_pic);
		else
			$('#xin_quanf1_img').prop('src',"");
		if(circle_back_img && circle_back_img!='undefined')
			$('#xin_back_img').prop('src',circle_back_img);
		else
			$('#xin_back_img').prop('src',"");
		$("#circle_title").val(circle_name);
		$("#circle_sign").val(content);
			
		$("#add_title").text("修改圈子");
	}
	
    $(".xinquan_que").click(function () {
    	var circle_id=$("#circle_id").text();
        var title = $("#circle_title").val();
        var content = $("#circle_sign").val();
        var circleImg = $("#xin_quanf1_ipt").val();
        var circle_back_img = $("#xin_quanf1_back").val();
    	if($("#add_title").text()=='修改圈子'){
		        if (!title) {
		            alert("圈子名不能为空!");
		            return;
		        }
		        if (!content) {
		            alert("圈子签名不能为空！");
		            return;
		        }
		        if (circleImg) {
		            var suffix = /.+\.(.+)/gi.exec($("#xin_quanf1_ipt").val());
		            if (suffix == null) {
		                alert("上传的图片类型非法！");
		                return;
		            }else {
		                suffix = suffix[1];
		                if (suffix != "jpg" && suffix != "png" && suffix != "gif" && suffix != "jpeg" && suffix != "bmp") {
		                    alert("上传的图片类型非法！");
		                    return;
		                }
		            }
		        }
		        if (circle_back_img) {
		            var suffix = /.+\.(.+)/gi.exec($("#xin_quanf1_back").val());
		            if (suffix == null) {
		                alert("上传的图片类型非法！");
		                return;
		            }
		            else {
		                suffix = suffix[1];
		                if (suffix != "jpg" && suffix != "png" && suffix != "gif" && suffix != "jpeg" && suffix != "bmp") {
		                    alert("上传的图片类型非法！");
		                    return;
		                }
		            }
		        }
		        $(".xin_quanf").ajaxSubmit({
		            url: "${ctx}/adminapi",
		            type: "post",
		            data: {
		                "type": "edit_circle",
		                'data': '{'
		                + '"circle_id":"' + circle_id + '",'
		                + '"circle_name":"' + title + '",'
		                + '"circle_sign":"' + content + '"'
		                + '}'
		            },
		            dataType: "json",
		            success: function (data) {
		                if (data != null && data.status == "ok") {
		                    alert("修改成功！");
		                    reload();
		                } else if (data.status == "parameter_error") {
		                    alert("添加失败，参数错误!");
		                }else if (data.status == "password_error") {
		                    alert("添加失败，密码错误!");
		                } else {
		                    alert("服务器内部错误，添加失败");
		                }
		            },
		            error: function () {
		                alert("服务器内部错误，添加失败");
		            }
		        });
	        }else if($("#add_title").text()=='新建圈子'){
	        if (!title) {
	            alert("圈子名不能为空!");
	            return;
	        }
	        if (!content) {
	            alert("圈子签名不能为空！");
	            return;
	        }
	        if (!circleImg) {
	            bannerImg = null;
	            alert("圈子图片不能为空！");
	            return;
	        }else {
	            var suffix = /.+\.(.+)/gi.exec($("#xin_quanf1_ipt").val());
	            if (suffix == null) {
	                alert("上传的图片类型非法！");
	                return;
	            }else {
	                suffix = suffix[1];
	                if (suffix != "jpg" && suffix != "png" && suffix != "gif" && suffix != "jpeg" && suffix != "bmp") {
	                    alert("上传的图片类型非法！");
	                    return;
	                }
	            }
	        }
	        if (!circle_back_img) {
	        	circle_back_img = null;
	            alert("背景图片不能为空！");
	            return;
	        }else {
	            var suffix = /.+\.(.+)/gi.exec($("#xin_quanf1_back").val());
	            if (suffix == null) {
	                alert("上传的图片类型非法！");
	                return;
	            }
	            else {
	                suffix = suffix[1];
	                if (suffix != "jpg" && suffix != "png" && suffix != "gif" && suffix != "jpeg" && suffix != "bmp") {
	                    alert("上传的图片类型非法！");
	                    return;
	                }
	            }
	        }
	        $(".xin_quanf").ajaxSubmit({
	            url: "${ctx}/adminapi",
	            type: "post",
	            data: {
	                "type": "admin_add_circle",
	                'data': '{'
	                + '"circle_name":"' + title + '",'
	                + '"circle_sign":"' + content + '"'
	                + '}'
	            },
	            dataType: "json",
	            success: function (data) {
	                if (data != null && data.status == "ok") {
	                    alert("添加成功！");
	                    reload();
	                } else if (data.status == "parameter_error") {
	                    alert("添加失败，参数错误!");
	                }else if (data.status == "password_error") {
	                    alert("添加失败，密码错误!");
	                } else {
	                    alert("服务器内部错误，添加失败");
	                }
	            },
	            error: function () {
	                alert("服务器内部错误，添加失败");
	            }
	        });
         }
        $('.zhezhao').fadeOut();
		$('.xin_quan').fadeOut();
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
	
	/*"查看详情"弹窗*/
	 function circle_detiel(e){
		 var circle_pic = $(e).parent().parent().find("[name=circle_pic_detail]").text();
		 var content = $(e).parent().parent().find("[name=content]").text();
		 var circle_name = $(e).parent().parent().find("[name=circle_name]").text();
		 $("#circle_pic_detail").attr("src",circle_pic);
		 $("#circle_name").text(circle_name);
		 $("#circle_content").text(content);
		 $('.circle_detiel').show();
		 $('.zhezhao').show();
	}
	 /*"发布帖子"弹窗*/
	/*添加子图片*/
	var num=0;
	$('.xin_tief3span').click(function(){
		if(num==8){
			alert('最多添加8张');
			return false;
		}
		num++;
		var for1 = 'xin_tiefzi_img'+num,
			for2 = 'xin_tiefzi_ipt'+num,
			for3 = 'xin_tiefzi_text'
		/*创建新的子内容*/
		var html = '<div class="xin_tiefzi">'+
			'<input id="'+for1+'" type="file" name="'+for1+'"/>'+
			'<img id="'+for1+'" />'+
			'<textarea class="z_futextsuo" rows="6" placeholder="请描述图片" id="'+for2+'" type="file"></textarea></div>'		
		$('.z_fu').append(html);
			
		/*预览*/
		var htmlz = '<div class="yu_tuwen">'+
		'<img src="" width="100%"  />'+
		'<p></div>'
		$('.td_top4').append(htmlz);
			
		
	})
	$('.xin_tiefzi input').live('change',function() {
		/*判断是否重复*/
		var that = $(this)
		var val1 = that.val();
		var count = 0;
		var z_fuimg_id=that.attr('id');
		$('.xin_tiefzi input').each(function(){
			if($(this).val().length != 0){
				var val2 =$(this).val()
				if( val2 == val1){
					count++;
				}
			}
		})
		if(count>1){
			alert("该图片已经选择过了！")
			var id  = document.getElementById(that.attr('id'))		
			var ie_id=document.getElementById(that.parent().find('img').attr('id'));
			ie_id.style.filter='';			
			that.parent().find('img').attr('src','')
			/*清空file值*/
			function clearFileInput(file){
				var form=document.createElement('form');
				document.body.appendChild(form);
				var pos = file.nextSibling;
				form.appendChild(file);
				form.reset();
				pos.parentNode.insertBefore(file, pos);
				document.body.removeChild(form);
			}
			clearFileInput(id)	
		}else{
			var z_fuimg0val=that.val();
			var $file = that;
			var fileObj = $file[0];
			var windowURL = window.URL || window.webkitURL;
			var dataURL;
			var $zhun_photo0 = that.parent().find('img');
	
			if(fileObj && fileObj.files && fileObj.files[0]){
				dataURL = windowURL.createObjectURL(fileObj.files[0]);
				$zhun_photo0.attr('src',dataURL);
			}else{
	
				that.select(); 
				dataURL = document.selection.createRange().text;
				var imgObj = document.getElementById($zhun_photo0.attr('id'));			
				imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
				imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;
			}
		}		
	});
	/*点击"发布帖子"按钮出现弹出框'发布帖子'*/
	 function cicle_tiezi(e){
		 var circle_id = $(e).parent().parent().find("[name=circle_id]").text();
		 $("#add_article_circle_id").text(circle_id);
		 $('.zhezhao').fadeIn();
		 $('.xin_tie').fadeIn();
	}
		
	/*高亮效果*/
	$('.nnee_tiet').focus(function(){
		$('.nnee_tiet_yan').html('')
	})
	$('.nnee_tiec').focus(function(){
		$('.nnee_tiec_yan').html('')
	})
	/*点击"确定"关闭弹层-新建贴子*/
	$('.xintiez_que').click(function(){
		/*校验*/
		 var nnee_tiet=$('.nnee_tiet').val();
		 var nnee_tiec=$('.nnee_tiec').val();
		 var z_fuinput_len=$(".z_fu input").length;
		 var nnee_neironginp_len=$(".nnee_neirong input").length;
		 var nnee_biaoqianinp_len=$(".nnee_biaoqian input").length;
		 
		 if(nnee_tiet==''){
			$('.nnee_tiet_yan').html('请输入帖子标题')
			return;
		 }else{
			 $('.nnee_tiet_yan').html('')
		 }
		 if(nnee_tiec==''){
			$('.nnee_tiec_yan').html('请输入帖子正文')
			return;
		 }else{
			$('.nnee_tiec_yan').html('')
		 }
		 for(var i=0;i<z_fuinput_len;i++){
	    	if($(".z_fu input[type='file']").eq(i).val()==''||$(".z_fu input[type='file']").eq(i).val()==null||$(".z_fu input[type='file']").eq(i).val()==undefined){
	    		alert("请选择图片！");
	            return;
	    	}
	     } 
		 if($('.z_futextsuo').val()=='请描述此图片'){
	            $('.z_futextsuo').val('')
	     }
		 
		 var d_width=$(window).width()+'px';
         var d_height=$(document).height()+'px';
         $('.zhezhao').css('minHeight',d_height);
         $('.zhezhao').css('height',$('.actin_list').height())

         /*加载数据中*/
         $('.zhezhao').fadeIn();
         $('.loading').fadeIn();

         var z_futext_le=$('.xin_tiefzi').length;
         var picArrary = new Array();
         for(var i=0;i<z_futext_le;i++){
             var article_pic_partname = $(".xin_tiefzi input:file").attr("name");
             var article_pic_des = $(".z_fu textarea").eq(i).val();
             var params = {
               "article_pic_partname" : article_pic_partname,
               "article_pic_des" : article_pic_des
             };
             picArrary.push(params);
         }
         
		for(var i=0;i<nnee_neironginp_len;i++){
			if($(".nnee_neirong input[type='checkbox']:checked").length <1){
	    		alert("请选择分类");
	            return;
	    	}
			if($(".nnee_neirong input[type='checkbox']:checked").length >3){
	    		alert("分类最多选择三个");
	            return;
	    	}
		}
		
		var typeArray = new Array();
        $(".nnee_neirong input[type='checkbox']:checked").each(function () {
            if ($(this).attr("checked")){
                var type = $(this).val();
                typeArray.push(type);
            }
        });
        
		for(var i=0;i<nnee_biaoqianinp_len;i++){
			if($(".nnee_biaoqian input[type='checkbox']:checked").length <1){
	    		alert("请选择标签");
	            return;
	    	}
			if($(".nnee_biaoqian input[type='checkbox']:checked").length >3){
	    		alert("标签最多选择三个");
	            return;
	    	}
		}
		
		var labelArray = new Array();
        $(".nnee_biaoqian input[type='checkbox']:checked").each(function () {
            if ($(this).attr("checked")){
                var label = $(this).val();
                labelArray.push(label);
            }
        });
	
		$('.zhezhao').fadeOut();
		$('.xin_tie').fadeOut();
		$('.nnee_tiet').val('');
		$('.nnee_tiec').val('');
		$(".nnee_neirong input[type='checkbox']").attr("checked",false)
		$(".nnee_biaoqian input[type='checkbox']").attr("checked",false)
		
	    var circle_id = $("#add_article_circle_id").text();
        var article_type = JSON.stringify(typeArray);
        var article_label = JSON.stringify(labelArray);
        var article_img_des = JSON.stringify(picArrary);
        var requestParams = {
              "type": "admin_add_article",
              'data': '{'
              +'"circle_id":"'+circle_id+'",'
              + '"title":"' + nnee_tiet + '",'
              + '"article_content":"' + nnee_tiec + '",'
              +'"article_type":'+article_type+','
              +'"article_label":'+article_label+','
              +'"article_pic_desc":'+article_img_des
              + '}'};
          $(".xin_tief").ajaxSubmit({
              url: "${ctx}/adminapi",
              type: "post",
              data: requestParams,
              dataType: "json",
              success: function (data) {
                  if (data != null && data.status == "ok") {
                      alert("添加成功！");
                      location.reload();
                  } else if (data.status == "parameter_error") {
                      alert("添加失败，参数错误!");
                  }else if (data.status == "password_error") {
                      alert("添加失败，密码错误!");
                  } else {
                      alert("服务器内部错误，添加失败");
                  }
              },
              error: function () {
                  alert("服务器内部错误，添加失败");
            }
     	});
		$('.z_fu').html('');
	})
	/*点击"取消"关闭弹层-新建贴子*/
	$('.xintiez_qu').click(function(){
		$('.nnee_tiet').val('');
		$('.nnee_tiec').val('');
		$('.z_fu').html('');
		$(".nnee_neirong input[type='checkbox']").attr("checked",false)
		$(".nnee_biaoqian input[type='checkbox']").attr("checked",false)
		$('.zhezhao').fadeOut();
		$('.xin_tie').fadeOut();
	})
	/*点击X*/
	$('.actinttop img').click(function(){
		$('.nnee_tiet').val('');
		$('.nnee_tiec').val('');
		$('.z_fu').html('');
		$(".nnee_neirong input[type='checkbox']").attr("checked",false)
		$(".nnee_biaoqian input[type='checkbox']").attr("checked",false)
		$('.zhezhao').fadeOut();
		$('.xin_tie').fadeOut();
	})
	
	/*********************************************************************************************/
	/*点击"预览"——>预览所有内容*/
	 $('.dian_yutie').click(function(){
		 $('.xin_tie').hide();
		 $('.yulan_tie').show();
		 var title=$('.nnee_tiet').val();
		 var content=$('.nnee_tiec').val();
		 var nnee_biaoqianle=$('.nnee_biaoqian').length;
		 $('.td_top1').html(title)
		 $('.td_zhengwen').html(content)
		 
		 /*标签*/
		 var veidoo=[]; 
		 var id=$('.nnee_biaoqian');
		 var cs=$("input[type='checkbox']):checked",id);
		 cs.each(function () {  
	         veidoo.push(this.value);  
	      });  
		 var leeth=veidoo.length;
		 for(var i=0;i<leeth;i++){
			 var jhtml='<li class="fl"><img src="${ctx}/admin/images/ic_label.png" />'+
			 '<span>'+veidoo[i]+'</span>'+
			 '</li>'
			 $('.td_top3').append(jhtml)
		 }
		 
		 /*图文*/
		var xin_tiefzilen=$('.xin_tiefzi').length;
		for(var i=0;i<xin_tiefzilen;i++){
			$('.td_top4').find('div.yu_tuwen').eq(i).find('img').attr('src',$('.z_fu').find('div.xin_tiefzi').eq(i).find('img').attr('src'))  
			$('.td_top4').find('div.yu_tuwen').eq(i).find('p').html($('.z_fu').find('div.xin_tiefzi').eq(i).find('textarea').val().replace(/\n/g,"</br>"))
		} 
	 })
	 
	/*点击“返回编辑”页面*/
	 $('.fanhuibian').click(function(){
		 $('.td_top3').html('')
		 $('.xin_tie').show();
		 $('.yulan_tie').hide(); 
	 })

</script>
</body>
</html>
