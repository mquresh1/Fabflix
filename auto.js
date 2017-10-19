/**
 * Javascript file defining functions for retrieving auto complete titles
 */

jQuery(function(){
	$(function(){
        $("#navbarsearchinput").autocomplete({
        	source: function(request, response) {
        		$.ajax({
        			url: "/Project2/servlet/AutoComplete",
        			dataType: "json",
        			data: {
        				term: request.term
        			},
        			success: function(data) {
        				response(data);
        			}
        		});
        	},
        	minLength: 2
        });
	});
});

