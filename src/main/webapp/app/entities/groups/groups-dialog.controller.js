(function () {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsDialogController', GroupsDialogController);

    GroupsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'GroupsService', 'MembersService'];

    function GroupsDialogController($timeout, $scope, $stateParams, $uibModalInstance, GroupsService, MembersService) {
        var vm = this;

        vm.groups = $stateParams.group;
        vm.clear = clear;
        vm.save = save;

        vm.memberIndex = null;
        vm.memberList = [];
        vm.saveMemberInGroup = function () {
            if (vm.memberIndex != null) {
                vm.memberList[vm.memberIndex] = {name: vm.member.name};
            } else {
                vm.memberList.push({name: vm.member.name});
                vm.member.name = "";
            }
            vm.memberIndex = null;

            vm.addNewMemberInput = false;
        };

        vm.editMember = function (memberIndex) {
            vm.memberIndex = memberIndex;
            vm.addNewMemberInput = true;
            vm.member.name = vm.memberList[memberIndex].name;
        };

        vm.removeMember = function (memberIndex) {
            vm.memberList.splice(memberIndex, 1);
        };

        vm.saveGroup = function () {
            var group = {
                name: vm.groups.name
            };

            GroupsService.create(group).$promise.then(function (result) {
                console.log(result);

                angular.forEach(vm.memberList, function (item, index) {
                   item.balance = 0;
                   item.groups = {id:result.id};
                });

                MembersService.saveMembers(vm.memberList).$promise.then(function (data) {
                    console.log(data)
                }).catch(function (reason) { console.error(reason) })
            }).catch(function (reason) {
                console.error(reason)
            })
        };


        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.groups.id !== null) {
                Groups.update(vm.groups, onSaveSuccess, onSaveError);
            } else {
                Groups.save(vm.groups, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('easySettleApp:groupsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
