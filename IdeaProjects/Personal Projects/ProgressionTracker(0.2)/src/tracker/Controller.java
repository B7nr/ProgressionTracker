package tracker;

import application.StageManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.*;

public class Controller {

    @FXML private ComboBox exListBox;
    @FXML private HBox setHBox; //TODO IMPLEMENT SETS
    @FXML private TextField eField00;
    @FXML private Label eLabel00;
    @FXML private ChoiceBox tBox00;
    @FXML private Label tLabel00;
    @FXML private GridPane gridPaneBuilder;
    @FXML private ScrollPane scrollPane;
    private HBox exButHBox;
    private StageManager stageManager;
    private ArrayList<Label> exerciseLabels; // consider changing these to maps
    private ArrayList<TextField> exerciseFields;
    private ArrayList<Label> typeLabels;
    private ArrayList<ChoiceBox> typeBoxes;
    private ArrayList<Label> measurementLabels;
    private ArrayList<TextField> measurementFields;
    private ArrayList<TextField> repsFields;
    private ArrayList<Label> repsLabels;
    private ArrayList<File> workoutList;

    private final int START_ROW = 2; // Row of first Label
    private final int START_COL = 3; // Column of Exercise tab

    public Controller() {
        exerciseLabels = new ArrayList<>();
        exerciseFields = new ArrayList<>();
        typeLabels = new ArrayList<>();
        typeBoxes = new ArrayList<>();
        measurementLabels = new ArrayList<>();
        measurementFields = new ArrayList<>();
        repsFields = new ArrayList<>();
        repsLabels = new ArrayList<>();
        workoutList = new ArrayList<>();
        exerciseFields.add(eField00);
        exerciseLabels.add(eLabel00);
        typeBoxes.add(tBox00);
        typeLabels.add(tLabel00);
    }

    @FXML protected void saveWorkout(ActionEvent e) {
        int fileNum = 0;
        File file = new File("workout" + fileNum);
        try {
            while (!file.createNewFile()) {
                fileNum++;
                file = new File("workout" + fileNum);
            }
            System.out.println("File Created");
            workoutList.add(file);
            int rowNum = 0;
            final char SPACE = 0;
            Writer out = new BufferedWriter(new FileWriter(file));
            while (rowNum < measurementFields.size()) {
                System.out.println(exerciseFields.get(rowNum));
                out.write(exerciseFields.get(rowNum).getText());
                out.write(SPACE);
                out.write((String) typeBoxes.get(rowNum).getValue());
                out.write(SPACE);
                out.write(measurementFields.get(rowNum).getText());
                out.write(SPACE);
                out.write(repsFields.get(rowNum).getText());
                out.write("\n");
                rowNum++;
            }
            out.close();
        } catch (IOException error) {
            System.out.println("Something went wrong with file");
        }

        System.out.println("workout saved");
    }

    @FXML protected void selectWorkout( ActionEvent e) {
        exListBox.getItems().clear(); // TODO probably really slow will probably want to fix this
        Collection<File> workouts = workoutList;
        exListBox.getItems().addAll(workouts);
    }

    @FXML protected void createSet(ActionEvent e) {
        int rowNum = parseSource(e.getSource().toString());
        Label setLabel = new Label("Set: ");
        GridPane.setColumnIndex(setLabel, 1);
        GridPane.setRowIndex(setLabel, rowNum + 2);
        gridPaneBuilder.getChildren().add(setLabel);
        createRow(rowNum);
        gridPaneBuilder.getChildren().remove(setHBox);
    }

    // adds the Measurement column, Reps Column, and the add new Exercise button
    @FXML private void finishRow(ActionEvent e) {
        int rowNum = parseSource(e.getSource().toString());
        typeBoxes.set(rowNum, (ChoiceBox) e.getSource());
        if (rowNum == 0)
            exerciseFields.set(rowNum, eField00);
        System.out.println(exerciseFields);
        if (rowNum < 0) throw new IllegalStateException("rowNum is a negative number");
        addMeasureColumn(rowNum, (ChoiceBox) e.getSource());
        if (repsLabels.size() - 1 <= rowNum && repsFields.size() - 1 <= rowNum)
            addRepsCol(rowNum);
        if (rowNum == measurementFields.size() - 1) {
            addNewExButton(rowNum);
           // addNewSetButton(rowNum);
        }
    }

    // parses the String for the first occurrence of a number
    private int parseSource(String id) {
        Scanner sc = new Scanner(id);
        sc.useDelimiter("");
        while (sc.hasNext()) {
            if (sc.hasNextInt()) {
                sc.useDelimiter(" ");
                return sc.nextInt();
            }
            sc.next();
        }
        return -1;
    }

