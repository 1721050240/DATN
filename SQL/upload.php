<?php  
include "connect.php"

$target_dir = "images/";  
$query = "select max(id) as id from sanphammoi";
$data = mysqli_query($conn, $query);
$response = array();
while ($row = mysqli_fetch_assoc($data)) {
     $result[] = ($row);

}
if ($result['id'] == null) {
   $name = 1;
}else{
   $name = ++$result['id'];
}
$target_file_name = $target_dir .$name. ".jpg";



if (isset($_FILES["file"]))  
   {  
   if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file_name))  
      {  
         $arr = [
      'success' => true,
      'message' => "OK", 
            ];
      }  
   else  
      {  
         $arr = [
      'success' => true,
      'message' => "Thanh cong 2", 
            ]; 
      }  
   }  
else  
   {  
         $arr = [
      'success' => true,
      'message' => "Lỗi", 
      ];
   }  

   echo json_encode($arr);  
?>  