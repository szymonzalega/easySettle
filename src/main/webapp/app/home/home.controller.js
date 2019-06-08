(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['JhiLanguageService', '$translate', 'tmhDynamicLocale', 'Auth', '$rootScope', '$scope', 'Principal', 'LoginService', '$state'];

    function HomeController (JhiLanguageService, $translate, tmhDynamicLocale, Auth, $rootScope, $scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        $rootScope.logged = false;
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

        vm.languages = [];
        JhiLanguageService.getAll().then(function (languages) {
            angular.forEach(languages, function (item) {
                var lang = {
                    name: item.toUpperCase(),
                    value: item
                };
                vm.languages.push(lang);
            })
        });

        vm.settedLang = tmhDynamicLocale.get();


        vm.changeLanguage = function (languageKey) {
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
            vm.settedLang = languageKey;
        }

    }
})();
