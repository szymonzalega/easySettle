(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsBalanceController2', GroupsBalanceController);

    GroupsBalanceController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'GroupsService', 'MembersService', '$state'];

    function GroupsBalanceController($scope, $rootScope, $stateParams, previousState, GroupsService, MembersService, $state) {
        let vm = this;

        vm.group = {
           id: parseInt($stateParams.id),
           name: $stateParams.name
        };

        // vm.previousState = previousState.name;

        vm.getMembersByGroup = function(groupId){
            vm.membersPromise = MembersService.getMembersByGroup(groupId).$promise.then(function (data) {
                vm.members = data;
            })
        };
        vm.getMembersByGroup(vm.group.id);

        vm.goToAddNewPayment = function(){
            let params = {
                members: vm.members,
                id: vm.group.id
            };
            $state.go('groups.balance.add', params);
        };

        vm.goToTransactionsList = function(){
            $state.go('payments', {id: vm.group.id});
        };

        vm.goToSettlement = function(){
            $state.go('settlement', {id: vm.group.id});
        };

    }
})();
