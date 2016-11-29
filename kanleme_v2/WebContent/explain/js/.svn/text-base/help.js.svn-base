// JavaScript Document
function jisuan(){
	var w_width=$(window).width();
	var w_height=$(window).height();
	
	var yhn3_h=$('.yhn3').height()+30;
	$('.help_wrap').css('height',yhn3_h)
	
}
jisuan();


/*帮助*/
$('.yhn3_listlid1').click(function(){
	jisuan();
	if($(this).parent().find('.yhn3_listlid2').css('display')=='none'){
		jisuan()
		$(this).parent().parent().find('.yhn3_listlid2').hide();
		$(this).parent().find('.yhn3_listlid2').show();
	}else{
		jisuan()
		$(this).parent().find('.yhn3_listlid2').hide();
	}
	if($(this).find('img').attr('src')=='images/ic_down.png'){
		jisuan()
		$('.yhn3_listlid1').find('img').attr('src','images/ic_down.png');
		$(this).find('img').attr('src','images/ic_up.png');
	}else{
		jisuan()
		$(this).find('img').attr('src','images/ic_down.png');
	}
})