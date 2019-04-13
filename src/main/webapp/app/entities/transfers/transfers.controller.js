(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('TransfersController', TransfersController);

    TransfersController.$inject = ['Transfers'];

    function TransfersController(Transfers) {

        var vm = this;

        vm.transfers = [];

        loadAll();

        function loadAll() {
            Transfers.query(function(result) {
                vm.transfers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
