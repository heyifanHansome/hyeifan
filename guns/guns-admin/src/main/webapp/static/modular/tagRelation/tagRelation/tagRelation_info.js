/**
 * 初始化标签关联详情对话框
 */
var TagRelationInfoDlg = {
    tagRelationInfoData : {}
};

/**
 * 清除数据
 */
TagRelationInfoDlg.clearData = function() {
    this.tagRelationInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TagRelationInfoDlg.set = function(key, val) {
    this.tagRelationInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TagRelationInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
TagRelationInfoDlg.close = function() {
    parent.layer.close(window.parent.TagRelation.layerIndex);
}

/**
 * 收集数据
 */
TagRelationInfoDlg.collectData = function() {
    this
    .set('id')
    .set('columnId')
    .set('commonTypeId')
    .set('createTime')
    .set('updateTime')
    .set('relationId');
}

/**
 * 提交添加
 */
TagRelationInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/tagRelation/add", function(data){
        Feng.success("添加成功!");
        window.parent.TagRelation.table.refresh();
        TagRelationInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.tagRelationInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
TagRelationInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/tagRelation/update", function(data){
        Feng.success("修改成功!");
        window.parent.TagRelation.table.refresh();
        TagRelationInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.tagRelationInfoData);
    ajax.start();
}

$(function() {

});
