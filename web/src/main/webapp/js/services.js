'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('evasionVisiteurApp.services', []).
        value('version', '0.1');


angular.module('evasionVisiteurApp.siteServices', ['restangular'])
        .config(function(RestangularProvider) {
    RestangularProvider.setBaseUrl("default-data");
}).
        factory('Site', function(Restangular) {
    return Restangular.one('site.json');
});