/**
 * 初始化评论管理详情对话框
 */
var UserCommentInfoDlg = {
    userCommentInfoData : {}
};

/**
 * 清除数据
 */
UserCommentInfoDlg.clearData = function() {
    this.userCommentInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserCommentInfoDlg.set = function(key, val) {
    this.userCommentInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserCommentInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserCommentInfoDlg.close = function() {
    parent.layer.close(window.parent.UserComment.layerIndex);
}

/**
 * 收集数据
 */
UserCommentInfoDlg.collectData = function() {
    this
    .set('id')
    // .set('userId')
    // .set('columnId')
    // .set('itemId')
    .set('content')
    // .set('createTime')
    // .set('updateTime')
    // .set('commentId');
}

/**
 * 提交添加
 */
UserCommentInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userComment/add", function(data){
        Feng.success("添加成功!");
        window.parent.UserComment.table.refresh();
        UserCommentInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userCommentInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
UserCommentInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userComment/update", function(data){
        Feng.success("修改成功!");
        window.parent.UserComment.table.refresh();
        UserCommentInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userCommentInfoData);
    ajax.start();
}

$(function() {

});
