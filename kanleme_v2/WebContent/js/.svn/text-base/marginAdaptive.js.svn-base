function marginAdaptive(selector) {
    var height = $(window.top).height();
    var $wrapperBox = $(selector);
    var offsetTopNumber = 60;

    var offsetTop = (height / 2) - ($wrapperBox.height() / 2) - offsetTopNumber;

    $wrapperBox.css("margin-top", offsetTop + "px");

    $(window).resize(function () {
        height = $(window.top).height();
        offsetTop = (height / 2) - ($wrapperBox.height() / 2) - offsetTopNumber;

        $wrapperBox.css("margin-top", offsetTop + "px");
    });
}