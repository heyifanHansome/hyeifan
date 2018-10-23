function showAll(ele, boxH) {
    boxH = boxH || '212.5rem'
    $('.showAllBtn').bind('click',function(){
        $(this).toggleClass('active')
        if($(this).hasClass('active')){
            $(this).find('.w').html('收起全部')
            $(ele).css('height','auto')
        }else{
            $(this).find('.w').html('查看全部')
            $(ele).css('height',boxH)
        }
    })
}