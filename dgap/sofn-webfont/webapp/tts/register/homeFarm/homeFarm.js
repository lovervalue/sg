var myapp = angular.module("myapp", []).controller("homeFarm", function ($scope, $http, $state) {
    $.fn.mycity("selectedSiteProvince","selectedSiteCity","selectedSiteArea");
    inits($scope);
    var third_json = window.localStorage.getItem("tts_register_thirds");
    if(third_json == null || third_json.length <= 0){

    }else {
        var thirds_json = JSON.parse(third_json);

        if(thirds_json.area1){
            $('#selectedSiteProvince')
                .find('option[value='+thirds_json.area1+']')
                .prop('selected', true)
                .end()
                .val(thirds_json.area1).change();
        }
        if(thirds_json.area2){
            $('#selectedSiteCity')
                .find('option[value='+thirds_json.area2+']')
                .prop('selected', true)
                .end()
                .val(thirds_json.area2).change();
        }
        if(thirds_json.area3){
            $('#selectedSiteArea')
                .find('option[value='+thirds_json.area3+']')
                .prop('selected', true)
                .end()
                .val(thirds_json.area3).change();
        }
            $scope.realName = thirds_json.realName;
            $scope.enterpriseName = thirds_json.enterpriseName;
            $scope.annualOutput = thirds_json.annualOutput;
            $scope.selectedSiteUnit = thirds_json.unit;
            $scope.address = thirds_json.address
            $scope.legalName = thirds_json.legalName;
            $scope.legalIdNumber = thirds_json.idcode
            $scope.legalIdNumber = thirds_json.legalIdnumber;
            $scope.legalPhone = thirds_json.legalPhone
            $scope.contactName = thirds_json.contactName;
            $scope.contactPhone = thirds_json.contactPhone;
            $scope.contactEmail = thirds_json.contactEmail;
            $scope.faxNumber = thirds_json.faxNumber;
            $scope.lat = thirds_json.latitude;
            $scope.lon = thirds_json.longitude;
            $scope.cardType = thirds_json.cardType;
            $scope.lonLatName = thirds_json.lonLatName;

        if(thirds_json.legalImages1!=null &&thirds_json.legalImages1!=''){
            $scope.img33 = thirds_json.legalImages1;
            $("#img33").attr("isUpload", "Y");
        }
        if(thirds_json.legalImages2!=null &&thirds_json.legalImages2!=''){
            $scope.img34 = thirds_json.legalImages2;
            $("#img34").attr("isUpload", "Y");
        }
        if(thirds_json.legalImages3!=null &&thirds_json.legalImages3!=''){
            $scope.img35 = thirds_json.legalImages3;
            $("#img35").attr("isUpload", "Y");
        }
    }

    //???????????????????????????????????????
    window.queryByIdCode = function () {
        if (null != $("#idCode").val() && $("#idCode").val().length == 18) {
            $http({
                url: "/sofn-tts-web/ttsScltxxcjRegiter/queryByIdCode",
                data: $scope.idCode,
                method: "post",
                //post????????????   ?????????
                headers: {
                    post: {'Content-Type': 'application/x-www-form-urlencoded'}
                }
            }).success(function (data) {
                if (data.isExists == "Y") {
                    $scope.flag = true;
                    isExist = "Y";
                    return false;
                } else {
                    $scope.flag = false;
                    isExist = "N";
                }
            })
        }
    }

    $scope.homeFarmReNextStep=function () {


        var thirds = {

            "realName": $scope.contactName,
            "enterpriseName": $scope.enterpriseName,
            "annualOutput": $scope.annualOutput,
            "unit": $("#selectedSiteUnit").val(),
            "unitName": $("#selectedSiteUnit option:selected").text(),
            "address": $scope.address,
            "legalName":$scope.contactName,
            "idcode": $scope.legalIdNumber,
            "legalIdnumber": $scope.legalIdNumber,
            "legalPhone":$scope.contactPhone,
            "contactName":$scope.contactName,
            "contactPhone": $scope.contactPhone,
            "contactEmail":$scope.contactEmail,
            "faxNumber":$scope.faxNumber,
            "latitude":$("#lat").val(),
            "longitude":$("#lon").val(),
            "cardType":$("input[name='cardType']:checked").val(),
            "legalImages1": $("#img33").attr("src"),
            "legalImages2": $("#img34").attr("src"),
            "legalImages3": $("#img35").attr("src"),
            "documentImages1": "",
            "documentImages2": "",
            "lonLatName":$("input[name='lonLatName']").val(),

            "area1": $("#selectedSiteProvince option:selected").val(),
            "areaname1": $("#selectedSiteProvince option:selected").text(),

            "area2": $("#selectedSiteCity option:selected").val(),
            "areaname2": $("#selectedSiteCity option:selected").text(),

            "area3": $("#selectedSiteArea option:selected").val(),
            "areaname3": $("#selectedSiteArea option:selected").text(),
        };
        thirds = JSON.stringify(thirds);
        var third_json = window.localStorage.setItem("tts_register_thirds",thirds);
        $state.go("tts/register/secondChoose/secondChoose");
    };

    function inits($scope) {
        var tts_register_second = $.parseJSON(window.localStorage.getItem("tts_register_second"));
        var id = tts_register_second.entityIndustry;
        $http({
            url: "/sofn-tts-web/ttsScltxxcjRegiter/getUnits",
            data: id,
            method: "post",
            //post????????????   ?????????
            headers: {
                post: {'Content-Type': 'application/x-www-form-urlencoded'}
            }
        }).success(function (data) {
            $scope.units=data.data.units;
        })

    };
    //????????????
    $scope.homeFarmNextStep = function () {
        //????????????
        if (!$("#attributeForm").data('bootstrapValidator').validate().isValid()) {
            alert("??????????????????!");
            return false;
        }
        if($("#lon").val()==undefined||$("#lon").val()==null||$("#lon").val()==""||$("#lat").val()==undefined||$("#lat").val()==null||$("#lat").val()=="" ){
            jBox.tip("???????????????", 'info');
            return false;
        }
        //??????????????????
        if ( $("#img33").attr("isUpload") == "N" || $("#img34").attr("isUpload") == "N" || $("#img35").attr("isUpload") == "N" ) {
            alert("??????????????????????????????,????????????,??????!")
            return false;
        }
        if (isExist == "Y") {
            alert("??????????????????????????????,?????????????????????!")
            return false;
        }
        if(!$("#selectedSiteArea option:selected").val()){
            alert("?????????????????????????????????");
            return false;
        }
        var third = {
            "realName": $scope.contactName,
            "enterpriseName": $scope.enterpriseName,
            "annualOutput": $scope.annualOutput,
            "unit": $("#selectedSiteUnit").val(),
            "unitName": $("#selectedSiteUnit option:selected").text(),
            "area": $("#selectedSiteArea option:selected").val(),
            "address": $scope.address,
            "legalName":$scope.contactName,
            "idcode": $scope.legalIdNumber,
            "legalIdnumber": $scope.legalIdNumber,
            "legalPhone":$scope.contactPhone,
            "contactName":$scope.contactName,
            "contactPhone": $scope.contactPhone,
            "contactEmail":$scope.contactEmail,
            "faxNumber":$scope.faxNumber,
            "latitude":$("#lat").val(),
            "longitude":$("#lon").val(),
            "cardType":$("input[name='cardType']:checked").val(),
            "legalImages":$("#img33").attr("src")+","+$("#img34").attr("src")+","+$("#img35").attr("src"),
            "documentImages":"",
            "businessLicenceimg" : "",
            "organizationCertificateimg" : "",
            "positiveIdcardeimg" : $("#img33").attr("src"),
            "negativeIdcardimg" : $("#img34").attr("src"),
            "handIdcardimg" : $("#img35").attr("src")
        };
        var first = $.parseJSON(window.localStorage.getItem("tts_register_first"));
        var second = $.parseJSON (window.localStorage.getItem("tts_register_second"));
        var third = third;
        var params = {};
        $.extend(params, first, second, third);
        //????????????????????????
        $http({
            url: "/sofn-tts-web/ttsScltxxcjRegiter/applyRecord",
            data: params,
            method: "post",
            //post????????????   ?????????
            headers: {
                post: {'Content-Type': 'application/x-www-form-urlencoded'}
            }
        }).success(function (data) {
            if(data.httpCode == "200"){
                $state.go("tts/register/success/success");
                //????????????
                window.localStorage.removeItem("tts_register_first");
                window.localStorage.removeItem("tts_register_second");
                window.localStorage.removeItem("tts_register_thirds");
            }else{
                $state.go("login/ttsLogin");
                jBox.tip("????????????");
            }
        })
    };


    //?????????
    $('#attributeForm').bootstrapValidator();//?????????

    //modal ??????
    $scope.myModal31=function(){
        $("#myModal31").modal("show");
    }
    $scope.myModal32=function(){
        $("#myModal32").modal("show");
    }
    //modal ??????
    $scope.myModal33=function(){
        $("#myModal33").modal("show");
    }
    $scope.myModal34=function(){
        $("#myModal34").modal("show");
    }
    //modal ??????
    $scope.myModal35=function(){
        $("#myModal35").modal("show");
    }

    //????????????===================================================================
    $scope.LunboName = "";
    $scope.uploadUrl = "/sofn-tts-web/ttsScltxxcjRegiter/upload";
    $("#file3-1").fileinput({
        language: 'zh',
        uploadUrl: $scope.uploadUrl,
        overwriteInitial: false,
        maxFileSize: 5300,
        allowedFileExtensions: ['jpg', 'jpeg', 'bmp', 'png'],
        maxFilesNum: 10,
        enctype: 'multipart/form-data',
        maxFileCount: 1, //?????????????????????????????????????????????
        msgFilesTooMany: "????????????{m}????????????",
        msgSizeTooLarge: "???????????????5MB???????????????",
        dropZoneTitle: '????????????'
    });
    //var response = [];
    $("#file3-1").on("fileuploaded", function (event, data, previewId, index) {
        if (data && data['response']) {
            $scope.response = data.response.path;
            $("#img31").attr("src", "http://" + data.response.path);
            $("#img31").attr("isUpload","Y");
            console.log($scope.response);
        }
        return;
    });

    //?????????????????????===================================================================
    $("#file3-2").fileinput({
        language: 'zh',
        uploadUrl: $scope.uploadUrl,
        overwriteInitial: false,
        maxFileSize: 5300,
        allowedFileExtensions: ['jpg', 'jpeg', 'bmp', 'png'],
        maxFilesNum: 10,
        enctype: 'multipart/form-data',
        maxFileCount: 1, //?????????????????????????????????????????????
        msgFilesTooMany: "????????????{m}????????????",
        msgSizeTooLarge: "???????????????5MB???????????????",
        dropZoneTitle: '?????????????????????'
    });
    //var response = [];
    $("#file3-2").on("fileuploaded", function (event, data, previewId, index) {
        if (data && data['response']) {
            $scope.response = data.response.path;
            $("#img32").attr("src", "http://" + data.response.path);
            $("#img32").attr("isUpload","Y");
            console.log($scope.response);
        }
        return;
    });


    //???????????????===================================================================
    $("#file3-3").fileinput({
        language: 'zh',
        uploadUrl: $scope.uploadUrl,
        overwriteInitial: false,
        maxFileSize: 5300,
        allowedFileExtensions: ['jpg', 'jpeg', 'bmp', 'png'],
        maxFilesNum: 10,
        enctype: 'multipart/form-data',
        maxFileCount: 1, //?????????????????????????????????????????????
        msgFilesTooMany: "????????????{m}????????????",
        msgSizeTooLarge: "???????????????5MB???????????????",
        dropZoneTitle: '???????????????'
    });
    //var response = [];
    $("#file3-3").on("fileuploaded", function (event, data, previewId, index) {
        if (data && data['response']) {
            $scope.response = data.response.path;
            $("#img33").attr("src", "http://" + data.response.path);
            $("#img33").attr("isUpload","Y");
            console.log($scope.response);
        }
        return;
    });

    //???????????????===================================================================
    $("#file3-4").fileinput({
        language: 'zh',
        uploadUrl: $scope.uploadUrl,
        overwriteInitial: false,
        maxFileSize: 5300,
        allowedFileExtensions: ['jpg', 'jpeg', 'bmp', 'png'],
        maxFilesNum: 10,
        enctype: 'multipart/form-data',
        maxFileCount: 1, //?????????????????????????????????????????????
        msgFilesTooMany: "????????????{m}????????????",
        msgSizeTooLarge: "???????????????5MB???????????????",
        dropZoneTitle: '???????????????'
    });
    //var response = [];
    $("#file3-4").on("fileuploaded", function (event, data, previewId, index) {
        if (data && data['response']) {
            $scope.response = data.response.path;
            $("#img34").attr("src", "http://" + data.response.path);
            $("#img34").attr("isUpload","Y");
            console.log($scope.response);
        }
        return;
    });
    //?????????===================================================================
    $("#file3-5").fileinput({
        language: 'zh',
        uploadUrl: $scope.uploadUrl,
        overwriteInitial: false,
        maxFileSize: 5300,
        allowedFileExtensions: ['jpg', 'jpeg', 'bmp', 'png'],
        maxFilesNum: 10,
        enctype: 'multipart/form-data',
        maxFileCount: 1, //?????????????????????????????????????????????
        msgFilesTooMany: "????????????{m}????????????",
        msgSizeTooLarge: "???????????????5MB???????????????",
        dropZoneTitle: '???????????????'
    });
    //var response = [];
    $("#file3-5").on("fileuploaded", function (event, data, previewId, index) {
        if (data && data['response']) {
            $scope.response = data.response.path;
            $("#img35").attr("src","http://" +  data.response.path);
            $("#img35").attr("isUpload","Y");
            console.log($scope.response);
        }
        return;
    });
});

