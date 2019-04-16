(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsBalanceAddController', GroupsBalanceAddController);

    GroupsBalanceAddController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'GroupsService', 'MembersService'];

    function GroupsBalanceAddController($scope, $rootScope, $stateParams, previousState, GroupsService, MembersService) {
        var vm = this;

        vm.newPayment = {};
        vm.newPayment.loanersList = [];
        vm.members = [];
        vm.members = $stateParams.members;

        vm.selectPayer = function(payerId){
            vm.newPayment.payerId = payerId;
        };

        vm.selectLoaner = function (loanerId) {
            var index = vm.checkLoanerIsSelected(loanerId);
            if(index === -1){
                vm.newPayment.loanersList.push(loanerId);
            } else {
                vm.newPayment.loanersList.splice(index, 1);
            }
        };

        vm.checkLoanerIsSelected = function (loanerId) {
            return vm.newPayment.loanersList.indexOf(loanerId);

        }
    }
})();
