(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('PaymentsDeleteController',PaymentsDeleteController);

    PaymentsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Payments'];

    function PaymentsDeleteController($uibModalInstance, entity, Payments) {
        var vm = this;

        vm.payments = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Payments.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
