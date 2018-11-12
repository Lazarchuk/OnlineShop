<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author" content="">
	<title>SignUp</title>
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
								<form class="logoutForm" action="profile" method="post"><button type="submit" class="btn btn-default but_log" name="logout" value="logout">Logout</button></form>
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


	<section id="form"><!--form-->
		<div class="container">
			<div class="row">
			
				<c:if test="${sessionScope.sessionUser eq null}">
				<div class="col-sm-4 col-sm-offset-4">
					<div class="signup-form"><!--sign up form-->
						<h2>New User Signup!</h2>
						<form:form method="POST" modelAttribute="newUserForm">
							<c:if test="${!validForm}">
								<div class="errors">
									<label>
										<ul>
											<c:forEach items="${errors}" var="error">
												<li> ${error}
											</c:forEach>
										</ul>
									</label>
								</div>
							</c:if>
							<div class="radio">
								<label>Your name</label>
							</div>
							<form:input path="name" cssClass="field" placeholder="Name"/>
							<div class="radio">
								<label>E-Mail address</label>
							</div>
							<form:input path="email" cssClass="field" placeholder="Email"/>
							<div class="radio">
								<label>Your password</label>
							</div>
							<form:password path="password" cssClass="field" placeholder="Password"/>
							<div class="radio">
								<label>Submit password</label>
							</div>
							<form:password path="submitPass" cssClass="field" placeholder="Submit password"/>
							<div class="radio">
								<label>Choose gender</label>
									<p>Male <form:radiobutton path="gender" value="Male"/></p>
									<p>Female <form:radiobutton path="gender" value="Female"/></p>
							</div>
							<div class="radio">
								<label>Select region</label>
							</div>
							<form:select path="area">
								<form:option value="NONE" label="--Select region--"/>
								<form:options items="${regions}"/>
							</form:select>
							<div class="radio">
								<label>Tell about yourself</label>
							</div>
							<form:textarea path="comment" cols="35" rows="7" placeholder="Your comment"/>
							<div class="radio">
								<label>I agree to the terms and conditions</label>
								<form:checkbox path="agreement"/>
							</div>	
							<button type="submit" class="btn btn-default">Signup</button>
						</form:form>				
					</div><!--/sign up form-->
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
