<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Mongodb测试工具</title>
<script type="text/javascript" src="js/lib/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="js/index.js"></script>
<style> 
#main {position: absolute;width:500px;height:150px;left:50%;top:50%; 
margin-left:-200px;margin-top:-100px;border:1px solid #00F} 
</style> 
</head>
<body>
<div id="main">
	<div id="center" align="center">
	读线程数:<input type="text" id="readthreads"/><input type="button" id="readstart" value="开始测试读"/><input type="button" id="readstop" value="停止测试读"/>
	<p/>
	<span id="readresult"></span>
	<p/>
	写线程数:<input type="text" id="writethreads"/><input type="button" id ="writestart" value="开始测试写"/><input type="button" id="writestop" value="停止测试写"/>
	<p/>
	<span id="writeresult"></span>
	<p/>
	</div>
</div>
</body>
</html>

