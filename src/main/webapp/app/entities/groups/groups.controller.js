(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsController', GroupsController);

    GroupsController.$inject = ['GroupsService', '$state'];

    function GroupsController(GroupsService, $state) {

        var vm = this;

        vm.groups = [];

        getAllGroups();

        function getAllGroups() {
            vm.groups = GroupsService.get().$promise.then(function (data) {
                vm.groups = data;
                console.log(data);
            })
        }

        vm.goToEdit = function(data){
            $state.go('groups.edit', {group: data});
        }
    }
})();
