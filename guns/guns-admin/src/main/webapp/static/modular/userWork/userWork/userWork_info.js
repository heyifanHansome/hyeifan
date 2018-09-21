/**
 * 初始化用户作品详情对话框
 */
var UserWorkInfoDlg = {
    userWorkInfoData : {}
};

/**
 * 清除数据
 */
UserWorkInfoDlg.clearData = function() {
    this.userWorkInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserWorkInfoDlg.set = function(key, val) {
    this.userWorkInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserWorkInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserWorkInfoDlg.close = function() {
    parent.layer.close(window.parent.UserWork.layerIndex);
}

/**
 * 收集数据
 */
UserWorkInfoDlg.collectData = function() {
    this
    .set('id')
    .set('roleId')
    .set('userId')
    .set('worksId')
    .set('createdTime')
    .set('updatedTime');
}

/**
 * 提交添加
 */
UserWorkInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userWork/add", function(data){
        Feng.success("添加成功!");
        window.parent.UserWork.table.refresh();
        UserWorkInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userWorkInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
UserWorkInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userWork/update", function(data){
        Feng.success("修改成功!");
        window.parent.UserWork.table.refresh();
        UserWorkInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userWorkInfoData);
    ajax.start();
}

$(function() {

});
