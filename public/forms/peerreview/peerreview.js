angular.module('SelfReport', ['schemaForm'])
    .controller('SelfReportController', SelfReportController);

SelfReportController.$inject = ['$http'];
function SelfReportController($http){
    var vm = this;
    vm.schema = {};
    vm.model = {};
    vm.form = {};

    $http.get('/assets/forms/peerreview/schema.json').success(function(data) {
        vm.schema = data;
    });

    $http.get('/assets/forms/peerreview/form.json').success(function(data) {
        vm.form = data;
    });
}