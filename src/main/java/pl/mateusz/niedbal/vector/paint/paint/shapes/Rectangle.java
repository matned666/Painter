package pl.mateusz.niedbal.vector.paint.paint.shapes;

import javafx.scene.canvas.GraphicsContext;

public class Rectangle extends Shape {

    private  double startX;
    private  double startY;
    private  double endX;
    private  double endY;

    public Rectangle(double startX, double startY, double endX, double endY) {
        this.startX = Math.min(startX,endX);
        this.startY = Math.min(startY,endY);
        this.endX = Math.abs(startX-endX);
        this.endY = Math.abs(startY-endY);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.fillRect(startX,startY,endX,endY);
        context.strokeRect(startX,startY,endX,endY);
    }

    @Override
    public String getData() {
        StringBuilder builder = new StringBuilder();
        builder.append("Rectangle;");
        builder.append(startX).append(";");
        builder.append(startY).append(";");
        builder.append(endX).append(";");
        builder.append(endY).append(";");
        builder.append(fillColor).append(";");
        builder.append(strokeColor).append(";");
        return builder.toString();
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }
}
