package files.parsingJsonFiles;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;


/**
 * This class is pretty much an helper class to help parse json files in to the request class objects
 */

public class JsonArrayParser {

    /**
     * The femaleName classes is a sub class that helps me
     * parse json files into array of female names
     */
    class FemaleNames {

        /**
         * arrays of all female names
         */
        String[] data;

        /**
         * This functions returns a female name at a given index
         * @param index
         * @return
         */
        String getFemaleNameAt(int index){
            return data[index];
        }

        int size(){
            return data.length;
        }
    }

    /**
     * The MaleName classes is a sub class that helps me
     * parse json files into array of male names
     */
    class MaleNames {
        /**
         * arrays of all male names
         */
        String[] data;

        /**
         * This functions returns a male name at a given index
         * @param index
         * @return
         */
        String getMaleNameAt(int index){
            return data[index];
        }
        int size(){
            return data.length;
        }
    }

    /**
     * The lastName classes is a sub class that helps me
     * parse json files into array of last names
     */
    class LastNames {
        /**
         * arrays of last names
         */
        String[] data;


        /**
         * This functions returns a last name at a given index
         * @param index
         * @return
         */
        String getLastNameAt(int index){
            return data[index];
        }
        int size(){
            return data.length;
        }
    }


    /**
     * The location class is a sub class that helps me
     * parse json files into a location model with given informations
     */
    class Location {
        /**
         * latitude of an event
         */
        float latitude;
        /**
         * longitude of an event
         */
        float longitude;
        /**
         * country of an event
         */
        String country;
        /**
         * city of an event
         */
        String city;
    }


    /**
     * The sub class helps in parsing arrays of jsons location files
     * into arrays of locations
     */
    public class LocationData {
        Location[] data;


        /**
         * This returns the latitude
         * @param index
         * @return latitude
         */
        public float getLatitude(int index){
            return data[index].latitude;
        }

        /**
         * This returns the longitude
         * @param index
         * @return longitude
         */
        public float getLongitude(int index){
            return data[index].longitude;
        }

        /**
         * This returns the city at the given index
         * @param index
         * @return city
         */
        public String getCity(int index){
            return data[index].city;
        }

        /**
         * This returns the country at the given index
         * @param index
         * @return country
         */
        public String getCountry(int index){
            return data[index].country;
        }

        /**
         * returns the size of the data array
         * @return
         */
        public  int size(){
            return data.length;
        }



    }


    /**
     *This function returns the parse json array of female name
     * @return array of female name
     * @throws FileNotFoundException
     */
    public ArrayList<String> getArrayOfFemaleNames() throws FileNotFoundException {
        ArrayList<String> arraysOfFemaleNames = new ArrayList<String>();
        Reader femaleNamesReader = new FileReader("json/fnames.json");
        FemaleNames femaleNames = new Gson().fromJson(femaleNamesReader, FemaleNames.class);
        for(int i = 0; i < femaleNames.size(); i++){
            arraysOfFemaleNames.add(femaleNames.getFemaleNameAt(i));
        }

        return arraysOfFemaleNames;

    }

    /**
     *This fucntion returns the parse json array of male names
     * @return array of male names
     * @throws FileNotFoundException
     */
    public ArrayList<String> getArrayOfMaleNames() throws FileNotFoundException {
        ArrayList<String> arraysMaleNames = new ArrayList<String>();
        Reader maleNamesReader = new FileReader("json/mnames.json");
        MaleNames maleNames = new Gson().fromJson(maleNamesReader, MaleNames.class);
        for(int i = 0; i < maleNames.size(); i++){
            arraysMaleNames.add(maleNames.getMaleNameAt(i));
        }

        return arraysMaleNames;
    }

    /**
     *This function retuns the parse json array of last names
     * @return array of last names
     * @throws FileNotFoundException
     */
    public ArrayList<String> getArrayOfLastNames() throws FileNotFoundException {
        ArrayList<String> arrayOfLastNames = new ArrayList<String>();
        Reader lastNamesReader = new FileReader("json/snames.json");
        LastNames lastNames = new Gson().fromJson(lastNamesReader, LastNames.class);
        for(int i = 0; i < lastNames.size(); i++){
            arrayOfLastNames.add(lastNames.getLastNameAt(i));
        }

        return arrayOfLastNames;
    }


    /**
     *This function returns the parse json array of locations
     * @return array of locations
     * @throws FileNotFoundException
     */
    public LocationData getArrayOfLocations() throws FileNotFoundException {
        ArrayList<String> arrayOfLocations = new ArrayList<String>();
        Reader locationsJsonFileReader = new FileReader("json/locations.json");
        LocationData locations = new Gson().fromJson(locationsJsonFileReader, LocationData.class);
        return locations;
    }



}
