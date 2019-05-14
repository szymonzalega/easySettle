(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('SettlementController', SettlementController);

    SettlementController.$inject = ['$scope', '$rootScope', '$stateParams', 'GroupsService', 'MembersService', '$state'];

    function SettlementController($scope, $rootScope, $stateParams, GroupsService, MembersService, $state) {
        let vm = this;

        let groupId = parseInt($stateParams.id);

        vm.settleDebt = function(groupId){
            vm.settlementPromise = MembersService.settleDebt(groupId).$promise.then(function (data) {
                vm.settlement = data;
            })
        };
        vm.settleDebt(groupId);

    }
})();
