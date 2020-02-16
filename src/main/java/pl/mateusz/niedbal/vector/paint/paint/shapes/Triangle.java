package pl.mateusz.niedbal.vector.paint.paint.shapes;

import javafx.scene.canvas.GraphicsContext;

public class Triangle extends Shape {

    private  double startX;
    private  double startY;
    private  double midX;
    private  double midY;
    private  double endX;
    private  double endY;

    public Triangle(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = startY;
        midX = startX+((endX-startX)/2);
        midY = endY;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.beginPath();
        context.lineTo(startX,startY);
        context.lineTo(midX,midY);
        context.lineTo(endX,endY);
        context.lineTo(startX,startY);

        context.closePath();
        context.stroke();
        context.fill();
    }

    @Override
    public String getData() {
        StringBuilder builder = new StringBuilder();
        builder.append("Triangle;");
        builder.append(startX).append(";");
        builder.append(startY).append(";");
        builder.append(midX).append(";");
        builder.append(midY).append(";");
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
