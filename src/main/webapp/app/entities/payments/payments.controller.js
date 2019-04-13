(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('PaymentsController', PaymentsController);

    PaymentsController.$inject = ['Payments'];

    function PaymentsController(Payments) {

        var vm = this;

        vm.payments = [];

        loadAll();

        function loadAll() {
            Payments.query(function(result) {
                vm.payments = result;
                vm.searchQuery = null;
            });
        }
    }
})();
