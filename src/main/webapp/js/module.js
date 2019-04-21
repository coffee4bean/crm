
$(function () {
    // 默认不显示上级菜单下拉
    $('#parentMenu').hide();

    $('#grade02').combobox({
        onChange:function (newValue, oldValue) {
            //console.log(newValue, oldValue);
            if(newValue==0){
                $('#parentMenu').hide();
            }else{
                $('#parentMenu').show();
                // 回显父级下拉框
                $('#parentId02').combobox({
                    url:ctx + '/module/queryModulesByGrade?grade='+(newValue-1),
                    valueField:'id',
                    textField:'moduleName'
                });

            }
        }
    });

});

function queryModulesByParams() {
    $('#dg').datagrid('load',{
        moduleName:$('#moduleName').val(),
        parentId:$('#pid').val(),
        grade:$('#grade').combobox('getValue'),
        optValue:$('#optValue').val()
    });
}

function formateGrade(val) {
    if(val==0){
        return "根菜单";
    }
    if(val==1){
        return "一级菜单";
    }
    if(val==2){
        return "二级菜单";
    }
}

// 添加
function openAddModuleDailog() {
    openAddOrUpdateDlg('dlg','添加模块')
}
function saveOrUpdateModule(){
    saveOrUpdateData('fm',ctx + '/module/saveOrUpdateModule','dlg',queryModulesByParams);
}
//删除
function deleteModule() {
    var rows = $('#dg').datagrid('getSelections');
    if(rows.length==0){
        $.messager.alert('来自Crm','请选择一条数据进行删除','info');
        return;
    }
    $.messager.confirm('来自Crm','确定要删除'+rows.length+'条数据?',function (r) {
        if(r){
           $.ajax({
               url: ctx + '/module/deleteModule?optValue='+rows[0].optValue,
               type:'post',
                success:function (data) {
                    if(data.code==200){
                        $.messager.alert('来自Crm',data.msg,'info',function(){
                            /***
                             * 1 关闭窗口
                             * 2 刷新表格
                             */
                            $('#dlg').dialog('close');
                            $('#dg').datagrid('load');//重新载入当前页面数据
                        })
                    }else{
                        $.messager.alert('来自Crm',data.msg,'error')
                    }
                }
           })
        }

    })
}