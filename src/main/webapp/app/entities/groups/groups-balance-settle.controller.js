(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsBalanceSettleController', GroupsController);

    GroupsController.$inject = ['MembersService', '$stateParams', 'GroupsService', '$state'];

    function GroupsController(MembersService, $stateParams, GroupsService, $state) {

        let vm = this;

        vm.groupParams = $stateParams;

        vm.settled = [];
        function settleDebts() {
            vm.settledPromise = MembersService.settleDebt(vm.groupParams.id).$promise.then(function (data) {
                vm.settled = data;
            }).catch(function (error) {
                console.log(error);
            })
        }
        settleDebts();

        function downloadGroupDetails(id) {
            vm.groupPromise = GroupsService.getOneGroup(id).$promise.then(function (data) {
                vm.groupParams.name = data.name;
            }).catch(function (error) {
                console.log(error);
            })
        }

        function getGroupDetails(groupInfo){
            if(!groupInfo.name){
                downloadGroupDetails(groupInfo.id);
            }
        }
        getGroupDetails(vm.groupParams);

    }
})();
