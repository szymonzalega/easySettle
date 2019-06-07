(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['Auth', '$rootScope', '$scope', 'Principal', 'LoginService', '$state'];

    function HomeController (Auth, $rootScope, $scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                if(vm.account){
                    $state.go('groups');
                }
            });
        }

        vm.goToLogin = function () {
            $state.go('login');
        };

        vm.goToRegister = function () {
            $state.go('register');
        };

    }
})();
