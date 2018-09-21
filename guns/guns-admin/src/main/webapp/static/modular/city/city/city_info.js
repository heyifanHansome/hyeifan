/**
 * 初始化城市管理详情对话框
 */
var CityInfoDlg = {
    cityInfoData : {}
};

/**
 * 清除数据
 */
CityInfoDlg.clearData = function() {
    this.cityInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CityInfoDlg.set = function(key, val) {
    this.cityInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CityInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CityInfoDlg.close = function() {
    parent.layer.close(window.parent.City.layerIndex);
}

/**
 * 收集数据
 */
CityInfoDlg.collectData = function() {
    this
    .set('id')
    .set('typeId')
    .set('name')
}

/**
 * 提交添加
 */
CityInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/city/add", function(data){
        Feng.success("添加成功!");
        window.parent.City.table.refresh();
        CityInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.cityInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
CityInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/city/update", function(data){
        Feng.success("修改成功!");
        window.parent.City.table.refresh();
        CityInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.cityInfoData);
    ajax.start();
}

$(function() {

});
