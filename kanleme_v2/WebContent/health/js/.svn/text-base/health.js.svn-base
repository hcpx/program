// JavaScript Document
function jisuan(){
	var w_width=$(window).width();
	var w_height=$(window).height();
	var shuju_jiazai_h=(w_height-40)/2+'px';
	
	$('.shuju_jiazai').css('top',shuju_jiazai_h)
	
	
}
jisuan();


/*首页－中青年健康互助-youg_health_nutual*/
/*互助规则*/
$('.yhn3_listlid1').click(function(){
	if($(this).parent().find('.yhn3_listlid2').css('display')=='none'){
		$(this).parent().parent().find('.yhn3_listlid2').hide();
		$(this).parent().find('.yhn3_listlid2').show();
	}else{
		$(this).parent().find('.yhn3_listlid2').hide();
	}
	if($(this).find('img').attr('src')=='images/ic_open.png'){
		$('.yhn3_listlid1').find('img').attr('src','images/ic_open.png');
		$(this).find('img').attr('src','images/ic_close.png');
	}else{
		$(this).find('img').attr('src','images/ic_open.png');
	}
})
