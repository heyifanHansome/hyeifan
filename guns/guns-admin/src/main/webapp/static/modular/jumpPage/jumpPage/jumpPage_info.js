/**
 * 初始化首页跳转模块详情对话框
 */
var JumpPageInfoDlg = {
    jumpPageInfoData : {}
};

/**
 * 清除数据
 */
JumpPageInfoDlg.clearData = function() {
    this.jumpPageInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JumpPageInfoDlg.set = function(key, val) {
    this.jumpPageInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JumpPageInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
JumpPageInfoDlg.close = function() {
    parent.layer.close(window.parent.JumpPage.layerIndex);
}

/**
 * 收集数据
 */
JumpPageInfoDlg.collectData = function() {
    this
    .set('id')
    .set('picture')
        .set('object_name')
        .set('old_object_name')
        .set('enable')
        .set('orders')
    .set('code');
}

/**
 * 提交添加
 */
JumpPageInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jumpPage/add", function(data){
        if(data.code==200){
            Feng.success("添加成功!");
        }else{
            Feng.error(data.message);
        }
        window.parent.JumpPage.table.refresh();
        JumpPageInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jumpPageInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
JumpPageInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jumpPage/update", function(data){
        if(data.code==200){
            Feng.success("修改成功!");
        }else{
            Feng.error(data.message);
        }
        window.parent.JumpPage.table.refresh();
        JumpPageInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jumpPageInfoData);
    ajax.start();
}

$(function() {
// 初始化缩略图上传
    var fileServerPathUp = new $WebUpload("picture","/tool/uploadFile");
    fileServerPathUp.setUploadBarId("progressBar");
    fileServerPathUp.init("/tool/uploadFile");
});
