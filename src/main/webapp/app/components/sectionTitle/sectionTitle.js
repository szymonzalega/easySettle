angular.module('easySettleApp')
    .directive('sectionTitle', function() {
        return {
            restrict: 'E',
            scope: {
                upper: '=',
                down: '='
            },
            templateUrl: 'app/components/sectionTitle/sectionTitle.html',
            link: function (scope) {

            }
        };
    });
