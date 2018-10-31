/**
 * 初始化搜索关键字管理详情对话框
 */
var SearchKeywordInfoDlg = {
    searchKeywordInfoData : {}
};

/**
 * 清除数据
 */
SearchKeywordInfoDlg.clearData = function() {
    this.searchKeywordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SearchKeywordInfoDlg.set = function(key, val) {
    this.searchKeywordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SearchKeywordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SearchKeywordInfoDlg.close = function() {
    parent.layer.close(window.parent.SearchKeyword.layerIndex);
}

/**
 * 收集数据
 */
SearchKeywordInfoDlg.collectData = function() {
    this
    .set('id')
    .set('content')
    .set('hitNum')
    .set('orders');
    if($.trim(this.searchKeywordInfoData.orders).length==0)this.searchKeywordInfoData.orders=0;
    if($.trim(this.searchKeywordInfoData.hitNum).length==0)this.searchKeywordInfoData.hitNum=0;
}

/**
 * 提交添加
 */
SearchKeywordInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/searchKeyword/add", function(data){
        Feng.success("添加成功!");
        window.parent.SearchKeyword.table.refresh();
        SearchKeywordInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.searchKeywordInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SearchKeywordInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/searchKeyword/update", function(data){
        Feng.success("修改成功!");
        window.parent.SearchKeyword.table.refresh();
        SearchKeywordInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.searchKeywordInfoData);
    ajax.start();
}

$(function() {

});
