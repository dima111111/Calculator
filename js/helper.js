$(document).ready(function() {
    $('.key').click(function() {
        $.ajax({
            url : 'Calculator',
            type: 'POST',
            data : getData($(this)),
            success : function(response) {
                $('[name="actionString"]').val(response.screen);
                $('[name="result"]').val(response.result);
            }
        });
    });
});
function getData (button) {
    let data;
    let type = button.attr('name');
    let value = button.attr('value');
    switch (type){
        case 'num':
            data = { num: value};
        break;
        case 'action':
            data = { action: value};
        break;
    }
    return data;
}
