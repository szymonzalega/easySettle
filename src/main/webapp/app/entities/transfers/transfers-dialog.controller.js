(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('TransfersDialogController', TransfersDialogController);

    TransfersDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Transfers', 'Members', 'Payments'];

    function TransfersDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Transfers, Members, Payments) {
        var vm = this;

        vm.transfers = entity;
        vm.clear = clear;
        vm.save = save;
        vm.members = Members.query();
        vm.payments = Payments.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transfers.id !== null) {
                Transfers.update(vm.transfers, onSaveSuccess, onSaveError);
            } else {
                Transfers.save(vm.transfers, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('easySettleApp:transfersUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
