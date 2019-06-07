(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('LogoutController', LogoutController);

    LogoutController.$inject = ['$rootScope', '$state', '$timeout', 'Auth'];


    function LogoutController ($rootScope, $state, $timeout, Auth) {
        var vm = this;

        function logout() {
            $rootScope.logged = false;
            Auth.logout();
            $state.go('home');
        }

        logout();
        
    }
})();
