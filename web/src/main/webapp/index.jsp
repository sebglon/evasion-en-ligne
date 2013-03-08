<!doctype html>
<html lang="en" ng-app="evasionVisiteurApp">
<head>
  <meta charset="utf-8">
  <title>Evasion en ligne</title>
  <link rel="stylesheet" href="css/app.css"/>
</head>
<body>
  <ul class="menu">
    <li><a href="#/booktravel">booktravel</a></li>
    <li><a href="#/roadMap">roadmap</a></li>
    <li><a href="#/author">author</a></li>
  </ul>

  <div ng-view></div>

  <div>Evasion en ligne visiteur: v<span app-version></span></div>

  <!-- In production use: -->
  <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.0.4/angular.min.js"></script>
  
  <!-- instead of
  <script src="lib/angular/angular.js"></script>
  -->
  <script src="js/app.js"></script>
  <script src="js/services.js"></script>
  <script src="js/controllers.js"></script>
  <script src="js/filters.js"></script>
  <script src="js/directives.js"></script>
</body>
</html>
