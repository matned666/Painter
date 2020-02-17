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
import java.util.*;

public class PainterController {


    @FXML
    public Button starTool;
    @FXML
    public Button multiplyTool1;
    @FXML
    public Button pointTool;
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

    private boolean dragMe;

    public PainterController() {
    }

    public void initialize(){
        currentTool = Tool.LINE;
        multiply = false;
        dragMe = false;
        shapeList = new LinkedList<>();
        fillColorTool.setValue(Color.RED);
        strokeColorTool.setValue(Color.BLACK);


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

            if(dragMe){
                startX = event.getX();
                startY = event.getY();
            }
//            System.out.printf("MDragged: actual: x=%f, y=%f \n",endX,endY);
            prepareShape();
            if (multiply || dragMe) applyShape();
            refreshCanvas();
        });
    }

    private void applyShape() {
        shapeList.add(currentShape);
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
                    dragMe = false;
                    return new Line(startX, startY, endX, endY);
                case RECTANGLE:
                    dragMe = false;
                    return new Rectangle(startX, startY, endX, endY);
                case TRIANGLE:
                    dragMe = false;
                    return new Triangle(startX, startY, endX, endY);
                case CIRCLE:
                    dragMe = false;
                    return new Circle(startX, startY, endX, endY);
                case ELLIPSE:
                    dragMe = false;
                    return new Ellipse(startX, startY, endX, endY);
                case STAR:
                    dragMe = false;
                    return new Star(startX, startY, endX, endY);
                case POINT:
                    dragMe = true;
                    return new Point(startX, startY);
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
        }else if(source ==starTool){
            currentTool = Tool.STAR;
        }else if(source ==pointTool){
            currentTool = Tool.POINT;
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
        List<String[]> temp = new LinkedList<>();
        if (file != null) {
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            if(sc.hasNextLine())temp.add(sc.nextLine().split(";"));
            else temp.add(sc.next().split(";"));
          }
        }
        readFile(temp);
    }

    private void readFile(List<String[]> list) {
        for (String[] savedElement : list) {
            StringBuilder fillTemp = new StringBuilder();
            StringBuilder strokeTemp = new StringBuilder();
            for(int i = 2; i < 8; i++) {
                fillTemp.append(savedElement[5].charAt(i));
                strokeTemp.append(savedElement[6].charAt(i));
            }
            savedElement[5] = fillTemp.toString();
            savedElement[6] = strokeTemp.toString();
            if (savedElement[0].equalsIgnoreCase("line")) currentTool = Tool.LINE;
            else if (savedElement[0].equalsIgnoreCase("rectangle")) currentTool = Tool.RECTANGLE;
            else if (savedElement[0].equalsIgnoreCase("triangle")) currentTool = Tool.TRIANGLE;
            else if (savedElement[0].equalsIgnoreCase("circle")) currentTool = Tool.CIRCLE;
            else if (savedElement[0].equalsIgnoreCase("ellipse")) currentTool = Tool.ELLIPSE;
            else if (savedElement[0].equalsIgnoreCase("star")) currentTool = Tool.STAR;
            else if (savedElement[0].equalsIgnoreCase("point")) currentTool = Tool.POINT;
            startX = Double.parseDouble(savedElement[1]);
            startY = Double.parseDouble(savedElement[2]);
            endX = Double.parseDouble(savedElement[3]);
            endY = Double.parseDouble(savedElement[4]);
            currentShape = shape();
            currentShape.setStrokeColor(Color.valueOf(savedElement[6]));
            currentShape.setFillColor(Color.valueOf(savedElement[5]));
            applyShape();
            refreshCanvas();
        }
    }
}
