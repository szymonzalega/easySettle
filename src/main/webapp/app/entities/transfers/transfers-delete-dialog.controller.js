(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('TransfersDeleteController',TransfersDeleteController);

    TransfersDeleteController.$inject = ['$uibModalInstance', 'entity', 'Transfers'];

    function TransfersDeleteController($uibModalInstance, entity, Transfers) {
        var vm = this;

        vm.transfers = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Transfers.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
