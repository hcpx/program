<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../../${ctx}/admin/images/logo.ico" rel="shortcut icon" />
<title>添加专题</title>
<style>
*{ margin: 0; padding: 0;}
body{ width:100%; height:100%; font-weight:100; font-family: "冬青黑体","微软雅黑","华文细黑","宋体";font-size:12px;}
input,button{outline: none; background:none; border:none;}
textarea{outline: none; background:none; }
img{border: none;}
ul,li{list-style: none;}
a,a:active,a:link,a:visited,a:focus,a:hover{text-decoration: none;}
.clearfix:after{content: "";width: 0;height: 0;clear: both;display: block;visibility: hidden;}
.clearfix{zoom: 1;}
.CL{clear:both; height:0px; overflow:hidden;}
.fl{float: left;}
.fr{float: right;}
button{ padding:0; margin:0;}
b{ font-weight:100;}

/*添加专题页面*/
.zhuanti_wrap{width: 600px; height: 700px; background: #fff; position: fixed;top: 50%; margin-top: -352px;left: 50%; margin-left: -340px; z-index: 1;border-top: 4px solid #eeeeee;overflow-y: auto;
    overflow-x: hidden;}
.head{ width:96%; height:40px; line-height:40px; background:#4b89de; border-top-left-radius:5px; border-top-right-radius:5px; margin-top:30px; padding:0 2%;font-size:16px;color:#fff; letter-spacing:2px;}
.zhuanti_body{ width:96%; height:auto; padding:10px 2%; background:#fff;}
.z_zhu{ width:98%; padding:20px 1%; background:#fff; margin-bottom:20px;}
.z_zhu label{ font-size:14px; height:30px; line-height:30px; margin-right:20px; width:100px; display:inline-block;}
.z_zhu input{ width:180px; height:30px; line-height:30px; border:1px solid #ccc; padding:0 10px; border-radius:3px; background:#eeeeee;}
.z_zhu3{ width:100%; height:auto; margin-bottom:20px; text-align:center;}
.z_zhu3 input{ border:none;}
.title_error{ width:100%; height:auto; margin-bottom:20px; text-align:center;}
.title_error input{ border:none;}
.z_zhu4{ width:540px; height:auto; margin:0 auto;}
.z_zhu4 label{ vertical-align:top;}
.z_zhu4 textarea{ padding:10px 0; width:250px; text-indent:2em;}
.z_fu{ width:98%; padding:0px 1%; background:#fff; padding-bottom:10px;}
.z_tianjia{ text-align:center; height:30px; line-height:30px; text-align:center; width:100px; border-radius:5px; font-size:14px;}
.z_fuadd{ width:24px; height:auto; vertical-align:middle; margin-top:-3px; cursor:pointer;}
.z_fucon{ margin:10px 0; padding:10px 0; border-bottom:1px solid #000;}
.z_fucon label{ font-size:14px; height:30px; line-height:30px; margin-right:20px; width:100px; display:inline-block;}
.z_futext input{width:200px; height:30px; line-height:30px; border:1px solid #ccc; padding:0 10px; border-radius:3px;}
.z_fuimg{ width:100%; text-align:left;}
.z_futext{ width:540px; margin:0 auto;}
.z_futext label{ vertical-align:top;}
.z_futext textarea{ padding:10px 0; width:540px; text-indent:2em;}
.zhuanti_tijiao{ width:100%; height:40px; padding:10px 0; text-align:center;padding-bottom:35px;}
.zhuanti_tijiao input{ padding:0 20px; margin:0 auto; height:30px; line-height:30px; background:#1ab394; border-radius:5px; font-size:15px; letter-spacing:1px; color:#fff; cursor:pointer; margin-top:10px;}
.error_z{height:30px; line-height:30px; font-size:14px; color:#f00; width:540px; text-align:left; margin:0 auto;}
.errorz_zhu1,.errorz_zhu2,.errorz_zhu3,.errorz_zhu4{}
.tianjia_fixed{ width:100%; height:50px; background:#fff; text-indent:2%;}
.actinttop { width: 100%; height: 60px;line-height: 60px;border-bottom: 1px solid #eeeeee;}
.actinttop span {font-size: 20px;margin-left: 18px;}
.actinttop img {width: 28px;height: 28px;margin-right: 18px;margin-top: 14px;cursor: pointer;}
.gu01{display: inline-block;width: 140px; height: 140px;margin: 0 auto;background: url(../images/07.png) no-repeat 0 0;background-size: 100% 100%;
    position: relative;cursor: pointer;margin-top: 10px; margin-left:10px;}
.gu01 img{position: absolute; top: 0; width: 140px; height: 140px;}   
.gu01 input{position: absolute;top: 0; right: 0; margin: 0; border: 1px solid transparent;opacity: 0;filter: alpha(opacity=0); width: 140px;height: 140px;
cursor: pointer;}   

/*loding动画*/
.zhezhao{ width:100%; height:100%; background-color:rgba(0,0,0,0.2); position:absolute; top:0; left:0;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#19000000,endColorstr=#19000000); display:none;}
.loading{ width:200px; height:150px; background:#fff; position:fixed; top:50%; margin-top:-75px; left:50%; margin-left:-100px; text-align:center; border-radius:4px; z-index:999;display:none;}
.loading img{ width:100px; margin:0 auto;}
.loading p{ font-size:13px; }
</style>
</head>

<body>
<div class="zhezhao"></div>
<div class="loading">
	<img src="${ctx}/admin/images/loading.jpg" />
    <p>数据正在加载中，请稍后...</p>
</div>
<div class="zhuanti_wrap">
	<div class="actinttop clearfix"><span class="fl">新建专题</span><img class="fr" src="${ctx}/admin/images/close.png"></div>
	<form class="zhuanti_body" id="addForm" action="${ctx}/adminapi" enctype="multipart/form-data" method="post">
        <div class="z_zhu clearfix"> 
            <!--添加专题大图片-->
            <div class="z_zhu3 z_fuimg ">
                <label style="position: relative;top: -39px;left: 38px;font-size: 14px;">封面</label>
                <span class="gu01">
                	<div><img id="zhun_photo" src=""></div>
                	<input class="yulan_zhun" type="file" id="subject_icon" name="subject_icon"></input>
                </span>
                <p class="error_z errorz_zhu3"></p>
            </div>
            <div class="z_zhu4 clearfix">
                <label>请输入正文标题</label>
                <input  value="请输入标题内容" id="subject_title" onfocus="javascript:if(this.value=='请输入标题内容')this.value='';"/>
                <p class="error_z title_error"></p>
            </div>
            <!--正文-->
            <div class="z_zhu4 clearfix">
                <label class="fl">请输入正文内容</label>
                <textarea class="fl" rows="11" id="subject_content">请输入正文内容</textarea>
            </div>
            <p class="error_z errorz_zhu4"></p>           
        </div>
        <!--添加子内容-->
        <div class="z_fu">              
        </div>
        <div class="tianjia_fixed"><span class="z_tianjia">添加图片</span>
            <img class="z_fuadd" src="${ctx}/admin/images/icon-add.png" />
       	</div>
        <!--提交信息-->
        <div class="zhuanti_tijiao">
			<input class="save" type="button" value="确定" />
			<input class="yulan" type="button" value="预览" style="background:#fff;color:#000; border:1px solid #e8e8e8;" />
			<input class="cancel" type="button" value="取消" style="background:#fff;color:#000; border:1px solid #e8e8e8;" />
		</div>
    </form>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script src="http://libs.useso.com/js/jquery.form/3.50/jquery.form.min.js"></script>
<script type="text/javascript">
$(function(){
	/*添加专题页面-创建新的子内容*/
	var num=0;
	$('.z_fuadd').click(function(){
		var for1 = 'subject_img'+num;
		var for2 = 'subject_img_des'+num;
		var for3 ='dd_ing'+num;
		num++;
		if(num>8){
			alert('最多只能添加8张图！');
			return;
		}
		/*创建新的子内容*/
		var html = '<div class="z_fucon clearfix"><div class="z_fuimg fl">'+
			'<label for="'+for1+'">请上传图片</label>'+
			'<input type="file" id="'+for1+'" name="'+for1+'"/>'+
			'<div style="width:540px;margin:10px auto;"><img id="'+for3+'" style="width:100%;height:auto; " "/></div>'+       
			'</div><div class="z_futext">'+
			'<label for="'+for2+'">图片信息</label>'+
			'<textarea class="z_futextsuo" rows="6" id="'+for2+'" style="padding: 10px 0;width: 540px;text-indent: 2em;"></textarea></div></div>'
		$('.z_fu').append(html);
		
		$('.z_futextsuo').focus(function(){
			$(this).css('border','1px solid #4b89de')
		})
		$('.z_futextsuo').blur(function(){
			$(this).css('border','1px solid #ccc')
		})	

	})
	
	$('.z_fuimg input').live('change',function() {
		if(subject_icon==''){
			$('.errorz_zhu3').html('该处不能为空，请选择图片');
			return;
		}else{
			$('.errorz_zhu3').html('');
		}
		/*判断是否重复*/
		var that = $(this)
		var val1 = that.val();
		var count = 0;
		var z_fuimg_id=that.attr('id');
		$('.z_fuimg input').each(function(){
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
			var ie_id=document.getElementById(that.siblings('div').find('img').attr('id'));
			ie_id.style.filter='';			
			that.siblings('div').find('img').attr('src','')
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
			var $zhun_photo0 = that.siblings('div').find('img');

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

	/*高亮效果*/
	$('.z_zhu4 textarea').focus(function(){
		$(this).css('border','1px solid #4b89de')
		$('.errorz_zhu4').html('');
	})
	$('.z_zhu4 textarea').blur(function(){
		$(this).css('border','1px solid #ccc')
	})
	$('.z_futextsuo').focus(function(){
		$(this).css('border','1px solid #4b89de')
	})
	$('.z_futextsuo').blur(function(){
		$(this).css('border','1px solid #ccc')
	})

	/*效验*/
	$('.save').click(function(){
		var subject_icon =$('.z_zhu3 input').val();
		var subject_content=$('.z_zhu4 textarea').val();
		var subject_title=$('#subject_title').val();
		
		if(subject_icon==''){
			$('.errorz_zhu3').html('该处不能为空，请选择图片');
			return;
		}else{
			$('.errorz_zhu3').html('');
		}
		if(subject_title=='请输入标题内容'||subject_title==''){
			$('.title_error').html('该处不能为空，请填写标题');
			return;
		}else{
			$('.title_error').html('');
		}
		if(subject_content=='请输入正文内容'||subject_content==''){
			$('.errorz_zhu4').html('该处不能为空，请填写正文');
			return;
		}else{
			$('.errorz_zhu4').html('');
		}
		if($('.z_futextsuo').val()=='请描述此图片'){
			$('.z_futextsuo').val('')
		}
	    var d_width=$(window).width()+'px';
		var d_height=$(document).height()+'px';
		$('.zhezhao').css('height',d_height)
		/*加载数据中*/
		$('.zhezhao').fadeIn();
		$('.loading').fadeIn();
		
		var z_futext_le=$('.z_fucon').length;
		var subject_img_des = "";
		if(z_futext_le==0){
			subject_img_des="[]";
		}
		for(var i=0;i<z_futext_le;i++){
			if(i == 0){
				subject_img_des = '[';
			}else{
				subject_img_des += ",";
			}
			subject_img_des += '"'+$('#subject_img_des'+i).val()+'"';
			if(i == (z_futext_le-1)){
				subject_img_des += "]";
			}
		}
		 $("#addForm").ajaxSubmit({
	            url: "${ctx}/adminapi",
	            type: "post",
	            data: {
	                "type": "admin_add_subject",
	                'data': '{'
	                + '"subject_content":"' + subject_content + '",'
	                + '"subject_title":"' + subject_title + '",'
	                + '"subject_img_des":' + subject_img_des + ''
	                + '}'
	            },
	            dataType: "json",
	            success: function (data) {
	            	$('.zhezhao').fadeOut();
	    			$('.loading').fadeOut();
	                if (data != null && data.status == "ok") {
	                    alert("添加成功！");
	                    window.location.href = "${ctx}/admin/admin/showSubjects.jsp";
	                } else {
	                    alert("服务器内部错误，添加失败");
	                }
	            },
	            error: function () {
	            	$('.zhezhao').fadeOut();
	    			$('.loading').fadeOut();
	                alert("请求失败,请检查网络");
	            }
	    });
		 $('.cancel').click(function(){
			 window.location.href = "${ctx}/admin/admin/showSubjects.jsp";
		 });
	})

})
</script>
</body>
</html>