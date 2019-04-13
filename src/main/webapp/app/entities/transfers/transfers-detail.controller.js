(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('TransfersDetailController', TransfersDetailController);

    TransfersDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Transfers', 'Members', 'Payments'];

    function TransfersDetailController($scope, $rootScope, $stateParams, previousState, entity, Transfers, Members, Payments) {
        var vm = this;

        vm.transfers = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('easySettleApp:transfersUpdate', function(event, result) {
            vm.transfers = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
