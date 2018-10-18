/**
 * 初始化活动报名管理详情对话框
 */
var ActivityApplyInfoDlg = {
    activityApplyInfoData : {}
};

/**
 * 清除数据
 */
ActivityApplyInfoDlg.clearData = function() {
    this.activityApplyInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ActivityApplyInfoDlg.set = function(key, val) {
    this.activityApplyInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ActivityApplyInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ActivityApplyInfoDlg.close = function() {
    parent.layer.close(window.parent.ActivityApply.layerIndex);
}

/**
 * 收集数据
 */
ActivityApplyInfoDlg.collectData = function() {
    var userApiIds=[];
    $('#userApis').children().each(function () {
        userApiIds.push($(this).find('input:eq(0)').val())
    });
    this.activityApplyInfoData['userApiId'] = userApiIds.join(',');
    this
    .set('id')
    .set('activityId');
}

/**
 * 提交添加
 */
ActivityApplyInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/activityApply/add", function(data){
        Feng.success("添加成功!");
        window.parent.ActivityApply.table.refresh();
        ActivityApplyInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.activityApplyInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ActivityApplyInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/activityApply/update", function(data){
        Feng.success("修改成功!");
        window.parent.ActivityApply.table.refresh();
        ActivityApplyInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.activityApplyInfoData);
    ajax.start();
}

$(function() {
    $.post(Feng.ctxPath + "/activityApply/activityList", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var option=$('<option value="' + jsonObj.id + '" '+(jsonObj.id==$('#activityId_').val()?'selected="selected"':'')+'>' + jsonObj.title + '</option>')
            $("#activityId").append(option);
        }
        $('#activityId').searchableSelect();
    });
    $.post(Feng.ctxPath + "/activityApply/userList", function (data) {
        var userApiIds=$('#userApiId_').val().split(",");
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            if($.inArray(jsonObj.id.toString(),userApiIds)<0){
                var option=$('<option value="' + jsonObj.id + '" avatar="'+(jsonObj.avatar!=undefined?jsonObj.avatar:'')+'" phone="'+(jsonObj.phone!=undefined?jsonObj.phone:'')+'" name="'+(jsonObj.name!=undefined?jsonObj.name:'')+'">' + (jsonObj.name!=undefined?jsonObj.name:'')+(jsonObj.phone!=undefined?('|'+jsonObj.phone):'')+'</option>')
                $("#userApiId").append(option);
            }else{
                $('#userApis').append('<div style="float: left;border: 1px solid #acc3ff;border-radius: 10px;overflow: hidden;width: 160px;margin: 0 10px 10px 0;position: relative;height: 71px;">' +
                    '                            <img src="'+(jsonObj.avatar!=undefined?jsonObj.avatar:'')+'" style="float: left;width: 69px;height: 69px;">' +
                    '                            <div style="top: 20px;position: relative;float: left;margin-left: 5px;">' +
                    '                                <p style="margin: 0;white-space: nowrap;">' + (jsonObj.name!=undefined?jsonObj.name:'') + '</p>' +
                    '                                <p style="margin: 0;white-space: nowrap;">' + (jsonObj.phone!=undefined?jsonObj.phone:'') + '</p>' +
                    '                            </div>' +
                    '                            <a onclick="backUser($(this).parent(),\''+jsonObj.id+'\',\''+(jsonObj.avatar!=undefined?jsonObj.avatar:'')+'\',\''+(jsonObj.phone!=undefined?jsonObj.phone:'')+'\',\''+(jsonObj.name!=undefined?jsonObj.name:'')+'\')" style="font-size: 25px;position: absolute;right: 5px;top: -5px;">×</a>' +
                    '                            <input value="' + jsonObj.id + '" type="hidden"/>' +
                    '                        </div>');
            }
        }
        $('#userApiId').searchableSelect();
    });
});
function backUser(htmlObj,id,avatar,phone,name) {
    $('#userApiId').append('<option value="' + id + '" avatar="'+avatar+'" phone="'+phone+'" name="'+name+'">' + name + '|' + phone + '</option>')
    $('#userApiId').next('div').remove();
    $('#userApiId').searchableSelect();
    $(htmlObj).remove();
}
function addUser(id,avatar,phone,name) {
    if($('#userApiId').children().length<1)return false;
    $("#userApiId option[value='"+id+"']").remove();
    $('#userApiId').next('div').remove();
    $('#userApiId').searchableSelect();
    $('#userApis').append('<div style="float: left;border: 1px solid #acc3ff;border-radius: 10px;overflow: hidden;width: 160px;margin: 0 10px 10px 0;position: relative;height: 71px;">' +
        '                            <img src="'+avatar+'" style="float: left;width: 69px;height: 69px;">' +
        '                            <div style="top: 20px;position: relative;float: left;margin-left: 5px;">' +
        '                                <p style="margin: 0;white-space: nowrap;">' + name + '</p>' +
        '                                <p style="margin: 0;white-space: nowrap;">' + phone + '</p>' +
        '                            </div>' +
        '                            <a onclick="backUser($(this).parent(),\''+id+'\',\''+avatar+'\',\''+phone+'\',\''+name+'\')" style="font-size: 25px;position: absolute;right: 5px;top: -5px;">×</a>' +
        '                            <input value="' + id + '" type="hidden"/>' +
        '                        </div>');
}