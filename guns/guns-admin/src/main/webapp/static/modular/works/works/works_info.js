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

    if(key == 'tagId'){
            var heyifanArr = $('#tagId').val();
            var newDemo ="" ;
            if(heyifanArr!=null&&heyifanArr.length>0){
                for (var z=0;z< heyifanArr.length;z++) {
                    newDemo += heyifanArr[z] +",";
                }
                this.worksInfoData[key] = newDemo;
                return this;
            }else {
                this.worksInfoData[key] = '';
                return this;
            }
    }

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

};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WorksInfoDlg.get = function(key) {
    return $("#" + key).val();
};

/**
 * 定义删除视频方法
 */

WorksInfoDlg.deleteVideo=function(v){

    var ajax = new $ax(Feng.ctxPath + "/works/deleteVideoByObjectName?objectName=" + v, function(data){
        Feng.success("删除成功! 请重新打开该页面查看效果!");

    },function(data){
        Feng.success("删除成功! 请重新打开该页面查看效果!");
    });
    ajax.set(this.worksInfoData);
    ajax.start();


};


/**
 * 定义审核方法
 */

WorksInfoDlg.checkVideo=function(v){
    debugger;
    var ajax = new $ax(Feng.ctxPath + "/works/checkVideo?objectName=" + v, function(data){
        Feng.success("审核通过! 请重新打开该页面查看效果!");

    },function(data){
        Feng.success("审核通过! 请重新打开该页面查看效果!");
    });
    ajax.set(this.worksInfoData);
    ajax.start();


};
/**
 * 关闭此对话框
 */
WorksInfoDlg.close = function() {
    parent.layer.close(window.parent.Works.layerIndex);
};

/**
 * 收集数据
 */
WorksInfoDlg.collectData = function() {
    this.worksInfoData['practice'] = WorksInfoDlg.editor.txt.html();
    this
    .set('id')
    .set('name')
    .set('type')
    .set('images')
    .set('mainIngredient','oneleftbuttons', 'onerightbuttons','main_ingredient')
    .set('supplementaryMaterial','twoleftbuttons','tworightbuttons','supplementary_material')
    .set('seasoning','threeleftbuttons','threerightbuttons','seasoning')
    .set('remark')
    .set('status')
    // .set('columnId')
    .set('baseId')
    .set('userId')
    .set('video')
    .set('tagId');
};

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
};

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
};

$(function() {

    var currentVideoName ;
    var threeArr;
    var twoArr;
    var oneArr;
    var onerightbuttons ;
    var oneleftbuttons;
    var tworightbuttons ;
    var twoleftbuttons ;
    var threerightbuttons ;
    var threeleftbuttons ;
    var videoHttpsArrays;
    var hyfVideoFile;
    var multArrays=[];
    /**
     * 动态获取所有用户
     */
    var ajax = new $ax(Feng.ctxPath + "/mgr/getAllUser", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            $("#userId").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();


    /**
     * 动态获取所有栏目
     */
    var ajax = new $ax(Feng.ctxPath + "/works/getAllColumnType", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            $("#columnId").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();

    // $(document).ready(function() {


    //初始化编辑器
    var E = window.wangEditor;
    var editor = new E('#editor');
    editor.create();
    editor.txt.html($("#practice").val());
    WorksInfoDlg.editor = editor;
});
