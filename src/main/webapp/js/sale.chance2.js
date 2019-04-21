/**
 *
 * @param value 当前值
 * @param row 当前行的值
 * @param index 当前索引
 */
function formatState(value,row,index) {
    if(value==0){
        return "未分配";
    }
    if(value==1){
        return "已分配";
    }
}

function formatDevResult(val) {
    if(val==0){
        return "未开发";
    }
    if(val==1){
        return "开发中";
    }
    if(val==2){
        return "开发成功";
    }
    if(val==3){
        return "开发失败";
    }
}
// 查询
function querySaleChancesByParams() {
    $('#dg').datagrid('load', {
        customerName: $('#customerName').val(),
        state: $('#state').combobox('getValue'),
        devResult: $('#devResult').combobox('getValue'),
        createDate: $('#time').datebox('getValue')
    });
}
$(function () {
    //在页面加载完成后,触发
    $('#dg').datagrid({
        rowStyler: function(index,row){
            var devResult = row.devResult;
            if (devResult == 0) {
                return "background-color:#5bc0de;"; // 蓝色
            } else if (devResult == 1) {
                return "background-color:#f0ad4e;"; // 黄色
            } else if (devResult == 2) {
                return "background-color:#5cb85c;"; // 绿色
            } else if (devResult == 3) {
                return "background-color:#d9534f;"; // 红色
            }
        }
    });
    $('#dlg').dialog({
        onClose:function(){
            console.log('clear....');
            $('#fm').form('clear');//清空列表
        }
    })
});
/*
//打开添加弹窗
function openAddSaleChanceDialog(){
    $('#dlg').dialog('open');
}
//添加和更新弹窗
function saveOrUpdateSaleChance() {
    $('#fm').form('submit',{
        url:ctx+"/saleChance/saveOrUpdateSaleChance",
            onSubmit: function () {
            return $(this).form('validate');	// 返回false终止表单提交
        },
        success:function(data){
            data = JSON.parse(data);
            if(data.code==200){
                $.messager.alert('来自Crm',data.msg,'info',function(){
                    /!**
                     * 1 关闭弹窗
                     * 2 属新表格
                     *!/
                    $('#dlg').dialog('close');
                    $('#dg').datagrid('load');//重新载入当前页面数据
                })
            }else{
                $.messager.alert('来自Crm',data.msg,'error');
            }
        }
    });
}
function openModifySaleChanceDialog () {
    /!**
     * 判断是否勾选以及多个勾选
     *!/
    var rows =$('#dg').datagrid('getSelections');
    if(rows.length==0){
        $.messager.alert('来自Crm','请选择一条数据进行更新','info');
        return;
    }
    if(rows.length>1){
        $.messager.alert('来自Crm','请选择一条数据进行更新','info');
        return;
    }
    /!**
     * 回显数据
     *!/
    $('#fm').form('load',rows[0]);
    $('#dlg').dialog('open').dialog('setTitle','更新营销机会');
}
//删除
function deleteSaleChance () {
    var rows = $('#dg').datagrid('getSelections');
    if (row.length == 0) {
        $.messager.alert('来自Crm', '请选择一条数据进行删除', 'info');
        return;
    }
    $.messager.confirm('来自Crm', '确定要删除' + rows.length + '条数据?', function (r) {
        if (r) {
            var ids = '';
            for (var i = 0; i < rows.length; i++) {
                ids += 'ids=' + rows[i].id + '&';
            }
            $.ajax({
                url: ctx + '/saleChance/delSaleChances?' + ids,
                type: 'post',
                sucess: function (data) {
                    console.log(data);
                    if (data.code == 200) {
                        $.messager.alert('来自Crm', data.msg, 'info', function () {
                            /!**
                             * 1 关闭弹窗
                             * 2 刷新表格
                             *!/
                            $('#dlg').dialog('close');
                            $('#dg').datagrid('load');
                        })
                    } else {
                        $.messager.alert('来自Crm', data.msg, 'error')
                    }
                }
            });
        }
    });
}*/
//打开添加弹窗
function openAddSaleChanceDialog(){
    openAddOrUpdateDlg('dlg','添加营销机会');
}
function saveOrUpdateSaleChance(){
    saveOrUpdateData('fm',ctx+'/saleChance/saveOrUpdateSaleChance',querySaleChancesByParams);
}
function openModifySaleChanceDialog(){
  openModifyDialog('dg','fm','dlg','更新营销机会');
}
function deleteSaleChance(){
    deleteData('dg',ctx+'/saleChance/delSaleChances',querySaleChancesByParams);
}
