// JavaScript Document
	jisuan();
	index_jisuan();
	
	/*动态获取网站公共宽高*/
	function jisuan(){
		var w_width=$(window).width();
		var w_height=$(window).height();
		var d_width=$(document).width();
		var d_height=$(document).height();
		
		$('.zhezhao').css('height',d_height)
		/*左侧头部*/
		var wrap_lty1_h=$('.wrap_lty1').height();
		var wrap_lty2_h=$('.wrap_lty2').height();
		var wrap_lty_mt=($('.wrap_lt').height()-wrap_lty1_h-wrap_lty2_h-10)/2+'px';
		$('.wrap_lty').css('marginTop',wrap_lty_mt)
		/*右侧的宽度和高度*/
		var wrap_r_w=d_width-240+'px';
		
		$('.wrap_r').css('minHeight',w_height)
		/*左侧导航列表*/
		$('.wrap_l').css('minHeight',w_height)
		if($(window).scrollTop()>0){
			$('.wrap_l').css('height',$('.wrap_r').height())
		}
	}
	
	/*公共部分*/
	/*展开收起左侧菜单*/
	$('.wrap_lcli').click(function(){
		$('.wrap_lc li').removeClass('active');
		$(this).addClass('active');
		$('.wrap_lc li').find('ul').stop().slideUp();
		$(this).find('ul').stop().slideDown();
	})
	/*首页-开始*/
	/*项目进度中间字居中*/
	function index_jisuan(){
		var index_brulyp_mt=(100-$('.index_bruly p').height())/2+'px';
		$('.index_bruly p').css('marginTop',index_brulyp_mt)
	}
	/*首页-结束*/
/*********************************************************************************************************/
	/*所有的遮罩层及弹层关闭效果*/
	/*点击"X"关闭弹层*/
	$('.actinttop img').click(function(){
		$('.zhezhao').fadeOut();
		$('.actint').fadeOut();
		/*系统管理-角色管理-角色信息修改*/
		$('.jiaoxiu').fadeOut();
		/*社区管理-帖子管理-新建圈子*/
		$('.xin_quan').fadeOut();
		/*社区管理-帖子管理-新建帖子*/
		$('.xin_tie').fadeOut();
		/*资讯管理-推送消息管理-新建推送消息*/
		$('.xin_tui').fadeOut();
		/*资讯管理-推送消息管理-系统消息*/
		$('.xin_xi').fadeOut();
		/*资讯管理-推送消息管理-新建公告*/
		$('.xin_gong').fadeOut();
		/*系统管理-用户管理-添加管理员*/
		$('.xin_guan').fadeOut();
		/*系统管理-用户管理-添加用户*/
		$('.xin_yong').fadeOut();
		/*首页-专题管理-添加专题*/
		$('.zhuanti_wrap').fadeOut();
		$('.chakan_del').fadeOut();
		$('.chakan_delar').fadeOut();
		$('.fenhaodiv').fadeOut();
		$('.circle_detiel').fadeOut();

	})
	/*点击"遮罩层"关闭弹层*/
	$('.zhezhao').click(function(){
		$('.zhezhao').fadeOut();
		$('.actint').fadeOut();
		/*系统管理-角色管理-角色信息修改*/
		$('.jiaoxiu').fadeOut();
		/*社区管理-帖子管理-新建圈子*/
		$('.xin_quan').fadeOut();
		/*社区管理-帖子管理-新建帖子*/
		$('.xin_tie').fadeOut();
		/*资讯管理-推送消息管理-新建推送消息*/
		$('.xin_tui').fadeOut();
		/*资讯管理-推送消息管理-系统消息*/
		$('.xin_xi').fadeOut();
		/*资讯管理-推送消息管理-新建公告*/
		$('.xin_gong').fadeOut();
		/*系统管理-用户管理-添加管理员*/
		$('.xin_guan').fadeOut();
		/*系统管理-用户管理-添加用户*/
		$('.xin_yong').fadeOut();
		/*首页-专题管理-添加专题*/
		$('.zhuanti_wrap').fadeOut();
		$('.chakan_del').fadeOut();
		$('.chakan_delar').fadeOut();
		$('.fenhaodiv').fadeOut();
		$('.circle_detiel').fadeOut();
	})
/*********************************************************************************************************/	

	/*首页滚动条效果*/
	$('.index_bl').perfectScrollbar();
	$('.index_br').perfectScrollbar();
	
	
	/*当滚动条滚动的时候*/
	$(window).scroll(function () {
		$('.wrap_l').css('height',$('.wrap_r').height())
	})
	window.onresize = function(){ 
		if($(window).scrollTop()>0){
			$('.wrap_l').css('height',$('.wrap_r').height())
		}
		jisuan();
		index_jisuan()
	}