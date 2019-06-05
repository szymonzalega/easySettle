(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('PaymentsController', PaymentsController);

    PaymentsController.$inject = ['$state', 'PaymentsService', '$stateParams'];

    function PaymentsController($state, PaymentsService, $stateParams) {

        var vm = this;

        vm.groupParams = $stateParams;

        vm.payments = [];

        vm.getAllPayments = function () {
            var obj = {
                id: parseInt(vm.groupId)
            };
            vm.paymentsPromise = PaymentsService.getAllPayments(vm.groupParams.id).$promise.then(function (data) {
                vm.payments = data;
            })
        };
        vm.getAllPayments();

        vm.goToPaymentDetails = function (payment) {

        };

        vm.removePayment = function(payment){

        };

        vm.goToAddNewPayment = function(){
            $state.go('payments.new', vm.groupParams);
        };

    }
})();
