
$(function () {
    //执行表格初始化
    var sid = $('#saleChanceId').val();
    $('#dg').edatagrid({
        url: ctx + '/cusDevPlan/queryCusDevPlansByParams?sid='+sid,// 查询地址
        saveUrl: ctx + '/cusDevPlan/saveOrUpdateCusDevPlan?sid='+sid,// 保存地址
        updateUrl: ctx + '/cusDevPlan/saveOrUpdateCusDevPlan?sid='+sid// 更新地址
        // destroyUrl: ""//删除地址
    });
});

function addRow() {
    $('#dg').edatagrid('addRow');
}
function saveOrUpdateCusDevPlan() {
    $('#dg').edatagrid('saveRow');
    // setTimeout(function () {
    //     $('#dg').edatagrid('reload');//加载数据
    // },3);
    $('#dg').edatagrid('reload');//加载数据
}
function delCusDevPlan(){
    var rows = $('#dg').datagrid('getSelections');
    if(rows.length==0){
        $.messager.alert('来自Crm','请选择一条数据进行删除','info')
        return;
    }
    $.messager.confirm('来自Crm','确定要删除'+rows.length+'条数据?',function (r) {
        if(r){
            var ids='';
            for(var i =0 ;i<rows.length;i++){
                ids+='ids='+rows[i].id+'&';
            }
            $.ajax({
                url:ctx + '/cusDevPlan/delCusDevPlans?'+ids,
                type:'post',
                success:function (data) {
                    if(data.code==200){
                    $.messager.alert('来自Crm',data.msg,'info',function () {
                        /****
                         * 1. 刷新表格
                         * */
                        $('#dg').edatagrid('load');    // 重新载入当前页面数据
                    })
                    }else{
                        $.messager.alert('来自Crm',data.msg,'error');
                    }
                }
            });
        }
    });
}
//修改开发状态
function updateSaleChanceDevResult(devResult){
    var sid = $('#saleChanceId').val();
    $.ajax({
        url:ctx+'/saleChance/updateDevResult',
        type:'post',
        data:{
            id:sid,
            devResult:devResult
        },
        success:function (data){
            if(data.code == 200){
                /**
                 * 1 隐藏工具条
                 * 2 把表格置为不可编辑状态
                 */
                $('#toolbar').hide();
                $('#dg').edatagrid('disableEditing')
            }else{
                $.messager.alert('来自Crm',data.msg,'error')
            }
        }
    })

}