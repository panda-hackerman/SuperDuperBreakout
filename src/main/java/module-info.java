module gov.superswag.superduperbreakout {
  requires javafx.controls;
  requires javafx.fxml;

  opens gov.superswag.superduperbreakout to javafx.fxml;
  exports gov.superswag.superduperbreakout;
  exports gov.superswag.superduperbreakout.game_objects;
  opens gov.superswag.superduperbreakout.game_objects to javafx.fxml;
  exports gov.superswag.superduperbreakout.util;
  opens gov.superswag.superduperbreakout.util to javafx.fxml;

}