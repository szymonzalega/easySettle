(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsBalanceController', GroupsBalanceController);

    GroupsBalanceController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'GroupsService', 'MembersService', '$state'];

    function GroupsBalanceController($scope, $rootScope, $stateParams, previousState, GroupsService, MembersService, $state) {
        var vm = this;

        // vm.previousState = previousState.name;

        vm.getMembersByGroup = function(groupId){
            vm.membersPromise = MembersService.getMembersByGroup(groupId).$promise.then(function (data) {
                vm.members = data;
            })
        };
        vm.getMembersByGroup($stateParams.id);

        vm.goToAddNewPayment = function(){
            $state.go('groups.balance.add', {members: vm.members, id: $stateParams.id});
        };

        vm.goToTransactionsList = function(){
            $state.go('payments', {id: $stateParams.id});
        };

    }
})();
