/**
 * 初始化餐厅信息管理详情对话框
 */
var RestaurantInfoManagerInfoDlg = {
    restaurantInfoManagerInfoData : {}
};

/**
 * 清除数据
 */
RestaurantInfoManagerInfoDlg.clearData = function() {
    this.restaurantInfoManagerInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RestaurantInfoManagerInfoDlg.set = function(key, val) {
    this.restaurantInfoManagerInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RestaurantInfoManagerInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
RestaurantInfoManagerInfoDlg.close = function() {
    parent.layer.close(window.parent.RestaurantInfoManager.layerIndex);
}

/**
 * 收集数据
 */
RestaurantInfoManagerInfoDlg.collectData = function() {
    var images=[];
    $('.putimg').children().each(function () {
        var imgHTMLObject=$(this);
        var img={};
        img.url=imgHTMLObject.find('img').attr('src');
        img.object_name=imgHTMLObject.find('#object_name').val();
        images.push(img);
    });
    this.restaurantInfoManagerInfoData['images'] = JSON.stringify(images);
    this
    .set('id')
    .set('userId')
    .set('restaurant')
    .set('thumb')
    .set('cityId')
    .set('address')
    .set('businessHours')
    .set('status')
    .set('object_name')
    .set('old_object_name');

}

/**
 * 提交添加
 */
RestaurantInfoManagerInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/restaurantInfoManager/add", function(data){
        Feng.success("添加成功!");
        window.parent.RestaurantInfoManager.table.refresh();
        RestaurantInfoManagerInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.restaurantInfoManagerInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
RestaurantInfoManagerInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/restaurantInfoManager/update", function(data){
        Feng.success("修改成功!");
        window.parent.RestaurantInfoManager.table.refresh();
        RestaurantInfoManagerInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.restaurantInfoManagerInfoData);
    ajax.start();
}

$(function() {
    if($('#status_').val()!=undefined&&$.trim($('#status_').val()).length>0)$('#status').val($('#status_').val()).change();

    if($('#images').text()!=undefined&&$.trim($('#images').text()).length>0){
        // console.log($.trim($('#images').text()));
        var images=JSON.parse($.trim($('#images').text()));
        // console.log(images);
        $(images).each(function () {
            $('.putimg').append('<div class="img" style="width: 20%;float: left;margin-right: 5%;margin-bottom: 20px;">' +
                '                     <img src="'+this.url+'" style="width: 100%" />' +
                '                     <input type="button" value="删除图片" onclick="deleteImg($(this).parent(),$(this).next().val(),$(\'#id\').val());" style="width: 100%" />' +
                '                     <input type="hidden" id="object_name" value="'+this.object_name+'" />' +
                '                 </div>');
        });
    }

    $.post(Feng.ctxPath + "/city/list", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var option=$('<option value="' + jsonObj.id + '" '+(jsonObj.id==$('#cityId_').val()?'selected="selected"':'')+'>' + jsonObj.name + '</option>')
            $("#cityId").append(option);
        }
        $('#cityId').searchableSelect();
    });

    $.post(Feng.ctxPath + "/mgr/list", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var option=$('<option value="' + jsonObj.id + '" '+(jsonObj.id==$('#userId_').val()?'selected="selected"':'')+'>' + jsonObj.name + '</option>')
            $("#userId").append(option);
        }
        $('#userId').searchableSelect();
    });

    // 初始化缩略图上传
    var fileServerPathUp = new $WebUpload("thumb","/tool/uploadFile");
    fileServerPathUp.setUploadBarId("progressBar");
    fileServerPathUp.init("/tool/uploadFile");
});

function addImg(obj) {
    var formData = new FormData();
    var uploadFile = $(obj).get(0).files[0];
    formData.append("file",uploadFile);
    console.log(uploadFile);
    if("undefined" != typeof(uploadFile) && uploadFile != null && uploadFile != ""){
        var imgTypes=uploadFile.type.split("/");
        if(imgTypes[0]!='image'){alert("请勿上传图片之外的文件");return false;}
        $.ajax({
            url:'/tool/uploadFile',
            type:'POST',
            data:formData,
            async: false,
            cache: false,
            contentType: false, //不设置内容类型
            processData: false, //不处理数据
            success:function(data){
                $('.putimg').append('<div class="img" style="width: 20%;float: left;margin-right: 5%;margin-bottom: 20px;">' +
                    '                     <img src="'+data.data+'" style="width: 100%" />' +
                    '                     <input type="button" value="删除图片" onclick="deleteImg($(this).parent(),$(this).next().val(),$(\'#id\').val());" style="width: 100%" />' +
                    '                     <input type="hidden" id="object_name" value="'+data.object_name+'" />' +
                    '                 </div>');

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
        url:'/restaurantInfoManager/deleteImg',
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