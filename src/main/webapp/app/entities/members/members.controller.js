(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('MembersController', MembersController);

    MembersController.$inject = ['Members'];

    function MembersController(Members) {

        var vm = this;

        vm.members = [];

        loadAll();

        function loadAll() {
            Members.query(function(result) {
                vm.members = result;
                vm.searchQuery = null;
            });
        }
    }
})();
