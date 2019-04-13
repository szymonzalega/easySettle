(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsDeleteController',GroupsDeleteController);

    GroupsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Groups'];

    function GroupsDeleteController($uibModalInstance, entity, Groups) {
        var vm = this;

        vm.groups = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Groups.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
