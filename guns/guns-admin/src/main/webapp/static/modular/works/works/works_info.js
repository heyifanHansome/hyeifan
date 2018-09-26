/**
 * 初始化作品管理详情对话框
 */
var WorksInfoDlg = {
    worksInfoData : {}
};

/**
 * 清除数据
 */
WorksInfoDlg.clearData = function() {
    this.worksInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WorksInfoDlg.set = function(key, val,val2, isClass ) {
    debugger;
    if(isClass !== undefined) {
        var rightinputs =$("#"+val2+" ."+isClass);
        var leftinput =$("#"+val+" ."+isClass);
     var arr =[];
        console.log(rightinputs)
        for (var i=0;i< rightinputs.length;i++){
            var map={
                key:$(leftinput[i]).val(),
                value:$(rightinputs[i]).val()
            };
            arr.push(map);
        }
        this.worksInfoData[key] = JSON.stringify(arr).toString();
        return this;
    }else{
        this.worksInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
        return this;
    }

}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WorksInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
WorksInfoDlg.close = function() {
    parent.layer.close(window.parent.Works.layerIndex);
}

/**
 * 收集数据
 */
WorksInfoDlg.collectData = function() {
    console.log(this)
    this
    .set('id')
    .set('name')
    .set('type')
    .set('images')
    .set('mainIngredient','oneleftbuttons', 'onerightbuttons','main_ingredient')
    .set('supplementaryMaterial','twoleftbuttons','tworightbuttons','supplementary_material')
    .set('seasoning','threeleftbuttons','threerightbuttons','seasoning')
    .set('practice')
    .set('remark')
    .set('status')
    .set('columnId');
}

/**
 * 提交添加
 */
WorksInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/works/add", function(data){
        Feng.success("添加成功!");
        window.parent.Works.table.refresh();
        WorksInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.worksInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
WorksInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/works/update", function(data){
        Feng.success("修改成功!");
        window.parent.Works.table.refresh();
        WorksInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.worksInfoData);
    ajax.start();
}

$(function() {
    var threeArr;
    var twoArr;
    var oneArr;
    var onerightbuttons ;
    var oneleftbuttons;
    var tworightbuttons ;
    var twoleftbuttons ;
    var threerightbuttons ;
    var threeleftbuttons ;
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

});
