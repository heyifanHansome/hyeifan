var contentArray=[];
/**
 * 初始化活动管理详情对话框
 */
var ActivityInfoDlg = {
    activityInfoData : {}
};

/**
 * 清除数据
 */
ActivityInfoDlg.clearData = function() {
    this.activityInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ActivityInfoDlg.set = function(key, val) {
    this.activityInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ActivityInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ActivityInfoDlg.close = function() {
    parent.layer.close(window.parent.Activity.layerIndex);
}

/**
 * 收集数据
 */
ActivityInfoDlg.collectData = function() {
    var images=[];
    $('.putimg').children().each(function () {
        var imgHTMLObject=$(this);
        var img={};
        img.url=imgHTMLObject.find('img').attr('src');
        img.object_name=imgHTMLObject.find('#object_name').val();
        images.push(img);
    });
    if($('#msg').val()!=undefined)this.restaurantInfoManagerInfoData['msg'] = $('#msg').val();
    this.restaurantInfoManagerInfoData['video'] = $('#video').attr('href');
    this.restaurantInfoManagerInfoData['thumb'] = JSON.stringify(images);
    // this.activityInfoData['content'] = ActivityInfoDlg.editor.txt.html();
    var contents=[];
    $(contentArray).each(function () {
        var content={};
        content.title=$(this.title).find('#title').val();
        content.content=this.content.txt.html();
    });
    this
    .set('id')
    .set('columnId')
    .set('title')
    .set('thumb')
        .set('object_name')
        .set('old_object_name')
    .set('description')
    .set('startTime')
    .set('endTime')
    .set('cityId')
        .set('sourceId')
        .set('video_object_name')
        .set('is_ok')
        .set('apply_num')
    // model

    // .set('uid')
    // .set('publishIp')
    // .set('createTime')
    // .set('updateTime');
}

/**
 * 提交添加
 */
ActivityInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/activity/add", function(data){
        Feng.success("添加成功!");
        window.parent.Activity.table.refresh();
        ActivityInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.activityInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ActivityInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/activity/update", function(data){
        Feng.success("修改成功!");
        window.parent.Activity.table.refresh();
        ActivityInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.activityInfoData);
    ajax.start();
}
function sendUserApiMsg(is_ok_object,is_ok_val) {
    if(is_ok_val=='2'){
        $(is_ok_object).after('<div style="float: left;">' +
            '原因:<input id="msg" />' +
            '</div>');
    }else{
        $(is_ok_object).next().remove();
    }
}
$(function() {
    $.post(Feng.ctxPath + "/city/list", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var option=$('<option value="' + jsonObj.id + '" '+(jsonObj.id==$('#cityId_').val()?'selected="selected"':'')+'>' + jsonObj.name + '</option>')
            $("#cityId").append(option);
        }
        $('#cityId').searchableSelect();
    });
    $('#is_ok').val($.trim($('#is_ok_').val())).change();
    $.post(Feng.ctxPath + "/columnType/getColumnTypeList", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var option=$('<option value="' + jsonObj.id + '" '+(jsonObj.id==$('#columnId_').val()?'selected="selected"':'')+'>' + jsonObj.name + '</option>')
            $("#columnId").append(option);
        }
        $('#columnId').searchableSelect();
    });
    Feng.initValidator("noticeInfoForm", ActivityInfoDlg.validateFields);
    // 初始化编辑器
    // var E = window.wangEditor;
    // var editor = new E('#editor');
    // editor.create();
    // editor.txt.html($("#content").val());
    // ActivityInfoDlg.editor = editor;
    // // 初始化缩略图上传
    // var avatarUp = new $WebUpload("thumb","/tool/uploadFile");
    // avatarUp.setUploadBarId("progressBar");
    // avatarUp.init("/tool/uploadFile");

    if($('#images').text()!=undefined&&$.trim($('#images').text()).length>0){
        // console.log($.trim($('#images').text()));
        var images=JSON.parse($.trim($('#images').text()));
        // console.log(images);
        $(images).each(function () {
            $('.putimg').eq(0).append('<div class="img" style="width: 20%;float: left;margin-right: 5%;margin-bottom: 20px;">' +
                '                     <img src="'+this.url+'" style="width: 100%" />' +
                '                     <input type="button" value="删除图片" onclick="deleteImg($(this).parent(),$(this).next().val(),$(\'#id\').val());" style="width: 100%" />' +
                '                     <input type="hidden" id="object_name" value="'+this.object_name+'" />' +
                '                 </div>');
        });
    }

    if($.trim($('#video_').val()).length>0||$.trim($('#video_object_name_').val()).length>0){
        $('.putimg').eq(1).empty().append('<div style="width: 20%;float: left;margin-right: 5%;margin-bottom: 20px;">' +
            '                     <div><a id="video" href="'+$.trim($('#video_').val())+'" target="_blank" style="width: 100%" /></div>' +
            '                     <input type="hidden" id="video_object_name" value="'+$.trim($('#video_object_name_').val())+'" />' +
            '                 </div>');
    }

});
function addContent(index_num,parentHtmlObj) {
    var div=$('<div></div>');
    var title=$('<div class="form-group">' +
        '                              <label class="col-sm-2 control-label">标题</label>' +
        '                            <div class="col-sm-7">' +
        '                              <input type="text" id="title" />' +
        '                             </div>' +
        '                        </div>')
    var content=$('<div class="form-group">' +
        '         <label class="col-sm-2 control-label">内容</label>' +
        '         <div class="col-sm-10">' +
        '           <div id="editor'+index_num+'" class="editorHeight"></div>' +
        '         </div>' +
        '</div>');
    $(parentHtmlObj).append(div.append(title).append(content));
    var E = window.wangEditor;
    var editorName=('#editor'+index_num);
    var editor = new E(editorName);
    editor.create();
    var contentObj={};
    contentObj.title=title
    contentObj.editor=editor
    contentArray.push(contentObj);
}
function addImg(obj,objectType) {
    if(objectType=='image'&&$('.putimg').eq(0).children().length>8)alert("已经有9张图片了")
    var formData = new FormData();
    var uploadFile = $(obj).get(0).files[0];
    formData.append("file",uploadFile);
    console.log(uploadFile);
    if("undefined" != typeof(uploadFile) && uploadFile != null && uploadFile != ""){
        var imgTypes=uploadFile.type.split("/");
        if(objectType=='image'&&imgTypes[0]!='image'){alert("请勿上传图片之外的文件");return false;}
        if(objectType=='video'&&imgTypes[0]!='video'){alert("请勿上传视频之外的文件");return false;}
        $.ajax({
            url:'/tool/uploadFile',
            type:'POST',
            data:formData,
            // async: false,
            cache: false,
            contentType: false, //不设置内容类型
            processData: false, //不处理数据
            beforeSend:function(){
                if ($('.putimg').eq(1).children().length > 0) $('#old_object_name').val($('#video_object_name').val());
                $('.putimg').eq(1).empty().append('<p>上传中...请稍后</p>')
            },
        success:function(data){
                console.log('video:',data)
                if(objectType=='image') {
                    $('.putimg').eq(0).append('<div class="img" style="width: 20%;float: left;margin-right: 5%;margin-bottom: 20px;">' +
                        '                     <img src="' + data.data + '" style="width: 100%" />' +
                        '                     <input type="button" value="删除图片" onclick="deleteImg($(this).parent(),$(this).next().val(),$(\'#id\').val());" style="width: 100%" />' +
                        '                 </div>');
                    '                     <input type="hidden" id="object_name" value="' + data.object_name + '" />'
                }
                if(objectType=='video'){
                    $('.putimg').eq(1).empty().append('<div style="width: 20%;float: left;margin-right: 5%;margin-bottom: 20px;">' +
                        '                     <div><a id="video" href="'+data.data+'" target="_blank" style="width: 100%" >'+data.data+'</a></div>' +
                        '                     <input type="hidden" id="video_object_name" value="'+data.object_name+'" />' +
                        '<p>上传完成!</p>'+
                        '                 </div>');
                }
            },
            error:function(e){
                console.log(e);
                alert("上传失败！");
            }
        })
    }else {
        alert("选择的文件无效！请重新选择");
    }
    $(obj).replaceWith('<input type="file" style="display: none;" onchange="addImg($(this));" />');
}
function deleteImg(parent_obj,object_name,id) {
    $.ajax({
        url:'/avtivity/deleteImg',
        data:{object_name:object_name,id:id},
        success:function (data) {
            alert(data.message)
            $(parent_obj).remove();
        },
        error:function (e) {
            console.log(e);
            alert("删除失败")
        }
    });
}