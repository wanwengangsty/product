<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/scripts/jquery.js" type="text/javascript"></script>
    <script>
        $(function () {
            var $ul=$("#newsList");
            //发送ajax请求
            $.post("${pageContext.request.contextPath}/findAllUsers",function (data) {
alert(data)
                //遍历json数组
                $(data).each(function () {
                    var $li=$("<li>"+this.id+"-"+this.name+"-"+this.email+"</li>");
                    $ul.append($li);
                });
            },"json")
        })
    </script>
</head>
<body>
<ul id="newsList">

</ul>
</body>
</html>
