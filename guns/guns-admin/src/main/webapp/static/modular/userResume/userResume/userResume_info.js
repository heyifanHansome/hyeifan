/**
 * 初始化用户简历管理详情对话框
 */
var UserResumeInfoDlg = {
    userResumeInfoData : {}
};

/**
 * 清除数据
 */
UserResumeInfoDlg.clearData = function() {
    this.userResumeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserResumeInfoDlg.set = function(key, val) {
    this.userResumeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserResumeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserResumeInfoDlg.close = function() {
    parent.layer.close(window.parent.UserResume.layerIndex);
}

/**
 * 收集数据
 */
UserResumeInfoDlg.collectData = function() {
    this.userResumeInfoData['info'] = UserResumeInfoDlg.editor.txt.html();
    this
    .set('id')
    .set('userId')
    .set('name')
    .set('applyStatus')
    .set('sex')
    .set('birthday')
    .set('workTime')
    .set('wechatId')
    .set('phone')
    .set('email')
    .set('advantage')
    .set('createTime')
}

/**
 * 提交添加
 */
UserResumeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userResume/add", function(data){
        Feng.success("添加成功!");
        window.parent.UserResume.table.refresh();
        UserResumeInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userResumeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
UserResumeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userResume/update", function(data){
        Feng.success("修改成功!");
        window.parent.UserResume.table.refresh();
        UserResumeInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userResumeInfoData);
    ajax.start();
}

$(function() {

    /**
     * 动态获取所有用户
     */
    var ajax = new $ax(Feng.ctxPath + "/mgr/getAllUser", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var optionstring = "";
            console.log(jsonObj)
            $("#userId").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();



    //初始化编辑器
    var E = window.wangEditor;
    var editor = new E('#editor');
    editor.create();
    editor.txt.html($("#info").val());
    UserResumeInfoDlg.editor = editor;
});
