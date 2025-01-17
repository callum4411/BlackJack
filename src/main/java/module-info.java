module com.example.callumaseprojects {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jdi;
    requires java.sql;
    requires java.desktop;


    opens com.example.callumaseprojects to javafx.fxml;
    exports com.example.callumaseprojects;

    opens login to javafx.fxml;
    exports login;

    opens UserInfo to javafx.fxml;
    exports UserInfo;

    opens ApplicationPortal to javafx.fxml;
    exports ApplicationPortal;

    opens StartLogin to javafx.fxml;
    exports StartLogin;

    opens AdminPage to javafx.fxml;
    exports AdminPage;

    opens UserPage to javafx.fxml;
    exports UserPage;

    opens quizApp to javafx.fxml;
    exports quizApp;

    opens blackjack to javafx.fxml;
    exports blackjack;

}