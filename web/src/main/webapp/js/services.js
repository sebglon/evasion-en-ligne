'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('evasionVisiteurApp.services', []).
  value('version', '0.1');


angular.module('evasionVisiteurApp.siteServices', ['ngResource']).
    factory('Site', function($resource){
  return $resource('default-data/site.json', {}, {
    query: {method:'GET', params:{siteId:'sites'}, isArray:false}
  });
});