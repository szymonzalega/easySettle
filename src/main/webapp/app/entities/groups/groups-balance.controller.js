(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsBalanceController', GroupsBalanceController);

    GroupsBalanceController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'GroupsService', 'MembersService', '$state'];

    function GroupsBalanceController($scope, $rootScope, $stateParams, previousState, GroupsService, MembersService, $state) {
        let vm = this;

        vm.group = $stateParams;

        vm.getMembersByGroup = function(groupId){
            vm.membersPromise = MembersService.getMembersByGroup(groupId).$promise.then(function (data) {
                vm.members = data;
            }).catch(function (error) {
                console.log(error);
            })
        };
        vm.getMembersByGroup(vm.group.id);

        function downloadGroupDetails(id) {
            vm.groupPromise = GroupsService.getOneGroup(id).$promise.then(function (data) {
                vm.group.name = data.name;
            }).catch(function (error) {
                console.log(error);
            })
        }

        function getGroupDetails(groupInfo){
            if(!groupInfo.name){
                downloadGroupDetails(groupInfo.id);
            }
        }
        getGroupDetails(vm.group);

        let params = {
            name: vm.group.name,
            id: vm.group.id
        };

        vm.goToAddNewPayment = function(){
            $state.go('payments.new', params);
        };

        vm.goToTransactionsList = function(){
            $state.go('payments', params);
        };

        vm.goToSettlement = function(){
            $state.go('groups.balance.settle', params);
        };

        vm.goToGroupSettings = function(){

        }

    }
})();
