/**
 * 初始化收徒详情详情对话框
 */
var RuleInfoDlg = {
    ruleInfoData : {}
};

/**
 * 清除数据
 */
RuleInfoDlg.clearData = function() {
    this.ruleInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RuleInfoDlg.set = function(key, val) {
    this.ruleInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RuleInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
RuleInfoDlg.close = function() {
    parent.layer.close(window.parent.Rule.layerIndex);
}

/**
 * 收集数据
 */
RuleInfoDlg.collectData = function() {
    this.ruleInfoData['rule'] = RuleInfoDlg.editor.txt.html();
    this.ruleInfoData['content'] = RuleInfoDlg.heyifanEdit.txt.html();
    this
    .set('id')
}

/**
 * 提交添加
 */
RuleInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/rule/add", function(data){
        Feng.success("添加成功!");
        window.parent.Rule.table.refresh();
        RuleInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ruleInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
RuleInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/rule/update", function(data){
        Feng.success("修改成功!");
        window.parent.Rule.table.refresh();
        RuleInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.ruleInfoData);
    ajax.start();
}

$(function() {



    //初始化图文编辑器

    var E = window.wangEditor;
    var editor = new E('#editor');
    editor.customConfig.uploadImgServer = '/img/wangEditImageUpLoad'  // 上传图片到服务器
    editor.customConfig.uploadImgTimeout = 300000
    editor.customConfig.uploadImgHooks = {
        before: function (xhr, editor, files) {
        },
        success: function (xhr, editor, result) {
        },
        fail: function (xhr, editor, result) {
            alert("上传图片失败!")
        },
        error: function (xhr, editor) {
        },
        timeout: function (xhr, editor) {
        },
        customInsert: function (insertImg, result, editor) {
            var url = result.url
            insertImg(url)
        }
    };
    editor.create();
    E.fullscreen.init('#editor');
    editor.txt.html($("#content").val());
    RuleInfoDlg.editor = editor;




    //初始化作者详情编辑器
    var userEdit = window.wangEditor;
    var heyifanEditor = new userEdit('#userEdit');
    heyifanEditor.customConfig.uploadImgServer = '/img/wangEditImageUpLoad';  // 上传图片到服务器
    heyifanEditor.customConfig.uploadImgTimeout = 300000;
    heyifanEditor.customConfig.uploadImgHooks = {
        before: function (xhr, editor, files) {
        },
        success: function (xhr, editor, result) {
        },
        fail: function (xhr, editor, result) {
            alert("上传图片失败!")
        },
        error: function (xhr, editor) {
        },
        timeout: function (xhr, editor) {
        },
        customInsert: function (insertImg, result, editor) {
            var url = result.url
            insertImg(url)
        }
    };
    heyifanEditor.create();
    userEdit.fullscreen.init('#userEdit');
    heyifanEditor.txt.html($("#userDescription").val());
    RuleInfoDlg.heyifanEdit = heyifanEditor;


});
