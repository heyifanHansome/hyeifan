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
        .set('lngx')
        .set('laty')
}

/**
 * 提交添加
 */
CityInfoDlg.addSubmit = function() {
    this.clearData();
    this.collectData();
    var goon=false,message=""
    //开始定位
    $.ajax({
        url:Feng.ctxPath + "/city/dingwei",
        async:false,
        data:{cityName:this.cityInfoData.name},
        beforeSend: function(){
            Feng.success("定位获取坐标中...");
        },
        success:function (r) {
            if(r.success=="ok"){
                message=r.message;
                goon=true;
                $('#lngx').val(r.data.geocodes[0].location.split(",")[0]);
                $('#laty').val(r.data.geocodes[0].location.split(",")[1]);
            }else{
                message=r.message;
            }
        },
        error:function (e) {
            console.log(e);
            message="服务器出现异常,城市定位失败!";
        }
    });
    if(goon){
        Feng.success(message);
    }else{
        Feng.error(message);return goon;
    }
    //填写完坐标再次收集数据
    this.clearData();
    this.collectData();
    if($.trim($('#lngx').val()).length==0||$.trim($('#laty').val()).length==0){Feng.error("未能获取坐标信息");return false;}
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
    var goon=false,message=""
    //开始定位
    $.ajax({
        url:Feng.ctxPath + "/city/dingwei",
        async:false,
        data:{cityName:this.cityInfoData.name},
        beforeSend: function(){
            Feng.success("定位获取坐标中...");
        },
        success:function (r) {
            if(r.success=="ok"){
                message=r.message;
                goon=true;
                $('#lngx').val(r.data.geocodes[0].location.split(",")[0]);
                $('#laty').val(r.data.geocodes[0].location.split(",")[1]);
            }else{
                message=r.message;
            }
        },
        error:function (e) {
            console.log(e);
            message="服务器出现异常,城市定位失败!";
        }
    });
    if(goon){
        Feng.success(message);
    }else{
        Feng.error(message);return goon;
    }
    //填写完坐标再次收集数据
    this.clearData();
    this.collectData();
    if($.trim($('#lngx').val()).length==0||$.trim($('#laty').val()).length==0){Feng.error("未能获取坐标信息");return false;}

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
    /**
     * 动态获取城市类型
     */
    var ajax = new $ax(Feng.ctxPath + "/cityType/getAllCityType", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            var optionstring = "";
            console.log(jsonObj)
            $("#typeId").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();
});
