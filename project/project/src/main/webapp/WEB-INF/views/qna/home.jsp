<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%--defaultTemplate를 적용할 때 템플릿의 title, body 영역은 여기에서 오버라이드한다 --%>
<!-- owl carousel css -->

<link href="<c:url value="../resources/css/owl.carousel.css"/>" rel="stylesheet">
<link href="<c:url value="../resources/css/owl.theme.css"/>" rel="stylesheet">

<!-- owl carousel -->
<script src="<c:url value="../resources/js/owl.carousel.min.js"/>"></script>

<tiles:insertDefinition name="mainTemplate">
	<tiles:putAttribute name="title">홈</tiles:putAttribute>
	<tiles:putAttribute name="body">
		<div id="all">
			<section>
				<!-- *** HOMEPAGE CAROUSEL ***
 _________________________________________________________ -->

				<div class="home-carousel">

					<div class="dark-mask"></div>

					<div class="container">
						<div class="homepage owl-carousel">
							<div class="item">
								<div class="row">
									<div class="col-sm-5 right">
										<p>
											<img src="<c:url value="../resources/img/logo.png"/>" alt="">
										</p>
										<h1>Multipurpose responsive theme</h1>
										<p>
											Business. Corporate. Agency. <br />Portfolio. Blog. E-commerce.
										</p>
									</div>
									<div class="col-sm-7">
										<img class="img-responsive" src="<c:url value="../resources/img/template-homepage.png"/>" alt="">
									</div>
								</div>
							</div>
							<div class="item">
								<div class="row">

									<div class="col-sm-7 text-center">
										<img class="img-responsive" src="<c:url value="../resources/img/template-mac.png"/>" alt="">
									</div>

									<div class="col-sm-5">
										<h2>46 HTML pages full of features</h2>
										<ul class="list-style-none">
											<li>Sliders and carousels</li>
											<li>4 Header variations</li>
											<li>Google maps, Forms, Megamenu, CSS3 Animations and much more</li>
											<li>+ 11 extra pages showing template features</li>
										</ul>
									</div>

								</div>
							</div>
							<div class="item">
								<div class="row">
									<div class="col-sm-5 right">
										<h1>Design</h1>
										<ul class="list-style-none">
											<li>Clean and elegant design</li>
											<li>Full width and boxed mode</li>
											<li>Easily readable Roboto font and awesome icons</li>
											<li>7 preprepared colour variations</li>
										</ul>
									</div>
									<div class="col-sm-7">
										<img class="img-responsive" src="<c:url value="../resources/img/template-easy-customize.png"/>" alt="">
									</div>
								</div>
							</div>
							<div class="item">
								<div class="row">
									<div class="col-sm-7">
										<img class="img-responsive" src="<c:url value="../resources/img/template-easy-code.png"/>" alt="">
									</div>
									<div class="col-sm-5">
										<h1>Easy to customize</h1>
										<ul class="list-style-none">
											<li>7 preprepared colour variations.</li>
											<li>Easily to change fonts</li>
										</ul>
									</div>
								</div>
							</div>
						</div>
						<!-- /.project owl-slider -->
					</div>
				</div>

				<!-- *** HOMEPAGE CAROUSEL END *** -->
			</section>



		</div>


	</tiles:putAttribute>
</tiles:insertDefinition>