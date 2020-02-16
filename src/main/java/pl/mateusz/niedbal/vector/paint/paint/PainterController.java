package pl.mateusz.niedbal.vector.paint.paint;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.mateusz.niedbal.vector.paint.paint.shapes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PainterController {


    @FXML
    private Button lineTool;
    @FXML
    private Button rectangleTool;
    @FXML
    private Button triangleTool;
    @FXML
    private Button circleTool;
    @FXML
    private Button ellipseTool;
    @FXML
    private ColorPicker fillColorTool;
    @FXML
    private ColorPicker strokeColorTool;
    @FXML
    private Canvas canvas;

    private boolean multiply;
    private LinkedList<Shape> shapeList;
    private Shape currentShape;
    private GraphicsContext context;
    private Tool currentTool;
    private  double startX;
    private  double startY;
    private  double endX;
    private  double endY;
    public PainterController() {
    }

    public void initialize(){
        multiply = false;
        shapeList = new LinkedList<>();
        fillColorTool.setValue(Color.RED);
        strokeColorTool.setValue(Color.BLACK);
        currentShape = new Line(startX, startY, endX, endY);

        System.out.println("Hello");
        refreshCanvas();
        canvas.setOnMousePressed(event -> {
            startX = event.getX();
            startY = event.getY();
//            System.out.printf("MPressed: start: x=%f, y=%f \n",startX,startY);
        });

        canvas.setOnMouseReleased(event -> {
            endX = event.getX();
            endY = event.getY();
//            System.out.printf("MReleased: end: x=%f, y=%f \n",endX,endY);
            prepareShape();
            applyShape();
            refreshCanvas();

        });

        canvas.setOnMouseDragged(event -> {
            endX = event.getX();
            endY = event.getY();
//            System.out.printf("MDragged: actual: x=%f, y=%f \n",endX,endY);
            prepareShape();
            if (multiply) applyShape();
            refreshCanvas();
        });
    }

    private void applyShape() {
        shapeList.add(currentShape);
//        currentShape =
    }

    private void prepareShape() {
        currentShape = shape();
        currentShape.setStrokeColor(strokeColorTool.getValue());
        currentShape.setFillColor(fillColorTool.getValue());


    }

    private Shape shape(){
            switch(currentTool) {
                default:
                case LINE:
                    return new Line(startX, startY, endX, endY);
                case RECTANGLE:
                    return new Rectangle(startX, startY, endX, endY);
                case TRIANGLE:
                    return new Triangle(startX, startY, endX, endY);
                case CIRCLE:
                    return new Circle(startX, startY, endX, endY);
                case ELLIPSE:
                    return new Ellipse(startX, startY, endX, endY);
            }
        }

    private void refreshCanvas() {
        context = canvas.getGraphicsContext2D();
        context.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
        context.setStroke(Color.BLACK);
        context.strokeRect(0,0,canvas.getWidth(), canvas.getHeight());
        for (Shape shape: shapeList){
            shape.drawShape(context);
        }
        if(currentShape!= null) {
            currentShape.drawShape(context);
        }

    }

    public  void changeTool(ActionEvent actionEvent){
        Object source = actionEvent.getSource();
        if(source == lineTool){
            currentTool = Tool.LINE;
        }else if(source == rectangleTool){
            currentTool = Tool.RECTANGLE;
        }else if(source == circleTool){
            currentTool = Tool.CIRCLE;
        }else if(source == ellipseTool){
            currentTool = Tool.ELLIPSE;
        }else if(source ==triangleTool){
            currentTool = Tool.TRIANGLE;
        }else{
            currentTool = Tool.NO_TOOL;
        }
    }

    public void newButtonAction() {
        context.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
        shapeList.clear();
    }

    public void multiplyAll() {
        multiply = !multiply;
    }

    public void saveAction() {
        Optional<String> reduce = shapeList.stream()
                .map(Shape::getData)
                .reduce((acc, text) -> acc + "\n" + text);
        if (reduce.isPresent()) {
            System.out.println(reduce.get());
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("YOLO files (*.yolo)", "*.yolo");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                saveTextToFile(reduce.get(), file);
            }
        }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadAction() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("YOLO files (*.yolo)", "*.yolo");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());
        String path;
        List<String> temp = new LinkedList<>();
        if (file != null) {
            path = file.getAbsolutePath();
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            temp.add(sc.nextLine());
          }
        }
        readFile(temp);
    }

    private void readFile(List<String> content) {
        List<String[]> list = new LinkedList<>();
        for (int i = 0; i < content.size(); i++) {
            System.out.println(content.get(i));
                list.add(content.get(i).split(";"));
               if(list.get(i)[0].equalsIgnoreCase("line")) currentTool = Tool.LINE;
               else if(list.get(i)[0].equalsIgnoreCase("rectangle")) currentTool = Tool.RECTANGLE;
               startX = Double.parseDouble(list.get(i)[1]);
               startY = Double.parseDouble(list.get(i)[2]);
               endX = Double.parseDouble(list.get(i)[3]);
               endY = Double.parseDouble(list.get(i)[4]);
               prepareShape();
            applyShape();
            refreshCanvas();

//                switch (list.get(i)[0]) {
//                    case "Line":
//                        currentShape =
//                                new Line(Double.parseDouble(list.get(i)[1]),Double.parseDouble(list.get(i)[2]),
//                                        Double.parseDouble(list.get(i)[3]),Double.parseDouble(list.get(i)[4]));
//                        break;
//
//                    case "Rectangle":
//                        currentShape =
//                                new Rectangle(Double.parseDouble(list.get(i)[1]),Double.parseDouble(list.get(i)[2]),
//                                        Double.parseDouble(list.get(i)[3]),Double.parseDouble(list.get(i)[4]));
//                        break;
//                    case "Triangle":
//                   currentShape =
//                                new Triangle(Double.parseDouble(list.get(i)[1]),Double.parseDouble(list.get(i)[2]),
//                                        Double.parseDouble(list.get(i)[5]),Double.parseDouble(list.get(i)[6]));
//                        break;
//                    case "Circle":
//                   currentShape =
//                                new Circle(Double.parseDouble(list.get(i)[1]),Double.parseDouble(list.get(i)[2]),
//                                        Double.parseDouble(list.get(i)[3]),Double.parseDouble(list.get(i)[4]));
//                        break;
//                    case "Ellipse":
//                   currentShape =
//                                new Ellipse(Double.parseDouble(list.get(i)[1]),Double.parseDouble(list.get(i)[2]),
//                                        Double.parseDouble(list.get(i)[3]),Double.parseDouble(list.get(i)[4]));
//                        break;
//                    default:
//                }
//            prepareShape();
        }
    }
}
