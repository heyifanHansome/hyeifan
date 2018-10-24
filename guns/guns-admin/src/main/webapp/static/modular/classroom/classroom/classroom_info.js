/**
 * 初始化星厨课堂详情对话框
 */
var ClassroomInfoDlg = {
    classroomInfoData: {}
};

/**
 * 清除数据
 */
ClassroomInfoDlg.clearData = function () {
    this.classroomInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ClassroomInfoDlg.set = function (key, val) {

    if (key == 'tagId') {
        var heyifanArr = $('#tagId').val();
        var newDemo = "";
        if (heyifanArr != null && heyifanArr.length > 0) {
            for (var z = 0; z < heyifanArr.length; z++) {
                newDemo += heyifanArr[z] + ",";
            }
            this.classroomInfoData[key] = newDemo;
            return this;
        } else {
            this.classroomInfoData[key] = '';
            return this;
        }
    } else {
        this.classroomInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
        return this;
    }


};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ClassroomInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
ClassroomInfoDlg.close = function () {
    parent.layer.close(window.parent.Classroom.layerIndex);
};

/**
 * 收集数据
 */
ClassroomInfoDlg.collectData = function () {
    this.classroomInfoData['content'] = ClassroomInfoDlg.editor.txt.html();
    this
        .set('id')
        .set('columnId')
        .set('title')
        .set('thumb')
        .set('url')
        .set('description')
        .set('startTime')
        .set('cityId')
        .set('sourceId')
        .set('uid')
        .set('publishIp')
        .set('video')
        .set('images')
        .set('tagId')
        .set('coverphoto')
};

/**
 * 提交添加
 */
ClassroomInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/classroom/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Classroom.table.refresh();
        ClassroomInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.classroomInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
ClassroomInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/classroom/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Classroom.table.refresh();
        ClassroomInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.classroomInfoData);
    ajax.start();
}

$(function () {

    var modelType;
    var classRoomArray;

    /**
     * 动态获取所有栏目
     */
    var ajax = new $ax(Feng.ctxPath + "/classroom/getAllColumunType", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            $("#columnId").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();


    /**
     * 动态获取所有城市
     */
    var ajax = new $ax(Feng.ctxPath + "/city/getAllCity", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            $("#cityId").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();

    /**
     * 动态获取所有用户
     */
    var ajax = new $ax(Feng.ctxPath + "/userApi/getAllUserApi", function (data) {
        for (var i = 0; i < data.length; i++) {
            var jsonObj = data[i];
            $("#uid").append('<option value="' + jsonObj.id + '">' + jsonObj.name + '</option>');
        }

    }, function (data) {

    });
    ajax.start();

    // 初始化缩略图上传
    var avatarUp = new $WebUpload("thumb", "/tool/uploadFile");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init("/tool/uploadFile");


    // 初始化缩略图上传
    var avatarUp = new $WebUpload("coverphoto", "/tool/uploadFile");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init("/tool/uploadFile");


    //初始化编辑器

    var E = window.wangEditor;
    var editor = new E('#editor');

    editor.customConfig.uploadImgServer = '/img/wangEditImageUpLoad'  // 上传图片到服务器
    editor.customConfig.uploadImgTimeout = 300000
    editor.customConfig.uploadImgHooks = {
        before: function (xhr, editor, files) {
            // 图片上传之前触发
            // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，files 是选择的图片文件

            // 如果返回的结果是 {prevent: true, msg: 'xxxx'} 则表示用户放弃上传
            // return {
            //     prevent: true,
            //     msg: '放弃上传'
            // }
        },
        success: function (xhr, editor, result) {
        },
        fail: function (xhr, editor, result) {
            // 图片上传并返回结果，但图片插入错误时触发
            // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
            alert("上传图片失败!")
        },
        error: function (xhr, editor) {
            // 图片上传出错时触发
            // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
        },
        timeout: function (xhr, editor) {
            // 图片上传超时时触发
            // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
        },

        // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
        // （但是，服务器端返回的必须是一个 JSON 格式字符串！！！否则会报错）
        customInsert: function (insertImg, result, editor) {
            // 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
            // insertImg 是插入图片的函数，editor 是编辑器对象，result 是服务器端返回的结果

            // 举例：假如上传图片成功后，服务器端返回的是 {url:'....'} 这种格式，即可这样插入图片：
            var url = result.url
            insertImg(url)

            // result 必须是一个 JSON 格式字符串！！！否则报错
        }
    };
    editor.create();
    E.fullscreen.init('#editor');
    editor.txt.html($("#content").val());
    ClassroomInfoDlg.editor = editor;
});
