//登录
function login(){
    /**
     * 1 获取表单参数
     * 2 判断参数是否为空
     * 3 发送登录请求
     */
    var userName = $("#username").val();
    var userPwd = $("#password").val();
    if(isEmpty(userName)){
        alert("用户名为空");
        return;
    }
    if(isEmpty(password)){
        alert("密码为空");
        return;
    }
    $.ajax({
        url:ctx + "/user/login",
        type:"post",
        data:{
            userName: userName,
            userPwd: userPwd
        },
        success:function (data) {
            if(data.code==200){
                /*
                1 跳转主页
                2 设置cookie信息
                 */
                $.cookie("userIdStr",data.result.userIdStr);
                $.cookie("userName",data.result.userName);
                $.cookie("realName",data.result.realName);
              window.location.href=ctx + "/main";
            }else{
                alert("123456");
            }
        }
    })

}