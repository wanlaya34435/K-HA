<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="th">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" href="${__BASEURL__}/assets/plugins/images/icon.ico">
    <title><spring:message code="app.title"/></title>

    <!-- Bootstrap Core CSS -->
    <link href="${__BASEURL__}/assets/themes/sb-admin/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- MetisMenu CSS -->
    <link href="${__BASEURL__}/assets/themes/sb-admin/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">
    <!-- DataTables CSS -->
    <link href="${__BASEURL__}/assets/themes/sb-admin/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css" rel="stylesheet">
    <!-- DataTables Responsive CSS -->
    <%--  <link href="${__BASEURL__}/assets/themes/sb-admin/bower_components/datatables-responsive/css/dataTables.responsive.css" rel="stylesheet"> --%>
    <!-- Custom CSS -->
    <link href="${__BASEURL__}/assets/themes/sb-admin/dist/css/sb-admin-2.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="${__BASEURL__}/assets/themes/sb-admin/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">



    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- JQUERY UI -->
    <link rel="stylesheet" href="${__BASEURL__}/assets/plugins/jquery/jquery-ui.css">
    <script src="${__BASEURL__}/assets/plugins/jquery/jquery-ui.js"></script>

    <!-- DATE PICKER -->
    <script src="${__BASEURL__}/assets/plugins/js/bootstrap_datetimepicker/bootstrap-datepicker.min.js" charset="UTF-8"></script>
    <script src="${__BASEURL__}/assets/plugins/js/bootstrap_datetimepicker/bootstrap-datepicker.th.min.js" charset="UTF-8"></script>
    <link href="${__BASEURL__}/assets/plugins/css/bootstrap_datetimepicker/bootstrap-datepicker.min.css" rel="stylesheet" type="text/css">

    <!-- TREEGRID -->
    <link href="${__BASEURL__}/assets/plugins/css/jquery.treegrid/jquery.treegrid.css" rel="stylesheet" />

    <!-- SELECT2 -->
    <link href="${__BASEURL__}/assets/plugins/css/select2/select2.css" rel="stylesheet" />

    <!-- JSTREE -->
    <link href="${__BASEURL__}/assets/plugins/css/jstree/themes/default/style.min.css" rel="stylesheet"/>

    <!-- SWEET ALERT -->
    <link href="${__BASEURL__}/assets/plugins/js/sweetalert/sweetalert.css" rel="stylesheet"/>

    <!-- TAG EDITOR -->
    <link href="${__BASEURL__}/assets/plugins/js/tagEditor/jquery.tag-editor.css" rel="stylesheet"/>

    <style type="text/css">
        .required {
            color: #d9534f;
        }
        .cursor:hover {
            cursor: pointer;
        }
    </style>

</head>

<body class="pace-done">
<div id="wrapper">


    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">

        <!-- ############################################## -->
        <!-- HEADER -->
        <!-- ############################################## -->
        <tiles:insertAttribute name="header" />


        <!-- ############################################## -->
        <!-- MENU -->
        <!-- ############################################## -->
        <tiles:insertAttribute name="menu" />
    </nav>



    <!-- page-wrapper -->
    <div id="page-wrapper">
        <tiles:insertAttribute name="body" />

        <!-- 			<footer class="footer gray-bg"> -->
        <!-- 				<div class="row"> -->
        <!-- 					<div class="col-sm-12"> -->
        <%-- 						<tiles:insertAttribute name="footer" /> --%>
        <!-- 					</div> -->
        <!-- 				</div> -->
        <!-- 			</footer> -->
    </div>
    <!-- /#page-wrapper -->

</div>


<!-- jQuery -->
<script src="${__BASEURL__}/assets/themes/sb-admin/bower_components/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap Core JavaScript -->
<script src="${__BASEURL__}/assets/themes/sb-admin/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- Metis Menu Plugin JavaScript -->
<script src="${__BASEURL__}/assets/themes/sb-admin/bower_components/metisMenu/dist/metisMenu.min.js"></script>
<!-- DataTables JavaScript -->
<script src="${__BASEURL__}/assets/themes/sb-admin/bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${__BASEURL__}/assets/themes/sb-admin/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
<!-- Custom Theme JavaScript -->
<script src="${__BASEURL__}/assets/themes/sb-admin/dist/js/sb-admin-2.js"></script>
<script src="${__BASEURL__}/assets/plugins/js/validate/jquery.form-validator.min.js"></script>




