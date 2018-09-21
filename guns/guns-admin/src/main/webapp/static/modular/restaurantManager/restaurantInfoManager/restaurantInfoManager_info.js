/**
 * 初始化餐厅信息管理详情对话框
 */
var RestaurantInfoManagerInfoDlg = {
    restaurantInfoManagerInfoData : {}
};

/**
 * 清除数据
 */
RestaurantInfoManagerInfoDlg.clearData = function() {
    this.restaurantInfoManagerInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RestaurantInfoManagerInfoDlg.set = function(key, val) {
    this.restaurantInfoManagerInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RestaurantInfoManagerInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
RestaurantInfoManagerInfoDlg.close = function() {
    parent.layer.close(window.parent.RestaurantInfoManager.layerIndex);
}

/**
 * 收集数据
 */
RestaurantInfoManagerInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('restaurant')
    .set('thumb')
    .set('images')
    .set('cityId')
    .set('address')
    .set('longitude')
    .set('latitude')
    .set('businessHours')
    .set('status')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
RestaurantInfoManagerInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/restaurantInfoManager/add", function(data){
        Feng.success("添加成功!");
        window.parent.RestaurantInfoManager.table.refresh();
        RestaurantInfoManagerInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.restaurantInfoManagerInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
RestaurantInfoManagerInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/restaurantInfoManager/update", function(data){
        Feng.success("修改成功!");
        window.parent.RestaurantInfoManager.table.refresh();
        RestaurantInfoManagerInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.restaurantInfoManagerInfoData);
    ajax.start();
}

$(function() {

});
