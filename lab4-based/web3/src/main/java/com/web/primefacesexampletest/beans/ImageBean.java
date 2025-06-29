package com.web.primefacesexampletest.beans;

import com.web.primefacesexampletest.database.Request;
import com.web.primefacesexampletest.database.RequestsRepository;
import com.web.primefacesexampletest.dto.Point;
import com.web.primefacesexampletest.dto.RawPoint;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Named("imageBean")
public class ImageBean {

    @Inject
    private RequestsRepository requestRepository;

    @Inject
    private PointConverter pointConverter;

    public static final int width = 500;

    public static final int height = 500;

    private StreamedContent image;

    private int index = 0;

    private Color[] colors = new Color[]{Color.BLACK, Color.BLUE, Color.CYAN, Color.RED, Color.GREEN};

    @PostConstruct
    public void init() {
        redraw(1f);
    }

    public void drawBackground(Graphics2D g2d) {
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/background.jpg")));
            g2d.drawImage(image, 0, 0, width, height, null);
        } catch (Exception e) {
            System.out.printf("Не получилось %s: %s\n", e.getClass(), e.getMessage());
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
        }
    }

    public void drawGraph(Graphics2D g2d, Float floatR) {
//        g2d.setColor(Color.WHITE);
//        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.BLUE);
        g2d.drawLine(width/2, 0, width/2, height);
        g2d.drawLine(0, height/2, width, height/2);


        Integer r = pointConverter.getPixelsR(floatR);



        g2d.fillRect(
                width/2-r,
                height/2-r/2,
                r,
                r/2
        );

        g2d.fillArc(
                width/2-r,
                height/2-r,
                2*r,
                2*r,
                180,
                90
        );

        g2d.fillPolygon(
                new int[]{width/2, width/2, width/2 + r},
                new int[]{height/2, r/2+height/2, height/2},
                3
        );


    }

    public void drawPoints(Graphics2D g2d, Float r) {
        g2d.setColor(Color.WHITE);
        requestRepository.findAllHitAndR(r)
                .map(request->new Point(request.getX(), request.getY()))
                .map(pointConverter::getRawPoint)
                .forEach(p -> drawPoint(g2d, p));
    }

    public List<Request> findAll() {
        return requestRepository.findAll().toList();
    }

    public void drawPoint(Graphics2D g2d, RawPoint point) {
        g2d.fillArc(point.getX(), point.getY(), 5, 5, 0, 360);
    }

    public StreamedContent pack(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageData = baos.toByteArray();

        // Step 3: Convert the byte array to StreamedContent
        InputStream inputStream = new ByteArrayInputStream(imageData);
        return DefaultStreamedContent.builder()
                .contentType("image/png")
                .stream(() -> inputStream)
                .build();
    }

    public StreamedContent redraw(Float r) {
        try {
            index = (index + 1)%colors.length;

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedImage.createGraphics();

            drawBackground(g2d);
            drawGraph(g2d, r);
            drawPoints(g2d, r);
            g2d.dispose();

            return pack(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public StreamedContent getImage() {
        return image;
    }

    public void setImage(StreamedContent image) {
        this.image = image;
    }

}

