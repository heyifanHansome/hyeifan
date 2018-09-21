/**
 * 初始化星厨俱乐部详情对话框
 */
var ClubInfoDlg = {
    clubInfoData : {}
};

/**
 * 清除数据
 */
ClubInfoDlg.clearData = function() {
    this.clubInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ClubInfoDlg.set = function(key, val) {
    this.clubInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ClubInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ClubInfoDlg.close = function() {
    parent.layer.close(window.parent.Club.layerIndex);
}

/**
 * 收集数据
 */
ClubInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('gradeId')
    .set('realName')
    .set('idCard')
    .set('status')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
ClubInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/club/add", function(data){
        Feng.success("添加成功!");
        window.parent.Club.table.refresh();
        ClubInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.clubInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ClubInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/club/update", function(data){
        Feng.success("修改成功!");
        window.parent.Club.table.refresh();
        ClubInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.clubInfoData);
    ajax.start();
}

$(function() {

});
