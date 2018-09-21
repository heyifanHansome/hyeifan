/**
 * 初始化城市分类详情对话框
 */
var CityTypeInfoDlg = {
    cityTypeInfoData : {}
};

/**
 * 清除数据
 */
CityTypeInfoDlg.clearData = function() {
    this.cityTypeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CityTypeInfoDlg.set = function(key, val) {
    this.cityTypeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CityTypeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CityTypeInfoDlg.close = function() {
    parent.layer.close(window.parent.CityType.layerIndex);
}

/**
 * 收集数据
 */
CityTypeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('createTime')
    .set('updatedTime');
}

/**
 * 提交添加
 */
CityTypeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/cityType/add", function(data){
        Feng.success("添加成功!");
        window.parent.CityType.table.refresh();
        CityTypeInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.cityTypeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
CityTypeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/cityType/update", function(data){
        Feng.success("修改成功!");
        window.parent.CityType.table.refresh();
        CityTypeInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.cityTypeInfoData);
    ajax.start();
}

$(function() {

});
