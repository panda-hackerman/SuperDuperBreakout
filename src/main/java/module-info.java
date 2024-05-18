module gov.superswag.superduperbreakout {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.media;
  requires org.jetbrains.annotations;

  opens gov.superswag.superduperbreakout to javafx.fxml;
  exports gov.superswag.superduperbreakout;
  opens gov.superswag.superduperbreakout.gameobjects to javafx.fxml;
  exports gov.superswag.superduperbreakout.gameobjects;
  opens gov.superswag.superduperbreakout.util to javafx.fxml;
  exports gov.superswag.superduperbreakout.util;
  opens gov.superswag.superduperbreakout.controller to javafx.fxml;
  exports gov.superswag.superduperbreakout.controller;
  opens gov.superswag.superduperbreakout.leaderboard to javafx.fxml;
  exports gov.superswag.superduperbreakout.leaderboard;


}