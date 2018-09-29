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
    var fj_ids=[];
    $('#fjs').children().eq(1).children().find('#fj_id').each(function () {
        fj_ids.push($(this).val());
    });
    this.userResumeInfoData['fj_ids'] = fj_ids.join(",");
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
        // window.parent.UserResume.table.refresh();
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


    /* 李俊开的简历附件内容,开始 */
    if($('#id').val()!=undefined&&$.trim($('#id').val())!=""){
        $.post(Feng.ctxPath + "/resumeFj/list",{resume_id:$('#id').val()}, function (data) {
            for (var i = 0; i < data.length; i++) {
                var fj=data[i];
                var parentDIV=$('#fjs');
                var Fjname_input_DIV=$(parentDIV).children().eq(0),
                    FjFile_input_DIV=$(parentDIV).children().eq(1);
                $(Fjname_input_DIV).append('<div class="form-group">' +
                    '    <label class="col-sm-3 control-label">简历名称</label>' +
                    '    <div class="col-sm-9">' +
                    '        <input class="form-control" id="fj_name" name="fj_name" value="'+fj.name+'" type="text">' +
                    '    </div>' +
                    '</div>');
                $(FjFile_input_DIV).append('<i  style="display: block;margin-bottom: 15px;">' +
                    '                <input type="file" name="file" id="file" style="display:none;" onChange="fileUp($(this));" />' +
                    '                <input onclick="window.open($(this).val());" title="点击观看附件" id="url" value="'+fj.url+'" readonly style="background-color: #FFFFFF;background-image: none;border: 1px solid #e5e6e7;border-radius: 1px;color: inherit;padding: 6px 12px;font-size: 14px;" />' +
                    '                <input type="button" value="上传附件" onclick="$(this).siblings(\'input:file\').click()" />' +
                    '                <input type="button" value="-" onclick="removeFj($(this))" class="next" />' +
                    '                <input type="hidden" value="'+fj.id+'" id="fj_id" />' +
                    '                <input type="hidden" value="'+fj.object_name+'" id="object_name" />' +
                    '                <input type="hidden" value="" id="old_object_name" />' +
                    '            </i>');
            }
        });
    }
});

function removeFj(obj) {
    var fj_id=$(obj).siblings("#fj_id").val();
    if($.trim(fj_id)!=""){
        $.ajax({
            url:'/resumeFj/delete',
            data:{resumeFjId:fj_id},
            success:function (r) {
                alert(r.message);
            },
            error:function (e) {
                console.log(e)
                alert('删除简历附件失败')
            }
        });
    }
    var parentDIV=$(obj).parent().parent().parent(),
        index=$(obj).parent().parent().children('i').index($(obj).parent());
    $(parentDIV).children().eq(0).children().eq(index).remove();
    $(parentDIV).children().eq(1).children().eq(index).remove();
}
function addFj(obj) {
    var parentDIV=$(obj).parent().next();
    var Fjname_input_DIV=$(parentDIV).children().eq(0),
        FjFile_input_DIV=$(parentDIV).children().eq(1);
    $(Fjname_input_DIV).append('<div class="form-group">' +
        '    <label class="col-sm-3 control-label">简历名称</label>' +
        '    <div class="col-sm-9">' +
        '        <input class="form-control" id="fj_name" name="fj_name" type="text">' +
        '    </div>' +
        '</div>');
    $(FjFile_input_DIV).append('<i  style="display: block;margin-bottom: 15px;">' +
        '                <input type="file" name="file" id="file" style="display:none;" onChange="fileUp($(this));" />' +
        '                <input onclick="window.open($(this).val());" title="点击观看附件" id="url" readonly style="background-color: #FFFFFF;background-image: none;border: 1px solid #e5e6e7;border-radius: 1px;color: inherit;padding: 6px 12px;font-size: 14px;" />' +
        '                <input type="button" value="上传附件" onclick="$(this).siblings(\'input:file\').click()" />' +
        '                <input type="button" value="-" onclick="removeFj($(this))" class="next" />' +
        '                <input type="hidden" value="" id="fj_id" />' +
        '                <input type="hidden" value="" id="object_name" />' +
        '                <input type="hidden" value="" id="old_object_name" />' +
        '            </i>');
}
function fileUp(obj) {
    var parentDIV=$(obj).parent().parent().parent(),
        index=$(obj).parent().parent().children('i').index($(obj).parent());
    var fj_name=$(parentDIV).children().eq(0).children().eq(index).find("#fj_name").val(),
        fj_id=$(parentDIV).children().eq(0).children().eq(index).find("#fj_id").val();
    if(fj_name==undefined||$.trim(fj_name)==""){
        alert("该份简历附件的'简历名称'不能为空");
        $(obj).replaceWith('<input type="file" name="file" id="file" style="display:none;" onChange="fileUp($(this));" />');
        return false;
    }
    // $(parentDIV).children().eq(0).children().eq(index).remove();
    // $(parentDIV).children().eq(1).children().eq(index).remove();
    var formData = new FormData();
    var uploadFile = $(obj).get(0).files[0];
    formData.append("file",uploadFile);
    // console.log(uploadFile);
    if("undefined" != typeof(uploadFile) && uploadFile != null && uploadFile != ""){
        $.ajax({
            url:'/tool/uploadFile',
            type:'POST',
            data:formData,
            async: false,
            cache: false,
            contentType: false, //不设置内容类型
            processData: false, //不处理数据
            success:function(data){
                // console.log(data);
                if($.trim($(obj).siblings('#object_name').val())!="")$(obj).siblings('#old_object_name').val($(obj).siblings('#object_name').val())
                $(obj).siblings('#url').val(data.data);
                $(obj).siblings('#object_name').val(data.object_name);
                var fj_data={}
                if($.trim(fj_id)!="")fj_data.id=fj_id;
                if($.trim($(obj).siblings('#old_object_name').val())!="")fj_data.old_object_name=$.trim($(obj).siblings('#old_object_name').val());
                fj_data.name=fj_name;
                fj_data.url=data.data;
                fj_data.object_name=data.object_name;
                fj_data.source="随便,这个字段就是用来告诉接口该返回新增/修改后的附件实体类,而不是返回状态码200什么的";
                $.ajax({
                    url:'/resumeFj/'+(fj_data.id!=undefined?'update':'add'),
                    data:fj_data,
                    success:function (data) {
                        $(obj).siblings('#fj_id').val(data.id);
                    },
                    error:function (e) {
                        console.log(e);
                        alert("保存简历附件信息失败")
                    }
                });
            },
            error:function(e){
                console.log(e);
                alert("上传失败！");
            }
        })
    }else {
        alert("选择的文件无效！请重新选择");
    }

}
