// JavaScript Document
$(function(){
	function jisuan(){
		var window_w=$(window).width();
		var window_h=$(window).height();
		var document_w=$(document).width();
		var document_h=$(document).height();
	}
	jisuan();
	
	/*首页-index.html*/
	/*banner中间内容垂直居中*/
	var bannerc_mt=($('.banner').height()-$('.bannerc').height())/2+'px'
	$('.bannerc').css('marginTop',bannerc_mt)
	/*banner切换“公众号”我二维码“的效果*/
	$('.banner_wei1').hover(function(){
		$('.banner_wei a').removeClass('active');
		$(this).addClass('active');
		$('.banner_weiimg img').stop().fadeOut();
		$('.banner_weiimg1').fadeIn();
	},function(){
		$('.banner_wei a').removeClass('active');
		$('.banner_weiimg img').stop().fadeOut();
	})
	$('.banner_wei2').hover(function(){
		$('.banner_wei a').removeClass('active');
		$(this).addClass('active');
		$('.banner_weiimg img').stop().fadeOut();
		$('.banner_weiimg2').fadeIn();
	},function(){
		$('.banner_wei a').removeClass('active');
		$('.banner_weiimg img').stop().fadeOut();
	})
	/*首页-index.html----------------------------------------------完*/
	
	/*app下载页-appDownload.html*/
	/*第一部分二维码全切换*/
	$('.adlbrcy_wei1').hover(function(){
			$('.adlbrcy_weiimg1').fadeIn()
			$('.adlbrcy_weiimg2').stop().fadeOut()
			$('.adlbrcy_wei2 img').attr('src','images/app_downbg1_05.png')
		if($('.adlbrcy_wei1 img').attr('src')=='images/app_downbg1_04.png'){
			$('.adlbrcy_wei1 img').attr('src','images/app_downbg1_04ac.png')
		}else{
			$('.adlbrcy_wei1 img').attr('src','images/app_downbg1_04.png')
		}
	},function(){
		$('.adlbrcy_wei1 img').attr('src','images/app_downbg1_04.png')
		$('.adlbrcy_wei2 img').attr('src','images/app_downbg1_05.png')
		$('.adlbrcy_weiimg1').stop().fadeOut()
		$('.adlbrcy_weiimg2').stop().fadeOut()
	})
	$('.adlbrcy_wei2').hover(function(){
			$('.adlbrcy_weiimg2').fadeIn()
			$('.adlbrcy_weiimg1').stop().fadeOut()
			$('.adlbrcy_wei1 img').attr('src','images/app_downbg1_04.png')
		if($('.adlbrcy_wei2 img').attr('src')=='images/app_downbg1_05.png'){
			$('.adlbrcy_wei2 img').attr('src','images/app_downbg1_05ac.png')
		}else{
			$('.adlbrcy_wei2 img').attr('src','images/app_downbg1_05.png')
		}
	},function(){
		$('.adlbrcy_wei1 img').attr('src','images/app_downbg1_04.png')
		$('.adlbrcy_wei2 img').attr('src','images/app_downbg1_05.png')
		$('.adlbrcy_weiimg1').stop().fadeOut()
		$('.adlbrcy_weiimg2').stop().fadeOut()
	})
	/*第五部分二维码切换*/
	$('.adlbrcy_weic1').hover(function(){
		$('.adlbrcy_weicimg2').stop().fadeOut()
		$('.adlbrcy_weicimg1').fadeIn()
	},function(){
		$('.adlbrcy_weicimg2').stop().fadeOut()
		$('.adlbrcy_weicimg1').stop().fadeOut()
	})
	$('.adlbrcy_weic2').hover(function(){
		$('.adlbrcy_weicimg1').stop().fadeOut()
		$('.adlbrcy_weicimg2').fadeIn()
	},function(){
		$('.adlbrcy_weicimg2').stop().fadeOut()
		$('.adlbrcy_weicimg1').stop().fadeOut()
	})
	
	/*app下载页-appDownload.html-------------------------------完*/
	
	/*帮助中心-helpCenter.html--------------------------------------*/
	/*中间内容的最小高度*/
	function hc_jisu(){
		var window_h=$(window).height();
		var top_h=$('.top').outerHeight();
		var foot_h=$('.foot').outerHeight();
		var bottom_h=$('.bottom').outerHeight();
		var hc_pt=parseInt($('.hc').css('padding-top'))*2;
		var hcc_minh=window_h-top_h-foot_h-bottom_h-hc_pt;
		$('.hcc').css('minHeight',hcc_minh)
		$('.hccy').css('minHeight',hcc_minh)		
		var hccy_h=$('.hccy').height();
		var hccz_h=$('.hccz').height();
		if(hccz_h>hccy_h){
			$('.hccz').css('height',hccz_h)
			$('.hccy').css('height',hccz_h)
		}else{
			$('.hccz').css('height',hccy_h)
			$('.hccy').css('height',hccy_h)
		}
	}
	hc_jisu()
	
	/*左边移入控制有限显示效果*/
	$('.hcczlist li').click(function(){
		$('.hcczlist li').removeClass('active');
		$(this).addClass('active');
		var _index=$(this).index()+1;
		$('.hccy .hccyw').hide();
		$('.hccy'+_index).show()
		hc_jisu()
	})
	
	/*帮助中心-helpCenter.html--------------------------------------完*/
	
	/*健康互助-healthHelp.html--------------------------------------*/
	/*第一部分*/
	/*中间内容居中显示*/
	var hHp1_h=$('.hHp1').height();
	var hHpc_h_xin1=$('.hHp1c_change1').outerHeight();
	var hHp1c_mt_xin1=(hHp1_h-hHpc_h_xin1)/2;
	$('.hHp1c_change1').css('marginTop',hHp1c_mt_xin1)
	/*第三部分*/
	/*中间内容居中显示*/
	var hHp3_h=$('.hHp3').height();
	var hHpc_h_xin1_1=$('.hHp1c_change1_1').outerHeight();
	var hHp1c_mt_xin1_1=(hHp3_h-hHpc_h_xin1_1)/2;
	$('.hHp1c_change1_1').css('marginTop',hHp1c_mt_xin1_1)
	/*第五部分*/
	/*左右切换效果*/
	$('.hHp5cu li').hover(function(){
		$('.hHp5cu li').removeClass('active')
		$(this).addClass('active')
	})
	/*移入马上加入*/
	/*健康互助-healthHelp.html--------------------------------------完*/
	
	/*新增*/
	/*第一部分*/
	$('.xin_jiaru1').hover(function(){
		$('.hHp1c_change1').hide();
		$('.hHp1c_change2').show();
		var hHp1_h=$('.hHp1').height();
		var hHpc_h_xin2=$('.hHp1c_change2').outerHeight();
		var hHp1c_mt_xin2=(hHp1_h-hHpc_h_xin2)/2;
		$('.hHp1c_change2').css('marginTop',hHp1c_mt_xin2)
	},function(){	
	})
	
	$('.hHp1c_change2').hover(function(){
		return false;
	},function(){
		$('.hHp1c_change2').hide();
		$('.hHp1c_change1').show();
		var hHp1_h=$('.hHp1').height();
		var hHpc_h_xin2=$('.hHp1c_change2').outerHeight();
		var hHp1c_mt_xin2=(hHp1_h-hHpc_h_xin2)/2;
		$('.hHp1c_change2').css('marginTop',hHp1c_mt_xin2)
		
	})
	
	/*第三部分*/
	$('.xin_jiaru2').hover(function(){
		$('.hHp1c_change1_1').hide();
		$('.hHp1c_change2_1').show();
		var hHp3_h=$('.hHp3').height();
		var hHpc_h_xin2_1=$('.hHp1c_change2_1').outerHeight();
		var hHp1c_mt_xin2_1=(hHp3_h-hHpc_h_xin2_1)/2;
		$('.hHp1c_change2_1').css('marginTop',hHp1c_mt_xin2_1)
	},function(){	
	})
	$('.hHp1c_change2_1').hover(function(){
		return false;
	},function(){
		$('.hHp1c_change2_1').hide();
		$('.hHp1c_change1_1').show();
		var hHp3_h=$('.hHp3').height();
		var hHpc_h_xin2_1=$('.hHp1c_change2_1').outerHeight();
		var hHp1c_mt_xin2_1=(hHp3_h-hHpc_h_xin2_1)/2;
		$('.hHp1c_change2_1').css('marginTop',hHp1c_mt_xin2_1)
	})
	
	/*点击回到顶部*/
	$(document).scroll(function(){
        var top = $(document).scrollTop();
        if(top >200){
			$(".back-to-top").fadeIn()
		}else{
			$(".back-to-top").fadeOut()
		}
    });
	$(".back-to-top").click(function(){  
		$('body,html').animate({scrollTop:0},1000);  
		return false;  
	});
	
})