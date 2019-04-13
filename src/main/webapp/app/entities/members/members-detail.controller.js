(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('MembersDetailController', MembersDetailController);

    MembersDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Members', 'Groups'];

    function MembersDetailController($scope, $rootScope, $stateParams, previousState, entity, Members, Groups) {
        var vm = this;

        vm.members = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('easySettleApp:membersUpdate', function(event, result) {
            vm.members = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
