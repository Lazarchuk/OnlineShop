<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author" content="">
	<title>Home</title>
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

<section>
	<div class="container">
		<div class="row">
			<div class="col-sm-3">
				<div class="left-sidebar">
					<h2>Category</h2>
					<div class="panel-group category-products" id="accordian"><!--category-productsr-->
						<div class="panel panel-default">
							<div class="panel-heading">
								<form action="home">
									<span class="panel-title">
										<select name="category">
											<option value="All" <c:out value="${category eq 'All' ? 'selected':''}"/>>All</option>
											<c:forEach items="${categories}" var="categ">
												<option value="${categ}" <c:out value="${category eq categ ? 'selected':''}"/>>${categ}</option>
											</c:forEach>
										</select>
									</span>
									<button class="submit-category" type="submit"><img src="static/images/submit_category.png" width="40" height="30"/></button>
								</form>
							</div>
						</div>
					</div><!--/category-products-->
					
					<div class="price-range"><!--price-range-->
						<h2>Price Range</h2>
						<div class="well text-center">
							<input type="text" class="span2" value="" data-slider-min="0" data-slider-max="${maxPrice}" data-slider-step="10" data-slider-value="[${lowerPrice},${upperPrice}]" id="sl2" ><br />
							<b class="pull-left">$ 0</b> <b class="pull-right">$ ${maxPrice}</b>
							<div id="form_but">	
								<form class="priceform" action="home" method="GET">
									<input type="hidden" name="price_range" value="" id="hidden_price"/>
									<button class="submit-category" type="submit"><img src="static/images/submit_category.png" width="40" height="30"/></button>
								</form>
							</div>
						</div>
					</div><!--/price-range-->

				</div>
			</div>

			<div class="col-sm-9 padding-right">
				<div class="features_items"><!--features_items-->
					<h2 class="title text-center">Features Items</h2>
					
					<c:forEach items="${requestScope['products']}" var="prod">
					<div class="col-sm-4">
						<div class="product-image-wrapper">
							<div class="single-products">
								<div class="productinfo text-center">
									<img src="static/images/products/${prod.id}.jpg" style="max-height:160px" alt="" />
									<h2>${prod.price} UAH</h2>
									<p>${prod.name}</p>
									<div class="cart_quantity_button">
										<img class="cart_quantity_up" src="static/images/minus.png" onclick="decreaseAmount(${prod.id})">
										<input class="cart_quantity_input" id="${prod.id}" type="text" name="quantity" value="1" autocomplete="off" size="2">
										<img class="cart_quantity_down" src="static/images/plus.png" onclick="increaseAmount(${prod.id})">
									</div>
									<button class="btn btn-default add-to-cart" onclick="addToCart(${prod.id})"><i class="fa fa-shopping-cart"></i>Add to cart</button>
								</div>
							</div>
							<div class="choose">
								<ul class="nav nav-pills nav-justified">
									<form class="details" action="product-details">
										<button type="submit" class="btn btn-default btn-details" name="prodId" value="${prod.id}">Details</button>
									</form>
								</ul>
							</div>
						</div>
					</div>
					</c:forEach>
				</div><!--features_items-->
			</div>
		</div>
	</div>
</section>

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
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/jquery.scrollUp.min.js"></script>
<script src="static/js/price-range.js"></script>
<script src="static/js/jquery.prettyPhoto.js"></script>
<script src="static/js/main.js"></script>
</body>
</html>
<script>
var slider = document.getElementById('price_range');
var priceRange = document.getElementById('hidden_price');
priceRange.value = slider.innerHTML;
document.getElementById('form_but').addEventListener('mouseover', listenPrice);

function listenPrice(){
	var slider = document.getElementById('price_range');
	var priceRange = document.getElementById('hidden_price');
	priceRange.value = slider.innerHTML;
}


function increaseAmount(id){
	var count = document.getElementById(id).value;
	count++;
	var res = document.getElementById(id);
	res.value = count;
}

function decreaseAmount(id){
	var count = document.getElementById(id).value;
	if (count>1){
		count--;
		var res = document.getElementById(id);
		res.value = count;
	}
	
}

function addToCart(id){
	$.ajax({
	  type: "POST",
	  url: "/OnlineShop/cart",
	  data: {
		productId: id,
		prodAmount: document.getElementById(id).value
	  },
	  success: function(result) {
		  alert(document.getElementById(id).value+" item(s) was added to cart!");
	  }
	});
}
</script>