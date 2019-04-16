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

        vm.goToGroupContext = function(id){
            $state.go('groups.balance', {id: id})
        };

        vm.goToEdit = function(data){
            $state.go('groups.edit', {group: data, id: data.id});
        };
    }
})();
