$(function () {
    $('#dlg').dialog({
        onClose:function () {
            console.log('clear...');
            $('#fm').form('clear');// 清空表单
        }
    })
});
function queryUsersByParams() {
    $('#dg').datagrid('load',{
        userName:$('#userName').val(),
        email:$('#email').val(),
        phone:$('#phone').val()
    })
}
function openAddUserDailog(){
    openAddOrUpdateDlg('dlg','添加用户');
}
//添加用户
function saveOrUpdateUser(){
    saveOrUpdateData('fm',ctx + '/user/saveOrUpdateUser','dlg',queryUsersByParams);
}
//更新用户
function openModifyUserDialog(){
    openModifyDialog('dg','fm','dlg','更新用户');
}
//删除用户
function deleteUser(){
    deleteData('dg',ctx + '/user/deleteUsers',queryUsersByParams);
}