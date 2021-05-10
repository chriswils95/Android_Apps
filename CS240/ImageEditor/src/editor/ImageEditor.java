package editor;
import passoff.Pixel;

import java.util.*;
import java.io.*;
public class ImageEditor {




    public static void main(String args[]) throws FileNotFoundException {
        // args[0] is the source file
        // args[1] is the destination file

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);

        PrintWriter writer = new PrintWriter(outputFile);
        copyInputFile fileScanner = new copyInputFile(inputFile, outputFile);
        fileScanner.copy();
        if(args[2].equals("invert")){
            fileScanner.ImageObject.applyInvertRules();
            StringBuilder str
                    = new StringBuilder("");
            str.append(fileScanner.ppmConstant + "\n");
            str.append(fileScanner.width + "\n");
            str.append(fileScanner.height + "\n");
            str.append(fileScanner.max_size + "\n");
            // writer.println(fileScanner.ppmConstant);
            // writer.println(fileScanner.width);
            // writer.println(fileScanner.height);
            // writer.println(fileScanner.max_size);
           for(int k = 0; k < fileScanner.height;k++){
               for(int l = 0; l < fileScanner.width;l++){
                   str.append(fileScanner.ImageObject.Rgb[k][l].toString() + "\n");
                   //writer.println(fileScanner.ImageObject.Rgb[k][l].toString());
               }
           }
            writer.println(str.toString());
            writer.close();
        }
        else if(args[2].equals("emboss")){
            fileScanner.ImageObject.applyEmbossRules();
            /*System.out.println(fileScanner.ppmConstant);
            System.out.println(fileScanner.width);
            System.out.println(fileScanner.height);
            System.out.println(fileScanner.max_size);*/
            StringBuilder str
                    = new StringBuilder("");
            str.append(fileScanner.ppmConstant);
            str.append("\n");
            str.append(fileScanner.width);
            str.append("\n");
            str.append(fileScanner.height);
            str.append("\n");
            str.append(fileScanner.max_size + "\n");
           /* writer.println(fileScanner.ppmConstant);
            writer.println(fileScanner.width);
            writer.println(fileScanner.height);
            writer.println(fileScanner.max_size);*/
            for(int k = 0; k < fileScanner.height;k++){
                for(int l = 0; l < fileScanner.width;l++){
                   // System.out.println(fileScanner.ImageObject.Rgb[k][l].toString());
                    str.append(fileScanner.ImageObject.Rgb[k][l].toString() + "\n");
                    //writer.println(fileScanner.ImageObject.Rgb[k][l].toString());
                }
               // str.append(" ");
            }
            writer.println(str.toString());
            writer.close();
        }
        else if(args[2].equals("grayscale")){

            fileScanner.ImageObject.applyGrayScaleRules();
            StringBuilder str
                    = new StringBuilder("");
            str.append(fileScanner.ppmConstant);
            str.append("\n");
            str.append(fileScanner.width);
            str.append("\n");
            str.append(fileScanner.height);
            str.append("\n");
            str.append(fileScanner.max_size + "\n");
            // writer.println(fileScanner.ppmConstant);
            // writer.println(fileScanner.width);
            // writer.println(fileScanner.height);
            // writer.println(fileScanner.max_size);
            for(int k = 0; k < fileScanner.height;k++){
                for(int l = 0; l < fileScanner.width;l++){
                    str.append(fileScanner.ImageObject.Rgb[k][l].toString() + "\n");
                   // writer.println(fileScanner.ImageObject.Rgb[k][l].toString());
                }
                str.append(" ");
            }
            writer.println(str.toString());
            writer.close();
        }else if(args[2].equals("motionblur")){

            fileScanner.ImageObject.applyMotionBlueRules(Integer.parseInt(args[3]));
            StringBuilder str
                    = new StringBuilder("");
            str.append(fileScanner.ppmConstant);
            str.append("\n");
            str.append(fileScanner.width);
            str.append("\n");
            str.append(fileScanner.height);
            str.append("\n");
            str.append(fileScanner.max_size + "\n");
            // writer.println(fileScanner.ppmConstant);
            // writer.println(fileScanner.width);
            // writer.println(fileScanner.height);
            // writer.println(fileScanner.max_size);
            for(int k = 0; k < fileScanner.height;k++){
                for(int l = 0; l < fileScanner.width;l++){
                    str.append(fileScanner.ImageObject.Rgb[k][l].toString() + "\n");
                    //writer.println(fileScanner.ImageObject.Rgb[k][l].toString());
                }
            }
            System.out.println(str);
            writer.println(str.toString());
            writer.close();
        }

       }
    }
class copyInputFile {
    File inputFile;
    File outputFile;
    int width;
    int height;
    int max_size;
    String ppmConstant;
    int myRgb[];
    //myRgb = new int[height * width];
    int temprgb[][];
    Pixels temp[][];
    Images ImageObject = new Images();




    public copyInputFile(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void copy() throws FileNotFoundException {
        Scanner scanner = new Scanner(inputFile);
        scanner.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");
        PrintWriter writer = new PrintWriter(outputFile);
        ppmConstant = scanner.next();
        width = scanner.nextInt();
        height = scanner.nextInt();
        max_size = scanner.nextInt();
        ImageObject.height = height;
        ImageObject.width = width;
        ImageObject.max_size = max_size;


        myRgb = new int[height * width];
        temp = new Pixels[height][width];
        temprgb = new int[width][height];

        while (scanner.hasNext()) {
            //int line = scanner.nextInt();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    temp[i][j] = new Pixels(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
                    ImageObject.Rgb = temp;
                }
                System.out.println("\n");
            }
         }
          scanner.close();
    }
}
