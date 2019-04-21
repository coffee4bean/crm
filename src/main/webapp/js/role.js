
// 查询
function queryRolesByParams() {
    $('#dg').datagrid('load',{
        roleName: $('#roleName').val(),
        createDate: $('#time').datebox('getValue')
    })
}

// 添加
function openAddRoleDailog() {
    openAddOrUpdateDlg('dlg','添加角色')
}
function saveOrUpdateRole() {
    saveOrUpdateData('fm',ctx + '/role/saveOrUpdateRole','dlg',queryRolesByParams)
}

//更新
function openModifyRoleDialog() {
    openModifyDialog('dg','fm','dlg','更新角色')
}

$(function () {
    $('#dlg').dialog({
        onClose:function () {
            console.log('clear...');
            $('#fm').form('clear');// 清空表单
        }
    })
});

//授权
var treeObj;
function openRelationPermissionDialog() {
    var rows = $('#dg').datagrid('getSelections');
    if (rows.length == 0) {
        $.messager.alert('来自Crm', '请选择一条数据进行授权', 'info');
        return;
    }
    if (rows.length > 1) {
        $.messager.alert('来自Crm', '只能选择一条数据进行授权', 'info');
        return;
    }
    /**
     * 初始化树的显示
     * 1 发送ajax请求
     * 2 树 init
     */
    loadTree(rows[0].id);

}
function loadTree(roleId){
    /**
     * 1 设置roleId到隐藏域
     * 2 清空moduleIds隐藏的值
     */
    $('#roleId').val(roleId);
    $('#moduleIds').val('');
    $.ajax({
        url:ctx + '/module/queryAllModulesByRoleId?roleId='+ roleId,
        type: 'post',
        success:function (data) {
            var setting = {
                check: {
                    enable: true,
                    chkboxType: {"Y": "ps", "N": "ps"}
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            }
            var zNodes =data;
            treeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            openAddOrUpdateDlg('permissionDlg','授权');
        }
    });
}
function zTreeOnCheck(event, treeId, treeNode) {
    // console.log(1);
    var nodes = treeObj.getCheckedNodes(true);
    var moduleIds='';//moduleIds=1&moduleIds=2&
    for(var i in nodes){
        moduleIds += 'moduleIds='+nodes[i].id+'&';
    }
    $('#moduleIds').val(moduleIds);
};

// 授权
function doGrant() {
    var roleId = $('#roleId').val();
    var moduleIds = $('#moduleIds').val();
    $.ajax({
        url: ctx + '/role/doGrant?roleId='+roleId+'&'+moduleIds,
        type:'post',
        success:function (data) {
            if(data.code==200){
                $.messager.alert('来自Crm',data.msg,'info',function () {
                    /***
                     * 1. 关闭弹窗
                     * */
                    closeDlgData('permissionDlg')
                })
            }else{
                $.messager.alert('来自Crm',data.msg,'error')
            }
        }
    });
}
//删除角色
function deleteRole(){
    deleteData('dg',ctx + '/role/deleteRoles',queryRolesByParams);
}