<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri ="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>

    <!-- 指定字符集 -->
    <meta charset="utf-8">

    <!-- 使用Edge最新的浏览器的渲染方式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
    width: 默认宽度与设备的宽度相同
    initial-scale: 初始的缩放比，为1:1 -->
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>用户信息管理系统</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="js/jquery-2.1.0.min.js"></script>

    <!-- 3. 导入bootstrap的js文件 -->
    <script src="js/bootstrap.min.js"></script>

    <style type="text/css">
        td, th {
            text-align: center;
        }
    </style>

    <script>

        function deleteUser(id) {
            if (confirm("确定删除？")) {      // 安全提示
                // 访问路径
                location.href="${pageContext.request.contextPath}/deleteUserServlet?id="+id;
            }
        }

        window.onload = function () {

            /*批量删除*/
            // 1.给删除选中按钮添加单击事件
            document.getElementById("deleteSelected").onclick = function () {
                if (confirm("确定删除选中条目？")) {     // 安全提示

                    // 前端解决空选误操作
                    let flag = false;
                    const checkboxes = document.getElementsByName("uid");
                    for (let cb of checkboxes) {
                        if (cb.checked) {   // 存在某个条目被选中了
                            flag = !flag;
                            break;
                        }
                    }

                    // 2.表单提交
                    if (flag) document.getElementById("form").submit();

                }

            };

            /*全选/全不选*/
            // 1.获取第一个checkbox
            document.getElementById("firstCb").onclick = function () {
                // 2.获取下面列表中所有的checkbox
                const checkboxes = document.getElementsByName("uid");
                // 3.遍历
                for (let cb of checkboxes) {
                    // 4.将每个cb的状态和firstCb同步
                    cb.checked = this.checked;
                }
            }
        }

    </script>

</head>
<body>
<div class="container">

    <h3 style="text-align: center">用户信息列表</h3>

    <!--查询表单输入栏-->
    <div style="float: left; margin: 5px">
        <form class="form-inline" action="${pageContext.request.contextPath}/findUserByPageServlet" method="post">
            <div class="form-group">
                <label for="inputName">姓名</label>
                <input type="text" class="form-control" value="${condition.name[0]}" name="name" id="inputName">
            </div>
            <div class="form-group">
                <label for="inputAddress">籍贯</label>
                <input type="text" class="form-control" value="${condition.address[0]}" name="address" id="inputAddress">
            </div>
            <div class="form-group">
                <label for="inputEmail">邮箱</label>
                <input type="text" class="form-control" value="${condition.email[0]}" name="email" id="inputEmail">
            </div>
            <button type="submit" class="btn btn-default">查询</button>
        </form>
    </div>

    <!--表单操作栏-->
    <div style="float: right; margin: 5px">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/add.jsp">添加联系人</a>
        <a class="btn btn-primary" href="javascript:void(0);" id="deleteSelected">删除选中</a>
    </div>

    <!--利用表单的提交特性实现复选功能-->
    <form id="form" action="${pageContext.request.contextPath}/deleteSelectedUserServlet" method="post">

        <!--数据展示栏-->
        <table border="1" class="table table-bordered table-hover">
            <tr class="success">
                <th><input type="checkbox" id="firstCb"></th>    <!--复选框-->
                <th>编号</th>
                <th>姓名</th>
                <th>性别</th>
                <th>年龄</th>
                <th>籍贯</th>
                <th>QQ</th>
                <th>邮箱</th>
                <th>操作</th>
            </tr>

            <c:forEach items="${pb.list}" var="user" varStatus="s">
                <tr>
                    <th><input type="checkbox" name="uid" value="${user.id}"></th>    <!--复选框-->
                    <td>${s.count}</td>
                    <td>${user.name}</td>
                    <td>${user.gender}</td>
                    <td>${user.age}</td>
                    <td>${user.address}</td>
                    <td>${user.qq}</td>
                    <td>${user.email}</td>
                    <td>
                        <a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/findUserServlet?id=${user.id}">修改</a>&nbsp;
                        <a class="btn btn-default btn-sm" href="javascript:deleteUser(${user.id});">删除</a>
                        <!--删除是比较危险的做法，有必要添加核实逻辑-->
                    </td>
                </tr>
            </c:forEach>

        </table>

    </form>

    <!--分页栏-->
    <div>
        <nav aria-label="Page navigation">
            <ul class="pagination">

                <c:if test="${pb.currentPage == 1}">
                    <li class="disabled">
                </c:if>

                <c:if test="${pb.currentPage != 1}">
                    <li>
                </c:if>

                    <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${pb.currentPage-1}&rows=${pb.rows}&name=${condition.name[0]}&address=${condition.address[0]}&email=${condition.email[0]}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>

                <c:forEach begin="1" end="${pb.totalPage}" var="i">
                    <c:if test="${pb.currentPage == i}">
                        <li class="active">
                            <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${i}&rows=${pb.rows}&name=${condition.name[0]}&address=${condition.address[0]}&email=${condition.email[0]}">${i}</a>
                        </li>
                    </c:if>
                    <c:if test="${pb.currentPage != i}">
                        <li>
                            <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${i}&rows=${pb.rows}&name=${condition.name[0]}&address=${condition.address[0]}&email=${condition.email[0]}">${i}</a>
                        </li>
                    </c:if>
                </c:forEach>

                <c:if test="${pb.currentPage == pb.totalPage}">
                    <li class="disabled">
                </c:if>

                <c:if test="${pb.currentPage != pb.totalPage}">
                    <li>
                </c:if>

                    <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${pb.currentPage+1}&rows=${pb.rows}&name=${condition.name[0]}&address=${condition.address[0]}&email=${condition.email[0]}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>


                <span style="font-size: 25px; margin-left: 5px">
                    共&nbsp${pb.totalCount}&nbsp条记录，共&nbsp${pb.totalPage}&nbsp页
                </span>
            </ul>
        </nav>
    </div>

</div>
</body>
</html>