    // adds the measurement label and field to the application
    private void addMeasureColumn(int fieldNum, ChoiceBox current) {
        if (stageManager == null)
            stageManager = new StageManager();
        String measureType = (String) current.getValue();
        final int FIELD_COLUMN = START_COL + 2;
        final int FIELD_ROW = (START_ROW + 1)  + (fieldNum * 2);
        addMeasureLabel(fieldNum, FIELD_ROW - 1, FIELD_COLUMN, measureType);
        addMeasureField(fieldNum, FIELD_ROW, FIELD_COLUMN, measureType);
        gridPaneBuilder.getChildren().addAll(measurementLabels.get(fieldNum), measurementFields.get(fieldNum));
        stageManager.showStage();
    }

    // dynamically changes the label depending on Type of Exercise
    private void addMeasureLabel(int labelNum, int row, int col, String measureType) {
        if (labelNum < measurementLabels.size()) {
            gridPaneBuilder.getChildren().remove(measurementLabels.get(labelNum));
            measurementLabels.set(labelNum, null);
        }
        if (measureType.equals("Interval"))
            if (labelNum < measurementLabels.size())
                measurementLabels.set(labelNum, new Label("Interval: "));
            else
                measurementLabels.add(new Label("Interval: "));
        else if (measureType.equals("Threshold"))
            if (labelNum < measurementLabels.size())
                measurementLabels.set(labelNum, new Label("Threshold: "));
            else
                measurementLabels.add(new Label("Threshold: "));
        measurementLabels.get(labelNum).setId("mLabel" + labelNum);
        GridPane.setColumnIndex(measurementLabels.get(labelNum), col);
        GridPane.setRowIndex(measurementLabels.get(labelNum), row);
    }

    // dynamically changes the field depending on type of Exercise
    private void addMeasureField(int fieldNum, int row, int col, String measureType) {
        if (fieldNum < measurementFields.size()) {
            gridPaneBuilder.getChildren().remove(measurementFields.get(fieldNum));
            measurementFields.set(fieldNum, new TextField());
        } else {
            measurementFields.add(new TextField());
        }
            measurementFields.get(fieldNum).setId("mField" + fieldNum);
        if (measureType.equals("Interval"))
            measurementFields.get(fieldNum).setText("hr:min:sec");
        GridPane.setColumnIndex(measurementFields.get(fieldNum), col);
        GridPane.setRowIndex(measurementFields.get(fieldNum), row);
    }

    // dynamically adds the reps field
    private void addRepsCol(int fieldNum) {
        if (stageManager == null)
            stageManager = new StageManager();
        final int FIELD_COLUMN = START_COL + 3;
        final int FIELD_ROW = (START_ROW + 1) + (fieldNum * 2);
        addRepField(FIELD_ROW, FIELD_COLUMN, fieldNum);
        addRepLabel(FIELD_ROW - 1, FIELD_COLUMN, fieldNum);
        gridPaneBuilder.getChildren().addAll(repsFields.get(fieldNum), repsLabels.get(fieldNum));
        stageManager.showStage();
    }

    // adds the rep label above the rep field
    private void addRepLabel(int row, int col, int fieldNum) {
        if (fieldNum < repsLabels.size())
            repsLabels.set(fieldNum, new Label("Reps: "));
        else
            repsLabels.add(new Label("Reps: "));
        repsLabels.get(fieldNum).setId("rLabel" + fieldNum);
        GridPane.setColumnIndex(repsLabels.get(fieldNum), col);
        GridPane.setRowIndex(repsLabels.get(fieldNum), row);
    }

    // adds the rep field
    private void addRepField(int row, int col, int fieldNum) {
        if (fieldNum < repsFields.size())
            repsFields.set(fieldNum, new TextField());
        else
            repsFields.add(new TextField());
        repsFields.get(fieldNum).setId("rField" + fieldNum);
        GridPane.setColumnIndex(repsFields.get(fieldNum), col);
        GridPane.setRowIndex(repsFields.get(fieldNum), row);
    }

    // adds the new exercise button
    private void addNewExButton(int buttonNum) {
        final int BUTTON_COLUMN = START_COL + 3;
        final int BUTTON_ROW = (START_ROW + 2) + (buttonNum * 2);
        final double SPACING = 10.0;
        exButHBox = new HBox();
        exButHBox.setSpacing(SPACING);
        GridPane.setColumnIndex(exButHBox, BUTTON_COLUMN);
        GridPane.setRowIndex(exButHBox, BUTTON_ROW);
        Button exButton = new Button();
        exButton.setText("New Exercise");
        exButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addNewRow(buttonNum);
            }
        });
        exButHBox.getChildren().add(exButton);
        gridPaneBuilder.getChildren().add(exButHBox);
    }

