package nz.trialsBot.views.helpers;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RewardsDrawer {
        private static List<BufferedImage> loadImagesFromUrls(List<String> urls) throws IOException {
            List<BufferedImage> res = new ArrayList<>();
            for(String url : urls){
                res.add(ImageIO.read(new URL(url)));
            }
            return res;
        }

        private static int getSectionWidth(List<BufferedImage> imgs, int padding){
            int w = 0;
            for(BufferedImage img : imgs){
                w=w+padding+img.getWidth()+padding;
            }
            return w;
        }

        private static BufferedImage resizeImage(BufferedImage img, int w, int h, int imageType){
            BufferedImage result = new BufferedImage(w,h,imageType);
            float currentH = img.getHeight();
            float currentW = img.getWidth();

            float ratiohw = currentH/currentW;
            float ratiowh = currentW/currentH;

            int endH;
            int endW;
            int endX = 0;
            int endY = 0;

            if(w*(w*ratiohw) < h*(h*ratiowh)){
                endH = (int) (w*ratiohw);
                endW = w;
                endY = (h-endH)/2;
            }else{
                endH = h;
                endW = (int) (h*ratiowh);
                endX = (w-endW)/2;
            }
            result.getGraphics().drawImage(img, endX, endY, endW, endH, null);

            return trimImage(result);
        }

        public static void drawRow(List<BufferedImage> images, Graphics g, int x, int y){
            for(BufferedImage img : images){
                g.drawImage(img, x+8,y,null);
                x=x+img.getWidth()+8;
            }
        }

        public static void drawText(String text, int x, int y, Color color, Graphics g){
            g.setColor(color);
            g.setFont(new Font("Neue Haas Grotesk Text Pro", Font.BOLD, 40));
            g.drawString(text, x, y);
        }

        public static void drawRectangle(int x, int y, int w, int h,Color fillColor, Color shaddowColor, Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            g2d.setColor(shaddowColor);
            g2d.fillRoundRect(x+9,y+6,w,h,70,70);

            g2d.setColor(fillColor);
            g2d.fillRoundRect(x,y,w,h,70,70);
        }

        public static BufferedImage drawRewards(List<String> three, List<String> five, List<String> seven, List<String> flawless, String map) throws IOException {
            map = getBackground(map);
            BufferedImage canvas = new BufferedImage(1000,1000,BufferedImage.TYPE_INT_ARGB);
            Graphics g = canvas.getGraphics();

            List<BufferedImage> t = loadImagesFromUrls(three);
            List<BufferedImage> f = loadImagesFromUrls(five);
            List<BufferedImage> s = loadImagesFromUrls(seven);
            List<BufferedImage> fl = loadImagesFromUrls(flawless);

            //Draw Reward images
            int padding = 8;
            int nextY = padding;
            drawRow(t, g,padding,nextY);
            drawText("3 Wins", getSectionWidth(t, padding)+padding, (t.get(0).getHeight()/2)+nextY, Color.white, g);

            nextY = nextY+t.get(0).getHeight()+padding;
            drawRow(f, g,padding,nextY+padding);
            drawText("5 Wins", getSectionWidth(f, padding)+padding, (f.get(0).getHeight()/2)+nextY+padding, Color.white, g);

            nextY = nextY+f.get(0).getHeight()+padding;
            drawRow(s, g,padding,nextY+padding);
            drawText("7 Wins", getSectionWidth(s, padding)+padding, (s.get(0).getHeight()/2)+nextY+padding, Color.white, g);

            nextY = nextY+s.get(0).getHeight()+padding;
            drawRow(fl, g,padding,nextY+padding);
            drawText("Flawless", getSectionWidth(fl, padding)+padding, (fl.get(0).getHeight()/2)+nextY+padding, Color.white, g);


            BufferedImage croppedRewards = trimImage(canvas);

            BufferedImage rewards = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);

            drawRectangle(0,0,croppedRewards.getWidth(), croppedRewards.getHeight(), new Color(20, 25, 30),new Color(11, 12, 16, 190), rewards.getGraphics());
            rewards.getGraphics().drawImage(croppedRewards,padding*3,padding*3,null);


            canvas = ImageIO.read(new File(map));
            canvas.getGraphics().drawImage(resizeImage(trimImage(rewards), 900,900, BufferedImage.TYPE_INT_ARGB), 10, 10, null);
            rewards = resizeImage(canvas, 1920, 1080, BufferedImage.TYPE_INT_RGB);
            return rewards;
            //ImageIO.write(rewards, "PNG", new File(outfile));
        }

        private static BufferedImage trimImage(BufferedImage image) {
            int width = image.getWidth();
            int height = image.getHeight();
            int top = height / 2;
            int bottom = top;
            int left = width / 2 ;
            int right = left;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (image.getRGB(x, y) != 0){
                        top    = Math.min(top, y);
                        bottom = Math.max(bottom, y);
                        left   = Math.min(left, x);
                        right  = Math.max(right, x);
                    }
                }
            }
            return image.getSubimage(left, top, right - left + 1, bottom - top + 1);
        }

        public static String getBackground(String name){
            File dir = new File(".\\maps");
            File[] files = dir.listFiles();
            for(File file : files) {
                if(file.getName().matches("(?i).*"+name+".*")){
                    return file.getAbsolutePath();
                }
            }
            return "";
        }

//        public static void main(String[] args) throws IOException {
//            List<String> three = new ArrayList<>(Arrays.asList(
//                    "https://www.bungie.net/common/destiny2_content/icons/6de1b41f6f6c94baaad45443f1634678.jpg"
//            ));
//            List<String> five = new ArrayList<>(Arrays.asList(
//                    "https://www.bungie.net/common/destiny2_content/icons/44ea5f89ff4f29e288a7bfda1e311914.jpg",
//                    "https://www.bungie.net/common/destiny2_content/icons/fe8d00be76907d16378f791741dadeaa.jpg",
//                    "https://www.bungie.net/common/destiny2_content/icons/2ebfcc66870875b86697a54c7b156c57.jpg"
//            ));
//            List<String> seven = new ArrayList<>(Arrays.asList(
//                    "https://www.bungie.net/common/destiny2_content/icons/542a03d9814ee8e3ff4ada45b3fbc33d.jpg"
//            ));
//            List<String> flawless = new ArrayList<>(Arrays.asList(
//                    "https://www.bungie.net/common/destiny2_content/icons/51bb525fdbfe40883dc8372e969b79b0.jpg"
//            ));
//
//            ImageIO.write(drawRewards(three,five,seven,flawless,"cauldron"), "png", new File("D:\\Users\\Sam\\Desktop\\tmp\\lgg\\out2.png"));
//        }

    }
