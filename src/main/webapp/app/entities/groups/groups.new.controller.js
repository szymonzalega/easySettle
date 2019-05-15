(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('CreateGroupController', CreateGroupController);

    CreateGroupController.$inject = ['GroupsService', '$state'];

    function CreateGroupController(GroupsService, $state) {

        let vm = this;
        vm.xx = "Xx";
    }
})();
