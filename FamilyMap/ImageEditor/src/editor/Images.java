package editor;

import passoff.Pixel;

public class Images {
    int width;
    int height;
    int max_size;
    int totalRgbPixels;
    Pixels Rgb[][];

    public Images(){
       totalRgbPixels = 3;
    }


    public void applyInvertRules(){
        for(int i = 0; i < height;i++){
            for(int j = 0; j < width; j++){
               Rgb[i][j].setRed(max_size - Rgb[i][j].getRed());
                Rgb[i][j].setGreen(max_size - Rgb[i][j].getGreen());
                Rgb[i][j].setBlue(max_size - Rgb[i][j].getBlue());
            }
        }
    }

    public void applyGrayScaleRules(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int sumsOfPixels = 0;
                 sumsOfPixels = Rgb[i][j].getRed() + Rgb[i][j].getBlue() + Rgb[i][j].getGreen();
                 int average = sumsOfPixels / totalRgbPixels;
                Rgb[i][j].setRed(average);
                Rgb[i][j].setGreen(average);
                Rgb[i][j].setBlue(average);
            }
        }

   }


   public boolean checkUpandLeft(int depth, int width){

          if(((depth - 1) < 0|| (width - 1) < 0)){
              return true;
          }

          return false;
   }

   public int checkHorizontalBlurLength(int blurLength, int width, int checkBounds){
         int counter = 0;
         int index = 0;
        for(int i = checkBounds; i < width; i++) {
            if(index == blurLength){
                break;
            }
                index++;
           counter ++;
        }

        if(counter < blurLength){
            return (width - checkBounds);
        }
        return -1;
   }

    public void applyMotionBlueRules(int blurLength){
        int sumsOfBlurRedPixels = 0;
        int sumsOfBlurBluePixels = 0;
        int sumsOfBlurGreenPixels = 0;

        int averageRedPixels = 0;
        int averageGreenPixels = 0;
        int averageBluePixels = 0;
       // checkHorizontalBlurLength(3, 5, 2);
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                //int average = sumsOfPixels / totalRgbPixels;
                if(checkHorizontalBlurLength(blurLength, width, j) == -1){
                    for(int k = 0; k < blurLength; k++){
                       sumsOfBlurRedPixels += Rgb[i][j + k].getRed();
                       sumsOfBlurGreenPixels += Rgb[i][j + k].getGreen();
                       sumsOfBlurBluePixels += Rgb[i][j + k].getBlue();
                    }

                    averageRedPixels = sumsOfBlurRedPixels / blurLength;
                    averageGreenPixels = sumsOfBlurGreenPixels / blurLength;
                    averageBluePixels = sumsOfBlurBluePixels / blurLength;
                    Rgb[i][j].setRed(averageRedPixels);
                    Rgb[i][j].setGreen(averageGreenPixels);
                    Rgb[i][j].setBlue(averageBluePixels);

                    sumsOfBlurRedPixels = 0;
                    sumsOfBlurBluePixels = 0;
                    sumsOfBlurGreenPixels = 0;

                }else{
                    int newIndex = 0;
                    int newArrayIndex = checkHorizontalBlurLength(blurLength, width, j);
                    for(int k = 0; k < (newArrayIndex); k++){
                            sumsOfBlurRedPixels += Rgb[i][j + newIndex].getRed();
                            sumsOfBlurGreenPixels += Rgb[i][j+ newIndex].getGreen();
                            sumsOfBlurBluePixels += Rgb[i][j + newIndex].getBlue();
                            newIndex++;
                        }
                    averageRedPixels = sumsOfBlurRedPixels / newArrayIndex;
                    averageGreenPixels = sumsOfBlurGreenPixels / newArrayIndex;
                    averageBluePixels = sumsOfBlurBluePixels / newArrayIndex;
                    Rgb[i][j].setRed(averageRedPixels);
                    Rgb[i][j].setGreen(averageGreenPixels);
                    Rgb[i][j].setBlue(averageBluePixels);

                    sumsOfBlurRedPixels = 0;
                    sumsOfBlurBluePixels = 0;
                    sumsOfBlurGreenPixels = 0;

                }
            }
        }
    }

   public  void applyEmbossRules(){
        int outOfBoundsMaxPixels = 128;
        int maxDifference = 0;
        int redPixelsDifference;
        int bluePixelsDifference;
        int greenPixelsDifference;
        int newPixelsValue;

       for(int i = height - 1; i > -1; i--){
            for(int j = width - 1; j > -1; j--) {

                if((i == 1) && (j == 614)){
                    System.out.println("nd");
                }
                if (checkUpandLeft(i, j)) {
                    Rgb[i][j].setRed(outOfBoundsMaxPixels);
                    Rgb[i][j].setGreen(outOfBoundsMaxPixels);
                    Rgb[i][j].setBlue(outOfBoundsMaxPixels);
                }
                else {
                    int maxValue;
                    redPixelsDifference = Rgb[i][j].getRed() - Rgb[i - 1][j - 1].getRed();
                    bluePixelsDifference = Rgb[i][j].getBlue() - Rgb[i - 1][j - 1].getBlue();
                    greenPixelsDifference = Rgb[i][j].getGreen() - Rgb[i - 1][j - 1].getGreen();


                    int tempRedPixelsDifference = Math.abs(redPixelsDifference);
                    int tempBluePixelsDifference = Math.abs(bluePixelsDifference);
                    int tempGreenPixelsDifference = Math.abs(greenPixelsDifference);
                    maxValue = Math.max(tempRedPixelsDifference, tempBluePixelsDifference);
                    maxValue = Math.max(maxValue, tempGreenPixelsDifference);
                    if (maxValue == tempRedPixelsDifference) {
                       /* if(maxValue == tempBluePixelsDifference){
                            if((bluePixelsDifference < 0) && (redPixelsDifference > 0)){
                                if(maxValue == tempGreenPixelsDifference){
                                    maxDifference = redPixelsDifference;
                                }else {
                                    maxDifference = bluePixelsDifference;
                                }
                            }
                            else {
                                maxDifference = redPixelsDifference;
                            }*/
                        /*  }
                        else if(maxValue == tempGreenPixelsDifference){
                            if((tempGreenPixelsDifference < 0) && (redPixelsDifference > 0)){
                                maxDifference = bluePixelsDifference;
                            }
                            else {
                                maxDifference = redPixelsDifference;
                            }
                           }
                        else {
                            maxDifference = redPixelsDifference;
                        }*/
                        maxDifference = redPixelsDifference;
                    } else if (maxValue == tempBluePixelsDifference) {
                        if(maxValue == tempGreenPixelsDifference) {
                           maxDifference = greenPixelsDifference;
                        }
                        else {
                            maxDifference = bluePixelsDifference;
                        }
                    }
                    else {
                        maxDifference = greenPixelsDifference;
                    }

                    newPixelsValue = outOfBoundsMaxPixels + maxDifference;
                    if (newPixelsValue < 0) {
                        newPixelsValue = 0;
                    }

                    if (newPixelsValue > max_size) {
                        newPixelsValue = max_size;
                    }
                    Rgb[i][j].setRed(newPixelsValue);
                    Rgb[i][j].setGreen(newPixelsValue);
                    Rgb[i][j].setBlue(newPixelsValue);
                  }

                }

            }
       }
   }



