import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;

public class ImagesResizer implements Runnable {
    private File[] files;
    private double scaleFactor;
    private String dstFolder;

    public ImagesResizer(File[] files, double scaleFactor, String dstFolder){
        this.dstFolder = dstFolder;
        this.scaleFactor = scaleFactor;
        this.files = files;
    }

    @Override
    public void run(){
        try {
            long start = System.currentTimeMillis();
            for (File file : files) {
                if(file == null) continue;

                BufferedImage image = ImageIO.read(file);
                if (image == null || scaleFactor >= 1.0) {
                    continue;
                }

                int width = (int)Math.round(scaleFactor * image.getWidth(null));
                int height = (int)Math.round(scaleFactor * image.getHeight(null));

                BufferedImage newImage = Scalr.resize(image,
                        Scalr.Method.QUALITY,
                        Scalr.Mode.FIT_EXACT,
                        width,
                        height,
                        (BufferedImageOp[]) null);

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);
            }
            System.out.println("Duration in thread: " + (System.currentTimeMillis() - start));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
