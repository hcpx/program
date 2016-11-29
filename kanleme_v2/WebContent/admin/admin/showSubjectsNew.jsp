<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
	long ctime = System.currentTimeMillis();
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>国立看了么后台管理</title>
<link href="${ctx}/admin/css/adminv1.css" type="text/css" rel="stylesheet" />
    <link href="${ctx}/js/jqueryui/jquery-ui.min.css" type="text/css" rel="stylesheet">
<style>
/*添加专题页面*/
.zhuanti_wrap{width: 600px; height: 570px; background: #fff; position: fixed;top: 50%; margin-top: -319px;left: 50%; margin-left: -340px; z-index: 1;border-top: 4px solid #eeeeee;overflow-y: auto;
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
.z_zhu4 label{ vertical-align:top; text-align: center;}
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

/*预览效果*/
.add_yulan{display:none;width: 600px;height: 700px; background: #fff; position: fixed;top: 50%;margin-top: -352px;
 left: 50%; margin-left: -340px; z-index: 1;border-top: 4px solid #eeeeee;overflow-y: auto;overflow-x: hidden;}
.yulan_banner{width:100%;height:auto; margin-bottom:10px;}
.yulan_title{width:90%;height:auto;margin:0 auto; margin-bottom:20px;font-size:14px;}
.yulan_con{width:90%;height:auto;margin:0 auto; margin-bottom:20px;font-size:14px;}
.zeng img{width:100%;height:auto; margin-bottom:10px;font-size:14px;}
.zeng p{width: 90%; height: auto;margin: 0 auto;margin-bottom: 20px;font-size: 14px;}
.fanhuibian{cursor:pointer;}
.chakan_del{display:none;width: 600px;height: 570px; background: #fff; position: fixed;top: 50%;margin-top: -319px;
 left: 50%; margin-left: -340px; z-index: 1;border-top: 4px solid #eeeeee;overflow-y: auto;overflow-x: hidden;}
.new_son img{width:100px; height:100px; margin:10px auto;margin-left:120px;}
.zhuanti_iframe{overflow-y: auto;height:680px;}
</style>
</head>

<body>
<div class="zhezhao"></div>
<div class="loading">
	<img src="${ctx}/admin/images/loading.jpg" />
    <p>数据正在加载中，请稍后...</p>
</div>
<div class="wrap">
    <!--首页右边-->
    <div class="wrap_r fl">
        <div class="actin">
        	<!--头部-->
        	<div class="actin_top">
            	<h2>专题管理</h2>
                <p>首页/专题管理</p>
            </div>
            <!--列表-->
            <div class="actin_list">
            	<p class="actin_list1">所有专题</p>
                <div class="actin_list2">
                	<a class="actin_list2_1" href="javascript:;" onclick="window.location.reload();">刷新</a>
                    <a class="actin_list2_2 xin_subiject" href="javascript:;">新增</a>
                    <input class="actin_list2_3" type="text" placeholder="请输入关键字" />
                    <button class="actin_list2_4">查询</button>
                </div>
                <!--table内容-->
                <div class="actin_tabdiv">
                <table cellpadding="0" cellspacing="0" class="actin_table">
                	<!--头部-->
                	<thead class="actin_tableh">
                        <tr>
                            <th style="width: 15%">专题ID</th>
                          <th style="width:20%">专题名称</th>
                            <th style="width: 10%">评论总数</th>
                            <th style="width: 10%;">浏览总数</th>
                          <th style="width:20%">创建时间</th>
                          <th style="width:25%">操作</th>
                        </tr>
                      </thead>
                      <!--中间内容-->
                      <tbody class="actin_tableb clearfix"></tbody>
                </table>
                <div class="yhe_morew"><button id="loadMoreBtn" class="yhe_more" style="display: none">查看更多</button></div>
                </div>
            </div>
        </div> 
    </div>
</div>

<!-- 弹出确定框 -->
<div id="dialog-confirm" title="删除">
    <p><span style="float:left; margin:0 7px 20px 0;"></span>这些项目将被永久删除，并且无法恢复。您确定吗？</p>
</div>

<!--点击"新建"——>'新建专题'弹层-->
<div class="zhuanti_wrap" style="display:none;">
	<div class="actinttop clearfix"><span class="fl">新建专题</span><img class="fr" src="${ctx}/admin/images/close.png" /></div>
	<form class="zhuanti_body" id="addForm" action="${ctx}/adminapi" enctype="multipart/form-data" method="post">
        <div class="z_zhu clearfix"> 
            <!--添加专题大图片-->
            <div class="z_zhu3 z_fuimg">
                <label style="position: relative;top: -39px;left: 38px;font-size: 14px;">封面</label>
                <span class="gu01">
                	<div><img id="zhun_photo" src="" /></div>
                	<input class="yulan_zhun" type="file" id="subject_icon" name="subject_icon">
                </span>
                <p class="error_z errorz_zhu3"></p>
            </div>
            <div class="z_zhu4 clearfix">
                <label>标题</label>
                <input value="请输入标题内容" id="subject_title" onfocus="javascript:if(this.value=='请输入标题内容')this.value='';"/>
                <p class="error_z title_error"></p>
            </div>
            <!--正文-->
            <div class="z_zhu4 clearfix">
                <label class="fl">正文</label>
                <textarea class="fl" rows="11" id="subject_content"></textarea>
            </div>
            <p class="error_z errorz_zhu4"></p>           
        </div>
        <!--添加子内容-->
        <div class="z_fu"></div>
        <div class="tianjia_fixed"><span class="z_tianjia">添加图文</span>
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
<!--预览效果-->
 <div class="add_yulan">
 	<div class="actinttop clearfix"><span class="fl fanhuibian"><— 返回编辑页</span></div>
 	<img src='' class="yulan_banner" />
 	<p class='yulan_title'>标题</p>
 	<p class='yulan_con'>正文</p>
 </div> 
 <!--点击"专题详情"-->
<div class="chakan_del">
	<div class="actinttop clearfix"><span class="fl">专题详情</span><img class="fr" src="${ctx}/admin/images/close.png" /></div>
    <iframe id="detail" class="zhuanti_iframe" width="100%" height="100%"  frameborder="0"></iframe>
</div>
<!--点击“删除”弹窗-->
<div class="com_delete">
	<h1 class="com_delete1">删除</h1>
	<div class="com_delete2">
		<p class="com_delete2p">这些项目将被永久删除，并且无法恢复。您确定吗？</p>
		<div class="com_delete2d"><span class="com_delete2d1">确定</span><span class="com_delete2d2">取消</span></div>
	</div>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/js/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/adminv1.js"></script>
<script type="text/javascript">

    function deleteSubject(e) {
        $( "#dialog-confirm" ).dialog({
            autoOpen:true,
            draggable: false,
            resizable: false,
            height: 140,
            modal: true,
            open: function (event, ui) {
                $(".ui-dialog-titlebar-close", $(this).parent()).hide();
            },
            buttons: {
                "确定": function() {
                    $( this ).dialog( "close" );
                    var subjectId = $(e).parent().parent().find("[name = subjectId]").text();
                    var requestParams = {
                        "type" : "admin_delete_subject",
                        'data': '{'
                        + '"subject_ids":["'+subjectId+'"]'
                        + '}'
                    };
                    $.ajax({
                        url: "${ctx}/adminapi",
                        type: "post",
                        data: requestParams,
                        dataType: "json",
                        success: function (data) {
                            $("#loading").empty();
                            if (data != null && data.status == "ok") {
                                alert("删除成功!!")
                                window.location.reload();
                            }else if (data != null && data.status == "server_error") {
                                alert("服务器内部错误");
                            }else if (data != null && data.status == "parameter_error") {
                                alert("发送参数错误");
                            }else if (data != null && data.status == "permission_error") {
                                alert("没有权限");
                            }else if (data != null && data.status == "token_error") {
                                alert("登陆超时,请重新登陆");
                            }
                        },
                        error: function () {
                            $("#loading").empty();
                            alert("服务器内部错误");
                        }
                    });
                },
                "取消": function() {
                    $( this ).dialog( "close" );
                }
            }
        });

    }

    function showSubjectDetail(e) {
        var subjectId = $(e).parent().parent().find("[name = subjectId]").text();
        var url = "${ctx}/subject/subject_detail_share.jsp?subject_id="+subjectId;
        $(".chakan_del").show();
        $(".zhezhao").show();
        $("#detail").attr("src", url);
    };

    $(function () {
            $("#dialog-confirm").dialog({autoOpen:false});

        var begin = 0;
        var hasMoreData = false;
        var key_word = "";

        loadData(begin, key_word, loadDataCallback);

        function loadData(begin, keyWord, callback) {


            var requestParam = {
                "type": "get_subjects",
                'data': '{' + '"begin":"' + begin + '",' +  '"keyword":"'+keyWord +'"' + '}'
            };
            $("#loading").html("<center><td colspan='5' style='text-align:center'>加载数据中，请稍后......</td></center>");
            $.ajax({
                url: "${ctx}/adminapi",
                type: "post",
                data: requestParam,
                dataType: "json",
                success: function (data) {
                    $("#loading").empty();
                    if (data != null && data.status == "ok") {
                        callback(data);
                        var d_width=$(window).width()+'px';
                        var d_height=$(document).height()+'px';
                        $('.zhezhao').css('minHeight',d_height);
                        $('.zhezhao').css('height',$('.actin_list').height())
                    }
                },
                error: function () {
                    $("#loading").empty();
                }
            });
        };

        function loadDataCallback(json) {
            var datas = json.subjects;
            if (datas != null && datas != undefined) {
                var dlen = datas.length;
                begin = begin + dlen;
                for (var i = 0; i < datas.length; ++i) {
                    var dataHtml = "<tr>";
                    dataHtml += "<td name='subjectId' style='text-align:center;'>"
                            + datas[i].subject_id + "</td>";
                    dataHtml += "<td style='text-align:center'>" + datas[i].title + "</td>";
                    dataHtml += "<td style='text-align: center'>" + datas[i].comment_count+"条</td>";
                    dataHtml += "<td style='text-align: center'>" + datas[i].browse_count+"次</td>";
                    dataHtml += "<td style='text-align:center'>" + localFormat(datas[i].create_time) + "</td>";
                    dataHtml += "<td style='text-align:center'><a class='actin_tableb_a' onclick='showSubjectDetail(this)'>详情</a>" +
                            "<a class='actin_tableb_a' href='javascript:void(0)' onclick='deleteSubject(this)'>删除</a> " +
                            "</td>";
                    dataHtml += "</tr>";
                    $(".actin_tableb").append(dataHtml);
                }
            }

            hasMoreData = json.has_more_data;
            if (hasMoreData) {
                $("#loadMoreBtn").show();
                var d_width=$(window).width()+'px';
                var d_height=$(document).height()+'px';
                $('.zhezhao').css('minHeight',d_height);
                $('.zhezhao').css('height',$('.actin_list').height())
            } else {
                $("#loadMoreBtn").hide();
            }
        };

        $("#loadMoreBtn").click(function() {
            $("#loadMoreBtn").hide();
            loadData(begin, key_word, loadDataCallback);
        });

        Date.prototype.Format = function (fmt) { //author: meizz
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds()
                //毫秒
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
                        .substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
                            : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
        function localFormat(time) {
            return new Date(time).Format("yyyy-MM-dd");
        }


        /*查询*/
        $('.actin_list2_4').click(function () {
            var kw = $(".actin_list2_3").val();
            if (kw == '') {
                alert('请输入查询关键字')
                return;
            }
            begin = 0;
            key_word=kw;
            $(".actin_tableb").empty();
            loadData(begin, key_word, loadDataCallback);
        });

        /*新增专题*/
        $('.xin_subiject').click(function(){
        	$('.chakan_del').hide();
            $('.zhezhao').show();
            $('.zhuanti_wrap').show();
        })
        /*点击"查看详情"*/
        $('.chakan_dee').click(function(){
            $('.zhezhao').show();
            $(".chakan_del").show();
            $('.liji_join').hide();
        })
        /*点击取消*/
        $('.cancel').click(function(){
            $('#zhun_photo').prop('src','');
            $('#subject_title').val('');
            $('#subject_content').val('');
            $('.z_fucon').remove();
            $('.zhuanti_wrap').hide();
            $('.zhezhao').hide();
        })
        /*点击X*/
        $('.actinttop img').click(function(){
            $('#zhun_photo').prop('src','');
            $('#subject_title').val('');
            $('#subject_content').val('');
            $('.z_fucon').remove();
            $('.zhezhao').hide();
            $('.zhuanti_wrap').hide();
        })
        /*添加专题页面-创建新的子内容*/
        var num=0;
        $('.z_fuadd').click(function(){
            var for1 = 'subject_img'+num;
            var for2 = 'subject_img_des'+num;
            var for3 ='dd_ing'+num;
            /*预览*/
            var for4='yulan_detimg'+num;
            var for5='yulan_detmiao'+num;
            num++;
            if(num>8){
                alert('最多只能添加8张图！');
                return;
            }
            /*创建新的子内容*/
            var html = '<div class="z_fucon clearfix"><div class="z_fuimg fl">'+
                    '<label for="'+for1+'">请上传图片</label>'+
                    '<input type="file" id="'+for1+'" name="'+for1+'"/>'+
                    '<div class="new_son"><img id="'+for3+'"/></div>'+
                    '<div class="z_futext new_sonc">'+
                    '<label for="'+for2+'">图片描述</label>'+
                    '<textarea class="z_futextsuo" rows="6" id="'+for2+'" style="padding: 10px 0;width: 252px;text-indent: 2em;"></textarea></div></div>'
            $('.z_fu').append(html);

            var htmlz = '<div class="zeng">'+
                    '<img class="'+for4+'" />'+
                    '<p class="'+for5+'"></p></div>'

            $('.add_yulan').append(htmlz);

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
        /*预览效果*/
        $('.yulan').click(function(){
            $('.zhuanti_wrap').fadeOut();
            $('.add_yulan').fadeIn();
            $('.yulan_title').html($('#subject_title').val())
            $('.yulan_con').html($('#subject_content').val().replace(/\n/g,"</br>"))
            var banner_src=$('#zhun_photo').attr('src');
            $('.yulan_banner').attr('src',banner_src);
            /*图文*/
            var new_sonlen=$('.new_son').length;
            for(var i=0;i<new_sonlen;i++){
                $('.add_yulan').find('div.zeng').eq(i).find('img').attr('src',$('.new_son').eq(i).find('img').attr('src'))
                $('.add_yulan').find('div.zeng').eq(i).find('p').html($('.new_sonc').eq(i).find('textarea').val().replace(/\n/g,"</br>"))
            }
        })
        /*点击返回编辑*/
        $('.fanhuibian').click(function(){
            $('.zhuanti_wrap').fadeIn();
            $('.add_yulan').fadeOut();
        })

        /*效验*/
        $('.save').click(function(){
            var subject_icon =$('.z_zhu3 input').val();
            var subject_content=$('.z_zhu4 textarea').val();
            var subject_title=$('#subject_title').val();
            var z_fuinput_len=$(".z_fu input").length;

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
                subject_img_des += '"'+$('#subject_img_des'+i).val().replace(/\n/g,"</br>")+'"';
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
                        window.location.href = "${ctx}/admin/admin/showSubjectsNew.jsp";
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
        })
    });
</script>
</body>
</html>
