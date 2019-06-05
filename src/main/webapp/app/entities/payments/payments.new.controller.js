(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('PaymentsAddController', PaymentsController);

    PaymentsController.$inject = ['$state', 'GroupsService', 'PaymentsService', 'MembersService', '$stateParams'];

    function PaymentsController($state, GroupsService, PaymentsService, MembersService, $stateParams) {

        var vm = this;

        vm.groupParams = $stateParams;

        vm.payment = {};
        vm.memberList = [];
        vm.getMembersByGroup = function(groupId){
            vm.membersPromise = MembersService.getMembersByGroup(groupId).$promise.then(function (data) {
                vm.memberList = data;
                angular.forEach(vm.memberList, function (item) {
                    item.selected = false;
                })
            }).catch(function (error) {
                console.log(error);
            });
        };

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

        vm.getMembersByGroup(vm.groupParams.id);

        vm.selectPayer = function (payer) {
            vm.payment.selectedPayer = payer;
        };

        vm.unSelectPayer = function () {
            vm.payment.selectedPayer = null;
        };

        vm.selectLoaner = function (loaner) {
            loaner.selected = !loaner.selected;
        };

        function  getLoanerList(){
            var loanersArr = [];
            angular.forEach(vm.memberList, function (item) {
                if(item.selected === true){
                    loanersArr.push(item.id);
                }
            });
            return loanersArr;
        }

        vm.savePayment = function () {
            var params = {
                name: vm.payment.name,
                amount: parseInt(vm.payment.amount),
                date: new Date(),
                group_id: vm.groupParams.id,
                payer_id: vm.payment.selectedPayer.id,
                loanersList: getLoanerList()
            };
            vm.saving = PaymentsService.create(params).$promise.then(function (promise) {
                $state.go('groups.balance', vm.groupParams);
            }).catch(function (error) {
                console.log(error);
            })
        }

    }
})();
