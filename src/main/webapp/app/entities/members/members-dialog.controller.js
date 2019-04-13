(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('MembersDialogController', MembersDialogController);

    MembersDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Members', 'Groups'];

    function MembersDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Members, Groups) {
        var vm = this;

        vm.members = entity;
        vm.clear = clear;
        vm.save = save;
        vm.groups = Groups.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.members.id !== null) {
                Members.update(vm.members, onSaveSuccess, onSaveError);
            } else {
                Members.save(vm.members, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('easySettleApp:membersUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
