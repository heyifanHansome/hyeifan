/**
 * 初始化用户详情详情对话框
 */
var UserInfoInfoDlg = {
    userInfoInfoData : {}
};

/**
 * 清除数据
 */
UserInfoInfoDlg.clearData = function() {
    this.userInfoInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserInfoInfoDlg.set = function(key, val) {
    this.userInfoInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserInfoInfoDlg.get = function(key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
UserInfoInfoDlg.close = function() {
    parent.layer.close(window.parent.UserInfo.layerIndex);
};

/**
 * 收集数据
 */
UserInfoInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('apiToken')
    .set('credits')
    .set('loginIp')
    .set('createTime')
    .set('realName')
    .set('idCard')
    .set('cityId')
    .set('joinClub')
    .set('appointment')
    .set('enlightening');
};

/**
 * 提交添加
 */
UserInfoInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userInfo/add", function(data){
        Feng.success("添加成功!");
        window.parent.UserInfo.table.refresh();
        UserInfoInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userInfoInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
UserInfoInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userInfo/update", function(data){
        Feng.success("修改成功!");
        window.parent.UserInfo.table.refresh();
        UserInfoInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userInfoInfoData);
    ajax.start();
};

$(function() {
    /**
     * 动态获取城市类型
     */
    var ajax = new $ax(Feng.ctxPath + "/city/getAllCity", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            console.log(jsonObj)
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
            $("#userId").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();

});
