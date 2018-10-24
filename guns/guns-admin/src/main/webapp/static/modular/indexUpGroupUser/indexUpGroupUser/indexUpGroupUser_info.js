/**
 * 初始化厨师推荐分组管理详情对话框
 */
var IndexUpGroupUserInfoDlg = {
    indexUpGroupUserInfoData : {}
};

/**
 * 清除数据
 */
IndexUpGroupUserInfoDlg.clearData = function() {
    this.indexUpGroupUserInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
IndexUpGroupUserInfoDlg.set = function(key, val) {
    this.indexUpGroupUserInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
IndexUpGroupUserInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
IndexUpGroupUserInfoDlg.close = function() {
    parent.layer.close(window.parent.IndexUpGroupUser.layerIndex);
}

/**
 * 收集数据
 */
IndexUpGroupUserInfoDlg.collectData = function() {
    var userApiIds=[];
    $('#userApis').children().each(function () {
        userApiIds.push($(this).find('input:eq(0)').val())
    });
    this.indexUpGroupUserInfoData['userApiId'] = userApiIds.join(',');
    this
    .set('id')
    .set('cityId');
}

/**
 * 提交添加
 */
IndexUpGroupUserInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/indexUpGroupUser/add", function(data){
        if(data.code==200){
            Feng.success("添加成功!");
        }else{
            Feng.error(data.message);
        }
        window.parent.IndexUpGroupUser.table.refresh();
        IndexUpGroupUserInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.indexUpGroupUserInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
IndexUpGroupUserInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();
    // return false;
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/indexUpGroupUser/update", function(data){
        if(data.code==200){
            Feng.success("修改成功!");
        }else{
            Feng.error(data.message);
        }
        window.parent.IndexUpGroupUser.table.refresh();
        IndexUpGroupUserInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.indexUpGroupUserInfoData);
    ajax.start();
}

$(function() {
    $.post(Feng.ctxPath + "/indexUpGroupUser/cityList", function (data) {
        $("#cityId").append('<option value="0" '+('0'==$('#cityId_').val()?'selected="selected"':'')+'>全国</option>')
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var option=$('<option value="' + jsonObj.id + '" '+(jsonObj.id==$('#cityId_').val()?'selected="selected"':'')+'>' + jsonObj.name + '</option>')
            $("#cityId").append(option);
        }
        $('#cityId').searchableSelect();
    });
    $.post(Feng.ctxPath + "/indexUpGroupUser/userList", function (data) {
        var userApiIds=$('#userApiId_').val().split(",");
        var userDivs=[];
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            if($.inArray(jsonObj.id.toString(),userApiIds)<0){
                var option=$('<option value="' + jsonObj.id + '" avatar="'+(jsonObj.avatar!=undefined?jsonObj.avatar:'')+'" phone="'+(jsonObj.phone!=undefined?jsonObj.phone:'')+'" name="'+(jsonObj.name!=undefined?jsonObj.name:'')+'">' + (jsonObj.name!=undefined?jsonObj.name:'')+(jsonObj.phone!=undefined?('|'+jsonObj.phone):'')+'</option>')
                $("#userApiId").append(option);
            }else{
                userDivs.push($('<div style="float: left;border: 1px solid #acc3ff;border-radius: 10px;overflow: hidden;width: 160px;margin: 0 10px 10px 0;position: relative;height: 71px;">' +
                    '                            <img src="'+(jsonObj.avatar!=undefined?jsonObj.avatar:'')+'" style="float: left;width: 69px;height: 69px;">' +
                    '                            <div style="top: 20px;position: relative;float: left;margin-left: 5px;">' +
                    '                                <p style="margin: 0;white-space: nowrap;">' + (jsonObj.name!=undefined?jsonObj.name:'') + '</p>' +
                    '                                <p style="margin: 0;white-space: nowrap;">' + (jsonObj.phone!=undefined?jsonObj.phone:'') + '</p>' +
                    '                            </div>' +
                    '                            <a onclick="backUser($(this).parent(),\''+jsonObj.id+'\',\''+(jsonObj.avatar!=undefined?jsonObj.avatar:'')+'\',\''+(jsonObj.phone!=undefined?jsonObj.phone:'')+'\',\''+(jsonObj.name!=undefined?jsonObj.name:'')+'\')" style="font-size: 25px;position: absolute;right: 5px;top: -5px;">×</a>' +
                    '                            <input value="' + jsonObj.id + '" type="hidden"/>' +
                    '                        </div>'));
            }
        }
        $('#userApiId').searchableSelect();
        for (var j=0;j<userApiIds.length;j++){
            for (var k=0;k<userDivs.length;k++){
                if($(userDivs[k]).find('input:eq(0)').val()==userApiIds[j]){
                    $('#userApis').append($(userDivs[k]));
                }
            }
        }
        var sortable = new Sortable(document.getElementById("userApis"), {animation: 150});
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