//    private void addNewSetButton(int rowNum) {
//        final int BUTTON_ROW = (START_ROW + 2) + (rowNum * 2);
//        final double SPACING = 10.0;
//        setHBox = new HBox();
//        setHBox.setSpacing(SPACING);
//        GridPane.setColumnIndex(setHBox, START_COL);
//        GridPane.setRowIndex(setHBox, BUTTON_ROW);
//        Button setButton = new Button();
//        setButton.setText("New Set");
//        setButton.setId("sButton" + (BUTTON_ROW));
//        setButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                gridPane.getChildren().remove(setHBox);
//                createSet(event);
//            }
//        });
//        setHBox.getChildren().add(setButton);
//        gridPane.getChildren().add(setHBox);
//    }


    // creates a new row when the new exercise button is pressed
    // pre: rowNum > 0
    private void addNewRow(int rowNum) {
        if (rowNum < 0) throw new IllegalArgumentException("rowNum is a negative value.");
        gridPaneBuilder.getChildren().remove(exButHBox); // deletes the old button
        createRow(rowNum + 1);
    }

    // creates the exercise column and the type column
    private void createRow(int rowNum) {
        addExerciseCol(rowNum);
        addTypeCol(rowNum);
    }

    // adds an exercise column to the row
    // pre: stageManager != null
    private void addExerciseCol(int exNum) {
        if (stageManager == null)
            stageManager = new StageManager();
        final int FIELD_COLUMN = START_COL;
        final int FIELD_ROW = (START_ROW + 1) + (exNum * 2);
        addExerciseLabel(FIELD_ROW - 1, FIELD_COLUMN, exNum);
        addExerciseField(FIELD_ROW, FIELD_COLUMN, exNum);
        gridPaneBuilder.getChildren().addAll(exerciseLabels.get(exNum), exerciseFields.get(exNum));
        stageManager.showStage();
    }

    // adds an exercise label
    private void addExerciseLabel(int row, int col, int labelNum) {
        if (labelNum < exerciseLabels.size())
            exerciseLabels.set(labelNum, new Label("Exercise"));
        else
            exerciseLabels.add(new Label("Exercise"));
        exerciseLabels.get(labelNum).setId("eLabel" + labelNum);
        GridPane.setColumnIndex(exerciseLabels.get(labelNum), col);
        GridPane.setRowIndex(exerciseLabels.get(labelNum), row);
    }

    // adds an exercise field
    private void addExerciseField(int row, int col, int fieldNum) {
        if (fieldNum < exerciseFields.size())
            exerciseFields.set(fieldNum, new TextField());
        else
        exerciseFields.add(new TextField());
        exerciseFields.get(fieldNum).setId("eField" + fieldNum);
        GridPane.setColumnIndex(exerciseFields.get(fieldNum), col);
        GridPane.setRowIndex(exerciseFields.get(fieldNum), row);
    }

    // adds a type column
    // pre: stageManager != null
    private void addTypeCol(int typeNum) {
        if (stageManager == null)
            stageManager = new StageManager();
        final int BOX_COLUMN = START_COL + 1;
        final int BOX_ROW = (START_ROW + 1) + (typeNum * 2);
        addTypeLabel(BOX_ROW - 1, BOX_COLUMN, typeNum);
        addTypeBox(BOX_ROW, BOX_COLUMN, typeNum);
        gridPaneBuilder.getChildren().addAll(typeLabels.get(typeNum), typeBoxes.get(typeNum));
        stageManager.showStage();
    }

    // adds a type label to the column
    private void addTypeLabel(int row, int col, int labelNum) {
        if (labelNum < typeLabels.size())
            typeLabels.set(labelNum, new Label("Type: "));
        else
            typeLabels.add(new Label("Type: "));
        typeLabels.get(labelNum).setId("tLabel" + labelNum);
        GridPane.setColumnIndex(typeLabels.get(labelNum), col);
        GridPane.setRowIndex(typeLabels.get(labelNum), row);
    }

    // adds a type choice box to the column
    private void addTypeBox(int row, int col, int boxNum) {
        if (boxNum < typeBoxes.size())
            typeBoxes.set(boxNum, new ChoiceBox());
        else
            typeBoxes.add(new ChoiceBox());
        typeBoxes.get(boxNum).getItems().addAll("Interval", "Threshold"); //TODO is this an issue
        typeBoxes.get(boxNum).setId("tBox" + boxNum);
        typeBoxes.get(boxNum).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                finishRow(event);
            }
        });
        GridPane.setColumnIndex(typeBoxes.get(boxNum), col);
        GridPane.setRowIndex(typeBoxes.get(boxNum), row);
    }


}
