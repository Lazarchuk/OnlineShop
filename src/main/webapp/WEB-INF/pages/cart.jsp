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
    <title>Cart</title>	
	<link href="resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/css/font-awesome.min.css" rel="stylesheet">
    <link href="resources/css/prettyPhoto.css" rel="stylesheet">
    <link href="resources/css/price-range.css" rel="stylesheet">
    <link href="resources/css/animate.css" rel="stylesheet">
	<link href="resources/css/main.css" rel="stylesheet">
	<link href="resources/css/responsive.css" rel="stylesheet">
	
    <!--[if lt IE 9]>
    <script src="static/js/html5shiv.js"></script>
    <script src="static/js/respond.min.js"></script>
    <![endif]-->  
	
    <!-- <link rel="shortcut icon" href="static/images/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="static/images/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="static/images/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="static/images/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="static/images/ico/apple-touch-icon-57-precomposed.png"> -->
	
	<link rel="shortcut icon" href="resources/images/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="resources/images/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="resources/images/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="resources/images/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="resources/images/ico/apple-touch-icon-57-precomposed.png">
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
                                <div class="btn-group">
                                    <h5 class="sessionUser">You logined as: ${sessionScope.sessionUser.name}</h5>
									<form class="logoutForm" action="profile" method="POST"><button type="submit" class="btn btn-default but_log" name="logout" value="logout">Logout</button></form>
                                </div>
                            </c:if>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="shop-menu pull-right">
							<ul class="nav navbar-nav collapse navbar-collapse">
								<li><a href="products">Products</a></li>
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

	<section id="cart_items">
		<div class="container">
			<div class="table-responsive cart_info">
				<table class="table table-condensed">
					<thead>
						<tr class="cart_menu">
							<td class="image">Item</td>
							<td class="description"></td>
							<td class="price">Price</td>
							<td class="quantity">Quantity</td>
							<td class="total">Total</td>
							<td></td>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="prod" items="${sessionScope.cart.cartItems}">
							<c:set var="prodPrice" value="${prod.key.price}"/>
							<c:set var="prodAmount" value="${prod.value}"/>
							<tr>
								<td class="cart_product">
									<a href="products/details/${prod.key.id}"><img src="resources/images/products/${prod.key.id}.jpg" width="140" alt=""></a>
								</td>
								<td class="cart_description">
									<h4><a href="">${prod.key.name}</a></h4>
								</td>
								<td class="cart_price">
									<p>${prod.key.price}</p>
								</td>
								<td class="cart_quantity">
									<form action="cart" method="POST">
									<div class="cart_quantity_button">
										<input type="hidden" name="reWriteId" value="${prod.key.id}">
										<img class="cart_quantity_up" src="resources/images/minus.png" onclick="decreaseAmount(${prod.key.id})">
										<input class="cart_quantity_input" type="text" id="${prod.key.id}" name="prodAmount" value="${prod.value}" autocomplete="off" size="2">
										<img class="cart_quantity_down" src="resources/images/plus.png" onclick="increaseAmount(${prod.key.id})">
										<div><button class="update-btn" type="submit" id="${prod.key.id}but"><img src="resources/images/update.png" style="margin:2px"></button></div>
									</div>
									</form>
								</td>
								<td class="cart_total">
									<p class="cart_total_price">${prodPrice*prodAmount} UAH</p>
								</td>
								<td>
									<!-- <a class="cart_quantity_delete" href=""><i class="fa fa-times"></i></a> -->
									<form class="cart_delete" action="cart" method="POST">
										<input type="hidden" name="deleteId" value="${prod.key.id}">
										<button class="cart_quantity_delete" type="submit"><i class="fa fa-times"></i></button>
									</form>
								</td>
							</tr>
						</c:forEach>
						
						<tr>
							<td colspan="3"></td>
							<td class="cart_total_quantity">
								<p class="cart_total_quant">${totalAmount}</p>
							</td>
							<td class="cart_total_amount">
								<p class="cart_total_price">${totalPrice} UAH</p>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="row">
				<div class="col-sm-12 text-right">
					<button class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>Buy</button>
				</div>
			</div>
		</div>
	</section> <!--/#cart_items-->
	
<%@include file="footer.jsp" %>

	<!-- <footer>
		<div class="container">
			<div class="footer-bottom">
				<div class="row">
					<div class="col-sm-12">
					<p class="pull-left">Copyright © 2013 E-SHOPPER Inc. All rights reserved.</p>
					</div>
				</div>
			</div>
		</div>
	</footer> -->
	
	<script src="resources/js/jquery.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/jquery.scrollUp.min.js"></script>
    <script src="resources/js/jquery.prettyPhoto.js"></script>
    <script src="resources/js/main.js"></script>
</body>
</html>

<script>
function increaseAmount(id){
	var count = document.getElementById(id).value;
	document.getElementById(id+"but").style.display = 'block';
	count++;
	var res = document.getElementById(id);
	res.value = count;
}

function decreaseAmount(id){
	var count = document.getElementById(id).value;
	document.getElementById(id+"but").style.display = 'block';
	if (count>1){
		count--;
		var res = document.getElementById(id);
		res.value = count;
	}
	
}

</script>