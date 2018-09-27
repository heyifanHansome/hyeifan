/**
 * 初始化招聘管理详情对话框
 */
var RecruitInfoDlg = {
    recruitInfoData : {},
    editor: null
};

/**
 * 清除数据
 */
RecruitInfoDlg.clearData = function() {
    this.recruitInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RecruitInfoDlg.set = function(key, val) {
    this.recruitInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RecruitInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
RecruitInfoDlg.close = function() {
    parent.layer.close(window.parent.Recruit.layerIndex);
}

/**
 * 收集数据
 */
RecruitInfoDlg.collectData = function() {
    this.recruitInfoData['content'] = RecruitInfoDlg.editor.txt.html();
    this
    .set('id')
    .set('columnId')
    .set('title')
    .set('thumb')
    .set('description')
    .set('cityId')
    .set('address')
    .set('sourceId');
    // .set('uid')
    // .set('publishIp');
    // .set('createTime')
    // .set('updatedTime');
}

/**
 * 提交添加
 */
RecruitInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/recruit/add", function(data){
        Feng.success("添加成功!");
        window.parent.Recruit.table.refresh();
        RecruitInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.recruitInfoData);
    ajax.start();
    console.log(this.recruitInfoData)
}

/**
 * 提交修改
 */
RecruitInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/recruit/update", function(data){
        Feng.success("修改成功!");
        window.parent.Recruit.table.refresh();
        RecruitInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.recruitInfoData);
    ajax.start();

}

$(function() {
    $("#cityId").append('<option value="0">全国</option>');
    $.post(Feng.ctxPath + "/city/list", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var option=$('<option value="' + jsonObj.id + '" '+(jsonObj.id==$('#cityId_').val()?'selected="selected"':'')+'>' + jsonObj.name + '</option>')
            $("#cityId").append(option);
        }
        $('#cityId').searchableSelect();
    });
    // $("#columnId").append('<option value="0">通用标签</option>');
    $.post(Feng.ctxPath + "/columnType/getColumnTypeList", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var option=$('<option value="' + jsonObj.id + '" '+(jsonObj.id==$('#columnId_').val()?'selected="selected"':'')+'>' + jsonObj.name + '</option>')
            $("#columnId").append(option);
        }
        $('#columnId').searchableSelect();
    });

    Feng.initValidator("noticeInfoForm", RecruitInfoDlg.validateFields);
    // 初始化编辑器
    var E = window.wangEditor;
    var editor = new E('#editor');
    editor.create();
    editor.txt.html($("#content").val());
    RecruitInfoDlg.editor = editor;

    // 初始化缩略图上传
    var fileServerPathUp = new $WebUpload("thumb","/tool/uploadFile");
    fileServerPathUp.setUploadBarId("progressBar");
    fileServerPathUp.init("/tool/uploadFile");
});
