var items = [];
hideAddMobile();

$(document).ready(function () {
    hideAddMobile();

    $("#btnAdd").on("click", function () {
        var item = $("#txtItem").val();
        var date;
        if ($("#dtItem").val()) {
            date = new Date($("#dtItem").val());
        }
        addItem({"item": item, "deadline": date});

        resetInputFields();
    });

    $("#btnAddMobile").on("click", function () {
        showAddMobile();
    });

    $("#btnAddTodoMobile").on("click", function () {
        hideAddMobile();

        var item = $("#inputTodo").val();
        var date;
        if ($("#inputDate").val()) {
            date = new Date($("#inputDate").val());
        }
        addItemMobile({"item": item, "deadline": date});

        resetInputFieldsMobile();
    });

    $("#btnCancelTodoMobile").on("click", function () {
        hideAddMobile();
        resetInputFieldsMobile();
    });
});

// WIDE SCREEN -----------------------------------------------------------------

function addItem(item) {
    items.push(item);

    var div = $("<div class=\"panel panel-default col-lg-2\">");
    var panel = $("<div class=\"panel-body\">");
    var h3 = $("<h3>");
    var p = $("<p>");

    if (item.deadline !== undefined) {
        p.text(item.deadline.getDate() + "/" + item.deadline.getMonth() +
                "/" + item.deadline.getFullYear());
    }
    h3.text(item.item);

    panel.append(h3);
    panel.append(p);
    div.append(panel);

    $("#items").append(div);
}

function resetInputFields() {
    $("#txtItem").val("");
    $("#dtItem").val("");
}

// MOBILE SCREEN ---------------------------------------------------------------

function showAddMobile() {
    $("#btnAddMobile").hide();
    $("#inputTodoMobile").show();
}

function hideAddMobile() {
    $("#inputTodoMobile").hide();
    $("#btnAddMobile").show();
}

function addItemMobile(item) {
    items.push(item);

    var li = $("<li class=\"list-group-item\">");
    var h5 = $("<h4>");
    var p = $("<p>");

    if (item.deadline !== undefined) {
        p.text(item.deadline.getDate() + "/" + item.deadline.getMonth() +
                "/" + item.deadline.getFullYear());
    }
    h5.text(item.item);

    li.append(h5);
    li.append(p);

    $("#itemsMobile").append(li);

    showNotificationAfterSeconds("ToDo item " + item.item + " added", 10, "FF0000");
}

function resetInputFieldsMobile() {
    $("#inputTodo").val("");
    $("#inputDate").val("");
}

// NOTIFICATION ----------------------------------------------------------------

function showNotificationAfterSeconds(message, seconds, ledColor) {
    var now = new Date().getTime();
    var xSecondsFromNow = new Date(now + seconds * 1000);
    if (ledColor === undefined) {
        ledColor = "FFFFFF";
    }

    cordova.plugins.notification.local.schedule({
        text: message,
        at: xSecondsFromNow,
        led: ledColor,
        sound: null
    });
}


