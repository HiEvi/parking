<?php 

	header('content-type:application/json;charset=utf-8');//设置浏览器解析格式为json
	$curPage = $_GET['curPage'];//前台传递的当前页
	$pageSize = 2;//页容量大小
	$start = ($curPage - 1) * $pageSize;//起始页数
	$totalPage = 0;//总页数
	$con = mysql_connect("localhost", "root", "123");//数据库连接参数
	mysql_select_db("db_wx_jqlr", $con);
	$pageList = array();//用来封装分页数据的数组
	
	//执行分页查询的同时在数据库中设置一个新字段，总记录数
	$sql="SELECT * ,(select count(*)FROM user )as total from user limit $start,$pageSize";
	$result = mysql_query($sql);
	
	while ($row = mysql_fetch_assoc($result)) {
	
	    $items = array(
	    "id" => $row['id'],
	    "name" => $row['name'],
	    "gender" => $row['gender'],
	    "total" => $row['total']//
	    );
	    //总页数等于总记录数/页容量，向上取整, 如3.5页，要有第四页
	    $totalPage = ceil($row['total'] / $pageSize);
	    array_push($pageList, $items);//填充分页数据
	}
			mysql_close($con);//关闭连接
	      
	        echo json_encode(array(   //将数据以转为json形式响应到客户端
		        "curPage" => $curPage,//当前页
		        "pageList" => $pageList,//分页数据集合
		        "totalPage" => $totalPage//总页数
	     ))
?>

