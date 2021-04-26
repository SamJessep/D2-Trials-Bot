package nz.trialsBot.views.helpers;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RewardsDrawer {
    private static List<BufferedImage> loadImagesFromUrls(List<String> urls) throws IOException {
        List<BufferedImage> res = new ArrayList<>();
        for (String url : urls) {
            res.add(ImageIO.read(new URL(url)));
        }
        return res;
    }

    private static int getSectionWidth(List<BufferedImage> imgs, int padding) {
        int w = 0;
        for (BufferedImage img : imgs) {
            w = w + padding + img.getWidth() + padding;
        }
        return w;
    }

    private static BufferedImage resizeImage(BufferedImage img, int w, int h, int imageType) {
        BufferedImage result = new BufferedImage(w, h, imageType);
        float currentH = img.getHeight();
        float currentW = img.getWidth();

        float ratiohw = currentH / currentW;
        float ratiowh = currentW / currentH;

        int endH;
        int endW;
        int endX = 0;
        int endY = 0;

        if (w * (w * ratiohw) < h * (h * ratiowh)) {
            endH = (int) (w * ratiohw);
            endW = w;
            endY = (h - endH) / 2;
        } else {
            endH = h;
            endW = (int) (h * ratiowh);
            endX = (w - endW) / 2;
        }
        result.getGraphics().drawImage(img, endX, endY, endW, endH, null);

        return trimImage(result);
    }

    public static void drawRow(List<BufferedImage> images, Graphics g, int x, int y) {
        for (BufferedImage img : images) {
            g.drawImage(img, x + 8, y, null);
            x = x + img.getWidth() + 8;
        }
    }

    public static void drawText(String text, int x, int y, Color color, Graphics g) {
        g.setColor(color);
        g.setFont(new Font("Neue Haas Grotesk Text Pro", Font.BOLD, 40));
        g.drawString(text, x, y);
    }

    public static void drawRectangle(int x, int y, int w, int h, Color fillColor, Color shaddowColor, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(shaddowColor);
        g2d.fillRoundRect(x + 9, y + 6, w, h, 70, 70);

        g2d.setColor(fillColor);
        g2d.fillRoundRect(x, y, w, h, 70, 70);
    }


    private static BufferedImage drawRewardsSection(RewardRow[] rewards, int padding) throws IOException {
        BufferedImage rewardsCanvas = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        Graphics g = rewardsCanvas.getGraphics();

        int nextY = 0;

        for (RewardRow row : rewards) {
            List<BufferedImage> images = loadImagesFromUrls(row.imgUrls);
            drawRow(images, g, padding, nextY + padding);
            drawText(row.title, getSectionWidth(images, padding) + padding, (images.get(0).getHeight() / 2) + nextY + padding, Color.white, g);
            nextY = nextY + images.get(0).getHeight() + padding;
        }
        return rewardsCanvas;
    }

    public static BufferedImage drawRewards(List<String> three, List<String> five, List<String> seven, List<String> flawless, String mapName) throws IOException, URISyntaxException {
        int padding = 8;
        //Setup & draw rewards section
        RewardRow[] rewardsMap = new RewardRow[]{
            new RewardRow("3 Wins", three),
            new RewardRow("5 Wins", five),
            new RewardRow("7 Wins", seven),
            new RewardRow("Flawless", flawless)
        };
        BufferedImage rewardsSection = trimImage(drawRewardsSection(rewardsMap, padding));
        BufferedImage backgroundCanvas = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        //Draw rewards card background
        drawRectangle(0, 0, rewardsSection.getWidth(), rewardsSection.getHeight(), new Color(20, 25, 30), new Color(11, 12, 16, 190), backgroundCanvas.getGraphics());
        //Draw rewards on card
        backgroundCanvas.getGraphics().drawImage(rewardsSection, padding * 3, padding * 3, null);
        //Load background map
        String mapFile = getBackground(mapName);
        rewardsSection = ImageIO.read(new File(mapFile));
        //Draw rewards on map
        rewardsSection.getGraphics().drawImage(resizeImage(trimImage(backgroundCanvas), 900, 900, BufferedImage.TYPE_INT_ARGB), 10, 10, null);
        return resizeImage(rewardsSection, 1920, 1080, BufferedImage.TYPE_INT_RGB);
    }

    private static BufferedImage trimImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int top = height / 2;
        int bottom = top;
        int left = width / 2;
        int right = left;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (image.getRGB(x, y) != 0) {
                    top = Math.min(top, y);
                    bottom = Math.max(bottom, y);
                    left = Math.min(left, x);
                    right = Math.max(right, x);
                }
            }
        }
        return image.getSubimage(left, top, right - left + 1, bottom - top + 1);
    }

    public static String getBackground(String name) throws URISyntaxException {
        URI mapdDIR = Objects.requireNonNull(RewardsDrawer.class.getClassLoader().getResource("maps")).toURI();
        File dir = new File(mapdDIR);
        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files)
            if (file.getName().matches("(?i).*" + name + ".*")) {
                return file.getAbsolutePath();
            }
        return "";
    }
    static class RewardRow{
        public String title;
        public List<String> imgUrls;

        RewardRow(String t, List<String> i){
            title = t;
            imgUrls = i;
        }
    }
}
