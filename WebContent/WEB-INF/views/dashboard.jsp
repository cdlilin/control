<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en"> 
    <!-- 3rd party -->
    <script src="<c:url value='/scripts/lib/jquery/jquery.js'/>"></script> 
    <script src="<c:url value='/scripts/lib/sockjs/sockjs.js'/>"></script>
    <script src="<c:url value='/scripts/lib/stomp/lib/stomp.min.js'/>"></script> 
    <!-- application --> 
    <script type="text/javascript">
      (function() {
        var socket = new SockJS('/control/portfolio');
       var stompClient = Stomp.over(socket);    
       
        stompClient.connect({}, function(frame) {  
            console.log('Connected ' + frame);
            //self.username(frame.headers['user-name']);

            stompClient.subscribe("/topic/price.stock.Train", function(message) {
            	alert(message); 
        	});
        }); 
       
      })();
      
      function dosomething(){ 
    	  $.get("dashboard.do",function(){ alert(1)});
      }; 
      
      function b(x, y, a) {
    	    arguments[2] = 10;
    	    alert(a);
    	}
    	b(1, 2, 3);
    </script>

  </body> 
  <input type="button" class="test" onclick="dosomething()">
</html>