<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="${__BASEURL__}/assets/plugins/js/metisMenu/metisMenu.min.js"></script>
<script src="${__BASEURL__}/assets/plugins/js/holder/holder.js"></script>
<script src="${__BASEURL__}/assets/plugins/js/validate/jquery.validate.min.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/validate/additional-methods.js" type="text/javascript"></script>

<script src="${__BASEURL__}/assets/plugins/js/formatDateTime/jquery.formatDateTime.min.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/redactor/redactor.min.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/price_format/jquery.price_format.2.0.min.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/iCheck/icheck.min.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/bootstrap_datetimepicker/moment.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/bootstrap_datetimepicker/th.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/bootstrap_datetimepicker/bootstrap-datetimepicker.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/bootstrap_daterangepicker/bootstrap-daterangepicker.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>

<!-- TREEGRID -->
<script src="${__BASEURL__}/assets/plugins/js/jquery.treegrid/jquery.treegrid.min.js"></script>

<!-- MULTI SELECT -->
<script src="${__BASEURL__}/assets/plugins/js/multiselect/multiselect.min.js"></script>

<!-- SELECT2 -->
<script src="${__BASEURL__}/assets/plugins/js/select2/select2.min.js"></script>

<!-- SWEET ALERT -->
<script src="${__BASEURL__}/assets/plugins/js/sweetalert/sweetalert.min.js"></script>

<!-- CONFIRM + TOOLTIPS -->
<script src="${__BASEURL__}/assets/plugins/js/confirm/bootstrap-tooltip.js" type="text/javascript"></script>
<script src="${__BASEURL__}/assets/plugins/js/confirm/bootstrap-confirmation.min.js" type="text/javascript"></script>

<!-- JSTREE -->
<script src="${__BASEURL__}/assets/plugins/js/jstree/jstree.js"></script>

<!-- TAG EDITOR -->
<script src="${__BASEURL__}/assets/plugins/js/tagEditor/jquery.caret.min.js"></script>
<script src="${__BASEURL__}/assets/plugins/js/tagEditor/jquery.tag-editor.min.js"></script>

