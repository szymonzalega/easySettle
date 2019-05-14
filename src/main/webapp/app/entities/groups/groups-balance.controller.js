(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsBalanceController', GroupsBalanceController);

    GroupsBalanceController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'GroupsService', 'MembersService', '$state'];

    function GroupsBalanceController($scope, $rootScope, $stateParams, previousState, GroupsService, MembersService, $state) {
        let vm = this;
        
        let groupId = parseInt($stateParams.id);

        // vm.previousState = previousState.name;

        vm.getMembersByGroup = function(groupId){
            vm.membersPromise = MembersService.getMembersByGroup(groupId).$promise.then(function (data) {
                vm.members = data;
            })
        };
        vm.getMembersByGroup(groupId);

        vm.goToAddNewPayment = function(){
            $state.go('groups.balance.add', {members: vm.members, id: groupId});
        };

        vm.goToTransactionsList = function(){
            $state.go('payments', {id: groupId});
        };

        vm.goToSettlement = function(){
            $state.go('settlement', {id: groupId});
        };

    }
})();
