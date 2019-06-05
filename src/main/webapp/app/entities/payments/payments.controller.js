(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('PaymentsController', PaymentsController);

    PaymentsController.$inject = ['$state', 'GroupsService', 'PaymentsService', '$stateParams'];

    function PaymentsController($state, GroupsService, PaymentsService, $stateParams) {

        var vm = this;

        vm.groupParams = $stateParams;

        vm.payments = [];

        vm.getAllPayments = function () {
            var obj = {
                id: parseInt(vm.groupId)
            };
            vm.paymentsPromise = PaymentsService.getAllPayments(vm.groupParams.id).$promise.then(function (data) {
                vm.payments = data;
            })
        };
        vm.getAllPayments();

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

        vm.goToPaymentDetails = function (payment) {

        };

        vm.removePayment = function(payment){

        };

        vm.goToAddNewPayment = function(){
            $state.go('payments.new', vm.groupParams);
        };

    }
})();
