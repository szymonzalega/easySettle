(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsDialogController', GroupsDialogController);

    GroupsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Groups'];

    function GroupsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Groups) {
        var vm = this;

        vm.groups = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.groups.id !== null) {
                Groups.update(vm.groups, onSaveSuccess, onSaveError);
            } else {
                Groups.save(vm.groups, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('easySettleApp:groupsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
