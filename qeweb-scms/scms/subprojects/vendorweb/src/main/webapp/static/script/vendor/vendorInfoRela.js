function survey(type)
{
	if(type==1)
	{
		$.post(ctx+"/manager/vendor/vendorInfor/getSurvey",{"orgid":$("#orgid").val(),"vendorId":$("#vendorId").val()},function(data){
			$("#survey").html(data);
		},"text");
	}
	else
	{
		$.post(ctx+"/manager/vendor/vendorInfor/getSurvey",{"orgid":$("#orgid").val(),"type":type,"vendorId":$("#vendorId").val()},function(data){
			$("#survey").html(data);
		},"text");
	}
}
function report()
{
	$.post(ctx+"/manager/vendor/vendorInfor/getReport",{"vendorId":$("#vendorId").val()},function(data){
		$("#report").html(data);
	},"text");
}
function overhaul()
{
	$.post(ctx+"/manager/vendor/vendorInfor/getOverhaul",{"vendorId":$("#vendorId").val()},function(data){
		$("#overhaul").html(data);
	},"text");
}
function extended()
{
	$.post(ctx+"/manager/vendor/vendorInfor/getExtended",{"orgId":$("#orgIdsss").val()},function(data){
		$("#extended").html(data);
	},"text");
}
