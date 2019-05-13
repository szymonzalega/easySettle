(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsBalanceAddController', GroupsBalanceAddController);

    GroupsBalanceAddController.$inject = ['$scope', '$rootScope', '$stateParams', 'PaymentsService'];

    function GroupsBalanceAddController($scope, $rootScope, $stateParams, PaymentsService) {
        var vm = this;

        vm.newPayment = {};
        vm.newPayment.loanersList = [];
        vm.members = [];
        vm.members = $stateParams.members;
        vm.groupId = $stateParams.id;

        vm.selectPayer = function(payer_id){
            vm.newPayment.payer_id = payer_id;
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

        };

        vm.savePayment = function () {
            vm.newPayment.date = new Date();
            vm.newPayment.group_id = parseInt(vm.groupId);
            PaymentsService.create(vm.newPayment);
        }
    }
})();
