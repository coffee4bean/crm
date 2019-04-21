function formatDevResult(val, row, index) {
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

function formatOp(val, row, index) {
    // var devResult = row.devResult;
    // if(devResult==0 || devResult==1){
    //     return "<a href='javascript:;'>开发</a>"
    // }else{
    //     return "<a href='javascript:;'>查看详情</a>"
    // }

    var devResult=row.devResult;
    if(devResult==0||devResult==1){
        var href="javascript:openSaleChanceInfoDialog("+'"开发机会数据"'+","+row.id+")";
        return "<a href='"+href+"'>开发</a>";
    }
    if(devResult==2||devResult==3){
        var href="javascript:openSaleChanceInfoDialog("+'"详情机会数据"'+","+row.id+")";
        return "<a href='"+href+"'>查看详情</a>";
    }
}


function openSaleChanceInfoDialog(title,id) {
    /**
     * 打开新的选项卡
     */
    window.parent.openTab(title+"_"+id,ctx+"/cusDevPlan/index?sid="+id);
}

function querySaleChancesByParams() {
    $('#dg').datagrid('load', {
        customerName: $('#customerName').val(),
        devResult: $('#devResult').combobox('getValue'),
        createDate: $('#time').datebox('getValue')
    });
}