(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsBalanceController', GroupsBalanceController);

    GroupsBalanceController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'GroupsService', 'MembersService', '$state'];

    function GroupsBalanceController($scope, $rootScope, $stateParams, previousState, GroupsService, MembersService, $state) {
        let vm = this;

        vm.group = {
           id: parseInt($stateParams.id),
           name: $stateParams.name
        };

        vm.getMembersByGroup = function(groupId){
            vm.membersPromise = MembersService.getMembersByGroup(groupId).$promise.then(function (data) {
                vm.members = data;
            })
        };
        vm.getMembersByGroup(vm.group.id);

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
            $state.go('settlement', {id: vm.group.id});
        };

        vm.goToGroupSettings = function(){

        }

    }
})();
