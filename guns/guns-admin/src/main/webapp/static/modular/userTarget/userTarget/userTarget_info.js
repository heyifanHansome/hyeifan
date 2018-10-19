/**
 * 初始化会员等级审核详情对话框
 */
var UserTargetInfoDlg = {
    userTargetInfoData : {}
};

/**
 * 清除数据
 */
UserTargetInfoDlg.clearData = function() {
    this.userTargetInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserTargetInfoDlg.set = function(key, val) {
    this.userTargetInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserTargetInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserTargetInfoDlg.close = function() {
    parent.layer.close(window.parent.UserTarget.layerIndex);
}

/**
 * 收集数据
 */
UserTargetInfoDlg.collectData = function() {
var target=[];
$('#target').children().each(function () {
    var targetOneHtmlObj=this;
    var targetOne={};
    targetOne.id=$(targetOneHtmlObj).children('#oneId').eq(0).val();
    targetOne.name=$.trim($(targetOneHtmlObj).children('#name').eq(0).text());
    targetOne.targets=[];
    $(targetOneHtmlObj).children('div').children().each(function () {
       var targetTwoHtmlObj=this;
       var targetTwo={};
       targetTwo.id=$(targetTwoHtmlObj).children('#twoId').eq(0).val();
       targetTwo.name=$.trim($(targetTwoHtmlObj).children('span').eq(0).text());
       targetTwo.targets=[];
       var targetThree={};
       targetThree.score=$(targetTwoHtmlObj).children('select').eq(0).val();
       if(targetThree.score>0){
           targetThree.grade=$(targetTwoHtmlObj).children('select').find("option:selected").attr('grade');
           targetThree.id=$(targetTwoHtmlObj).children('select').find("option:selected").attr('id');
           targetThree.pid=$(targetTwoHtmlObj).children('select').find("option:selected").attr('pid');
           targetThree.name=$.trim($(targetTwoHtmlObj).children('select').find("option:selected").text());
           targetTwo.targets.push(targetThree);
       }
       if(targetTwo.targets.length>0)targetOne.targets.push(targetTwo);
    });
    if(targetOne.targets.length>0)target.push(targetOne);
});
    this.userTargetInfoData['target']=JSON.stringify(target);
    var replenish=[];
    $('#replenish').children(":not(:eq(0))").each(function (i,item) {
        var replenishObj={};
        replenishObj.data=[];
        $(item).children().eq(1).children().eq(1).children().each(function (i1,fileObj) {
            var imgObj={};
            imgObj.object_name=$(fileObj).find('#object_name').val();
            imgObj.url=$(fileObj).find('img').length>0?$(fileObj).find('img').attr('src'):$(fileObj).find('a').attr('href');
            replenishObj.data.push(imgObj);
        });
        if(replenishObj.data.length>0){
            replenishObj.title=$(item).children().eq(0).find('#title').val();
            replenish.push(replenishObj);
        }
    });
    this.userTargetInfoData['replenish']=JSON.stringify(replenish);
    this
    .set('id')
    .set('lv')
    .set('check')
}

/**
 * 提交添加
 */
UserTargetInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userTarget/add", function(data){
        Feng.success("添加成功!");
        window.parent.UserTarget.table.refresh();
        UserTargetInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userTargetInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
UserTargetInfoDlg.editSubmit = function() {
    this.clearData();
    this.collectData();
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userTarget/update", function(data){
        Feng.success("修改成功!");
        window.parent.UserTarget.table.refresh();
        UserTargetInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userTargetInfoData);
    ajax.start();
}

$(function() {

});
