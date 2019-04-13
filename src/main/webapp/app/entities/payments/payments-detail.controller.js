(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('PaymentsDetailController', PaymentsDetailController);

    PaymentsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Payments', 'Transfers'];

    function PaymentsDetailController($scope, $rootScope, $stateParams, previousState, entity, Payments, Transfers) {
        var vm = this;

        vm.payments = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('easySettleApp:paymentsUpdate', function(event, result) {
            vm.payments = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
