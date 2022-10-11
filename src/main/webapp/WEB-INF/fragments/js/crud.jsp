<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript" >
    function onUpdate(id){
        var url = "/${appName }/${context}/update/${pk}/"+id;
        window.location = url;
    }


    function onInsert(){
        var url = "/${appName }/${context}/insert";
        window.location=  url;
    }

    function setDeleteItems(id){
        $("#delId").val(id);
    }

    // 	function onDelete(id){
    // 		window.location="../${context}/delete/${pk}/"+$("#delId").val();
    // 	}

    function onDelete(id){
        swal({
                title: '<spring:message code="msg.confirm_delete"/>  ${pageHeader}',
//				  text: '',
                type: "warning",
                cancelButtonText: '<spring:message code="btn.cancel"/>',
                showCancelButton: true,
                confirmButtonText: '<spring:message code="btn.confirm"/>',
                closeOnConfirm: true
            },
            function(){
                var url = "../${appName }/${context}/delete/${pk}/"+id;
                $.ajax({
                    url: url,
// 				    type: 'DELETE',
                    complete: function(xhr, textStatus) {
// 				        console.log(xhr.status);
                        if(xhr.status == 200){
// 				        	datatable.ajax.reload( null, false);
                            location.reload();
                        }else{
                            //alert("Can't delete : code "+xhr.status)
                            console.log("Can't delete : code "+xhr.status);
                        }
                    }
                });
            });
    }


</script>