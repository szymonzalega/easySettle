(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('GroupsController', GroupsController);

    GroupsController.$inject = ['Groups'];

    function GroupsController(Groups) {

        var vm = this;

        vm.groups = [];

        loadAll();

        function loadAll() {
            Groups.query(function(result) {
                vm.groups = result;
                vm.searchQuery = null;
            });
        }
    }
})();
