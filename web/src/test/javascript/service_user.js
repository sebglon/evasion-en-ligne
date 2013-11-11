describe('Test du service user_auth', function() {
    var serverUrl = 'http://local';
    beforeEach(function() {
        module('evasionVisiteurApp.services', function(apiProvider) {
            apiProvider.setServerUrl(serverUrl);
        });
    });

    describe('Test de version', function() {
        var service;
        beforeEach(inject(function(version) {
            service = version;
        }));

        it('Validation de la version', function() {
            expect(service).toBeDefined();
            expect(service, '1.0');
        });
    });

    describe('Test de API', function() {
        var service, httpBackend, popupMock, eventFnct, postMessageMock;
        beforeEach(inject(function(api, $httpBackend) {
            httpBackend = $httpBackend;
            service = api;
            spyOn(window, 'open').andCallFake(function() {
                popupMock = {
                    focus: jasmine.createSpy(),
                };
                return popupMock;
            });

            spyOn(angular, 'element').andCallFake(function() {
                mockWindow = jasmine.createSpy('windowElement');
                mockWindow.bind = jasmine.createSpy('bind').andCallFake(function(evnt, fnx) {
                    eventFnct = fnx;
                });
                mockWindow.unbind = jasmine.createSpy('unbind');
                return mockWindow;
            });
        }));

        it('Test flow auth et token ', function() {
            //httpBackend.when("GET", serverUrl + '/ws/user/auth?redirect=' + window.location.href).respond(200);
            var authCallBack = jasmine.createSpy();
            expect(service).toBeDefined();
            expect(httpBackend).toBeDefined();
            expect(service.getServerUrl, serverUrl);
            service.user.auth(authCallBack);
            expect(window.open).toHaveBeenCalledWith(serverUrl + '/ws/user/auth?redirect=' + window.location.href, 'name', 'height=600,width=450');
            
            // popupMock.location = window.location.href+'/callback.html';
                        httpBackend.whenGET( serverUrl + '/ws/user/token').respond(200);
//            service.user.token();
//            httpBackend.flush();
//            eventFnct( {source: popupMock, orgin: window.location.origin});
//            expect(authCallBack).toHaveBeenCalled();
        });

        it('Test user logout on success', function() {
            httpBackend.whenGET(serverUrl + '/ws/user/logout').respond(200);
            var promise = service.user.logout(), theData;
            promise.then(function(data) {
                theData = data;
            });
            httpBackend.flush();
            expect(theData, true);
        });

        it('Test user logout on fail', function() {
            httpBackend.whenGET(serverUrl + '/ws/user/logout').respond(500);
            var promise = service.user.logout(), theData, result=null;
            promise.then(function(data) {
                theData = data;
            },function(reason) {
                result =reason;
            });
            httpBackend.flush();
            expect(result, 'error');
        });

            it('Test user info on success', function() {
            httpBackend.whenGET(serverUrl + '/ws/user/info').respond(200);
            var promise = service.user.info(), theData;
            promise.then(function(data) {
                theData = data;
            });
            httpBackend.flush();
            expect(theData, true);
        });

        it('Test user info on fail', function() {
            httpBackend.whenGET(serverUrl + '/ws/user/info').respond(500);
            var promise = service.user.info(), theData, result=null;
            promise.then(function(data) {
                theData = data;
            },function(reason) {
                result =reason;
            });
            httpBackend.flush();
            expect(result, 'error');
        });
    });

});