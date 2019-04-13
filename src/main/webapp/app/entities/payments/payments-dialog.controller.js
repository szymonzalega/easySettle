(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('PaymentsDialogController', PaymentsDialogController);

    PaymentsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Payments', 'Transfers'];

    function PaymentsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Payments, Transfers) {
        var vm = this;

        vm.payments = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.transfers = Transfers.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.payments.id !== null) {
                Payments.update(vm.payments, onSaveSuccess, onSaveError);
            } else {
                Payments.save(vm.payments, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('easySettleApp:paymentsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
