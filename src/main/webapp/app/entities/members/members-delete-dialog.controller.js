(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('MembersDeleteController',MembersDeleteController);

    MembersDeleteController.$inject = ['$uibModalInstance', 'entity', 'Members'];

    function MembersDeleteController($uibModalInstance, entity, Members) {
        var vm = this;

        vm.members = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Members.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
