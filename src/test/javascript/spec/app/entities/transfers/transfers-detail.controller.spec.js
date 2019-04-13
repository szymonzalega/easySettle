'use strict';

describe('Controller Tests', function() {

    describe('Transfers Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTransfers, MockMembers, MockPayments;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTransfers = jasmine.createSpy('MockTransfers');
            MockMembers = jasmine.createSpy('MockMembers');
            MockPayments = jasmine.createSpy('MockPayments');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Transfers': MockTransfers,
                'Members': MockMembers,
                'Payments': MockPayments
            };
            createController = function() {
                $injector.get('$controller')("TransfersDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'easySettleApp:transfersUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