<!-- Page-Level Demo Scripts - Tables - Use for reference -->
<script>

    $(document).ready(function() {
        if (typeof onLayoutReady !== "undefined") {
            // safe to use the function
            onLayoutReady();
        }

        var searchMsg = "<spring:message code='form.search'/>"
        $('#search').multiselect({
            sort: false,
            search: {
                left: '<input type="text" name="q" class="form-control" placeholder="'+searchMsg+'" />',
                right: '<input type="text" name="q" class="form-control" placeholder="'+searchMsg+'" />',
            }
        });
        $('#searchA').multiselect({
            sort: false,
            search: {
                left: '<input type="text" name="q" class="form-control" placeholder="'+searchMsg+'" />',
                right: '<input type="text" name="q" class="form-control" placeholder="'+searchMsg+'" />',
            }
        });

        /*==============================*/
        /* data table					*/
        /*==============================*/
        $('#dataTables-list').dataTable({
            responsive: true,
            "pageLength": 25,
            "language": {
                "decimal":        "",
                "emptyTable":     "<spring:message code='datatable.emptyTable'/>",
                "info":           "<spring:message code='datatable.info'/>",
                "infoEmpty":      "<spring:message code='datatable.infoEmpty'/>",
                "infoFiltered":   "<spring:message code='datatable.infoFiltered'/>",
                "infoPostFix":    "",
                "thousands":      ",",
                "lengthMenu":     "<spring:message code='datatable.lengthMenu'/>",
                "loadingRecords": "<spring:message code='datatable.loadingRecords'/>",
                "processing":     "<spring:message code='datatable.processing'/>",
                "search":         "<spring:message code='datatable.search'/>",
                "zeroRecords":    "<spring:message code='datatable.zeroRecords'/>",
                "paginate": {
                    "first":      "<spring:message code='datatable.first'/>",
                    "last":       "<spring:message code='datatable.last'/>",
                    "next":       "<spring:message code='datatable.next'/>",
                    "previous":   "<spring:message code='datatable.previous'/>",
                },
                "aria": {
                    "sortAscending":  "<spring:message code='datatable.sortAscending'/>",
                    "sortDescending": "<spring:message code='datatable.sortDescending'/>",
                }
            }
        });

        /*==============================*/
        /* flash message				*/
        /*==============================*/
        setTimeout(function() {
            $("#flash-area").fadeOut("slow", function() {
                $("#flash-area").remove();
            });
        }, 1000);
    });

    // validate field
    function validator(field){
        if(!field.val()) {
            field.closest('.form-group').removeClass('has-success').addClass('has-error');
            return false;
        } else {
            field.closest('.form-group').removeClass('has-error').addClass('has-success');
            return true;
        }
    }

    /*==============================*/
    /* form validate				*/
    /*==============================*/
    var validateLanguage = {
        errorTitle: '<spring:message code="validate.errorTitle"/>',
        requiredFields: '<spring:message code="validate.requiredFields"/>',
        badTime: '<spring:message code="validate.badTime"/>',
        badEmail: '<spring:message code="validate.badEmail"/>',
        badTelephone: '<spring:message code="validate.badTelephone"/>',
        badSecurityAnswer: 'You have not given a correct answer to the security question',
        badDate: 'You have not given a correct date',
        lengthBadStart: '<spring:message code="validate.lengthBadStart"/>',
        lengthBadEnd: '<spring:message code="validate.lengthBadEnd"/>',
        lengthTooLongStart: '<spring:message code="validate.lengthTooLongStart"/>',
        lengthTooShortStart: '<spring:message code="validate.lengthTooShortStart"/>',
        notConfirmed: '<spring:message code="validate.notConfirmed"/>',
        badDomain: 'Incorrect domain value',
        badUrl: '<spring:message code="validate.badUrl"/>',
        badCustomVal: '<spring:message code="validate.badCustomVal"/>',
        andSpaces: ' and spaces ',
        badInt: '<spring:message code="validate.badInt"/>',
        badSecurityNumber: 'Your social security number was incorrect',
        badUKVatAnswer: 'Incorrect UK VAT Number',
        badStrength: 'The password isn\'t strong enough',
        badNumberOfSelectedOptionsStart: 'You have to choose at least ',
        badNumberOfSelectedOptionsEnd: ' answers',
        badAlphaNumeric: '<spring:message code="validate.badAlphaNumeric"/>',
        badAlphaNumericExtra: '<spring:message code="validate.badAlphaNumericExtra"/>',
        wrongFileSize: '<spring:message code="validate.wrongFileSize"/>',
        wrongFileType: '<spring:message code="validate.wrongFileType"/>',
        groupCheckedRangeStart: 'Please choose between ',
        groupCheckedTooFewStart: 'Please choose at least ',
        groupCheckedTooManyStart: 'Please choose a maximum of ',
        groupCheckedEnd: ' item(s)',
        badCreditCard: 'The credit card number is not correct',
        badCVV: 'The CVV number was not correct',
        wrongFileDim : 'Incorrect image dimensions,',
        imageTooTall : 'the image can not be taller than',
        imageTooWide : 'the image can not be wider than',
        imageTooSmall : 'the image was too small',
        min : 'min',
        max : 'max',
        imageRatioNotAccepted : 'Image ratio is not accepted'
    };
    $.validate({
        language : validateLanguage,
    });


    $(function () {
//     	$(document).on('mouseover','.del', function(){
        $(".del").confirmation({
            title : '<h2><spring:message code="msg.confirm_delete" /></h2>',
            btnOkLabel : '<spring:message code="btn.confirm" />',
            btnCancelLabel : '<spring:message code="btn.cancel" />',
            popout : true,
            singleton : true,
            placement: "left"
        });
// 		});
    });


</script>


</body>
</html>
