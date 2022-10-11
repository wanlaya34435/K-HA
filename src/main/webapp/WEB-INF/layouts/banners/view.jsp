<%@ include file="../../common/taglibs.jsp"%>


<form:form method="POST" action="">
    <input type="hidden" id="delId" name="delId" />
    <div class="row">
        <div class="col-sm-9">
            <h3><spring:message code="form.list"/>${moduleName}</h3>
        </div>

        <jsp:include page="../../fragments/buttons/create.jsp" />
    </div>



    <!-- <FILTER> -->
    <jsp:include page="filter.jsp" />
    <!-- </FILTER> -->

    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">

                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div class="dataTable_wrapper">
                        <table id="tableView" class="table table-striped table-hover table-ellipsis" width="100%">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th><spring:message code="${context }.type"/></th>
                                    <th><spring:message code="${context }.link"/></th>
                                    <th><spring:message code="${context }.status"/></th>
                                    <th></th>
                                </tr>
                            </thead>
                        </table>
                    </div>

                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
        </div>
        <!-- /.col-lg-12 -->
    </div>
</form:form>

<!-- javascript for CRUD -->
<jsp:include page="../../fragments/js/crud.jsp" />

<script>
    $(document).ready(function(){
        $(".select2").select2({
            placeholder: '<spring:message code="resp.msg.choose" />',
            allowClear: true
        });

        loadDataTable();
    });

    function loadDataTable(){
        var __apiUrl__ 			= "/${appName }/${context}/datatable/";
        var __actionColumn__ 	= 4;
        var __updateUrl__ 		= '<a href="#" onclick="onUpdate(\'{$1}\')" class="has-tooltip" title="<spring:message code="btn.update"/>" ><em class="fa fa-lg fa-pencil-square-o text-info"></em></a>';
        var __deleteUrl__ 		= '<a href="#" onclick="onDelete(\'{$1}\')" class="has-tooltip" title="<spring:message code="btn.delete"/>"><em class="fa fa-lg fa-trash-o text-danger"></em></a>';

        var datatable = $('#tableView').DataTable({
            "language": {
                "url": "/${appName}/assets/datatable-language-th.json"
            },
            "pageLength": 25,
            "scrollX": true,
            "destroy": true,
            "processing": true,
            "serverSide": true,
            "aaSorting": [[0, "desc"]],
            "aoColumnDefs":[
                {
                    "aTargets": [ 0 ],
                    "sWidth": '40px',
                    "className": 'text-center',
                },
                {
                    "aTargets": [ __actionColumn__ ],
                    "sWidth": '80px',
                    "className": 'text-center',
                    "bSortable" : false,
                    "searchable": false,
                    "mData": function (rows) {
                        var navContent = [];
                        var updateUrl = __updateUrl__.replace("{$1}",rows.DT_RowData.pkey);
                        var deleteUrl = __deleteUrl__.replace("{$1}",rows.DT_RowData.pkey);

                        // update
                        navContent.push(updateUrl);
                        // delete
                        navContent.push(deleteUrl);

                        return navContent.join('&nbsp;&nbsp;');
                    }
                }
            ],
            "ajax": {
                "url": __apiUrl__ ,
                "data": function ( d ) {

                },
                "type": "GET",
                "dataSrc": function ( json ) {
                    return json.aaData;
                }
            }
        });
    }

    $('#btnFilter').on('click', function() {
        loadDataTable();
    });


</script>
