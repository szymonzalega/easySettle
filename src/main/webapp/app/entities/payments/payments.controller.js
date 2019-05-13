(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('PaymentsController', PaymentsController);

    PaymentsController.$inject = ['PaymentsService'];

    function PaymentsController(PaymentsService) {

        var vm = this;

        vm.payments = [];

        vm.getAllPayments = function () {
            vm.paymentsPromise = PaymentsService.getAllPayments().$promise.then(function (data) {
                vm.payments = data;
            })
        }

    }
})();
