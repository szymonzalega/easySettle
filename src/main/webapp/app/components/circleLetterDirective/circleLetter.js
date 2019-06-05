angular.module('easySettleApp')
    .directive('circleLetter', function() {
        return {
            restrict: 'E',
            scope: {
                text: '=',
                size: '=',
                background: '='
            },
            templateUrl: 'app/components/circleLetterDirective/circleLetter.html',
            link: function (scope) {
                function getFirstLetter(word) {
                    let arr = word.split('');
                    return arr[0].toUpperCase();
                }

                scope.firstLetter = getFirstLetter(scope.text);
            }
        };
    });
