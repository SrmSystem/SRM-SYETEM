function sumits()
{
	$("#formsd").submit();
}
function changt(id)
{
	$("#changT").val(id);
}
function changs(id)
{
	if($("#changS").val()=="")
	{
		$("#changS").val(id);
	}
	else
	{
		$("#changS").val($("#changS").val()+","+id);
	}
	
}
function changa(id)
{
	if($("#changA").val()=="")
	{
		$("#changA").val(id);
	}
	else
	{
		$("#changA").val($("#changA").val()+","+id);
	}
	
}