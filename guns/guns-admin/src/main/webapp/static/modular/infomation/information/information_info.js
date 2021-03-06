/**
 * 初始化资讯管理详情对话框
 */
var InformationInfoDlg = {
    informationInfoData : {}
};

/**
 * 清除数据
 */
InformationInfoDlg.clearData = function() {
    this.informationInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
InformationInfoDlg.set = function(key, val) {
    if(key == 'tagId'){
        var heyifanArr = $('#tagId').val();
        var newDemo ="" ;
        if(heyifanArr!=null&&heyifanArr.length>0){
            for (var z=0;z< heyifanArr.length;z++) {
                newDemo += heyifanArr[z] +",";
            }
            this.informationInfoData[key] = newDemo;
            return this;
        }else {
            this.informationInfoData[key] = '';
            return this;
        }
    }else{
        this.informationInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
        return this;
    }

};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
InformationInfoDlg.get = function(key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
InformationInfoDlg.close = function() {
    parent.layer.close(window.parent.Information.layerIndex);
};

/**
 * 收集数据
 */
InformationInfoDlg.collectData = function() {
    this.informationInfoData['content'] = InformationInfoDlg.editor.txt.html();
    this
    .set('id')
    .set('columnId')
    .set('title')
    .set('thumb')
    .set('images')
    .set('description')
    .set('cityId')
    .set('sourceId')
    .set('uid')
    .set('tagId')
};

/**
 * 提交添加
 */
InformationInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/information/add", function(data){
        Feng.success("添加成功!");
        window.parent.Information.table.refresh();
        InformationInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.informationInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
InformationInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/information/update", function(data){
        Feng.success("修改成功!");
        window.parent.Information.table.refresh();
        InformationInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.informationInfoData);
    ajax.start();
};

$(function() {

    /**
     * 动态获取所有栏目
     */
    var ajax = new $ax(Feng.ctxPath + "/information/getColumnTypeInformation", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            $("#columnId").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();


    /**
     * 动态获取所有城市
     */
    var ajax = new $ax(Feng.ctxPath + "/city/getAllCity", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            $("#cityId").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();

    /**
     * 动态获取所有用户
     */
    var ajax = new $ax(Feng.ctxPath + "/userApi/getAllUserApi", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            $("#uid").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();

    // 初始化缩略图上传
    var avatarUp = new $WebUpload("thumb", "/tool/uploadFile");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init("/tool/uploadFile");



    //初始化编辑器
    var E = window.wangEditor;
    var editor = new E('#editor');
    editor.create();
    editor.txt.html($("#content").val());
    InformationInfoDlg.editor = editor;
});
