/**
 * 初始化招聘管理详情对话框
 */
var RecruitInfoDlg = {
    recruitInfoData : {}
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
    this
    .set('id')
    .set('columnId')
    .set('title')
    .set('thumb')
    .set('description')
    .set('cityId')
    .set('address')
    .set('sourceId')
    .set('uid')
    .set('publishIp')
    .set('content')
    .set('createTime')
    .set('updatedTime');
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

});
