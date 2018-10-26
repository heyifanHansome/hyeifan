/**
 * 初始化标签管理表详情对话框
 */
var TagInfoDlg = {
    tagInfoData : {}
};

/**
 * 清除数据
 */
TagInfoDlg.clearData = function() {
    this.tagInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TagInfoDlg.set = function(key, val) {
    this.tagInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TagInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
TagInfoDlg.close = function() {
    parent.layer.close(window.parent.Tag.layerIndex);
}

/**
 * 收集数据
 */
TagInfoDlg.collectData = function() {
    this
        .set('id')
        .set('columnId')
        .set('name')
        .set('picture')
        .set('object_name')
        .set('old_object_name')
    // .set('createTime')
    // .set('updateTime');
}

/**
 * 提交添加
 */
TagInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/tag/add", function(data){
        Feng.success("添加成功!");
        window.parent.Tag.table.refresh();
        TagInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.tagInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
TagInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/tag/update", function(data){
        Feng.success("修改成功!");
        window.parent.Tag.table.refresh();
        TagInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.tagInfoData);
    ajax.start();
}

$(function() {
    $("#columnId").append('<option value="0">通用标签</option>');
    $.post(Feng.ctxPath + "/columnType/getColumnTypeList", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var optionstring = "";
            var option=$('<option value="' + jsonObj.id + '" '+(jsonObj.id==$('#parentId_').val()?'selected="selected"':'')+'>' + jsonObj.name + '</option>')
            $("#columnId").append(option);
        }
    });
    var fileServerPathUp = new $WebUpload("picture","/tool/uploadFile");
    fileServerPathUp.setUploadBarId("progressBar");
    fileServerPathUp.init("/tool/uploadFile");
});
