(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('PaymentsController', PaymentsController);

    PaymentsController.$inject = ['PaymentsService', '$stateParams'];

    function PaymentsController(PaymentsService, $stateParams) {

        var vm = this;
        vm.groupId = $stateParams.id;

        vm.payments = [];

        vm.getAllPayments = function () {
            var obj = {
                id: parseInt(vm.groupId)
            };
            vm.paymentsPromise = PaymentsService.getAllPayments(vm.groupId).$promise.then(function (data) {
                vm.payments = data;
                angular.forEach(vm.payments, function (payment) {
                    payment.loanersName = "";
                    angular.forEach(payment.loanersNameList, function (name, index) {
                        if (index === payment.loanersNameList.length - 1) {
                            payment.loanersName += name;
                        } else {
                            payment.loanersName += `${name}, `;
                        }
                    })
                });
                console.log(vm.payments);
            })
        };
        vm.getAllPayments();

    }
})();
