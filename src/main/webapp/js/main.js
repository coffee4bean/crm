function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}

//退出
function logout(){
    /**
     * 1 清除cookie
     * 2 跳转到登录页
     */
    $.messager.confirm('来自Crm','您确认要退出系统吗？',function(r){
        if (r){
            $.removeCookie("userIdStr");
            $.removeCookie("userName");
            $.removeCookie("realName");
            window.location.href=ctx + "/index";
        }
    });
}
//修改密码
function openPasswordModifyDialog() {
    $("#dlg").dialog("open");
}
//修改密码
function modifyPassword(){
    $("#fm").form("submit",{
        url:ctx+"/user/updateUserPwd",
        onSubmit:function (){
            return $(this).form("validate");//返回false终止表单提交
        },
        success:function(data){
            data = JSON.parse(data);
            if(data.code==200){
                $.messager.alert('来自Crm',data.msg,'info',function () {
                    /****
                     * 1. 清除登陆信息
                     * 2. 跳转到登录页
                     * */
                    $.removeCookie("userIdStr");
                    $.removeCookie("userName");
                    $.removeCookie("realName");
                    window.location.href = ctx + "/index";
                })
            }else{
                $.messager.alert('来自Crm',data.msg,'error');
            }
        }
    });
}