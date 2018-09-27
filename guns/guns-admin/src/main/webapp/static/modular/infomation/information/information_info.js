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
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
InformationInfoDlg.set = function(key, val) {
    this.informationInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
InformationInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
InformationInfoDlg.close = function() {
    parent.layer.close(window.parent.Information.layerIndex);
}

/**
 * 收集数据
 */
InformationInfoDlg.collectData = function() {
    this
    .set('id')
    .set('columnId')
    .set('title')
    .set('thumb')
    .set('images')
    .set('url')
    .set('description')
    .set('cityId')
    .set('sourceId')
    .set('uid')
    .set('publishIp')
    .set('content')
}

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
}

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
}

$(function() {


    /**
     * 动态获取所有栏目
     */
    var ajax = new $ax(Feng.ctxPath + "/works/getAllColumnType", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            $("#columnId").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();


    /**
     * 动态获取所有用户
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
    var ajax = new $ax(Feng.ctxPath + "/mgr/getAllUser", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            $("#uid").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();

    // 初始化缩略图上传
    var avatarUp = new $WebUpload("thumb");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init();

});
