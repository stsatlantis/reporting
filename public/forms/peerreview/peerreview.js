angular.module('SelfReport', ['schemaForm'])
    .controller('SelfReportController', SelfReportController);

SelfReportController.$inject = ['$http'];
function SelfReportController($http) {
    var vm = this;
    vm.schema = {};
    vm.form = {};

    vm.saveReview = saveReview;
    vm.initForm = initForm;

    function initForm(id, name) {
        vm.model = {};
        vm.model.name = name;
        vm.model.id = id;
        console.log(vm.model);
    }

    function saveReview() {
        console.log(vm.model);
    }

    $http.get('/assets/forms/peerreview/schema.json').success(function (data) {
        vm.schema = data;
    });

    $http.get('/assets/forms/peerreview/form.json').success(function (data) {
        vm.form = data;
    });
}