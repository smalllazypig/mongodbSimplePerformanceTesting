$( document ).ready(function() {
	var readInterval;
	var writeInterval;
    $('#readstart').click(function() {
    	$.ajax({
    		  url: "handle.action",
    		  type: "POST",
    		  data: {
    		    readthreads: $('#readthreads').val(), command: 'read'
    		  },
    		  success: function( data ) {
    			  readInterval = setInterval(function(){getReadInfo();},1000);
    		    $( "#readresult" ).text( "" + data + "" );
    		  }
    		});
    });
    
    function getReadInfo() {
    	$.ajax({
  		  url: "handle.action",
  		  type: "POST",
  		  data: {
  		    command: 'readInfo'
  		  },
  		  success: function( data ) {
  		    $( "#readresult" ).text( "" + data + "" );
  		  }
  		});
    }
    
    $('#readstop').click(function() {
    	$.ajax({
    		  url: "handle.action",
    		  type: "POST",
    		  data: {
    		    command: 'readstop'
    		  },
    		  success: function( data ) {
    			if(readInterval != null) {
    				clearInterval(readInterval);
    			}
    		    //$( "#readresult" ).text( "" + data + "" );
    		  }
    		});
    });
    $('#writestart').click(function() {
    	$.ajax({
    		  url: "handle.action",
    		  type: "POST",
    		  data: {
    		    writethreads: $('#writethreads').val(), command: 'write'
    		  },
    		  success: function( data ) {
    			  writeInterval = setInterval(function(){getWriteInfo();},1000);
    		    $( "#writeresult" ).text( "" + data + "" );
    		  }
    		});
    });
    
    function getWriteInfo() {
    	$.ajax({
  		  url: "handle.action",
  		  type: "POST",
  		  data: {
  		    command: 'writeInfo'
  		  },
  		  success: function( data ) {
  		    $( "#writeresult" ).text( "" + data + "" );
  		  }
  		});
    }
    
    $('#writestop').click(function() {
    	$.ajax({
    		  url: "handle.action",
    		  type: "POST",
    		  data: {
    		    command: 'writestop'
    		  },
    		  success: function( data ) {
    			  if(writeInterval != null) {
      				clearInterval(writeInterval);
      			}
    		    //$( "#writeresult" ).text( "" + data + "" );
    		  }
    		});
    });
});