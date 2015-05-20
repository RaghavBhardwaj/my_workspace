<html>
 <body>
<script type="text/javascript">
function checkLoc(){
var loc=window.location.href;
var loc1=window.location.href;
var str = loc1.substring(loc1.indexOf("documentlibrary"));

var substr1=loc.split("/");
var substr3=substr1.slice(5);
 


window.open('http://localhost:8084/alfresco/service/getChildren?foldername='+substr3[1]+'&'+str,'servicepage','toolbar=0,titlebar=0,addressbar=0,top=500, left=500, width=400, height=400');

}

</script>
<button onclick="checkLoc()">UPLOAD OCR</button>
</body>     
</html>