(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsController', GroupsController);

    GroupsController.$inject = ['GroupsService', '$state'];

    function GroupsController(GroupsService, $state) {

        let vm = this;

        vm.groups = [];

        function getAllGroupsWithMembers() {
            vm.groupsPromise = GroupsService.getGroupsWithMembers().$promise.then(function (data) {
                vm.groups = data;
                angular.forEach(vm.groups, function (group) {
                    group.name = uppercaseFirstLetter(group.name);
                    group.firstLetter = getFirstLetter(group.name);
                    group.membersName = "";

                    angular.forEach(group.members, function (member, index) {
                        if (index === group.members.length - 1) {
                            group.membersName += uppercaseFirstLetter(member.name);
                        } else {
                            group.membersName += `${uppercaseFirstLetter(member.name)}, `;
                        }
                    })
                })
            })
        }

        getAllGroupsWithMembers();

        function getFirstLetter(word) {
            let arr = word.split('');
            return arr[0].toUpperCase();
        }

        function uppercaseFirstLetter(word) {
            return word.charAt(0).toUpperCase() + word.slice(1);
        }

        vm.goToGroupContext = function(group){
            let params = {
              id: group.id,
              name: group.name
            };
            $state.go('groups.balance2', params);
        };

        vm.goToEdit = function(data){
            $state.go('groups.edit', {group: data, id: data.id});
        };

        vm.goToAddNewGroup = function(){
            $state.go('groups.new');
        };
    }
})();
