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
	<title>Profile</title>
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
				<div class="col-sm-3">
					<div class="left-sidebar">						
						<div class="panel-group category-products" id="accordian"><!--profile menu-->							
							<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title"><a href="profile">Main information</a></h4>
								</div>
							</div>
							<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title"><a href="?tab=additional_info">Additional information</a></h4>
								</div>
							</div>
							<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title"><a href="?tab=password">Password</a></h4>
								</div>
							</div>
						</div><!--/profile menu-->
					</div>
				</div>
			
				<div class="col-sm-5"><!--edit profile-->
				<c:if test="${param['tab'] eq null}">
					<div class="features_items">
						<h2 class="title text-center">Main information</h2>
						<div class="signup-form">												
							<form:form method="POST" modelAttribute="userMainInfo">
								<c:if test="${updated}">
									<div class="success">
										<label>Update success</label>
									</div>
								</c:if>
								
								<c:if test="${!validEditForm}">
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
									<label>Name</label>
								</div>
								<form:input path="name" cssClass="field" placeholder="Name"/>
								<div class="radio">
									<label>E-Mail address</label>
								</div>
								<form:input path="email" cssClass="field" placeholder="Email"/>
								<div class="radio">
									<label>Gender</label>
										<p>Male <form:radiobutton path="gender" value="Male"/></p>
										<p>Female <form:radiobutton path="gender" value="Female"/></p>								
								</div>
								<input type="hidden" name="mainInfo_hidden" value="hidden"/>
								<button type="submit" class="btn btn-default">Save</button>					
							</form:form>
						</div>
					</div>
				</c:if>
				
				<c:if test="${param['tab'] eq 'additional_info'}">
				<div class="features_items">
						<h2 class="title text-center">Additional information</h2>
						<div class="signup-form">												
							<form:form method="POST" modelAttribute="userAddInfo">
								<c:if test="${updated}">
									<div class="success">
										<label>Update success</label>
									</div>
								</c:if>
								
								<c:if test="${!validEditForm}">
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
									<label>Region</label>
								</div>								
								<form:select path="region">
									<form:option value="NONE" label="--Select area--"/>
									<form:options items="${regions}"/>
								</form:select>
								<div class="radio">
									<label>About yourself</label>
								</div>
								<form:textarea path="comment" cols="35" rows="7" placeholder="Comment"/>
								<input type="hidden" name="addInfo_hidden" value="hidden"/>
								<button type="submit" class="btn btn-default">Save</button>					
							</form:form>							
						</div>
					</div>
				</c:if>
				
				<c:if test="${param['tab'] eq 'password'}">
				<div class="features_items">
						<h2 class="title text-center">Password</h2>
						<div class="signup-form">												
							<form:form method="POST" modelAttribute="userPassword">
								<c:if test="${updated}">
									<div class="success">
										<label>Update success</label>
									</div>
								</c:if>
								
								<c:if test="${!validEditForm}">
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
									<label>Old password</label>
								</div>
								<form:password path="oldPassword" cssClass="field" placeholder="Old password"/>
								<div class="radio">
									<label>New password</label>
								</div>
								<form:password path="newPassword" cssClass="field" placeholder="New password"/>
								<div class="radio">
									<label>Submit new password</label>
								</div>
								<form:password path="submitPassword" cssClass="field" placeholder="Submit new password"/>
								<input type="hidden" name="password_hidden" value="hidden"/>
								<button type="submit" class="btn btn-default">Change password</button>					
							</form:form>								
						</div>
					</div>
				</c:if>
				</div><!--/edit profile-->
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
		function changeForm() {
			if(visible) {
				document.getElementById('mainInformation' ).style.display = 'none';
				document.getElementById('changePass' ).style.display = 'block';
				visible = false;
			} else {
				document.getElementById('mainInformation' ).style.display = 'block';
				document.getElementById('changePass' ).style.display = 'none';
				visible = true;
			}
		}
</script>