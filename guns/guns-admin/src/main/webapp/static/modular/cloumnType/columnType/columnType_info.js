/**
 * 初始化栏目分类详情对话框
 */
var ColumnTypeInfoDlg = {
    columnTypeInfoData : {}
};

/**
 * 清除数据
 */
ColumnTypeInfoDlg.clearData = function() {
    this.columnTypeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ColumnTypeInfoDlg.set = function(key, val) {
    this.columnTypeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ColumnTypeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ColumnTypeInfoDlg.close = function() {
    parent.layer.close(window.parent.ColumnType.layerIndex);
}

/**
 * 收集数据
 */
ColumnTypeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('parentId')
    .set('orders')
    .set('name')
    // .set('createTime')
    // .set('updateTime');
}

/**
 * 提交添加
 */
ColumnTypeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/columnType/add", function(data){
        Feng.success("添加成功!");
        window.parent.ColumnType.table.refresh();
        ColumnTypeInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.columnTypeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ColumnTypeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/columnType/update", function(data){
        Feng.success("修改成功!");
        window.parent.ColumnType.table.refresh();
        ColumnTypeInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.columnTypeInfoData);
    ajax.start();
}

$(function() {
    $("#parentId").append('<option value="0">顶级栏目</option>');
    $.post(Feng.ctxPath + "/columnType/getColumnTypeList",{id:$('#id').val()}, function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var optionstring = "";
            var option=$('<option value="' + jsonObj.id + '" '+(jsonObj.id==$('#parentId_').val()?'selected="selected"':'')+'>' + jsonObj.name + '</option>')
            $("#parentId").append(option);
        }
    });
});
