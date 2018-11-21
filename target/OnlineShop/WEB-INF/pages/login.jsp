<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author" content="">
	<title>Login</title>
	<link href="static/css/bootstrap.min.css" rel="stylesheet">
	<link href="static/css/font-awesome.min.css" rel="stylesheet">
	<link href="static/css/prettyPhoto.css" rel="stylesheet">
	<link href="static/css/price-range.css" rel="stylesheet">
	<link href="static/css/animate.css" rel="stylesheet">
	<link href="static/css/main.css" rel="stylesheet">
	<link href="static/css/responsive.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="static/js/html5shiv.js"></script>
    <script src="static/js/respond.min.js"></script>
  <![endif]-->       
  <link rel="shortcut icon" href="static/images/ico/favicon.ico">
  <link rel="apple-touch-icon-precomposed" sizes="144x144" href="static/images/ico/apple-touch-icon-144-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="114x114" href="static/images/ico/apple-touch-icon-114-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="72x72" href="static/images/ico/apple-touch-icon-72-precomposed.png">
  <link rel="apple-touch-icon-precomposed" href="static/images/ico/apple-touch-icon-57-precomposed.png">
</head><!--/head-->

<body>
	<header id="header">
		<div class="header-middle"><!--header-middle-->
			<div class="container">
				<div class="row">
					<div class="col-sm-6">
						<div class="logo pull-left">
							<!-- <a href="product"><img src="static/images/home/logo.png" alt="" /></a> -->
						</div>
						<div class="btn-group pull-right">
							<c:if test="${sessionScope.sessionUser ne null}">
							<div class="btn-group clearfix">
								<h5 class="sessionUser">You logined as: ${sessionScope.sessionUser.name}</h5>
								<form class="logoutForm" action="profile" method="POST"><button type="submit" class="btn btn-default but_log" name="logout" value="logout">Logout</button></form>
							</div>
							</c:if>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="shop-menu pull-right">
							<ul class="nav navbar-nav collapse navbar-collapse">
								<li><a href="home">Home</a></li>
								<li><a href="cart">Cart</a></li>
								
								<c:if test="${sessionScope.sessionUser eq null}">
									<li><a href="signup">SignUp</a></li>
									<li><a href="login">Login</a></li>
								</c:if>
								
								<c:if test="${sessionScope.sessionUser ne null}">
									<li><a href="profile">Profile</a></li>
								</c:if>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div><!--/header-middle-->
	</header>


	<section id="form_login"><!--form-->
		<div class="container">
			<div class="row">
			
				<c:if test="${sessionScope.sessionUser eq null}">
				<div class="col-sm-4 col-sm-offset-4">
					<div class="login-form"><!--login form-->
						<h2>Login to your account</h2>					
						<form action="login" method="POST">
						
							<c:if test="${isDeniedAccess}">
							<div class="denied">
								<!-- <label>Access denied!</label> -->
								<label>${deniedMessage}</label>
							</div>
							</c:if>
							
							<input type="email" placeholder="Email Address" name="login" value="${login}" autofocus/>
							<input type="password" placeholder="Password" name="pass" value=""/>
							<span>
								<input type="checkbox" class="checkbox" name="keep_sign_in" value="check"/> 
								Keep me signed in
							</span>
							<button type="submit" class="btn btn-default">Login</button>
						</form>
					</div><!--/login form-->
				</div>
				</c:if>
			</div>
		</div>
	</section><!--/form-->

	<footer><!--/Footer-->
		<div class="container">
			<div class="footer-bottom">
				<div class="row">
					<div class="col-sm-12">
					<p class="pull-left">Copyright © 2013 E-SHOPPER Inc. All rights reserved.</p>
					</div>
				</div>
			</div>
		</div>
	</footer><!--/Footer-->


	<script src="static/js/jquery.js"></script>
	<script src="static/js/price-range.js"></script>
	<script src="static/js/jquery.scrollUp.min.js"></script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/jquery.prettyPhoto.js"></script>
	<script src="static/js/main.js"></script>
</body>
</html>
<script>
		var visible = true;
		function showPass() {
			if(visible) {
				document.getElementById('showChangePass' ).style.display = 'none';
				visible = false;
			} else {
				document.getElementById('showChangePass' ).style.display = 'block';
				visible = true;
			}
		}
</script>