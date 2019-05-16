(function () {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('CreateGroupController', CreateGroupController);

    CreateGroupController.$inject = ['GroupsService', 'MembersService', '$state'];

    function CreateGroupController(GroupsService, MembersService, $state) {

        let vm = this;

        vm.groups = {};
        vm.memberList = [];

        vm.addMemberToGroup = function () {
            let memberObj = {
                name: vm.groups.memberName
            };
            vm.memberList.unshift(memberObj);
            vm.groups.memberName = "";
        };

        vm.removeMember = function (index) {
            vm.memberList.splice(index, 1);
        };

        vm.goToAddNewGroup = function () {
            let group = {
                name: vm.groups.name
            };

            GroupsService.create(group).$promise.then(function (result) {
                let group = {
                    id: result.id,
                    name: result.name
                };

                angular.forEach(vm.memberList, function (item, index) {
                    item.balance = 0;
                    item.groups = {id: result.id};
                });

                MembersService.saveMembers(vm.memberList).$promise.then(function (data) {
                    $state.go('groups.balance', group);
                }).catch(function (reason) {
                    console.error(reason)
                })
            }).catch(function (reason) {
                console.error(reason)
            })
        };
    }
})();
