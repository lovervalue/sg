var myapp = angular.module("myapp", []);
angular
    .module("myapp", [])
    .controller("qryDailyEnforceLawList_new", function($scope, $http, $state) {
        var area = "";
        var jibie = "";
        $http({
            url: "/sofn-qry-web/subject/findCurrentUserByToken",
            method: "post",
            dataType: "json",
            headers: {
                token: window.localStorage.getItem("token")
            }
        })
            .success(function(data) {
                area = data.user.regionId;
                jibie = data.user.orgLevel;
                if (jibie == 1) {
                    area = 0;
                }
                if (jibie == 2) {
                    area = area.substr(0, 2);
                }
                if (jibie == 3) {
                    area = area.substr(0.4);
                }
                if (jibie == 4) {
                    area = area.substring(0, 6);
                }
                $.fn.mycity("sheng", "shi", "xian", area);
                //$.fn.mycity("sheng","shi","xian",$scope.organization.regionId);
                if (
                    $("#sheng").val() != null &&
                    $("#sheng").val() != "" &&
                    $("#sheng").val() != undefined
                ) {
                    $("#sheng")
                        .attr("disabled", true)
                        .parent()
                        .addClass("disabled");
                }
                if (
                    $("#shi").val() != null &&
                    $("#shi").val() != "" &&
                    $("#shi").val() != undefined
                ) {
                    $("#shi")
                        .attr("disabled", true)
                        .parent()
                        .addClass("disabled");
                }
                if (
                    $("#xian").val() != null &&
                    $("#xian").val() != "" &&
                    $("#xian").val() != undefined
                ) {
                    $("#xian")
                        .attr("disabled", true)
                        .end()
                        .parent()
                        .addClass("disabled");
                }
                //??????????????????????????????????????????????????????????????????
                $("input[name='entityIndustryLimit']:first").attr(
                    "checked",
                    true
                );
                // $("input[name='entityScaleLimit']:first").attr("checked",true);
                $("input[name='entityTypeLimit']:first").attr("checked", true);
                loadGrid();
            })
            .error(function(data) {
                console.log(data.httpCode);
                
            });
        //????????????????????????datatable
        $("#ToCheck").click(function() {
            $("#grid")
                .dataTable()
                .api()
                .ajax.reload();
        });
        //??????

        //????????????????????????
        $scope.industryType = [];
        $http({
            url: "/sofn-qry-web/sysDict/getSysDictByCode",
            data: 2,
            method: "post"
        })
            .success(function(data) {
                $scope.industryType = JSON.parse(data.data);
            })
            .error(function(data) {
                console.log(data);
                
            });

        //??????????????????????????????
        $scope.entityType = [];
        $http({
            url: "/sofn-qry-web/sysDict/getSysDictByCode",
            data: 31,
            method: "post"
        })
            .success(function(data) {
                $scope.entityType = JSON.parse(data.data);
            })
            .error(function(data) {
                console.log(data);
                
            });
        function formatParams(str) {
            return str == undefined ? "" : str;
        }
        //bootstrap??????
        function loadGrid() {
            $("#grid").dataTable({
                ajax: {
                    url:
                        "/sofn-qry-web/dailyEnforceLaw/getDailyEnforceLawList_two",
                    //dataSrc : "rows", // ??????????????????????????????
                    dataSrc: function(json) {
                        json.recordsTotal = json.data.total; //?????????
                        json.recordsFiltered = json.data.total;
                        json.pageNumber = json.data.pageNum; //??????
                        json.pageSize = json.data.pageSize; //????????????
                        json.start =
                            (json.data.pageNum - 1) * json.data.pageSize; //????????????
                        json.length = json.data.pageSize;
                        //console.log(json)
                        for (var i = 0; i < json.data.list.length; i++) {
                            if (
                                json.data.list[i].TASK_BEGIN_TIME != null &&
                                json.data.list[i].TASK_BEGIN_TIME != "" &&
                                json.data.list[i].TASK_BEGIN_TIME != undefined
                            ) {
                                json.data.list[i].TASK_BEGIN_TIME = new Date(
                                    parseInt(
                                        json.data.list[i].TASK_BEGIN_TIME
                                    ) + 28800000
                                )
                                    .toISOString()
                                    .slice(0, 10);
                            }
                            if (
                                json.data.list[i].CREATE_TIME != null &&
                                json.data.list[i].CREATE_TIME != "" &&
                                json.data.list[i].CREATE_TIME != undefined
                            ) {
                                json.data.list[i].CREATE_TIME = new Date(
                                    parseInt(json.data.list[i].CREATE_TIME) +
                                        28800000
                                )
                                    .toISOString()
                                    .slice(0, 10);
                            }
                        }
                        $scope.$apply(function() {
                            $scope.tableTotal = json.data.total;
                        });
                        return json.data.list;
                    },
                    error: function(json) {
                        console.log(json);
                        
                    },
                    headers: {
                        token: window.localStorage.getItem("token")
                    },
                    data: function(params) {
                        // params.entityIndustry = $("select[name='entityIndustry']").val();
                        //????????????
                        var entityIndustry = $(
                            "input[name='entityIndustry']:checked"
                        );
                        var arr01 = new Array();
                        for (var i = 0; i < entityIndustry.length; i++) {
                            arr01.push($(entityIndustry[i]).val());
                        }
                        params.entityIndustry = arr01.toString();

                        //????????????
                        var entityType = $("input[name='entityType']:checked");
                        var arr02 = new Array();
                        for (var i = 0; i < entityType.length; i++) {
                            arr02.push($(entityType[i]).val());
                        }
                        params.entityType = arr02.toString();
                        params.xcPsersionId = window.localStorage.getItem(
                            "xcPsersionId",
                            "xcPsersionId"
                        );
                        params.taskYear = $("input[name='taskYear']").val();
                        params.queryCon = $("input[name='queryCon']").val();
                        params.dateBegin = $("input[name='datebegin']").val();
                        params.dateEnd = $("input[name='dateend']").val();
                        params.taskPersonName = formatParams(
                            $scope.taskPersonName
                        );
                        params.taskName = formatParams($scope.taskName);
                        params.enforceLawResult = $(
                            "select[name='enforceLawResult']"
                        ).val();
                        ObjShen = $("select[name='ObjShen']").val();
                        ObjShi = $("select[name='ObjShi']").val();
                        ObjXian = $("select[name='ObjXian']").val();
                        if (
                            ObjShen != "" &&
                            ObjShen != null &&
                            ObjShen != undefined
                        ) {
                            if (
                                ObjShi != "" &&
                                ObjShi != null &&
                                ObjShi != undefined
                            ) {
                                if (
                                    ObjXian != "" &&
                                    ObjXian != null &&
                                    ObjXian != undefined
                                ) {
                                    params.areaId = ObjXian;
                                } else {
                                    params.areaId = ObjShi;
                                }
                            } else {
                                params.areaId = ObjShen;
                            }
                        }
                    },
                    type: "POST"
                },
                columns: [
                    {
                        title: "??????",
                        data: function(data, type, row, meta) {
                            return meta.row + 1;
                        },
                        width: "4%"
                    },
                    {
                        data: "TASK_BEGIN_TIME",
                        title: "??????",
                        width: "4%",
                        render: function(data, type, row) {
                            // ????????????????????????
                            if (
                                data != null &&
                                data != "" &&
                                data != undefined
                            ) {
                                return data.substring(0, 4);
                            } else {
                                return "";
                            }
                        }
                    },
                    {
                        data: "TASK_NAME",
                        title: "????????????",
                        render: function(data, type, row) {
                            if (data.length > 10) {
                                // return "<a id='" + row.id + "' onmouseover=showPopContent('" + row.id + "','" + data + "')>" + data.substring(0, 10) + "...</a>";
                                return (
                                    "<a id='TASK_NAME" +
                                    row.ID +
                                    "' onmouseover=showPopContent('TASK_NAME" +
                                    row.ID +
                                    "','" +
                                    data +
                                    "')>" +
                                    data.substring(0, 10) +
                                    "...</a>"
                                );
                            } else {
                                return data;
                            }
                        }
                    },
                    {
                        data: "ENTITY_INDUSTRY_NAME",
                        title: "????????????"
                    },
                    {
                        data: "ENTERPRISE_NAME",
                        title: "???????????????"
                    },
                    {
                        data: "ENTITY_TYPE_NAME",
                        title: "????????????"
                    },
                    {
                        data: "AREA_ID",
                        title: "??????",
                        render: function(data, type, row) {
                            // ????????????????????????
                            if (
                                data != null &&
                                data != "" &&
                                data != undefined
                            ) {
                                return $.fn.Cityname(row.AREA_ID);
                            }
                        }
                    } /*
                {
                    data : "ENTERPRISE_ADDRESS",
                    title : "?????????????????????"
                },*/,
                    {
                        data: "TASK_BEGIN_TIME",
                        title: "????????????"
                    },
                    {
                        data: "CREATE_TIME",
                        title: "????????????"
                    },
                    {
                        data: "ENFORCE_LAW_RESULT_FLAG",
                        title: "????????????",
                        render: function(data, type, row) {
                            if (
                                data != null &&
                                data != "" &&
                                data != undefined
                            ) {
                                if ("1" == data) {
                                    return "??????";
                                } else if ("2" == data) {
                                    return "?????????";
                                } else if ("3" == data) {
                                    return "??????";
                                } else {
                                    return "";
                                }
                            }
                        }
                    },
                    {
                        data: "ID",
                        title: "??????",
                        width: "4%",
                        render: function(data, type, row, meta) {
                            // ????????????????????????
                            return (
                                '<a type="button" style=" padding-left: 0px;padding-right: 0px;margin-left: 0px;cursor:pointer"onclick="Checkreport(\'' +
                                meta.row +
                                "')\">??????</a>"
                            ); //????????????
                        }
                    }
                ]
            });
            /**
             * ?????????????????????????????????????????????
             * @param id
             * @param content
             */
            window.showPopContent = function(id, content) {
                $("#" + id).popover({
                    animation: true,
                    html: false,
                    placement: "right",
                    trigger: "hover",
                    content: content,
                    delay: 0,
                    container: "body"
                });
                $("#" + id).popover("show");
            };
            //window.Checkreport=function(obj){
            //    window.localStorage.setItem("ID",obj);
            //    $state.go("index.content.ales/etl/etlCheckreport")
        }
        /*    window.GotoUpdateWindow = function(data){
            window.localStorage.setItem("id",data);
            window.localStorage.setItem("ales_etl_updateFlag","1");
            $state.go("index.content.ales/etl/etlAddDailyEnforceLaw")
        }*/
        $scope.cleanSelectModelQuery = function() {
            $("input[name='queryCon']").val("");
            $("input[name='taskYear']").val("");
            $("input[name='datebegin']").val("");
            $("input[name='dateend']").val("");
            $("select[name='enforceLawResult']").val("");
            $("select[value='']").attr("selected", true);
            $("input[type='checkbox']:checked").attr("checked", false);
            $("input[type='checkbox']").attr("disabled", false);
            $.fn.mycity("sheng", "shi", "xian", area);
            $scope.taskName = null;
            $scope.taskPersonName = null;
            $("#grid")
                .dataTable()
                .api()
                .ajax.reload();
        };

        //??????????????????
        window.Checkreport = function(index) {
            var rows = $("#grid")
                .DataTable()
                .rows()
                .data();
            console.log(rows[index]);
            /* return false;*/
            $http({
                headers: {
                    token: window.localStorage.getItem("token")
                },
                url: "/sofn-qry-web/dailyEnforceLaw/getDailyEnforceLawById",
                method: "post",
                data: { id: "" }
            })
                .success(function(data) {
                    $scope.reCord = rows[index];
                    if (
                        $scope.reCord.AREA_ID != null &&
                        $scope.reCord.AREA_ID != "" &&
                        $scope.reCord.AREA_ID != undefined
                    ) {
                        $scope.areaId = $.fn.Cityname($scope.reCord.AREA_ID);
                    }
                    $("#myModal").modal("show");
                    //$scope.reCord=data.dailyEnforceLaw;
                    //if($scope.reCord.taskBeginTime!=null&&$scope.reCord.taskBeginTime!=""&&$scope.reCord.taskBeginTime!=undefined) {
                    //    $scope.beginTime = new Date(parseInt($scope.reCord.taskBeginTime)+28800000).toISOString().slice(0, 10);
                    //    $scope.endTime = new Date(parseInt($scope.reCord.taskEndTime)+28800000).toISOString().slice(0, 10);
                    //}
                    //if($scope.reCord.areaId!=null&&$scope.reCord.areaId!=""&&$scope.reCord.areaId!=undefined) {
                    //    $scope.areaId=$.fn.Cityname($scope.reCord.areaId);
                    //}
                })
                .error(function(data) {
                    console.log(data);
                    
                });
        };
        //    ??????
        /*   $scope.lawAdd=function(){
        window.localStorage.setItem("ales_etl_updateFlag","0")
    }*/

        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        $scope.limitCheckBox = function(checkBoxName, limitCheckBoxName) {
            for (
                var i = 0;
                i < $("input[name='" + limitCheckBoxName + "']").length;
                i++
            ) {
                var lim = $("input[name='" + limitCheckBoxName + "']")[i]
                    .checked;
                if (lim) {
                    $("input[name='" + checkBoxName + "']:checked").attr(
                        "checked",
                        false
                    );
                }
            }
        };

        //????????????
        $scope.exportData = function() {
            //????????????
            var entityIndustry = $("input[name='entityIndustry']:checked");
            var arr01 = new Array();
            for (var i = 0; i < entityIndustry.length; i++) {
                arr01.push($(entityIndustry[i]).val());
            }

            //????????????
            var entityType = $("input[name='entityType']:checked");
            var arr02 = new Array();
            for (var i = 0; i < entityType.length; i++) {
                arr02.push($(entityType[i]).val());
            }
            var entityIndustry = arr01.toString();
            var entityType = arr02.toString();
            var taskYear = $("input[name='taskYear']").val();
            var queryCon = $("input[name='queryCon']").val();
            var dateBegin = $("input[name='datebegin']").val();
            var dateEnd = $("input[name='dateend']").val();
            ObjShen = $("select[name='ObjShen']").val();
            ObjShi = $("select[name='ObjShi']").val();
            ObjXian = $("select[name='ObjXian']").val();
            var areaId = "";
            if (ObjShen != "" && ObjShen != null && ObjShen != undefined) {
                if (ObjShi != "" && ObjShi != null && ObjShi != undefined) {
                    if (
                        ObjXian != "" &&
                        ObjXian != null &&
                        ObjXian != undefined
                    ) {
                        var areaId = ObjXian;
                    } else {
                        var areaId = ObjShi;
                    }
                } else {
                    var areaId = ObjShen;
                }
            }
            //????????????
            var token = window.localStorage.getItem("token");
            //??????????????????
            var flag = 0; //????????????
            //form
            var form = $("<form>");
            form.attr("style", "display:none");
            form.attr("target", "");
            form.attr("method", "post");
            form.attr(
                "action",
                "/sofn-qry-web/dailyEnforceLaw/exportSubjSv?token=" +
                    token +
                    "&entityIndustry=" +
                    entityIndustry +
                    "&entityType=" +
                    entityType +
                    "&taskYear=" +
                    taskYear +
                    "&queryCon=" +
                    queryCon +
                    "&dateBegin=" +
                    dateBegin +
                    "&dateEnd=" +
                    dateEnd +
                    "&areaId=" +
                    areaId +
                    "&flag=" +
                    flag
            );
            $("body").append(form);
            form.submit();
        };
        /**************************
        CUSTOM QUERY CONDITION
    *************************/

        $scope.confirmCondition = function() {
            var modal = $("#modalCondition");
            modal
                .find(".gui-list li")
                .not(".disabled")
                .map(function() {
                    var key = $.trim($(this).attr("data-key"));
                    var checked = $(this)
                        .find(":checkbox")
                        .is(":checked");
                    if (!checked) {
                        var control = $(
                            '.gui-form-group .condition-custom[ng-show="visible' +
                                key +
                                '"]'
                        );
                        var controlType = control.attr("data-control");
                        switch (controlType) {
                            case "text":
                                $scope[key] = "";
                                break;
                            case "select":
                                $scope[key] = "";
                                control
                                    .find("option:first")
                                    .prop("selected", true)
                                    .trigger("change");
                                break;
                            case "checkbox":
                                $scope[key] = "";
                                control
                                    .find(":checkbox")
                                    .first()
                                    .prop("checked", true)
                                    .end()
                                    .siblings()
                                    .prop("checked", false);
                                break;
                            case "date":
                                control
                                    .find("input:text")
                                    .val("")
                                    .trigger("input");
                                break;
                            case "radio":
                                break;
                        }
                    }
                    $scope["visible" + key] = checked;
                    //???????????????????????????
                    var table = $("#svGrid").DataTable();
                    var column = table.column(key + ":name");
                    column.visible(checked);
                });
            modal.modal("hide");
        };
        $("#modalCondition")
            .on("change", ".condition-checked-all", function() {
                var checked = $(this).is(":checked");
                $("#modalCondition")
                    .find("li")
                    .not(".disabled")
                    .find(":checkbox")
                    .prop("checked", checked);
            })
            .on("change", ".gui-list :checkbox", function() {
                var unchecked = $(this)
                    .closest(".gui-list")
                    .find(":checkbox")
                    .filter(function() {
                        return !$(this).is(":checked");
                    });
                $(".condition-checked-all").prop(
                    "checked",
                    !!!unchecked.length
                );
            })
            .on("shown.bs.modal", function() {
                $(".gui-form-group .condition-custom").each(function() {
                    var visible = $(this).is(":visible");
                    var key = $(this)
                        .attr("ng-show")
                        .replace("visible", "");
                    $(".gui-list.list-condition")
                        .find('li[data-key="' + key + '"]')
                        .find(":checkbox")
                        .prop("checked", visible)
                        .trigger("change");
                });
            });
    });
