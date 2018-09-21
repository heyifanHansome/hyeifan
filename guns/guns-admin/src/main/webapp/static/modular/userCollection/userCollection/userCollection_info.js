/**
 * 初始化用户收藏管理详情对话框
 */
var UserCollectionInfoDlg = {
    userCollectionInfoData : {}
};

/**
 * 清除数据
 */
UserCollectionInfoDlg.clearData = function() {
    this.userCollectionInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserCollectionInfoDlg.set = function(key, val) {
    this.userCollectionInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserCollectionInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserCollectionInfoDlg.close = function() {
    parent.layer.close(window.parent.UserCollection.layerIndex);
}

/**
 * 收集数据
 */
UserCollectionInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('columnId')
    .set('worksId')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
UserCollectionInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userCollection/add", function(data){
        Feng.success("添加成功!");
        window.parent.UserCollection.table.refresh();
        UserCollectionInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userCollectionInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
UserCollectionInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userCollection/update", function(data){
        Feng.success("修改成功!");
        window.parent.UserCollection.table.refresh();
        UserCollectionInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userCollectionInfoData);
    ajax.start();
}

$(function() {

});
